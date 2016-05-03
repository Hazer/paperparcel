package nz.bradcampbell.paperparcel;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.primitives.Ints;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import javax.lang.model.element.Modifier;
import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;
import nz.bradcampbell.paperparcel.model.Adapter;
import nz.bradcampbell.paperparcel.model.Clazz;
import nz.bradcampbell.paperparcel.model.Field;
import nz.bradcampbell.paperparcel.model.Model;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static nz.bradcampbell.paperparcel.PaperParcelProcessor.DATA_VARIABLE_NAME;
import static nz.bradcampbell.paperparcel.utils.StringUtils.uncapitalizeFirstCharacter;

public class WrapperGenerator {
  private static final ClassName PARCEL = ClassName.get("android.os", "Parcel");
  private static final ClassName PARCELABLE_WRAPPER = ClassName.get(
      ParcelableWrapper.class);

  public JavaFile generateParcelableWrapper(Model model) throws IOException {
    TypeSpec.Builder wrapperBuilder =
        TypeSpec.classBuilder(model.getWrapperClassName().simpleName())
            .addModifiers(PUBLIC, FINAL)
            .addSuperinterface(
                ParameterizedTypeName.get(PARCELABLE_WRAPPER, model.getClassName()));

    FieldSpec creator = generateCreator(model.getClassName(), model.getWrapperClassName(),
        model.isSingleton(), model.getFields());

    wrapperBuilder.addField(creator)
        .addField(generateContentsField(model.getClassName()))
        .addMethod(generateContentsGetter(model.getClassName()))
        .addMethod(generateContentsConstructor(model.getClassName()))
        .addMethod(generateDescribeContents())
        .addMethod(generateWriteToParcel(model.getFields()));

    // Build the java file
    return JavaFile.builder(model.getClassPackage(), wrapperBuilder.build()).build();
  }

  private FieldSpec generateCreator(ClassName className, ClassName wrapperClassName,
      boolean isSingleton, List<Field> fields) {

    // TODO: duplicate names

    ClassName creator = ClassName.get("android.os", "Parcelable", "Creator");
    TypeName creatorOfClass = ParameterizedTypeName.get(creator, wrapperClassName);

    ParameterSpec in = ParameterSpec.builder(PARCEL, "in").build();

    final CodeBlock.Builder block = CodeBlock.builder()
        .beginControlFlow("new $T()", ParameterizedTypeName.get(creator, wrapperClassName))
        .beginControlFlow("@$T public $T createFromParcel($T $N)", Override.class, wrapperClassName,
            PARCEL, in);

    if (!isSingleton) {
      Map<Object, String> dependencyNameMap = initializeTypeAdaptersForFields(block, fields);

      for (Field field : fields) {
        String adapterName = dependencyNameMap.get(field.getAdapter());
        readField(block, adapterName, field.getName(), field.getTypeName(), in);
      }

      final String fieldName = PaperParcelProcessor.DATA_VARIABLE_NAME;

      List<Field> constructorArgs =
          FluentIterable.from(fields).filter(new Predicate<Field>() {
            @Override public boolean apply(Field input) {
              return input.getConstructorPosition() >= 0;
            }
          }).toSortedList(new Comparator<Field>() {
            @Override public int compare(Field left, Field right) {
              return Ints.compare(left.getConstructorPosition(), right.getConstructorPosition());
            }
          });

      // Construct data class
      constructType(constructorArgs, className, fieldName, block);

      // Write fields to data class directly
      FluentIterable.from(fields).filter(new Predicate<Field>() {
        @Override public boolean apply(Field input) {
          return input.getConstructorPosition() < 0;
        }
      }).filter(new Predicate<Field>() {
        @Override public boolean apply(Field input) {
          return input.isVisible();
        }
      }).forEach(new Consumer<Field>() {
        @Override public void accept(Field field) {
          block.addStatement("$N.$N = $N", fieldName, field.getName(), field.getName());
        }
      });

      // Write remaining fields via setters
      FluentIterable.from(fields).filter(new Predicate<Field>() {
        @Override public boolean apply(Field input) {
          return input.getConstructorPosition() < 0;
        }
      }).filter(new Predicate<Field>() {
        @Override public boolean apply(Field input) {
          return !input.isVisible();
        }
      }).forEach(new Consumer<Field>() {
        @Override public void accept(Field field) {
          block.addStatement("$N.$N($N)", fieldName, field.getSetterMethodName(), field.getName());
        }
      });

      block.addStatement("return new $T($N)", wrapperClassName, fieldName);
    } else {
      block.addStatement("return new $T($T.INSTANCE)", wrapperClassName, className);
    }

    block.endControlFlow()
        .beginControlFlow("@$T public $T[] newArray($T size)", Override.class, wrapperClassName,
            int.class)
        .addStatement("return new $T[size]", wrapperClassName)
        .endControlFlow()
        .unindent()
        .add("}");

    return FieldSpec.builder(creatorOfClass, "CREATOR", Modifier.PUBLIC, Modifier.FINAL,
        Modifier.STATIC).initializer(block.build()).build();
  }

  private FieldSpec generateContentsField(TypeName className) {
    return FieldSpec.builder(className, DATA_VARIABLE_NAME, PRIVATE, FINAL).build();
  }

  private MethodSpec generateContentsGetter(TypeName className) {
    return MethodSpec.methodBuilder("get")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .returns(className)
        .addStatement("return this.$N", DATA_VARIABLE_NAME)
        .build();
  }

  private MethodSpec generateContentsConstructor(TypeName className) {
    return MethodSpec.constructorBuilder()
        .addModifiers(PUBLIC)
        .addParameter(className, DATA_VARIABLE_NAME)
        .addStatement("this.$N = $N", DATA_VARIABLE_NAME, DATA_VARIABLE_NAME)
        .build();
  }

  private MethodSpec generateDescribeContents() {
    return MethodSpec.methodBuilder("describeContents")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .returns(int.class)
        .addStatement("return 0")
        .build();
  }

  private MethodSpec generateWriteToParcel(List<Field> fields) {
    ParameterSpec dest = ParameterSpec.builder(PARCEL, "dest").build();
    ParameterSpec flags = ParameterSpec.builder(int.class, "flags").build();

    MethodSpec.Builder builder = MethodSpec.methodBuilder("writeToParcel")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .addParameter(dest)
        .addParameter(flags);

    CodeBlock.Builder block = CodeBlock.builder();

    Map<Object, String> dependencyNameMap = initializeTypeAdaptersForFields(block, fields);

    for (Field field : fields) {
      String accessorStrategy = field.isVisible() ?
          field.getName() : field.getGetterMethodName() + "()";
      CodeBlock value = CodeBlock.of("$N.$N", "this." + DATA_VARIABLE_NAME, accessorStrategy);
      String adapterName = dependencyNameMap.get(field.getAdapter());
      writeField(block, adapterName, value, field.getTypeName(), dest, flags);
    }

    return builder.addCode(block.build()).build();
  }

  private void readField(CodeBlock.Builder block, String adapterName, String name,
      TypeName typeName, ParameterSpec in) {
    if (typeName.isPrimitive()) {
      readPrimitiveType(block, typeName, name, in);
    } else {
      block.addStatement("$T $N = $N.readFromParcel($N)", typeName, name, adapterName, in);
    }
  }

  private void writeField(CodeBlock.Builder block, String adapterName, CodeBlock value,
      TypeName typeName, ParameterSpec dest, ParameterSpec flags) {
    if (typeName.isPrimitive()) {
      writePrimitiveType(block, value, typeName, dest);
    } else {
      block.addStatement("$N.writeToParcel($L, $N, $N)", adapterName, value, dest, flags);
    }
  }

  private Map<Object, String> initializeTypeAdaptersForFields(CodeBlock.Builder block,
      List<Field> fields) {
    Map<Object, String> dependencyNameMap = new LinkedHashMap<>();
    Set<Adapter> scopedAdapters = new LinkedHashSet<>();
    for (Field field : fields) {
      Adapter adapter = field.getAdapter();
      if (adapter != null) {
        initializeTypeAdapter(block, adapter, scopedAdapters, dependencyNameMap);
      }
    }
    return dependencyNameMap;
  }

  private void initializeTypeAdapter(CodeBlock.Builder block, Adapter adapter,
      Set<Adapter> scopedAdapters, Map<Object, String> dependencyNameMap) {

    if (!scopedAdapters.contains(adapter)) {
      TypeName adapterTypeName = adapter.getTypeName();
      String name = uncapitalizeFirstCharacter(getNameFromTypeName(adapter.getTypeName()));

      if (adapter.isSingleton()) {
        block.addStatement("$1T $2N = $1T.INSTANCE", adapterTypeName, name);
      } else {
        List<Object> dependencies = adapter.getDependencies();
        for (Object dependency : dependencies) {
          if (dependency instanceof Adapter) {
            initializeTypeAdapter(block, (Adapter) dependency, scopedAdapters, dependencyNameMap);
          } else if (dependency instanceof Clazz) {
            Clazz clazz = (Clazz) dependency;
            String clazzName = uncapitalizeFirstCharacter(getNameFromTypeName(clazz.getTypeName()));
            if (clazz.getTypeArgument() instanceof ParameterizedTypeName) {
              ParameterizedTypeName parameterizedTypeArgument =
                  (ParameterizedTypeName) clazz.getTypeArgument();
              block.addStatement("@$1T(\"unchecked\") $2T $3N = ($2T)($4T)$5T.class",
                  SuppressWarnings.class, clazz.getTypeName(), clazzName, Object.class,
                  parameterizedTypeArgument.rawType);
            } else {
              block.addStatement("$1T $2N = $3T.class", clazz.getTypeName(), clazzName,
                  clazz.getTypeArgument());
            }
            dependencyNameMap.put(clazz, clazzName);
          } else {
            throw new AssertionError();
          }
        }
        block.add("$T $N = new $T(", adapterTypeName, name, adapterTypeName);
        for (int i = 0; i < dependencies.size(); i++) {
          Object dependency = dependencies.get(i);
          block.add(dependencyNameMap.get(dependency));
          if (i != dependencies.size() - 1) {
            block.add(", ");
          }
        }
        block.addStatement(")");
      }

      scopedAdapters.add(adapter);
      dependencyNameMap.put(adapter, name);
    }
  }

  private String getNameFromTypeName(TypeName typeName) {
    String adapterName = null;
    if (typeName instanceof WildcardTypeName) {
      WildcardTypeName wildcardTypeName = (WildcardTypeName) typeName;
      String upperBoundsPart = "";
      String lowerBoundsPart = "";
      for (TypeName upperBound : wildcardTypeName.upperBounds) {
        upperBoundsPart += getNameFromTypeName(upperBound);
      }
      for (TypeName lowerBound : wildcardTypeName.lowerBounds) {
        lowerBoundsPart += getNameFromTypeName(lowerBound);
      }
      adapterName = upperBoundsPart + lowerBoundsPart;
    }
    if (typeName instanceof ArrayTypeName) {
      ArrayTypeName arrayTypeName = (ArrayTypeName) typeName;
      adapterName = getNameFromTypeName(arrayTypeName.componentType) + "Array";
    }
    if (typeName instanceof ParameterizedTypeName) {
      ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) typeName;
      String paramPart = "";
      for (TypeName param : parameterizedTypeName.typeArguments) {
        paramPart += getNameFromTypeName(param);
      }
      adapterName = paramPart + parameterizedTypeName.rawType.simpleName();
    }
    if (typeName instanceof ClassName) {
      ClassName className = (ClassName) typeName;
      adapterName = className.simpleName();
    }
    if (adapterName == null) {
      throw new AssertionError("Unknown type " + typeName.getClass());
    }
    return adapterName;
  }

  private void readPrimitiveType(CodeBlock.Builder block, TypeName typeName, String name,
      ParameterSpec in) {
    if (TypeName.INT.equals(typeName)) {
      block.addStatement("$T $N = $N.readInt()", typeName, name, in);
    } else if (TypeName.BOOLEAN.equals(typeName)) {
      block.addStatement("$T $N = $N.readInt() == 1", typeName, name, in);
    } else if (TypeName.BYTE.equals(typeName)) {
      block.addStatement("$T $N = $N.readByte()", typeName, name, in);
    } else if (TypeName.CHAR.equals(typeName)) {
      block.addStatement("$T $N = (char) $N.readInt()", typeName, name, in);
    } else if (TypeName.DOUBLE.equals(typeName)) {
      block.addStatement("$T $N = $N.readDouble()", typeName, name, in);
    } else if (TypeName.FLOAT.equals(typeName)) {
      block.addStatement("$T $N = $N.readFloat()", typeName, name, in);
    } else if (TypeName.LONG.equals(typeName)) {
      block.addStatement("$T $N = $N.readLong()", typeName, name, in);
    } else if (TypeName.SHORT.equals(typeName)) {
      block.addStatement("$T $N = (short) $N.readInt()", typeName, name, in);
    } else {
      throw new AssertionError("Unknown primitive type " + typeName);
    }
  }

  private void writePrimitiveType(CodeBlock.Builder block, CodeBlock value, TypeName typeName,
      ParameterSpec dest) {
    if (TypeName.INT.equals(typeName)) {
      block.addStatement("$N.writeInt($L)", dest, value);
    } else if (TypeName.BOOLEAN.equals(typeName)) {
      block.addStatement("$N.writeInt($L ? 1 : 0)", dest, value);
    } else if (TypeName.BYTE.equals(typeName)) {
      block.addStatement("$N.writeByte($L)", dest, value);
    } else if (TypeName.CHAR.equals(typeName)) {
      block.addStatement("$N.writeInt($L)", dest, value);
    } else if (TypeName.DOUBLE.equals(typeName)) {
      block.addStatement("$N.writeDouble($L)", dest, value);
    } else if (TypeName.FLOAT.equals(typeName)) {
      block.addStatement("$N.writeFloat($L)", dest, value);
    } else if (TypeName.LONG.equals(typeName)) {
      block.addStatement("$N.writeLong($L)", dest, value);
    } else if (TypeName.SHORT.equals(typeName)) {
      block.addStatement("$N.writeInt($L)", dest, value);
    } else {
      throw new AssertionError("Unknown primitive type " + typeName);
    }
  }

  private void constructType(List<Field> args, TypeName typeName, String fieldName,
      CodeBlock.Builder block) {
    String initializer = "$1T $2N = new $1T(";
    int paramsOffset = 2;
    int numConstructorArgs = args.size();
    Object[] params = new Object[numConstructorArgs + paramsOffset];
    params[0] = typeName;
    params[1] = fieldName;
    for (int i = 0; i < numConstructorArgs; i++) {
      Field field = args.get(i);
      params[i + paramsOffset] = field.getName();
      initializer += "$" + (i + paramsOffset + 1) + "N";
      if (i != args.size() - 1) {
        initializer += ", ";
      }
    }
    initializer += ")";
    block.addStatement(initializer, params);
  }
}

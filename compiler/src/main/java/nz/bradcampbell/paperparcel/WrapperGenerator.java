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
import nz.bradcampbell.paperparcel.model.AdapterInfo;
import nz.bradcampbell.paperparcel.model.ClassInfo;
import nz.bradcampbell.paperparcel.model.FieldInfo;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PRIVATE;
import static javax.lang.model.element.Modifier.PUBLIC;
import static nz.bradcampbell.paperparcel.PaperParcelProcessor.DATA_VARIABLE_NAME;
import static nz.bradcampbell.paperparcel.utils.StringUtils.uncapitalizeFirstCharacter;

public class WrapperGenerator {
  private static final ClassName PARCEL = ClassName.get("android.os", "Parcel");
  private static final ClassName PARCELABLE_WRAPPER = ClassName.get(
      ParcelableWrapper.class);

  public JavaFile generateParcelableWrapper(ClassInfo classInfo) throws IOException {
    TypeSpec.Builder wrapperBuilder =
        TypeSpec.classBuilder(classInfo.getWrapperClassName().simpleName())
            .addModifiers(PUBLIC, FINAL)
            .addSuperinterface(
                ParameterizedTypeName.get(PARCELABLE_WRAPPER, classInfo.getClassName()));

    FieldSpec creator = generateCreator(classInfo.getClassName(), classInfo.getWrapperClassName(),
        classInfo.isSingleton(), classInfo.getFields());

    wrapperBuilder.addField(creator)
        .addField(generateContentsField(classInfo.getClassName()))
        .addMethod(generateContentsGetter(classInfo.getClassName()))
        .addMethod(generateContentsConstructor(classInfo.getClassName()))
        .addMethod(generateDescribeContents())
        .addMethod(generateWriteToParcel(classInfo.getFields()));

    // Build the java file
    return JavaFile.builder(classInfo.getClassPackage(), wrapperBuilder.build()).build();
  }

  private FieldSpec generateCreator(ClassName className, ClassName wrapperClassName,
      boolean isSingleton, List<FieldInfo> fields) {

    // TODO: duplicate names

    ClassName creator = ClassName.get("android.os", "Parcelable", "Creator");
    TypeName creatorOfClass = ParameterizedTypeName.get(creator, wrapperClassName);

    ParameterSpec in = ParameterSpec.builder(PARCEL, "in").build();

    final CodeBlock.Builder block = CodeBlock.builder()
        .beginControlFlow("new $T()", ParameterizedTypeName.get(creator, wrapperClassName))
        .beginControlFlow("@$T public $T createFromParcel($T $N)", Override.class, wrapperClassName,
            PARCEL, in);

    if (!isSingleton) {
      Map<AdapterInfo, String> adapterNameMap = initializeTypeAdaptersForFields(block, fields);

      for (FieldInfo field : fields) {
        String adapterName = adapterNameMap.get(field.getAdapterInfo());
        readField(block, adapterName, field.getName(), field.getTypeName(), in);
      }

      final String fieldName = PaperParcelProcessor.DATA_VARIABLE_NAME;

      List<FieldInfo> constructorArgs =
          FluentIterable.from(fields).filter(new Predicate<FieldInfo>() {
            @Override public boolean apply(FieldInfo input) {
              return input.getConstructorPosition() >= 0;
            }
          }).toSortedList(new Comparator<FieldInfo>() {
            @Override public int compare(FieldInfo left, FieldInfo right) {
              return Ints.compare(left.getConstructorPosition(), right.getConstructorPosition());
            }
          });

      // Construct data class
      constructType(constructorArgs, className, fieldName, block);

      // Write fields to data class directly
      FluentIterable.from(fields).filter(new Predicate<FieldInfo>() {
        @Override public boolean apply(FieldInfo input) {
          return input.getConstructorPosition() < 0;
        }
      }).filter(new Predicate<FieldInfo>() {
        @Override public boolean apply(FieldInfo input) {
          return input.isVisible();
        }
      }).forEach(new Consumer<FieldInfo>() {
        @Override public void accept(FieldInfo field) {
          block.addStatement("$N.$N = $N", fieldName, field.getName(), field.getName());
        }
      });

      // Write remaining fields via setters
      FluentIterable.from(fields).filter(new Predicate<FieldInfo>() {
        @Override public boolean apply(FieldInfo input) {
          return input.getConstructorPosition() < 0;
        }
      }).filter(new Predicate<FieldInfo>() {
        @Override public boolean apply(FieldInfo input) {
          return !input.isVisible();
        }
      }).forEach(new Consumer<FieldInfo>() {
        @Override public void accept(FieldInfo field) {
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

  private MethodSpec generateWriteToParcel(List<FieldInfo> fields) {
    ParameterSpec dest = ParameterSpec.builder(PARCEL, "dest").build();
    ParameterSpec flags = ParameterSpec.builder(int.class, "flags").build();

    MethodSpec.Builder builder = MethodSpec.methodBuilder("writeToParcel")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .addParameter(dest)
        .addParameter(flags);

    CodeBlock.Builder block = CodeBlock.builder();

    Map<AdapterInfo, String> adapterNameMap = initializeTypeAdaptersForFields(block, fields);

    for (FieldInfo field : fields) {
      String accessorStrategy = field.isVisible() ?
          field.getName() : field.getGetterMethodName() + "()";
      CodeBlock value = CodeBlock.of("$N.$N", "this." + DATA_VARIABLE_NAME, accessorStrategy);
      String adapterName = adapterNameMap.get(field.getAdapterInfo());
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

  private Map<AdapterInfo, String> initializeTypeAdaptersForFields(CodeBlock.Builder block,
      List<FieldInfo> fields) {
    Map<AdapterInfo, String> adapterNameMap = new LinkedHashMap<>();
    Set<AdapterInfo> scopedAdapters = new LinkedHashSet<>();
    for (FieldInfo field : fields) {
      AdapterInfo adapterInfo = field.getAdapterInfo();
      if (adapterInfo != null) {
        initializeTypeAdapter(block, adapterInfo, scopedAdapters, adapterNameMap);
      }
    }
    return adapterNameMap;
  }

  private void initializeTypeAdapter(CodeBlock.Builder block, AdapterInfo adapterInfo,
      Set<AdapterInfo> scopedAdapters, Map<AdapterInfo, String> adapterNameMap) {

    if (!scopedAdapters.contains(adapterInfo)) {
      TypeName adapterTypeName = adapterInfo.getTypeName();
      String name = uncapitalizeFirstCharacter(getTypeAdapterName(adapterInfo.getTypeName()));

      if (adapterInfo.isSingleton()) {
        block.addStatement("$1T $2N = $1T.INSTANCE", adapterTypeName, name);
      } else {
        List<AdapterInfo> dependencies = adapterInfo.getDependencies();
        for (AdapterInfo dependency : dependencies) {
          initializeTypeAdapter(block, dependency, scopedAdapters, adapterNameMap);
        }
        block.add("$T $N = new $T(", adapterTypeName, name, adapterTypeName);
        for (int i = 0; i < dependencies.size(); i++) {
          AdapterInfo dependency = dependencies.get(i);
          block.add(adapterNameMap.get(dependency));
          if (i != dependencies.size() - 1) {
            block.add(", ");
          }
        }
        block.addStatement(")");
      }

      scopedAdapters.add(adapterInfo);
      adapterNameMap.put(adapterInfo, name);
    }
  }

  private String getTypeAdapterName(TypeName adapterTypeName) {
    String adapterName = null;
    if (adapterTypeName instanceof WildcardTypeName) {
      WildcardTypeName wildcardTypeName = (WildcardTypeName) adapterTypeName;
      String upperBoundsPart = "";
      String lowerBoundsPart = "";
      for (TypeName upperBound : wildcardTypeName.upperBounds) {
        upperBoundsPart += getTypeAdapterName(upperBound);
      }
      for (TypeName lowerBound : wildcardTypeName.lowerBounds) {
        lowerBoundsPart += getTypeAdapterName(lowerBound);
      }
      adapterName = upperBoundsPart + lowerBoundsPart;
    }
    if (adapterTypeName instanceof ArrayTypeName) {
      ArrayTypeName arrayTypeName = (ArrayTypeName) adapterTypeName;
      adapterName = getTypeAdapterName(arrayTypeName.componentType) + "Array";
    }
    if (adapterTypeName instanceof ParameterizedTypeName) {
      ParameterizedTypeName parameterizedTypeName = (ParameterizedTypeName) adapterTypeName;
      String paramPart = "";
      for (TypeName param : parameterizedTypeName.typeArguments) {
        paramPart += getTypeAdapterName(param);
      }
      adapterName = paramPart + parameterizedTypeName.rawType.simpleName();
    }
    if (adapterTypeName instanceof ClassName) {
      ClassName className = (ClassName) adapterTypeName;
      adapterName = className.simpleName();
    }
    if (adapterName == null) {
      throw new AssertionError("Unknown type " + adapterTypeName.getClass());
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

  private void constructType(List<FieldInfo> args, TypeName typeName, String fieldName,
      CodeBlock.Builder block) {
    String initializer = "$1T $2N = new $1T(";
    int paramsOffset = 2;
    int numConstructorArgs = args.size();
    Object[] params = new Object[numConstructorArgs + paramsOffset];
    params[0] = typeName;
    params[1] = fieldName;
    for (int i = 0; i < numConstructorArgs; i++) {
      FieldInfo field = args.get(i);
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

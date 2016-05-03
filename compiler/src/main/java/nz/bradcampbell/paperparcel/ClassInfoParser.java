package nz.bradcampbell.paperparcel;

import com.google.auto.common.Visibility;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import nz.bradcampbell.paperparcel.FieldMatcher.AnyAnnotation;
import nz.bradcampbell.paperparcel.FieldMatcher.AnyClass;
import nz.bradcampbell.paperparcel.model.Adapter;
import nz.bradcampbell.paperparcel.model.Clazz;
import nz.bradcampbell.paperparcel.model.Field;
import nz.bradcampbell.paperparcel.model.Model;
import nz.bradcampbell.paperparcel.typeadapters.EnumAdapter;
import nz.bradcampbell.paperparcel.utils.AnnotationUtils;
import nz.bradcampbell.paperparcel.utils.StringUtils;

import static com.google.auto.common.MoreElements.asType;
import static com.google.auto.common.MoreElements.getLocalAndInheritedMethods;
import static com.google.auto.common.MoreTypes.asArray;
import static com.google.auto.common.MoreTypes.asDeclared;
import static com.google.auto.common.MoreTypes.isTypeOf;
import static java.util.Collections.singletonList;
import static javax.lang.model.element.Modifier.STATIC;
import static javax.lang.model.element.Modifier.TRANSIENT;
import static javax.lang.model.util.ElementFilter.constructorsIn;
import static javax.lang.model.util.ElementFilter.fieldsIn;
import static nz.bradcampbell.paperparcel.FieldMatcher.ANY_NAME;
import static nz.bradcampbell.paperparcel.utils.AnnotationUtils.getAnnotation;
import static nz.bradcampbell.paperparcel.utils.StringUtils.startsWithVowel;
import static nz.bradcampbell.paperparcel.utils.TypeUtils.getArgumentsOfClassFromType;
import static nz.bradcampbell.paperparcel.utils.TypeUtils.isEqualIgnoringWildcards;
import static nz.bradcampbell.paperparcel.utils.TypeUtils.isSingleton;

public class ClassInfoParser {
  private static final Pattern KT_9609_BUG_NAME_FORMAT = Pattern.compile("arg(\\d+)");

  private final ProcessingEnvironment processingEnv;

  private final Map<TypeName, String> adapters;
  private final Map<ClassName, ClassName> wrappers;
  private final Map<ClassName, ClassName> delegates;

  public ClassInfoParser(ProcessingEnvironment processingEnv, Map<TypeName, String> adapters,
      Map<ClassName, ClassName> wrappers, Map<ClassName, ClassName> delegates) {
    this.processingEnv = processingEnv;
    this.adapters = adapters;
    this.wrappers = wrappers;
    this.delegates = delegates;
  }

  public Set<Model> parseModels(Set<String> unprocessedTypes, boolean isLastRound) {
    Set<Model> models = new LinkedHashSet<>();
    for (Iterator<String> iterator = unprocessedTypes.iterator(); iterator.hasNext(); ) {
      String elementName = iterator.next();
      TypeElement element = processingEnv.getElementUtils().getTypeElement(elementName);
      Model model;
      try {
        model = parseClass(element);
        models.add(model);
        iterator.remove();
      } catch (UnknownFieldTypeException e) {
        // Only log an error in the last round as the unknown type might
        // be known in a later processing round
        if (isLastRound) {
          String aOrAn = startsWithVowel(e.element.asType().toString()) ? "an " : "a ";
          processingEnv.getMessager()
              .printMessage(Diagnostic.Kind.ERROR,
                  "PaperParcel does not know how to process "
                      + element.toString()
                      + " because the "
                      + e.element.toString()
                      + " field is "
                      + aOrAn
                      + e.element.asType().toString()
                      + " and "
                      + e.unknownType.toString()
                      + " is not a supported PaperParcel type. Define a TypeAdapter<"
                      + e.unknownType.toString()
                      + "> to add support for "
                      + e.unknownType.toString()
                      + " objects. Alternatively you can exclude the field by making it static,"
                      + " transient, or using the ExcludeFields annotation on "
                      + element.toString(),
                  e.element);
        }
      } catch (NonReadablePropertyException e) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR,
                "PaperParcel cannot read from the field named \""
                    + e.element.toString()
                    + "\" which was found when processing "
                    + element.toString()
                    + ". The field must either be non-private, or have a getter method with no"
                    + " arguments and have one of the following names: "
                    + possibleGetterNames(e.element.toString())
                    + ". Alternatively you can exclude the field by making it static, transient,"
                    + " or using the ExcludeFields annotation on "
                    + element.toString(),
                e.element);
      } catch (NonWritablePropertyException e) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR,
                "PaperParcel cannot write to the field named \""
                    + e.element.toString()
                    + "\" which was found when processing "
                    + element.toString()
                    + ". The field must either be have a constructor argument named "
                    + e.element.toString()
                    + ", be non-private, or have a setter method with one "
                    + e.element.asType().toString()
                    + " parameter and have one of the following names: "
                    + possibleSetterNames(e.element.toString())
                    + ". Alternatively you can exclude the field by making it static, transient,"
                    + " or using the ExcludeFields annotation on "
                    + element.toString(),
                e.element);
      } catch (NoVisibleConstructorException e) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR,
                "PaperParcel requires at least one non-private constructor to instantiate "
                    + e.typeElement,
                e.typeElement);
      } catch (UnsatisfiableConstructorException e) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR,
                "PaperParcel cannot satisfy constructor "
                    + e.constructor
                    + ". PaperParcel was able to find "
                    + e.fieldsToPassToConstructorCount
                    + " arguments, but needed "
                    + e.constructor.getParameters().size()
                    + ". The missing arguments were "
                    + e.missingArguments,
                e.constructor);
      } catch (DuplicateFieldNameException e) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR,
                "PaperParcel cannot process "
                    + element.toString()
                    + " because it has two non-ignored fields named \""
                    + e.first.toString()
                    + "\". The first can be found in "
                    + e.first.getEnclosingElement().toString()
                    + " and the second can be found in "
                    + e.second.getEnclosingElement().toString(),
                e.first);
      } catch (TooManyConstructorsException e) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR,
                "TypeAdapters can only have one constructor",
                e.element);
      } catch (InvalidParameterInTypeAdapterConstructorException e) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR,
                "TypeAdapter constructor arguments may only be other TypeAdapters or generic "
                    + "Class instances",
                e.element);
      } catch (UnexpectedTypeArgumentsListException e) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR,
                "TypeAdapter "
                + e.element
                + " has an unexpected amount of type arguments. Found "
                + (e.actual.size() == 0 ? "none" : e.actual)
                + " but expected "
                + (e.expected.size() == 0 ? "none" : e.expected)
                + ".",
                e.element);
      } catch (FieldWithRawTypeException e) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR,
                "PaperParcel cannot process the field \""
                    + e.element
                    + "\" in "
                    + element
                    + " because it is a raw type.",
                e.element);
      } catch (RawParameterInTypeAdapterConstructorException e) {
        processingEnv.getMessager()
            .printMessage(Diagnostic.Kind.ERROR,
                "Invalid TypeAdapter constructor found in "
                + e.adapter
                + ". It declares \""
                + e.param
                + "\" as a raw type.",
                e.element);
      }
    }
    return models;
  }

  private Model parseClass(TypeElement element)
      throws UnknownFieldTypeException, NonReadablePropertyException,
      NonWritablePropertyException, NoVisibleConstructorException,
      UnsatisfiableConstructorException, DuplicateFieldNameException,
      InvalidParameterInTypeAdapterConstructorException, TooManyConstructorsException,
      UnexpectedTypeArgumentsListException, FieldWithRawTypeException,
      RawParameterInTypeAdapterConstructorException {

    ClassName className = ClassName.get(element);
    ClassName wrappedClassName = wrappers.get(className);
    ClassName delegateClassName = delegates.get(className);

    List<Field> fieldList = new ArrayList<>();

    boolean singleton = isSingleton(processingEnv.getTypeUtils(), element);
    if (!singleton) {

      Map<TypeName, String> classScopedAdapters = new LinkedHashMap<>(adapters);

      // Override the type adapters with the current element preferences
      Map<String, Object> typeAdapterAnnotationMap =
          getLocalOrInheritedAnnotationAsMap(TypeAdapters.class, element);

      classScopedAdapters.putAll(getTypeAdaptersFromAnnotation(typeAdapterAnnotationMap));

      // Get exclusions
      List<FieldMatcher> fieldMatchers = getFieldMatchers(
          getLocalOrInheritedAnnotation(ExcludeFields.class, element));

      // Get all variable element information
      List<VariableElementInfo> variableElementInfoList =
          getVariableElementInfoList(element, fieldMatchers);

      for (VariableElementInfo variableElementInfo : variableElementInfoList) {
        VariableElement variable = variableElementInfo.element;

        // Get the variable scoped type adapters
        Map<TypeName, String> variableScopedAdapters =
            new LinkedHashMap<>(classScopedAdapters);
        Map<String, Object> annotation =
            getAnnotation(TypeAdapters.class, variableElementInfo.element);
        if (annotation == null) {
          annotation = getAnnotation(TypeAdapters.class, variableElementInfo.getterMethod);
        }
        variableScopedAdapters.putAll(getTypeAdaptersFromAnnotation(annotation));

        String name = variable.getSimpleName().toString();
        TypeName typeName = TypeName.get(variable.asType());

        Adapter adapter;
        try {
          AdapterType adapterType = getAdapterForField(variable.asType(), variableScopedAdapters);
          adapter = parseAdapterInfo(adapterType, variableScopedAdapters);
        } catch (UnknownTypeException e) {
          throw new UnknownFieldTypeException(variable, e.unknownType);
        } catch (RawTypeException e) {
          throw new FieldWithRawTypeException(variable, e.type);
        }

        String setterMethodName = null;
        if (variableElementInfo.setterMethod != null) {
          setterMethodName = variableElementInfo.setterMethod.getSimpleName().toString();
        }

        String getterMethodName = null;
        if (variableElementInfo.getterMethod != null) {
          getterMethodName = variableElementInfo.getterMethod.getSimpleName().toString();
        }

        fieldList.add(new Field(adapter, typeName, name, variableElementInfo.visible,
            variableElementInfo.constructorIndex, getterMethodName, setterMethodName));
      }
    }

    return new Model(className.packageName(), fieldList, className, wrappedClassName,
        delegateClassName, singleton);
  }

  private Map<String, Object> getLocalOrInheritedAnnotationAsMap(
      Class<? extends Annotation> annotationType, TypeElement element) {
    while (element != null) {
      Map<String, Object> annotation = getAnnotation(annotationType, element);
      if (annotation != null) return annotation;
      element = (TypeElement) processingEnv.getTypeUtils().asElement(element.getSuperclass());
    }
    return null;
  }

  private Map<TypeName, String> getTypeAdaptersFromAnnotation(Map<String, Object> annotation) {
    Map<TypeName, String> typeAdapters = new LinkedHashMap<>();
    if (annotation != null) {
      Object[] typeAdaptersArray = (Object[]) annotation.get("value");
      for (Object o : typeAdaptersArray) {
        TypeMirror type = (TypeMirror) o;
        TypeName typeName = getTypeArgumentFromTypeAdapterType(type);
        typeAdapters.put(typeName, type.toString());
      }
    }
    return typeAdapters;
  }

  private TypeName getTypeArgumentFromTypeAdapterType(TypeMirror type) {
    Types types = processingEnv.getTypeUtils();
    List<? extends TypeMirror> typeAdapterArguments = getArgumentsOfClassFromType(types, type,
        TypeAdapter.class.getName());
    if (typeAdapterArguments == null) {
      throw new AssertionError("TypeAdapter should have a type argument: " + type);
    }
    return TypeName.get(types.erasure(typeAdapterArguments.get(0)));
  }

  private <T extends Annotation> T getLocalOrInheritedAnnotation(Class<T> annotationType,
      TypeElement element) {
    while (element != null) {
      T annotation = element.getAnnotation(annotationType);
      if (annotation != null) return annotation;
      element = (TypeElement) processingEnv.getTypeUtils().asElement(element.getSuperclass());
    }
    return null;
  }

  private List<FieldMatcher> getFieldMatchers(ExcludeFields annotation) {
    List<FieldMatcher> fieldMatchers = new ArrayList<>();
    if (annotation != null) {
      Collections.addAll(fieldMatchers, annotation.value());
    }
    return fieldMatchers;
  }

  private List<VariableElementInfo> getVariableElementInfoList(final TypeElement typeElement,
      final List<FieldMatcher> fieldMatchers)
      throws NonReadablePropertyException, NonWritablePropertyException,
      NoVisibleConstructorException, UnsatisfiableConstructorException,
      DuplicateFieldNameException {
    final ImmutableSet<ExecutableElement> methods =
        getLocalAndInheritedMethods(typeElement, processingEnv.getElementUtils());

    final FluentIterable<VariableElementInfo> readableFields =
        FluentIterable.from(getLocalAndInheritedFields(typeElement))
            .filter(new Predicate<VariableElement>() {
              @Override public boolean apply(VariableElement input) {
                Set<Modifier> modifiers = input.getModifiers();
                return !modifiers.contains(STATIC) && !modifiers.contains(TRANSIENT);
              }
            })
            .filter(new Predicate<VariableElement>() {
              @Override public boolean apply(VariableElement input) {
                // Remove field immediately if annotated with @ExcludeField
                if (AnnotationUtils.hasAnnotation(input, ExcludeField.class)) {
                  return false;
                }
                // Remove all fields that match the rules set in each the field matchers
                for (FieldMatcher fieldMatcher : fieldMatchers) {
                  boolean matches = fieldMatcher.name().equals(ANY_NAME)
                      || input.getSimpleName().contentEquals(fieldMatcher.name());

                  TypeMirror declaring = null;
                  try {
                    fieldMatcher.declaringClass();
                  } catch (MirroredTypeException mte) {
                    declaring = mte.getTypeMirror();
                  }
                  assert declaring != null;
                  matches &= AnyClass.class.getCanonicalName().equals(declaring.toString())
                      || declaring.toString().equals(input.getEnclosingElement().toString());

                  TypeMirror type = null;
                  try {
                    fieldMatcher.type();
                  } catch (MirroredTypeException mte) {
                    type = mte.getTypeMirror();
                  }
                  assert type != null;
                  matches &= AnyClass.class.getCanonicalName().equals(type.toString())
                      || type.toString().equals(input.asType().toString());

                  TypeMirror annotation = null;
                  try {
                    fieldMatcher.annotation();
                  } catch (MirroredTypeException mte) {
                    annotation = mte.getTypeMirror();
                  }
                  assert annotation != null;
                  matches &= AnyAnnotation.class.getCanonicalName().equals(annotation.toString())
                      || AnnotationUtils.hasAnnotation(input, annotation);

                  if (matches) {
                    return false;
                  }
                }
                // Keep the remaining fields
                return true;
              }
            })
            .transform(new Function<VariableElement, VariableElementInfo>() {
              @Override public VariableElementInfo apply(final VariableElement field) {
                boolean visible = Visibility.ofElement(field) != Visibility.PRIVATE;
                ExecutableElement getterMethod = getGetterMethod(methods, field);
                return new VariableElementInfo(visible, getterMethod, field);
              }
            });

    Ordering<ExecutableElement> mostParametersOrdering = new Ordering<ExecutableElement>() {
      @Override public int compare(ExecutableElement left, ExecutableElement right) {
        return Ints.compare(left.getParameters().size(), right.getParameters().size());
      }
    };

    FluentIterable<ExecutableElement> visibleConstructors =
        FluentIterable.from(constructorsIn(typeElement.getEnclosedElements()))
            .filter(new Predicate<ExecutableElement>() {
              @Override public boolean apply(ExecutableElement input) {
                return Visibility.ofElement(input) != Visibility.PRIVATE;
              }
            });

    if (visibleConstructors.size() == 0) {
      throw new NoVisibleConstructorException(typeElement);
    }

    // Main constructor being the constructor with the most parameters
    final ExecutableElement mainConstructor = mostParametersOrdering.max(visibleConstructors);

    final FluentIterable<ParameterInfo> constructorParameterInfo =
        FluentIterable.from(mainConstructor.getParameters())
            .transform(new Function<VariableElement, ParameterInfo>() {
              @Override public ParameterInfo apply(VariableElement input) {
                String name = input.getSimpleName().toString();

                // Temporary workaround for https://youtrack.jetbrains.com/issue/KT-9609
                Matcher nameMatcher = KT_9609_BUG_NAME_FORMAT.matcher(name);
                if (nameMatcher.matches()) {
                  int offset = readableFields.size() - mainConstructor.getParameters().size();
                  int index = Integer.valueOf(nameMatcher.group(1)) + offset;
                  name = readableFields.get(index).element.getSimpleName().toString();
                }

                return new ParameterInfo(name, input.asType(), input);
              }
            });

    List<VariableElementInfo> result =
        readableFields.transform(new Function<VariableElementInfo, VariableElementInfo>() {
          @Override public VariableElementInfo apply(VariableElementInfo input) {
            ExecutableElement setterMethod = getSetterMethod(methods, input.element);
            VariableElement param =
                getConstructorParameter(constructorParameterInfo, input.element);
            int constructorIndex =
                param == null ? -1 : mainConstructor.getParameters().indexOf(param);
            return new VariableElementInfo(input.visible, input.getterMethod, input.element,
                setterMethod, constructorIndex);
          }
        }).toList();

    Map<String, VariableElement> nameToFieldMap = new HashMap<>();

    int mainConstructorSize = mainConstructor.getParameters().size();
    int fieldsToPassToConstructorCount = 0;
    VariableElement[] fieldsToPassToConstructor = new VariableElement[mainConstructorSize];

    for (VariableElementInfo fieldInfo : result) {
      // Ensure that the field can be read
      if (!fieldInfo.visible && fieldInfo.getterMethod == null) {
        throw new NonReadablePropertyException(fieldInfo.element);
      }
      // Ensure that the field can be written
      if (!fieldInfo.visible
          && fieldInfo.constructorIndex == -1
          && fieldInfo.setterMethod == null) {
        throw new NonWritablePropertyException(fieldInfo.element);
      }
      // Keep track of constructor arguments for later validation
      if (fieldInfo.constructorIndex != -1) {
        fieldsToPassToConstructor[fieldInfo.constructorIndex] = fieldInfo.element;
        fieldsToPassToConstructorCount++;
      }
      // Ensure there are no duplicate variable names. This can happen with inheritance.
      String variableName = fieldInfo.element.toString();
      if (nameToFieldMap.containsKey(variableName)) {
        throw new DuplicateFieldNameException(nameToFieldMap.get(variableName), fieldInfo.element);
      }
      nameToFieldMap.put(variableName, fieldInfo.element);
    }

    // Ensure that the main constructor can be satisfied
    int constructorArgumentSizeDifference = mainConstructorSize - fieldsToPassToConstructorCount;
    if (constructorArgumentSizeDifference > 0) {
      List<VariableElement> missingArguments = new ArrayList<>(constructorArgumentSizeDifference);
      List<? extends VariableElement> constructorArguments = mainConstructor.getParameters();
      for (int i = 0; i < constructorArguments.size(); i++) {
        if (fieldsToPassToConstructor[i] == null) {
          missingArguments.add(constructorArguments.get(i));
        }
      }
      throw new UnsatisfiableConstructorException(fieldsToPassToConstructorCount,
          mainConstructor, missingArguments);
    }

    return result;
  }

  private List<VariableElement> getLocalAndInheritedFields(TypeElement element) {
    List<VariableElement> variables = fieldsIn(element.getEnclosedElements());
    TypeMirror superType = element.getSuperclass();
    if (superType.getKind() != TypeKind.NONE) {
      TypeElement superElement = (TypeElement) processingEnv.getTypeUtils().asElement(superType);
      variables.addAll(getLocalAndInheritedFields(superElement));
    }
    return variables;
  }

  private ExecutableElement getGetterMethod(Set<ExecutableElement> methods,
      final VariableElement field) {
    final Set<String> possibleGetterNames = possibleGetterNames(field.getSimpleName().toString());
    return FluentIterable.from(methods).filter(new Predicate<ExecutableElement>() {
      @Override public boolean apply(ExecutableElement input) {
        return possibleGetterNames.contains(input.getSimpleName().toString())
            && input.getReturnType().toString().equals(field.asType().toString());
      }
    }).filter(new Predicate<ExecutableElement>() {
      @Override public boolean apply(ExecutableElement input) {
        return input.getParameters().size() == 0;
      }
    }).first().orNull();
  }

  private Set<String> possibleGetterNames(String name) {
    Set<String> possibleGetterNames = new LinkedHashSet<>(3);
    possibleGetterNames.add(name);
    possibleGetterNames.add("is" + StringUtils.capitalizeFirstCharacter(name));
    possibleGetterNames.add("has" + StringUtils.capitalizeFirstCharacter(name));
    possibleGetterNames.add("get" + StringUtils.capitalizeFirstCharacter(name));
    return possibleGetterNames;
  }

  private ExecutableElement getSetterMethod(Set<ExecutableElement> methods,
      final VariableElement field) {
    final Set<String> possibleSetterNames = possibleSetterNames(field.getSimpleName().toString());
    return FluentIterable.from(methods).filter(new Predicate<ExecutableElement>() {
      @Override public boolean apply(ExecutableElement input) {
        return possibleSetterNames.contains(input.getSimpleName().toString());
      }
    }).filter(new Predicate<ExecutableElement>() {
      @Override public boolean apply(ExecutableElement input) {
        List<? extends VariableElement> parameters = input.getParameters();
        Types types = processingEnv.getTypeUtils();
        return parameters.size() == 1
            && types.isAssignable(parameters.get(0).asType(), field.asType());
      }
    }).first().orNull();
  }

  private Set<String> possibleSetterNames(String name) {
    Set<String> possibleSetterNames = new LinkedHashSet<>(2);
    possibleSetterNames.add(name);
    possibleSetterNames.add("set" + StringUtils.capitalizeFirstCharacter(name));
    return possibleSetterNames;
  }

  private VariableElement getConstructorParameter(FluentIterable<ParameterInfo> parameterInfo,
      final VariableElement field) {
    return parameterInfo.filter(new Predicate<ParameterInfo>() {
      @Override public boolean apply(ParameterInfo input) {
        return input.name.equals(field.getSimpleName().toString())
            && isEqualIgnoringWildcards(input.type, field.asType());
      }
    }).transform(new Function<ParameterInfo, VariableElement>() {
      @Override public VariableElement apply(ParameterInfo input) {
        return input.element;
      }
    }).first().orNull();
  }

  private AdapterType getAdapterForField(TypeMirror field, Map<TypeName, String> scopedAdapters)
      throws UnknownTypeException, RawTypeException, UnexpectedTypeArgumentsListException {

    if (field.getKind().isPrimitive()) {
      // Primitive types do not need adapters
      return null;
    }

    if (field instanceof WildcardType) {
      // Wildcards are currently not supported
      throw new UnknownTypeException(field);
    }

    Types types = processingEnv.getTypeUtils();
    Elements elements = processingEnv.getElementUtils();

    String typeAdapterClassName = findAdapterName(types.erasure(field), scopedAdapters);
    TypeElement typeAdapterElement = elements.getTypeElement(typeAdapterClassName);
    TypeMirror[] typeArgumentsArray;

    int typeArgumentsSize = typeAdapterElement.getTypeParameters().size();
    if (typeAdapterClassName.equals(EnumAdapter.class.getName())) {
      List<? extends TypeMirror> typeArguments =
          getArgumentsOfClassFromType(types, field, Enum.class.getName());
      typeArgumentsArray = new TypeMirror[] { typeArguments.get(0) };
    } else if (field instanceof DeclaredType) {
      DeclaredType declaredFieldType = asDeclared(field);
      List<? extends TypeMirror> typeArgumentsList = declaredFieldType.getTypeArguments();
      if (typeArgumentsList.size() !=
          asType(types.asElement(field)).getTypeParameters().size()) {
        throw new RawTypeException(field);
      }
      if (typeArgumentsSize != typeArgumentsList.size()) {
        throw new UnexpectedTypeArgumentsListException(typeAdapterElement,
            asType(types.asElement(field)).getTypeParameters(),
            asType(typeAdapterElement).getTypeParameters());
      }
      typeArgumentsArray = new TypeMirror[typeArgumentsList.size()];
      typeArgumentsList.toArray(typeArgumentsArray);
    } else if (field instanceof ArrayType) {
      ArrayType arrayFieldType = asArray(field);
      if (typeArgumentsSize > 1) {
        throw new UnexpectedTypeArgumentsListException(typeAdapterElement,
            singletonList(types.asElement(arrayFieldType.getComponentType())),
            asType(typeAdapterElement).getTypeParameters());
      } else if (typeArgumentsSize == 1)  {
        TypeMirror componentType = arrayFieldType.getComponentType();
        typeArgumentsArray = new TypeMirror[] { componentType };
      } else {
        typeArgumentsArray = new TypeMirror[0];
      }
    } else {
      throw new AssertionError("Unknown type kind " + field.getKind() + " from " + field);
    }

    return new AdapterType(typeAdapterElement, typeArgumentsArray);
  }

  private final class AdapterType {
    private final TypeElement typeElement;
    private final TypeMirror[] typeArguments;

    private AdapterType(TypeElement typeElement, TypeMirror[] typeArguments) {
      this.typeElement = typeElement;
      this.typeArguments = typeArguments;
    }
  }

  private Adapter parseAdapterInfo(AdapterType adapterType, Map<TypeName, String> scopedAdapters)
      throws TooManyConstructorsException, InvalidParameterInTypeAdapterConstructorException,
      RawParameterInTypeAdapterConstructorException, RawTypeException,
      UnknownTypeException, UnexpectedTypeArgumentsListException, NoVisibleConstructorException {

    if (adapterType == null) {
      return null;
    }

    Types types = processingEnv.getTypeUtils();
    Elements elements = processingEnv.getElementUtils();

    TypeElement typeAdapterElement = adapterType.typeElement;
    TypeMirror[] typeArgumentsArray = adapterType.typeArguments;

    List<ExecutableElement> visibleTypeAdapterConstructors =
        FluentIterable.from(constructorsIn(typeAdapterElement.getEnclosedElements()))
            .filter(new Predicate<ExecutableElement>() {
              @Override public boolean apply(ExecutableElement input) {
                return !input.getModifiers().contains(Modifier.PRIVATE);
              }
            })
            .toList();

    if (visibleTypeAdapterConstructors.size() > 1) {
      throw new TooManyConstructorsException(typeAdapterElement);
    }

    DeclaredType declaredTypeAdapterType =
        types.getDeclaredType(typeAdapterElement, typeArgumentsArray);

    List<Object> dependencies = new ArrayList<>();

    if (visibleTypeAdapterConstructors.size() > 0) {
      ExecutableElement constructor = visibleTypeAdapterConstructors.get(0);
      ExecutableType constructorType =
          (ExecutableType) types.asMemberOf(declaredTypeAdapterType, constructor);

      TypeMirror rawTypeAdapterTypeMirror =
          types.erasure(elements.getTypeElement(TypeAdapter.class.getName()).asType());

      for (TypeMirror param : constructorType.getParameterTypes()) {
        if (types.isAssignable(types.erasure(param), rawTypeAdapterTypeMirror)) {
          TypeName dependencyTypeName = TypeName.get(types.erasure(param));
          String elementName = scopedAdapters.get(dependencyTypeName);
          if (elementName != null) {
            TypeElement dependencyTypeElement = elements.getTypeElement(elementName);
            AdapterType dependency = new AdapterType(dependencyTypeElement, typeArgumentsArray);
            dependencies.add(parseAdapterInfo(dependency, scopedAdapters));
          } else {
            List<? extends TypeMirror> typeAdapterArguments =
                getArgumentsOfClassFromType(types, param, TypeAdapter.class.getName());
            if (typeAdapterArguments.size() == 0) {
              throw new RawParameterInTypeAdapterConstructorException(typeAdapterElement,
                  constructor, param);
            }
            AdapterType dependency =
                getAdapterForField(typeAdapterArguments.get(0), scopedAdapters);
            dependencies.add(parseAdapterInfo(dependency, scopedAdapters));
          }
        } else if (isTypeOf(Class.class, param)) {
          TypeName typeName = TypeName.get(param);
          List<? extends TypeMirror> typeArguments =
              getArgumentsOfClassFromType(types, param, Class.class.getName());
          if (typeArguments.size() == 0) {
            throw new RawParameterInTypeAdapterConstructorException(typeAdapterElement,
                constructor, param);
          }
          TypeName typeArgument = TypeName.get(typeArguments.get(0));
          dependencies.add(new Clazz(typeName, typeArgument));
        } else {
          throw new InvalidParameterInTypeAdapterConstructorException(constructor);
        }
      }
    }

    boolean singleton = isSingleton(types, typeAdapterElement);

    if (visibleTypeAdapterConstructors.size() == 0 && !singleton) {
      throw new NoVisibleConstructorException(typeAdapterElement);
    }

    return new Adapter(dependencies, TypeName.get(declaredTypeAdapterType), singleton);
  }

  private String findAdapterName(TypeMirror erasedField,
      Map<TypeName, String> scopedTypeAdapters) throws UnknownTypeException {
    TypeName typeName = TypeName.get(erasedField);
    String adapterName = scopedTypeAdapters.get(typeName);

    if (adapterName != null) {
      return adapterName;
    }

    if (erasedField instanceof ArrayType) {
      // Fallback to default array adapter
      return scopedTypeAdapters.get(ArrayTypeName.of(ClassName.get(Object.class)));
    }

    Types types = processingEnv.getTypeUtils();

    TypeMirror type = erasedField;
    while (type.getKind() != TypeKind.NONE) {
      if (isTypeOf(Enum.class, type)) {
        // Fallback to default enum adapter
        return scopedTypeAdapters.get(ClassName.get(Enum.class));
      }
      TypeElement element = (TypeElement) types.asElement(type);
      type = element.getSuperclass();
    }

    throw new UnknownTypeException(erasedField);
  }

  public static class DuplicateFieldNameException extends Exception {
    final VariableElement first;
    final VariableElement second;

    public DuplicateFieldNameException(VariableElement first, VariableElement second) {
      this.first = first;
      this.second = second;
    }
  }

  public static class UnsatisfiableConstructorException extends Exception {
    final int fieldsToPassToConstructorCount;
    final ExecutableElement constructor;
    final List<VariableElement> missingArguments;

    public UnsatisfiableConstructorException(int fieldsToPassToConstructorCount,
        ExecutableElement constructor, List<VariableElement> missingArguments) {
      this.fieldsToPassToConstructorCount = fieldsToPassToConstructorCount;
      this.constructor = constructor;
      this.missingArguments = missingArguments;
    }
  }

  public static class NoVisibleConstructorException extends Exception {
    final TypeElement typeElement;

    public NoVisibleConstructorException(TypeElement typeElement) {
      this.typeElement = typeElement;
    }
  }

  public static class UnknownFieldTypeException extends Exception {
    final VariableElement element;
    final TypeMirror unknownType;

    UnknownFieldTypeException(VariableElement element, TypeMirror unknownType) {
      this.element = element;
      this.unknownType = unknownType;
    }
  }

  public static class UnknownTypeException extends Exception {
    final TypeMirror unknownType;

    UnknownTypeException(TypeMirror unknownType) {
      this.unknownType = unknownType;
    }
  }

  public static class NonReadablePropertyException extends Exception {
    final VariableElement element;

    NonReadablePropertyException(VariableElement element) {
      this.element = element;
    }
  }

  public static class NonWritablePropertyException extends Exception {
    final VariableElement element;

    NonWritablePropertyException(VariableElement element) {
      this.element = element;
    }
  }

  public static class TooManyConstructorsException extends Exception {
    final TypeElement element;

    public TooManyConstructorsException(TypeElement element) {
      this.element = element;
    }
  }

  public static class RawTypeException extends Exception {
    final TypeMirror type;

    public RawTypeException(TypeMirror type) {
      this.type = type;
    }
  }

  public static class FieldWithRawTypeException extends Exception {
    final VariableElement element;
    final TypeMirror type;

    public FieldWithRawTypeException(VariableElement element, TypeMirror type) {
      this.element = element;
      this.type = type;
    }
  }

  public static class InvalidParameterInTypeAdapterConstructorException extends Exception {
    final ExecutableElement element;

    public InvalidParameterInTypeAdapterConstructorException(ExecutableElement element) {
      this.element = element;
    }
  }

  public static class RawParameterInTypeAdapterConstructorException extends Exception {
    final TypeElement adapter;
    final ExecutableElement element;
    final TypeMirror param;

    public RawParameterInTypeAdapterConstructorException(TypeElement adapter,
        ExecutableElement element,
        TypeMirror param) {
      this.adapter = adapter;
      this.element = element;
      this.param = param;
    }
  }

  public static class UnexpectedTypeArgumentsListException extends Exception {
    final TypeElement element;
    final List<? extends Element> expected;
    final List<? extends Element> actual;

    public UnexpectedTypeArgumentsListException(TypeElement element,
        List<? extends Element> expected, List<? extends Element> actual) {
      this.element = element;
      this.expected = expected;
      this.actual = actual;
    }
  }

  private static class VariableElementInfo {
    public final boolean visible;
    public final ExecutableElement getterMethod;
    public final VariableElement element;
    public final ExecutableElement setterMethod;
    public final int constructorIndex;

    public VariableElementInfo(boolean visible, ExecutableElement getterMethod,
        VariableElement field) {
      this(visible, getterMethod, field, null, -1);
    }

    public VariableElementInfo(boolean visible, ExecutableElement getterMethod,
        VariableElement element, ExecutableElement setterMethod, int constructorIndex) {
      this.visible = visible;
      this.getterMethod = getterMethod;
      this.element = element;
      this.setterMethod = setterMethod;
      this.constructorIndex = constructorIndex;
    }
  }

  private static class ParameterInfo {
    private final String name;
    private final TypeMirror type;
    private final VariableElement element;

    public ParameterInfo(String name, TypeMirror type, VariableElement element) {
      this.name = name;
      this.type = type;
      this.element = element;
    }
  }
}

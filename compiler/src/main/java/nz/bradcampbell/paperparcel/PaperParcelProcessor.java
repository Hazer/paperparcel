package nz.bradcampbell.paperparcel;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.TypeName;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import nz.bradcampbell.paperparcel.model.ClassInfo;
import nz.bradcampbell.paperparcel.typeadapters.BundleAdapter;
import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;
import nz.bradcampbell.paperparcel.typeadapters.ListAdapter;

import static nz.bradcampbell.paperparcel.PaperParcels.DELEGATE_SUFFIX;
import static nz.bradcampbell.paperparcel.PaperParcels.WRAPPER_SUFFIX;
import static nz.bradcampbell.paperparcel.utils.TypeUtils.getArgumentsOfClassFromType;
import static nz.bradcampbell.paperparcel.utils.TypeUtils.hasTypeArguments;

/**
 * An annotation processor that creates Parcelable wrappers for all Kotlin data classes annotated
 * with @PaperParcel
 */
@AutoService(Processor.class)
public class PaperParcelProcessor extends AbstractProcessor {
  public static final String DATA_VARIABLE_NAME = "data";

  private Set<Class<? extends TypeAdapter>> builtInAdapters =
      ImmutableSet.<Class<? extends TypeAdapter>>builder()
          .add(IntegerAdapter.class)
          .add(BundleAdapter.class)
          .add(ListAdapter.class)
          .build();

  private final Set<TypeElement> unprocessedTypes = new LinkedHashSet<>();

  private final Map<ClassName, ClassName> wrappers = new LinkedHashMap<>();
  private final Map<ClassName, ClassName> delegates = new LinkedHashMap<>();
  private final Map<TypeName, TypeElement> adapters = new LinkedHashMap<>();

  private final WrapperGenerator wrapperGenerator = new WrapperGenerator();
  private final DelegateGenerator delegateGenerator = new DelegateGenerator();

  private Filer filer;
  private Types types;
  private Elements elements;

  private ClassInfoParser classInfoParser;

  @Override public Set<String> getSupportedAnnotationTypes() {
    Set<String> types = new LinkedHashSet<>();
    types.add(PaperParcel.class.getCanonicalName());
    types.add(DefaultAdapter.class.getCanonicalName());
    return types;
  }

  @Override public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.latestSupported();
  }

  @Override public synchronized void init(ProcessingEnvironment env) {
    super.init(env);
    types = env.getTypeUtils();
    elements = env.getElementUtils();
    filer = env.getFiler();
    classInfoParser = new ClassInfoParser(processingEnv, adapters, wrappers, delegates);

    // Convert all built-in adapters
    for (Class<? extends TypeAdapter> typeAdapterClass : builtInAdapters) {
      TypeElement element = elements.getTypeElement(typeAdapterClass.getName());
      TypeName typeArgumentTypeName = getTypeArgumentFromTypeAdapterType(element.asType());
      adapters.put(typeArgumentTypeName, element);
    }
  }

  @Override public boolean process(Set<? extends TypeElement> annotations,
      RoundEnvironment roundEnvironment) {
    findDefaultAdapters(roundEnvironment);
    findPaperParcels(roundEnvironment);

    boolean isLastRound = roundEnvironment.processingOver();

    // Parse and generate wrappers
    for (ClassInfo classInfo : classInfoParser.parseClasses(unprocessedTypes, isLastRound)) {
      try {
        wrapperGenerator.generateParcelableWrapper(classInfo).writeTo(filer);
        delegateGenerator.generatePaperParcelsDelegate(classInfo).writeTo(filer);
      } catch (IOException e) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "Could not write generated class "
            + classInfo.getClassName()
            + ": "
            + e);
      }
    }

    return true;
  }

  private void findDefaultAdapters(RoundEnvironment roundEnvironment) {
    for (Element element : roundEnvironment.getElementsAnnotatedWith(DefaultAdapter.class)) {

      // Ensure we are dealing with a TypeAdapter
      TypeMirror elementMirror = types.erasure(element.asType());
      TypeMirror typeAdapterMirror = types.erasure(
          elements.getTypeElement(TypeAdapter.class.getCanonicalName()).asType());

      if (!(types.isAssignable(elementMirror, typeAdapterMirror))) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "@DefaultAdapter must be applied to a TypeAdapter<T>",
            element);
        continue;
      }

      if (element.getKind() == ElementKind.INTERFACE) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "@DefaultAdapter cannot be applied to an interface",
            element);
        continue;
      }

      if (element.getModifiers().contains(Modifier.ABSTRACT)) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "@DefaultAdapter cannot be applied to an abstract class",
            element);
        continue;
      }

      TypeName typeArgumentTypeName = getTypeArgumentFromTypeAdapterType(element.asType());
      adapters.put(typeArgumentTypeName, (TypeElement) element);
    }
  }

  private void findPaperParcels(RoundEnvironment roundEnvironment) {
    for (Element element : roundEnvironment.getElementsAnnotatedWith(PaperParcel.class)) {
      TypeElement typeElement = (TypeElement) element;

      // Ensure the element isn't parameterized
      if (hasTypeArguments(typeElement)) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "@PaperParcel cannot be applied to a class with type parameters",
            typeElement);
        continue;
      }

      if (element.getKind() == ElementKind.INTERFACE) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "@PaperParcel cannot be applied to an interface",
            element);
        continue;
      }

      if (element.getModifiers().contains(Modifier.ABSTRACT)) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
            "@PaperParcel cannot be applied to an abstract class",
            element);
        continue;
      }

      unprocessedTypes.add(typeElement);

      ClassName className = ClassName.get(typeElement);

      ClassName wrapperName =
          ClassName.get(className.packageName(), className.simpleName() + WRAPPER_SUFFIX);
      wrappers.put(className, wrapperName);

      ClassName delegateName =
          ClassName.get(className.packageName(), className.simpleName() + DELEGATE_SUFFIX);
      delegates.put(className, delegateName);
    }
  }

  private TypeName getTypeArgumentFromTypeAdapterType(TypeMirror type) {
    List<? extends TypeMirror> typeAdapterArguments = getArgumentsOfClassFromType(types, type,
        TypeAdapter.class);
    if (typeAdapterArguments == null) {
      throw new AssertionError("TypeAdapter should have a type argument: " + type);
    }
    return TypeName.get(types.erasure(typeAdapterArguments.get(0)));
  }
}

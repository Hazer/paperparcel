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
import nz.bradcampbell.paperparcel.typeadapters.ArrayAdapter;
import nz.bradcampbell.paperparcel.typeadapters.BigDecimalAdapter;
import nz.bradcampbell.paperparcel.typeadapters.BigIntegerAdapter;
import nz.bradcampbell.paperparcel.typeadapters.BooleanAdapter;
import nz.bradcampbell.paperparcel.typeadapters.BooleanArrayAdapter;
import nz.bradcampbell.paperparcel.typeadapters.BundleAdapter;
import nz.bradcampbell.paperparcel.typeadapters.ByteAdapter;
import nz.bradcampbell.paperparcel.typeadapters.ByteArrayAdapter;
import nz.bradcampbell.paperparcel.typeadapters.CharArrayAdapter;
import nz.bradcampbell.paperparcel.typeadapters.CharSequenceAdapter;
import nz.bradcampbell.paperparcel.typeadapters.CharacterAdapter;
import nz.bradcampbell.paperparcel.typeadapters.DateAdapter;
import nz.bradcampbell.paperparcel.typeadapters.DoubleAdapter;
import nz.bradcampbell.paperparcel.typeadapters.DoubleArrayAdapter;
import nz.bradcampbell.paperparcel.typeadapters.FloatAdapter;
import nz.bradcampbell.paperparcel.typeadapters.FloatArrayAdapter;
import nz.bradcampbell.paperparcel.typeadapters.IntArrayAdapter;
import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;
import nz.bradcampbell.paperparcel.typeadapters.ListAdapter;
import nz.bradcampbell.paperparcel.typeadapters.LongAdapter;
import nz.bradcampbell.paperparcel.typeadapters.LongArrayAdapter;
import nz.bradcampbell.paperparcel.typeadapters.MapAdapter;
import nz.bradcampbell.paperparcel.typeadapters.ParcelableAdapter;
import nz.bradcampbell.paperparcel.typeadapters.PersistableBundleAdapter;
import nz.bradcampbell.paperparcel.typeadapters.QueueAdapter;
import nz.bradcampbell.paperparcel.typeadapters.SetAdapter;
import nz.bradcampbell.paperparcel.typeadapters.ShortAdapter;
import nz.bradcampbell.paperparcel.typeadapters.ShortArrayAdapter;
import nz.bradcampbell.paperparcel.typeadapters.SizeAdapter;
import nz.bradcampbell.paperparcel.typeadapters.SizeFAdapter;
import nz.bradcampbell.paperparcel.typeadapters.SparseArrayAdapter;
import nz.bradcampbell.paperparcel.typeadapters.StringAdapter;
import nz.bradcampbell.paperparcel.typeadapters.StringArrayAdapter;

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

          // Boxed primitives
          .add(BooleanAdapter.class)
          .add(ByteAdapter.class)
          .add(CharacterAdapter.class)
          .add(DoubleAdapter.class)
          .add(FloatAdapter.class)
          .add(IntegerAdapter.class)
          .add(LongAdapter.class)
          .add(ShortAdapter.class)

          // Arrays
          .add(ArrayAdapter.class)
          .add(BooleanArrayAdapter.class)
          .add(ByteArrayAdapter.class)
          .add(CharArrayAdapter.class)
          .add(DoubleArrayAdapter.class)
          .add(FloatArrayAdapter.class)
          .add(IntArrayAdapter.class)
          .add(LongArrayAdapter.class)
          .add(ShortArrayAdapter.class)
          .add(StringArrayAdapter.class)

          // Java language types
          .add(CharSequenceAdapter.class)
          .add(StringAdapter.class)

          // Java util types
          .add(ListAdapter.class)
          .add(SetAdapter.class)
          .add(QueueAdapter.class)
          .add(MapAdapter.class)
          .add(DateAdapter.class)

          // Java math types
          .add(BigIntegerAdapter.class)
          .add(BigDecimalAdapter.class)

          // Android sdk types
          .add(BundleAdapter.class)
          .add(ParcelableAdapter.class)
          .add(PersistableBundleAdapter.class)
          .add(SizeFAdapter.class)
          .add(SizeAdapter.class)
          .add(SparseArrayAdapter.class)

          .build();

  // TODO: this might need to be a Set<String> due to https://bugs.openjdk.java.net/browse/JDK-8144105
  private final Set<TypeElement> unprocessedTypes = new LinkedHashSet<>();

  private final Map<ClassName, ClassName> wrappers = new LinkedHashMap<>();
  private final Map<ClassName, ClassName> delegates = new LinkedHashMap<>();
  private final Map<TypeName, String> adapters = new LinkedHashMap<>();

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
      adapters.put(typeArgumentTypeName, element.toString());
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
      adapters.put(typeArgumentTypeName, element.toString());
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

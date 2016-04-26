package nz.bradcampbell.paperparcel;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ArrayTypeName;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import nz.bradcampbell.paperparcel.model.ClassInfo;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

public class DelegateGenerator {
  private static final ClassName DELEGATE = ClassName.get(PaperParcels.Delegate.class);

  public JavaFile generatePaperParcelsDelegate(ClassInfo classInfo) {
    AnnotationSpec suppressWarningsSpec = AnnotationSpec.builder(SuppressWarnings.class)
        .addMember("value", CodeBlock.of("$S", "unused"))
        .build();
    TypeName delegateInterface = ParameterizedTypeName.get(DELEGATE, classInfo.getClassName(),
        classInfo.getWrapperClassName());
    TypeSpec delegateSpec = TypeSpec.classBuilder(classInfo.getDelegateClassName().simpleName())
        .addAnnotation(suppressWarningsSpec)
        .addModifiers(PUBLIC, FINAL)
        .addSuperinterface(delegateInterface)
        .addMethod(generateWrapMethod(classInfo.getClassName(), classInfo.getWrapperClassName()))
        .addMethod(generateNewArrayMethod(classInfo.getClassName()))
        .build();
    return JavaFile.builder(classInfo.getClassPackage(), delegateSpec).build();
  }

  private MethodSpec generateNewArrayMethod(ClassName className) {
    return MethodSpec.methodBuilder("newArray")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .returns(ArrayTypeName.of(className))
        .addParameter(int.class, "size")
        .addStatement("return new $T[size]", className)
        .build();
  }

  private MethodSpec generateWrapMethod(ClassName className, ClassName wrapperClassName) {
    return MethodSpec.methodBuilder("wrap")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC)
        .returns(wrapperClassName)
        .addParameter(className, "original")
        .addStatement("return new $T(original)", wrapperClassName)
        .build();
  }
}

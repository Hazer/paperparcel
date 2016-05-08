package nz.bradcampbell.paperparcel;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import java.io.IOException;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;

import static javax.lang.model.element.Modifier.FINAL;
import static javax.lang.model.element.Modifier.PUBLIC;

public class WrapperAdapterGenerator {
  private static final ClassName PARCEL = ClassName.get("android.os", "Parcel");
  private static final ClassName ABSTRACT_ADAPTER = ClassName.get(AbstractAdapter.class);

  public JavaFile generateWrapperAdapter(String packageName, String name, TypeName type,
      TypeName wrapperType) throws IOException {
    return JavaFile.builder(packageName,
        TypeSpec.classBuilder(name)
            .addModifiers(PUBLIC, FINAL)
            .superclass(ParameterizedTypeName.get(ABSTRACT_ADAPTER, type))
            .addMethod(generateReadFromParcel(type, wrapperType))
            .addMethod(generateWriteToParcel(type, wrapperType))
            .addAnnotation(DefaultAdapter.class)
            .build())
        .build();
  }

  private MethodSpec generateReadFromParcel(TypeName type, TypeName wrapperType) {
    return MethodSpec.methodBuilder("read")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC, FINAL)
        .returns(type)
        .addParameter(PARCEL, "in")
        .addStatement("$1T wrapper = in.readParcelable($1T.class.getClassLoader())", wrapperType)
        .addStatement("return wrapper.get()")
        .build();
  }

  private MethodSpec generateWriteToParcel(TypeName type, TypeName wrapperType) {
    return MethodSpec.methodBuilder("write")
        .addAnnotation(Override.class)
        .addModifiers(PUBLIC, FINAL)
        .addParameter(type, "value")
        .addParameter(PARCEL, "dest")
        .addParameter(int.class, "flags")
        .addStatement("dest.writeParcelable(new $T(value), flags)", wrapperType)
        .build();
  }
}

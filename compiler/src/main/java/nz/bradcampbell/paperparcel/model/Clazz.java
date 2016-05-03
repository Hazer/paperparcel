package nz.bradcampbell.paperparcel.model;

import com.squareup.javapoet.TypeName;

public final class Clazz {
  private final TypeName typeName;
  private final TypeName typeArgument;

  public Clazz(TypeName typeName, TypeName typeArgument) {
    this.typeName = typeName;
    this.typeArgument = typeArgument;
  }

  public TypeName getTypeName() {
    return typeName;
  }

  public TypeName getTypeArgument() {
    return typeArgument;
  }
}

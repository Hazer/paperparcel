package nz.bradcampbell.paperparcel.model;

import com.google.common.base.Objects;
import com.squareup.javapoet.TypeName;

public final class Field {
  private final Adapter adapter;
  private final TypeName typeName;
  private final String name;
  private final boolean isVisible;
  private final int constructorPosition;
  private final String getterMethodName;
  private final String setterMethodName;

  public Field(Adapter adapter, TypeName typeName, String name, boolean isVisible,
      int constructorPosition, String getterMethodName, String setterMethodName) {
    this.adapter = adapter;
    this.typeName = typeName;
    this.name = name;
    this.isVisible = isVisible;
    this.constructorPosition = constructorPosition;
    this.getterMethodName = getterMethodName;
    this.setterMethodName = setterMethodName;
  }

  public Adapter getAdapter() {
    return adapter;
  }

  public TypeName getTypeName() {
    return typeName;
  }

  public String getName() {
    return name;
  }

  public boolean isVisible() {
    return isVisible;
  }

  public int getConstructorPosition() {
    return constructorPosition;
  }

  public String getGetterMethodName() {
    return getterMethodName;
  }

  public String getSetterMethodName() {
    return setterMethodName;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Field field = (Field) o;
    return isVisible == field.isVisible &&
        constructorPosition == field.constructorPosition &&
        Objects.equal(adapter, field.adapter) &&
        Objects.equal(typeName, field.typeName) &&
        Objects.equal(name, field.name) &&
        Objects.equal(getterMethodName, field.getterMethodName) &&
        Objects.equal(setterMethodName, field.setterMethodName);
  }

  @Override public int hashCode() {
    return Objects.hashCode(adapter, typeName, name, isVisible, constructorPosition,
        getterMethodName, setterMethodName);
  }
}

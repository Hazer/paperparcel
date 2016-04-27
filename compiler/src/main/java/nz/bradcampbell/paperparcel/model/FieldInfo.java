package nz.bradcampbell.paperparcel.model;

import com.google.common.base.Objects;
import com.squareup.javapoet.TypeName;

public final class FieldInfo {
  private final AdapterInfo adapterInfo;
  private final TypeName typeName;
  private final String name;
  private final boolean isVisible;
  private final int constructorPosition;
  private final String getterMethodName;
  private final String setterMethodName;

  public FieldInfo(AdapterInfo adapterInfo, TypeName typeName, String name, boolean isVisible,
      int constructorPosition, String getterMethodName, String setterMethodName) {
    this.adapterInfo = adapterInfo;
    this.typeName = typeName;
    this.name = name;
    this.isVisible = isVisible;
    this.constructorPosition = constructorPosition;
    this.getterMethodName = getterMethodName;
    this.setterMethodName = setterMethodName;
  }

  public AdapterInfo getAdapterInfo() {
    return adapterInfo;
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
    FieldInfo fieldInfo = (FieldInfo) o;
    return isVisible == fieldInfo.isVisible &&
        constructorPosition == fieldInfo.constructorPosition &&
        Objects.equal(adapterInfo, fieldInfo.adapterInfo) &&
        Objects.equal(typeName, fieldInfo.typeName) &&
        Objects.equal(name, fieldInfo.name) &&
        Objects.equal(getterMethodName, fieldInfo.getterMethodName) &&
        Objects.equal(setterMethodName, fieldInfo.setterMethodName);
  }

  @Override public int hashCode() {
    return Objects.hashCode(adapterInfo, typeName, name, isVisible, constructorPosition,
        getterMethodName, setterMethodName);
  }
}

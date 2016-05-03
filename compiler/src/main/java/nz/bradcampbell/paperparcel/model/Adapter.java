package nz.bradcampbell.paperparcel.model;

import com.google.common.base.Objects;
import com.squareup.javapoet.TypeName;
import java.util.List;

public final class Adapter {
  private final List<Object> dependencies;
  private final TypeName typeName;
  private final boolean singleton;

  public Adapter(List<Object> dependencies, TypeName typeName, boolean singleton) {
    this.dependencies = dependencies;
    this.typeName = typeName;
    this.singleton = singleton;
  }

  public TypeName getTypeName() {
    return typeName;
  }

  public boolean isSingleton() {
    return singleton;
  }

  public List<Object> getDependencies() {
    return dependencies;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Adapter that = (Adapter) o;
    return singleton == that.singleton &&
        Objects.equal(dependencies, that.dependencies) &&
        Objects.equal(typeName, that.typeName);
  }

  @Override public int hashCode() {
    return Objects.hashCode(dependencies, typeName, singleton);
  }
}

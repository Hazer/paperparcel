package nz.bradcampbell.paperparcel.model;

import com.google.common.base.Objects;
import com.squareup.javapoet.TypeName;
import java.util.List;

public class AdapterInfo {
  private final List<AdapterInfo> dependencies;
  private final TypeName typeName;
  private final boolean singleton;

  public AdapterInfo(List<AdapterInfo> dependencies, TypeName typeName,
      boolean singleton) {
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

  public List<AdapterInfo> getDependencies() {
    return dependencies;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AdapterInfo that = (AdapterInfo) o;
    return singleton == that.singleton &&
        Objects.equal(dependencies, that.dependencies) &&
        Objects.equal(typeName, that.typeName);
  }

  @Override public int hashCode() {
    return Objects.hashCode(dependencies, typeName, singleton);
  }
}

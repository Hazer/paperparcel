package nz.bradcampbell.paperparcel.model;

import com.google.common.base.Objects;
import com.squareup.javapoet.ClassName;
import java.util.List;

/**
 * A model object that holds information needed to build a Parcelable data class wrapper
 */
public final class Model {
  private final String classPackage;
  private final List<Field> fields;
  private final ClassName className;
  private final ClassName wrapperClassName;
  private final ClassName delegateClassName;
  private final boolean singleton;

  public Model(String classPackage, List<Field> fields, ClassName className,
      ClassName wrapperClassName, ClassName delegateClassName, boolean singleton) {
    this.classPackage = classPackage;
    this.fields = fields;
    this.className = className;
    this.wrapperClassName = wrapperClassName;
    this.delegateClassName = delegateClassName;
    this.singleton = singleton;
  }

  public String getClassPackage() {
    return classPackage;
  }

  public List<Field> getFields() {
    return fields;
  }

  public ClassName getClassName() {
    return className;
  }

  public ClassName getWrapperClassName() {
    return wrapperClassName;
  }

  public ClassName getDelegateClassName() {
    return delegateClassName;
  }

  public boolean isSingleton() {
    return singleton;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Model model = (Model) o;
    return singleton == model.singleton &&
        Objects.equal(classPackage, model.classPackage) &&
        Objects.equal(fields, model.fields) &&
        Objects.equal(className, model.className) &&
        Objects.equal(wrapperClassName, model.wrapperClassName) &&
        Objects.equal(delegateClassName, model.delegateClassName);
  }

  @Override public int hashCode() {
    return Objects.hashCode(classPackage, fields, className, wrapperClassName, delegateClassName,
        singleton);
  }
}

package nz.bradcampbell.paperparcel.typeadapters;

import java.lang.reflect.Array;
import nz.bradcampbell.paperparcel.TypeAdapter;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractArrayAdapter;
import org.jetbrains.annotations.NotNull;

public final class ReflectArrayAdapter<T> extends AbstractArrayAdapter<T> {
  private final Class<T> arrayClass;

  public ReflectArrayAdapter(Class<T> arrayClass, TypeAdapter<T> itemAdapter) {
    super(itemAdapter);
    this.arrayClass = arrayClass;
  }

  @SuppressWarnings("unchecked") @NotNull @Override protected T[] newArray(int length) {
    return (T[]) Array.newInstance(arrayClass, length);
  }
}

package nz.bradcampbell.paperparcel.typeadapters.base;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.TypeAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class ArrayAdapter<T> extends AbstractAdapter<T[]> {
  protected final TypeAdapter<T> itemAdapter;

  public ArrayAdapter(TypeAdapter<T> itemAdapter) {
    this.itemAdapter = itemAdapter;
  }

  @NotNull @Override public T[] readFromParcelInner(@NotNull Parcel source) {
    int size = source.readInt();
    T[] array = newArray(size);
    for (int i = 0; i < size; i++) {
      array[i] = itemAdapter.readFromParcel(source);
    }
    return array;
  }

  @Override
  public void writeToParcelInner(@NotNull T[] value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value.length);
    for (T item : value) {
      itemAdapter.writeToParcel(item, dest, flags);
    }
  }

  public abstract T[] newArray(int size);
}

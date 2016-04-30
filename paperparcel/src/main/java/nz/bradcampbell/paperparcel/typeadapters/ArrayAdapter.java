package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.TypeAdapter;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class ArrayAdapter<T> extends AbstractAdapter<T[]> {
  private final TypeAdapter<T> itemAdapter;

  public ArrayAdapter(TypeAdapter<T> itemAdapter) {
    this.itemAdapter = itemAdapter;
  }

  @NotNull @Override protected T[] read(@NotNull Parcel source) {
    int length = source.readInt();
    T[] value = itemAdapter.newArray(length);
    for (int i = 0; i < length; i++) {
      value[i] = itemAdapter.readFromParcel(source);
    }
    return value;
  }

  @Override protected void write(@NotNull T[] value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value.length);
    for (T item : value) {
      itemAdapter.writeToParcel(item, dest, flags);
    }
  }

  @NotNull @Override public T[][] newArray(int length) {
    throw new UnsupportedOperationException();
  }
}

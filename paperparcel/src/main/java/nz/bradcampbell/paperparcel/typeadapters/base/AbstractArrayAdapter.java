package nz.bradcampbell.paperparcel.typeadapters.base;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.TypeAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractArrayAdapter<T> extends AbstractAdapter<T[]> {
  private final TypeAdapter<T> itemAdapter;

  public AbstractArrayAdapter(TypeAdapter<T> itemAdapter) {
    this.itemAdapter = itemAdapter;
  }

  @NotNull @Override protected T[] read(@NotNull Parcel source) {
    int length = source.readInt();
    T[] value = newArray(length);
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

  @NotNull protected abstract T[] newArray(int length);
}

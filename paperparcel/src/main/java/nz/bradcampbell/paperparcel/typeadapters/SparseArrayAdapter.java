package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import android.util.SparseArray;
import nz.bradcampbell.paperparcel.TypeAdapter;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class SparseArrayAdapter<T> extends AbstractAdapter<SparseArray<T>> {
  private final TypeAdapter<T> itemAdapter;

  public SparseArrayAdapter(TypeAdapter<T> itemAdapter) {
    this.itemAdapter = itemAdapter;
  }

  @NotNull @Override protected SparseArray<T> read(@NotNull Parcel source) {
    int size = source.readInt();
    SparseArray<T> sparseArray = new SparseArray<>(size);
    for (int i = 0; i < size; i++) {
      sparseArray.put(source.readInt(), itemAdapter.readFromParcel(source));
    }
    return sparseArray;
  }

  @Override
  protected void write(@NotNull SparseArray<T> value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value.size());
    for (int i = 0; i < value.size(); i++) {
      int key = value.keyAt(i);
      dest.writeInt(key);
      itemAdapter.writeToParcel(value.get(key), dest, flags);
    }
  }
}

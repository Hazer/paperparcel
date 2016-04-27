package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import java.util.ArrayList;
import java.util.List;
import nz.bradcampbell.paperparcel.TypeAdapter;
import org.jetbrains.annotations.NotNull;

public final class ListAdapter<T> extends CollectionAdapter<List<T>, T> {
  public ListAdapter(TypeAdapter<T> itemAdapter) {
    super(itemAdapter);
  }

  @NotNull @Override public List<T> readFromParcelInner(@NotNull Parcel source) {
    int size = source.readInt();
    ArrayList<T> list = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      list.add(itemAdapter.readFromParcel(source));
    }
    return list;
  }

  @Override
  public void writeToParcelInner(@NotNull List<T> value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value.size());
    for (T item : value) {
      itemAdapter.writeToParcel(item, dest, flags);
    }
  }
}

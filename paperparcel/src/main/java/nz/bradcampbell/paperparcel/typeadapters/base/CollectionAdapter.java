package nz.bradcampbell.paperparcel.typeadapters.base;

import android.os.Parcel;
import java.util.Collection;
import nz.bradcampbell.paperparcel.TypeAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class CollectionAdapter<C extends Collection<T>, T> extends AbstractAdapter<C> {
  protected final TypeAdapter<T> itemAdapter;

  public CollectionAdapter(TypeAdapter<T> itemAdapter) {
    this.itemAdapter = itemAdapter;
  }

  @NotNull @Override public C readFromParcelInner(@NotNull Parcel source) {
    int size = source.readInt();
    C collection = newCollection(size);
    for (int i = 0; i < size; i++) {
      collection.add(itemAdapter.readFromParcel(source));
    }
    return collection;
  }

  @Override
  public void writeToParcelInner(@NotNull C value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value.size());
    for (T item : value) {
      itemAdapter.writeToParcel(item, dest, flags);
    }
  }

  public abstract C newCollection(int size);
}

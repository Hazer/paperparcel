package nz.bradcampbell.paperparcel.typeadapters.base;

import android.os.Parcel;
import java.util.Collection;
import nz.bradcampbell.paperparcel.TypeAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractCollectionAdapter<C extends Collection<T>, T> extends AbstractAdapter<C> {
  protected final TypeAdapter<T> itemAdapter;

  public AbstractCollectionAdapter(TypeAdapter<T> itemAdapter) {
    this.itemAdapter = itemAdapter;
  }

  @NotNull @Override protected C read(@NotNull Parcel source) {
    int size = source.readInt();
    C collection = newCollection(size);
    for (int i = 0; i < size; i++) {
      collection.add(itemAdapter.readFromParcel(source));
    }
    return collection;
  }

  @Override
  protected void write(@NotNull C value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value.size());
    for (T item : value) {
      itemAdapter.writeToParcel(item, dest, flags);
    }
  }

  protected abstract C newCollection(int size);
}

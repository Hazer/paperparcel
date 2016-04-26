package nz.bradcampbell.paperparcel.typeadapters;

import java.util.Collection;
import nz.bradcampbell.paperparcel.TypeAdapter;

public abstract class CollectionAdapter<C extends Collection<T>, T> extends AbstractAdapter<C> {
  protected final TypeAdapter<T> itemAdapter;

  public CollectionAdapter(TypeAdapter<T> itemAdapter) {
    this.itemAdapter = itemAdapter;
  }
}

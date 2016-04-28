package nz.bradcampbell.paperparcel.typeadapters;

import java.util.ArrayList;
import java.util.List;
import nz.bradcampbell.paperparcel.TypeAdapter;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractCollectionAdapter;

public final class ListAdapter<T> extends AbstractCollectionAdapter<List<T>, T> {
  public ListAdapter(TypeAdapter<T> itemAdapter) {
    super(itemAdapter);
  }

  @Override public List<T> newCollection(int size) {
    return new ArrayList<>(size);
  }
}

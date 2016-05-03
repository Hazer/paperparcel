package nz.bradcampbell.paperparcel.typeadapters;

import java.util.LinkedHashSet;
import java.util.Set;
import nz.bradcampbell.paperparcel.TypeAdapter;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractCollectionAdapter;

public final class SetAdapter<T> extends AbstractCollectionAdapter<Set<T>, T> {
  public SetAdapter(TypeAdapter<T> itemAdapter) {
    super(itemAdapter);
  }

  @Override protected Set<T> newCollection(int size) {
    return new LinkedHashSet<>(size);
  }
}

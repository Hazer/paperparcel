package nz.bradcampbell.paperparcel.typeadapters;

import java.util.LinkedList;
import java.util.Queue;
import nz.bradcampbell.paperparcel.TypeAdapter;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractCollectionAdapter;

public final class QueueAdapter<T> extends AbstractCollectionAdapter<Queue<T>, T> {
  public QueueAdapter(TypeAdapter<T> itemAdapter) {
    super(itemAdapter);
  }

  @Override protected Queue<T> newCollection(int size) {
    return new LinkedList<>();
  }
}

package nz.bradcampbell.paperparcel.typeadapters;

import java.util.LinkedHashMap;
import java.util.Map;
import nz.bradcampbell.paperparcel.TypeAdapter;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractMapAdapter;

public final class MapAdapter<K, V> extends AbstractMapAdapter<Map<K, V>, K, V> {
  public MapAdapter(TypeAdapter<K> keyAdapter, TypeAdapter<V> valueAdapter) {
    super(keyAdapter, valueAdapter);
  }

  @Override public Map<K, V> newMap(int size) {
    return new LinkedHashMap<>(size);
  }
}

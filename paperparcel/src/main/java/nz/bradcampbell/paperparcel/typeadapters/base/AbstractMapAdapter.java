package nz.bradcampbell.paperparcel.typeadapters.base;

import android.os.Parcel;
import java.util.Map;
import nz.bradcampbell.paperparcel.TypeAdapter;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractMapAdapter<M extends Map<K, V>, K, V> extends AbstractAdapter<M> {
  protected final TypeAdapter<K> keyAdapter;
  protected final TypeAdapter<V> valueAdapter;

  public AbstractMapAdapter(TypeAdapter<K> keyAdapter, TypeAdapter<V> valueAdapter) {
    this.keyAdapter = keyAdapter;
    this.valueAdapter = valueAdapter;
  }

  @NotNull @Override protected M read(@NotNull Parcel source) {
    int size = source.readInt();
    M map = newMap(size);
    for (int i = 0; i < size; i++) {
      map.put(keyAdapter.readFromParcel(source), valueAdapter.readFromParcel(source));
    }
    return map;
  }

  @Override
  protected void write(@NotNull M value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value.size());
    for (Map.Entry<K, V> entry : value.entrySet()) {
      keyAdapter.writeToParcel(entry.getKey(), dest, flags);
      valueAdapter.writeToParcel(entry.getValue(), dest, flags);
    }
  }

  protected abstract M newMap(int size);
}

package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class ShortArrayAdapter extends AbstractAdapter<short[]> {
  @NotNull @Override public short[] readFromParcelInner(@NotNull Parcel source) {
    int size = source.readInt();
    short[] value = new short[size];
    for (int i = 0; i < size; i++) {
      value[i] = (short) source.readInt();
    }
    return value;
  }

  @Override
  public void writeToParcelInner(@NotNull short[] value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value.length);
    for (short s : value) {
      dest.writeInt((int) s);
    }
  }
}

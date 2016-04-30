package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class ByteArrayAdapter extends AbstractAdapter<byte[]> {
  @NotNull @Override protected byte[] read(@NotNull Parcel source) {
    return source.createByteArray();
  }

  @Override
  protected void write(@NotNull byte[] value, @NotNull Parcel dest, int flags) {
    dest.writeByteArray(value);
  }

  @NotNull @Override public byte[][] newArray(int length) {
    return new byte[length][];
  }
}

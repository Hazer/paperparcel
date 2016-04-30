package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class ByteAdapter extends AbstractAdapter<Byte> {
  @NotNull @Override protected Byte read(@NotNull Parcel source) {
    return (byte) source.readInt();
  }

  @Override
  protected void write(@NotNull Byte value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value);
  }

  @NotNull @Override public Byte[] newArray(int length) {
    return new Byte[length];
  }
}

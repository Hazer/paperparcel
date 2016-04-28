package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class ByteAdapter extends AbstractAdapter<Byte> {
  @NotNull @Override public Byte readFromParcelInner(@NotNull Parcel source) {
    return (byte) source.readInt();
  }

  @Override
  public void writeToParcelInner(@NotNull Byte value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value);
  }
}

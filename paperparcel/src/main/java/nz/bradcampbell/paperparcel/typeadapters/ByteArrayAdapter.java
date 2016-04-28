package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class ByteArrayAdapter extends AbstractAdapter<byte[]> {
  @NotNull @Override public byte[] readFromParcelInner(@NotNull Parcel source) {
    return source.createByteArray();
  }

  @Override
  public void writeToParcelInner(@NotNull byte[] value, @NotNull Parcel dest, int flags) {
    dest.writeByteArray(value);
  }
}

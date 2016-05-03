package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class ShortAdapter extends AbstractAdapter<Short> {
  @NotNull @Override public Short read(@NotNull Parcel source) {
    return (short) source.readInt();
  }

  @Override
  protected void write(@NotNull Short value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value);
  }
}

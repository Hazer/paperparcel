package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class LongAdapter extends AbstractAdapter<Long> {
  @NotNull @Override protected Long read(@NotNull Parcel source) {
    return source.readLong();
  }

  @Override
  protected void write(@NotNull Long value, @NotNull Parcel dest, int flags) {
    dest.writeLong(value);
  }

  @NotNull @Override public Long[] newArray(int length) {
    return new Long[length];
  }
}

package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class LongAdapter extends AbstractAdapter<Long> {
  @NotNull @Override public Long readFromParcelInner(@NotNull Parcel source) {
    return source.readLong();
  }

  @Override
  public void writeToParcelInner(@NotNull Long value, @NotNull Parcel dest, int flags) {
    dest.writeLong(value);
  }
}

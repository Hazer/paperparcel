package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class LongArrayAdapter extends AbstractAdapter<long[]> {
  @NotNull @Override public long[] readFromParcelInner(@NotNull Parcel source) {
    return source.createLongArray();
  }

  @Override
  public void writeToParcelInner(@NotNull long[] value, @NotNull Parcel dest, int flags) {
    dest.writeLongArray(value);
  }
}

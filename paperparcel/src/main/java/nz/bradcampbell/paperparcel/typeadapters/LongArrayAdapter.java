package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class LongArrayAdapter extends AbstractAdapter<long[]> {
  @NotNull @Override protected long[] read(@NotNull Parcel source) {
    return source.createLongArray();
  }

  @Override
  protected void write(@NotNull long[] value, @NotNull Parcel dest, int flags) {
    dest.writeLongArray(value);
  }
}

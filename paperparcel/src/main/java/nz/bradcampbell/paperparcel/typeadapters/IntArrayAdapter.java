package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class IntArrayAdapter extends AbstractAdapter<int[]> {
  @NotNull @Override protected int[] read(@NotNull Parcel source) {
    return source.createIntArray();
  }

  @Override
  protected void write(@NotNull int[] value, @NotNull Parcel dest, int flags) {
    dest.writeIntArray(value);
  }

  @NotNull @Override public int[][] newArray(int length) {
    return new int[length][];
  }
}

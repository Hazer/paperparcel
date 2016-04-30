package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class BooleanArrayAdapter extends AbstractAdapter<boolean[]> {
  @NotNull @Override protected boolean[] read(@NotNull Parcel source) {
    return source.createBooleanArray();
  }

  @Override
  protected void write(@NotNull boolean[] value, @NotNull Parcel dest, int flags) {
    dest.writeBooleanArray(value);
  }

  @NotNull @Override public boolean[][] newArray(int length) {
    return new boolean[length][];
  }
}

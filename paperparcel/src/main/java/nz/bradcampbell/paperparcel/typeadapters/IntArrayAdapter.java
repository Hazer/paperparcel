package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class IntArrayAdapter extends AbstractAdapter<int[]> {
  @NotNull @Override public int[] readFromParcelInner(@NotNull Parcel source) {
    return source.createIntArray();
  }

  @Override
  public void writeToParcelInner(@NotNull int[] value, @NotNull Parcel dest, int flags) {
    dest.writeIntArray(value);
  }
}

package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class DoubleArrayAdapter extends AbstractAdapter<double[]> {
  @NotNull @Override protected double[] read(@NotNull Parcel source) {
    return source.createDoubleArray();
  }

  @Override
  protected void write(@NotNull double[] value, @NotNull Parcel dest, int flags) {
    dest.writeDoubleArray(value);
  }
}

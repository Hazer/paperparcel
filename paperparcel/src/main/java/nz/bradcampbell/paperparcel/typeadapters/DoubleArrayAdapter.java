package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class DoubleArrayAdapter extends AbstractAdapter<double[]> {
  @NotNull @Override public double[] readFromParcelInner(@NotNull Parcel source) {
    return source.createDoubleArray();
  }

  @Override
  public void writeToParcelInner(@NotNull double[] value, @NotNull Parcel dest, int flags) {
    dest.writeDoubleArray(value);
  }
}

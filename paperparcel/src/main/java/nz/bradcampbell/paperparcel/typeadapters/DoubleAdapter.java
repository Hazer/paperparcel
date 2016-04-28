package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class DoubleAdapter extends AbstractAdapter<Double> {
  @NotNull @Override public Double readFromParcelInner(@NotNull Parcel source) {
    return source.readDouble();
  }

  @Override
  public void writeToParcelInner(@NotNull Double value, @NotNull Parcel dest, int flags) {
    dest.writeDouble(value);
  }
}

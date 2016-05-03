package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class DoubleAdapter extends AbstractAdapter<Double> {
  @NotNull @Override protected Double read(@NotNull Parcel source) {
    return source.readDouble();
  }

  @Override
  protected void write(@NotNull Double value, @NotNull Parcel dest, int flags) {
    dest.writeDouble(value);
  }
}

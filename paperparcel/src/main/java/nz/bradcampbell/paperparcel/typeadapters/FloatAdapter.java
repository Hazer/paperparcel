package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class FloatAdapter extends AbstractAdapter<Float> {
  @NotNull @Override public Float readFromParcelInner(@NotNull Parcel source) {
    return source.readFloat();
  }

  @Override
  public void writeToParcelInner(@NotNull Float value, @NotNull Parcel dest, int flags) {
    dest.writeFloat(value);
  }
}

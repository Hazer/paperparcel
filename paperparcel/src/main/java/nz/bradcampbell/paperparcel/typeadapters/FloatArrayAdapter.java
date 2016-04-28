package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class FloatArrayAdapter extends AbstractAdapter<float[]> {
  @NotNull @Override public float[] readFromParcelInner(@NotNull Parcel source) {
    return source.createFloatArray();
  }

  @Override
  public void writeToParcelInner(@NotNull float[] value, @NotNull Parcel dest, int flags) {
    dest.writeFloatArray(value);
  }
}

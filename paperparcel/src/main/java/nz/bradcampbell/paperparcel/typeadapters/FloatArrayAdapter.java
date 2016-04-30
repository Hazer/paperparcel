package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class FloatArrayAdapter extends AbstractAdapter<float[]> {
  @NotNull @Override protected float[] read(@NotNull Parcel source) {
    return source.createFloatArray();
  }

  @Override
  protected void write(@NotNull float[] value, @NotNull Parcel dest, int flags) {
    dest.writeFloatArray(value);
  }

  @NotNull @Override public float[][] newArray(int length) {
    return new float[length][];
  }
}

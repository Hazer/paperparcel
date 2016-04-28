package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class BooleanArrayAdapter extends AbstractAdapter<boolean[]> {
  @NotNull @Override public boolean[] readFromParcelInner(@NotNull Parcel source) {
    return source.createBooleanArray();
  }

  @Override
  public void writeToParcelInner(@NotNull boolean[] value, @NotNull Parcel dest, int flags) {
    dest.writeBooleanArray(value);
  }
}

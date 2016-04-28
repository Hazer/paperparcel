package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class BooleanAdapter extends AbstractAdapter<Boolean> {
  @NotNull @Override public Boolean readFromParcelInner(@NotNull Parcel source) {
    return source.readInt() == 1;
  }

  @Override
  public void writeToParcelInner(@NotNull Boolean value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value ? 1 : 0);
  }
}

package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import android.util.Size;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class SizeAdapter extends AbstractAdapter<Size> {
  @NotNull @Override public Size readFromParcelInner(@NotNull Parcel source) {
    return source.readSize();
  }

  @Override
  public void writeToParcelInner(@NotNull Size value, @NotNull Parcel dest, int flags) {
    dest.writeSize(value);
  }
}

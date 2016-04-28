package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import android.util.SizeF;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class SizeFAdapter extends AbstractAdapter<SizeF> {
  @NotNull @Override public SizeF readFromParcelInner(@NotNull Parcel source) {
    return source.readSizeF();
  }

  @Override
  public void writeToParcelInner(@NotNull SizeF value, @NotNull Parcel dest, int flags) {
    dest.writeSizeF(value);
  }
}

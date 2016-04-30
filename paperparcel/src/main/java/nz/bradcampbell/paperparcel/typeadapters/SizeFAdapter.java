package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import android.util.SizeF;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class SizeFAdapter extends AbstractAdapter<SizeF> {
  @NotNull @Override public SizeF read(@NotNull Parcel source) {
    return source.readSizeF();
  }

  @Override
  protected void write(@NotNull SizeF value, @NotNull Parcel dest, int flags) {
    dest.writeSizeF(value);
  }

  @NotNull @Override public SizeF[] newArray(int length) {
    return new SizeF[length];
  }
}

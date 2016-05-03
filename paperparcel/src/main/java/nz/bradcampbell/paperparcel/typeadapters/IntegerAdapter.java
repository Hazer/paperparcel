package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class IntegerAdapter extends AbstractAdapter<Integer> {
  @NotNull @Override protected Integer read(@NotNull Parcel source) {
    return source.readInt();
  }

  @Override
  protected void write(@NotNull Integer value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value);
  }
}

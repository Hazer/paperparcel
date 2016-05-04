package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import android.os.Parcelable;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class ParcelableAdapter<T extends Parcelable> extends AbstractAdapter<T> {
  @NotNull @Override protected T read(@NotNull Parcel source) {
    return source.readParcelable(ParcelableAdapter.class.getClassLoader());
  }

  @Override
  protected void write(@NotNull T value, @NotNull Parcel dest, int flags) {
    dest.writeParcelable(value, flags);
  }
}

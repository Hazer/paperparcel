package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import android.os.Parcelable;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class ParcelableAdapter extends AbstractAdapter<Parcelable> {
  @NotNull @Override protected Parcelable read(@NotNull Parcel source) {
    return source.readParcelable(ParcelableAdapter.class.getClassLoader());
  }

  @Override
  protected void write(@NotNull Parcelable value, @NotNull Parcel dest, int flags) {
    dest.writeParcelable(value, flags);
  }
}

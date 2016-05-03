package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import java.util.Date;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class DateAdapter extends AbstractAdapter<Date> {
  @NotNull @Override protected Date read(@NotNull Parcel source) {
    return new Date(source.readLong());
  }

  @Override
  protected void write(@NotNull Date value, @NotNull Parcel dest, int flags) {
    dest.writeLong(value.getTime());
  }
}

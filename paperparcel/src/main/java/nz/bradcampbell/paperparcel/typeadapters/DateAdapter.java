package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import java.util.Date;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class DateAdapter extends AbstractAdapter<Date> {
  @NotNull @Override public Date readFromParcelInner(@NotNull Parcel source) {
    return new Date(source.readLong());
  }

  @Override
  public void writeToParcelInner(@NotNull Date value, @NotNull Parcel dest, int flags) {
    dest.writeLong(value.getTime());
  }
}

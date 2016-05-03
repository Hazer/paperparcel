package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class StringArrayAdapter extends AbstractAdapter<String[]> {
  @NotNull @Override protected String[] read(@NotNull Parcel source) {
    return source.createStringArray();
  }

  @Override
  protected void write(@NotNull String[] value, @NotNull Parcel dest, int flags) {
    dest.writeStringArray(value);
  }
}

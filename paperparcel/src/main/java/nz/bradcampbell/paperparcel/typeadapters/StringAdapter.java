package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class StringAdapter extends AbstractAdapter<String> {
  @NotNull @Override public String read(@NotNull Parcel source) {
    return source.readString();
  }

  @Override
  protected void write(@NotNull String value, @NotNull Parcel dest, int flags) {
    dest.writeString(value);
  }
}

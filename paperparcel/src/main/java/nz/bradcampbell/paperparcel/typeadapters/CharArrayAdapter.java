package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class CharArrayAdapter extends AbstractAdapter<char[]> {
  @NotNull @Override protected char[] read(@NotNull Parcel source) {
    return source.createCharArray();
  }

  @Override
  protected void write(@NotNull char[] value, @NotNull Parcel dest, int flags) {
    dest.writeCharArray(value);
  }
}

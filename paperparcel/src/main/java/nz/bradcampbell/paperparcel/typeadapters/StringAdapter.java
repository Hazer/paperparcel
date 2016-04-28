package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class StringAdapter extends AbstractAdapter<String> {
  @NotNull @Override public String readFromParcelInner(@NotNull Parcel source) {
    return source.readString();
  }

  @Override
  public void writeToParcelInner(@NotNull String value, @NotNull Parcel dest, int flags) {
    dest.writeString(value);
  }
}

package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class CharacterAdapter extends AbstractAdapter<Character> {
  @NotNull @Override public Character readFromParcelInner(@NotNull Parcel source) {
    return (char) source.readInt();
  }

  @Override
  public void writeToParcelInner(@NotNull Character value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value);
  }
}

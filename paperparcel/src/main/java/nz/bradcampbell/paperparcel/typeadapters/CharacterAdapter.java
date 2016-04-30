package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class CharacterAdapter extends AbstractAdapter<Character> {
  @NotNull @Override protected Character read(@NotNull Parcel source) {
    return (char) source.readInt();
  }

  @Override
  protected void write(@NotNull Character value, @NotNull Parcel dest, int flags) {
    dest.writeInt(value);
  }

  @NotNull @Override public Character[] newArray(int length) {
    return new Character[length];
  }
}

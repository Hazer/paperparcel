package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class EnumAdapter<T extends Enum<T>> extends AbstractAdapter<T> {
  private final Class<T> enumClass;

  public EnumAdapter(Class<T> enumClass) {
    this.enumClass = enumClass;
  }

  @NotNull @Override protected T read(@NotNull Parcel source) {
    return Enum.valueOf(enumClass, source.readString());
  }

  @Override
  protected void write(@NotNull T value, @NotNull Parcel dest, int flags) {
    dest.writeString(value.name());
  }
}

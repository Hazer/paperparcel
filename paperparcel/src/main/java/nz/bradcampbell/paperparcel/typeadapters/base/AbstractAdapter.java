package nz.bradcampbell.paperparcel.typeadapters.base;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.TypeAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAdapter<T> implements TypeAdapter<T> {
  @Nullable @Override public final T readFromParcel(@NotNull Parcel source) {
    T value = null;
    if (source.readInt() == 1) {
      value = read(source);
    }
    return value;
  }

  @NotNull protected abstract T read(@NotNull Parcel source);

  @Override public final void writeToParcel(@Nullable T value, @NotNull Parcel dest, int flags) {
    if (value == null) {
      dest.writeInt(0);
    } else {
      dest.writeInt(1);
      write(value, dest, flags);
    }
  }

  protected abstract void write(@NotNull T value, @NotNull Parcel dest, int flags);
}

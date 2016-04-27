package nz.bradcampbell.paperparcel.typeadapters.base;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.TypeAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractAdapter<T> implements TypeAdapter<T> {
  @Nullable @Override public final T readFromParcel(@NotNull Parcel source) {
    T value = null;
    if (source.readInt() == 0) {
      value = readFromParcelInner(source);
    }
    return value;
  }

  @NotNull public abstract T readFromParcelInner(@NotNull Parcel source);

  @Override public final void writeToParcel(@Nullable T value, @NotNull Parcel dest, int flags) {
    if (value == null) {
      dest.writeInt(0);
    } else {
      dest.writeInt(1);
    }
  }

  public abstract void writeToParcelInner(@NotNull T value, @NotNull Parcel dest, int flags);
}

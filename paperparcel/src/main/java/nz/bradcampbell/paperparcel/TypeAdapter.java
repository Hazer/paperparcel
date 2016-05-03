package nz.bradcampbell.paperparcel;

import android.os.Parcel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A custom method for reading/writing a Type
 *
 * @param <T> The Type to override the default reading/writing functionality for
 */
public interface TypeAdapter<T> {

  /**
   * Creates a new instance of the desired Type by reading values from the Parcel {@code inParcel}
   *
   * @param source The {@link Parcel} which contains the values of {@code T}
   * @return A new object based on the values in {@code inParcel}.
   */
  @Nullable T readFromParcel(@NotNull Parcel source);

  /**
   * Writes {@code value} to the Parcel {@code outParcel}
   *
   * @param value The object to be written to the {@link Parcel}
   * @param dest The {@link Parcel} which will contain the value of {@code T}
   * @param flags Additional flags about how the object should be written. May be 0 or
   * {@link android.os.Parcelable#PARCELABLE_WRITE_RETURN_VALUE}.
   */
  void writeToParcel(@Nullable T value, @NotNull Parcel dest, int flags);
}

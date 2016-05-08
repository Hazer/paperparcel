package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import java.util.UUID;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class UuidAdapter extends AbstractAdapter<UUID> {
  @NotNull @Override protected UUID read(@NotNull Parcel source) {
    return new UUID(source.readLong(), source.readLong());
  }

  @Override
  protected void write(@NotNull UUID value, @NotNull Parcel dest, int flags) {
    dest.writeLong(value.getMostSignificantBits());
    dest.writeLong(value.getLeastSignificantBits());
  }
}

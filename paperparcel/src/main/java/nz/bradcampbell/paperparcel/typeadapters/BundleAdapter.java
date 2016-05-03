package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Bundle;
import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class BundleAdapter extends AbstractAdapter<Bundle> {
  @NotNull @Override protected Bundle read(@NotNull Parcel source) {
    return source.readBundle(BundleAdapter.class.getClassLoader());
  }

  @Override protected void write(@NotNull Bundle value, @NotNull Parcel dest, int flags) {
    dest.writeBundle(value);
  }
}

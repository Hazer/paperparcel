package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Bundle;
import android.os.Parcel;
import org.jetbrains.annotations.NotNull;

public class BundleAdapter extends AbstractAdapter<Bundle> {
  @NotNull @Override public Bundle readFromParcelInner(@NotNull Parcel source) {
    return source.readBundle(BundleAdapter.class.getClassLoader());
  }

  @Override public void writeToParcelInner(@NotNull Bundle value, @NotNull Parcel dest, int flags) {
    dest.writeBundle(value);
  }
}

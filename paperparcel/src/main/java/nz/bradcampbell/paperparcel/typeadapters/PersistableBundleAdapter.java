package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import android.os.PersistableBundle;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class PersistableBundleAdapter extends AbstractAdapter<PersistableBundle> {
  @NotNull @Override protected PersistableBundle read(@NotNull Parcel source) {
    return source.readPersistableBundle(PersistableBundleAdapter.class.getClassLoader());
  }

  @Override protected void write(@NotNull PersistableBundle value, @NotNull Parcel dest, int flags) {
    dest.writePersistableBundle(value);
  }
}

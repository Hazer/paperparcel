package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import android.util.SparseBooleanArray;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class SparseBooleanArrayAdapter extends AbstractAdapter<SparseBooleanArray> {
  @NotNull @Override protected SparseBooleanArray read(@NotNull Parcel source) {
    return source.readSparseBooleanArray();
  }

  @Override
  protected void write(@NotNull SparseBooleanArray value, @NotNull Parcel dest, int flags) {
    dest.writeSparseBooleanArray(value);
  }
}

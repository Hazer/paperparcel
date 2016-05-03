package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import android.text.TextUtils;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public class CharSequenceAdapter extends AbstractAdapter<CharSequence> {
  @NotNull @Override protected CharSequence read(@NotNull Parcel source) {
    return TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
  }

  @Override
  protected void write(@NotNull CharSequence value, @NotNull Parcel dest, int flags) {
    TextUtils.writeToParcel(value, dest, flags);
  }
}

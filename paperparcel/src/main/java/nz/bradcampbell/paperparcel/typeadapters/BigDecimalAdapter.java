package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import java.math.BigDecimal;
import java.math.BigInteger;
import nz.bradcampbell.paperparcel.TypeAdapter;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class BigDecimalAdapter extends AbstractAdapter<BigDecimal> {
  private final TypeAdapter<BigInteger> bigIntegerAdapter;

  public BigDecimalAdapter(TypeAdapter<BigInteger> bigIntegerAdapter) {
    this.bigIntegerAdapter = bigIntegerAdapter;
  }

  @NotNull @Override protected BigDecimal read(@NotNull Parcel source) {
    return new BigDecimal(bigIntegerAdapter.readFromParcel(source), source.readInt());
  }

  @Override
  protected void write(@NotNull BigDecimal value, @NotNull Parcel dest, int flags) {
    bigIntegerAdapter.writeToParcel(value.unscaledValue(), dest, flags);
    dest.writeInt(value.scale());
  }

  @NotNull @Override public BigDecimal[] newArray(int length) {
    return new BigDecimal[length];
  }
}

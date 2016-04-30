package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import java.math.BigInteger;
import nz.bradcampbell.paperparcel.TypeAdapter;
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter;
import org.jetbrains.annotations.NotNull;

public final class BigIntegerAdapter extends AbstractAdapter<BigInteger> {
  private final TypeAdapter<byte[]> byteArrayAdapter;

  public BigIntegerAdapter(TypeAdapter<byte[]> byteArrayAdapter) {
    this.byteArrayAdapter = byteArrayAdapter;
  }

  @NotNull @Override protected BigInteger read(@NotNull Parcel source) {
    return new BigInteger(byteArrayAdapter.readFromParcel(source));
  }

  @Override
  protected void write(@NotNull BigInteger value, @NotNull Parcel dest, int flags) {
    byteArrayAdapter.writeToParcel(value.toByteArray(), dest, flags);
  }

  @NotNull @Override public BigInteger[] newArray(int length) {
    return new BigInteger[length];
  }
}

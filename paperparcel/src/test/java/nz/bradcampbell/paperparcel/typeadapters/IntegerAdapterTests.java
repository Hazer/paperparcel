package nz.bradcampbell.paperparcel.typeadapters;

import android.os.Parcel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class IntegerAdapterTests {
  @Test public void integersAreCorrectlyParcelled() {
    IntegerAdapter integerAdapter = new IntegerAdapter();
    Parcel parcel = Parcel.obtain();
    integerAdapter.writeToParcelInner(42, parcel, 0);
    parcel.setDataPosition(0);
    assertThat(integerAdapter.readFromParcelInner(parcel)).isEqualTo(42);
    parcel.recycle();
  }
}

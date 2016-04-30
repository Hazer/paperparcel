package nz.bradcampbell.paperparcel;

import android.os.Parcel;
import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;
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
    Integer expected = 42;
    integerAdapter.writeToParcel(expected, parcel, 0);
    parcel.setDataPosition(0);
    Integer result = integerAdapter.readFromParcel(parcel);
    parcel.recycle();
    assertThat(expected).isEqualTo(result);
  }
}

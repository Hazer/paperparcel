package nz.bradcampbell.paperparcel;

import java.math.BigDecimal;
import nz.bradcampbell.paperparcel.typeadapters.BigDecimalAdapter;
import nz.bradcampbell.paperparcel.typeadapters.BigIntegerAdapter;
import nz.bradcampbell.paperparcel.typeadapters.ByteArrayAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BigDecimalAdapterTests {
  @Test public void bigDecimalsAreCorrectlyParcelled() {
    BigDecimalAdapter adapter = new BigDecimalAdapter(new BigIntegerAdapter(new ByteArrayAdapter()));
    BigDecimal expected = new BigDecimal("42.42");
    BigDecimal result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

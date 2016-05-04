package nz.bradcampbell.paperparcel;

import java.math.BigInteger;
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
public class BigIntegerAdapterTests {
  @Test public void bigIntegersAreCorrectlyParcelled() {
    BigIntegerAdapter adapter = new BigIntegerAdapter(new ByteArrayAdapter());
    BigInteger expected = new BigInteger("42");
    BigInteger result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

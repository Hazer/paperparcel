package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.ByteAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ByteAdapterTests {
  @Test public void bytesAreCorrectlyParcelled() {
    ByteAdapter adapter = new ByteAdapter();
    Byte expected = (byte) 42;
    Byte result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

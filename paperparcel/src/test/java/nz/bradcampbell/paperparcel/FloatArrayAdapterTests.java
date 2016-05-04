package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.FloatArrayAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FloatArrayAdapterTests {
  @Test public void byteArraysAreCorrectlyParcelled() {
    FloatArrayAdapter adapter = new FloatArrayAdapter();
    float[] expected = new float[] { 42.42f };
    float[] result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected, 0);
  }
}

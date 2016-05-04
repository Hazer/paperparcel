package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.FloatAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class FloatAdapterTests2 {
  @Test public void floatsAreCorrectlyParcelled() {
    FloatAdapter adapter = new FloatAdapter();
    Float expected = 42.42f;
    Float result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

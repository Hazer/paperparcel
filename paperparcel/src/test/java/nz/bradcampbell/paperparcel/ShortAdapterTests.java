package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.ShortAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ShortAdapterTests {
  @Test public void shortsAreCorrectlyParcelled() {
    ShortAdapter adapter = new ShortAdapter();
    Short expected = 42;
    Short result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

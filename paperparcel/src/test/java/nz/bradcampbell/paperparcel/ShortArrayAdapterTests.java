package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.ShortArrayAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ShortArrayAdapterTests {
  @Test public void shortArraysAreCorrectlyParcelled() {
    ShortArrayAdapter adapter = new ShortArrayAdapter();
    short[] expected = new short[] { 42 };
    short[] result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.LongArrayAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class LongArrayAdapterTests {
  @Test public void longArraysAreCorrectlyParcelled() {
    LongArrayAdapter adapter = new LongArrayAdapter();
    long[] expected = new long[] { Long.MAX_VALUE, Long.MIN_VALUE };
    long[] result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

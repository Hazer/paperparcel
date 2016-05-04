package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.BooleanArrayAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BooleanArrayAdapterTests {
  @Test public void booleanArraysAreCorrectlyParcelled() {
    BooleanArrayAdapter adapter = new BooleanArrayAdapter();
    boolean[] expected = new boolean[] { true, false };
    boolean[] result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.DoubleArrayAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DoubleArrayAdapterTests {
  @Test public void doubleArraysAreCorrectlyParcelled() {
    DoubleArrayAdapter adapter = new DoubleArrayAdapter();
    double[] expected = new double[] { 42.42 };
    double[] result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected, 0);
  }
}

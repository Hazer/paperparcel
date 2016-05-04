package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;
import nz.bradcampbell.paperparcel.typeadapters.ReflectArrayAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class ReflectArrayAdapterTests {
  @Test public void arraysAreCorrectlyParcelled() {
    ReflectArrayAdapter<Integer> adapter = new ReflectArrayAdapter<>(Integer.class, new IntegerAdapter());
    Integer[] expected = new Integer[] { 42 };
    Integer[] result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

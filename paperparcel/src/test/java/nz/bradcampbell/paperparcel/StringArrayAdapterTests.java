package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.StringArrayAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class StringArrayAdapterTests {
  @Test public void stringArraysAreCorrectlyParcelled() {
    StringArrayAdapter adapter = new StringArrayAdapter();
    String[] expected = new String[] { "hello world" };
    String[] result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

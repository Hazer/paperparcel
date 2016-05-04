package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.CharArrayAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CharArrayAdapterTests {
  @Test public void charArraysAreCorrectlyParcelled() {
    CharArrayAdapter adapter = new CharArrayAdapter();
    char[] expected = new char[] { 42 };
    char[] result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

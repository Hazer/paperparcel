package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.CharSequenceAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CharSequenceAdapterTests {
  @Test public void charSequencesAreCorrectlyParcelled() {
    CharSequenceAdapter adapter = new CharSequenceAdapter();
    CharSequence expected = "Hello";
    CharSequence result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

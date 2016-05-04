package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.CharacterAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CharacterAdapterTests {
  @Test public void charactersAreCorrectlyParcelled() {
    CharacterAdapter adapter = new CharacterAdapter();
    Character expected = 42;
    Character result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

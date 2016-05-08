package nz.bradcampbell.paperparcel;

import java.util.UUID;
import nz.bradcampbell.paperparcel.typeadapters.UuidAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class UuidAdapterTests {
  @Test public void uuidsAreCorrectlyParcelled() {
    UuidAdapter adapter = new UuidAdapter();
    UUID expected = UUID.randomUUID();
    UUID result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

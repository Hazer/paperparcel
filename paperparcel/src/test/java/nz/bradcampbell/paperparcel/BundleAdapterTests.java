package nz.bradcampbell.paperparcel;

import android.os.Bundle;
import nz.bradcampbell.paperparcel.typeadapters.BundleAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class BundleAdapterTests {
  @Test public void bundlesAreCorrectlyParcelled() {
    BundleAdapter adapter = new BundleAdapter();
    Bundle expected = new Bundle();
    expected.putString("TEST_KEY", "TEST_VALUE");
    Bundle result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result.getString("TEST_KEY")).isEqualTo("TEST_VALUE");
  }
}

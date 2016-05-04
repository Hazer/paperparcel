package nz.bradcampbell.paperparcel;

import java.util.HashMap;
import java.util.Map;
import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;
import nz.bradcampbell.paperparcel.typeadapters.MapAdapter;
import nz.bradcampbell.paperparcel.typeadapters.StringAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class MapAdapterTests {
  @Test public void mapsAreCorrectlyParcelled() {
    MapAdapter<String, Integer> adapter = new MapAdapter<>(new StringAdapter(), new IntegerAdapter());
    Map<String, Integer> expected = new HashMap<>();
    expected.put("LIFE_MEANING", 42);
    Map<String, Integer> result = TestUtils.writeThenRead(adapter, expected);
    assertThat(result).isEqualTo(expected);
  }
}

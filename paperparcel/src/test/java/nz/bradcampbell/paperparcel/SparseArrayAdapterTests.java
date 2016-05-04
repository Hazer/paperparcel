package nz.bradcampbell.paperparcel;

import android.util.SparseArray;
import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;
import nz.bradcampbell.paperparcel.typeadapters.SparseArrayAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class SparseArrayAdapterTests {
  @Test public void sparseArraysAreCorrectlyParcelled() {
    SparseArrayAdapter<Integer> adapter = new SparseArrayAdapter<>(new IntegerAdapter());
    SparseArray<Integer> expected = new SparseArray<>();
    expected.put(42, 42);
    SparseArray<Integer> result = TestUtils.writeThenRead(adapter, expected);
    assertThat(expected.size()).isEqualTo(result.size());
    for (int i = 0; i < result.size(); i++) {
      int key = result.keyAt(i);
      assertThat(result.get(key)).isEqualTo(expected.get(key));
    }
  }
}

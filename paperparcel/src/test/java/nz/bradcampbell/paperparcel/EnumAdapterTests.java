package nz.bradcampbell.paperparcel;

import nz.bradcampbell.paperparcel.typeadapters.EnumAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class EnumAdapterTests {
  enum TestEnum {
    ONE, TWO
  }

  @Test public void enumsAreCorrectlyParcelled() {
    EnumAdapter<TestEnum> adapter = new EnumAdapter<>(TestEnum.class);

    TestEnum expected1 = TestEnum.ONE;
    TestEnum result1 = TestUtils.writeThenRead(adapter, expected1);
    assertThat(result1).isEqualTo(expected1);

    TestEnum expected2 = TestEnum.TWO;
    TestEnum result2 = TestUtils.writeThenRead(adapter, expected2);
    assertThat(result2).isEqualTo(expected2);
  }
}

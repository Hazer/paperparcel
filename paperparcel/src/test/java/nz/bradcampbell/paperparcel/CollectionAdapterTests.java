package nz.bradcampbell.paperparcel;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;
import nz.bradcampbell.paperparcel.typeadapters.ListAdapter;
import nz.bradcampbell.paperparcel.typeadapters.QueueAdapter;
import nz.bradcampbell.paperparcel.typeadapters.SetAdapter;
import nz.bradcampbell.paperparcel.utils.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class CollectionAdapterTests {
  @Test public void listsAreCorrectlyParcelled() {
    ListAdapter<Integer> adapter = new ListAdapter<>(new IntegerAdapter());
    List<Integer> expected = Arrays.asList(42, 0);
    List<Integer> result = TestUtils.writeThenRead(adapter, expected);
    assertItemsAreEqual(expected, result);
  }

  @Test public void queuesAreCorrectlyParcelled() {
    QueueAdapter<Integer> adapter = new QueueAdapter<>(new IntegerAdapter());
    Queue<Integer> expected = new PriorityQueue<>();
    expected.add(42);
    Queue<Integer> result = TestUtils.writeThenRead(adapter, expected);
    assertItemsAreEqual(expected, result);
  }

  @Test public void setsAreCorrectlyParcelled() {
    SetAdapter<Integer> adapter = new SetAdapter<>(new IntegerAdapter());
    Set<Integer> expected = new LinkedHashSet<>();
    expected.add(42);
    Set<Integer> result = TestUtils.writeThenRead(adapter, expected);
    assertItemsAreEqual(expected, result);
  }

  private <T> void assertItemsAreEqual(Collection<T> first, Collection<T> second) {
    assertThat(first.size()).isEqualTo(second.size());
    Iterator<T> firstIterator = first.iterator();
    Iterator<T> secondIterator = second.iterator();
    while (firstIterator.hasNext()) {
      T firstItem = firstIterator.next();
      T secondItem = secondIterator.next();
      assertThat(firstItem).isEqualTo(secondItem);
    }
  }
}

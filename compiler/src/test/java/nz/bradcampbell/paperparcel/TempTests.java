package nz.bradcampbell.paperparcel;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class TempTests {

  @Test public void listOfParcelableTypesTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.List;",
            "@PaperParcel",
            "public final class Test {",
            "  private final List<Integer> child;",
            "  public Test(List<Integer> child) {",
            "    this.child = child;",
            "  }",
            "  public List<Integer> getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString("test/Test$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import java.lang.Integer;",
            "import java.lang.Override;",
            "import java.util.List;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ListAdapter;",
            "public final class Test$$Wrapper implements ParcelableWrapper<Test> {",
            "  public static final Parcelable.Creator<Test$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Test$$Wrapper>() {",
            "    @Override public Test$$Wrapper createFromParcel(Parcel in) {",
            "      IntegerAdapter integerAdapter = new IntegerAdapter();",
            "      ListAdapter<Integer> integerListAdapter = ",
            "          new ListAdapter<Integer>(integerAdapter);",
            "      List<Integer> child = integerListAdapter.readFromParcel(in);",
            "      Test data = new Test(child);",
            "      return new Test$$Wrapper(data);",
            "    }",
            "    @Override public Test$$Wrapper[] newArray(int size) {",
            "      return new Test$$Wrapper[size];",
            "    }",
            "  };",
            "  private final Test data;",
            "  public Test$$Wrapper(Test data) {",
            "    this.data = data;",
            "  }",
            "  @Override",
            "  public Test get() {",
            "    return this.data;",
            "  }",
            "  @Override",
            "  public int describeContents() {",
            "    return 0;",
            "  }",
            "  @Override",
            "  public void writeToParcel(Parcel dest, int flags) {",
            "    IntegerAdapter integerAdapter = new IntegerAdapter();",
            "    ListAdapter<Integer> integerListAdapter = ",
            "        new ListAdapter<Integer>(integerAdapter);",
            "    integerListAdapter.writeToParcel(this.data.getChild(), dest, flags);",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }
}

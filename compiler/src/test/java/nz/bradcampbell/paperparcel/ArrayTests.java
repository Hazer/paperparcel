package nz.bradcampbell.paperparcel;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import java.util.Arrays;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class ArrayTests {

  @Test public void basicArrayTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test {",
            "  private final Boolean[] child;",
            "  public Test(Boolean[] child) {",
            "    this.child = child;",
            "  }",
            "  public Boolean[] getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString("test/Test$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import java.lang.Boolean;",
            "import java.lang.Override;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "import nz.bradcampbell.paperparcel.typeadapters.ArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.BooleanAdapter;",
            "public final class Test$$Wrapper implements ParcelableWrapper<Test> {",
            "  public static final Parcelable.Creator<Test$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Test$$Wrapper>() {",
            "    @Override public Test$$Wrapper createFromParcel(Parcel in) {",
            "      BooleanAdapter booleanAdapter = new BooleanAdapter();",
            "      ArrayAdapter<Boolean> booleanArrayAdapter = new ArrayAdapter<Boolean>(booleanAdapter);",
            "      Boolean[] child = booleanArrayAdapter.readFromParcel(in);",
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
            "  @Override public Test get() {",
            "    return this.data;",
            "  }",
            "  @Override public int describeContents() {",
            "    return 0;",
            "  }",
            "  @Override public void writeToParcel(Parcel dest, int flags) {",
            "    BooleanAdapter booleanAdapter = new BooleanAdapter();",
            "    ArrayAdapter<Boolean> booleanArrayAdapter = new ArrayAdapter<Boolean>(booleanAdapter);",
            "    booleanArrayAdapter.writeToParcel(this.data.getChild(), dest, flags);",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void multiDimensionalArrayTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test {",
            "  private final Boolean[][] child;",
            "  public Test(Boolean[][] child) {",
            "    this.child = child;",
            "  }",
            "  public Boolean[][] getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString("test/Test$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import java.lang.Boolean;",
            "import java.lang.Override;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "import nz.bradcampbell.paperparcel.typeadapters.ArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.BooleanAdapter;",
            "public final class Test$$Wrapper implements ParcelableWrapper<Test> {",
            "  public static final Parcelable.Creator<Test$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Test$$Wrapper>() {",
            "    @Override public Test$$Wrapper createFromParcel(Parcel in) {",
            "      BooleanAdapter booleanAdapter = new BooleanAdapter();",
            "      ArrayAdapter<Boolean> booleanArrayAdapter = new ArrayAdapter<Boolean>(booleanAdapter);",
            "      ArrayAdapter<Boolean[]> booleanArrayArrayAdapter = new ArrayAdapter<Boolean[]>(booleanArrayAdapter);",
            "      Boolean[][] child = booleanArrayArrayAdapter.readFromParcel(in);",
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
            "  @Override public Test get() {",
            "    return this.data;",
            "  }",
            "  @Override public int describeContents() {",
            "    return 0;",
            "  }",
            "  @Override public void writeToParcel(Parcel dest, int flags) {",
            "    BooleanAdapter booleanAdapter = new BooleanAdapter();",
            "    ArrayAdapter<Boolean> booleanArrayAdapter = new ArrayAdapter<Boolean>(booleanAdapter);",
            "    ArrayAdapter<Boolean[]> booleanArrayArrayAdapter = new ArrayAdapter<Boolean[]>(booleanArrayAdapter);",
            "    booleanArrayArrayAdapter.writeToParcel(this.data.getChild(), dest, flags);",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void arrayWithGenericTypeTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.List;",
            "@PaperParcel",
            "public final class Test {",
            "  private final List<Integer>[] child;",
            "  public Test(List<Integer>[] child) {",
            "    this.child = child;",
            "  }",
            "  public List<Integer>[] getChild() {",
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
            "import nz.bradcampbell.paperparcel.typeadapters.ArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ListAdapter;",
            "public final class Test$$Wrapper implements ParcelableWrapper<Test> {",
            "  public static final Parcelable.Creator<Test$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Test$$Wrapper>() {",
            "    @Override public Test$$Wrapper createFromParcel(Parcel in) {",
            "      IntegerAdapter integerAdapter = new IntegerAdapter();",
            "      ListAdapter<Integer> integerListAdapter = new ListAdapter<Integer>(integerAdapter);",
            "      ArrayAdapter<List<Integer>> integerListArrayAdapter = new ArrayAdapter<List<Integer>>(integerListAdapter);",
            "      List<Integer>[] child = integerListArrayAdapter.readFromParcel(in);",
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
            "  @Override public Test get() {",
            "    return this.data;",
            "  }",
            "  @Override public int describeContents() {",
            "    return 0;",
            "  }",
            "  @Override public void writeToParcel(Parcel dest, int flags) {",
            "    IntegerAdapter integerAdapter = new IntegerAdapter();",
            "    ListAdapter<Integer> integerListAdapter = new ListAdapter<Integer>(integerAdapter);",
            "    ArrayAdapter<List<Integer>> integerListArrayAdapter = new ArrayAdapter<List<Integer>>(integerListAdapter);",
            "    integerListArrayAdapter.writeToParcel(this.data.getChild(), dest, flags);",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void overriddenArrayAdapterTypeTest() {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test {",
            "  private final Boolean[] child;",
            "  public Test(Boolean[] child) {",
            "    this.child = child;",
            "  }",
            "  public Boolean[] getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject typeAdapter =
        JavaFileObjects.forSourceString("test.CustomBooleanArrayTypeAdapter", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.DefaultAdapter;",
            "import nz.bradcampbell.paperparcel.TypeAdapter;",
            "import java.util.Date;",
            "import android.os.Parcel;",
            "@DefaultAdapter",
            "public class CustomBooleanArrayTypeAdapter implements TypeAdapter<Boolean[]> {",
            "  public Boolean[] readFromParcel(Parcel in) {",
            "    return null;",
            "  }",
            "  public void writeToParcel(Boolean[] value, Parcel dest, int flags) {",
            "  }",
            "  public Boolean[][] newArray(int length) {",
            "    return null;",
            "  }",
            "}"
        ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString("test/Test$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import java.lang.Boolean;",
            "import java.lang.Override;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "public final class Test$$Wrapper implements ParcelableWrapper<Test> {",
            "  public static final Parcelable.Creator<Test$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Test$$Wrapper>() {",
            "    @Override public Test$$Wrapper createFromParcel(Parcel in) {",
            "      CustomBooleanArrayTypeAdapter customBooleanArrayTypeAdapter = new CustomBooleanArrayTypeAdapter();",
            "      Boolean[] child = customBooleanArrayTypeAdapter.readFromParcel(in);",
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
            "  @Override public Test get() {",
            "    return this.data;",
            "  }",
            "  @Override public int describeContents() {",
            "    return 0;",
            "  }",
            "  @Override public void writeToParcel(Parcel dest, int flags) {",
            "    CustomBooleanArrayTypeAdapter customBooleanArrayTypeAdapter = new CustomBooleanArrayTypeAdapter();",
            "    customBooleanArrayTypeAdapter.writeToParcel(this.data.getChild(), dest, flags);",
            "  }",
            "}"
        ));

    assertAbout(javaSources()).that(Arrays.asList(source, typeAdapter))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }
}

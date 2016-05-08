package nz.bradcampbell.paperparcel;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;
import static java.util.Arrays.asList;

public class TypeAdapterTests {

  @Test public void classScopedTypeAdapterTest() throws Exception {
    JavaFileObject yes =
        JavaFileObjects.forSourceString("test.Yes", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.TypeAdapters;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "@PaperParcel",
            "@TypeAdapters(CustomDateTypeAdapter.class)",
            "public final class Yes {",
            "  private final Date child;",
            "  public Yes(Date child) {",
            "    this.child = child;",
            "  }",
            "  public Date getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject no =
        JavaFileObjects.forSourceString("test.No", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "@PaperParcel",
            "public final class No {",
            "  private final Date child;",
            "  public No(Date child) {",
            "    this.child = child;",
            "  }",
            "  public Date getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject typeAdapter =
        JavaFileObjects.forSourceString("test.CustomDateTypeAdapter", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.TypeAdapter;",
            "import java.util.Date;",
            "import android.os.Parcel;",
            "public class CustomDateTypeAdapter implements TypeAdapter<Date> {",
            "  public Date readFromParcel(Parcel in) {",
            "    return new Date(in.readLong());",
            "  }",
            "  public void writeToParcel(Date value, Parcel dest, int flags) {",
            "    dest.writeLong(value.getTime());",
            "  }",
            "  public Date[] newArray(int length) {",
            "    return new Date[length];",
            "  }",
            "}"
        ));

    JavaFileObject yesExpectedSource =
        JavaFileObjects.forSourceString("test/Yes$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import java.lang.Override;",
            "import java.util.Date;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "public final class Yes$$Wrapper implements ParcelableWrapper<Yes> {",
            "  public static final Parcelable.Creator<Yes$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Yes$$Wrapper>() {",
            "    @Override public Yes$$Wrapper createFromParcel(Parcel in) {",
            "      CustomDateTypeAdapter customDateTypeAdapter = new CustomDateTypeAdapter();",
            "      Date child = customDateTypeAdapter.readFromParcel(in);",
            "      Yes data = new Yes(child);",
            "      return new Yes$$Wrapper(data);",
            "    }",
            "    @Override public Yes$$Wrapper[] newArray(int size) {",
            "      return new Yes$$Wrapper[size];",
            "    }",
            "  };",
            "  private final Yes data;",
            "  public Yes$$Wrapper(Yes data) {",
            "    this.data = data;",
            "  }",
            "  @Override public Yes get() {",
            "    return this.data;",
            "  }",
            "  @Override public int describeContents() {",
            "    return 0;",
            "  }",
            "  @Override public void writeToParcel(Parcel dest, int flags) {",
            "    CustomDateTypeAdapter customDateTypeAdapter = new CustomDateTypeAdapter();",
            "    customDateTypeAdapter.writeToParcel(this.data.getChild(), dest, flags);",
            "  }",
            "}"
        ));

    JavaFileObject noExpectedSource =
        JavaFileObjects.forSourceString("test/No$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import java.lang.Override;",
            "import java.util.Date;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "import nz.bradcampbell.paperparcel.typeadapters.DateAdapter;",
            "public final class No$$Wrapper implements ParcelableWrapper<No> {",
            "  public static final Parcelable.Creator<No$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<No$$Wrapper>() {",
            "    @Override public No$$Wrapper createFromParcel(Parcel in) {",
            "      DateAdapter dateAdapter = new DateAdapter();",
            "      Date child = dateAdapter.readFromParcel(in);",
            "      No data = new No(child);",
            "      return new No$$Wrapper(data);",
            "    }",
            "    @Override public No$$Wrapper[] newArray(int size) {",
            "      return new No$$Wrapper[size];",
            "    }",
            "  };",
            "  private final No data;",
            "  public No$$Wrapper(No data) {",
            "    this.data = data;",
            "  }",
            "  @Override public No get() {",
            "    return this.data;",
            "  }",
            "  @Override public int describeContents() {",
            "    return 0;",
            "  }",
            "  @Override public void writeToParcel(Parcel dest, int flags) {",
            "    DateAdapter dateAdapter = new DateAdapter();",
            "    dateAdapter.writeToParcel(this.data.getChild(), dest, flags);",
            "  }",
            "}"
        ));

    assertAbout(javaSources()).that(asList(yes, no, typeAdapter))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(yesExpectedSource, noExpectedSource);
  }

  @Test public void defaultTypeAdapterTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "@PaperParcel",
            "public final class Test {",
            "  private final Date child;",
            "  public Test(Date child) {",
            "    this.child = child;",
            "  }",
            "  public Date getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject typeAdapter =
        JavaFileObjects.forSourceString("test.CustomDateTypeAdapter", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.DefaultAdapter;",
            "import nz.bradcampbell.paperparcel.TypeAdapter;",
            "import java.util.Date;",
            "import android.os.Parcel;",
            "@DefaultAdapter",
            "public class CustomDateTypeAdapter implements TypeAdapter<Date> {",
            "  public Date readFromParcel(Parcel in) {",
            "    return new Date(in.readLong());",
            "  }",
            "  public void writeToParcel(Date value, Parcel dest, int flags) {",
            "    dest.writeLong(value.getTime());",
            "  }",
            "  public Date[] newArray(int length) {",
            "    return new Date[length];",
            "  }",
            "}"
        ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString("test/Test$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import java.lang.Override;",
            "import java.util.Date;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "public final class Test$$Wrapper implements ParcelableWrapper<Test> {",
            "  public static final Parcelable.Creator<Test$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Test$$Wrapper>() {",
            "    @Override public Test$$Wrapper createFromParcel(Parcel in) {",
            "      CustomDateTypeAdapter customDateTypeAdapter = new CustomDateTypeAdapter();",
            "      Date child = customDateTypeAdapter.readFromParcel(in);",
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
            "    CustomDateTypeAdapter customDateTypeAdapter = new CustomDateTypeAdapter();",
            "    customDateTypeAdapter.writeToParcel(this.data.getChild(), dest, flags);",
            "  }",
            "}"
        ));

    assertAbout(javaSources()).that(asList(source, typeAdapter))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void multipleDeclarationsOfTheSameTypeAdapterDoNoDuplicateTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.TypeAdapters;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "@PaperParcel",
            "public final class Test {",
            "  @TypeAdapters(CustomDateTypeAdapter.class) public Date child1;",
            "  @TypeAdapters(CustomDateTypeAdapter.class) public Date child2;",
            "}"
        ));

    JavaFileObject typeAdapter =
        JavaFileObjects.forSourceString("test.CustomDateTypeAdapter", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.TypeAdapter;",
            "import java.util.Date;",
            "import android.os.Parcel;",
            "public class CustomDateTypeAdapter implements TypeAdapter<Date> {",
            "  public Date readFromParcel(Parcel in) {",
            "    return new Date(in.readLong());",
            "  }",
            "  public void writeToParcel(Date value, Parcel dest, int flags) {",
            "    dest.writeLong(value.getTime());",
            "  }",
            "  public Date[] newArray(int length) {",
            "    return new Date[length];",
            "  }",
            "}"
        ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString("test/Test$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import java.lang.Override;",
            "import java.util.Date;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "public final class Test$$Wrapper implements ParcelableWrapper<Test> {",
            "  public static final Parcelable.Creator<Test$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Test$$Wrapper>() {",
            "    @Override public Test$$Wrapper createFromParcel(Parcel in) {",
            "      CustomDateTypeAdapter customDateTypeAdapter = new CustomDateTypeAdapter();",
            "      Date child1 = customDateTypeAdapter.readFromParcel(in);",
            "      Date child2 = customDateTypeAdapter.readFromParcel(in);",
            "      Test data = new Test();",
            "      data.child1 = child1;",
            "      data.child2 = child2;",
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
            "    CustomDateTypeAdapter customDateTypeAdapter = new CustomDateTypeAdapter();",
            "    customDateTypeAdapter.writeToParcel(this.data.child1, dest, flags);",
            "    customDateTypeAdapter.writeToParcel(this.data.child2, dest, flags);",
            "  }",
            "}"
        ));

    assertAbout(javaSources()).that(asList(source, typeAdapter))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void singleVariableScopedTypeAdapterTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.TypeAdapters;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "@PaperParcel",
            "public final class Test {",
            "  @TypeAdapters(CustomDateTypeAdapter.class) public Date child1;",
            "  public Date child2;",
            "}"
        ));

    JavaFileObject typeAdapter =
        JavaFileObjects.forSourceString("test.CustomDateTypeAdapter", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.TypeAdapter;",
            "import java.util.Date;",
            "import android.os.Parcel;",
            "public class CustomDateTypeAdapter implements TypeAdapter<Date> {",
            "  public Date readFromParcel(Parcel in) {",
            "    return new Date(in.readLong());",
            "  }",
            "  public void writeToParcel(Date value, Parcel dest, int flags) {",
            "    dest.writeLong(value.getTime());",
            "  }",
            "  public Date[] newArray(int length) {",
            "    return new Date[length];",
            "  }",
            "}"
        ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString("test/Test$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import java.lang.Override;",
            "import java.util.Date;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "import nz.bradcampbell.paperparcel.typeadapters.DateAdapter;",
            "public final class Test$$Wrapper implements ParcelableWrapper<Test> {",
            "  public static final Parcelable.Creator<Test$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Test$$Wrapper>() {",
            "    @Override public Test$$Wrapper createFromParcel(Parcel in) {",
            "      CustomDateTypeAdapter customDateTypeAdapter = new CustomDateTypeAdapter();",
            "      DateAdapter dateAdapter = new DateAdapter();",
            "      Date child1 = customDateTypeAdapter.readFromParcel(in);",
            "      Date child2 = dateAdapter.readFromParcel(in);",
            "      Test data = new Test();",
            "      data.child1 = child1;",
            "      data.child2 = child2;",
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
            "    CustomDateTypeAdapter customDateTypeAdapter = new CustomDateTypeAdapter();",
            "    DateAdapter dateAdapter = new DateAdapter();",
            "    customDateTypeAdapter.writeToParcel(this.data.child1, dest, flags);",
            "    dateAdapter.writeToParcel(this.data.child2, dest, flags);",
            "  }",
            "}"
        ));

    assertAbout(javaSources()).that(asList(source, typeAdapter))
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void failIfTypeAdapterRequiresArbitraryTypeArguments() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import test.DateTypeAdapter;",
            "import nz.bradcampbell.paperparcel.TypeAdapters;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "@PaperParcel",
            "public final class Test {",
            "  private final Date child;",
            "  public Test(Date child) {",
            "    this.child = child;",
            "  }",
            "  public Date getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject typeAdapter =
        JavaFileObjects.forSourceString("test.DateTypeAdapter", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.DefaultAdapter;",
            "import nz.bradcampbell.paperparcel.TypeAdapter;",
            "import java.util.Date;",
            "import android.os.Parcel;",
            "@DefaultAdapter",
            "public class DateTypeAdapter<T> implements TypeAdapter<Date> {",
            "  public Date readFromParcel(Parcel in) {",
            "    return new Date(in.readLong());",
            "  }",
            "  public void writeToParcel(Date value, Parcel dest, int flags) {",
            "    dest.writeLong(value.getTime());",
            "  }",
            "  public Date[] newArray(int length) {",
            "    return new Date[length];",
            "  }",
            "}"
        ));

    assertAbout(javaSources()).that(asList(source, typeAdapter))
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel does not know what to do with the type argument T in "
            + "test.DateTypeAdapter when processing java.util.Date")
        .in(typeAdapter)
        .onLine(7);
  }

  @Test public void failIfConstructorHasNonSupportedParameter() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.TypeAdapters;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "@PaperParcel",
            "public final class Test {",
            "  private final Date child;",
            "  public Test(Date child) {",
            "    this.child = child;",
            "  }",
            "  public Date getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject typeAdapter =
        JavaFileObjects.forSourceString("test.CustomDateTypeAdapter", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.DefaultAdapter;",
            "import nz.bradcampbell.paperparcel.TypeAdapter;",
            "import java.util.Date;",
            "import android.os.Parcel;",
            "@DefaultAdapter",
            "public class CustomDateTypeAdapter implements TypeAdapter<Date> {",
            "  public CustomDateTypeAdapter(int fail) {",
            "  }",
            "  public Date readFromParcel(Parcel in) {",
            "    return new Date(in.readLong());",
            "  }",
            "  public void writeToParcel(Date value, Parcel dest, int flags) {",
            "    dest.writeLong(value.getTime());",
            "  }",
            "  public Date[] newArray(int length) {",
            "    return new Date[length];",
            "  }",
            "}"
        ));

    assertAbout(javaSources()).that(asList(source, typeAdapter))
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("TypeAdapter constructor arguments may only be other TypeAdapters")
        .in(typeAdapter)
        .onLine(8);
  }

  @Test public void failIfConstructorHasRawTypeParameter() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.TypeAdapters;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.List;",
            "import java.util.Date;",
            "@PaperParcel",
            "public final class Test {",
            "  private final List<Date> child;",
            "  public Test(List<Date> child) {",
            "    this.child = child;",
            "  }",
            "  public List<Date> getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    JavaFileObject typeAdapter =
        JavaFileObjects.forSourceString("test.ListTypeAdapter", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.DefaultAdapter;",
            "import nz.bradcampbell.paperparcel.TypeAdapter;",
            "import java.util.List;",
            "import android.os.Parcel;",
            "@DefaultAdapter",
            "public class ListTypeAdapter<T> implements TypeAdapter<List<T>> {",
            "  public ListTypeAdapter(TypeAdapter ta) {",
            "  }",
            "  public List<T> readFromParcel(Parcel in) {",
            "    return null;",
            "  }",
            "  public void writeToParcel(List<T> value, Parcel dest, int flags) {",
            "  }",
            "  public List<T>[] newArray(int length) {",
            "    return new List[length];",
            "  }",
            "}"
        ));

    assertAbout(javaSources()).that(asList(source, typeAdapter))
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel cannot process constructor found in test.ListTypeAdapter "
            + "because \"nz.bradcampbell.paperparcel.TypeAdapter\" can't be declared as a raw type.")
        .in(typeAdapter)
        .onLine(8);
  }
}

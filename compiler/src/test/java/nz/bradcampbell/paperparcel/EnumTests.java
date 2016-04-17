package nz.bradcampbell.paperparcel;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class EnumTests {

  @Test public void enumTest() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n')
        .join("package test;", "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.List;", "@PaperParcel", "public final class Test {",
            "private final TestEnum child;", "public Test(TestEnum child) {", "this.child = child;",
            "}", "enum TestEnum {", "ONE;", "}", "public TestEnum getChild() {",
            "return this.child;", "}", "}"));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/TestParcel",
        Joiner.on('\n')
            .join("package test;", "import android.os.Parcel;", "import android.os.Parcelable;",
                "import java.lang.Enum;", "import java.lang.Override;",
                "import nz.bradcampbell.paperparcel.TypedParcelable;",
                "public final class TestParcel implements TypedParcelable<Test> {",
                "public static final Parcelable.Creator<TestParcel> CREATOR = new Parcelable.Creator<TestParcel>() {",
                "@Override public TestParcel createFromParcel(Parcel in) {",
                "Test.TestEnum outChild = null;", "if (in.readInt() == 0) {",
                "outChild = Enum.valueOf(Test.TestEnum.class, in.readString());", "}",
                "Test data = new Test(outChild);", "return new TestParcel(data);", "}",
                "@Override public TestParcel[] newArray(int size) {",
                "return new TestParcel[size];", "}", "};", "private final Test data;",
                "public TestParcel(Test data) {", "this.data = data;", "}",
                "@Override public Test get() {", "return this.data;", "}", "@Override",
                "public int describeContents() {", "return 0;", "}", "@Override",
                "public void writeToParcel(Parcel dest, int flags) {",
                "Test.TestEnum child = this.data.getChild();", "if (child == null) {",
                "dest.writeInt(1);", "} else {", "dest.writeInt(0);",
                "dest.writeString(child.name());", "}", "}", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }
}

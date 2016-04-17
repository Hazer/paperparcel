package nz.bradcampbell.paperparcel;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class IntTests {

  @Test public void primitiveIntTest() throws Exception {
    JavaFileObject source = JavaFileObjects.forSourceString("test.Test", Joiner.on('\n')
        .join("package test;", "import nz.bradcampbell.paperparcel.PaperParcel;", "@PaperParcel",
            "public final class Test {", "private final int child;", "public Test(int child) {",
            "this.child = child;", "}", "public int getChild() {", "return this.child;", "}", "}"));

    JavaFileObject expectedSource = JavaFileObjects.forSourceString("test/TestParcel",
        Joiner.on('\n')
            .join("package test;", "import android.os.Parcel;", "import android.os.Parcelable;",
                "import java.lang.Override;", "import nz.bradcampbell.paperparcel.TypedParcelable;",
                "public final class TestParcel implements TypedParcelable<Test> {",
                "public static final Parcelable.Creator<TestParcel> CREATOR = new Parcelable.Creator<TestParcel>() {",
                "@Override public TestParcel createFromParcel(Parcel in) {",
                "int child = in.readInt();", "Test data = new Test(child);",
                "return new TestParcel(data);", "}",
                "@Override public TestParcel[] newArray(int size) {",
                "return new TestParcel[size];", "}", "};", "private final Test data;",
                "public TestParcel(Test data) {", "this.data = data;", "}",
                "@Override public Test get() {", "return this.data;", "}",
                "@Override public int describeContents() {", "return 0;", "}",
                "@Override public void writeToParcel(Parcel dest, int flags) {",
                "int child = this.data.getChild();", "dest.writeInt(child);", "}", "}"));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }
}

package nz.bradcampbell.paperparcel;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import java.util.Arrays;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class PaperParcelProcessorTests {

  @Test public void allBuiltInAdaptersTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import android.os.Bundle;",
            "import android.os.Parcelable;",
            "import android.os.PersistableBundle;",
            "import android.util.SparseArray;",
            "import android.util.SparseBooleanArray;",
            "import android.util.Size;",
            "import android.util.SizeF;",
            "import java.util.List;",
            "import java.util.Map;",
            "import java.util.Set;",
            "import java.util.Queue;",
            "import java.math.BigInteger;",
            "import java.math.BigDecimal;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test {",
            "  public boolean aa;",
            "  public Boolean ab;",
            "  public byte ac;",
            "  public Byte ad;",
            "  public Bundle ae;",
            "  public CharSequence af;",
            "  public List<Integer> ag;",
            "  public char ah;",
            "  public Character ai;",
            "  public double aj;",
            "  public Double ak;",
            "  public float al;",
            "  public Float am;",
            "  public int an;",
            "  public Integer ao;",
            "  public long ap;",
            "  public Long aq;",
            "  public Map<Integer, Integer> ar;",
            "  public Parcelable as;",
            "  public PersistableBundle at;",
            "  public Queue<Integer> au;",
            "  public Set<Integer> av;",
            "  public short aw;",
            "  public Short ax;",
            "  public SizeF ay;",
            "  public Size az;",
            "  public SparseArray<Integer> ba;",
            "  public String bb;",
            "  public boolean[] bc;",
            "  public byte[] bd;",
            "  public char[] be;",
            "  public double[] bf;",
            "  public float[] bg;",
            "  public int[] bh;",
            "  public long[] bi;",
            "  public short[] bj;",
            "  public String[] bk;",
            "  public BigInteger bl;",
            "  public BigDecimal bm;",
            "  public SparseBooleanArray bn;",
            "}"
        ));

    JavaFileObject expectedSource =
        JavaFileObjects.forSourceString("test/Test$$Wrapper", Joiner.on('\n').join(
            "package test;",
            "import android.os.Bundle;",
            "import android.os.Parcel;",
            "import android.os.Parcelable;",
            "import android.os.PersistableBundle;",
            "import android.util.Size;",
            "import android.util.SizeF;",
            "import android.util.SparseArray;",
            "import android.util.SparseBooleanArray;",
            "import java.lang.Boolean;",
            "import java.lang.Byte;",
            "import java.lang.CharSequence;",
            "import java.lang.Character;",
            "import java.lang.Double;",
            "import java.lang.Float;",
            "import java.lang.Integer;",
            "import java.lang.Long;",
            "import java.lang.Override;",
            "import java.lang.Short;",
            "import java.lang.String;",
            "import java.math.BigDecimal;",
            "import java.math.BigInteger;",
            "import java.util.List;",
            "import java.util.Map;",
            "import java.util.Queue;",
            "import java.util.Set;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "import nz.bradcampbell.paperparcel.typeadapters.BigDecimalAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.BigIntegerAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.BooleanAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.BooleanArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.BundleAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ByteAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ByteArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.CharArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.CharSequenceAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.CharacterAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.DoubleAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.DoubleArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.FloatAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.FloatArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.IntArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ListAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.LongAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.LongArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.MapAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ParcelableAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.PersistableBundleAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.QueueAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.SetAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ShortAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ShortArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.SizeAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.SizeFAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.SparseArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.SparseBooleanArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.StringAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.StringArrayAdapter;",
            "public final class Test$$Wrapper implements ParcelableWrapper<Test> {",
            "  public static final Parcelable.Creator<Test$$Wrapper> CREATOR = ",
            "      new Parcelable.Creator<Test$$Wrapper>() {",
            "    @Override public Test$$Wrapper createFromParcel(Parcel in) {",
            "      BooleanAdapter booleanAdapter = new BooleanAdapter();",
            "      ByteAdapter byteAdapter = new ByteAdapter();",
            "      BundleAdapter bundleAdapter = new BundleAdapter();",
            "      CharSequenceAdapter charSequenceAdapter = new CharSequenceAdapter();",
            "      IntegerAdapter integerAdapter = new IntegerAdapter();",
            "      ListAdapter<Integer> integerListAdapter = new ListAdapter<Integer>(integerAdapter);",
            "      CharacterAdapter characterAdapter = new CharacterAdapter();",
            "      DoubleAdapter doubleAdapter = new DoubleAdapter();",
            "      FloatAdapter floatAdapter = new FloatAdapter();",
            "      LongAdapter longAdapter = new LongAdapter();",
            "      MapAdapter<Integer, Integer> integerIntegerMapAdapter = new MapAdapter<Integer, Integer>(integerAdapter, integerAdapter);",
            "      ParcelableAdapter parcelableAdapter = new ParcelableAdapter();",
            "      PersistableBundleAdapter persistableBundleAdapter = new PersistableBundleAdapter();",
            "      QueueAdapter<Integer> integerQueueAdapter = new QueueAdapter<Integer>(integerAdapter);",
            "      SetAdapter<Integer> integerSetAdapter = new SetAdapter<Integer>(integerAdapter);",
            "      ShortAdapter shortAdapter = new ShortAdapter();",
            "      SizeFAdapter sizeFAdapter = new SizeFAdapter();",
            "      SizeAdapter sizeAdapter = new SizeAdapter();",
            "      SparseArrayAdapter<Integer> integerSparseArrayAdapter = new SparseArrayAdapter<Integer>(integerAdapter);",
            "      StringAdapter stringAdapter = new StringAdapter();",
            "      BooleanArrayAdapter booleanArrayAdapter = new BooleanArrayAdapter();",
            "      ByteArrayAdapter byteArrayAdapter = new ByteArrayAdapter();",
            "      CharArrayAdapter charArrayAdapter = new CharArrayAdapter();",
            "      DoubleArrayAdapter doubleArrayAdapter = new DoubleArrayAdapter();",
            "      FloatArrayAdapter floatArrayAdapter = new FloatArrayAdapter();",
            "      IntArrayAdapter intArrayAdapter = new IntArrayAdapter();",
            "      LongArrayAdapter longArrayAdapter = new LongArrayAdapter();",
            "      ShortArrayAdapter shortArrayAdapter = new ShortArrayAdapter();",
            "      StringArrayAdapter stringArrayAdapter = new StringArrayAdapter();",
            "      BigIntegerAdapter bigIntegerAdapter = new BigIntegerAdapter(byteArrayAdapter);",
            "      BigDecimalAdapter bigDecimalAdapter = new BigDecimalAdapter(bigIntegerAdapter);",
            "      SparseBooleanArrayAdapter sparseBooleanArrayAdapter = new SparseBooleanArrayAdapter();",
            "      boolean aa = in.readInt() == 1;",
            "      Boolean ab = booleanAdapter.readFromParcel(in);",
            "      byte ac = in.readByte();",
            "      Byte ad = byteAdapter.readFromParcel(in);",
            "      Bundle ae = bundleAdapter.readFromParcel(in);",
            "      CharSequence af = charSequenceAdapter.readFromParcel(in);",
            "      List<Integer> ag = integerListAdapter.readFromParcel(in);",
            "      char ah = (char) in.readInt();",
            "      Character ai = characterAdapter.readFromParcel(in);",
            "      double aj = in.readDouble();",
            "      Double ak = doubleAdapter.readFromParcel(in);",
            "      float al = in.readFloat();",
            "      Float am = floatAdapter.readFromParcel(in);",
            "      int an = in.readInt();",
            "      Integer ao = integerAdapter.readFromParcel(in);",
            "      long ap = in.readLong();",
            "      Long aq = longAdapter.readFromParcel(in);",
            "      Map<Integer, Integer> ar = integerIntegerMapAdapter.readFromParcel(in);",
            "      Parcelable as = parcelableAdapter.readFromParcel(in);",
            "      PersistableBundle at = persistableBundleAdapter.readFromParcel(in);",
            "      Queue<Integer> au = integerQueueAdapter.readFromParcel(in);",
            "      Set<Integer> av = integerSetAdapter.readFromParcel(in);",
            "      short aw = (short) in.readInt();",
            "      Short ax = shortAdapter.readFromParcel(in);",
            "      SizeF ay = sizeFAdapter.readFromParcel(in);",
            "      Size az = sizeAdapter.readFromParcel(in);",
            "      SparseArray<Integer> ba = integerSparseArrayAdapter.readFromParcel(in);",
            "      String bb = stringAdapter.readFromParcel(in);",
            "      boolean[] bc = booleanArrayAdapter.readFromParcel(in);",
            "      byte[] bd = byteArrayAdapter.readFromParcel(in);",
            "      char[] be = charArrayAdapter.readFromParcel(in);",
            "      double[] bf = doubleArrayAdapter.readFromParcel(in);",
            "      float[] bg = floatArrayAdapter.readFromParcel(in);",
            "      int[] bh = intArrayAdapter.readFromParcel(in);",
            "      long[] bi = longArrayAdapter.readFromParcel(in);",
            "      short[] bj = shortArrayAdapter.readFromParcel(in);",
            "      String[] bk = stringArrayAdapter.readFromParcel(in);",
            "      BigInteger bl = bigIntegerAdapter.readFromParcel(in);",
            "      BigDecimal bm = bigDecimalAdapter.readFromParcel(in);",
            "      SparseBooleanArray bn = sparseBooleanArrayAdapter.readFromParcel(in);",
            "      Test data = new Test();",
            "      data.aa = aa;",
            "      data.ab = ab;",
            "      data.ac = ac;",
            "      data.ad = ad;",
            "      data.ae = ae;",
            "      data.af = af;",
            "      data.ag = ag;",
            "      data.ah = ah;",
            "      data.ai = ai;",
            "      data.aj = aj;",
            "      data.ak = ak;",
            "      data.al = al;",
            "      data.am = am;",
            "      data.an = an;",
            "      data.ao = ao;",
            "      data.ap = ap;",
            "      data.aq = aq;",
            "      data.ar = ar;",
            "      data.as = as;",
            "      data.at = at;",
            "      data.au = au;",
            "      data.av = av;",
            "      data.aw = aw;",
            "      data.ax = ax;",
            "      data.ay = ay;",
            "      data.az = az;",
            "      data.ba = ba;",
            "      data.bb = bb;",
            "      data.bc = bc;",
            "      data.bd = bd;",
            "      data.be = be;",
            "      data.bf = bf;",
            "      data.bg = bg;",
            "      data.bh = bh;",
            "      data.bi = bi;",
            "      data.bj = bj;",
            "      data.bk = bk;",
            "      data.bl = bl;",
            "      data.bm = bm;",
            "      data.bn = bn;",
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
            "    BooleanAdapter booleanAdapter = new BooleanAdapter();",
            "    ByteAdapter byteAdapter = new ByteAdapter();",
            "    BundleAdapter bundleAdapter = new BundleAdapter();",
            "    CharSequenceAdapter charSequenceAdapter = new CharSequenceAdapter();",
            "    IntegerAdapter integerAdapter = new IntegerAdapter();",
            "    ListAdapter<Integer> integerListAdapter = new ListAdapter<Integer>(integerAdapter);",
            "    CharacterAdapter characterAdapter = new CharacterAdapter();",
            "    DoubleAdapter doubleAdapter = new DoubleAdapter();",
            "    FloatAdapter floatAdapter = new FloatAdapter();",
            "    LongAdapter longAdapter = new LongAdapter();",
            "    MapAdapter<Integer, Integer> integerIntegerMapAdapter = new MapAdapter<Integer, Integer>(integerAdapter, integerAdapter);",
            "    ParcelableAdapter parcelableAdapter = new ParcelableAdapter();",
            "    PersistableBundleAdapter persistableBundleAdapter = new PersistableBundleAdapter();",
            "    QueueAdapter<Integer> integerQueueAdapter = new QueueAdapter<Integer>(integerAdapter);",
            "    SetAdapter<Integer> integerSetAdapter = new SetAdapter<Integer>(integerAdapter);",
            "    ShortAdapter shortAdapter = new ShortAdapter();",
            "    SizeFAdapter sizeFAdapter = new SizeFAdapter();",
            "    SizeAdapter sizeAdapter = new SizeAdapter();",
            "    SparseArrayAdapter<Integer> integerSparseArrayAdapter = new SparseArrayAdapter<Integer>(integerAdapter);",
            "    StringAdapter stringAdapter = new StringAdapter();",
            "    BooleanArrayAdapter booleanArrayAdapter = new BooleanArrayAdapter();",
            "    ByteArrayAdapter byteArrayAdapter = new ByteArrayAdapter();",
            "    CharArrayAdapter charArrayAdapter = new CharArrayAdapter();",
            "    DoubleArrayAdapter doubleArrayAdapter = new DoubleArrayAdapter();",
            "    FloatArrayAdapter floatArrayAdapter = new FloatArrayAdapter();",
            "    IntArrayAdapter intArrayAdapter = new IntArrayAdapter();",
            "    LongArrayAdapter longArrayAdapter = new LongArrayAdapter();",
            "    ShortArrayAdapter shortArrayAdapter = new ShortArrayAdapter();",
            "    StringArrayAdapter stringArrayAdapter = new StringArrayAdapter();",
            "    BigIntegerAdapter bigIntegerAdapter = new BigIntegerAdapter(byteArrayAdapter);",
            "    BigDecimalAdapter bigDecimalAdapter = new BigDecimalAdapter(bigIntegerAdapter);",
            "    SparseBooleanArrayAdapter sparseBooleanArrayAdapter = new SparseBooleanArrayAdapter();",
            "    dest.writeInt(this.data.aa ? 1 : 0);",
            "    booleanAdapter.writeToParcel(this.data.ab, dest, flags);",
            "    dest.writeByte(this.data.ac);",
            "    byteAdapter.writeToParcel(this.data.ad, dest, flags);",
            "    bundleAdapter.writeToParcel(this.data.ae, dest, flags);",
            "    charSequenceAdapter.writeToParcel(this.data.af, dest, flags);",
            "    integerListAdapter.writeToParcel(this.data.ag, dest, flags);",
            "    dest.writeInt(this.data.ah);",
            "    characterAdapter.writeToParcel(this.data.ai, dest, flags);",
            "    dest.writeDouble(this.data.aj);",
            "    doubleAdapter.writeToParcel(this.data.ak, dest, flags);",
            "    dest.writeFloat(this.data.al);",
            "    floatAdapter.writeToParcel(this.data.am, dest, flags);",
            "    dest.writeInt(this.data.an);",
            "    integerAdapter.writeToParcel(this.data.ao, dest, flags);",
            "    dest.writeLong(this.data.ap);",
            "    longAdapter.writeToParcel(this.data.aq, dest, flags);",
            "    integerIntegerMapAdapter.writeToParcel(this.data.ar, dest, flags);",
            "    parcelableAdapter.writeToParcel(this.data.as, dest, flags);",
            "    persistableBundleAdapter.writeToParcel(this.data.at, dest, flags);",
            "    integerQueueAdapter.writeToParcel(this.data.au, dest, flags);",
            "    integerSetAdapter.writeToParcel(this.data.av, dest, flags);",
            "    dest.writeInt(this.data.aw);",
            "    shortAdapter.writeToParcel(this.data.ax, dest, flags);",
            "    sizeFAdapter.writeToParcel(this.data.ay, dest, flags);",
            "    sizeAdapter.writeToParcel(this.data.az, dest, flags);",
            "    integerSparseArrayAdapter.writeToParcel(this.data.ba, dest, flags);",
            "    stringAdapter.writeToParcel(this.data.bb, dest, flags);",
            "    booleanArrayAdapter.writeToParcel(this.data.bc, dest, flags);",
            "    byteArrayAdapter.writeToParcel(this.data.bd, dest, flags);",
            "    charArrayAdapter.writeToParcel(this.data.be, dest, flags);",
            "    doubleArrayAdapter.writeToParcel(this.data.bf, dest, flags);",
            "    floatArrayAdapter.writeToParcel(this.data.bg, dest, flags);",
            "    intArrayAdapter.writeToParcel(this.data.bh, dest, flags);",
            "    longArrayAdapter.writeToParcel(this.data.bi, dest, flags);",
            "    shortArrayAdapter.writeToParcel(this.data.bj, dest, flags);",
            "    stringArrayAdapter.writeToParcel(this.data.bk, dest, flags);",
            "    bigIntegerAdapter.writeToParcel(this.data.bl, dest, flags);",
            "    bigDecimalAdapter.writeToParcel(this.data.bm, dest, flags);",
            "    sparseBooleanArrayAdapter.writeToParcel(this.data.bn, dest, flags);",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .compilesWithoutError()
        .and()
        .generatesSources(expectedSource);
  }

  @Test public void failIfGetterHasAParameterTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test {",
            "  private final int child;",
            "  public Test(int child) {",
            "    this.child = child;",
            "  }",
            "  public int getChild(int x) {",
            "    return this.child;",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel cannot read from the field named \"child\" which was "
            + "found when processing test.Test. The field must either be non-private, or have a "
            + "getter method with no arguments and have one of the following names: [child, "
            + "isChild, hasChild, getChild]. Alternatively you can exclude the field by making it "
            + "static, transient, or using the ExcludeFields annotation on test.Test")
        .in(source)
        .onLine(5);
  }

  @Test public void failIfGetterHasWrongReturnTypeTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test {",
            "  private final int child;",
            "  public Test(int child) {",
            "    this.child = child;",
            "  }",
            "  public long getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel cannot read from the field named \"child\" which was "
            + "found when processing test.Test. The field must either be non-private, or have a "
            + "getter method with no arguments and have one of the following names: [child, "
            + "isChild, hasChild, getChild]. Alternatively you can exclude the field by making it "
            + "static, transient, or using the ExcludeFields annotation on test.Test")
        .in(source)
        .onLine(5);
  }

  @Test public void failIfGetterHasWrongNameTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test {",
            "  private final int child;",
            "  public Test(int child) {",
            "    this.child = child;",
            "  }",
            "  public int getKid() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel cannot read from the field named \"child\" which was "
            + "found when processing test.Test. The field must either be non-private, or have a "
            + "getter method with no arguments and have one of the following names: [child, "
            + "isChild, hasChild, getChild]. Alternatively you can exclude the field by making it "
            + "static, transient, or using the ExcludeFields annotation on test.Test")
        .in(source)
        .onLine(5);
  }

  @Test public void failIfSetterHasNoParametersTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test {",
            "  private final int child;",
            "  public int getChild() {",
            "    return this.child;",
            "  }",
            "  public void setChild() {",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel cannot write to the field named \"child\" which was "
            + "found when processing test.Test. The field must either be have a constructor "
            + "argument named child, be non-private, or have a setter method with one int "
            + "parameter and have one of the following names: [child, setChild]. Alternatively "
            + "you can exclude the field by making it static, transient, or using the "
            + "ExcludeFields annotation on test.Test")
        .in(source)
        .onLine(5);
  }

  @Test public void failIfConstructorArgHasWrongNameTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test {",
            "  private final int child;",
            "  public Test(int kid) {",
            "    this.child = kid;",
            "  }",
            "  public int getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel cannot write to the field named \"child\" which was "
            + "found when processing test.Test. The field must either be have a constructor "
            + "argument named child, be non-private, or have a setter method with one int "
            + "parameter and have one of the following names: [child, setChild]. Alternatively "
            + "you can exclude the field by making it static, transient, or using the "
            + "ExcludeFields annotation on test.Test")
        .in(source)
        .onLine(5);
  }

  @Test public void failIfPaperParcelClassIsGenericTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public final class Test<T> {",
            "  public T child;",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("@PaperParcel cannot be applied to a class with type parameters")
        .in(source)
        .onLine(4);
  }

  @Test public void failIfPaperParcelClassIsAbstractTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public abstract class Test {",
            "  public int child;",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("@PaperParcel cannot be applied to an abstract class")
        .in(source)
        .onLine(4);
  }

  @Test public void failIfPaperParcelClassIsAnInterfaceTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "@PaperParcel",
            "public interface Test {",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("@PaperParcel cannot be applied to an interface")
        .in(source)
        .onLine(4);
  }

  @Test public void failIfDefaultAdapterClassIsAnInterfaceTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.DefaultAdapter;",
            "import nz.bradcampbell.paperparcel.TypeAdapter;",
            "@DefaultAdapter",
            "public interface Test extends TypeAdapter<Integer> {",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("@DefaultAdapter cannot be applied to an interface")
        .in(source)
        .onLine(5);
  }

  @Test public void failIfDefaultAdapterClassIsAbstractTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.DefaultAdapter;",
            "import nz.bradcampbell.paperparcel.TypeAdapter;",
            "@DefaultAdapter",
            "public abstract class Test implements TypeAdapter<Integer> {",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("@DefaultAdapter cannot be applied to an abstract class")
        .in(source)
        .onLine(5);
  }

  @Test public void failIfAnUnsupportedTypeIsFoundTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.List;",
            "@PaperParcel",
            "public final class Test {",
            "  public List<MyCustomType> child;",
            "}"
        ));

    JavaFileObject unknownType =
        JavaFileObjects.forSourceString("test.MyCustomType", Joiner.on('\n').join(
            "package test;",
            "public final class MyCustomType {",
            "}"
        ));

    assertAbout(javaSources()).that(Arrays.asList(source, unknownType))
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel does not know how to process test.Test because the "
            + "child field is a java.util.List<test.MyCustomType> and test.MyCustomType is not a "
            + "supported PaperParcel type. Define a TypeAdapter<test.MyCustomType> to add support "
            + "for test.MyCustomType objects. Alternatively you can exclude the field by making it "
            + "static, transient, or using the ExcludeFields annotation on test.Test");
  }

  @Test public void failIfThereAreNoVisibleConstructorsTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "import java.util.List;",
            "@PaperParcel",
            "public final class Test {",
            "  private Test() {",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel requires at least one non-private constructor to "
            + "instantiate test.Test")
        .in(source)
        .onLine(6);
  }

  @Test public void failIfConstructorIsNotSatisfiableTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "import java.util.List;",
            "@PaperParcel",
            "public final class Test {",
            "  private final String s1;",
            "  private final String s2;",
            "  public Test(String s1, String missing, String s2) {",
            "    this.s1 = s1;",
            "    this.s2 = s2;",
            "  }",
            "  public String s1() {",
            "    return s1;",
            "  }",
            "  public String s2() {",
            "    return s2;",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel cannot satisfy constructor "
            + "Test(java.lang.String,java.lang.String,java.lang.String). PaperParcel was able to "
            + "find 2 arguments, but needed 3. The missing arguments were [missing]")
        .in(source)
        .onLine(9);
  }

  @Test public void failIfBaseClassHasFieldWithTheSameNameAsSuperclassTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "import java.util.List;",
            "@PaperParcel",
            "public final class Test extends Base {",
            "  private final String test;",
            "  public Test(String test) {",
            "    super(test);",
            "    this.test = test;",
            "  }",
            "  public String test() {",
            "    return test;",
            "  }",
            "}"
        ));

    JavaFileObject base =
        JavaFileObjects.forSourceString("test.Base", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.Date;",
            "import java.util.List;",
            "public class Base {",
            "  private final String test;",
            "  public Base(String test) {",
            "    this.test = test;",
            "  }",
            "}"
        ));

    assertAbout(javaSources()).that(Arrays.asList(source, base))
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel cannot process test.Test because it has two non-ignored "
            + "fields named \"test\". The first can be found in test.Test and the second can be "
            + "found in test.Base")
        .in(source)
        .onLine(7);
  }

  @Test public void failIfTypeArgumentsAreNotUsed() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import nz.bradcampbell.paperparcel.PaperParcel;",
            "import java.util.List;",
            "@PaperParcel",
            "public final class Test {",
            "  private final List child;",
            "  public Test(List child) {",
            "    this.child = child;",
            "  }",
            "  public List getChild() {",
            "    return this.child;",
            "  }",
            "}"
        ));

    assertAbout(javaSource()).that(source)
        .processedWith(new PaperParcelProcessor())
        .failsToCompile()
        .withErrorContaining("PaperParcel cannot process the field \"child\" in test.Test "
            + "because it is a raw type.")
        .in(source)
        .onLine(6);
  }
}

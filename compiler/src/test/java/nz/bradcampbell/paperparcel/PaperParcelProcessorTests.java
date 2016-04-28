package nz.bradcampbell.paperparcel;

import com.google.common.base.Joiner;
import com.google.testing.compile.JavaFileObjects;
import javax.tools.JavaFileObject;
import org.junit.Test;

import static com.google.common.truth.Truth.assertAbout;
import static com.google.testing.compile.JavaSourceSubjectFactory.javaSource;

public class PaperParcelProcessorTests {

  @Test public void allBuiltInAdaptersTest() throws Exception {
    JavaFileObject source =
        JavaFileObjects.forSourceString("test.Test", Joiner.on('\n').join(
            "package test;",
            "import android.os.Bundle;",
            "import android.os.Parcelable;",
            "import android.os.PersistableBundle;",
            "import android.util.SparseArray;",
            "import android.util.Size;",
            "import android.util.SizeF;",
            "import java.util.List;",
            "import java.util.Map;",
            "import java.util.Set;",
            "import java.util.Queue;",
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
            "import java.util.List;",
            "import java.util.Map;",
            "import java.util.Queue;",
            "import java.util.Set;",
            "import nz.bradcampbell.paperparcel.internal.ParcelableWrapper;",
            "import nz.bradcampbell.paperparcel.typeadapters.BooleanAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.BundleAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ByteAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.CharSequenceAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.CharacterAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.DoubleAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.FloatAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.IntegerAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ListAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.LongAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.MapAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ParcelableAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.PersistableBundleAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.QueueAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.SetAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.ShortAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.SizeAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.SizeFAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.SparseArrayAdapter;",
            "import nz.bradcampbell.paperparcel.typeadapters.StringAdapter;",
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
package nz.bradcampbell.paperparcel.kotlinexample

import android.os.Parcel
import nz.bradcampbell.paperparcel.typeadapters.AbstractAdapter
import java.util.*

object DateTypeAdapter : AbstractAdapter<Date>() {
  override fun writeToParcelInner(value: Date, dest: Parcel, flags: Int) {
    dest.writeLong(value.time);
  }

  override fun readFromParcelInner(source: Parcel): Date {
    return Date(source.readLong())
  }
}

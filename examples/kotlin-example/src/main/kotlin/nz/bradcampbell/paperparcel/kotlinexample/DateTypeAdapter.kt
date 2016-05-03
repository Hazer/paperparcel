package nz.bradcampbell.paperparcel.kotlinexample

import android.os.Parcel
import nz.bradcampbell.paperparcel.typeadapters.base.AbstractAdapter
import java.util.*

object DateTypeAdapter : AbstractAdapter<Date>() {
  override fun write(value: Date, dest: Parcel, flags: Int) {
    dest.writeLong(value.time);
  }

  override fun read(source: Parcel): Date {
    return Date(source.readLong())
  }
}

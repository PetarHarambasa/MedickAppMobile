package hr.medick.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pacijent(
     val id: Long? = null,
) : Parcelable {
     constructor(osoba: Osoba) : this()
}

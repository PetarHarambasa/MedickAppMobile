package hr.medick.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.Date

@Parcelize
data class Vitali(
    val id: Long? = null,
    val pacijent: Pacijent? = null,
    val glukozaukrvi: String? = null,
    val krvnitlak: String? = null,
    val datummjerenja: Date? = null
) : Parcelable

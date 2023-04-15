package hr.medick.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Podsjetnik(
    val id: Long? = null,
    val terapija: Terapija? = null,
    val uzet: Boolean? = null
): Parcelable

package hr.medick.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lijek(
    val id: Long? = null,
    val naziv: String? = null, val proizvodac: String? = null
) : Parcelable

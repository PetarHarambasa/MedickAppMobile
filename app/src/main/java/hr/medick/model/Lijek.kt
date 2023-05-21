package hr.medick.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lijek(
    var id: Long? = null,
    var naziv: String? = null,
    var proizvodac: String? = null

) : Parcelable {

    override fun toString(): String {
        return "$naziv"
    }
}





package hr.medick.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Terapija(
    var id: Long? = null,
    var lijek: Lijek? = null,
    var pacijent: Pacijent? = null,
    var vitali: Vitali? = null,
    var dozalijeka: String? = null,
    var ponavljanja: Float? = null,
    var prvadoza: Date? = null,
    var kolicinatableta: Int? = null,
    var kolicinadnevno: Int? = null,
) : Parcelable {
    //Singleton
    companion object {
        private val instance = Terapija()

        fun getInstance() = instance
    }
}

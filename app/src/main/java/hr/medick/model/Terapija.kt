package hr.medick.model

import java.util.*

data class Terapija(
    val id: Long? = null,
    val lijek: Lijek? = null,
    val pacijent: Pacijent? = null,
    val vitali: Vitali? = null,
    val dozalijeka: String? = null,
    val ponavljanja: Float? = null,
    val prvadoza: Date? = null,
    val kolicinatableta: Int? = null,
    val kolicinadnevno: Int? = null,
)

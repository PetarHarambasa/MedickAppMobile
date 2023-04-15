package hr.medick.model

import java.util.Date

data class Vitali(
    val id: Long? = null,
    val pacijent: Pacijent? = null,
    val glukozaukrvi: String? = null, val krvnitlak: String? = null,
    val date: Date? = null
)

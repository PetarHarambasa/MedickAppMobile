package hr.medick.dto

import hr.medick.model.Osoba

class ProfileDto(
    val osoba: Osoba = Osoba(),
    val changedEmail: Boolean = false
)


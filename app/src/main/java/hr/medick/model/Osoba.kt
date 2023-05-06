package hr.medick.model

data class Osoba (
    var id: Long? = null,
    var ime: String? = null,
    var prezime: String? = null,
    var email: String? = null,
    var telefon: String? = null,
    var adresaStanovanja: String? = null,
    var lozinka: String? = null
) : java.io.Serializable
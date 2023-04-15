package hr.medick.model

data class Pacijent(
     val id: Long? = null,
){
     constructor(osoba: Osoba) : this()
}

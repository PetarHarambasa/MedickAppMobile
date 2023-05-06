package hr.medick.session

import android.content.Context
import android.content.SharedPreferences
import hr.medick.model.Osoba

class Session(context: Context) {
    val PREFS_KEY = "prefs"
    val FIRSTNAME_KEY = "ime"
    val LASTNAME_KEY = "prezime"
    val EMAIL_KEY = "email"
    val PHONENMBR_KEY = "telefon"
    val ADRESS_KEY = "adresaStanovanja"
    val PWD_KEY = "password"

    val prefs = context.getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE)
    val editor = prefs.edit()
//    private lateinit var osoba: Osoba

//    init {
//        if (isLoggedIn()) {
//            osoba.ime = prefs.getString(FIRSTNAME_KEY, "")
//            osoba.prezime = prefs.getString(LASTNAME_KEY, "")
//            osoba.email =  prefs.getString(EMAIL_KEY, "")
//            osoba.telefon =  prefs.getString(PHONENMBR_KEY, "")
//            osoba.adresaStanovanja =  prefs.getString(ADRESS_KEY, "")
//            osoba.lozinka =  prefs.getString(PWD_KEY, "")
//        }
//    }

    fun setUser(osoba: Osoba) {
        editor.apply {
            putString(FIRSTNAME_KEY, osoba.ime)
            putString(LASTNAME_KEY, osoba.prezime)
            putString(EMAIL_KEY, osoba.email)
            putString(PHONENMBR_KEY, osoba.telefon)
            putString(ADRESS_KEY, osoba.adresaStanovanja)
            putString(PWD_KEY, osoba.lozinka)
            apply()
        }
    }

    @JvmName("getOsoba1")
    fun getOsoba(): Osoba{
        var osoba = Osoba()
        if (isLoggedIn()) {
            osoba.ime = prefs.getString(FIRSTNAME_KEY, "")
            osoba.prezime = prefs.getString(LASTNAME_KEY, "")
            osoba.email =  prefs.getString(EMAIL_KEY, "")
            osoba.telefon =  prefs.getString(PHONENMBR_KEY, "")
            osoba.adresaStanovanja =  prefs.getString(ADRESS_KEY, "")
            osoba.lozinka =  prefs.getString(PWD_KEY, "")
            return osoba
        }
        return osoba
    }

    fun isLoggedIn(): Boolean {
        return !prefs.getString(FIRSTNAME_KEY, "").isNullOrBlank() &&
                !prefs.getString(LASTNAME_KEY, "").isNullOrBlank() &&
                !prefs.getString(EMAIL_KEY, "").isNullOrBlank() &&
                !prefs.getString(PHONENMBR_KEY, "").isNullOrBlank() &&
                !prefs.getString(ADRESS_KEY, "").isNullOrBlank() &&
                !prefs.getString(PWD_KEY, "").isNullOrBlank()
    }

    fun logout() {
        editor.apply {
            clear()
            apply()
        }
    }
}
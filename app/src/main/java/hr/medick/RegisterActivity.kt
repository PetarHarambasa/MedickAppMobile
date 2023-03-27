package hr.medick

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import hr.medick.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val ime = binding.imeEditText.text
            val prezime = binding.prezimeEditText.text
            val email = binding.emailEditText.text
            val telefon = binding.telefonEditText.text
            val adresaStanovanja = binding.adresaStanovanjaEditText.text
            val lozinka = binding.lozinkaEditText.text
            val ponovljenaLozinka = binding.ponovnoLozinkaEditText.text

            // Validate user input
            if (ime.isEmpty() || prezime.isEmpty() || email.isEmpty() || telefon.isEmpty() || adresaStanovanja.isEmpty() || lozinka.isEmpty() || ponovljenaLozinka.isEmpty()) {
                Toast.makeText(this, "Molim, popunite sva polja", Toast.LENGTH_SHORT).show()
            } else if(!email.contains("@")){
                Toast.makeText(this, "Molim, upisite svoj email", Toast.LENGTH_SHORT).show()
            }
            else if (lozinka.trim() != ponovljenaLozinka.trim()) {
                Toast.makeText(this, "Molim, potvrdite lozinku", Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "Registration successful (Fake)", Toast.LENGTH_SHORT).show()
                clearInputs()
            }
        }
    }

    fun clearInputs(){
        binding.imeEditText.text.clear()
        binding.prezimeEditText.text.clear()
        binding.emailEditText.text.clear()
        binding.telefonEditText.text.clear()
        binding.adresaStanovanjaEditText.text.clear()
        binding.lozinkaEditText.text.clear()
        binding.ponovnoLozinkaEditText.text.clear()
    }
}
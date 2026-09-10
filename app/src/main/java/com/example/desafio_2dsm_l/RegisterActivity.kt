package com.example.desafio_2dsm_l

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio_2dsm_l.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Evento click del botón Registrarse
        binding.btnRegister.setOnClickListener {
            if (validateInputs()) {
                performRegistration()
            }
        }

        // Evento click para ir al Login
        binding.tvLoginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        if (name.isEmpty()) {
            binding.tilName.error = "El Nombre Es Obligatorio"
            isValid = false
        } else {
            binding.tilName.error = null
        }

        if (email.isEmpty()) {
            binding.tilEmail.error = "El Correo Electrónico Es Obligatorio"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Ingresa Un Correo Electrónico Válido"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "La Contraseña Es Obligatoria"
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = "La Contraseña Debe Tener Al Menos 6 Caracteres"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        if (confirmPassword.isEmpty()) {
            binding.tilConfirmPassword.error = "Debes Confirmar Tu Contraseña"
            isValid = false
        } else if (confirmPassword != password) {
            binding.tilConfirmPassword.error = "Las Contraseñas No Coinciden"
            isValid = false
        } else {
            binding.tilConfirmPassword.error = null
        }

        return isValid
    }

    private fun performRegistration() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        binding.btnRegister.isEnabled = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.btnRegister.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(this, "¡Registro Exitoso!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finishAffinity() // Cierra las pantallas de autenticación
                } else {
                    val errorMessage = getErrorMessage(task.exception)
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    // Método para traducir las excepciones de Registro de Firebase Auth al español
    private fun getErrorMessage(exception: Exception?): String {
        val msg = exception?.message ?: ""
        return when {
            msg.contains("already in use", ignoreCase = true) ->
                "El Correo Electrónico Ya Está Registrado. Intenta Iniciar Sesión."
            msg.contains("badly formatted", ignoreCase = true) ->
                "El Correo Electrónico No Tiene Un Formato Válido."
            msg.contains("weak password", ignoreCase = true) ->
                "La Contraseña Es Demasiado Débil. Usa al Menos 6 Caracteres."
            msg.contains("network", ignoreCase = true) ->
                "Error de Red. Revisa Tu Conexión a Internet."
            msg.contains("too many", ignoreCase = true) ||
                    msg.contains("blocked", ignoreCase = true) ->
                "Acceso Bloqueado Temporalmente Por Demasiados Intentos Fallidos."
            else ->
                "Error al Registrar la Cuenta. Verifica Tus Datos e Inténtalo de Nuevo."
        }
    }
}
package com.example.desafio_2dsm_l

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio_2dsm_l.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inicializar ViewBinding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Evento click del botón Iniciar Sesión
        binding.btnLogin.setOnClickListener {
            if (validateInputs()) {
                performLogin()
            }
        }

        // Evento click para ir al registro
        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun validateInputs(): Boolean {
        var isValid = true

        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Validación del Correo Electrónico
        if (email.isEmpty()) {
            binding.tilEmail.error = "El Correo Electrónico Es Obligatorio"
            isValid = false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.tilEmail.error = "Ingresa Un Correo Electrónico Válido"
            isValid = false
        } else {
            binding.tilEmail.error = null
        }

        // Validación de la Contraseña
        if (password.isEmpty()) {
            binding.tilPassword.error = "La Contraseña Es Obligatoria"
            isValid = false
        } else if (password.length < 6) {
            binding.tilPassword.error = "La Contraseña Debe Tener Al Menos 6 Caracteres"
            isValid = false
        } else {
            binding.tilPassword.error = null
        }

        return isValid
    }

    private fun performLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        binding.btnLogin.isEnabled = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.btnLogin.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(this, "¡Bienvenido a Agencia de Viajes!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    val errorMessage = getErrorMessage(task.exception)
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    // Método para traducir las excepciones de Firebase Auth al español
    private fun getErrorMessage(exception: Exception?): String {
        val msg = exception?.message ?: ""
        return when {
            msg.contains("no user record", ignoreCase = true) ->
                "El Usuario No Está Registrado. Crea una Cuenta Primero."
            msg.contains("INVALID_LOGIN_CREDENTIALS", ignoreCase = true) ||
                    msg.contains("password", ignoreCase = true) ->
                "Correo o Contraseña Incorrectos."
            msg.contains("badly formatted", ignoreCase = true) ->
                "El Correo Electrónico No Tiene Un Formato Válido."
            msg.contains("network", ignoreCase = true) ->
                "Error de Red. Revisa Tu Conexión a Internet."
            msg.contains("too many", ignoreCase = true) ||
                    msg.contains("blocked", ignoreCase = true) ->
                "Acceso Bloqueado Temporalmente Por Demasiados Intentos Fallidos."
            else ->
                "Error al Iniciar Sesión. Verifica Tus Credenciales."
        }
    }
}
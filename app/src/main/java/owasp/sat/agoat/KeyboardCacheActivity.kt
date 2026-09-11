package owasp.sat.agoat

import android.app.AlertDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class KeyboardCacheActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set FLAG_SECURE to prevent screen capture and reduce IME attack surface
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_keyboard_cache)
        val username = findViewById<EditText>(R.id.userName)
        val password = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.Logging1)

        loginButton.setOnClickListener {
            Toast.makeText(applicationContext, "Please wait while verifying your credentials", Toast.LENGTH_LONG).show()
        }

        // Add runtime IME detection
        warnIfThirdPartyImeActive()
    }

    private fun warnIfThirdPartyImeActive() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentIme = Settings.Secure.getString(
            contentResolver, Settings.Secure.DEFAULT_INPUT_METHOD
        )

        // Define a set of known system keyboard packages
        // This list might need to be expanded based on target devices/OEMs
        val systemPackages = setOf(
            "com.android.inputmethod.latin", // AOSP Keyboard
            "com.google.android.inputmethod.latin" // Gboard
            // Add other trusted system keyboard packages if necessary
        )

        if (currentIme != null) {
            val activePackage = currentIme.substring(0, currentIme.indexOf('/'))
            if (!systemPackages.contains(activePackage)) {
                AlertDialog.Builder(this)
                    .setTitle("Security Warning")
                    .setMessage("A third-party keyboard is active. Sensitive data may be at risk.")
                    .setPositiveButton("Continue") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }
}

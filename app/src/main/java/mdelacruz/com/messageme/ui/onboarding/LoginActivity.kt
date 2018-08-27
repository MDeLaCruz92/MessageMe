package mdelacruz.com.messageme.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import mdelacruz.com.messageme.R
import mdelacruz.com.messageme.ui.messages.RecentMessagesActivity
import mdelacruz.com.messageme.utils.textString

class LoginActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    login_button.setOnClickListener {
      performLogin()
    }

    back_registration_textview.setOnClickListener {
      finish()
    }
  }

  private fun performLogin() {
    val email = emailEditText.textString()
    val password = passwordEditText.textString()

    Log.d("Login", "Attempt login with email/password: $email/***")

    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {
          if (!it.isSuccessful) return@addOnCompleteListener

          Log.d("Login", "Successfully logged in: ${it.result.user.uid}")

          val intent = Intent(this, RecentMessagesActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
          startActivity(intent)
        }
        .addOnFailureListener {
          Toast.makeText(this, "Failed to login: ${it.message}", Toast.LENGTH_SHORT).show()
        }
  }
}
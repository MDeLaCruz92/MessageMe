package mdelacruz.com.messageme.ui.onboarding

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import mdelacruz.com.messageme.R
import mdelacruz.com.messageme.data.User
import mdelacruz.com.messageme.ui.messages.RecentMessagesActivity
import mdelacruz.com.messageme.utils.start
import mdelacruz.com.messageme.utils.textString
import mdelacruz.com.messageme.utils.toast
import java.util.*

class RegisterActivity : AppCompatActivity() {

  //==============================================================================================
  // Properties
  //==============================================================================================

  private var selectedPhotoUri: Uri? = null

  //==============================================================================================
  // Lifecycle Methods
  //==============================================================================================

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_register)

    registerButton.setOnClickListener {
      performRegister()
    }

    alreadyHaveAccountTextView.setOnClickListener {
      Log.d("RegisterActivity", "Try to show login Activity")
      start(LoginActivity::class.java)
    }

    selectPhotoButton.setOnClickListener {
      val intent = Intent(Intent.ACTION_PICK)
      intent.type = "image/*"
      startActivityForResult(intent, 0)
    }
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
      // proceed and check what the selected image was..
      Log.d("RegisterActivity", "Photo was selected")

      selectedPhotoUri = data.data
      val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

      selectPhotoImageView.setImageBitmap(bitmap)
      selectPhotoButton.alpha = 0f
    }
  }

  //==============================================================================================
  // Firebase
  //==============================================================================================

  private fun performRegister() {
    val email = emailEditText.textString()
    val password = passwordEditText.textString()

    if (email.isEmpty() || password.isEmpty()) {
      toast(R.string.empty_field_message)
    }

    Log.d("RegisterActivity", "Email is: $email")
    Log.d("RegisterActivity", "Password: $password")

    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {
          if (!it.isSuccessful) return@addOnCompleteListener

          Log.d("ResgisterActivity", "Successfully created user with uid: ${it.result.user.uid}")

          uploadImageToFirebaseStorage()
        }
        .addOnFailureListener {
          Log.d("RegisterActivity", "Failed to create user: ${it.message}")
        }
  }

  private fun uploadImageToFirebaseStorage() {
    if (selectedPhotoUri == null ) return

    val filename = UUID.randomUUID().toString()
    val storageRef = FirebaseStorage.getInstance().getReference("/images/$filename")

    storageRef.putFile(selectedPhotoUri!!)
        .addOnSuccessListener { it ->
          Log.d("RegisterActivity", "Sucessfully uploaded image: ${it.metadata?.path}")

          storageRef.downloadUrl.addOnSuccessListener {
            Log.d("RegisterActivity", "File Location: $it")

            saveUserToFirebaseDatabase(it.toString())
          }
        }
        .addOnFailureListener {
          Log.d("RegisterActivity", "Failed to upload image: ${it.message}")
        }
  }

  private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
    val uid = FirebaseAuth.getInstance().uid ?: ""
    val databaseRef = FirebaseDatabase.getInstance().getReference("/users/$uid")
    val user = User(uid, usernameEditText.textString(), profileImageUrl)

    databaseRef.setValue(user)
        .addOnSuccessListener {
          Log.d("RegisterActivity", "Finally we saved the user to Firebase Database")

          val intent = Intent(this, RecentMessagesActivity::class.java)
          intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
          startActivity(intent)
        }
        .addOnFailureListener {
          Log.d("RegisterActivity", "Failed to set value to database: ${it.message}")
        }
  }

}

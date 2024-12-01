package AniMo.com

import AniMo.com.database.User
import AniMo.com.databinding.ActivitySignupBinding
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var name: EditText
    private lateinit var password: EditText
    private lateinit var username: EditText
    private lateinit var email: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signupButton.setOnClickListener() {
            auth = FirebaseAuth.getInstance()
            database = FirebaseDatabase.getInstance()
            reference = database.reference.child("Users")
            name = findViewById(R.id.name)
            password = findViewById(R.id.password)
            email = findViewById(R.id.email)
            username = findViewById(R.id.username)

            if (name.text.isNotEmpty() && username.text.isNotEmpty() && password.text.isNotEmpty() && email.text.isNotEmpty()) {
                val nameValue = name.text.toString()
                val usernameValue = username.text.toString()
                val passwordValue = password.text.toString()
                val emailAddress = email.text.toString()
                authenticateUser(emailAddress, passwordValue, nameValue, usernameValue)

            } else {
                Toast.makeText(this, "Please populate all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelButton.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun authenticateUser(email: String, password: String, name: String, username: String) {
        CoroutineScope(IO).launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this@SignupActivity) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val new_user = User(name, email, user!!.uid)
                        reference.child(user.uid).setValue(new_user)

                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(
                            this@SignupActivity, "Successfully added user", Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(
                            this@SignupActivity, MainActivity::class.java
                        )
                        intent.putExtra("name", name)
                        intent.putExtra("uid", user.uid)
                        startActivity(intent)
                        finish()
                    } else {
                        // If sign in fails, display a message to the user.
                        if (password.length < 6) {
                            Toast.makeText(
                                baseContext,
                                "password too small, must be at least 6 characters",
                                Toast.LENGTH_SHORT,
                            ).show()
                        } else {
                            Toast.makeText(
                                baseContext,
                                "email invalid or in use",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
                }
        }
    }
}
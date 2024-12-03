package AniMo.com


import AniMo.com.database.User
import AniMo.com.databinding.ActivityLoginBinding
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

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button
    private lateinit var guestButton: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var email: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {

        database = FirebaseDatabase.getInstance()
        reference = database.reference.child("Users")
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        val cur_user = auth.currentUser
        if (cur_user != null) {
            Toast.makeText(this, "Logging in, please wait", Toast.LENGTH_LONG).show()
            reference.child(cur_user.uid).get().addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    println("Raw data: ${snapshot.value}")
                    val user = snapshot.getValue(User::class.java)
                    println(user)
                    if (user != null) {
                        println(user.name)
                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("name", user.name)
                        intent.putExtra("uid", user.uid)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
        else {
            Toast.makeText(this, "Cannot Log in", Toast.LENGTH_SHORT).show()
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        loginButton = findViewById(R.id.login_button)
        binding.loginButton.setOnClickListener() {
            if (email.text.isNotEmpty() && password.text.isNotEmpty()) {
                CoroutineScope(IO).launch {
                    signIn(email.text.toString(), password.text.toString())
                }
            } else {
                Toast.makeText(
                    this,
                    "Cannot login please enter username and password",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        createAccountButton = findViewById(R.id.create_account_button)
        binding.createAccountButton.setOnClickListener() {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
            finish()
        }

        guestButton = findViewById(R.id.guest_button)
        binding.guestButton.setOnClickListener() {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.forgotPassword.setOnClickListener() {
            if (email.text.isNotEmpty()) {
                auth.sendPasswordResetEmail(email.text.toString()).addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email sent", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Invalid email", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Enter email first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val curUser = auth.currentUser
                    println(curUser!!.uid)
                    Toast.makeText(this, "Successfully Logged in", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    reference.child(curUser.uid).get().addOnSuccessListener { snapshot ->
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(User::class.java)
                            println(user)
                            if (user != null) {
                                println(user.name)
                                intent.putExtra("name", user.name)
                                intent.putExtra("uid", user.uid)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }

                } else {
                    Toast.makeText(
                        this,
                        "Cannot login please, invalid username or password",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }
}

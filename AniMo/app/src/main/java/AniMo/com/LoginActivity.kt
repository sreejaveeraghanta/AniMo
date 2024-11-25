package AniMo.com


import AniMo.com.database.User
import AniMo.com.databinding.ActivityLoginBinding
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button
    private lateinit var guestButton: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var username: EditText
    private lateinit var password: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)

        loginButton = findViewById(R.id.login_button)
        binding.loginButton.setOnClickListener() {
            if (username.text.isNotEmpty() && password.text.isNotEmpty()) {
                database = FirebaseDatabase.getInstance()
                reference = database.reference.child("Users")
                reference.orderByChild("username").equalTo(
                    username.text.toString()).addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userdata in snapshot.children) {
                                val user = userdata.getValue(User::class.java)
                                if (user != null && user.password == Util.hashPassword(password.text.toString())) {
                                    Toast.makeText(this@LoginActivity, "Successfully logged in", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                    intent.putExtra("name", user.name)
                                    intent.putExtra("username", username.text.toString())
                                    startActivity(intent)
                                    finish()
                                }else {
                                    Toast.makeText(this@LoginActivity, "Incorrect username or password", Toast.LENGTH_SHORT).show()

                                }
                            }
                        }else {
                            Toast.makeText(this@LoginActivity, "Incorrect username or password", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@LoginActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else {
                Toast.makeText(this, "Cannot login please enter username and password", Toast.LENGTH_SHORT).show()
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
    }
}

package AniMo.com

import AniMo.com.database.User
import AniMo.com.databinding.ActivitySignupBinding
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class SignupActivity: AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var name: EditText
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var email : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.signupButton.setOnClickListener() {
            database = FirebaseDatabase.getInstance()
            reference = database.reference.child("Users")
            name = findViewById(R.id.name)
            username = findViewById(R.id.username)
            password = findViewById(R.id.password)
            email = findViewById(R.id.email)
            if (name.text.isNotEmpty() && username.text.isNotEmpty() && password.text.isNotEmpty() && email.text.isNotEmpty()) {
                val nameValue = name.text.toString()
                val usernameValue = username.text.toString()
                val passwordValue = password.text.toString()
                val hashedPassword = Util.hashPassword(passwordValue)
                val emailAddress = email.text.toString()
                val user = User(nameValue, emailAddress, usernameValue, hashedPassword)
                CoroutineScope(IO).launch {
                    reference.child(usernameValue).setValue(user)
                    Toast.makeText(this@SignupActivity, "Successfully added user", Toast.LENGTH_SHORT).show()
                }
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("name", nameValue)
                intent.putExtra("username", usernameValue)
                startActivity(intent)
                finish()
            }
            else {
                Toast.makeText(this, "Please populate all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.cancelButton.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
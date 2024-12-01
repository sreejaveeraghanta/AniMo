package AniMo.com

import AniMo.com.database.User
import AniMo.com.databinding.ActivityResetPasswordBinding
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class ResetPasswordActivity: AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    private lateinit var user_name: EditText
    private lateinit var email_address: EditText
    private lateinit var new_password: EditText

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user_name = findViewById(R.id.username)
        email_address = findViewById(R.id.email)
        new_password = findViewById(R.id.password)

        binding.resetButton.setOnClickListener() {
            if (email_address.text.isNotEmpty() && user_name.text.isNotEmpty() && new_password.text.isNotEmpty()){
                CoroutineScope(IO).launch {
                    database = FirebaseDatabase.getInstance()
                    reference = database.reference.child("Users")
                    reference.orderByChild("username").equalTo(
                        user_name.text.toString()
                    ).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (userdata in snapshot.children) {
                                    val user = userdata.getValue(User::class.java)
                                    if (user != null && user.email == email_address.text.toString()) {
                                        Toast.makeText(this@ResetPasswordActivity, "Successfully reset password", Toast.LENGTH_SHORT).show()
                                        reference.child(user.username).child("password").setValue(Util.hashPassword(new_password.text.toString()))
                                        val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@ResetPasswordActivity,
                                            "Incorrect username or email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    this@ResetPasswordActivity,
                                    "Incorrect username or email",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                "Error: ${error.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
            else {
                Toast.makeText(this, "Cannot reset please enter username and password", Toast.LENGTH_SHORT).show()
            }

        }

        binding.cancelButton.setOnClickListener() {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}
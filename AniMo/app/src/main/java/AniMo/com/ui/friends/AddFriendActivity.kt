package AniMo.com.ui.friends

import AniMo.com.R
import AniMo.com.Util
import AniMo.com.database.User
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

class AddFriendActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var cancel: Button
    private lateinit var add: Button
    private lateinit var email: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        cancel = findViewById(R.id.cancel_button)
        add = findViewById(R.id.add_button)
        email = findViewById(R.id.email)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        reference = database.reference.child("Users")

        cancel.setOnClickListener() {
            finish()
        }

        var list: MutableList<String> = mutableListOf()

        add.setOnClickListener() {
            if (email.text.isNotEmpty()) {
                val curUser = auth.currentUser
                reference.orderByChild("email").equalTo(email.text.toString())
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (user in snapshot.children) {
                                    val friend = user.getValue(User::class.java)
                                    reference.child(curUser!!.uid).get()
                                        .addOnSuccessListener { data ->
                                            if (data.exists()) {
                                                val current_user = data.getValue(User::class.java)
                                                // add friend to both current user and the friend being added
                                                val friends = current_user!!.friendList.toMutableList()
                                                val friends_friends = friend!!.friendList.toMutableList()
                                                if (!friends.contains(friend!!.uid)) {
                                                    friends.add(friend.uid)
                                                    friends_friends.add(current_user.uid)
                                                    reference.child(current_user.uid)
                                                        .child("friendList").setValue(friends)
                                                    reference.child(friend.uid)
                                                        .child("friendList").setValue(friends_friends)
                                                    Util.updateStats(current_user.uid, 0, 0, 0.0, 0, 0, 1)
                                                    Util.updateStats(friend.uid, 0, 0, 0.0, 0, 0, 1)
                                                    Toast.makeText(
                                                        this@AddFriendActivity,
                                                        "${friend.name} added to friends",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                    finish()
                                                } else {
                                                    Toast.makeText(
                                                        this@AddFriendActivity,
                                                        "${friend.name} is already your friend",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            }
                                        }
                                }
                            }
                            else {
                                Toast.makeText(
                                    this@AddFriendActivity,
                                    "Invalid User",
                                    Toast.LENGTH_SHORT).show()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            println(error)
                        }

                    })
            }

        }
    }
}
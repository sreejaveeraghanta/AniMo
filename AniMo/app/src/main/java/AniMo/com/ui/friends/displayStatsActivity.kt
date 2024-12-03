package AniMo.com.ui.friends

import AniMo.com.R
import AniMo.com.Util
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class displayStatsActivity : AppCompatActivity(){
    private lateinit var tasks: TextView
    private lateinit var friends: TextView
    private lateinit var visitors: TextView
    private lateinit var visited: TextView
    private lateinit var timeSpent: TextView
    private lateinit var text: TextView
    private lateinit var back_button: Button

    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var curUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.display_friends_stats)
        tasks = findViewById(R.id.tasks_completed)
        friends = findViewById(R.id.friends)
        visited = findViewById(R.id.visited)
        visitors = findViewById(R.id.visitors)
        timeSpent = findViewById(R.id.time_spent)
        text = findViewById(R.id.text_stats)
        back_button = findViewById(R.id.back_button)

        val _name = intent.getStringExtra("name")
        val _tasks = intent.getIntExtra("tasks", 0)
        val _friends = intent.getIntExtra("friends", 0)
        val _visted = intent.getIntExtra("visited", 0)
        val _visitors = intent.getIntExtra("visitors", 0)
        val _timeSpent = intent.getDoubleExtra("timeSpent", 0.0)
        val uid = intent.getStringExtra("uid").toString()

        text.text = "${_name}'s Statistics"
        tasks.text = _tasks.toString()
        friends.text = _friends.toString()
        visited.text = _visted.toString()
        visitors.text = _visitors.toString()
        timeSpent.text = _timeSpent.toString()

        Util.updateStats(curUser!!.uid, 0, 0, 0.0, 0, 1, 0)
        Util.updateStats(uid, 0, 0, 0.0, 1, 0, 0)
        back_button.setOnClickListener() {
            finish()
        }

    }
}
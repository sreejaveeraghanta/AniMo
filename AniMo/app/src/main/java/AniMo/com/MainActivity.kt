package AniMo.com

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import AniMo.com.databinding.ActivityMainBinding
import AniMo.com.ui.tasks.NewTaskActivity
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(findViewById(R.id.appBar))
        setContentView(binding.root)
        val name = intent.getStringExtra("name")
        val uid = intent.getStringExtra("uid")
        println(name)

        val sharedPreferences = getSharedPreferences("user", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", name)
        editor.putString("uid", uid)
        editor.apply()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_stats,
                R.id.navigation_friends,
                R.id.navigation_store
            )
        )

        navView.setupWithNavController(navController)

        binding.logout.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, NewTaskActivity::class.java)
            startActivity(intent)
            // Handle FAB click
        }
    }
}
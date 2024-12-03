package AniMo.com

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import AniMo.com.databinding.ActivityMainBinding
import AniMo.com.ui.inventory.InventoryViewModel
import AniMo.com.ui.tasks.NewTaskActivity
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var mainViewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModel.MainViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        println("CHECK 1")
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
        println("CHECK 2")

        navView.setupWithNavController(navController)



        if (uid != null){
            viewModelFactory = MainViewModel.MainViewModelFactory(uid)
            // hearts
            mainViewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
            mainViewModel.userhearts.observe(this) {
                binding.heartnumtextview.text = it.toString()
            }
            mainViewModel.updateHearts()
            println("CHECK 3")


        }
        println("CHECK 4")

        binding.logout.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        println("CHECK 5")

        binding.fab.setOnClickListener {
            val intent = Intent(this, NewTaskActivity::class.java)
            startActivity(intent)
            // Handle FAB click
        }
    }
}
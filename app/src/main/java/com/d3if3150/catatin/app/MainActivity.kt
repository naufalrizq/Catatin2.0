
package com.d3if3150.catatin.app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import com.d3if3150.catatin.R
import com.d3if3150.catatin.databinding.ActivityMainBinding
import com.d3if3150.catatin.repo.NotesRepo
import com.d3if3150.catatin.ui.notes.NotesViewModel
import com.d3if3150.catatin.utils.factory.viewModelFactory
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
//    companion object {
//        const val CHANNEL_ID = "updater"
//        const val PERMISSION_REQUEST_CODE = 1
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = getString(R.string.channel_name)
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance)
//            channel.description = getString(R.string.channel_desc)
//            val manager = getSystemService(Context.NOTIFICATION_SERVICE)
//                    as NotificationManager?
//            manager?.createNotificationChannel(channel)
//        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.api_frag -> {
                    navController.navigate(R.id.apiFetchFragment)
                    true
                }
                R.id.notes_frag -> {
                    navController.navigate(R.id.notesFragment)
                    true
                }
                R.id.notif_frag -> {
                    navController.navigate(R.id.notificationFragment)
                    true
                }
                else -> false
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}

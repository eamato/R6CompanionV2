package eamato.funn.r6companion.ui.activities

import android.Manifest
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.transition.TransitionManager
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.google.android.material.transition.platform.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.isPermissionGranted
import eamato.funn.r6companion.core.extenstions.setViewVisibleOrGone
import eamato.funn.r6companion.core.notifications.R6NotificationManager
import eamato.funn.r6companion.databinding.ActivityMainBinding
import eamato.funn.r6companion.ui.viewmodels.MainNavigationViewModel
import eamato.funn.r6companion.ui.viewmodels.MainViewModel

@AndroidEntryPoint
class ActivityMain : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainNavigationViewModel: MainNavigationViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    private val requestPermissionLauncher: ActivityResultLauncher<String> by lazy {
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                createNotificationChannel()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition { mainViewModel.isLoadingSplash.value }
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContentView(binding.root)

        val navController = binding.appNavHostContainer.getFragment<NavHostFragment>().navController
        binding.bnv.setOnItemSelectedListener { menuItem ->
            return@setOnItemSelectedListener when (menuItem.itemId) {
                R.id.FragmentHome -> {
                    onBottomNavigationItemClicked(R.id.FragmentHome, navController)
                    true
                }
                R.id.FragmentRoulette -> {
                    onBottomNavigationItemClicked(R.id.FragmentRoulette, navController)
                    true
                }
                R.id.FragmentCompanion -> {
                    onBottomNavigationItemClicked(R.id.FragmentCompanion, navController)
                    true
                }
                R.id.FragmentSettings -> {
                    onBottomNavigationItemClicked(R.id.FragmentSettings, navController)
                    true
                }
                else -> false
            }
        }

        addDestinationChangeListener(navController)
        addOnBackPressCallback(navController)

        requestPushNotificationPermissionIfNeeded()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            val v: View? = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    fun hideNavigation() {
        val modalOut = MaterialSharedAxis(MaterialSharedAxis.Y, false)
        TransitionManager.beginDelayedTransition(binding.bnv, modalOut)
        binding.bnv.setViewVisibleOrGone(false)
    }

    fun showNavigation() {
        val modalIn = MaterialSharedAxis(MaterialSharedAxis.Y, true)
        TransitionManager.beginDelayedTransition(binding.bnv, modalIn)
        binding.bnv.setViewVisibleOrGone(true)
    }

    private fun onBottomNavigationItemClicked(destinationId: Int, navController: NavController) {
        if (navController.currentDestination?.id == destinationId) {
            mainNavigationViewModel.submitBackToGraphRootEvent()
        } else {
            navigateTo(destinationId, navController)
        }
    }

    private fun navigateTo(destinationId: Int, navController: NavController) {
        navController.navigate(destinationId, null, navOptions {
            launchSingleTop = true
            restoreState = true
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
        })
    }

    private fun addOnBackPressCallback(navController: NavController) {
        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {
                    navController.run {
                        val startDestinationId = graph.findStartDestination().id
                        isEnabled = currentBackStackEntry?.destination?.id != startDestinationId
                        if (isEnabled) {
                            popBackStack(startDestinationId, false)
                        }
                    }
                }
            })
    }

    private fun addDestinationChangeListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            destination.hierarchy.forEach {
                when (it.id) {
                    R.id.FragmentHome -> binding.bnv.menu.findItem(R.id.FragmentHome).isChecked = true
                    R.id.FragmentRoulette -> binding.bnv.menu.findItem(R.id.FragmentRoulette).isChecked = true
                    R.id.FragmentCompanion -> binding.bnv.menu.findItem(R.id.FragmentCompanion).isChecked = true
                    R.id.FragmentSettings -> binding.bnv.menu.findItem(R.id.FragmentSettings).isChecked = true
                }
            }
        }
    }

    private fun requestPushNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (this.isPermissionGranted( Manifest.permission.POST_NOTIFICATIONS)) {
                createNotificationChannel()
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            createNotificationChannel()
        }
    }

    private fun createNotificationChannel() {
        R6NotificationManager.createNotificationChannel(
            context = this,
            notificationChannelName = getString(R.string.notification_channel_name),
            notificationChannelDescription = getString(R.string.notification_channel_description)
        )
    }
}
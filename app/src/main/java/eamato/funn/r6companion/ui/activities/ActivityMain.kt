package eamato.funn.r6companion.ui.activities

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.databinding.ActivityMainBinding
import eamato.funn.r6companion.ui.viewmodels.MainNavigationViewModel
import eamato.funn.r6companion.ui.viewmodels.MainViewModel

@AndroidEntryPoint
class ActivityMain : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val mainNavigationViewModel: MainNavigationViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(binding.root)

        mainViewModel

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
}
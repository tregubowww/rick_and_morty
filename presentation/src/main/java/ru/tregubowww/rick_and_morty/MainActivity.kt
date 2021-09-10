package ru.tregubowww.rick_and_morty

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.tregubowww.rick_and_morty.app.appComponent
import ru.tregubowww.rick_and_morty.databinding.ActivityMainBinding
import ru.tregubowww.rick_and_morty.databinding.FragmentLocationsBinding
import ru.tregubowww.rick_and_morty.navigation.Navigation
import javax.inject.Inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val viewBinding by viewBinding(ActivityMainBinding::bind)

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var navigation: Navigation

    private val navigator = AppNavigator(this, R.id.fragmentContainerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Rick_and_morty)
        appComponent.inject(this)
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            navigation.characters()
        }

        viewBinding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.characters -> navigation.characters()
                R.id.locations -> navigation.locations()
                R.id.episodes -> navigation.episodes()
            }
            true
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }
}

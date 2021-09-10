package ru.tregubowww.rick_and_morty.location.locations_filter

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.tregubowww.domain.query.FilterQueryLocation
import ru.tregubowww.rick_and_morty.R
import ru.tregubowww.rick_and_morty.app.appComponent
import ru.tregubowww.rick_and_morty.databinding.FragmentLocationFilterChoiceBinding
import ru.tregubowww.rick_and_morty.navigation.Navigation
import javax.inject.Inject

class LocationFilterChoiceFragment : Fragment(R.layout.fragment_location_filter_choice) {

    private val viewBinding by viewBinding(FragmentLocationFilterChoiceBinding::bind)

    @Inject
    lateinit var navigation: Navigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireContext().appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

        with(viewBinding) {
            buttonSearch.setOnClickListener {

                val nameText = getTextFromEditText(nameEditText.text.toString())
                val typeText = getTextFromEditText(typeEditText.text.toString())
                val dimensionText = getTextFromEditText(dimensionEditText.text.toString())

                val filterData = FilterQueryLocation(
                    name = nameText,
                    type = typeText,
                    dimension = dimensionText
                )
                navigation.locationsFilter(filterData)
            }
            buttonCancel.setOnClickListener { navigation.back() }
        }
    }

    private fun getTextFromEditText(text: String): String? {
        return if (text.isBlank()) null
        else text
    }

    companion object {
        fun newInstance() = LocationFilterChoiceFragment()
    }
}
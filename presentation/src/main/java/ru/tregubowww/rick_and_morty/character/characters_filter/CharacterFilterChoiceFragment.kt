package ru.tregubowww.rick_and_morty.character.characters_filter

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.tregubowww.domain.query.FilterQueryCharacter
import ru.tregubowww.rick_and_morty.R
import ru.tregubowww.rick_and_morty.app.appComponent
import ru.tregubowww.rick_and_morty.databinding.FragmentCharactersFilterChoiceBinding
import ru.tregubowww.rick_and_morty.navigation.Navigation
import javax.inject.Inject

class CharacterFilterChoiceFragment : Fragment(R.layout.fragment_characters_filter_choice) {

    private val viewBinding by viewBinding(FragmentCharactersFilterChoiceBinding::bind)

    @Inject
    lateinit var navigation: Navigation

    @Inject
    lateinit var characterListViewModelFactory: ViewModelProvider.Factory
    private val viewModel: CharactersFilterViewModel by viewModels {
        characterListViewModelFactory
    }

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

                val textStatus = getTextRadioButton(statusRadioGroup.checkedRadioButtonId)
                val textSpecies = getTextRadioButton(speciesRadioGroup.checkedRadioButtonId)
                val textGender = getTextRadioButton(genderRadioGroup.checkedRadioButtonId)
                val nameText = getTextFromEditText(nameEditText.text.toString())
                val typeText = getTextFromEditText(typeEditText.text.toString())

                val filterData = FilterQueryCharacter(
                    name = nameText,
                    status = textStatus,
                    species = textSpecies,
                    type = typeText,
                    gender = textGender
                )
                navigation.charactersFilter(filterData)
            }
            buttonCancel.setOnClickListener { navigation.back() }
        }
    }

    private fun getTextFromEditText(text: String): String? {
        return if (text.isBlank()) null
        else text
    }

    private fun getTextRadioButton(checkedRadioButtonId: Int): String? {
        return if (checkedRadioButtonId == -1) null
        else viewBinding.root.findViewById<RadioButton>(checkedRadioButtonId).text.toString()
    }

    companion object {
        fun newInstance() = CharacterFilterChoiceFragment()
    }
}
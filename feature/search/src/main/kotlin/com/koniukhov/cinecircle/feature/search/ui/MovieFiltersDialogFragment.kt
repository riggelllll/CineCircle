package com.koniukhov.cinecircle.feature.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.koniukhov.cinecircle.core.common.sort.MovieSortOption
import com.koniukhov.cinecircle.core.ui.base.BaseFragment
import com.koniukhov.cinecircle.feature.search.R
import com.koniukhov.cinecircle.feature.search.databinding.FragmentMovieFiltersBinding
import com.koniukhov.cinecircle.feature.search.model.MovieFilterParams
import com.koniukhov.cinecircle.feature.search.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MovieFiltersDialogFragment(private val onSearchClick: () -> Unit) :
    BaseFragment<FragmentMovieFiltersBinding, SearchViewModel>() {

    override val viewModel: SearchViewModel by activityViewModels()

    private val collator = java.text.Collator.getInstance(Locale.getDefault())
    private val pairComparator: Comparator<Pair<String, String>> =
        Comparator { a, b -> collator.compare(a.first, b.first) }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieFiltersBinding {
        return FragmentMovieFiltersBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupDatePickers()
        setupDropdowns()
        setupYearSlider()
        setupVoteAverageRangeSlider()
        setupVoteCountRangeSlider()
        setupSearchBtn()
        setupResetBtn()
    }

    override fun observeViewModel() {
        launchWhenStarted {
            populateGenreChipsAndLoadParams()
        }
    }

    private fun setupYearSlider() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toFloat()
        binding.sliderYear.valueTo = currentYear
        if (binding.sliderYear.value < binding.sliderYear.valueFrom || binding.sliderYear.value > currentYear) {
            binding.sliderYear.value = currentYear
        }
    }

    private fun setupVoteAverageRangeSlider() {
        with(binding.voteAverage) {
            setValues(0f, 10f)
            stepSize = 0.1f
            setLabelFormatter { value -> String.format(Locale.getDefault(), "%.1f", value) }
        }
    }

    private fun setupVoteCountRangeSlider() {
        with(binding.voteCount) {
            setValues(0f, 10000f)
            stepSize = 10f
            setLabelFormatter { value -> value.toInt().toString() }
        }
    }

    private fun setupDatePickers() {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        binding.releaseDateGte.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.label_release_date_gte)
                .build()
            picker.addOnPositiveButtonClickListener { millis ->
                binding.releaseDateGte.setText(formatter.format(Date(millis)))
            }
            picker.show(parentFragmentManager, DATE_GTE_TAG)
        }
        binding.releaseDateLte.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.label_release_date_lte)
                .build()
            picker.addOnPositiveButtonClickListener { millis ->
                binding.releaseDateLte.setText(formatter.format(Date(millis)))
            }
            picker.show(parentFragmentManager, DATE_LTE_TAG)
        }
    }

    private fun setupDropdowns() {
        setupOriginCountryDropdown()
        setupOriginalLanguageDropdown()
        setupSortByDropdown()
    }

    private fun setupSortByDropdown() {
        val options = MovieSortOption.entries
        val items = options.map { getString(it.labelRes) }
        binding.sortBy.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        )
        val defaultIndex = options.indexOf(MovieSortOption.POPULARITY_DESC).takeIf { it >= 0 } ?: 0
        binding.sortBy.setText(items[defaultIndex], false)
    }

    private fun setupOriginCountryDropdown() {
        val items = viewModel.countries
            .toList()
            .sortedWith(pairComparator)
            .map { it.first }
        binding.originCountry.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        )
    }

    private fun setupOriginalLanguageDropdown() {
        val items = viewModel.languages
            .toList()
            .sortedWith(pairComparator)
            .map { it.first }
        binding.originalLanguage.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        )
    }

    private fun setupResetBtn() {
        binding.btnReset.setOnClickListener {
            resetFilters()
        }
    }

    private fun setupSearchBtn() {
        binding.btnApply.setOnClickListener {
            viewModel.selectMovieFilters(getEnteredFilters())
            onSearchClick.invoke()
        }
    }

    private suspend fun populateGenreChipsAndLoadParams() {
        viewModel.movieGenres.collectLatest { map ->
            binding.chipGroupInclude.removeAllViews()
            binding.chipGroupExclude.removeAllViews()

            map.forEach { (id, name) ->
                val ctx = ContextThemeWrapper(
                    requireContext(),
                    com.google.android.material.R.style.Widget_Material3_Chip_Filter
                )
                val chipInclude = Chip(ctx).apply {
                    text = name
                    isCheckable = true
                    tag = id
                }
                val chipExclude = Chip(ctx).apply {
                    text = name
                    isCheckable = true
                    tag = id
                }
                binding.chipGroupInclude.addView(chipInclude)
                binding.chipGroupExclude.addView(chipExclude)
            }
            loadFilterParams()
        }
    }

    private fun getEnteredFilters(): MovieFilterParams {
        val sortBy = MovieSortOption.apiFromDisplayText(binding.sortBy.text.toString(), requireContext())
        val sliderYear = binding.sliderYear.value.toInt()
        val year = if (sliderYear != binding.sliderYear.valueFrom.toInt()) sliderYear else null
        val releaseDateGte = binding.releaseDateGte.text?.toString()?.ifBlank { null }
        val releaseDateLte = binding.releaseDateLte.text?.toString()?.ifBlank { null }
        val minVoteAverage = binding.voteAverage.values.getOrNull(0)
        val maxVoteAverage = binding.voteAverage.values.getOrNull(1)
        val minVoteCount = binding.voteCount.values.getOrNull(0)?.toInt()
        val maxVoteCount = binding.voteCount.values.getOrNull(1)?.toInt()
        val withOriginCountry = viewModel.countries[binding.originCountry.text?.toString()]
        val withOriginalLanguage = viewModel.languages[binding.originalLanguage.text?.toString()]
        val withGenres = binding.chipGroupInclude.checkedChipIds.mapNotNull { id ->
            binding.chipGroupInclude.findViewById<Chip>(id)?.tag as? Int
        }.takeIf { it.isNotEmpty() }?.joinToString(",")
        val withoutGenres = binding.chipGroupExclude.checkedChipIds.mapNotNull { id ->
            binding.chipGroupExclude.findViewById<Chip>(id)?.tag as? Int
        }.takeIf { it.isNotEmpty() }?.joinToString(",")

        return MovieFilterParams(
            sortBy = sortBy,
            year = year,
            releaseDateGte = releaseDateGte,
            releaseDateLte = releaseDateLte,
            minVoteAverage = minVoteAverage,
            maxVoteAverage = maxVoteAverage,
            minVoteCount = minVoteCount,
            maxVoteCount = maxVoteCount,
            withOriginCountry = withOriginCountry,
            withOriginalLanguage = withOriginalLanguage,
            withGenres = withGenres,
            withoutGenres = withoutGenres
        )
    }

    private fun loadFilterParams() {
        viewModel.moviesFilterParamsState.value?.let { params ->
            params.sortBy?.let { sortBy ->
                val sortOption = MovieSortOption.entries.find { it.apiValue == sortBy }
                sortOption?.let { option ->
                    val displayText = getString(option.labelRes)
                    binding.sortBy.setText(displayText, false)
                }
            }

            params.year?.let { year ->
                binding.sliderYear.value = year.toFloat()
            }

            params.releaseDateGte?.let { date ->
                binding.releaseDateGte.setText(date)
            }
            params.releaseDateLte?.let { date ->
                binding.releaseDateLte.setText(date)
            }

            if (params.minVoteAverage != null && params.maxVoteAverage != null) {
                binding.voteAverage.setValues(params.minVoteAverage, params.maxVoteAverage)
            }

            if (params.minVoteCount != null && params.maxVoteCount != null) {
                binding.voteCount.setValues(params.minVoteCount.toFloat(), params.maxVoteCount.toFloat())
            }

            params.withOriginCountry?.let { countryCode ->
                val countryName = viewModel.countries.entries.find { it.value == countryCode }?.key
                countryName?.let { binding.originCountry.setText(it, false) }
            }

            params.withOriginalLanguage?.let { languageCode ->
                val languageName = viewModel.languages.entries.find { it.value == languageCode }?.key
                languageName?.let { binding.originalLanguage.setText(it, false) }
            }

            params.withGenres?.let { genresString ->
                val genreIds = genresString.split(",").mapNotNull { it.toIntOrNull() }
                loadGenreChips(genreIds, binding.chipGroupInclude)
            }

            params.withoutGenres?.let { genresString ->
                val genreIds = genresString.split(",").mapNotNull { it.toIntOrNull() }
                loadGenreChips(genreIds, binding.chipGroupExclude)
            }
        }
    }

    private fun loadGenreChips(genreIds: List<Int>, chipGroup: ChipGroup) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            chip?.let {
                val chipGenreId = it.tag as? Int
                if (chipGenreId in genreIds) {
                    it.isChecked = true
                }
            }
        }
    }

    private fun resetFilters() {
        viewModel.resetMovieFilters()


        binding.sortBy.setText(getString(MovieSortOption.POPULARITY_DESC.labelRes), false)

        binding.sliderYear.value = binding.sliderYear.valueFrom

        binding.releaseDateGte.setText("")
        binding.releaseDateLte.setText("")

        binding.voteAverage.setValues(0f, 10f)

        binding.voteCount.setValues(0f, 10000f)

        binding.originCountry.setText("")
        binding.originalLanguage.setText("")

        clearAllChips(binding.chipGroupInclude)
        clearAllChips(binding.chipGroupExclude)
    }

    private fun clearAllChips(chipGroup: ChipGroup) {
        for (i in 0 until chipGroup.childCount) {
            val chip = chipGroup.getChildAt(i) as? Chip
            chip?.isChecked = false
        }
    }

    companion object {
        const val DATE_GTE_TAG = "date_gte"
        const val DATE_LTE_TAG = "date_lte"
    }


}
package com.koniukhov.cinecirclex.feature.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.activityViewModels
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.datepicker.MaterialDatePicker
import com.koniukhov.cinecirclex.core.common.sort.TvSeriesSortOption
import com.koniukhov.cinecirclex.core.ui.base.BaseFragment
import com.koniukhov.cinecirclex.feature.search.R
import com.koniukhov.cinecirclex.feature.search.databinding.FragmentTvSeriesFiltersBinding
import com.koniukhov.cinecirclex.feature.search.model.TvSeriesFilterParams
import com.koniukhov.cinecirclex.feature.search.ui.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import java.text.Collator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.collections.get

class TvSeriesFiltersDialogFragment(private val onSearchClick: () -> Unit) :
    BaseFragment<FragmentTvSeriesFiltersBinding, SearchViewModel>() {

    override val viewModel: SearchViewModel by activityViewModels()

    private val collator = Collator.getInstance(Locale.getDefault())
    private val pairComparator: Comparator<Pair<String, String>> =
        Comparator { a, b -> collator.compare(a.first, b.first) }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTvSeriesFiltersBinding {
        return FragmentTvSeriesFiltersBinding.inflate(inflater, container, false)
    }

    override fun initViews() {
        setupDatePickers()
        setupDropdowns()
        setupFirstAirDateYearSlider()
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

    private fun setupFirstAirDateYearSlider() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toFloat()
        binding.firstAirDateYear.valueTo = currentYear
        if (binding.firstAirDateYear.value < binding.firstAirDateYear.valueFrom ||
            binding.firstAirDateYear.value > currentYear) {
            binding.firstAirDateYear.value = currentYear
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

        binding.firstAirDateGte.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.label_first_air_date_gte)
                .build()
            picker.addOnPositiveButtonClickListener { millis ->
                binding.firstAirDateGte.setText(formatter.format(Date(millis)))
            }
            picker.show(parentFragmentManager, FIRST_AIR_DATE_GTE_TAG)
        }

        binding.firstAirDateLte.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.label_first_air_date_lte)
                .build()
            picker.addOnPositiveButtonClickListener { millis ->
                binding.firstAirDateLte.setText(formatter.format(Date(millis)))
            }
            picker.show(parentFragmentManager, FIRST_AIR_DATE_LTE_TAG)
        }

        binding.airDateGte.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.label_air_date_gte)
                .build()
            picker.addOnPositiveButtonClickListener { millis ->
                binding.airDateGte.setText(formatter.format(Date(millis)))
            }
            picker.show(parentFragmentManager, AIR_DATE_GTE_TAG)
        }

        binding.airDateLte.setOnClickListener {
            val picker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.label_air_date_lte)
                .build()
            picker.addOnPositiveButtonClickListener { millis ->
                binding.airDateLte.setText(formatter.format(Date(millis)))
            }
            picker.show(parentFragmentManager, AIR_DATE_LTE_TAG)
        }
    }

    private fun setupDropdowns() {
        setupOriginCountryDropdown()
        setupOriginalLanguageDropdown()
        setupSortByDropdown()
    }

    private fun setupSortByDropdown() {
        val options = TvSeriesSortOption.entries
        val items = options.map { getString(it.labelRes) }
        binding.sortBy.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        )
        val defaultIndex = options.indexOf(TvSeriesSortOption.POPULARITY_DESC).takeIf { it >= 0 } ?: 0
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
            viewModel.selectTvSeriesFilters(getEnteredFilters())
            onSearchClick.invoke()
        }
    }

    private suspend fun populateGenreChipsAndLoadParams() {
        viewModel.tvSeriesGenres.collectLatest { map ->
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

    private fun getEnteredFilters(): TvSeriesFilterParams {
        val sortBy = TvSeriesSortOption.apiFromDisplayText(binding.sortBy.text.toString(), requireContext())
        val sliderYear = binding.firstAirDateYear.value.toInt()
        val year = if (sliderYear != binding.firstAirDateYear.valueFrom.toInt()) sliderYear else null
        val firstAirDateGte = binding.firstAirDateGte.text?.toString()?.ifBlank { null }
        val firstAirDateLte = binding.firstAirDateLte.text?.toString()?.ifBlank { null }
        val airDateGte = binding.airDateGte.text?.toString()?.ifBlank { null }
        val airDateLte = binding.airDateLte.text?.toString()?.ifBlank { null }
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

        return TvSeriesFilterParams(
            sortBy = sortBy,
            year = year,
            firstAirDateGte = firstAirDateGte,
            firstAirDateLte = firstAirDateLte,
            airDateGte = airDateGte,
            airDateLte = airDateLte,
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
        viewModel.tvSeriesFilterParamsState.value?.let { params ->
            params.sortBy?.let { sortBy ->
                val sortOption = TvSeriesSortOption.entries.find { it.apiValue == sortBy }
                sortOption?.let { option ->
                    val displayText = getString(option.labelRes)
                    binding.sortBy.setText(displayText, false)
                }
            }

            params.year?.let { year ->
                binding.firstAirDateYear.value = year.toFloat()
            }

            params.firstAirDateGte?.let { date ->
                binding.firstAirDateGte.setText(date)
            }
            params.firstAirDateLte?.let { date ->
                binding.firstAirDateLte.setText(date)
            }

            params.airDateGte?.let { date ->
                binding.airDateGte.setText(date)
            }
            params.airDateLte?.let { date ->
                binding.airDateLte.setText(date)
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
        viewModel.resetTvSeriesFilters()

        binding.sortBy.setText(getString(TvSeriesSortOption.POPULARITY_DESC.labelRes), false)

        binding.firstAirDateYear.value = binding.firstAirDateYear.valueFrom

        binding.firstAirDateGte.setText("")
        binding.firstAirDateLte.setText("")
        binding.airDateGte.setText("")
        binding.airDateLte.setText("")

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
        const val FIRST_AIR_DATE_GTE_TAG = "first_air_date_gte"
        const val FIRST_AIR_DATE_LTE_TAG = "first_air_date_lte"
        const val AIR_DATE_GTE_TAG = "air_date_gte"
        const val AIR_DATE_LTE_TAG = "air_date_lte"
    }
}
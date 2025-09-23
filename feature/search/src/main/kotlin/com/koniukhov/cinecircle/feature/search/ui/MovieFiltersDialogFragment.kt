package com.koniukhov.cinecircle.feature.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.google.android.material.datepicker.MaterialDatePicker
import com.koniukhov.cinecircle.core.common.sort.MovieSortOption
import com.koniukhov.cinecircle.feature.search.R
import com.koniukhov.cinecircle.feature.search.databinding.FragmentMovieFiltersBinding
import com.koniukhov.cinecircle.feature.search.model.MovieFilterParams
import com.koniukhov.cinecircle.feature.search.viewmodel.SearchViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MovieFiltersDialogFragment (private val onSearchClick: () -> Unit) : Fragment() {
    private var _binding: FragmentMovieFiltersBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SearchViewModel by activityViewModels()
    private val collator = java.text.Collator.getInstance(Locale.getDefault())
    private val pairComparator: Comparator<Pair<String, String>> =
        Comparator { a, b -> collator.compare(a.first, b.first) }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieFiltersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupDatePickers()
        setupDropdowns()
        populateGenreChips()
        setupYearSlider()
        setupVoteAverageRangeSlider()
        setupVoteCountRangeSlider()
        setupSearchBtn()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupYearSlider() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toFloat()
        binding.sliderYear.valueTo = currentYear
        if (binding.sliderYear.value < binding.sliderYear.valueFrom || binding.sliderYear.value > currentYear) {
            binding.sliderYear.value = currentYear
        }
    }

    private fun setupVoteAverageRangeSlider() {
        with(binding.rsVoteAverage) {
            setValues(0f, 10f)
            stepSize = 0.1f
            setLabelFormatter { value -> String.format(Locale.getDefault(), "%.1f", value) }
        }
    }

    private fun setupVoteCountRangeSlider() {
        with(binding.rsVoteCount) {
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
            .sortedWith (pairComparator)
            .map { it.first }
        binding.originCountry.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        )
    }

    private fun setupOriginalLanguageDropdown() {
        val items = viewModel.languages
            .toList()
            .sortedWith (pairComparator)
            .map { it.first }
        binding.originalLanguage.setAdapter(
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, items)
        )
    }

    private fun setupSearchBtn() {
        binding.btnApply.setOnClickListener {
            viewModel.updateFilters(getEnteredFilters())
            onSearchClick.invoke()
        }
    }

    private fun populateGenreChips() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(androidx.lifecycle.Lifecycle.State.STARTED) {
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
                }
            }
        }
    }

    private fun getEnteredFilters(): MovieFilterParams {
        val sortBy = MovieSortOption.apiFromDisplayText(binding.sortBy.text.toString(), requireContext())
        val sliderYear = binding.sliderYear.value.toInt()
        val year = if (sliderYear != binding.sliderYear.valueFrom.toInt()) sliderYear else null
        val releaseDateGte = binding.releaseDateGte.text?.toString()?.ifBlank { null }
        val releaseDateLte = binding.releaseDateLte.text?.toString()?.ifBlank { null }
        val minVoteAverage = binding.rsVoteAverage.values.getOrNull(0)
        val maxVoteAverage = binding.rsVoteAverage.values.getOrNull(1)
        val minVoteCount = binding.rsVoteCount.values.getOrNull(0)?.toInt()
        val maxVoteCount = binding.rsVoteCount.values.getOrNull(1)?.toInt()
        val withOriginCountry = viewModel.countries[binding.originCountry.text?.toString()]
        val withOriginalLanguage = viewModel.languages[binding.originalLanguage.text?.toString()]
        val withGenres = binding.chipGroupInclude.checkedChipIds.mapNotNull { id ->
            binding.chipGroupInclude.findViewById<Chip>(id)?.tag as? Int
        }.takeIf { it.isNotEmpty() }?.joinToString(",")
        val withoutGenres = binding.chipGroupExclude.checkedChipIds.map { id ->
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

    companion object {
        const val DATE_GTE_TAG = "date_gte"
        const val DATE_LTE_TAG = "date_lte"
    }


}
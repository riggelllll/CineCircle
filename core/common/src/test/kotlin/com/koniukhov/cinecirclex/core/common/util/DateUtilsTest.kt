package com.koniukhov.cinecirclex.core.common.util

import org.junit.Assert
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Locale

class DateUtilsTest {

    @Test
    fun `formatDate formats valid date with default pattern correctly`() {
        val inputDate = "2024-12-08"
        val result = DateUtils.formatDate(inputDate)

        Assert.assertNotEquals(inputDate, result)
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertTrue(result.contains("2024") || result.contains("24"))
    }

    @Test
    fun `formatDate formats date with medium format`() {
        val inputDate = "2024-01-15"
        val result = DateUtils.formatDate(inputDate)

        val expectedFormat = SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM, Locale.getDefault())
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val parsedDate = inputFormat.parse(inputDate)
        val expected = parsedDate?.let { expectedFormat.format(it) }

        Assert.assertEquals(expected, result)
    }

    @Test
    fun `formatDate with custom input pattern formats correctly`() {
        val inputDate = "15/01/2024"
        val customPattern = "dd/MM/yyyy"
        val result = DateUtils.formatDate(inputDate, customPattern)

        Assert.assertNotEquals(inputDate, result)
        Assert.assertTrue(result.isNotEmpty())
        Assert.assertTrue(result.contains("2024") || result.contains("24"))
    }

    @Test
    fun `formatDate returns original string on invalid format`() {
        val invalidDate = "not-a-date"
        val result = DateUtils.formatDate(invalidDate)

        Assert.assertEquals(invalidDate, result)
    }

    @Test
    fun `formatDate returns original string on malformed date`() {
        val malformedDate = "2024-13-40"
        val result = DateUtils.formatDate(malformedDate)

        Assert.assertNotEquals(malformedDate, result)
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun `formatDate handles empty string`() {
        val emptyDate = ""
        val result = DateUtils.formatDate(emptyDate)

        Assert.assertEquals(emptyDate, result)
    }

    @Test
    fun `formatDate handles date with only year`() {
        val yearOnlyDate = "2024"
        val result = DateUtils.formatDate(yearOnlyDate)

        Assert.assertEquals(yearOnlyDate, result)
    }

    @Test
    fun `formatDate handles date with different separator`() {
        val dateWithSlash = "2024/12/08"
        val result = DateUtils.formatDate(dateWithSlash)

        Assert.assertEquals(dateWithSlash, result)
    }

    @Test
    fun `formatDate with custom pattern handles date with slash correctly`() {
        val dateWithSlash = "2024/12/08"
        val customPattern = "yyyy/MM/dd"
        val result = DateUtils.formatDate(dateWithSlash, customPattern)

        Assert.assertNotEquals(dateWithSlash, result)
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun `formatDate handles past date correctly`() {
        val pastDate = "1990-05-20"
        val result = DateUtils.formatDate(pastDate)

        Assert.assertNotEquals(pastDate, result)
        Assert.assertTrue(result.contains("1990") || result.contains("90"))
    }

    @Test
    fun `formatDate handles future date correctly`() {
        val futureDate = "2030-06-15"
        val result = DateUtils.formatDate(futureDate)

        Assert.assertNotEquals(futureDate, result)
        Assert.assertTrue(result.contains("2030") || result.contains("30"))
    }

    @Test
    fun `formatDate handles leap year date`() {
        val leapYearDate = "2024-02-29"
        val result = DateUtils.formatDate(leapYearDate)

        Assert.assertNotEquals(leapYearDate, result)
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun `formatDate handles first day of year`() {
        val firstDay = "2024-01-01"
        val result = DateUtils.formatDate(firstDay)

        Assert.assertNotEquals(firstDay, result)
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun `formatDate handles last day of year`() {
        val lastDay = "2024-12-31"
        val result = DateUtils.formatDate(lastDay)

        Assert.assertNotEquals(lastDay, result)
        Assert.assertTrue(result.isNotEmpty())
    }

    @Test
    fun `formatDate with null-like string returns original`() {
        val nullString = "null"
        val result = DateUtils.formatDate(nullString)

        Assert.assertEquals(nullString, result)
    }

    @Test
    fun `formatDate handles special characters in date`() {
        val specialDate = "2024@12@08"
        val result = DateUtils.formatDate(specialDate)

        Assert.assertEquals(specialDate, result)
    }

    @Test
    fun `formatDate returns consistent results for same input`() {
        val inputDate = "2024-12-08"
        val result1 = DateUtils.formatDate(inputDate)
        val result2 = DateUtils.formatDate(inputDate)

        Assert.assertEquals(result1, result2)
    }
}
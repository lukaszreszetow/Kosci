import Game.Companion.RESULT_CROSS_OUT
import Game.Companion.RESULT_LEAVE
import java.lang.Exception
import java.nio.channels.SelectionKey
import java.util.*

fun Map<String, Array<Int>>.getCrossOutFromResult() =
    getOrDefault(RESULT_CROSS_OUT, arrayOf(SectionRow.NOTHING.numericVal))

fun Map<String, Array<Int>>.getLeftDicesFromResult() = getOrDefault(RESULT_LEAVE, emptyArray())

fun Int.mapValueToSectionRow() = SectionRow.values().first { it.numericVal == this }

fun SectionRow.calculatePoints(dices: Array<Int>): Int {
    return calculateFromDices(dices)
}

fun calculateAcesPoints(dices: Array<Int>): Int {
    return dices.filter { it == 1 }.sum()
}

fun calculateTwosPoints(dices: Array<Int>): Int {
    return dices.filter { it == 2 }.sum()
}

fun calculateThreesPoints(dices: Array<Int>): Int {
    return dices.filter { it == 3 }.sum()
}

fun calculateFoursPoints(dices: Array<Int>): Int {
    return dices.filter { it == 4 }.sum()
}

fun calculateFivesPoints(dices: Array<Int>): Int {
    return dices.filter { it == 5 }.sum()
}

fun calculateSixesPoints(dices: Array<Int>): Int {
    return dices.filter { it == 6 }.sum()
}

fun calculateThreeOfAKindPoints(dices: Array<Int>): Int {
    val maxPair =
        (1..6).sortedDescending().map { value -> value to dices.count { dice -> dice == value } }.maxBy { it.second }
    return if (maxPair != null && maxPair.second >= 3) {
        dices.sum()
    } else {
        0
    }
}

fun calculateFourOfAKindPoints(dices: Array<Int>): Int {
    val maxPair =
        (1..6).sortedDescending().map { value -> value to dices.count { dice -> dice == value } }.maxBy { it.second }
    return if (maxPair != null && maxPair.second >= 4) {
        dices.sum()
    } else {
        0
    }
}

fun calculateYahtzee(dices: Array<Int>): Int {
    val maxPair =
        (1..6).sortedDescending().map { value -> value to dices.count { dice -> dice == value } }.maxBy { it.second }
    return if (maxPair != null && maxPair.second >= 5) {
        50
    } else {
        0
    }
}

fun calculateFullHouse(dices: Array<Int>): Int {
    val threeTimesValue = dices.firstOrNull { diceValue -> dices.count { it == diceValue } == 3 }
    val twoTimesValue = dices.firstOrNull { diceValue -> dices.count { it == diceValue } == 2 }
    return if (threeTimesValue != null && twoTimesValue != null) {
        25
    } else {
        0
    }
}

fun calculateSmallStraight(dices: Array<Int>): Int {
    val possibilities = listOf(listOf(1, 2, 3, 4), listOf(2, 3, 4, 5), listOf(3, 4, 5, 6))
    val contains = possibilities.any { possibility -> possibility.all { dices.contains(it) } }
    return if (contains) {
        30
    } else {
        0
    }
}

fun calculateLargeStraight(dices: Array<Int>): Int {
    val possibilities = listOf(listOf(1, 2, 3, 4, 5), listOf(2, 3, 4, 5, 6))
    val contains = possibilities.any { possibility -> possibility.all { dices.contains(it) } }
    return if (contains) {
        40
    } else {
        0
    }
}


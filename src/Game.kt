import java.lang.Exception
import kotlin.random.Random

class Game(private val players: List<Player>) {

    var playersSections = players.map { PlayerSection(it.name) }
    var roundNumber = 1

    fun start() {
        while (roundNumber <= 13) {
            println()
            println("Playing round $roundNumber")
            println()
            playWholeRound()
            roundNumber++
        }
        println("Game has ended! Final results:")
        playersSections.sortedByDescending { it.finalResult }.forEachIndexed { index, playerSection ->
            println("Player ${playerSection.name} got ${playerSection.finalResult} points and got ${index + 1} place!")
        }
    }

    private fun playWholeRound() {
        players.forEachIndexed { index, player ->
            playRoundForPlayer(player, index)
            println()
        }
    }

    private fun playRoundForPlayer(player: Player, playerIndex: Int) {
        println("Player ${player.name} starts his round")
        val firstDices = getRolledDices()
        val leftDicesAfterFirst = playSingleRound(0, player, playerIndex, firstDices)
        if (leftDicesAfterFirst != null) {
            println("Player ${player.name} starts his second round")
            val secondDices = getRolledDices(leftDicesAfterFirst)
            val leftDicesAfterSecond = playSingleRound(1, player, playerIndex, secondDices)
            if (leftDicesAfterSecond != null) {
                println("Player ${player.name} starts his third round")
                val thirdDices = getRolledDices(leftDicesAfterSecond)
                playSingleRound(2, player, playerIndex, thirdDices)
            }
        }
    }

    private fun getRolledDices(leftDices: Array<Int> = emptyArray()): Array<Int> {
        return rollDices(5 - leftDices.size).toMutableList().apply { addAll(leftDices.toList()) }.toTypedArray()
    }

    private fun rollDices(numberOfDices: Int): Array<Int> {
        return Array(numberOfDices) { Random.nextInt(1, 7) }
    }

    private fun playSingleRound(rollNumber: Int, player: Player, playerIndex: Int, dices: Array<Int>): Array<Int>? {
        printDices(dices)
        val firstRoundResult = player.onDiceRoll(rollNumber, dices)
        val crossAfterFirst = firstRoundResult.getCrossOutFromResult()
        val leftDicesAfterFirst = firstRoundResult.getLeftDicesFromResult()
        if (crossAfterFirst[0] != SectionRow.NOTHING.numericVal) {
            finishRoundForPlayer(playerIndex, dices, crossAfterFirst[0])
            return null
        }
        cheatCheck(dices, leftDicesAfterFirst)
        return leftDicesAfterFirst
    }

    private fun printDices(dices: Array<Int>) {
        println("Dices are: [${dices.joinToString(", ")}]")
    }

    private fun cheatCheck(dices: Array<Int>, leftDices: Array<Int>): Boolean {
        var availableDices = dices.toMutableList()
        return leftDices.all { leftDice ->
            val firstIndexOf = availableDices.indexOfFirst { it == leftDice }
            if (firstIndexOf != -1) {
                availableDices.removeAt(firstIndexOf)
                true
            } else {
                println("CHEATER! CHEATER")
                throw Exception("CHEATER!")
            }
        }
    }


    private fun finishRoundForPlayer(playerIndex: Int, dices: Array<Int>, selectedResult: Int) {
        println("Player finished his round with dices [${dices.joinToString(", ")}] selecting result ${selectedResult.mapValueToSectionRow().name}")
        val sectionRow = getAvailableSectionRow(playerIndex, selectedResult).mapValueToSectionRow()
        val pointsAcquired = sectionRow.calculatePoints(dices)
        playersSections[playerIndex].updateResult(sectionRow, pointsAcquired)
    }

    private fun getAvailableSectionRow(playerIndex: Int, selectedResult: Int): Int {
        return when {
            selectedResult == SectionRow.NOTHING.numericVal -> {
                println("Player selected no cross out result. Returning some free row.")
                playersSections[playerIndex].section.first { !it.crossedOut }.sectionRow.numericVal
            }
            playersSections[playerIndex].section.first { it.sectionRow.numericVal == selectedResult }.crossedOut -> {
                println("This player has already crossed out this result! Returning some other free row.")
                playersSections[playerIndex].section.first { !it.crossedOut }.sectionRow.numericVal
            }
            else -> {
                selectedResult
            }
        }
    }

    companion object {
        const val RESULT_LEAVE = "leave"
        const val RESULT_CROSS_OUT = "cross_out"
    }

}


class PlayerSection(val name: String) {
    var section = SectionRow.values().map { Row(it) }

    fun updateResult(sectionRow: SectionRow, points: Int) {
        val rowToChange = section.first { it.sectionRow == sectionRow }
        val refreshedSectionRow = rowToChange.copy(points = points, crossedOut = true)
        section = section.toMutableList().apply {
            remove(rowToChange)
            add(refreshedSectionRow)
            sortBy { it.sectionRow.numericVal }
        }
    }

    val finalResult get() = section.map { it.points }.sum() + bonusForTopSection()

    private fun bonusForTopSection(): Int {
        val topSectionCount = (1..6).sumBy { section[it].points }
        return if (topSectionCount >= 63) {
            35
        } else {
            0
        }
    }
}

data class Row(val sectionRow: SectionRow, val points: Int = 0, val crossedOut: Boolean = false)
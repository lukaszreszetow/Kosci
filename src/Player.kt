/**
 * Przykład:
 * gra wysyła onDiceRoll(0, [1,3,1,6,5])
 * - rollInRound bedzie 0, 1, 2
 * Dostępne opcje w odpowiedzi:
 * - String: "leave", Array: na przyklad [1,1] (gra nie przerzuci wam dwóch jedynek)
 * - String: "cross_out", Array: na przykład [1] (Przekreślamy wartość z enuma SectionRow,
 * jeżeli wyślę cross_out 1 to znaczy, że chcę przekreślić ACES. Jeżeli nic nie chcecie przekreślać, to wyślicie 0
 * jeżeli [rollInRound] w [onDiceRoll] było 2 to MUSICIE wysłać jak cross_out cos innego niz 0!!
 *
 */
interface Player {
    val name: String
    fun onDiceRoll(rollInRound: Int, rolls: Array<Int>): Map<String, Array<Int>>
}


//nazwy wziete z https://en.wikipedia.org/wiki/Yahtzee
enum class SectionRow(val numericVal: Int, val calculateFromDices: (Array<Int>) -> Int) {
    NOTHING(0, { 0 }),
    ACES(1, { calculateAcesPoints(it) }),
    TWOS(2, { calculateTwosPoints(it) }),
    THREES(3, { calculateThreesPoints(it) }),
    FOURS(4, { calculateFoursPoints(it) }),
    FIVES(5, { calculateFivesPoints(it) }),
    SIXES(6, { calculateSixesPoints(it) }),
    THREE_OF_A_KIND(7, { calculateThreeOfAKindPoints(it) }),
    FOUR_OF_A_KIND(8, { calculateFourOfAKindPoints(it) }),
    FULL_HOUSE(9, { calculateFullHouse(it) }),
    SMALL_STRAIGHT(10, { calculateSmallStraight(it) }),
    LARGE_STRAIGHT(11, { calculateLargeStraight(it) }),
    YAHTZEE(12, { calculateYahtzee(it) }),
    CHANCE(13, { it.sum() })
}

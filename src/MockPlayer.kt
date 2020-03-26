class MockPlayer(private val fakeName: String, private val whenToStop: Int) : Player {

    var roundCounter = 1
    override val name: String
        get() = fakeName

    override fun onDiceRoll(rollInRound: Int, rolls: Array<Int>): Map<String, Array<Int>> {
        return if (rollInRound != whenToStop) {
            mapOf("leave" to emptyArray(), "cross_out" to arrayOf(0))
        } else {
            mapOf("leave" to emptyArray(), "cross_out" to arrayOf(roundCounter++))
        }
    }

}
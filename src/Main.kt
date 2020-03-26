fun main() {
    val player = MockJavaPlayer()
    val game = Game(listOf(player))
    game.start()
}
class Position(
    val symbolicPosition: IntArray = (0 until 72).toList().toIntArray()
) {
    companion object {
        // the "triangles" are well positioned, no constraint for the others
        val firstLevelPattern = (0 until 72).map { if ((it % 9) in listOf(1, 3, 6)) it else -1 }.toIntArray()

        // the "hexagones" are well positioned, no constraint for the others
        val secondLevelPattern = (0 until 72).map { if ((it % 9) in listOf(0, 4, 8)) -1 else it }.toIntArray()

        // Everything is well positioned
        val thirdLevelPattern = (0 until 72).toList().toIntArray()
    }

    fun numDifferencesFirstLevelPattern() = LowLevelHelpers.differences(symbolicPosition, firstLevelPattern)
    fun numDifferencesSecondLevelPattern() = LowLevelHelpers.differences(symbolicPosition, secondLevelPattern)
    fun numDifferencesThirdLevelPattern() = LowLevelHelpers.differences(symbolicPosition, thirdLevelPattern)

    fun apply(move: Move, optionalBuffer: IntArray? = null): Position {
        return Position(LowLevelHelpers.permut(symbolicPosition, move.permutation, optionalBuffer))
    }

}
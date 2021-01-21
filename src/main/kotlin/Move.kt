class Move(
    val shortName: String,
    val name: String,
    val face: Int, // See comment below for explanation on faces
    val permutation: IntArray
) {
    companion object {
        // Those are the canonical moves. Note that you could replace those to solve the actual Rubik's cube
        // The Octaedre is composed of 2 pyramids glued one on top of the other
        // The faces of the first pyramid are 0, 1, 2, 3
        // The faces of the other pyramid are 4, 5, 6, 7, in such a way that
        // face 0 is next to side 1, 4, and 3, and face 1 is next to side 0, 5 and 2.
        val F0C = Move("F0C", "Turn face 0 clockwise", 0, arrayOf(4, 6, 5, 1, 8, 7, 3, 2, 0, 35, 30, 34, 12, 27, 29, 15, 16, 17, 67, 19, 20, 21, 22, 23, 24, 25, 26, 44, 28, 43, 42, 31, 32, 33, 41, 40, 36, 37, 38, 39, 9, 11, 10, 14, 13, 45, 46, 47, 48, 49, 50, 51, 52, 18, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 53, 68, 69, 70, 71).toIntArray())
        val F0A = Move("F0A", "Turn face 0 anti-clockwise", 0, LowLevelHelpers.permut(F0C.permutation, F0C.permutation, null))

        val F1C = Move("F1C", "Turn face 1 clockwise", 1, arrayOf(53, 1, 52, 51, 4, 5, 6, 50, 49, 13, 15, 14, 10, 17, 16, 12, 11, 9, 8, 3, 7, 21, 0, 2, 24, 25, 26, 40, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 62, 41, 42, 43, 44, 45, 46, 47, 48, 18, 20, 19, 23, 22, 54, 55, 56, 57, 58, 59, 60, 61, 27, 63, 64, 65, 66, 67, 68, 69, 70, 71).toIntArray())
        val F1A = Move("F1A", "Turn face 1 anti-clockwise", 1, LowLevelHelpers.permut(F1C.permutation, F1C.permutation, null))

        val F2C = Move("F2C", "Turn face 2 clockwise", 2, arrayOf(49, 1, 2, 3, 4, 5, 6, 7, 8, 62, 10, 61, 60, 13, 14, 15, 59, 58, 22, 24, 23, 19, 26, 25, 21, 20, 18, 17, 12, 16, 30, 9, 11, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 71, 50, 51, 52, 53, 54, 55, 56, 57, 27, 29, 28, 32, 31, 63, 64, 65, 66, 67, 68, 69, 70, 0).toIntArray())
        val F2A = Move("F2A", "Turn face 2 anti-clockwise", 2, LowLevelHelpers.permut(F2C.permutation, F2C.permutation, null))

        val F3C = Move("F3C", "Turn face 3 clockwise", 3, arrayOf(26, 21, 25, 3, 18, 20, 6, 7, 8, 58, 10, 11, 12, 13, 14, 15, 16, 17, 71, 19, 70, 69, 22, 23, 24, 68, 67, 31, 33, 32, 28, 35, 34, 30, 29, 27, 36, 37, 38, 39, 40, 41, 42, 43, 9, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 44, 59, 60, 61, 62, 63, 64, 65, 66, 0, 2, 1, 5, 4).toIntArray())
        val F3A = Move("F3A", "Turn face 3 anti-clockwise", 3, LowLevelHelpers.permut(F3C.permutation, F3C.permutation, null))

        val F4C = Move("F4C", "Turn face 4 clockwise", 4, arrayOf(0, 1, 2, 3, 63, 65, 64, 68, 67, 9, 10, 11, 12, 35, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 54, 40, 42, 41, 37, 44, 43, 39, 38, 36, 8, 46, 7, 6, 49, 50, 51, 5, 4, 13, 55, 56, 57, 58, 59, 60, 61, 62, 53, 48, 52, 66, 45, 47, 69, 70, 71).toIntArray())
        val F4A = Move("F4A", "Turn face 4 anti-clockwise", 4, LowLevelHelpers.permut(F4C.permutation, F4C.permutation, null))

        val F5C = Move("F5C", "Turn face 5 clockwise", 5, arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 63, 9, 10, 11, 12, 36, 38, 37, 41, 40, 18, 19, 20, 21, 8, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 62, 57, 61, 39, 54, 56, 42, 43, 44, 49, 51, 50, 46, 53, 52, 48, 47, 45, 17, 55, 16, 15, 58, 59, 60, 14, 13, 22, 64, 65, 66, 67, 68, 69, 70, 71).toIntArray())
        val F5A = Move("F5A", "Turn face 5 anti-clockwise", 5, LowLevelHelpers.permut(F5C.permutation, F5C.permutation, null))

        val F6C = Move("F6C", "Turn face 6 clockwise", 6, arrayOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 36, 18, 19, 20, 21, 45, 47, 46, 50, 49, 27, 28, 29, 30, 17, 32, 33, 34, 35, 31, 37, 38, 39, 40, 41, 42, 43, 44, 71, 66, 70, 48, 63, 65, 51, 52, 53, 58, 60, 59, 55, 62, 61, 57, 56, 54, 26, 64, 25, 24, 67, 68, 69, 23, 22).toIntArray())
        val F6A = Move("F6A", "Turn face 6 anti-clockwise", 6, LowLevelHelpers.permut(F6C.permutation, F6C.permutation, null))

        val F7C = Move("F7C", "Turn face 7 clockwise", 7, arrayOf(0, 1, 2, 3, 26, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 45, 27, 28, 29, 30, 54, 56, 55, 59, 58, 35, 37, 34, 33, 40, 41, 42, 32, 31, 4, 46, 47, 48, 49, 50, 51, 52, 53, 44, 39, 43, 57, 36, 38, 60, 61, 62, 67, 69, 68, 64, 71, 70, 66, 65, 63).toIntArray())
        val F7A = Move("F7A", "Turn face 7 anti-clockwise", 7, LowLevelHelpers.permut(F7C.permutation, F7C.permutation, null))

        val canonicalMoves = listOf(F0C, F0A, F1C, F1A, F2C, F2A, F3C, F3A, F4C, F4A, F5C, F5A, F6C, F6A, F7C, F7A)
        private val oppositeFace = arrayOf(6, 7, 4, 5, 2, 3, 0, 1)
    }

    fun hasOppositeFace(move: Move) = move.face == oppositeFace[face]
}


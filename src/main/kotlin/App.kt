import java.util.*

fun explore(
    current: Position,
    path: Stack<Move>,
    maxDepth: Int,
    evaluatePosition: (current: Position, path: Stack<Move>) -> Unit
) {
    evaluatePosition(current, path)
    if (path.size == maxDepth) {
        return
    }
    val lastMove = path.peek()
    val nextMoves = Move.canonicalMoves.filter { move ->
        // We are not moving the same face twice.
        // Also not moving opposite face one after the other. No justification, it just didn't feel... necessary. But I can't prove it
        lastMove.face != move.face && !lastMove.hasOppositeFace(move)
    }

    // In order to exploit as much as possible multi core, we want to make things parallel....
    // But not too much (because otherwise most of the compute is spent handling the parallelization)
    // Therefore for depth < 5, we spread accross thread, but above that we keep it single threaded.
    if (path.size > 4) {
        // The following code, which use efficient buffering, is 2x faster. But can't be parallelized.
        val buffer = IntArray(current.symbolicPosition.size)
        nextMoves.forEach { move ->
            val newPosition = current.apply(move, buffer)
            path.push(move)
            explore(newPosition, path, maxDepth, evaluatePosition)
            path.pop()
        }
    }
    else {
        nextMoves.parallelStream().forEach { move ->
            val newPosition = current.apply(move)
            val newPath = path.clone() as Stack<Move>
            newPath.push(move)
            explore(newPosition, newPath, maxDepth, evaluatePosition)
        }
    }

}

fun main() {
    var startPosition = Position()
    val startPath = Stack<Move>()

    listOf(Move.F0C, Move.F4C).forEach {
        startPosition = startPosition.apply(it)
        startPath.push(it)
    }

    explore(startPosition, startPath, maxDepth = 8) { currrentPosition, currentPath ->
        // Leaves the triangles invariant, and less than 3 differences in the hexagons
        if (currrentPosition.numDifferencesFirstLevelPattern() == 0 && currrentPosition.numDifferencesSecondLevelPattern() in 1..3) {
            println("We found an interesting move: " + currentPath.map { it.shortName }.joinToString(" "))
        }
    }
    
}
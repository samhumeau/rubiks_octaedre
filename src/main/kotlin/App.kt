import java.util.*
import java.util.concurrent.atomic.AtomicLong

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
        // We are not moving the same face twice. Also not moving opposite face one after the other.
        // This last part is intuition
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
    val path = Stack<Move>()

    listOf(Move.F0C, Move.F4C, Move.F0A, Move.F5C).forEach {
        startPosition = startPosition.apply(it)
        path.push(it)
    }

    val start = System.currentTimeMillis()
    var numPos = AtomicLong(0)
    explore(startPosition, path, 12) { currrentPosition, currentPath ->
        numPos.addAndGet(1)
        if (currrentPosition.numDifferencesSecondLevelPattern() == 0 && currrentPosition.numDifferencesThirdLevelPattern() in 1..12) {
            println("We found an interesting move: " + currentPath.map { it.shortName }.joinToString(" "))
        }
    }
    val end = System.currentTimeMillis()
    println(end - start)
    println(numPos)


}
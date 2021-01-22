import java.util.concurrent.atomic.AtomicLong

fun explore(
    current: Position,
    path: Path<Move>,
    maxDepth: Int,
    evaluatePosition: (current: Position, path: Path<Move>) -> Unit
) {
    evaluatePosition(current, path)
    if (path.size == maxDepth) return

    val lastMove = path.current
    val nextMoves = Move.canonicalMoves.filter { move ->
        // We are not moving the same face twice.
        // Also not moving opposite face one after the other. No justification, it just didn't feel... necessary. But I can't prove it
        lastMove.face != move.face && !lastMove.hasOppositeFace(move)
    }

    // In order to exploit as much as possible multi core, we want to make things parallel....
    // But not too much (because otherwise most of the compute is spent handling the parallelization)
    // Therefore for depth < 5, we spread accross thread, but above that we keep it single threaded.
    nextMoves.forEach(parallelIf = path.size < 5) { move ->
        val newPosition = current.apply(move)
        explore(newPosition, path.withNewElement(move), maxDepth, evaluatePosition)
    }
}

fun main() {
    val startPosition = Position().apply(Move.F0C).apply(Move.F4C)
    val startPath = Path(Move.F0C).withNewElement(Move.F4C)

    val start = System.currentTimeMillis()
    val numberPossibilitiesExplored = AtomicLong()
    explore(startPosition, startPath, maxDepth = 10) { currrentPosition, currentPath ->
        // Leaves the triangles invariant, and less than 3 differences in the hexagons
        numberPossibilitiesExplored.addAndGet(1)
        if (currrentPosition.matchesFirstLevelPattern() && currrentPosition.numDifferencesSecondLevelPattern() in 1..3) {
            println("We found an interesting move: " + currentPath.toList().joinToString(" ") { it.shortName })
        }
    }
    val end = System.currentTimeMillis()
    print("Done. Explored $numberPossibilitiesExplored combinations in ${end - start}ms (${numberPossibilitiesExplored.get() * 1000 / (end - start)} per second)")

}
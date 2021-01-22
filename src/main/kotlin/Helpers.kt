// This turned out to be slightly faster than java's Stack implementation
class Path<T> (
    val current: T,
    val previous : Path<T>? = null
) {
    val size : Int = 1 + (previous?.size ?: 0)
    fun withNewElement(elem: T) : Path<T> {
        return Path(elem, this)
    }

    fun toList(): List<T> {
        var path = this
        val res = mutableListOf<T>()
        while (true){
            res.add(path.current)
            val previousPath = path.previous ?: break
            path = previousPath

        }
        return res.reversed()
    }
}

// A forEach operator that is conditionally parallel
fun <T> List<T>.forEach(parallelIf: Boolean, block: (T) -> Unit) {
    if (parallelIf) {
        this.parallelStream().forEach(block)
    }
    else {
        this.forEach(block)
    }
}

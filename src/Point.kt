data class Point(val x: Int, val y: Int) {
    operator fun plus(shift: Point) = Point(x + shift.x, y + shift.y)
}


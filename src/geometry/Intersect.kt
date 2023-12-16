package geometry

import kotlin.math.max
import kotlin.math.min

fun intersect(a: Point, b: Point, c: Point, d: Point) : Boolean = intersect(a.x, b.x, c.x, d.x)
        && intersect(a.y, b.y, c.y, d.y)
        && area(a,b,c) * area(a,b,d) <= 0
        && area(c,d,a) * area(c,d,b) <= 0

private fun area(a: Point, b: Point, c: Point) : Int = (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x)

private fun intersect(a: Int, b: Int, c: Int, d: Int) : Boolean = when {
    a > b -> intersect(b, a, c, d)
    c > d -> intersect(a, d, d, c)
    else -> max(a, c) <= min(b, d)
}


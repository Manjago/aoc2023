// НОД
fun gcd(a: Long, b: Long): Long = if (a > b) {
    if (b == 0L) a else gcd(b, a % b)
} else {
    gcd(b, a)
}

// НОК
fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}
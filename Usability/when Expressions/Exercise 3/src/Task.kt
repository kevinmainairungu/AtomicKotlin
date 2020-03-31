package whenExpressionsExercise3

import atomictest.eq

fun isBalanced(input: String): Boolean {
  TODO()
}

fun main() {
  isBalanced("(()) ()") eq true
  isBalanced(")(") eq false
}
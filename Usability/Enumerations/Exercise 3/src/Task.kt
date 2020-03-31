package enumerationsExercise3

import atomictest.eq
import enumerationsExercise3.Result.*
import enumerationsExercise3.Rochambeau.*

enum class Rochambeau {
  ROCK, PAPER, SCISSORS
}

enum class Result {
  DRAW, FIRST_WINS, SECOND_WINS
}

fun findWinner(first: Rochambeau, second: Rochambeau): Result {
  TODO()
}

fun main() {
  findWinner(ROCK, SCISSORS) eq FIRST_WINS
  findWinner(SCISSORS, ROCK) eq SECOND_WINS
  findWinner(PAPER, PAPER) eq DRAW
}
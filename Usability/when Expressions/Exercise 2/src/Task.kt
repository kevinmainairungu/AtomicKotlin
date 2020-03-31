package whenExpressionsExercise2

import atomictest.eq

fun getTemperatureDescription(temperature: Int): String =
  TODO()

fun main() {
  getTemperatureDescription(30) eq "Hot"
  getTemperatureDescription(10) eq "Cool"
  getTemperatureDescription(-30) eq "Freezing"
}
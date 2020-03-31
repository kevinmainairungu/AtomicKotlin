// LocalFunctions/ReturnInsideLambda.kt
package localfunctions
import atomictest.eq

fun main() {
  sessions.any { session ->
    if (session.title.contains("Kotlin") &&
      session.speaker in myFavSpeakers) {
      return@any true
    }
    // ... more checks
    false
  } eq true
}
package constructorsExercise1

import org.junit.Assert
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runners.MethodSorters
import util.TIMEOUT
import util.checkMainIsImplemented
import util.checkParametersOfConstructor
import util.loadClass

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class TestConstructorsExercise1 {
  @Test(timeout = TIMEOUT)
  fun test1() {
    val floatingClass = loadClass("constructorsExercise1", "Floating")
    val floatingConstructor = floatingClass.constructors.first()
    checkParametersOfConstructor(floatingConstructor, floatingClass, listOf("d" to "kotlin.Double"))
    val floatingInstance = floatingConstructor.call(1.0)
    Assert.assertEquals("Wrong value for 'Floating(1.0).toString()", "1.0", floatingInstance.toString())
  }

  @Test(timeout = TIMEOUT)
  fun test2() {
    checkMainIsImplemented(::main)
  }
}
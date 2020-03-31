package util

import org.junit.Assert
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.lang.reflect.Method
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.KParameter
import kotlin.reflect.KProperty
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

const val TIMEOUT = 3000L

val LINE_SEPARATOR: String = System.getProperty("line.separator")

fun String.normalizeLineSeparators(): String {
  return replace("\\R".toRegex(), LINE_SEPARATOR)
}

inline fun runAndGetSystemOutput(action: () -> Unit): String {
  val byteArrayOutputStream = ByteArrayOutputStream()
  System.setOut(PrintStream(byteArrayOutputStream))

  action()

  return byteArrayOutputStream.toString()
}

inline fun runAndCheckSystemOutput(message: String, expectedOutput: String, action: () -> Unit) {
  val actual = runAndGetSystemOutput(action)
  checkSystemOutput(message, expectedOutput, actual)
}

fun checkSystemOutput(message: String, expected: String, actual: String) {
  Assert.assertEquals(message, expected.trim().normalizeLineSeparators(), actual.trim().normalizeLineSeparators())
}

fun checkInputOutput(message: String, expectedInputAndOutput: String, action: () -> Unit) {
  val (inputLines, outputLines) = expectedInputAndOutput
    .lines()
    .partition { it.startsWith(">>>") }
  val input = inputLines.joinToString(LINE_SEPARATOR) { it.substringAfter(">>> ") }
  val expectedOutput = outputLines.joinToString(LINE_SEPARATOR)

  checkInputOutput(message, input, expectedOutput, action)
}

fun checkInputOutput(message: String, input: String, expectedOutput: String, action: () -> Unit) {
  val inputStream = ByteArrayInputStream(input.toByteArray())
  System.setIn(inputStream)

  val out = ByteArrayOutputStream()
  System.setOut(PrintStream(out))

  action()

  checkSystemOutput(message, expectedOutput, out.toString())
}

fun loadClass(packageName: String, className: String): KClass<*> {
  return try {
    ClassLoader.getSystemClassLoader().loadClass("$packageName.$className").kotlin
  } catch (e: ClassNotFoundException) {
    throw AssertionError("Can't find the '$className' class in '$packageName' package")
  }
}

fun loadMemberFunction(kClass: KClass<*>, methodName: String): KFunction<*> {
  fun error(): Nothing {
    throw AssertionError("Can't find the '$methodName()' member function in '${kClass.simpleName}' class")
  }
  return try {
    kClass.memberFunctions.find { it.name == methodName } ?: error()
  } catch (e: NoSuchMethodException) {
    error()
  }
}

fun loadMemberProperty(kClass: KClass<*>, propertyName: String): KProperty<*> {
  fun error(): Nothing {
    throw AssertionError("Can't find the '$propertyName' member property in '${kClass.simpleName}' class")
  }
  return try {
    kClass.memberProperties.find { it.name == propertyName } ?: error()
  } catch (e: NoSuchMethodException) {
    error()
  }
}

class KFileFacade(val packageName: String, val fileName: String, val jClass: Class<*>)

fun loadFileFacade(packageName: String, fileName: String): KFileFacade {
  return try {
    KFileFacade(packageName, fileName,
      ClassLoader.getSystemClassLoader().loadClass("$packageName.${fileName.capitalize()}Kt"))
  } catch (e: ClassNotFoundException) {
    throw AssertionError("Can't find the '$fileName.kt' file in '$packageName' package")
  }
}

private fun loadToplevelMember(fileFacade: KFileFacade, memberName: String, isGetter: Boolean): Method {
  fun error(): Nothing {
    val obj = if (isGetter) "'$memberName' property" else "'$memberName()' function"
    throw AssertionError("Can't find the $obj in '${fileFacade.fileName}.kt' file")
  }
  return try {
    val name = if (isGetter) "get" + memberName.capitalize() else memberName
    fileFacade.jClass.declaredMethods.find { it.name == name } ?: error()
  } catch (e: NoSuchMethodException) {
    error()
  }
}

fun checkParametersOfConstructor(
  constructor: KFunction<*>,
  kClass: KClass<*>,
  params: List<Pair<String, String>>
) {
  checkParameters("constructor of '${kClass.simpleName}'", constructor, params)
}

fun checkParametersOfTopLevelFunction(
  function: KFunction<*>,
  params: List<Pair<String, String>>
) {
  checkParameters("function '${function.name}'", function, params)
}

fun checkParametersOfMemberFunction(
  function: KFunction<*>,
  params: List<Pair<String, String>>
) {
  checkParametersOfTopLevelFunction(function,
    listOf("" to "") // this parameter refers to a class and ignored while checking
      + params)
}

private fun checkParameters(
  funcOrConstructorName: String,
  function: KFunction<*>,
  params: List<Pair<String, String>>
) {
  Assert.assertEquals("${funcOrConstructorName.capitalize()} is expected to have ${params.size} parameter(s)",
    params.size, function.parameters.size)

  val expectedParams = params.toList()
  function.parameters.forEachIndexed { index, kParameter ->
    val (name, type) = expectedParams[index]

    checkParameter(index, name, type, kParameter, funcOrConstructorName)
  }
}

private fun checkParameter(index: Int, name: String, type: String, param: KParameter, funcName: String) {
  val ordinal = when (index) {
    0 -> "first"
    1 -> "second"
    2 -> "third"
    3 -> "forth"
    5 -> "fifth"
    else -> "$index"
  }
  if (name.isNotEmpty()) {
    Assert.assertEquals("Expected the $ordinal parameter named '$name' for ${funcName.decapitalize()}",
      name, param.name)
    Assert.assertEquals("Expected the parameter '$name' of type '$type' for ${funcName.decapitalize()}",
      type, param.type.toString())
  }
  // type is empty for first parameter of member functions, where class instance is passes at first
  // this case isn't tested explicitly to avoid mysterious messages for students
  else if (type.isNotEmpty()) {
    Assert.assertEquals("Expected the $ordinal parameter of type '$type' for ${funcName.decapitalize()}",
      type, param.type.toString())
  }
}


fun loadToplevelFunction(fileFacade: KFileFacade, functionName: String): Method {
  return loadToplevelMember(fileFacade, functionName, false)
}

fun loadToplevelPropertyGetter(fileFacade: KFileFacade, propertyName: String): Method {
  return loadToplevelMember(fileFacade, propertyName, true)
}

fun untestable() {
  Assert.assertTrue("No tests: tests always pass", true)
}

inline fun checkMainIsImplemented(main: () -> Unit) {
  try {
    main()
  } catch (e: NotImplementedError) {
    throw AssertionError("The 'main' function is not implemented")
  }
}
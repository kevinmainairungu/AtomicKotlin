## Scope Functions (#3)

Rewrite the following function using one of the scope functions 
to have only one call chain:

```kotlin
fun transform(list: List): List {
  val intermediateResult = list
      .filter { it % 2 == 1 }
      .map { it * it }
  return intermediateResult.subList(
      1, intermediateResult.size)
}
```
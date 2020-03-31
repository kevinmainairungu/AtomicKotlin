## Summary 2 (#8)

Convert from a number in the Roman numeral system into a natural number. For
example: XXIII = 23, XLIV = 44, C = 100.

<div class="hint">

You can simply iterate over each numeral in the Roman representation and
calculate the answer! Traverse a Roman number in reverse order, a single
numeral at a time (for example, `IV` contains two numerals) and store the
maximum numeral found so far. If the next Roman numeral is more or equal than
the current maximum numeral, add it to the result; if it's less than the
maximum, subtract it instead. For instance, to convert `XLIV = 44`, iterate
over `VILX` which is the reverse of `XLIV`. You add `V`(`5`) and `L`(`50`), but
subtract `1`(`I`) because it's less than the current maximum `V`, and subtract
`10`(`X`) because it's less than the updated maximum `X`:

| numeral | current maximum | action |
| ------- |-----------------|--------|
| V       | 5               | + 5    |
| I       | 5               | - 1    |
| L       | 50              | + 50   |
| X       | 50              | - 10   |

The result is `+ 5 - 1 + 50 - 10 = 44`

</div>
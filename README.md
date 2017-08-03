![name](https://img.shields.io/badge/ylfzq-Calculator-green.svg)
[![GitHub issues](https://img.shields.io/github/issues/ylfzq/Calculator.svg)](https://github.com/ylfzq/Calculator/issues)
[![GitHub forks](https://img.shields.io/github/forks/ylfzq/Calculator.svg)](https://github.com/ylfzq/Calculator/network)
[![GitHub stars](https://img.shields.io/github/stars/ylfzq/Calculator.svg)](https://github.com/ylfzq/Calculator/stargazers)
[![Travis](https://img.shields.io/travis/ylfzq/Calculator.svg)]()
[![Github file size](https://img.shields.io/github/size/ylfzq/Calculator/target/Calculator-latest.jar.svg)]()

![progress](http://progressed.io/bar/100?title=done)
[![release](https://img.shields.io/github/release/ylfzq/Calculator.svg)]()
[![Maven Central](https://img.shields.io/maven-central/v/tech.zhouqian.java/Calculator.svg)]()
[![license](https://img.shields.io/github/license/ylfzq/Calculator.svg)]()

## How to use

Simple usage is:

```java
System.out.println(
    Calculator.getDefault().calcExpression("1+2*(3+4*6)/min(5, 2, PI)")
);
```

which outputs 28.0 as result.


You can print calculate steps to PrintStream by

```java
System.out.println(
	Calculator.getDefault()
    .setCalcStepPrintStream(System.err)		// print calculate steps to System.err
    .calcExpression("1+2*(3+4*6)/min(5, 2, PI)")
);
```

the output is:

```
4.0*6.0=24.0
3.0+24.0=27.0
2.0*27.0=54.0
PI=3.141592653589793
min(5.0,2.0,3.141592653589793)=2.0
54.0/2.0=27.0
1.0+27.0=28.0
28.0
```

## How to extend

We call all operands, functions and constants as "CalcUnit". CalcUnit's hierarchy:

```
CalcUnit
|-- ConstantCalcUnit
|-- FunctionCalcUnit
|-- OperatorCalcUnit
    |-- SingleOperandOperatorCalcUnit
    |-- TwoOperandsOperatorCalcUnit
```

You can call `Calculator.addCalcUnit(CalcUnit[])` to add custom implemented CalcUnit. For example:

```java
Calculator.getDefault().addCalcUnit(new CalcUnit[] {
	new ConstantCalcUnit("e", 2.718281828),
	new ConstantCalcUnit("g", 9.8),
	new FunctionCalcUnit("sqrt") {	// define function named sqrt
      @Override public Number doCalc(Number... vals) {
        if (vals.length > 1) {
          throw new IllegalArgumentException("sqrt() can only receive 1 param");
        }

        return Math.sqrt(vals[0].doubleValue());
      }
    },
    new TwoOperandsOperatorCalcUnit("**", CalcUnitsImpl.CALC_LEVEL_MULTIPLY_DIVID + 1) {
      @Override public Number doCalc(Number val1, Number val2) {
        return Math.pow(val1.doubleValue(), val2.doubleValue());
      }
    }
});
```

### extend Constant

```java
// The first param is the constant name, the second param is the constant value
new ConstantCalcUnit("e", 2.718281828);
```

### extend Function

```java
new FunctionCalcUnit("sqrt") {	// define function named sqrt
  @Override public Number doCalc(Number[] vals) {	// the function params, you can throw if the param count is not correct
    if (vals.length > 1) {
      throw new IllegalArgumentException("sqrt() can only receive 1 param");
    }

    return Math.sqrt(vals[0].doubleValue());
  }
}
```

### extend Operator

There are 3 types of Operators: left-single-operand operator, right-single-operand operator, two operands operator.

```java
// define a factorial operator "!", the second param true stands the operand is left to operator, otherwise is stands right operand.
public static final CalcUnit OPERATOR_FACTORIAL = new SingleOperandOperatorCalcUnit("!", true) {
    @Override public Number doCalc(Number val) {
      double mul = 1;
      int a = val.intValue();
      for (int i = 2; i <= a; ++i) {
        mul *= i;
      }
      return mul;
    }
  };
```


## How to debug

Call `setStackPrintStream(PrintStream)` or `setCalcStepPrintStream(PrintStream)` on Calculator to print steps.

```java
System.out.println(
    Calculator.getDefault()
    .setStackPrintStream(System.err)
    .calcExpression("1+2*(3+4*6)/min(5, 2, PI)")
);
```

```
[]
[1.0]

[+]
[1.0]

[+]
[2.0, 1.0]

[*, +]
[2.0, 1.0]

[(, *, +]
[2.0, 1.0]

[(, *, +]
[3.0, 2.0, 1.0]

[+, (, *, +]
[3.0, 2.0, 1.0]

[+, (, *, +]
[4.0, 3.0, 2.0, 1.0]

[*, +, (, *, +]
[4.0, 3.0, 2.0, 1.0]

[*, +, (, *, +]
[6.0, 4.0, 3.0, 2.0, 1.0]

[*, +]
[27.0, 2.0, 1.0]

[/, +]
[54.0, 1.0]

[min, /, +]
[(, 54.0, 1.0]

[(, min, /, +]
[(, 54.0, 1.0]

[(, min, /, +]
[5.0, (, 54.0, 1.0]

[(, min, /, +]
[5.0, (, 54.0, 1.0]

[(, min, /, +]
[2.0, 5.0, (, 54.0, 1.0]

[(, min, /, +]
[2.0, 5.0, (, 54.0, 1.0]

[(, min, /, +]
[3.141592653589793, 2.0, 5.0, (, 54.0, 1.0]

[/, +]
[2.0, 54.0, 1.0]
```

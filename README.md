# Infix Calculator

I made this calculator during my [Java course](https://d3s.mff.cuni.cz/teaching/nprg013/).

If you are taking, or plan to take the course, please follow the student's honor code. In particular, do not plagiarize.

## Usage

```
> javac src/main/java/calc/* -d target/classes
> java -classpath ./target/classes calc.Calc
>> 2*-3
-6.00000
>> ---4
-4.00000
>> 2/3
0.66667
>> 2/0
Infinity
>> fox+cow=?
ERROR
>> me=60
60.00000
>> you=9
9.00000
>> me+you
69.00000
```

## Expression Parsing

The following unambiguous CFG is used for parsing valid expressions:

```
E -> T | E '+' T | E '-' T
T -> F | T '*' F | T '/' F
F -> P | '-'P
P -> E | '('E')' | V | N
```

The CFG is implemented by a recursive descent parser.
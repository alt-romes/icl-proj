## The ICL Lang

The interpreter and compiler can be compiled and tested with the phony rules in the `makefile` with
```
make compiler
make interpreter
make test
```

Which is equivalent to running:
```
javacc Parser.jj
javac  Interpreter.java
javac  Compiler.java
./run-tests.sh
```

To compile a source code file:
```
java Compiler source.ext
```
When compiled, the JVM classes will be generated. To run the compiled program
run `java Main` after the compiler.

To run the interpreter, or interpret a file:
```
java Interpreter
java Interpreter < source.ext
```

There is a list of extensive tests/example programs under `/tests`.
These are all run automatically and compared against a correct solution when the
script `run-tests.sh` is executed.


## The C to LLVM Compiler

Additionally, I developed another compiler that parses the ANSI C language and
generates LLVM IR code. This compiler supports a big part of the ANSI C
language, and missing `structs` and `enums`, `storage_classifiers`, among some
other quite specific things. The project is also hosted on github: https://github.com/alt-romes/c-compiler
It also has an extensive (for every feature) test battery under `/compiler-tests`

It is capable of compiling fun programs such as:
```c
void* (*f(void* (*(*p)(int (*)[2]))[]))[] {

    int y[2];
    void* (*x)[] = (void* (*)[])p(&y);
    return x;
}
```

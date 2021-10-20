all: compiler interpreter

compiler: Compiler.class
interpreter: Interpreter.class

Parser.java: Parser.jj
	javacc Parser.jj

Interpreter.class: Interpreter.java Parser.java
	javac Interpreter.java

Compiler.class: Compiler.java Parser.java
	javac Compiler.java

Main.class: Compiler.class Main.j source.txt
	head -n 20 Main.j >> Prog.j
	java Compiler source.txt >> Prog.j
	tail -n 7 Main.j >> Prog.j
	jasmin Prog.j

.PHONY: clean

clean:
	rm Parse*.java
	rm Token*.java
	rm SimpleCharStream.java
	rm *.class
	rm Prog.j

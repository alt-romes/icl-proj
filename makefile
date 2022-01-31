all: clean compiler interpreter test

compiler: Compiler.class
interpreter: Interpreter.class

Parser.java: Parser.jj
	javacc Parser.jj

Interpreter.class: Interpreter.java Parser.java
	javac Interpreter.java

Compiler.class: Compiler.java Parser.java
	javac Compiler.java

# Main.class Main.j: Compiler.class source.txt
# 	java Compiler source.txt

.PHONY: clean test
clean:
	-rm Parse*.java
	-rm Token*.java
	-rm SimpleCharStream.java
	-rm *.class
	-rm *.j

test:
	./run-tests.sh

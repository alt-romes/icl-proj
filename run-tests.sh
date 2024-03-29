#!/bin/bash


: << "testing-docs"
## Testing

The folder `tests` has two files for each test. `file.test` and `file.correct`

The `.test` file is the input passed into `the program`, and the `.correct` file is the expected output

To test one of these files run:
```
$ ./run-tests file
```

To save the output of a test run:
```
$ ./run-tests -s file
```

If something is printed out to the console then the actual output and the expected output differ, and changes should be made until all tests pass.

To test all files at the same time run:
```
$ ./run-tests.sh
```
testing-docs


# Define tests to run
tests=()
# Fill tests array
while IFS='' read -r line; do tests+=("$line"); done < <(find tests -maxdepth 1 -name '*.test' | sed 's/tests\///' | sed 's/\.test//')

# Define command to run tests here
run_command() {
    test_name=$1
    if [[ "$test_name" = compiler* ]]; then
        java Compiler tests/"$test_name.test" && cat Main.j
    else
        java Interpreter < tests/"$test_name.test"
    fi
}

if [ "$1" ]
then
    # If an argument is supplied run that test and output to stdout
    if [ "$1" = "--save" ] || [ "$1" = "-s" ]
    then
        if [ -z "$2" ]; then exit 2; fi
        # If --save or -s is used, save the output to the .correct file
        run_command "$2" | tee tests/"$2".correct
        echo "Saved test result as correct."
    else
        run_command "$1"
    fi
    exit 0
fi

if for t in "${tests[@]}"
do
    echo "Running test: $t" &&
        if ! (diff -b tests/"$t".correct <(run_command "$t")); then
            echo "Test failed: $t" &&
            exit 1
        fi

done
then
    echo "All tests passed"
fi

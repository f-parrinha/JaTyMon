#!/bin/zsh

VERSION="1.0"
SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
PROCESSOR_JAR="$SCRIPT_DIR/core-$VERSION-jar-with-dependencies.jar"
LIB_JAR="$SCRIPT_DIR/lib-$VERSION-jar-with-dependencies.jar"

if [ "$#" -ne 2 ]; then
    echo "Usage: $0 <project path> <confidence level>"
    exit 1
fi

if ! [[ "$2" =~ ^[0-9]+(\.[0-9]+)?$ ]]; then
    echo "Error: confidence level must be a number"
    exit 1
fi

# Convert to absolute path to handle spaces safely
ABS_PROJECT_PATH=$(cd "$1" && pwd)
CONFIDENCE_LEVEL="$2"
SOURCE_PATH="src/main/java"
MONITORS_OUTPUT="$ABS_PROJECT_PATH/$SOURCE_PATH"
DEP_DIR="$ABS_PROJECT_PATH/target/dependency"

echo
echo "Collecting Maven dependencies..."
mvn -f "$ABS_PROJECT_PATH/pom.xml"     org.apache.maven.plugins:maven-dependency-plugin:3.6.0:copy-dependencies     -DoutputDirectory="$DEP_DIR" -q

if [ ! -d "$DEP_DIR" ]; then
    echo "ERROR: Failed to create dependency folder at $DEP_DIR"
    exit 1
fi

CP_DEPS=$(find "$DEP_DIR" -name "*.jar" | paste -sd ":" -)

echo "Running JaTyMon Processor..."
find "$ABS_PROJECT_PATH/$SOURCE_PATH" -name "*.java" -print0 | xargs -0 javac \
    -processor jatymon.JaTyMonProcessor \
    -processorpath "$PROCESSOR_JAR" \
    -classpath "$LIB_JAR:$PROCESSOR_JAR:$CP_DEPS" \
    -sourcepath "$ABS_PROJECT_PATH/$SOURCE_PATH" \
    -proc:only \
    -Aoutput="$MONITORS_OUTPUT" \
    -AconfidenceLevel="$CONFIDENCE_LEVEL"

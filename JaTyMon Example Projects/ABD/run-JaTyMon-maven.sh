#!/bin/zsh

SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
PROCESSOR_JAR="$SCRIPT_DIR/jatymon-core-1.0.jar"

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <confidence level>"
    exit 1
fi

if ! [[ "$1" =~ ^[0-9]+(\.[0-9]+)?$ ]]; then
    echo "Error: confidence level must be a number"
    exit 1
fi

CONFIDENCE_LEVEL="$1"
SOURCE_PATH="src/main/java"
DEP_DIR="target/dependency"

echo
echo "Collecting Maven dependencies..."
mvn -f "pom.xml" org.apache.maven.plugins:maven-dependency-plugin:3.6.0:copy-dependencies -DoutputDirectory="$DEP_DIR" -q

if [ ! -d "$DEP_DIR" ]; then
    echo "ERROR: Failed to create dependency folder at $DEP_DIR"
    exit 1
fi

CP_DEPS=$(find "$DEP_DIR" -name "*.jar" | paste -sd ":" -)

echo "Running JaTyMon Processor..."
find "$SOURCE_PATH" -name "*.java" -print0 | xargs -0 javac \
    -processor jatymon.JaTyMonProcessor \
    -processorpath "$PROCESSOR_JAR" \
    -classpath "$CP_DEPS" \
    -sourcepath "$SOURCE_PATH" \
    -proc:only \
    -Aoutput="$SOURCE_PATH" \
    -AconfidenceLevel="$CONFIDENCE_LEVEL"

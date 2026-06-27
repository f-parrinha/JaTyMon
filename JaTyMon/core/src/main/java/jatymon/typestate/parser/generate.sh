#!/bin/zsh

# Source your main shell where your ANTLR aliases are stored
source ~/.zshrc

GRAMMAR="Typestate.g4"
OUTDIR="generated"

# Clear generated folder's content
if [[ -d "$OUTDIR" ]]; then
  rm -rf "$OUTDIR"/*
fi

antlr4 -visitor -o $OUTDIR $GRAMMAR
package jatymon.typestate.ast;

import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.declaration.TTypestateNode;

import java.util.List;

public record TypestateAst(TTypestateNode root, List<AbstractDiagnostic> diagnostics) { }

package jatymon.typestate.ast;

import jatymon.actions.ActionType;
import jatymon.common.globals.BoolOperator;
import jatymon.common.globals.ComparisonOperator;
import jatymon.diagnostics.AbstractDiagnostic;
import jatymon.typestate.ast.nodes.*;
import jatymon.common.globals.ArithOperator;
import jatymon.typestate.ast.nodes.action.TActionNode;
import jatymon.typestate.ast.nodes.declaration.TDeclarationNode;
import jatymon.typestate.ast.nodes.declaration.TTypestateNode;
import jatymon.typestate.ast.nodes.imports.TImportNode;
import jatymon.typestate.ast.nodes.imports.TPackageNode;
import jatymon.typestate.ast.nodes.internalstate.arith.operand.TOperandNode;
import jatymon.typestate.ast.nodes.internalstate.predicates.*;
import jatymon.typestate.ast.nodes.internalstate.arith.TArithExprNode;
import jatymon.typestate.ast.nodes.internalstate.arith.TBinaryArithExprNode;
import jatymon.typestate.ast.nodes.internalstate.arith.operand.TIdOperandNode;
import jatymon.typestate.ast.nodes.internalstate.arith.operand.TNumberOperandNode;
import jatymon.typestate.ast.nodes.internalstate.assignments.TAssignExprNode;
import jatymon.typestate.ast.nodes.internalstate.assignments.TAssignNode;
import jatymon.typestate.ast.nodes.internalstate.fields.TExtNode;
import jatymon.typestate.ast.nodes.internalstate.fields.TValNode;
import jatymon.typestate.ast.nodes.key.TKeyNode;
import jatymon.typestate.ast.nodes.ref.*;
import jatymon.typestate.ast.nodes.state.*;
import jatymon.ratios.NullRatio;
import jatymon.ratios.Ratio;
import jatymon.common.TokenPosition;
import jatymon.diagnostics.syntax.InvalidDestinationDiagnostic;
import jatymon.diagnostics.syntax.InvalidIdTypeDiagnostic;
import jatymon.diagnostics.syntax.SyntaxErrorDiagnostic;
import jatymon.exceptions.typestate.InvalidArithExprSize;
import jatymon.exceptions.typestate.InvalidBoolExprSize;
import jatymon.exceptions.typestate.InvalidOperandType;
import jatymon.typestate.parser.generated.TypestateBaseVisitor;
import jatymon.typestate.parser.generated.TypestateParser;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.nio.file.Paths;
import java.util.*;

/**
 * Class {@code AstBuilder} is a visitor to the Typestate parser.
 * It can receive a typestate file and build an AST from it.
 */
public class TypestateAstVisitor extends TypestateBaseVisitor<TNode> {
    private final List<AbstractDiagnostic> diagnostics;

    public TypestateAstVisitor() {
        this.diagnostics = new LinkedList<>();
    }

    public List<AbstractDiagnostic> getDiagnostics() {
        return Collections.unmodifiableList(diagnostics);
    }

    @Override
    public TTypestateNode visitStart(final TypestateParser.StartContext ctx) {
        final TPackageNode pkg = ctx.package_statement() == null ? null : (TPackageNode) visit(ctx.package_statement());
        final List<TImportNode> imports = ctx.import_statement().stream()
                .map(el -> (TImportNode) visit(el))
                .toList();
        final TDeclarationNode declaration = (TDeclarationNode) visit(ctx.typestate_declaration());
        return new TTypestateNode(pkg, imports, declaration);
    }

    @Override
    public TPackageNode visitPackage_statement(final TypestateParser.Package_statementContext ctx) {
        final TRefNode ref = (TRefNode) visitRef(ctx.ref());
        return new TPackageNode(TokenPosition.createFrom(ctx), ref);
    }

    @Override
    public TImportNode visitImport_statement(final TypestateParser.Import_statementContext ctx) {
        final TokenPosition tokenPosition = TokenPosition.createFrom(ctx);
        final TRefNode ref = (TRefNode) visit(ctx.ref());
        final boolean isStatic = ctx.STATIC() != null;
        final boolean star = ctx.getText().endsWith(".*;");
        return new TImportNode(tokenPosition, ref, isStatic, star);
    }

    @Override
    public TDeclarationNode visitTypestate_declaration(final TypestateParser.Typestate_declarationContext ctx) {
        final String name = ctx.ID().getText();
        final List<TExtNode> exts = new ArrayList<>();
        final List<TValNode> vals = new ArrayList<>();
        final List<TAssignNode> assigns = new ArrayList<>();
        final List<TPredNode> preds = new ArrayList<>();

        // Build internal state
        for (var internal : ctx.internal_state_declaration()) {
            if (internal.ext_declaration() != null) {
                exts.add((TExtNode) visit(internal.ext_declaration()));
            } else if (internal.val_declaration() != null) {
                vals.add((TValNode) visit(internal.val_declaration()));
            } else if (internal.assignment_declaration() != null) {
                assigns.add((TAssignNode) visit(internal.assignment_declaration()));
            } else if (internal.predicate_declaration() != null) {
                preds.add((TPredNode) visit(internal.predicate_declaration()));
            }
        }

        // Build states
        final List<TStateNode> states = ctx.state_declaration().stream()
                .map(el -> (TStateNode) visit(el))
                .toList();

        return new TDeclarationNode.Builder()
                .withTokenPos(TokenPosition.createFrom(ctx))
                .withName(name)
                .withKey(visitTypestate_key(ctx.typestate_key()))
                .withExts(exts)
                .withVals(vals)
                .withAssigns(assigns)
                .withPreds(preds)
                .withStates(states)
                .build();
    }

    @Override
    public TKeyNode visitTypestate_key(final TypestateParser.Typestate_keyContext ctx) {
        if (ctx == null) {
            return null;
        }

        return new TKeyNode(ctx.ID().getText(), TokenPosition.createFrom(ctx));
    }

    @Override
    public TExtNode visitExt_declaration(final TypestateParser.Ext_declarationContext ctx) {
        final String name = ctx.ID().getText();
        return new TExtNode(TokenPosition.createFrom(ctx), name);
    }

    @Override
    public TValNode visitVal_declaration(final TypestateParser.Val_declarationContext ctx) {
        final String name = ctx.ID().getText();
        final TypestateParser.OperandContext operandCtx = ctx.operand();
        if (operandCtx == null) {
            return TValNode.createDefault(TokenPosition.createFrom(ctx), name);
        }

        final TokenPosition operandTokenPos = TokenPosition.createFrom(operandCtx);
        final TOperandNode<?> operandNode = operandCtx.NUMBER() != null ?
                new TNumberOperandNode(operandTokenPos, Integer.parseInt(operandCtx.NUMBER().getText())) :
                new TIdOperandNode(operandTokenPos, operandCtx.ID().getText());
        return new TValNode(TokenPosition.createFrom(ctx), name, operandNode);
    }

    @Override
    public TAssignNode visitAssignment_declaration(final TypestateParser.Assignment_declarationContext ctx) {
        final String name = ctx.ID().getText();
        final TAssignExprNode assignExpr = (TAssignExprNode) visit(ctx.assign_expr());
        return new TAssignNode(TokenPosition.createFrom(ctx), name, assignExpr);
    }

    @Override
    public TPredNode visitPredicate_declaration(final TypestateParser.Predicate_declarationContext ctx) {
        final String name = ctx.ID().getText();
        final TBoolExprNode boolExpr = (TBoolExprNode) visit(ctx.bool_expr());
        return new TPredNode(TokenPosition.createFrom(ctx), name, boolExpr);
    }

    @Override
    public TAssignExprNode visitAssign_expr(final TypestateParser.Assign_exprContext ctx) {
        final String name = ctx.ID().getText();
        final TArithExprNode arithExprNode = (TArithExprNode) visit(ctx.arith_expr());
        return new TAssignExprNode(TokenPosition.createFrom(ctx), name, arithExprNode);
    }

    @Override
    public TNode visitBool_expr(final TypestateParser.Bool_exprContext ctx) {
        var cmpCtx = ctx.cmp_expr();
        var boolExprCtx = ctx.bool_expr();
        if (cmpCtx != null) {
            return visit(cmpCtx);
        }
        if (boolExprCtx.size() == 1) {
            return visit(boolExprCtx.getFirst());
        }
        if (boolExprCtx.size() == 2) {
            final TBoolExprNode left = (TBoolExprNode) visit(ctx.bool_expr(0));
            final TBoolExprNode right = (TBoolExprNode) visit(ctx.bool_expr(1));
            final String op = ctx.getChild(1).getText();
            return new TBinaryBoolExprNode(TokenPosition.createFrom(ctx), left, right, BoolOperator.fromString(op));
        }

        throw new InvalidBoolExprSize(boolExprCtx.size());
    }

    @Override
    public TCmpExprNode visitCmp_expr(final TypestateParser.Cmp_exprContext ctx) {
        if (ctx.arith_expr().size() != 2) {
            throw new InvalidArithExprSize(ctx.arith_expr().size());
        }

        final TArithExprNode arithExpr1 = (TArithExprNode) visit(ctx.arith_expr(0));
        final TArithExprNode arithExpr2 = (TArithExprNode) visit(ctx.arith_expr(1));
        try {
            final ComparisonOperator operator = ComparisonOperator.fromString(ctx.cmp_operator().getText());
            return new TCmpExprNode(TokenPosition.createFrom(ctx), arithExpr1, arithExpr2, operator);
        } catch (ComparisonOperator.InvalidException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TNode visitArith_expr(final TypestateParser.Arith_exprContext ctx) {
        var operandCtx = ctx.operand();
        var arithExprCtx = ctx.arith_expr();
        if (operandCtx != null) {
            return visit(operandCtx);
        }

        if (arithExprCtx.size() == 1) {
            return visit(arithExprCtx.getFirst());
        } else if (arithExprCtx.size() == 2) {
            final TArithExprNode arithExpr1 = (TArithExprNode) visit(arithExprCtx.get(0));
            final TArithExprNode arithExpr2 = (TArithExprNode) visit(arithExprCtx.get(1));
            final String op = ctx.getChild(1).getText();
            return new TBinaryArithExprNode(TokenPosition.createFrom(ctx), arithExpr1, arithExpr2, ArithOperator.fromString(op));
        }

        throw new InvalidArithExprSize(arithExprCtx.size());
    }

    @Override
    public TNode visitOperand(final TypestateParser.OperandContext ctx) {
        final TokenPosition tokenPosition = TokenPosition.createFrom(ctx);
        if (ctx.NUMBER() != null) {
            final Integer number = Integer.parseInt(ctx.NUMBER().getText());
            return new TNumberOperandNode(tokenPosition, number);
        } if (ctx.ID() != null) {
            return new TIdOperandNode(tokenPosition, ctx.ID().getText());
        }
        throw new InvalidOperandType();
    }

    @Override
    public TStateNode visitState_declaration(final TypestateParser.State_declarationContext ctx) {
        final String name = ctx.ID().getText();
        final TypestateParser.StateContext stateCtx = ctx.state();
        final TokenPosition tokenPosition = TokenPosition.createFrom(ctx);

        // No state context? return empty state (semantic errors will be presented)
        if (stateCtx == null) {
            return new TStateNode(tokenPosition, name, List.of(), List.of());
        }

        return TStateNode.fromAnonymousState(tokenPosition, name, visitState(stateCtx));
    }

    @Override
    public TAnonymousStateNode visitState(final TypestateParser.StateContext ctx) {
        List<TActionNode> inputActions = new LinkedList<>();
        List<TActionNode> outputActions = new LinkedList<>();
        final TypestateParser.Input_stateContext inputStateCtx = ctx.input_state();
        final TypestateParser.Output_stateContext outputStateCtx = ctx.output_state();
        if (outputStateCtx != null) {
            outputActions = outputStateCtx.output_action().stream()
                    .map(this::visitOutput_action)
                    .filter(Objects::nonNull)
                    .toList();
        }
        if (inputStateCtx != null) {
            inputActions = inputStateCtx.input_action().stream()
                    .map(this::visitInput_action)
                    .filter(Objects::nonNull)
                    .toList();
        }

        return new TAnonymousStateNode(TokenPosition.createFrom(ctx), inputActions, outputActions);
    }

    @Override
    public TActionNode visitInput_action(final TypestateParser.Input_actionContext ctx) {
        return parseAction(ctx.action(), ActionType.Input);
    }

    @Override
    public TActionNode visitOutput_action(final TypestateParser.Output_actionContext ctx) {
        return parseAction(ctx.action(), ActionType.Output);
    }

    @Override
    public TDecisionStateNode visitDecision_state(final TypestateParser.Decision_stateContext ctx) {
        final List<TDecisionNode> decisions = ctx.decision().stream()
                .map(el -> (TDecisionNode) visit(el))
                .toList();
        return new TDecisionStateNode(TokenPosition.createFrom(ctx), decisions);
    }

    @Override
    public TDecisionNode visitDecision(final TypestateParser.DecisionContext ctx) {
        final String label = ctx.decision_label().getText();
        final TNode destination = ctx.id() != null ? (TIdNode) visit(ctx.id()) : (TStateNode) visit(ctx.state());
        return new TDecisionNode(TokenPosition.createFrom(ctx), label, destination);
    }

    @Override
    public TNode visitRef(final TypestateParser.RefContext ctx) {
        final List<TerminalNode> ids = ctx.ID();
        final TokenPosition refStartPos = TokenPosition.createFrom(ctx);

        // Note: a ref can be ref1.ref2.ref3...
        TRefNode base = null;
        for (int i = 0; i < ids.size(); i++) {
            final TerminalNode id = ids.get(i);
            final var pos = TokenPosition.createFrom(id.getSymbol());
            final var idNode = new TIdNode(pos, id.getText());

            if (i == 0) {
                base = idNode;
            } else {
                base = new TMemberNode(refStartPos, base, idNode);
            }
        }

        return base;
    }

    @Override
    public TIdNode visitId(final TypestateParser.IdContext ctx) {
        if (ctx.ID() != null) {
            return new TIdNode(TokenPosition.createFrom(ctx), ctx.ID().getText());
        } else if (ctx.END() != null) {
            return new TIdNode(TokenPosition.createFrom(ctx), ctx.END().getText());
        } else {
            String fileName = ctx.getStart().getTokenSource().getInputStream().getSourceName();
            diagnostics.add(new InvalidIdTypeDiagnostic(Paths.get(fileName).getFileName().toString(), TokenPosition.createFrom(ctx)));
            return null;
        }
    }

    @Override
    public TRefNode visitJavaType(final TypestateParser.JavaTypeContext ctx) {
        final TokenPosition tokenPos = TokenPosition.createFrom(ctx);
        TRefNode ref = (TRefNode) visit(ctx.ref());

        // Is generic type
        final var typeArgumentsCtx = ctx.typeArguments();
        if (typeArgumentsCtx != null) {
            ref = new TGenericTypeNode(tokenPos, ref, typeArgumentsCtx.javaType().stream()
                    .map(t -> (TRefNode) visit(t))
                    .toList());
        }

        // Is array type (supporting multiple arrays [][][]...)
        for (int i = 0; i < ctx.getChildCount() - 1; i++) {
            if (ctx.getChild(i).getText().equals("[]")) {
                ref = new TArrayTypeNode(TokenPosition.createFrom(ctx), ref);
            }
        }

        return ref;
    }

    @Override
    public TNode visitErrorNode(final ErrorNode node) {
        Token token = node.getSymbol(); // get the underlying token
        String fileName = token.getTokenSource().getInputStream().getSourceName();
        TokenPosition pos = TokenPosition.createFrom(token);
        diagnostics.add(new SyntaxErrorDiagnostic(Paths.get(fileName).getFileName().toString(), pos, node.getText()));
        return null;
    }


    /* --------------- AUX METHODS --------------- */


    private TActionNode parseAction(final TypestateParser.ActionContext ctx, final ActionType actionType) {
        /*
         * JavaType in the grammar is a list bcs it is used for the return type
         *  and for the method's args. The first in the list is the return type, the remaining are the args
         */
        final TRefNode returnType = (TRefNode) visit(ctx.javaType(0));
        final String name= ctx.ID().getText();
        final List<TRefNode> args = ctx.javaType().stream()
                .skip(1)
                .map(el -> (TRefNode) visit(el))
                .toList();

        final TNode destination = parseActionDestination(ctx);
        final TypestateParser.Action_propertiesContext actionProps = ctx.action_properties();
        final TActionNode.Builder actionNodeBuilder = new TActionNode.Builder(TokenPosition.createFrom(ctx), returnType, name, args, destination, actionType);
        if (actionProps != null) {
            parseActionProperties(actionNodeBuilder, actionProps, ctx.post_assignments_list());
        }
        return actionNodeBuilder.build();
    }

    private TActionNode.Builder parseActionProperties(final TActionNode.Builder methodBuilder,
                                                      final TypestateParser.Action_propertiesContext transCondCtx,
                                                      final TypestateParser.Post_assignments_listContext postAssignmentsListCtx) {
        final var ratioCtx = transCondCtx.ratio();
        final var preAssignmentNamesCtx = transCondCtx.pre_assignments_list();
        final var predicateNamesCtx = transCondCtx.predicates_list();

        Ratio ratio = new NullRatio();
        List<String> preAssignmentNames = new LinkedList<>();
        List<String> predicateNames = new LinkedList<>();
        List<String> postAssignmentNames = new LinkedList<>();

        if (ratioCtx != null && (ratioCtx.ADD() == null || ratioCtx.SUB() == null)) {
            ratio =  new Ratio(Double.parseDouble(ratioCtx.getText()));
        }
        if (preAssignmentNamesCtx != null && preAssignmentNamesCtx.AUTO_ASSIGN() == null) {
            preAssignmentNames = preAssignmentNamesCtx.ID().stream().map(TerminalNode::getText).toList();
        }
        if (predicateNamesCtx != null && predicateNamesCtx.AUTO_ASSIGN() == null) {
            predicateNames = predicateNamesCtx.ID().stream().map(TerminalNode::getText).toList();
        }
        if (postAssignmentsListCtx != null) {
            postAssignmentNames = postAssignmentsListCtx.ID().stream().map(TerminalNode::getText).toList();
        }
        return methodBuilder.withRatio(ratio)
                .withPreAssignmentNames(preAssignmentNames)
                .withPredicateNames(predicateNames)
                .withPostAssignmentNames(postAssignmentNames);
    }

    private TNode parseActionDestination(final TypestateParser.ActionContext ctx) {
        final TypestateParser.ReturnTargetContext returnCtx = ctx.returnTarget();
        if (returnCtx == null) {
            return null;
        } else if (returnCtx.id() != null) {
            return visit(returnCtx.id());
        } else if (returnCtx.state() != null) {
            return visit(returnCtx.state());
        } else if (returnCtx.decision_state() != null) {
            return visit(returnCtx.decision_state());
        } else {
            String fileName = ctx.getStart().getTokenSource().getInputStream().getSourceName();
            diagnostics.add(new InvalidDestinationDiagnostic(Paths.get(fileName).getFileName().toString(), TokenPosition.createFrom(ctx)));
            return null;
        }
    }
}

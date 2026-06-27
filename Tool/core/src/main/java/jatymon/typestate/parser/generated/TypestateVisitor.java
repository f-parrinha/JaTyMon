// Generated from Typestate.g4 by ANTLR 4.13.2

    package jatymon.typestate.parser.generated;

import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link TypestateParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface TypestateVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link TypestateParser#start}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStart(TypestateParser.StartContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#ref}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRef(TypestateParser.RefContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#javaType}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJavaType(TypestateParser.JavaTypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#typeArguments}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypeArguments(TypestateParser.TypeArgumentsContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#package_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPackage_statement(TypestateParser.Package_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#import_statement}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitImport_statement(TypestateParser.Import_statementContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#typestate_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypestate_declaration(TypestateParser.Typestate_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#typestate_key}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTypestate_key(TypestateParser.Typestate_keyContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#internal_state_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInternal_state_declaration(TypestateParser.Internal_state_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#ext_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExt_declaration(TypestateParser.Ext_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#val_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVal_declaration(TypestateParser.Val_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#predicate_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicate_declaration(TypestateParser.Predicate_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#assignment_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssignment_declaration(TypestateParser.Assignment_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#assign_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_expr(TypestateParser.Assign_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_expr(TypestateParser.Bool_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#bool_atom}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool_atom(TypestateParser.Bool_atomContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#arith_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArith_expr(TypestateParser.Arith_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#cmp_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmp_expr(TypestateParser.Cmp_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#cmp_operator}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmp_operator(TypestateParser.Cmp_operatorContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#operand}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOperand(TypestateParser.OperandContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#state_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitState_declaration(TypestateParser.State_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#state}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitState(TypestateParser.StateContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#input_state}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInput_state(TypestateParser.Input_stateContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#output_state}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutput_state(TypestateParser.Output_stateContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#decision_state}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecision_state(TypestateParser.Decision_stateContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#decision}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecision(TypestateParser.DecisionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#decision_label}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecision_label(TypestateParser.Decision_labelContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction(TypestateParser.ActionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#input_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInput_action(TypestateParser.Input_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#output_action}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOutput_action(TypestateParser.Output_actionContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#post_assignments_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPost_assignments_list(TypestateParser.Post_assignments_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#returnTarget}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitReturnTarget(TypestateParser.ReturnTargetContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#action_properties}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAction_properties(TypestateParser.Action_propertiesContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#ratio}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRatio(TypestateParser.RatioContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#pre_assignments_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPre_assignments_list(TypestateParser.Pre_assignments_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#predicates_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPredicates_list(TypestateParser.Predicates_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link TypestateParser#id}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(TypestateParser.IdContext ctx);
}
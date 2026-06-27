// Generated from Typestate.g4 by ANTLR 4.13.2

    package jatymon.typestate.parser.generated;

import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link TypestateParser}.
 */
public interface TypestateListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link TypestateParser#start}.
	 * @param ctx the parse tree
	 */
	void enterStart(TypestateParser.StartContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#start}.
	 * @param ctx the parse tree
	 */
	void exitStart(TypestateParser.StartContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#ref}.
	 * @param ctx the parse tree
	 */
	void enterRef(TypestateParser.RefContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#ref}.
	 * @param ctx the parse tree
	 */
	void exitRef(TypestateParser.RefContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#javaType}.
	 * @param ctx the parse tree
	 */
	void enterJavaType(TypestateParser.JavaTypeContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#javaType}.
	 * @param ctx the parse tree
	 */
	void exitJavaType(TypestateParser.JavaTypeContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void enterTypeArguments(TypestateParser.TypeArgumentsContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#typeArguments}.
	 * @param ctx the parse tree
	 */
	void exitTypeArguments(TypestateParser.TypeArgumentsContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#package_statement}.
	 * @param ctx the parse tree
	 */
	void enterPackage_statement(TypestateParser.Package_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#package_statement}.
	 * @param ctx the parse tree
	 */
	void exitPackage_statement(TypestateParser.Package_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#import_statement}.
	 * @param ctx the parse tree
	 */
	void enterImport_statement(TypestateParser.Import_statementContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#import_statement}.
	 * @param ctx the parse tree
	 */
	void exitImport_statement(TypestateParser.Import_statementContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#typestate_declaration}.
	 * @param ctx the parse tree
	 */
	void enterTypestate_declaration(TypestateParser.Typestate_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#typestate_declaration}.
	 * @param ctx the parse tree
	 */
	void exitTypestate_declaration(TypestateParser.Typestate_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#typestate_key}.
	 * @param ctx the parse tree
	 */
	void enterTypestate_key(TypestateParser.Typestate_keyContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#typestate_key}.
	 * @param ctx the parse tree
	 */
	void exitTypestate_key(TypestateParser.Typestate_keyContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#internal_state_declaration}.
	 * @param ctx the parse tree
	 */
	void enterInternal_state_declaration(TypestateParser.Internal_state_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#internal_state_declaration}.
	 * @param ctx the parse tree
	 */
	void exitInternal_state_declaration(TypestateParser.Internal_state_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#ext_declaration}.
	 * @param ctx the parse tree
	 */
	void enterExt_declaration(TypestateParser.Ext_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#ext_declaration}.
	 * @param ctx the parse tree
	 */
	void exitExt_declaration(TypestateParser.Ext_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#val_declaration}.
	 * @param ctx the parse tree
	 */
	void enterVal_declaration(TypestateParser.Val_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#val_declaration}.
	 * @param ctx the parse tree
	 */
	void exitVal_declaration(TypestateParser.Val_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#predicate_declaration}.
	 * @param ctx the parse tree
	 */
	void enterPredicate_declaration(TypestateParser.Predicate_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#predicate_declaration}.
	 * @param ctx the parse tree
	 */
	void exitPredicate_declaration(TypestateParser.Predicate_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#assignment_declaration}.
	 * @param ctx the parse tree
	 */
	void enterAssignment_declaration(TypestateParser.Assignment_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#assignment_declaration}.
	 * @param ctx the parse tree
	 */
	void exitAssignment_declaration(TypestateParser.Assignment_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#assign_expr}.
	 * @param ctx the parse tree
	 */
	void enterAssign_expr(TypestateParser.Assign_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#assign_expr}.
	 * @param ctx the parse tree
	 */
	void exitAssign_expr(TypestateParser.Assign_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#bool_expr}.
	 * @param ctx the parse tree
	 */
	void enterBool_expr(TypestateParser.Bool_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#bool_expr}.
	 * @param ctx the parse tree
	 */
	void exitBool_expr(TypestateParser.Bool_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#bool_atom}.
	 * @param ctx the parse tree
	 */
	void enterBool_atom(TypestateParser.Bool_atomContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#bool_atom}.
	 * @param ctx the parse tree
	 */
	void exitBool_atom(TypestateParser.Bool_atomContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void enterArith_expr(TypestateParser.Arith_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#arith_expr}.
	 * @param ctx the parse tree
	 */
	void exitArith_expr(TypestateParser.Arith_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#cmp_expr}.
	 * @param ctx the parse tree
	 */
	void enterCmp_expr(TypestateParser.Cmp_exprContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#cmp_expr}.
	 * @param ctx the parse tree
	 */
	void exitCmp_expr(TypestateParser.Cmp_exprContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#cmp_operator}.
	 * @param ctx the parse tree
	 */
	void enterCmp_operator(TypestateParser.Cmp_operatorContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#cmp_operator}.
	 * @param ctx the parse tree
	 */
	void exitCmp_operator(TypestateParser.Cmp_operatorContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#operand}.
	 * @param ctx the parse tree
	 */
	void enterOperand(TypestateParser.OperandContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#operand}.
	 * @param ctx the parse tree
	 */
	void exitOperand(TypestateParser.OperandContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#state_declaration}.
	 * @param ctx the parse tree
	 */
	void enterState_declaration(TypestateParser.State_declarationContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#state_declaration}.
	 * @param ctx the parse tree
	 */
	void exitState_declaration(TypestateParser.State_declarationContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#state}.
	 * @param ctx the parse tree
	 */
	void enterState(TypestateParser.StateContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#state}.
	 * @param ctx the parse tree
	 */
	void exitState(TypestateParser.StateContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#input_state}.
	 * @param ctx the parse tree
	 */
	void enterInput_state(TypestateParser.Input_stateContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#input_state}.
	 * @param ctx the parse tree
	 */
	void exitInput_state(TypestateParser.Input_stateContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#output_state}.
	 * @param ctx the parse tree
	 */
	void enterOutput_state(TypestateParser.Output_stateContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#output_state}.
	 * @param ctx the parse tree
	 */
	void exitOutput_state(TypestateParser.Output_stateContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#decision_state}.
	 * @param ctx the parse tree
	 */
	void enterDecision_state(TypestateParser.Decision_stateContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#decision_state}.
	 * @param ctx the parse tree
	 */
	void exitDecision_state(TypestateParser.Decision_stateContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#decision}.
	 * @param ctx the parse tree
	 */
	void enterDecision(TypestateParser.DecisionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#decision}.
	 * @param ctx the parse tree
	 */
	void exitDecision(TypestateParser.DecisionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#decision_label}.
	 * @param ctx the parse tree
	 */
	void enterDecision_label(TypestateParser.Decision_labelContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#decision_label}.
	 * @param ctx the parse tree
	 */
	void exitDecision_label(TypestateParser.Decision_labelContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#action}.
	 * @param ctx the parse tree
	 */
	void enterAction(TypestateParser.ActionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#action}.
	 * @param ctx the parse tree
	 */
	void exitAction(TypestateParser.ActionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#input_action}.
	 * @param ctx the parse tree
	 */
	void enterInput_action(TypestateParser.Input_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#input_action}.
	 * @param ctx the parse tree
	 */
	void exitInput_action(TypestateParser.Input_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#output_action}.
	 * @param ctx the parse tree
	 */
	void enterOutput_action(TypestateParser.Output_actionContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#output_action}.
	 * @param ctx the parse tree
	 */
	void exitOutput_action(TypestateParser.Output_actionContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#post_assignments_list}.
	 * @param ctx the parse tree
	 */
	void enterPost_assignments_list(TypestateParser.Post_assignments_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#post_assignments_list}.
	 * @param ctx the parse tree
	 */
	void exitPost_assignments_list(TypestateParser.Post_assignments_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#returnTarget}.
	 * @param ctx the parse tree
	 */
	void enterReturnTarget(TypestateParser.ReturnTargetContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#returnTarget}.
	 * @param ctx the parse tree
	 */
	void exitReturnTarget(TypestateParser.ReturnTargetContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#action_properties}.
	 * @param ctx the parse tree
	 */
	void enterAction_properties(TypestateParser.Action_propertiesContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#action_properties}.
	 * @param ctx the parse tree
	 */
	void exitAction_properties(TypestateParser.Action_propertiesContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#ratio}.
	 * @param ctx the parse tree
	 */
	void enterRatio(TypestateParser.RatioContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#ratio}.
	 * @param ctx the parse tree
	 */
	void exitRatio(TypestateParser.RatioContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#pre_assignments_list}.
	 * @param ctx the parse tree
	 */
	void enterPre_assignments_list(TypestateParser.Pre_assignments_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#pre_assignments_list}.
	 * @param ctx the parse tree
	 */
	void exitPre_assignments_list(TypestateParser.Pre_assignments_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#predicates_list}.
	 * @param ctx the parse tree
	 */
	void enterPredicates_list(TypestateParser.Predicates_listContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#predicates_list}.
	 * @param ctx the parse tree
	 */
	void exitPredicates_list(TypestateParser.Predicates_listContext ctx);
	/**
	 * Enter a parse tree produced by {@link TypestateParser#id}.
	 * @param ctx the parse tree
	 */
	void enterId(TypestateParser.IdContext ctx);
	/**
	 * Exit a parse tree produced by {@link TypestateParser#id}.
	 * @param ctx the parse tree
	 */
	void exitId(TypestateParser.IdContext ctx);
}
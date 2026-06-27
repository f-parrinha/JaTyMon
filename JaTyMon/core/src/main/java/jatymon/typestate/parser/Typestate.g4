grammar Typestate;

@header {
    package jatymon.typestate.parser.generated;
}

/** NOTE:  for lists (values with multiple ",") we may want to include a "," at the end as well for simplification and better suser-experience */


/** -------------------------
 *        PARSER RULES
 *  ------------------------- */

start
    : package_statement? import_statement* typestate_declaration EOF
    ;

/** ---------- JAVA ---------- **/

ref
    : ID ('.' ID)*
    ;
javaType
    : ref typeArguments? ('[]')*
    ;
typeArguments
    : '<' javaType (',' javaType)* '>'
    ;
package_statement
    : PACKAGE ref ';'
    ;
import_statement
    : IMPORT STATIC? ref ('.' '*')? ';'
    ;

/** ---------- TYPESTATE ---------- **/

typestate_declaration
    : TYPESTATE ID typestate_key? '{' internal_state_declaration* state_declaration* '}'
    ;

typestate_key : KEYED ID;

/** INTERNAL STATE **/
internal_state_declaration
    : ext_declaration
    | val_declaration
    | assignment_declaration
    | predicate_declaration
    ;

ext_declaration
    : 'Ext' ID ';'
    ;
val_declaration
    : 'Val' ID EQ operand ';'
    ;
predicate_declaration
    : 'Pred' ID ':' bool_expr ';'
    ;
assignment_declaration
    : 'Assign' ID ':' assign_expr ';'
    ;

assign_expr
    : ID ':=' arith_expr
    ;
bool_expr
    : NOT bool_atom
    | bool_expr AND bool_expr
    | bool_expr OR bool_expr
    | '(' bool_expr ')'
    | TRUE
    | FALSE
    | cmp_expr
    ;
bool_atom
    : NOT bool_atom
    |'(' bool_expr ')'
    | TRUE
    | FALSE
    ;


arith_expr
    : arith_expr (MULTIPLY | DIVIDE) arith_expr
    | arith_expr (ADD | SUB) arith_expr
    | '(' arith_expr ')'
    | operand
    ;

cmp_expr
    : arith_expr cmp_operator arith_expr
    ;
cmp_operator
    : EQ
    | NEQ
    | GT
    | GEQ
    | LT
    | LEQ
    ;

operand
    : ID
    | NUMBER
    ;

/** STATES **/
state_declaration
    : ID '=' state
    ;
state
    : input_state
    | output_state
    | input_state '+' output_state
    | output_state '+' input_state
    ;
input_state
    : '{' (input_action (',' input_action)*)? '}'
    ;
output_state
    : '(' (output_action (',' output_action)*)? ')'
    ;
decision_state
    : '<' decision ( ',' decision )* '>'
    ;
decision
    : decision_label ':' ( id | state)
    ;
decision_label
    : ID
    | TRUE
    | FALSE
    ;
/** ACTIONS **/
action
    : javaType ID '(' (javaType (',' javaType)*)? ')' action_properties? ':' returnTarget post_assignments_list
    ;
input_action
    : action
    ;
output_action
    : action
    ;

/** ACTION PARAMS */
post_assignments_list
    : ('[' ID (',' ID)* ']')?
    ;
returnTarget
    : state
    | decision_state
    | id
    ;
action_properties
    : '[' ratio ']'
    | '[' ratio ( ';' pre_assignments_list ) ( ';' predicates_list ) ']'
    | '[' pre_assignments_list ( ';' predicates_list ) ']'
    ;
ratio
    : (ADD | SUB)? NUMBER
    ;
pre_assignments_list
    : AUTO_ASSIGN
    | ID (',' ID)*
    ;
predicates_list
    : AUTO_ASSIGN
    | ID (',' ID)*
    ;

/** COMMON */
id
    : END
    | ID
    ;

/** -------------------------
 *  LEXER RULES
 *  ------------------------- */

/* keywords */
TYPESTATE   : 'typestate';
END         : 'end';
IMPORT      : 'import';
PACKAGE     : 'package';
STATIC      : 'static';
KEYED       : 'keyed';
//LATE        : 'late';

/* math operators */
ADD         : '+';
MULTIPLY    : '*';
DIVIDE      : '/';
SUB         : '-';

/* boolean operators */
AND         : '&&';
OR          : '||';
NOT         : '!';
EQ          : '=';
NEQ         : '!=';
GT          : '>';
GEQ         : '>=';
LT          : '<';
LEQ         : '<=';
TRUE        : 'true';
FALSE       : 'false';

/* common */
AUTO_ASSIGN : '_';
NUMBER      : [0-9]+ ('.' [0-9]+)? ;
ID          : [$_a-zA-Z] [$_a-zA-Z0-9]* ;

/* whitespace/comments */
WS          : [ \t\r\n]+ -> skip ;
BlComment   : '/*' .*? '*/' -> skip ;
LnComment   : '//' ~[\r\n]* -> skip ;

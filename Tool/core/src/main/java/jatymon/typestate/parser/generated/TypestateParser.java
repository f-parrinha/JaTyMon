// Generated from Typestate.g4 by ANTLR 4.13.2

    package jatymon.typestate.parser.generated;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue", "this-escape"})
public class TypestateParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.13.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, TYPESTATE=17, 
		END=18, IMPORT=19, PACKAGE=20, STATIC=21, KEYED=22, ADD=23, MULTIPLY=24, 
		DIVIDE=25, SUB=26, AND=27, OR=28, NOT=29, EQ=30, NEQ=31, GT=32, GEQ=33, 
		LT=34, LEQ=35, TRUE=36, FALSE=37, AUTO_ASSIGN=38, NUMBER=39, ID=40, WS=41, 
		BlComment=42, LnComment=43;
	public static final int
		RULE_start = 0, RULE_ref = 1, RULE_javaType = 2, RULE_typeArguments = 3, 
		RULE_package_statement = 4, RULE_import_statement = 5, RULE_typestate_declaration = 6, 
		RULE_typestate_key = 7, RULE_internal_state_declaration = 8, RULE_ext_declaration = 9, 
		RULE_val_declaration = 10, RULE_predicate_declaration = 11, RULE_assignment_declaration = 12, 
		RULE_assign_expr = 13, RULE_bool_expr = 14, RULE_arith_expr = 15, RULE_cmp_expr = 16, 
		RULE_cmp_operator = 17, RULE_operand = 18, RULE_state_declaration = 19, 
		RULE_state = 20, RULE_input_state = 21, RULE_output_state = 22, RULE_decision_state = 23, 
		RULE_decision = 24, RULE_decision_label = 25, RULE_action = 26, RULE_input_action = 27, 
		RULE_output_action = 28, RULE_post_assignments_list = 29, RULE_returnTarget = 30, 
		RULE_action_properties = 31, RULE_ratio = 32, RULE_pre_assignments_list = 33, 
		RULE_predicates_list = 34, RULE_id = 35;
	private static String[] makeRuleNames() {
		return new String[] {
			"start", "ref", "javaType", "typeArguments", "package_statement", "import_statement", 
			"typestate_declaration", "typestate_key", "internal_state_declaration", 
			"ext_declaration", "val_declaration", "predicate_declaration", "assignment_declaration", 
			"assign_expr", "bool_expr", "arith_expr", "cmp_expr", "cmp_operator", 
			"operand", "state_declaration", "state", "input_state", "output_state", 
			"decision_state", "decision", "decision_label", "action", "input_action", 
			"output_action", "post_assignments_list", "returnTarget", "action_properties", 
			"ratio", "pre_assignments_list", "predicates_list", "id"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", "'[]'", "','", "';'", "'{'", "'}'", "'Ext'", "'Val'", "'Pred'", 
			"':'", "'Assign'", "':='", "'('", "')'", "'['", "']'", "'typestate'", 
			"'end'", "'import'", "'package'", "'static'", "'keyed'", "'+'", "'*'", 
			"'/'", "'-'", "'&&'", "'||'", "'!'", "'='", "'!='", "'>'", "'>='", "'<'", 
			"'<='", "'true'", "'false'", "'_'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, null, null, null, null, null, null, null, null, null, null, 
			null, null, null, null, null, "TYPESTATE", "END", "IMPORT", "PACKAGE", 
			"STATIC", "KEYED", "ADD", "MULTIPLY", "DIVIDE", "SUB", "AND", "OR", "NOT", 
			"EQ", "NEQ", "GT", "GEQ", "LT", "LEQ", "TRUE", "FALSE", "AUTO_ASSIGN", 
			"NUMBER", "ID", "WS", "BlComment", "LnComment"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "Typestate.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TypestateParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StartContext extends ParserRuleContext {
		public Typestate_declarationContext typestate_declaration() {
			return getRuleContext(Typestate_declarationContext.class,0);
		}
		public TerminalNode EOF() { return getToken(TypestateParser.EOF, 0); }
		public Package_statementContext package_statement() {
			return getRuleContext(Package_statementContext.class,0);
		}
		public List<Import_statementContext> import_statement() {
			return getRuleContexts(Import_statementContext.class);
		}
		public Import_statementContext import_statement(int i) {
			return getRuleContext(Import_statementContext.class,i);
		}
		public StartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_start; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterStart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitStart(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitStart(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StartContext start() throws RecognitionException {
		StartContext _localctx = new StartContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_start);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(73);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==PACKAGE) {
				{
				setState(72);
				package_statement();
				}
			}

			setState(78);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==IMPORT) {
				{
				{
				setState(75);
				import_statement();
				}
				}
				setState(80);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(81);
			typestate_declaration();
			setState(82);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RefContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(TypestateParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TypestateParser.ID, i);
		}
		public RefContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ref; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterRef(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitRef(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitRef(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RefContext ref() throws RecognitionException {
		RefContext _localctx = new RefContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_ref);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			match(ID);
			setState(89);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(85);
					match(T__0);
					setState(86);
					match(ID);
					}
					} 
				}
				setState(91);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,2,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class JavaTypeContext extends ParserRuleContext {
		public RefContext ref() {
			return getRuleContext(RefContext.class,0);
		}
		public TypeArgumentsContext typeArguments() {
			return getRuleContext(TypeArgumentsContext.class,0);
		}
		public JavaTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_javaType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterJavaType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitJavaType(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitJavaType(this);
			else return visitor.visitChildren(this);
		}
	}

	public final JavaTypeContext javaType() throws RecognitionException {
		JavaTypeContext _localctx = new JavaTypeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_javaType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			ref();
			setState(94);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==LT) {
				{
				setState(93);
				typeArguments();
				}
			}

			setState(99);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(96);
				match(T__1);
				}
				}
				setState(101);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class TypeArgumentsContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(TypestateParser.LT, 0); }
		public List<JavaTypeContext> javaType() {
			return getRuleContexts(JavaTypeContext.class);
		}
		public JavaTypeContext javaType(int i) {
			return getRuleContext(JavaTypeContext.class,i);
		}
		public TerminalNode GT() { return getToken(TypestateParser.GT, 0); }
		public TypeArgumentsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeArguments; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterTypeArguments(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitTypeArguments(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitTypeArguments(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TypeArgumentsContext typeArguments() throws RecognitionException {
		TypeArgumentsContext _localctx = new TypeArgumentsContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_typeArguments);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(102);
			match(LT);
			setState(103);
			javaType();
			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(104);
				match(T__2);
				setState(105);
				javaType();
				}
				}
				setState(110);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(111);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Package_statementContext extends ParserRuleContext {
		public TerminalNode PACKAGE() { return getToken(TypestateParser.PACKAGE, 0); }
		public RefContext ref() {
			return getRuleContext(RefContext.class,0);
		}
		public Package_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_package_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterPackage_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitPackage_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitPackage_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Package_statementContext package_statement() throws RecognitionException {
		Package_statementContext _localctx = new Package_statementContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_package_statement);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(PACKAGE);
			setState(114);
			ref();
			setState(115);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Import_statementContext extends ParserRuleContext {
		public TerminalNode IMPORT() { return getToken(TypestateParser.IMPORT, 0); }
		public RefContext ref() {
			return getRuleContext(RefContext.class,0);
		}
		public TerminalNode STATIC() { return getToken(TypestateParser.STATIC, 0); }
		public TerminalNode MULTIPLY() { return getToken(TypestateParser.MULTIPLY, 0); }
		public Import_statementContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_import_statement; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterImport_statement(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitImport_statement(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitImport_statement(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Import_statementContext import_statement() throws RecognitionException {
		Import_statementContext _localctx = new Import_statementContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_import_statement);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(117);
			match(IMPORT);
			setState(119);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==STATIC) {
				{
				setState(118);
				match(STATIC);
				}
			}

			setState(121);
			ref();
			setState(124);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(122);
				match(T__0);
				setState(123);
				match(MULTIPLY);
				}
			}

			setState(126);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Typestate_declarationContext extends ParserRuleContext {
		public TerminalNode TYPESTATE() { return getToken(TypestateParser.TYPESTATE, 0); }
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public Typestate_keyContext typestate_key() {
			return getRuleContext(Typestate_keyContext.class,0);
		}
		public List<Internal_state_declarationContext> internal_state_declaration() {
			return getRuleContexts(Internal_state_declarationContext.class);
		}
		public Internal_state_declarationContext internal_state_declaration(int i) {
			return getRuleContext(Internal_state_declarationContext.class,i);
		}
		public List<State_declarationContext> state_declaration() {
			return getRuleContexts(State_declarationContext.class);
		}
		public State_declarationContext state_declaration(int i) {
			return getRuleContext(State_declarationContext.class,i);
		}
		public Typestate_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typestate_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterTypestate_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitTypestate_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitTypestate_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Typestate_declarationContext typestate_declaration() throws RecognitionException {
		Typestate_declarationContext _localctx = new Typestate_declarationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_typestate_declaration);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(128);
			match(TYPESTATE);
			setState(129);
			match(ID);
			setState(131);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==KEYED) {
				{
				setState(130);
				typestate_key();
				}
			}

			setState(133);
			match(T__4);
			setState(137);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & 2944L) != 0)) {
				{
				{
				setState(134);
				internal_state_declaration();
				}
				}
				setState(139);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(143);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==ID) {
				{
				{
				setState(140);
				state_declaration();
				}
				}
				setState(145);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(146);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Typestate_keyContext extends ParserRuleContext {
		public TerminalNode KEYED() { return getToken(TypestateParser.KEYED, 0); }
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public Typestate_keyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typestate_key; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterTypestate_key(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitTypestate_key(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitTypestate_key(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Typestate_keyContext typestate_key() throws RecognitionException {
		Typestate_keyContext _localctx = new Typestate_keyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_typestate_key);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			match(KEYED);
			setState(149);
			match(ID);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Internal_state_declarationContext extends ParserRuleContext {
		public Ext_declarationContext ext_declaration() {
			return getRuleContext(Ext_declarationContext.class,0);
		}
		public Val_declarationContext val_declaration() {
			return getRuleContext(Val_declarationContext.class,0);
		}
		public Assignment_declarationContext assignment_declaration() {
			return getRuleContext(Assignment_declarationContext.class,0);
		}
		public Predicate_declarationContext predicate_declaration() {
			return getRuleContext(Predicate_declarationContext.class,0);
		}
		public Internal_state_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_internal_state_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterInternal_state_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitInternal_state_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitInternal_state_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Internal_state_declarationContext internal_state_declaration() throws RecognitionException {
		Internal_state_declarationContext _localctx = new Internal_state_declarationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_internal_state_declaration);
		try {
			setState(155);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__6:
				enterOuterAlt(_localctx, 1);
				{
				setState(151);
				ext_declaration();
				}
				break;
			case T__7:
				enterOuterAlt(_localctx, 2);
				{
				setState(152);
				val_declaration();
				}
				break;
			case T__10:
				enterOuterAlt(_localctx, 3);
				{
				setState(153);
				assignment_declaration();
				}
				break;
			case T__8:
				enterOuterAlt(_localctx, 4);
				{
				setState(154);
				predicate_declaration();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Ext_declarationContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public Ext_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ext_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterExt_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitExt_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitExt_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Ext_declarationContext ext_declaration() throws RecognitionException {
		Ext_declarationContext _localctx = new Ext_declarationContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_ext_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(157);
			match(T__6);
			setState(158);
			match(ID);
			setState(159);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Val_declarationContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public TerminalNode EQ() { return getToken(TypestateParser.EQ, 0); }
		public OperandContext operand() {
			return getRuleContext(OperandContext.class,0);
		}
		public Val_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_val_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterVal_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitVal_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitVal_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Val_declarationContext val_declaration() throws RecognitionException {
		Val_declarationContext _localctx = new Val_declarationContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_val_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(161);
			match(T__7);
			setState(162);
			match(ID);
			setState(163);
			match(EQ);
			setState(164);
			operand();
			setState(165);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Predicate_declarationContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public Bool_exprContext bool_expr() {
			return getRuleContext(Bool_exprContext.class,0);
		}
		public Predicate_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicate_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterPredicate_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitPredicate_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitPredicate_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Predicate_declarationContext predicate_declaration() throws RecognitionException {
		Predicate_declarationContext _localctx = new Predicate_declarationContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_predicate_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			match(T__8);
			setState(168);
			match(ID);
			setState(169);
			match(T__9);
			setState(170);
			bool_expr(0);
			setState(171);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Assignment_declarationContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public Assign_exprContext assign_expr() {
			return getRuleContext(Assign_exprContext.class,0);
		}
		public Assignment_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assignment_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterAssignment_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitAssignment_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitAssignment_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Assignment_declarationContext assignment_declaration() throws RecognitionException {
		Assignment_declarationContext _localctx = new Assignment_declarationContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_assignment_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(173);
			match(T__10);
			setState(174);
			match(ID);
			setState(175);
			match(T__9);
			setState(176);
			assign_expr();
			setState(177);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Assign_exprContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public Arith_exprContext arith_expr() {
			return getRuleContext(Arith_exprContext.class,0);
		}
		public Assign_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assign_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterAssign_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitAssign_expr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitAssign_expr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Assign_exprContext assign_expr() throws RecognitionException {
		Assign_exprContext _localctx = new Assign_exprContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_assign_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(179);
			match(ID);
			setState(180);
			match(T__11);
			setState(181);
			arith_expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Bool_exprContext extends ParserRuleContext {
		public TerminalNode NOT() { return getToken(TypestateParser.NOT, 0); }
		public List<Bool_exprContext> bool_expr() {
			return getRuleContexts(Bool_exprContext.class);
		}
		public Bool_exprContext bool_expr(int i) {
			return getRuleContext(Bool_exprContext.class,i);
		}
		public Cmp_exprContext cmp_expr() {
			return getRuleContext(Cmp_exprContext.class,0);
		}
		public TerminalNode TRUE() { return getToken(TypestateParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(TypestateParser.FALSE, 0); }
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public TerminalNode AND() { return getToken(TypestateParser.AND, 0); }
		public TerminalNode OR() { return getToken(TypestateParser.OR, 0); }
		public Bool_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterBool_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitBool_expr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitBool_expr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Bool_exprContext bool_expr() throws RecognitionException {
		return bool_expr(0);
	}

	private Bool_exprContext bool_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Bool_exprContext _localctx = new Bool_exprContext(_ctx, _parentState);
		Bool_exprContext _prevctx = _localctx;
		int _startState = 28;
		enterRecursionRule(_localctx, 28, RULE_bool_expr, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(194);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,12,_ctx) ) {
			case 1:
				{
				setState(184);
				match(NOT);
				setState(185);
				bool_expr(8);
				}
				break;
			case 2:
				{
				setState(186);
				match(T__12);
				setState(187);
				bool_expr(0);
				setState(188);
				match(T__13);
				}
				break;
			case 3:
				{
				setState(190);
				cmp_expr();
				}
				break;
			case 4:
				{
				setState(191);
				match(TRUE);
				}
				break;
			case 5:
				{
				setState(192);
				match(FALSE);
				}
				break;
			case 6:
				{
				setState(193);
				match(ID);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(204);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(202);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
					case 1:
						{
						_localctx = new Bool_exprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_bool_expr);
						setState(196);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(197);
						match(AND);
						setState(198);
						bool_expr(8);
						}
						break;
					case 2:
						{
						_localctx = new Bool_exprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_bool_expr);
						setState(199);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(200);
						match(OR);
						setState(201);
						bool_expr(7);
						}
						break;
					}
					} 
				}
				setState(206);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,14,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Arith_exprContext extends ParserRuleContext {
		public List<Arith_exprContext> arith_expr() {
			return getRuleContexts(Arith_exprContext.class);
		}
		public Arith_exprContext arith_expr(int i) {
			return getRuleContext(Arith_exprContext.class,i);
		}
		public OperandContext operand() {
			return getRuleContext(OperandContext.class,0);
		}
		public TerminalNode MULTIPLY() { return getToken(TypestateParser.MULTIPLY, 0); }
		public TerminalNode DIVIDE() { return getToken(TypestateParser.DIVIDE, 0); }
		public TerminalNode ADD() { return getToken(TypestateParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(TypestateParser.SUB, 0); }
		public Arith_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arith_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterArith_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitArith_expr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitArith_expr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Arith_exprContext arith_expr() throws RecognitionException {
		return arith_expr(0);
	}

	private Arith_exprContext arith_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Arith_exprContext _localctx = new Arith_exprContext(_ctx, _parentState);
		Arith_exprContext _prevctx = _localctx;
		int _startState = 30;
		enterRecursionRule(_localctx, 30, RULE_arith_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__12:
				{
				setState(208);
				match(T__12);
				setState(209);
				arith_expr(0);
				setState(210);
				match(T__13);
				}
				break;
			case NUMBER:
			case ID:
				{
				setState(212);
				operand();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(223);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(221);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
					case 1:
						{
						_localctx = new Arith_exprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_arith_expr);
						setState(215);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(216);
						_la = _input.LA(1);
						if ( !(_la==MULTIPLY || _la==DIVIDE) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(217);
						arith_expr(5);
						}
						break;
					case 2:
						{
						_localctx = new Arith_exprContext(_parentctx, _parentState);
						pushNewRecursionContext(_localctx, _startState, RULE_arith_expr);
						setState(218);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(219);
						_la = _input.LA(1);
						if ( !(_la==ADD || _la==SUB) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(220);
						arith_expr(4);
						}
						break;
					}
					} 
				}
				setState(225);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,17,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cmp_exprContext extends ParserRuleContext {
		public List<Arith_exprContext> arith_expr() {
			return getRuleContexts(Arith_exprContext.class);
		}
		public Arith_exprContext arith_expr(int i) {
			return getRuleContext(Arith_exprContext.class,i);
		}
		public Cmp_operatorContext cmp_operator() {
			return getRuleContext(Cmp_operatorContext.class,0);
		}
		public Cmp_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmp_expr; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterCmp_expr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitCmp_expr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitCmp_expr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cmp_exprContext cmp_expr() throws RecognitionException {
		Cmp_exprContext _localctx = new Cmp_exprContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_cmp_expr);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(226);
			arith_expr(0);
			setState(227);
			cmp_operator();
			setState(228);
			arith_expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Cmp_operatorContext extends ParserRuleContext {
		public TerminalNode EQ() { return getToken(TypestateParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(TypestateParser.NEQ, 0); }
		public TerminalNode GT() { return getToken(TypestateParser.GT, 0); }
		public TerminalNode GEQ() { return getToken(TypestateParser.GEQ, 0); }
		public TerminalNode LT() { return getToken(TypestateParser.LT, 0); }
		public TerminalNode LEQ() { return getToken(TypestateParser.LEQ, 0); }
		public Cmp_operatorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cmp_operator; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterCmp_operator(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitCmp_operator(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitCmp_operator(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cmp_operatorContext cmp_operator() throws RecognitionException {
		Cmp_operatorContext _localctx = new Cmp_operatorContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_cmp_operator);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(230);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 67645734912L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class OperandContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public TerminalNode NUMBER() { return getToken(TypestateParser.NUMBER, 0); }
		public OperandContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_operand; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterOperand(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitOperand(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitOperand(this);
			else return visitor.visitChildren(this);
		}
	}

	public final OperandContext operand() throws RecognitionException {
		OperandContext _localctx = new OperandContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_operand);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(232);
			_la = _input.LA(1);
			if ( !(_la==NUMBER || _la==ID) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class State_declarationContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public TerminalNode EQ() { return getToken(TypestateParser.EQ, 0); }
		public StateContext state() {
			return getRuleContext(StateContext.class,0);
		}
		public State_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state_declaration; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterState_declaration(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitState_declaration(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitState_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final State_declarationContext state_declaration() throws RecognitionException {
		State_declarationContext _localctx = new State_declarationContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_state_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			match(ID);
			setState(235);
			match(EQ);
			setState(236);
			state();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class StateContext extends ParserRuleContext {
		public Input_stateContext input_state() {
			return getRuleContext(Input_stateContext.class,0);
		}
		public Output_stateContext output_state() {
			return getRuleContext(Output_stateContext.class,0);
		}
		public TerminalNode ADD() { return getToken(TypestateParser.ADD, 0); }
		public StateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_state; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterState(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitState(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitState(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StateContext state() throws RecognitionException {
		StateContext _localctx = new StateContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_state);
		try {
			setState(248);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(238);
				input_state();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(239);
				output_state();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(240);
				input_state();
				setState(241);
				match(ADD);
				setState(242);
				output_state();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(244);
				output_state();
				setState(245);
				match(ADD);
				setState(246);
				input_state();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Input_stateContext extends ParserRuleContext {
		public List<Input_actionContext> input_action() {
			return getRuleContexts(Input_actionContext.class);
		}
		public Input_actionContext input_action(int i) {
			return getRuleContext(Input_actionContext.class,i);
		}
		public Input_stateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_input_state; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterInput_state(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitInput_state(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitInput_state(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Input_stateContext input_state() throws RecognitionException {
		Input_stateContext _localctx = new Input_stateContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_input_state);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(250);
			match(T__4);
			setState(259);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(251);
				input_action();
				setState(256);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(252);
					match(T__2);
					setState(253);
					input_action();
					}
					}
					setState(258);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(261);
			match(T__5);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Output_stateContext extends ParserRuleContext {
		public List<Output_actionContext> output_action() {
			return getRuleContexts(Output_actionContext.class);
		}
		public Output_actionContext output_action(int i) {
			return getRuleContext(Output_actionContext.class,i);
		}
		public Output_stateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_output_state; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterOutput_state(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitOutput_state(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitOutput_state(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Output_stateContext output_state() throws RecognitionException {
		Output_stateContext _localctx = new Output_stateContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_output_state);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			match(T__12);
			setState(272);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(264);
				output_action();
				setState(269);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(265);
					match(T__2);
					setState(266);
					output_action();
					}
					}
					setState(271);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(274);
			match(T__13);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Decision_stateContext extends ParserRuleContext {
		public TerminalNode LT() { return getToken(TypestateParser.LT, 0); }
		public List<DecisionContext> decision() {
			return getRuleContexts(DecisionContext.class);
		}
		public DecisionContext decision(int i) {
			return getRuleContext(DecisionContext.class,i);
		}
		public TerminalNode GT() { return getToken(TypestateParser.GT, 0); }
		public Decision_stateContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decision_state; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterDecision_state(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitDecision_state(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitDecision_state(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Decision_stateContext decision_state() throws RecognitionException {
		Decision_stateContext _localctx = new Decision_stateContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_decision_state);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(276);
			match(LT);
			setState(277);
			decision();
			setState(282);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(278);
				match(T__2);
				setState(279);
				decision();
				}
				}
				setState(284);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(285);
			match(GT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class DecisionContext extends ParserRuleContext {
		public Decision_labelContext decision_label() {
			return getRuleContext(Decision_labelContext.class,0);
		}
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public StateContext state() {
			return getRuleContext(StateContext.class,0);
		}
		public DecisionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decision; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterDecision(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitDecision(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitDecision(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DecisionContext decision() throws RecognitionException {
		DecisionContext _localctx = new DecisionContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_decision);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(287);
			decision_label();
			setState(288);
			match(T__9);
			setState(291);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case END:
			case ID:
				{
				setState(289);
				id();
				}
				break;
			case T__4:
			case T__12:
				{
				setState(290);
				state();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Decision_labelContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public TerminalNode TRUE() { return getToken(TypestateParser.TRUE, 0); }
		public TerminalNode FALSE() { return getToken(TypestateParser.FALSE, 0); }
		public Decision_labelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_decision_label; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterDecision_label(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitDecision_label(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitDecision_label(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Decision_labelContext decision_label() throws RecognitionException {
		Decision_labelContext _localctx = new Decision_labelContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_decision_label);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(293);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & 1305670057984L) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ActionContext extends ParserRuleContext {
		public List<JavaTypeContext> javaType() {
			return getRuleContexts(JavaTypeContext.class);
		}
		public JavaTypeContext javaType(int i) {
			return getRuleContext(JavaTypeContext.class,i);
		}
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public ReturnTargetContext returnTarget() {
			return getRuleContext(ReturnTargetContext.class,0);
		}
		public Post_assignments_listContext post_assignments_list() {
			return getRuleContext(Post_assignments_listContext.class,0);
		}
		public Action_propertiesContext action_properties() {
			return getRuleContext(Action_propertiesContext.class,0);
		}
		public ActionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterAction(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitAction(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitAction(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActionContext action() throws RecognitionException {
		ActionContext _localctx = new ActionContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_action);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			javaType();
			setState(296);
			match(ID);
			setState(297);
			match(T__12);
			setState(306);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(298);
				javaType();
				setState(303);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(299);
					match(T__2);
					setState(300);
					javaType();
					}
					}
					setState(305);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
			}

			setState(308);
			match(T__13);
			setState(310);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(309);
				action_properties();
				}
			}

			setState(312);
			match(T__9);
			setState(313);
			returnTarget();
			setState(314);
			post_assignments_list();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Input_actionContext extends ParserRuleContext {
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public Input_actionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_input_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterInput_action(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitInput_action(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitInput_action(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Input_actionContext input_action() throws RecognitionException {
		Input_actionContext _localctx = new Input_actionContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_input_action);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			action();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Output_actionContext extends ParserRuleContext {
		public ActionContext action() {
			return getRuleContext(ActionContext.class,0);
		}
		public Output_actionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_output_action; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterOutput_action(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitOutput_action(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitOutput_action(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Output_actionContext output_action() throws RecognitionException {
		Output_actionContext _localctx = new Output_actionContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_output_action);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(318);
			action();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Post_assignments_listContext extends ParserRuleContext {
		public List<TerminalNode> ID() { return getTokens(TypestateParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TypestateParser.ID, i);
		}
		public Post_assignments_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_post_assignments_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterPost_assignments_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitPost_assignments_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitPost_assignments_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Post_assignments_listContext post_assignments_list() throws RecognitionException {
		Post_assignments_listContext _localctx = new Post_assignments_listContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_post_assignments_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==T__14) {
				{
				setState(320);
				match(T__14);
				setState(321);
				match(ID);
				setState(326);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(322);
					match(T__2);
					setState(323);
					match(ID);
					}
					}
					setState(328);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(329);
				match(T__15);
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class ReturnTargetContext extends ParserRuleContext {
		public StateContext state() {
			return getRuleContext(StateContext.class,0);
		}
		public Decision_stateContext decision_state() {
			return getRuleContext(Decision_stateContext.class,0);
		}
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public ReturnTargetContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_returnTarget; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterReturnTarget(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitReturnTarget(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitReturnTarget(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ReturnTargetContext returnTarget() throws RecognitionException {
		ReturnTargetContext _localctx = new ReturnTargetContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_returnTarget);
		try {
			setState(335);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__4:
			case T__12:
				enterOuterAlt(_localctx, 1);
				{
				setState(332);
				state();
				}
				break;
			case LT:
				enterOuterAlt(_localctx, 2);
				{
				setState(333);
				decision_state();
				}
				break;
			case END:
			case ID:
				enterOuterAlt(_localctx, 3);
				{
				setState(334);
				id();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Action_propertiesContext extends ParserRuleContext {
		public RatioContext ratio() {
			return getRuleContext(RatioContext.class,0);
		}
		public Pre_assignments_listContext pre_assignments_list() {
			return getRuleContext(Pre_assignments_listContext.class,0);
		}
		public Predicates_listContext predicates_list() {
			return getRuleContext(Predicates_listContext.class,0);
		}
		public Action_propertiesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_action_properties; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterAction_properties(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitAction_properties(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitAction_properties(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Action_propertiesContext action_properties() throws RecognitionException {
		Action_propertiesContext _localctx = new Action_propertiesContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_action_properties);
		try {
			setState(358);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(337);
				match(T__14);
				setState(338);
				ratio();
				setState(339);
				match(T__15);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(341);
				match(T__14);
				setState(342);
				ratio();
				{
				setState(343);
				match(T__3);
				setState(344);
				pre_assignments_list();
				}
				{
				setState(346);
				match(T__3);
				setState(347);
				predicates_list();
				}
				setState(349);
				match(T__15);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(351);
				match(T__14);
				setState(352);
				pre_assignments_list();
				{
				setState(353);
				match(T__3);
				setState(354);
				predicates_list();
				}
				setState(356);
				match(T__15);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class RatioContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(TypestateParser.NUMBER, 0); }
		public TerminalNode ADD() { return getToken(TypestateParser.ADD, 0); }
		public TerminalNode SUB() { return getToken(TypestateParser.SUB, 0); }
		public RatioContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_ratio; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterRatio(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitRatio(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitRatio(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RatioContext ratio() throws RecognitionException {
		RatioContext _localctx = new RatioContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_ratio);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(361);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ADD || _la==SUB) {
				{
				setState(360);
				_la = _input.LA(1);
				if ( !(_la==ADD || _la==SUB) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				}
			}

			setState(363);
			match(NUMBER);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Pre_assignments_listContext extends ParserRuleContext {
		public TerminalNode AUTO_ASSIGN() { return getToken(TypestateParser.AUTO_ASSIGN, 0); }
		public List<TerminalNode> ID() { return getTokens(TypestateParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TypestateParser.ID, i);
		}
		public Pre_assignments_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pre_assignments_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterPre_assignments_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitPre_assignments_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitPre_assignments_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Pre_assignments_listContext pre_assignments_list() throws RecognitionException {
		Pre_assignments_listContext _localctx = new Pre_assignments_listContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_pre_assignments_list);
		int _la;
		try {
			setState(374);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AUTO_ASSIGN:
				enterOuterAlt(_localctx, 1);
				{
				setState(365);
				match(AUTO_ASSIGN);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(366);
				match(ID);
				setState(371);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(367);
					match(T__2);
					setState(368);
					match(ID);
					}
					}
					setState(373);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class Predicates_listContext extends ParserRuleContext {
		public TerminalNode AUTO_ASSIGN() { return getToken(TypestateParser.AUTO_ASSIGN, 0); }
		public List<TerminalNode> ID() { return getTokens(TypestateParser.ID); }
		public TerminalNode ID(int i) {
			return getToken(TypestateParser.ID, i);
		}
		public Predicates_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_predicates_list; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterPredicates_list(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitPredicates_list(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitPredicates_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Predicates_listContext predicates_list() throws RecognitionException {
		Predicates_listContext _localctx = new Predicates_listContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_predicates_list);
		int _la;
		try {
			setState(385);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case AUTO_ASSIGN:
				enterOuterAlt(_localctx, 1);
				{
				setState(376);
				match(AUTO_ASSIGN);
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(377);
				match(ID);
				setState(382);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(378);
					match(T__2);
					setState(379);
					match(ID);
					}
					}
					setState(384);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	@SuppressWarnings("CheckReturnValue")
	public static class IdContext extends ParserRuleContext {
		public TerminalNode END() { return getToken(TypestateParser.END, 0); }
		public TerminalNode ID() { return getToken(TypestateParser.ID, 0); }
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypestateListener ) ((TypestateListener)listener).exitId(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof TypestateVisitor ) return ((TypestateVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_id);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			_la = _input.LA(1);
			if ( !(_la==END || _la==ID) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 14:
			return bool_expr_sempred((Bool_exprContext)_localctx, predIndex);
		case 15:
			return arith_expr_sempred((Arith_exprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean bool_expr_sempred(Bool_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 7);
		case 1:
			return precpred(_ctx, 6);
		}
		return true;
	}
	private boolean arith_expr_sempred(Arith_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 2:
			return precpred(_ctx, 4);
		case 3:
			return precpred(_ctx, 3);
		}
		return true;
	}

	public static final String _serializedATN =
		"\u0004\u0001+\u0186\u0002\u0000\u0007\u0000\u0002\u0001\u0007\u0001\u0002"+
		"\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002\u0004\u0007\u0004\u0002"+
		"\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002\u0007\u0007\u0007\u0002"+
		"\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002\u000b\u0007\u000b\u0002"+
		"\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e\u0002\u000f\u0007\u000f"+
		"\u0002\u0010\u0007\u0010\u0002\u0011\u0007\u0011\u0002\u0012\u0007\u0012"+
		"\u0002\u0013\u0007\u0013\u0002\u0014\u0007\u0014\u0002\u0015\u0007\u0015"+
		"\u0002\u0016\u0007\u0016\u0002\u0017\u0007\u0017\u0002\u0018\u0007\u0018"+
		"\u0002\u0019\u0007\u0019\u0002\u001a\u0007\u001a\u0002\u001b\u0007\u001b"+
		"\u0002\u001c\u0007\u001c\u0002\u001d\u0007\u001d\u0002\u001e\u0007\u001e"+
		"\u0002\u001f\u0007\u001f\u0002 \u0007 \u0002!\u0007!\u0002\"\u0007\"\u0002"+
		"#\u0007#\u0001\u0000\u0003\u0000J\b\u0000\u0001\u0000\u0005\u0000M\b\u0000"+
		"\n\u0000\f\u0000P\t\u0000\u0001\u0000\u0001\u0000\u0001\u0000\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0005\u0001X\b\u0001\n\u0001\f\u0001[\t\u0001"+
		"\u0001\u0002\u0001\u0002\u0003\u0002_\b\u0002\u0001\u0002\u0005\u0002"+
		"b\b\u0002\n\u0002\f\u0002e\t\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0005\u0003k\b\u0003\n\u0003\f\u0003n\t\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0005"+
		"\u0001\u0005\u0003\u0005x\b\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0003\u0005}\b\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0003\u0006\u0084\b\u0006\u0001\u0006\u0001\u0006\u0005\u0006"+
		"\u0088\b\u0006\n\u0006\f\u0006\u008b\t\u0006\u0001\u0006\u0005\u0006\u008e"+
		"\b\u0006\n\u0006\f\u0006\u0091\t\u0006\u0001\u0006\u0001\u0006\u0001\u0007"+
		"\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001\b\u0001\b\u0003\b\u009c"+
		"\b\b\u0001\t\u0001\t\u0001\t\u0001\t\u0001\n\u0001\n\u0001\n\u0001\n\u0001"+
		"\n\u0001\n\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b\u0001\u000b"+
		"\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\f\u0001\r\u0001"+
		"\r\u0001\r\u0001\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0003\u000e\u00c3\b\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0005\u000e\u00cb\b\u000e\n\u000e\f\u000e"+
		"\u00ce\t\u000e\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0003\u000f\u00d6\b\u000f\u0001\u000f\u0001\u000f\u0001\u000f"+
		"\u0001\u000f\u0001\u000f\u0001\u000f\u0005\u000f\u00de\b\u000f\n\u000f"+
		"\f\u000f\u00e1\t\u000f\u0001\u0010\u0001\u0010\u0001\u0010\u0001\u0010"+
		"\u0001\u0011\u0001\u0011\u0001\u0012\u0001\u0012\u0001\u0013\u0001\u0013"+
		"\u0001\u0013\u0001\u0013\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014\u0001\u0014"+
		"\u0003\u0014\u00f9\b\u0014\u0001\u0015\u0001\u0015\u0001\u0015\u0001\u0015"+
		"\u0005\u0015\u00ff\b\u0015\n\u0015\f\u0015\u0102\t\u0015\u0003\u0015\u0104"+
		"\b\u0015\u0001\u0015\u0001\u0015\u0001\u0016\u0001\u0016\u0001\u0016\u0001"+
		"\u0016\u0005\u0016\u010c\b\u0016\n\u0016\f\u0016\u010f\t\u0016\u0003\u0016"+
		"\u0111\b\u0016\u0001\u0016\u0001\u0016\u0001\u0017\u0001\u0017\u0001\u0017"+
		"\u0001\u0017\u0005\u0017\u0119\b\u0017\n\u0017\f\u0017\u011c\t\u0017\u0001"+
		"\u0017\u0001\u0017\u0001\u0018\u0001\u0018\u0001\u0018\u0001\u0018\u0003"+
		"\u0018\u0124\b\u0018\u0001\u0019\u0001\u0019\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0005\u001a\u012e\b\u001a\n"+
		"\u001a\f\u001a\u0131\t\u001a\u0003\u001a\u0133\b\u001a\u0001\u001a\u0001"+
		"\u001a\u0003\u001a\u0137\b\u001a\u0001\u001a\u0001\u001a\u0001\u001a\u0001"+
		"\u001a\u0001\u001b\u0001\u001b\u0001\u001c\u0001\u001c\u0001\u001d\u0001"+
		"\u001d\u0001\u001d\u0001\u001d\u0005\u001d\u0145\b\u001d\n\u001d\f\u001d"+
		"\u0148\t\u001d\u0001\u001d\u0003\u001d\u014b\b\u001d\u0001\u001e\u0001"+
		"\u001e\u0001\u001e\u0003\u001e\u0150\b\u001e\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001\u001f\u0001"+
		"\u001f\u0003\u001f\u0167\b\u001f\u0001 \u0003 \u016a\b \u0001 \u0001 "+
		"\u0001!\u0001!\u0001!\u0001!\u0005!\u0172\b!\n!\f!\u0175\t!\u0003!\u0177"+
		"\b!\u0001\"\u0001\"\u0001\"\u0001\"\u0005\"\u017d\b\"\n\"\f\"\u0180\t"+
		"\"\u0003\"\u0182\b\"\u0001#\u0001#\u0001#\u0000\u0002\u001c\u001e$\u0000"+
		"\u0002\u0004\u0006\b\n\f\u000e\u0010\u0012\u0014\u0016\u0018\u001a\u001c"+
		"\u001e \"$&(*,.02468:<>@BDF\u0000\u0006\u0001\u0000\u0018\u0019\u0002"+
		"\u0000\u0017\u0017\u001a\u001a\u0001\u0000\u001e#\u0001\u0000\'(\u0002"+
		"\u0000$%((\u0002\u0000\u0012\u0012((\u0190\u0000I\u0001\u0000\u0000\u0000"+
		"\u0002T\u0001\u0000\u0000\u0000\u0004\\\u0001\u0000\u0000\u0000\u0006"+
		"f\u0001\u0000\u0000\u0000\bq\u0001\u0000\u0000\u0000\nu\u0001\u0000\u0000"+
		"\u0000\f\u0080\u0001\u0000\u0000\u0000\u000e\u0094\u0001\u0000\u0000\u0000"+
		"\u0010\u009b\u0001\u0000\u0000\u0000\u0012\u009d\u0001\u0000\u0000\u0000"+
		"\u0014\u00a1\u0001\u0000\u0000\u0000\u0016\u00a7\u0001\u0000\u0000\u0000"+
		"\u0018\u00ad\u0001\u0000\u0000\u0000\u001a\u00b3\u0001\u0000\u0000\u0000"+
		"\u001c\u00c2\u0001\u0000\u0000\u0000\u001e\u00d5\u0001\u0000\u0000\u0000"+
		" \u00e2\u0001\u0000\u0000\u0000\"\u00e6\u0001\u0000\u0000\u0000$\u00e8"+
		"\u0001\u0000\u0000\u0000&\u00ea\u0001\u0000\u0000\u0000(\u00f8\u0001\u0000"+
		"\u0000\u0000*\u00fa\u0001\u0000\u0000\u0000,\u0107\u0001\u0000\u0000\u0000"+
		".\u0114\u0001\u0000\u0000\u00000\u011f\u0001\u0000\u0000\u00002\u0125"+
		"\u0001\u0000\u0000\u00004\u0127\u0001\u0000\u0000\u00006\u013c\u0001\u0000"+
		"\u0000\u00008\u013e\u0001\u0000\u0000\u0000:\u014a\u0001\u0000\u0000\u0000"+
		"<\u014f\u0001\u0000\u0000\u0000>\u0166\u0001\u0000\u0000\u0000@\u0169"+
		"\u0001\u0000\u0000\u0000B\u0176\u0001\u0000\u0000\u0000D\u0181\u0001\u0000"+
		"\u0000\u0000F\u0183\u0001\u0000\u0000\u0000HJ\u0003\b\u0004\u0000IH\u0001"+
		"\u0000\u0000\u0000IJ\u0001\u0000\u0000\u0000JN\u0001\u0000\u0000\u0000"+
		"KM\u0003\n\u0005\u0000LK\u0001\u0000\u0000\u0000MP\u0001\u0000\u0000\u0000"+
		"NL\u0001\u0000\u0000\u0000NO\u0001\u0000\u0000\u0000OQ\u0001\u0000\u0000"+
		"\u0000PN\u0001\u0000\u0000\u0000QR\u0003\f\u0006\u0000RS\u0005\u0000\u0000"+
		"\u0001S\u0001\u0001\u0000\u0000\u0000TY\u0005(\u0000\u0000UV\u0005\u0001"+
		"\u0000\u0000VX\u0005(\u0000\u0000WU\u0001\u0000\u0000\u0000X[\u0001\u0000"+
		"\u0000\u0000YW\u0001\u0000\u0000\u0000YZ\u0001\u0000\u0000\u0000Z\u0003"+
		"\u0001\u0000\u0000\u0000[Y\u0001\u0000\u0000\u0000\\^\u0003\u0002\u0001"+
		"\u0000]_\u0003\u0006\u0003\u0000^]\u0001\u0000\u0000\u0000^_\u0001\u0000"+
		"\u0000\u0000_c\u0001\u0000\u0000\u0000`b\u0005\u0002\u0000\u0000a`\u0001"+
		"\u0000\u0000\u0000be\u0001\u0000\u0000\u0000ca\u0001\u0000\u0000\u0000"+
		"cd\u0001\u0000\u0000\u0000d\u0005\u0001\u0000\u0000\u0000ec\u0001\u0000"+
		"\u0000\u0000fg\u0005\"\u0000\u0000gl\u0003\u0004\u0002\u0000hi\u0005\u0003"+
		"\u0000\u0000ik\u0003\u0004\u0002\u0000jh\u0001\u0000\u0000\u0000kn\u0001"+
		"\u0000\u0000\u0000lj\u0001\u0000\u0000\u0000lm\u0001\u0000\u0000\u0000"+
		"mo\u0001\u0000\u0000\u0000nl\u0001\u0000\u0000\u0000op\u0005 \u0000\u0000"+
		"p\u0007\u0001\u0000\u0000\u0000qr\u0005\u0014\u0000\u0000rs\u0003\u0002"+
		"\u0001\u0000st\u0005\u0004\u0000\u0000t\t\u0001\u0000\u0000\u0000uw\u0005"+
		"\u0013\u0000\u0000vx\u0005\u0015\u0000\u0000wv\u0001\u0000\u0000\u0000"+
		"wx\u0001\u0000\u0000\u0000xy\u0001\u0000\u0000\u0000y|\u0003\u0002\u0001"+
		"\u0000z{\u0005\u0001\u0000\u0000{}\u0005\u0018\u0000\u0000|z\u0001\u0000"+
		"\u0000\u0000|}\u0001\u0000\u0000\u0000}~\u0001\u0000\u0000\u0000~\u007f"+
		"\u0005\u0004\u0000\u0000\u007f\u000b\u0001\u0000\u0000\u0000\u0080\u0081"+
		"\u0005\u0011\u0000\u0000\u0081\u0083\u0005(\u0000\u0000\u0082\u0084\u0003"+
		"\u000e\u0007\u0000\u0083\u0082\u0001\u0000\u0000\u0000\u0083\u0084\u0001"+
		"\u0000\u0000\u0000\u0084\u0085\u0001\u0000\u0000\u0000\u0085\u0089\u0005"+
		"\u0005\u0000\u0000\u0086\u0088\u0003\u0010\b\u0000\u0087\u0086\u0001\u0000"+
		"\u0000\u0000\u0088\u008b\u0001\u0000\u0000\u0000\u0089\u0087\u0001\u0000"+
		"\u0000\u0000\u0089\u008a\u0001\u0000\u0000\u0000\u008a\u008f\u0001\u0000"+
		"\u0000\u0000\u008b\u0089\u0001\u0000\u0000\u0000\u008c\u008e\u0003&\u0013"+
		"\u0000\u008d\u008c\u0001\u0000\u0000\u0000\u008e\u0091\u0001\u0000\u0000"+
		"\u0000\u008f\u008d\u0001\u0000\u0000\u0000\u008f\u0090\u0001\u0000\u0000"+
		"\u0000\u0090\u0092\u0001\u0000\u0000\u0000\u0091\u008f\u0001\u0000\u0000"+
		"\u0000\u0092\u0093\u0005\u0006\u0000\u0000\u0093\r\u0001\u0000\u0000\u0000"+
		"\u0094\u0095\u0005\u0016\u0000\u0000\u0095\u0096\u0005(\u0000\u0000\u0096"+
		"\u000f\u0001\u0000\u0000\u0000\u0097\u009c\u0003\u0012\t\u0000\u0098\u009c"+
		"\u0003\u0014\n\u0000\u0099\u009c\u0003\u0018\f\u0000\u009a\u009c\u0003"+
		"\u0016\u000b\u0000\u009b\u0097\u0001\u0000\u0000\u0000\u009b\u0098\u0001"+
		"\u0000\u0000\u0000\u009b\u0099\u0001\u0000\u0000\u0000\u009b\u009a\u0001"+
		"\u0000\u0000\u0000\u009c\u0011\u0001\u0000\u0000\u0000\u009d\u009e\u0005"+
		"\u0007\u0000\u0000\u009e\u009f\u0005(\u0000\u0000\u009f\u00a0\u0005\u0004"+
		"\u0000\u0000\u00a0\u0013\u0001\u0000\u0000\u0000\u00a1\u00a2\u0005\b\u0000"+
		"\u0000\u00a2\u00a3\u0005(\u0000\u0000\u00a3\u00a4\u0005\u001e\u0000\u0000"+
		"\u00a4\u00a5\u0003$\u0012\u0000\u00a5\u00a6\u0005\u0004\u0000\u0000\u00a6"+
		"\u0015\u0001\u0000\u0000\u0000\u00a7\u00a8\u0005\t\u0000\u0000\u00a8\u00a9"+
		"\u0005(\u0000\u0000\u00a9\u00aa\u0005\n\u0000\u0000\u00aa\u00ab\u0003"+
		"\u001c\u000e\u0000\u00ab\u00ac\u0005\u0004\u0000\u0000\u00ac\u0017\u0001"+
		"\u0000\u0000\u0000\u00ad\u00ae\u0005\u000b\u0000\u0000\u00ae\u00af\u0005"+
		"(\u0000\u0000\u00af\u00b0\u0005\n\u0000\u0000\u00b0\u00b1\u0003\u001a"+
		"\r\u0000\u00b1\u00b2\u0005\u0004\u0000\u0000\u00b2\u0019\u0001\u0000\u0000"+
		"\u0000\u00b3\u00b4\u0005(\u0000\u0000\u00b4\u00b5\u0005\f\u0000\u0000"+
		"\u00b5\u00b6\u0003\u001e\u000f\u0000\u00b6\u001b\u0001\u0000\u0000\u0000"+
		"\u00b7\u00b8\u0006\u000e\uffff\uffff\u0000\u00b8\u00b9\u0005\u001d\u0000"+
		"\u0000\u00b9\u00c3\u0003\u001c\u000e\b\u00ba\u00bb\u0005\r\u0000\u0000"+
		"\u00bb\u00bc\u0003\u001c\u000e\u0000\u00bc\u00bd\u0005\u000e\u0000\u0000"+
		"\u00bd\u00c3\u0001\u0000\u0000\u0000\u00be\u00c3\u0003 \u0010\u0000\u00bf"+
		"\u00c3\u0005$\u0000\u0000\u00c0\u00c3\u0005%\u0000\u0000\u00c1\u00c3\u0005"+
		"(\u0000\u0000\u00c2\u00b7\u0001\u0000\u0000\u0000\u00c2\u00ba\u0001\u0000"+
		"\u0000\u0000\u00c2\u00be\u0001\u0000\u0000\u0000\u00c2\u00bf\u0001\u0000"+
		"\u0000\u0000\u00c2\u00c0\u0001\u0000\u0000\u0000\u00c2\u00c1\u0001\u0000"+
		"\u0000\u0000\u00c3\u00cc\u0001\u0000\u0000\u0000\u00c4\u00c5\n\u0007\u0000"+
		"\u0000\u00c5\u00c6\u0005\u001b\u0000\u0000\u00c6\u00cb\u0003\u001c\u000e"+
		"\b\u00c7\u00c8\n\u0006\u0000\u0000\u00c8\u00c9\u0005\u001c\u0000\u0000"+
		"\u00c9\u00cb\u0003\u001c\u000e\u0007\u00ca\u00c4\u0001\u0000\u0000\u0000"+
		"\u00ca\u00c7\u0001\u0000\u0000\u0000\u00cb\u00ce\u0001\u0000\u0000\u0000"+
		"\u00cc\u00ca\u0001\u0000\u0000\u0000\u00cc\u00cd\u0001\u0000\u0000\u0000"+
		"\u00cd\u001d\u0001\u0000\u0000\u0000\u00ce\u00cc\u0001\u0000\u0000\u0000"+
		"\u00cf\u00d0\u0006\u000f\uffff\uffff\u0000\u00d0\u00d1\u0005\r\u0000\u0000"+
		"\u00d1\u00d2\u0003\u001e\u000f\u0000\u00d2\u00d3\u0005\u000e\u0000\u0000"+
		"\u00d3\u00d6\u0001\u0000\u0000\u0000\u00d4\u00d6\u0003$\u0012\u0000\u00d5"+
		"\u00cf\u0001\u0000\u0000\u0000\u00d5\u00d4\u0001\u0000\u0000\u0000\u00d6"+
		"\u00df\u0001\u0000\u0000\u0000\u00d7\u00d8\n\u0004\u0000\u0000\u00d8\u00d9"+
		"\u0007\u0000\u0000\u0000\u00d9\u00de\u0003\u001e\u000f\u0005\u00da\u00db"+
		"\n\u0003\u0000\u0000\u00db\u00dc\u0007\u0001\u0000\u0000\u00dc\u00de\u0003"+
		"\u001e\u000f\u0004\u00dd\u00d7\u0001\u0000\u0000\u0000\u00dd\u00da\u0001"+
		"\u0000\u0000\u0000\u00de\u00e1\u0001\u0000\u0000\u0000\u00df\u00dd\u0001"+
		"\u0000\u0000\u0000\u00df\u00e0\u0001\u0000\u0000\u0000\u00e0\u001f\u0001"+
		"\u0000\u0000\u0000\u00e1\u00df\u0001\u0000\u0000\u0000\u00e2\u00e3\u0003"+
		"\u001e\u000f\u0000\u00e3\u00e4\u0003\"\u0011\u0000\u00e4\u00e5\u0003\u001e"+
		"\u000f\u0000\u00e5!\u0001\u0000\u0000\u0000\u00e6\u00e7\u0007\u0002\u0000"+
		"\u0000\u00e7#\u0001\u0000\u0000\u0000\u00e8\u00e9\u0007\u0003\u0000\u0000"+
		"\u00e9%\u0001\u0000\u0000\u0000\u00ea\u00eb\u0005(\u0000\u0000\u00eb\u00ec"+
		"\u0005\u001e\u0000\u0000\u00ec\u00ed\u0003(\u0014\u0000\u00ed\'\u0001"+
		"\u0000\u0000\u0000\u00ee\u00f9\u0003*\u0015\u0000\u00ef\u00f9\u0003,\u0016"+
		"\u0000\u00f0\u00f1\u0003*\u0015\u0000\u00f1\u00f2\u0005\u0017\u0000\u0000"+
		"\u00f2\u00f3\u0003,\u0016\u0000\u00f3\u00f9\u0001\u0000\u0000\u0000\u00f4"+
		"\u00f5\u0003,\u0016\u0000\u00f5\u00f6\u0005\u0017\u0000\u0000\u00f6\u00f7"+
		"\u0003*\u0015\u0000\u00f7\u00f9\u0001\u0000\u0000\u0000\u00f8\u00ee\u0001"+
		"\u0000\u0000\u0000\u00f8\u00ef\u0001\u0000\u0000\u0000\u00f8\u00f0\u0001"+
		"\u0000\u0000\u0000\u00f8\u00f4\u0001\u0000\u0000\u0000\u00f9)\u0001\u0000"+
		"\u0000\u0000\u00fa\u0103\u0005\u0005\u0000\u0000\u00fb\u0100\u00036\u001b"+
		"\u0000\u00fc\u00fd\u0005\u0003\u0000\u0000\u00fd\u00ff\u00036\u001b\u0000"+
		"\u00fe\u00fc\u0001\u0000\u0000\u0000\u00ff\u0102\u0001\u0000\u0000\u0000"+
		"\u0100\u00fe\u0001\u0000\u0000\u0000\u0100\u0101\u0001\u0000\u0000\u0000"+
		"\u0101\u0104\u0001\u0000\u0000\u0000\u0102\u0100\u0001\u0000\u0000\u0000"+
		"\u0103\u00fb\u0001\u0000\u0000\u0000\u0103\u0104\u0001\u0000\u0000\u0000"+
		"\u0104\u0105\u0001\u0000\u0000\u0000\u0105\u0106\u0005\u0006\u0000\u0000"+
		"\u0106+\u0001\u0000\u0000\u0000\u0107\u0110\u0005\r\u0000\u0000\u0108"+
		"\u010d\u00038\u001c\u0000\u0109\u010a\u0005\u0003\u0000\u0000\u010a\u010c"+
		"\u00038\u001c\u0000\u010b\u0109\u0001\u0000\u0000\u0000\u010c\u010f\u0001"+
		"\u0000\u0000\u0000\u010d\u010b\u0001\u0000\u0000\u0000\u010d\u010e\u0001"+
		"\u0000\u0000\u0000\u010e\u0111\u0001\u0000\u0000\u0000\u010f\u010d\u0001"+
		"\u0000\u0000\u0000\u0110\u0108\u0001\u0000\u0000\u0000\u0110\u0111\u0001"+
		"\u0000\u0000\u0000\u0111\u0112\u0001\u0000\u0000\u0000\u0112\u0113\u0005"+
		"\u000e\u0000\u0000\u0113-\u0001\u0000\u0000\u0000\u0114\u0115\u0005\""+
		"\u0000\u0000\u0115\u011a\u00030\u0018\u0000\u0116\u0117\u0005\u0003\u0000"+
		"\u0000\u0117\u0119\u00030\u0018\u0000\u0118\u0116\u0001\u0000\u0000\u0000"+
		"\u0119\u011c\u0001\u0000\u0000\u0000\u011a\u0118\u0001\u0000\u0000\u0000"+
		"\u011a\u011b\u0001\u0000\u0000\u0000\u011b\u011d\u0001\u0000\u0000\u0000"+
		"\u011c\u011a\u0001\u0000\u0000\u0000\u011d\u011e\u0005 \u0000\u0000\u011e"+
		"/\u0001\u0000\u0000\u0000\u011f\u0120\u00032\u0019\u0000\u0120\u0123\u0005"+
		"\n\u0000\u0000\u0121\u0124\u0003F#\u0000\u0122\u0124\u0003(\u0014\u0000"+
		"\u0123\u0121\u0001\u0000\u0000\u0000\u0123\u0122\u0001\u0000\u0000\u0000"+
		"\u01241\u0001\u0000\u0000\u0000\u0125\u0126\u0007\u0004\u0000\u0000\u0126"+
		"3\u0001\u0000\u0000\u0000\u0127\u0128\u0003\u0004\u0002\u0000\u0128\u0129"+
		"\u0005(\u0000\u0000\u0129\u0132\u0005\r\u0000\u0000\u012a\u012f\u0003"+
		"\u0004\u0002\u0000\u012b\u012c\u0005\u0003\u0000\u0000\u012c\u012e\u0003"+
		"\u0004\u0002\u0000\u012d\u012b\u0001\u0000\u0000\u0000\u012e\u0131\u0001"+
		"\u0000\u0000\u0000\u012f\u012d\u0001\u0000\u0000\u0000\u012f\u0130\u0001"+
		"\u0000\u0000\u0000\u0130\u0133\u0001\u0000\u0000\u0000\u0131\u012f\u0001"+
		"\u0000\u0000\u0000\u0132\u012a\u0001\u0000\u0000\u0000\u0132\u0133\u0001"+
		"\u0000\u0000\u0000\u0133\u0134\u0001\u0000\u0000\u0000\u0134\u0136\u0005"+
		"\u000e\u0000\u0000\u0135\u0137\u0003>\u001f\u0000\u0136\u0135\u0001\u0000"+
		"\u0000\u0000\u0136\u0137\u0001\u0000\u0000\u0000\u0137\u0138\u0001\u0000"+
		"\u0000\u0000\u0138\u0139\u0005\n\u0000\u0000\u0139\u013a\u0003<\u001e"+
		"\u0000\u013a\u013b\u0003:\u001d\u0000\u013b5\u0001\u0000\u0000\u0000\u013c"+
		"\u013d\u00034\u001a\u0000\u013d7\u0001\u0000\u0000\u0000\u013e\u013f\u0003"+
		"4\u001a\u0000\u013f9\u0001\u0000\u0000\u0000\u0140\u0141\u0005\u000f\u0000"+
		"\u0000\u0141\u0146\u0005(\u0000\u0000\u0142\u0143\u0005\u0003\u0000\u0000"+
		"\u0143\u0145\u0005(\u0000\u0000\u0144\u0142\u0001\u0000\u0000\u0000\u0145"+
		"\u0148\u0001\u0000\u0000\u0000\u0146\u0144\u0001\u0000\u0000\u0000\u0146"+
		"\u0147\u0001\u0000\u0000\u0000\u0147\u0149\u0001\u0000\u0000\u0000\u0148"+
		"\u0146\u0001\u0000\u0000\u0000\u0149\u014b\u0005\u0010\u0000\u0000\u014a"+
		"\u0140\u0001\u0000\u0000\u0000\u014a\u014b\u0001\u0000\u0000\u0000\u014b"+
		";\u0001\u0000\u0000\u0000\u014c\u0150\u0003(\u0014\u0000\u014d\u0150\u0003"+
		".\u0017\u0000\u014e\u0150\u0003F#\u0000\u014f\u014c\u0001\u0000\u0000"+
		"\u0000\u014f\u014d\u0001\u0000\u0000\u0000\u014f\u014e\u0001\u0000\u0000"+
		"\u0000\u0150=\u0001\u0000\u0000\u0000\u0151\u0152\u0005\u000f\u0000\u0000"+
		"\u0152\u0153\u0003@ \u0000\u0153\u0154\u0005\u0010\u0000\u0000\u0154\u0167"+
		"\u0001\u0000\u0000\u0000\u0155\u0156\u0005\u000f\u0000\u0000\u0156\u0157"+
		"\u0003@ \u0000\u0157\u0158\u0005\u0004\u0000\u0000\u0158\u0159\u0003B"+
		"!\u0000\u0159\u015a\u0001\u0000\u0000\u0000\u015a\u015b\u0005\u0004\u0000"+
		"\u0000\u015b\u015c\u0003D\"\u0000\u015c\u015d\u0001\u0000\u0000\u0000"+
		"\u015d\u015e\u0005\u0010\u0000\u0000\u015e\u0167\u0001\u0000\u0000\u0000"+
		"\u015f\u0160\u0005\u000f\u0000\u0000\u0160\u0161\u0003B!\u0000\u0161\u0162"+
		"\u0005\u0004\u0000\u0000\u0162\u0163\u0003D\"\u0000\u0163\u0164\u0001"+
		"\u0000\u0000\u0000\u0164\u0165\u0005\u0010\u0000\u0000\u0165\u0167\u0001"+
		"\u0000\u0000\u0000\u0166\u0151\u0001\u0000\u0000\u0000\u0166\u0155\u0001"+
		"\u0000\u0000\u0000\u0166\u015f\u0001\u0000\u0000\u0000\u0167?\u0001\u0000"+
		"\u0000\u0000\u0168\u016a\u0007\u0001\u0000\u0000\u0169\u0168\u0001\u0000"+
		"\u0000\u0000\u0169\u016a\u0001\u0000\u0000\u0000\u016a\u016b\u0001\u0000"+
		"\u0000\u0000\u016b\u016c\u0005\'\u0000\u0000\u016cA\u0001\u0000\u0000"+
		"\u0000\u016d\u0177\u0005&\u0000\u0000\u016e\u0173\u0005(\u0000\u0000\u016f"+
		"\u0170\u0005\u0003\u0000\u0000\u0170\u0172\u0005(\u0000\u0000\u0171\u016f"+
		"\u0001\u0000\u0000\u0000\u0172\u0175\u0001\u0000\u0000\u0000\u0173\u0171"+
		"\u0001\u0000\u0000\u0000\u0173\u0174\u0001\u0000\u0000\u0000\u0174\u0177"+
		"\u0001\u0000\u0000\u0000\u0175\u0173\u0001\u0000\u0000\u0000\u0176\u016d"+
		"\u0001\u0000\u0000\u0000\u0176\u016e\u0001\u0000\u0000\u0000\u0177C\u0001"+
		"\u0000\u0000\u0000\u0178\u0182\u0005&\u0000\u0000\u0179\u017e\u0005(\u0000"+
		"\u0000\u017a\u017b\u0005\u0003\u0000\u0000\u017b\u017d\u0005(\u0000\u0000"+
		"\u017c\u017a\u0001\u0000\u0000\u0000\u017d\u0180\u0001\u0000\u0000\u0000"+
		"\u017e\u017c\u0001\u0000\u0000\u0000\u017e\u017f\u0001\u0000\u0000\u0000"+
		"\u017f\u0182\u0001\u0000\u0000\u0000\u0180\u017e\u0001\u0000\u0000\u0000"+
		"\u0181\u0178\u0001\u0000\u0000\u0000\u0181\u0179\u0001\u0000\u0000\u0000"+
		"\u0182E\u0001\u0000\u0000\u0000\u0183\u0184\u0007\u0005\u0000\u0000\u0184"+
		"G\u0001\u0000\u0000\u0000%INY^clw|\u0083\u0089\u008f\u009b\u00c2\u00ca"+
		"\u00cc\u00d5\u00dd\u00df\u00f8\u0100\u0103\u010d\u0110\u011a\u0123\u012f"+
		"\u0132\u0136\u0146\u014a\u014f\u0166\u0169\u0173\u0176\u017e\u0181";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
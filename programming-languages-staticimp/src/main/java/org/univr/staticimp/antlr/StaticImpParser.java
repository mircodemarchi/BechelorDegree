// Generated from /Users/mircodemarchi/IdeaProjects/Project-20181220/Base/staticimp/src/main/resources/StaticImp.g4 by ANTLR 4.7.2
package org.univr.staticimp.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class StaticImpParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.2", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		TNAT=1, TBOOL=2, NAT=3, BOOL=4, PLUS=5, MINUS=6, MUL=7, DIV=8, MOD=9, 
		POW=10, AND=11, OR=12, EQQ=13, NEQ=14, LEQ=15, GEQ=16, LT=17, GT=18, NOT=19, 
		IF=20, THEN=21, ELSE=22, WHILE=23, SKIPP=24, ASSIGN=25, OUT=26, FOR=27, 
		DO=28, ELSEIF=29, ND=30, LPAR=31, RPAR=32, LBRACE=33, RBRACE=34, SEMICOLON=35, 
		COMMA=36, ID=37, WS=38;
	public static final int
		RULE_prog = 0, RULE_com = 1, RULE_exp = 2;
	private static String[] makeRuleNames() {
		return new String[] {
			"prog", "com", "exp"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'nat'", "'bool'", null, null, "'+'", "'-'", "'*'", "'/'", "'mod'", 
			"'^'", "'&'", "'|'", "'=='", "'!='", "'<='", "'>='", "'<'", "'>'", "'!'", 
			"'if'", "'then'", "'else'", "'while'", "'skip'", "'='", "'out'", "'for'", 
			"'do'", "'elseif'", "'nd'", "'('", "')'", "'{'", "'}'", "';'", "','"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "TNAT", "TBOOL", "NAT", "BOOL", "PLUS", "MINUS", "MUL", "DIV", 
			"MOD", "POW", "AND", "OR", "EQQ", "NEQ", "LEQ", "GEQ", "LT", "GT", "NOT", 
			"IF", "THEN", "ELSE", "WHILE", "SKIPP", "ASSIGN", "OUT", "FOR", "DO", 
			"ELSEIF", "ND", "LPAR", "RPAR", "LBRACE", "RBRACE", "SEMICOLON", "COMMA", 
			"ID", "WS"
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
	public String getGrammarFileName() { return "StaticImp.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public StaticImpParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgContext extends ParserRuleContext {
		public ComContext com() {
			return getRuleContext(ComContext.class,0);
		}
		public TerminalNode EOF() { return getToken(StaticImpParser.EOF, 0); }
		public ProgContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_prog; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitProg(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgContext prog() throws RecognitionException {
		ProgContext _localctx = new ProgContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_prog);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6);
			com(0);
			setState(7);
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

	public static class ComContext extends ParserRuleContext {
		public ComContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_com; }
	 
		public ComContext() { }
		public void copyFrom(ComContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class DowhileContext extends ComContext {
		public TerminalNode DO() { return getToken(StaticImpParser.DO, 0); }
		public TerminalNode LBRACE() { return getToken(StaticImpParser.LBRACE, 0); }
		public ComContext com() {
			return getRuleContext(ComContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(StaticImpParser.RBRACE, 0); }
		public TerminalNode WHILE() { return getToken(StaticImpParser.WHILE, 0); }
		public TerminalNode LPAR() { return getToken(StaticImpParser.LPAR, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(StaticImpParser.RPAR, 0); }
		public DowhileContext(ComContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitDowhile(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DeclContext extends ComContext {
		public Token type;
		public TerminalNode ID() { return getToken(StaticImpParser.ID, 0); }
		public TerminalNode TNAT() { return getToken(StaticImpParser.TNAT, 0); }
		public TerminalNode TBOOL() { return getToken(StaticImpParser.TBOOL, 0); }
		public DeclContext(ComContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitDecl(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NdContext extends ComContext {
		public TerminalNode ND() { return getToken(StaticImpParser.ND, 0); }
		public TerminalNode LPAR() { return getToken(StaticImpParser.LPAR, 0); }
		public List<ComContext> com() {
			return getRuleContexts(ComContext.class);
		}
		public ComContext com(int i) {
			return getRuleContext(ComContext.class,i);
		}
		public TerminalNode COMMA() { return getToken(StaticImpParser.COMMA, 0); }
		public TerminalNode RPAR() { return getToken(StaticImpParser.RPAR, 0); }
		public NdContext(ComContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitNd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ForContext extends ComContext {
		public TerminalNode FOR() { return getToken(StaticImpParser.FOR, 0); }
		public TerminalNode LPAR() { return getToken(StaticImpParser.LPAR, 0); }
		public TerminalNode ID() { return getToken(StaticImpParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(StaticImpParser.ASSIGN, 0); }
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public List<TerminalNode> SEMICOLON() { return getTokens(StaticImpParser.SEMICOLON); }
		public TerminalNode SEMICOLON(int i) {
			return getToken(StaticImpParser.SEMICOLON, i);
		}
		public List<ComContext> com() {
			return getRuleContexts(ComContext.class);
		}
		public ComContext com(int i) {
			return getRuleContext(ComContext.class,i);
		}
		public TerminalNode RPAR() { return getToken(StaticImpParser.RPAR, 0); }
		public TerminalNode LBRACE() { return getToken(StaticImpParser.LBRACE, 0); }
		public TerminalNode RBRACE() { return getToken(StaticImpParser.RBRACE, 0); }
		public ForContext(ComContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitFor(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SkipContext extends ComContext {
		public TerminalNode SKIPP() { return getToken(StaticImpParser.SKIPP, 0); }
		public SkipContext(ComContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitSkip(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class WhileContext extends ComContext {
		public TerminalNode WHILE() { return getToken(StaticImpParser.WHILE, 0); }
		public TerminalNode LPAR() { return getToken(StaticImpParser.LPAR, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(StaticImpParser.RPAR, 0); }
		public TerminalNode LBRACE() { return getToken(StaticImpParser.LBRACE, 0); }
		public ComContext com() {
			return getRuleContext(ComContext.class,0);
		}
		public TerminalNode RBRACE() { return getToken(StaticImpParser.RBRACE, 0); }
		public WhileContext(ComContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitWhile(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IfContext extends ComContext {
		public TerminalNode IF() { return getToken(StaticImpParser.IF, 0); }
		public List<TerminalNode> LPAR() { return getTokens(StaticImpParser.LPAR); }
		public TerminalNode LPAR(int i) {
			return getToken(StaticImpParser.LPAR, i);
		}
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public List<TerminalNode> RPAR() { return getTokens(StaticImpParser.RPAR); }
		public TerminalNode RPAR(int i) {
			return getToken(StaticImpParser.RPAR, i);
		}
		public TerminalNode THEN() { return getToken(StaticImpParser.THEN, 0); }
		public List<TerminalNode> LBRACE() { return getTokens(StaticImpParser.LBRACE); }
		public TerminalNode LBRACE(int i) {
			return getToken(StaticImpParser.LBRACE, i);
		}
		public List<ComContext> com() {
			return getRuleContexts(ComContext.class);
		}
		public ComContext com(int i) {
			return getRuleContext(ComContext.class,i);
		}
		public List<TerminalNode> RBRACE() { return getTokens(StaticImpParser.RBRACE); }
		public TerminalNode RBRACE(int i) {
			return getToken(StaticImpParser.RBRACE, i);
		}
		public List<TerminalNode> ELSEIF() { return getTokens(StaticImpParser.ELSEIF); }
		public TerminalNode ELSEIF(int i) {
			return getToken(StaticImpParser.ELSEIF, i);
		}
		public TerminalNode ELSE() { return getToken(StaticImpParser.ELSE, 0); }
		public IfContext(ComContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitIf(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class SeqContext extends ComContext {
		public List<ComContext> com() {
			return getRuleContexts(ComContext.class);
		}
		public ComContext com(int i) {
			return getRuleContext(ComContext.class,i);
		}
		public TerminalNode SEMICOLON() { return getToken(StaticImpParser.SEMICOLON, 0); }
		public SeqContext(ComContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitSeq(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AssignContext extends ComContext {
		public TerminalNode ID() { return getToken(StaticImpParser.ID, 0); }
		public TerminalNode ASSIGN() { return getToken(StaticImpParser.ASSIGN, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public AssignContext(ComContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitAssign(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OutContext extends ComContext {
		public TerminalNode OUT() { return getToken(StaticImpParser.OUT, 0); }
		public TerminalNode LPAR() { return getToken(StaticImpParser.LPAR, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(StaticImpParser.RPAR, 0); }
		public OutContext(ComContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitOut(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ComContext com() throws RecognitionException {
		return com(0);
	}

	private ComContext com(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ComContext _localctx = new ComContext(_ctx, _parentState);
		ComContext _prevctx = _localctx;
		int _startState = 2;
		enterRecursionRule(_localctx, 2, RULE_com, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(87);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case SKIPP:
				{
				_localctx = new SkipContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(10);
				match(SKIPP);
				}
				break;
			case TNAT:
			case TBOOL:
				{
				_localctx = new DeclContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(11);
				((DeclContext)_localctx).type = _input.LT(1);
				_la = _input.LA(1);
				if ( !(_la==TNAT || _la==TBOOL) ) {
					((DeclContext)_localctx).type = (Token)_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(12);
				match(ID);
				}
				break;
			case ID:
				{
				_localctx = new AssignContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(13);
				match(ID);
				setState(14);
				match(ASSIGN);
				setState(15);
				exp(0);
				}
				break;
			case OUT:
				{
				_localctx = new OutContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(16);
				match(OUT);
				setState(17);
				match(LPAR);
				setState(18);
				exp(0);
				setState(19);
				match(RPAR);
				}
				break;
			case WHILE:
				{
				_localctx = new WhileContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(21);
				match(WHILE);
				setState(22);
				match(LPAR);
				setState(23);
				exp(0);
				setState(24);
				match(RPAR);
				setState(25);
				match(LBRACE);
				setState(26);
				com(0);
				setState(27);
				match(RBRACE);
				}
				break;
			case IF:
				{
				_localctx = new IfContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(29);
				match(IF);
				setState(30);
				match(LPAR);
				setState(31);
				exp(0);
				setState(32);
				match(RPAR);
				setState(33);
				match(THEN);
				setState(34);
				match(LBRACE);
				setState(35);
				com(0);
				setState(36);
				match(RBRACE);
				setState(47);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(37);
						match(ELSEIF);
						setState(38);
						match(LPAR);
						setState(39);
						exp(0);
						setState(40);
						match(RPAR);
						setState(41);
						match(LBRACE);
						setState(42);
						com(0);
						setState(43);
						match(RBRACE);
						}
						} 
					}
					setState(49);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
				}
				setState(55);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
				case 1:
					{
					setState(50);
					match(ELSE);
					setState(51);
					match(LBRACE);
					setState(52);
					com(0);
					setState(53);
					match(RBRACE);
					}
					break;
				}
				}
				break;
			case FOR:
				{
				_localctx = new ForContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(57);
				match(FOR);
				setState(58);
				match(LPAR);
				setState(59);
				match(ID);
				setState(60);
				match(ASSIGN);
				setState(61);
				exp(0);
				setState(62);
				match(SEMICOLON);
				setState(63);
				exp(0);
				setState(64);
				match(SEMICOLON);
				setState(65);
				com(0);
				setState(66);
				match(RPAR);
				setState(67);
				match(LBRACE);
				setState(68);
				com(0);
				setState(69);
				match(RBRACE);
				}
				break;
			case DO:
				{
				_localctx = new DowhileContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(71);
				match(DO);
				setState(72);
				match(LBRACE);
				setState(73);
				com(0);
				setState(74);
				match(RBRACE);
				setState(75);
				match(WHILE);
				setState(76);
				match(LPAR);
				setState(77);
				exp(0);
				setState(78);
				match(RPAR);
				}
				break;
			case ND:
				{
				_localctx = new NdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(80);
				match(ND);
				setState(81);
				match(LPAR);
				setState(82);
				com(0);
				setState(83);
				match(COMMA);
				setState(84);
				com(0);
				setState(85);
				match(RPAR);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(94);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new SeqContext(new ComContext(_parentctx, _parentState));
					pushNewRecursionContext(_localctx, _startState, RULE_com);
					setState(89);
					if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
					setState(90);
					match(SEMICOLON);
					setState(91);
					com(6);
					}
					} 
				}
				setState(96);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
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

	public static class ExpContext extends ParserRuleContext {
		public ExpContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_exp; }
	 
		public ExpContext() { }
		public void copyFrom(ExpContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class NatContext extends ExpContext {
		public TerminalNode NAT() { return getToken(StaticImpParser.NAT, 0); }
		public NatContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitNat(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class DivMulModContext extends ExpContext {
		public Token op;
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode DIV() { return getToken(StaticImpParser.DIV, 0); }
		public TerminalNode MUL() { return getToken(StaticImpParser.MUL, 0); }
		public TerminalNode MOD() { return getToken(StaticImpParser.MOD, 0); }
		public DivMulModContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitDivMulMod(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class NotContext extends ExpContext {
		public TerminalNode NOT() { return getToken(StaticImpParser.NOT, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public NotContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitNot(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PlusMinusContext extends ExpContext {
		public Token op;
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(StaticImpParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(StaticImpParser.MINUS, 0); }
		public PlusMinusContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitPlusMinus(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class EqExpContext extends ExpContext {
		public Token op;
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode EQQ() { return getToken(StaticImpParser.EQQ, 0); }
		public TerminalNode NEQ() { return getToken(StaticImpParser.NEQ, 0); }
		public EqExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitEqExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class BoolContext extends ExpContext {
		public TerminalNode BOOL() { return getToken(StaticImpParser.BOOL, 0); }
		public BoolContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitBool(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CmpExpContext extends ExpContext {
		public Token op;
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode LT() { return getToken(StaticImpParser.LT, 0); }
		public TerminalNode LEQ() { return getToken(StaticImpParser.LEQ, 0); }
		public TerminalNode GEQ() { return getToken(StaticImpParser.GEQ, 0); }
		public TerminalNode GT() { return getToken(StaticImpParser.GT, 0); }
		public CmpExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitCmpExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class LogicExpContext extends ExpContext {
		public Token op;
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode AND() { return getToken(StaticImpParser.AND, 0); }
		public TerminalNode OR() { return getToken(StaticImpParser.OR, 0); }
		public LogicExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitLogicExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParExpContext extends ExpContext {
		public TerminalNode LPAR() { return getToken(StaticImpParser.LPAR, 0); }
		public ExpContext exp() {
			return getRuleContext(ExpContext.class,0);
		}
		public TerminalNode RPAR() { return getToken(StaticImpParser.RPAR, 0); }
		public ParExpContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitParExp(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class PowContext extends ExpContext {
		public List<ExpContext> exp() {
			return getRuleContexts(ExpContext.class);
		}
		public ExpContext exp(int i) {
			return getRuleContext(ExpContext.class,i);
		}
		public TerminalNode POW() { return getToken(StaticImpParser.POW, 0); }
		public PowContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitPow(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class IdContext extends ExpContext {
		public TerminalNode ID() { return getToken(StaticImpParser.ID, 0); }
		public IdContext(ExpContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof StaticImpVisitor ) return ((StaticImpVisitor<? extends T>)visitor).visitId(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpContext exp() throws RecognitionException {
		return exp(0);
	}

	private ExpContext exp(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpContext _localctx = new ExpContext(_ctx, _parentState);
		ExpContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_exp, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case NAT:
				{
				_localctx = new NatContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(98);
				match(NAT);
				}
				break;
			case BOOL:
				{
				_localctx = new BoolContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(99);
				match(BOOL);
				}
				break;
			case LPAR:
				{
				_localctx = new ParExpContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(100);
				match(LPAR);
				setState(101);
				exp(0);
				setState(102);
				match(RPAR);
				}
				break;
			case NOT:
				{
				_localctx = new NotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(104);
				match(NOT);
				setState(105);
				exp(8);
				}
				break;
			case ID:
				{
				_localctx = new IdContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(106);
				match(ID);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(129);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(127);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
					case 1:
						{
						_localctx = new PowContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(109);
						if (!(precpred(_ctx, 7))) throw new FailedPredicateException(this, "precpred(_ctx, 7)");
						setState(110);
						match(POW);
						setState(111);
						exp(7);
						}
						break;
					case 2:
						{
						_localctx = new DivMulModContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(112);
						if (!(precpred(_ctx, 6))) throw new FailedPredicateException(this, "precpred(_ctx, 6)");
						setState(113);
						((DivMulModContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MOD))) != 0)) ) {
							((DivMulModContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(114);
						exp(7);
						}
						break;
					case 3:
						{
						_localctx = new PlusMinusContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(115);
						if (!(precpred(_ctx, 5))) throw new FailedPredicateException(this, "precpred(_ctx, 5)");
						setState(116);
						((PlusMinusContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
							((PlusMinusContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(117);
						exp(6);
						}
						break;
					case 4:
						{
						_localctx = new CmpExpContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(118);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(119);
						((CmpExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LEQ) | (1L << GEQ) | (1L << LT) | (1L << GT))) != 0)) ) {
							((CmpExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(120);
						exp(5);
						}
						break;
					case 5:
						{
						_localctx = new EqExpContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(121);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(122);
						((EqExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==EQQ || _la==NEQ) ) {
							((EqExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(123);
						exp(4);
						}
						break;
					case 6:
						{
						_localctx = new LogicExpContext(new ExpContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_exp);
						setState(124);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(125);
						((LogicExpContext)_localctx).op = _input.LT(1);
						_la = _input.LA(1);
						if ( !(_la==AND || _la==OR) ) {
							((LogicExpContext)_localctx).op = (Token)_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(126);
						exp(3);
						}
						break;
					}
					} 
				}
				setState(131);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,6,_ctx);
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 1:
			return com_sempred((ComContext)_localctx, predIndex);
		case 2:
			return exp_sempred((ExpContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean com_sempred(ComContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 5);
		}
		return true;
	}
	private boolean exp_sempred(ExpContext _localctx, int predIndex) {
		switch (predIndex) {
		case 1:
			return precpred(_ctx, 7);
		case 2:
			return precpred(_ctx, 6);
		case 3:
			return precpred(_ctx, 5);
		case 4:
			return precpred(_ctx, 4);
		case 5:
			return precpred(_ctx, 3);
		case 6:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3(\u0087\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\7\3\60\n\3\f\3\16\3\63\13\3\3\3\3\3\3\3"+
		"\3\3\3\3\5\3:\n\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3"+
		"\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\5"+
		"\3Z\n\3\3\3\3\3\3\3\7\3_\n\3\f\3\16\3b\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\5\4n\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\4\7\4\u0082\n\4\f\4\16\4\u0085\13\4\3\4\2\4\4"+
		"\6\5\2\4\6\2\b\3\2\3\4\3\2\t\13\3\2\7\b\3\2\21\24\3\2\17\20\3\2\r\16\2"+
		"\u0098\2\b\3\2\2\2\4Y\3\2\2\2\6m\3\2\2\2\b\t\5\4\3\2\t\n\7\2\2\3\n\3\3"+
		"\2\2\2\13\f\b\3\1\2\fZ\7\32\2\2\r\16\t\2\2\2\16Z\7\'\2\2\17\20\7\'\2\2"+
		"\20\21\7\33\2\2\21Z\5\6\4\2\22\23\7\34\2\2\23\24\7!\2\2\24\25\5\6\4\2"+
		"\25\26\7\"\2\2\26Z\3\2\2\2\27\30\7\31\2\2\30\31\7!\2\2\31\32\5\6\4\2\32"+
		"\33\7\"\2\2\33\34\7#\2\2\34\35\5\4\3\2\35\36\7$\2\2\36Z\3\2\2\2\37 \7"+
		"\26\2\2 !\7!\2\2!\"\5\6\4\2\"#\7\"\2\2#$\7\27\2\2$%\7#\2\2%&\5\4\3\2&"+
		"\61\7$\2\2\'(\7\37\2\2()\7!\2\2)*\5\6\4\2*+\7\"\2\2+,\7#\2\2,-\5\4\3\2"+
		"-.\7$\2\2.\60\3\2\2\2/\'\3\2\2\2\60\63\3\2\2\2\61/\3\2\2\2\61\62\3\2\2"+
		"\2\629\3\2\2\2\63\61\3\2\2\2\64\65\7\30\2\2\65\66\7#\2\2\66\67\5\4\3\2"+
		"\678\7$\2\28:\3\2\2\29\64\3\2\2\29:\3\2\2\2:Z\3\2\2\2;<\7\35\2\2<=\7!"+
		"\2\2=>\7\'\2\2>?\7\33\2\2?@\5\6\4\2@A\7%\2\2AB\5\6\4\2BC\7%\2\2CD\5\4"+
		"\3\2DE\7\"\2\2EF\7#\2\2FG\5\4\3\2GH\7$\2\2HZ\3\2\2\2IJ\7\36\2\2JK\7#\2"+
		"\2KL\5\4\3\2LM\7$\2\2MN\7\31\2\2NO\7!\2\2OP\5\6\4\2PQ\7\"\2\2QZ\3\2\2"+
		"\2RS\7 \2\2ST\7!\2\2TU\5\4\3\2UV\7&\2\2VW\5\4\3\2WX\7\"\2\2XZ\3\2\2\2"+
		"Y\13\3\2\2\2Y\r\3\2\2\2Y\17\3\2\2\2Y\22\3\2\2\2Y\27\3\2\2\2Y\37\3\2\2"+
		"\2Y;\3\2\2\2YI\3\2\2\2YR\3\2\2\2Z`\3\2\2\2[\\\f\7\2\2\\]\7%\2\2]_\5\4"+
		"\3\b^[\3\2\2\2_b\3\2\2\2`^\3\2\2\2`a\3\2\2\2a\5\3\2\2\2b`\3\2\2\2cd\b"+
		"\4\1\2dn\7\5\2\2en\7\6\2\2fg\7!\2\2gh\5\6\4\2hi\7\"\2\2in\3\2\2\2jk\7"+
		"\25\2\2kn\5\6\4\nln\7\'\2\2mc\3\2\2\2me\3\2\2\2mf\3\2\2\2mj\3\2\2\2ml"+
		"\3\2\2\2n\u0083\3\2\2\2op\f\t\2\2pq\7\f\2\2q\u0082\5\6\4\trs\f\b\2\2s"+
		"t\t\3\2\2t\u0082\5\6\4\tuv\f\7\2\2vw\t\4\2\2w\u0082\5\6\4\bxy\f\6\2\2"+
		"yz\t\5\2\2z\u0082\5\6\4\7{|\f\5\2\2|}\t\6\2\2}\u0082\5\6\4\6~\177\f\4"+
		"\2\2\177\u0080\t\7\2\2\u0080\u0082\5\6\4\5\u0081o\3\2\2\2\u0081r\3\2\2"+
		"\2\u0081u\3\2\2\2\u0081x\3\2\2\2\u0081{\3\2\2\2\u0081~\3\2\2\2\u0082\u0085"+
		"\3\2\2\2\u0083\u0081\3\2\2\2\u0083\u0084\3\2\2\2\u0084\7\3\2\2\2\u0085"+
		"\u0083\3\2\2\2\t\619Y`m\u0081\u0083";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
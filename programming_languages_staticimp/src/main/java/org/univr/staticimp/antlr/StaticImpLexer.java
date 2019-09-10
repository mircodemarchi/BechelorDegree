// Generated from /Users/mircodemarchi/IdeaProjects/Project-20181220/Base/staticimp/src/main/resources/StaticImp.g4 by ANTLR 4.7.2
package org.univr.staticimp.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class StaticImpLexer extends Lexer {
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
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"TNAT", "TBOOL", "NAT", "BOOL", "PLUS", "MINUS", "MUL", "DIV", "MOD", 
			"POW", "AND", "OR", "EQQ", "NEQ", "LEQ", "GEQ", "LT", "GT", "NOT", "IF", 
			"THEN", "ELSE", "WHILE", "SKIPP", "ASSIGN", "OUT", "FOR", "DO", "ELSEIF", 
			"ND", "LPAR", "RPAR", "LBRACE", "RBRACE", "SEMICOLON", "COMMA", "ID", 
			"WS"
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


	public StaticImpLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "StaticImp.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2(\u00d8\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\3\2\3\2\3\2\3\2\3\3\3\3\3"+
		"\3\3\3\3\3\3\4\3\4\3\4\7\4\\\n\4\f\4\16\4_\13\4\5\4a\n\4\3\5\3\5\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\5\5\5l\n\5\3\6\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3"+
		"\n\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20"+
		"\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\3\25"+
		"\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\3\30\3\30\3\31\3\31\3\31\3\31\3\31\3\32\3\32\3\33\3\33\3\33\3\33\3\34"+
		"\3\34\3\34\3\34\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\36\3\36\3\36\3\37"+
		"\3\37\3\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\6&\u00ce\n&\r&\16"+
		"&\u00cf\3\'\6\'\u00d3\n\'\r\'\16\'\u00d4\3\'\3\'\2\2(\3\3\5\4\7\5\t\6"+
		"\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24"+
		"\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!A\"C#E$G%I&K"+
		"\'M(\3\2\6\3\2\63;\3\2\62;\3\2c|\5\2\13\f\17\17\"\"\2\u00dc\2\3\3\2\2"+
		"\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3"+
		"\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2"+
		"\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2"+
		"\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2"+
		"\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3"+
		"\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2"+
		"\2\2K\3\2\2\2\2M\3\2\2\2\3O\3\2\2\2\5S\3\2\2\2\7`\3\2\2\2\tk\3\2\2\2\13"+
		"m\3\2\2\2\ro\3\2\2\2\17q\3\2\2\2\21s\3\2\2\2\23u\3\2\2\2\25y\3\2\2\2\27"+
		"{\3\2\2\2\31}\3\2\2\2\33\177\3\2\2\2\35\u0082\3\2\2\2\37\u0085\3\2\2\2"+
		"!\u0088\3\2\2\2#\u008b\3\2\2\2%\u008d\3\2\2\2\'\u008f\3\2\2\2)\u0091\3"+
		"\2\2\2+\u0094\3\2\2\2-\u0099\3\2\2\2/\u009e\3\2\2\2\61\u00a4\3\2\2\2\63"+
		"\u00a9\3\2\2\2\65\u00ab\3\2\2\2\67\u00af\3\2\2\29\u00b3\3\2\2\2;\u00b6"+
		"\3\2\2\2=\u00bd\3\2\2\2?\u00c0\3\2\2\2A\u00c2\3\2\2\2C\u00c4\3\2\2\2E"+
		"\u00c6\3\2\2\2G\u00c8\3\2\2\2I\u00ca\3\2\2\2K\u00cd\3\2\2\2M\u00d2\3\2"+
		"\2\2OP\7p\2\2PQ\7c\2\2QR\7v\2\2R\4\3\2\2\2ST\7d\2\2TU\7q\2\2UV\7q\2\2"+
		"VW\7n\2\2W\6\3\2\2\2Xa\7\62\2\2Y]\t\2\2\2Z\\\t\3\2\2[Z\3\2\2\2\\_\3\2"+
		"\2\2][\3\2\2\2]^\3\2\2\2^a\3\2\2\2_]\3\2\2\2`X\3\2\2\2`Y\3\2\2\2a\b\3"+
		"\2\2\2bc\7v\2\2cd\7t\2\2de\7w\2\2el\7g\2\2fg\7h\2\2gh\7c\2\2hi\7n\2\2"+
		"ij\7u\2\2jl\7g\2\2kb\3\2\2\2kf\3\2\2\2l\n\3\2\2\2mn\7-\2\2n\f\3\2\2\2"+
		"op\7/\2\2p\16\3\2\2\2qr\7,\2\2r\20\3\2\2\2st\7\61\2\2t\22\3\2\2\2uv\7"+
		"o\2\2vw\7q\2\2wx\7f\2\2x\24\3\2\2\2yz\7`\2\2z\26\3\2\2\2{|\7(\2\2|\30"+
		"\3\2\2\2}~\7~\2\2~\32\3\2\2\2\177\u0080\7?\2\2\u0080\u0081\7?\2\2\u0081"+
		"\34\3\2\2\2\u0082\u0083\7#\2\2\u0083\u0084\7?\2\2\u0084\36\3\2\2\2\u0085"+
		"\u0086\7>\2\2\u0086\u0087\7?\2\2\u0087 \3\2\2\2\u0088\u0089\7@\2\2\u0089"+
		"\u008a\7?\2\2\u008a\"\3\2\2\2\u008b\u008c\7>\2\2\u008c$\3\2\2\2\u008d"+
		"\u008e\7@\2\2\u008e&\3\2\2\2\u008f\u0090\7#\2\2\u0090(\3\2\2\2\u0091\u0092"+
		"\7k\2\2\u0092\u0093\7h\2\2\u0093*\3\2\2\2\u0094\u0095\7v\2\2\u0095\u0096"+
		"\7j\2\2\u0096\u0097\7g\2\2\u0097\u0098\7p\2\2\u0098,\3\2\2\2\u0099\u009a"+
		"\7g\2\2\u009a\u009b\7n\2\2\u009b\u009c\7u\2\2\u009c\u009d\7g\2\2\u009d"+
		".\3\2\2\2\u009e\u009f\7y\2\2\u009f\u00a0\7j\2\2\u00a0\u00a1\7k\2\2\u00a1"+
		"\u00a2\7n\2\2\u00a2\u00a3\7g\2\2\u00a3\60\3\2\2\2\u00a4\u00a5\7u\2\2\u00a5"+
		"\u00a6\7m\2\2\u00a6\u00a7\7k\2\2\u00a7\u00a8\7r\2\2\u00a8\62\3\2\2\2\u00a9"+
		"\u00aa\7?\2\2\u00aa\64\3\2\2\2\u00ab\u00ac\7q\2\2\u00ac\u00ad\7w\2\2\u00ad"+
		"\u00ae\7v\2\2\u00ae\66\3\2\2\2\u00af\u00b0\7h\2\2\u00b0\u00b1\7q\2\2\u00b1"+
		"\u00b2\7t\2\2\u00b28\3\2\2\2\u00b3\u00b4\7f\2\2\u00b4\u00b5\7q\2\2\u00b5"+
		":\3\2\2\2\u00b6\u00b7\7g\2\2\u00b7\u00b8\7n\2\2\u00b8\u00b9\7u\2\2\u00b9"+
		"\u00ba\7g\2\2\u00ba\u00bb\7k\2\2\u00bb\u00bc\7h\2\2\u00bc<\3\2\2\2\u00bd"+
		"\u00be\7p\2\2\u00be\u00bf\7f\2\2\u00bf>\3\2\2\2\u00c0\u00c1\7*\2\2\u00c1"+
		"@\3\2\2\2\u00c2\u00c3\7+\2\2\u00c3B\3\2\2\2\u00c4\u00c5\7}\2\2\u00c5D"+
		"\3\2\2\2\u00c6\u00c7\7\177\2\2\u00c7F\3\2\2\2\u00c8\u00c9\7=\2\2\u00c9"+
		"H\3\2\2\2\u00ca\u00cb\7.\2\2\u00cbJ\3\2\2\2\u00cc\u00ce\t\4\2\2\u00cd"+
		"\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00cd\3\2\2\2\u00cf\u00d0\3\2"+
		"\2\2\u00d0L\3\2\2\2\u00d1\u00d3\t\5\2\2\u00d2\u00d1\3\2\2\2\u00d3\u00d4"+
		"\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6"+
		"\u00d7\b\'\2\2\u00d7N\3\2\2\2\b\2]`k\u00cf\u00d4\3\b\2\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}
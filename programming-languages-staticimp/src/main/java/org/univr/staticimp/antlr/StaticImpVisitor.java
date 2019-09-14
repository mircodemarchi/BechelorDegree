// Generated from /Users/mircodemarchi/IdeaProjects/Project-20181220/Base/staticimp/src/main/resources/StaticImp.g4 by ANTLR 4.7.2
package org.univr.staticimp.antlr;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link StaticImpParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface StaticImpVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link StaticImpParser#prog}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitProg(StaticImpParser.ProgContext ctx);
	/**
	 * Visit a parse tree produced by the {@code dowhile}
	 * labeled alternative in {@link StaticImpParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDowhile(StaticImpParser.DowhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code decl}
	 * labeled alternative in {@link StaticImpParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDecl(StaticImpParser.DeclContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nd}
	 * labeled alternative in {@link StaticImpParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNd(StaticImpParser.NdContext ctx);
	/**
	 * Visit a parse tree produced by the {@code for}
	 * labeled alternative in {@link StaticImpParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFor(StaticImpParser.ForContext ctx);
	/**
	 * Visit a parse tree produced by the {@code skip}
	 * labeled alternative in {@link StaticImpParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSkip(StaticImpParser.SkipContext ctx);
	/**
	 * Visit a parse tree produced by the {@code while}
	 * labeled alternative in {@link StaticImpParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitWhile(StaticImpParser.WhileContext ctx);
	/**
	 * Visit a parse tree produced by the {@code if}
	 * labeled alternative in {@link StaticImpParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIf(StaticImpParser.IfContext ctx);
	/**
	 * Visit a parse tree produced by the {@code seq}
	 * labeled alternative in {@link StaticImpParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeq(StaticImpParser.SeqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assign}
	 * labeled alternative in {@link StaticImpParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign(StaticImpParser.AssignContext ctx);
	/**
	 * Visit a parse tree produced by the {@code out}
	 * labeled alternative in {@link StaticImpParser#com}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOut(StaticImpParser.OutContext ctx);
	/**
	 * Visit a parse tree produced by the {@code nat}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNat(StaticImpParser.NatContext ctx);
	/**
	 * Visit a parse tree produced by the {@code divMulMod}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDivMulMod(StaticImpParser.DivMulModContext ctx);
	/**
	 * Visit a parse tree produced by the {@code not}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot(StaticImpParser.NotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code plusMinus}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPlusMinus(StaticImpParser.PlusMinusContext ctx);
	/**
	 * Visit a parse tree produced by the {@code eqExp}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEqExp(StaticImpParser.EqExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code bool}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBool(StaticImpParser.BoolContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cmpExp}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCmpExp(StaticImpParser.CmpExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code logicExp}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLogicExp(StaticImpParser.LogicExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code parExp}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParExp(StaticImpParser.ParExpContext ctx);
	/**
	 * Visit a parse tree produced by the {@code pow}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPow(StaticImpParser.PowContext ctx);
	/**
	 * Visit a parse tree produced by the {@code id}
	 * labeled alternative in {@link StaticImpParser#exp}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitId(StaticImpParser.IdContext ctx);
}
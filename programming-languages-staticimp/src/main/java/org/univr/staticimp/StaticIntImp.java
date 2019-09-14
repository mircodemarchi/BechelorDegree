package org.univr.staticimp;

import org.univr.staticimp.antlr.StaticImpBaseVisitor;
import org.univr.staticimp.antlr.StaticImpParser;
import org.univr.staticimp.type.*;

public class StaticIntImp extends StaticImpBaseVisitor<Type> {

    private final StaticConf conf;

    public StaticIntImp(StaticConf conf) {
        this.conf = conf;
    }

    @Override
    public ComType visitProg(StaticImpParser.ProgContext ctx) {
        return visitCom(ctx.com());
    }

    @Override
    //======================================== StaticSemantics: DOWHILE ================================================
    public ComType visitDowhile(StaticImpParser.DowhileContext ctx) {
        // Check for exp and com well-typed, and exp of boolean-type.
        visitBoolExp(ctx.exp());
        visitCom(ctx.com());

        // "do { com } while ( exp )" is a com.
        return ComType.INSTANCE;
    }

    private NaturalType visitNatExp(StaticImpParser.ExpContext ctx) throws StaticSemanticsException {
        try {
            return (NaturalType) visitExp(ctx);
        }
        catch (ClassCastException e) {
            System.err.println("Type mismatch in the expression:");
            System.err.println();
            System.err.println(ctx.getText());
            System.err.println();
            System.err.println("@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());
            System.err.println("> Numerical expression expected");

            throw new StaticSemanticsException();
        }
    }

    private BooleanType visitBoolExp(StaticImpParser.ExpContext ctx) throws StaticSemanticsException {
        try {
            return (BooleanType) visitExp(ctx);
        }
        catch (ClassCastException e) {
            System.err.println("Type mismatch in the expression:");
            System.err.println();
            System.err.println(ctx.getText());
            System.err.println();
            System.err.println("@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());
            System.err.println("> Boolean expression expected");

            throw new StaticSemanticsException();
        }
    }

    private ExpType visitExp(StaticImpParser.ExpContext ctx) {
        return (ExpType) visit(ctx);
    }

    private ComType visitCom(StaticImpParser.ComContext ctx) {
        return (ComType) visit(ctx);
    }

    @Override
    public ComType visitSkip(StaticImpParser.SkipContext ctx) {
        return ComType.INSTANCE;
    }

    @Override
    public ComType visitDecl(StaticImpParser.DeclContext ctx) {
        String id = ctx.ID().getText();

        if (conf.get(id) != null) {
            System.err.println("Variable " + id + " has already been declared with type " + conf.get(id).toString());
            System.err.println("@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());

            throw new StaticSemanticsException();
        }

        switch (ctx.type.getType()) {
            case StaticImpParser.TNAT  : conf.put(id, NaturalType.INSTANCE); break;
            case StaticImpParser.TBOOL : conf.put(id, BooleanType.INSTANCE);
        }

        return ComType.INSTANCE;
    }

    @Override
    //======================================== StaticSemantics: ND =====================================================
    public ComType visitNd(StaticImpParser.NdContext ctx) {
        // Check for commands well-typed.
        visitCom(ctx.com(0));
        visitCom(ctx.com(1));

        // "nd ( com , com )" is a com.
        return ComType.INSTANCE;
    }

    @Override
    //======================================== StaticSemantics: FOR ====================================================
    public ComType visitFor(StaticImpParser.ForContext ctx) {
        // Check for "ID ASSIGN exp" well-typed.
        // ID must already be declared and exp has to be ID's same type.
        // The same code of visitAssign().
        String id = ctx.ID().getText();
        ExpType t = conf.get(id);

        if (t == null) {
            System.err.println("Variable " + id + " assigned but never declared");
            System.err.println("@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());

            throw new StaticSemanticsException();
        }

        if (t.equals(NaturalType.INSTANCE))
            visitNatExp(ctx.exp(0));
        else
            visitBoolExp(ctx.exp(0));

        // Check for exp well-typed and boolean.
        visitBoolExp(ctx.exp(1));

        // Check for com(0) and com(1) well-typed.
        visitCom(ctx.com(0));
        visitCom(ctx.com(1));

        // "for ( assign ; exp ; com ) { com }" is a com
        return ComType.INSTANCE;
    }

    @Override
    public ComType visitAssign(StaticImpParser.AssignContext ctx) {
        String id = ctx.ID().getText();
        ExpType t = conf.get(id);

        if (t == null) {
            System.err.println("Variable " + id + " assigned but never declared");
            System.err.println("@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());

            throw new StaticSemanticsException();
        }

        if (t.equals(NaturalType.INSTANCE))
            visitNatExp(ctx.exp());
        else
            visitBoolExp(ctx.exp());

        return ComType.INSTANCE;
    }

    @Override
    public ComType visitOut(StaticImpParser.OutContext ctx) {
        visit(ctx.exp());
        return ComType.INSTANCE;
    }

    @Override
    public ComType visitWhile(StaticImpParser.WhileContext ctx) {
        visitBoolExp(ctx.exp());
        visit(ctx.com());

        return ComType.INSTANCE;
    }

    @Override
    public ComType visitSeq(StaticImpParser.SeqContext ctx) {
        visit(ctx.com(0));
        visit(ctx.com(1));

        return ComType.INSTANCE;
    }

    @Override
    //======================================== StaticSemantics: IF =====================================================
    public ComType visitIf(StaticImpParser.IfContext ctx) {
        // Check all expressions well-typed
        for(StaticImpParser.ExpContext exp: ctx.exp()){
            visitBoolExp(exp);
        }

        // Check all commands well-typed
        for(StaticImpParser.ComContext com: ctx.com()) {
            visitCom(com);
        }

        // "if ( exp ) then { com } [elseif ( exp ) { com }]* [else ( exp ) { com }]?" is a com
        return ComType.INSTANCE;
    }

    @Override
    public NaturalType visitNat(StaticImpParser.NatContext ctx) {
        return NaturalType.INSTANCE;
    }

    @Override
    public BooleanType visitBool(StaticImpParser.BoolContext ctx) {
        return BooleanType.INSTANCE;
    }

    @Override
    public ExpType visitParExp(StaticImpParser.ParExpContext ctx) {
        return visitExp(ctx.exp());
    }

    @Override
    public BooleanType visitNot(StaticImpParser.NotContext ctx) {
        visitBoolExp(ctx.exp());
        return BooleanType.INSTANCE;
    }

    @Override
    public NaturalType visitPow(StaticImpParser.PowContext ctx) {
        visitNatExp(ctx.exp(0));
        visitNatExp(ctx.exp(1));

        return NaturalType.INSTANCE;
    }

    @Override
    public NaturalType visitDivMulMod(StaticImpParser.DivMulModContext ctx) {
        visitNatExp(ctx.exp(0));
        visitNatExp(ctx.exp(1));

        return NaturalType.INSTANCE;
    }

    @Override
    public NaturalType visitPlusMinus(StaticImpParser.PlusMinusContext ctx) {
        visitNatExp(ctx.exp(0));
        visitNatExp(ctx.exp(1));

        return NaturalType.INSTANCE;
    }

    @Override
    public BooleanType visitCmpExp(StaticImpParser.CmpExpContext ctx) {
        visitNatExp(ctx.exp(0));
        visitNatExp(ctx.exp(1));

        return BooleanType.INSTANCE;
    }

    @Override
    public BooleanType visitEqExp(StaticImpParser.EqExpContext ctx) {
        ExpType left = visitExp(ctx.exp(0));
        ExpType right = visitExp(ctx.exp(1));

        if (!left.equals(right)) {
            System.err.println("Left side and right side must have the same type");
            System.err.println("@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());

            throw new StaticSemanticsException();
        }

        return BooleanType.INSTANCE;
    }

    @Override
    public BooleanType visitLogicExp(StaticImpParser.LogicExpContext ctx) {
        visitBoolExp(ctx.exp(0));
        visitBoolExp(ctx.exp(1));

        return BooleanType.INSTANCE;
    }

    @Override
    public ExpType visitId(StaticImpParser.IdContext ctx) {
        String id = ctx.ID().getText();

        if (conf.get(id) == null) {
            System.err.println("Variable " + id + " used but never declared");
            System.err.println("@" + ctx.start.getLine() + ":" + ctx.start.getCharPositionInLine());

            throw new StaticSemanticsException();
        }

        return conf.get(id);
    }
}

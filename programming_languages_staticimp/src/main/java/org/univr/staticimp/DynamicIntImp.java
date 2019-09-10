package org.univr.staticimp;

import java.util.Random;
import org.univr.staticimp.antlr.StaticImpBaseVisitor;
import org.univr.staticimp.antlr.StaticImpParser;
import org.univr.staticimp.value.*;
import org.univr.staticimp.value.Value;

public class DynamicIntImp extends StaticImpBaseVisitor<Value> {

    private final DynamicConf conf;
    private final Random genRandom;

    public DynamicIntImp(DynamicConf conf) {
        this.conf = conf;
        this.genRandom = new Random();
    }

    @Override
    public ComValue visitProg(StaticImpParser.ProgContext ctx) {
        return visitCom(ctx.com());
    }

    @Override
    //======================================== DinamicSemantics: DOWHILE ===============================================
    public ComValue visitDowhile(StaticImpParser.DowhileContext ctx) {
        // First visit com parsing tree.
        visit(ctx.com());

        // Then check boolean exp.
        // If true, continue interation, otherwise terminate "do { com } while ( exp )" com.
        if (!visitBoolExp(ctx.exp()))
            return ComValue.INSTANCE;

        // To continue iteration restart to visit the input DoWhile context.
        // visitCom() return a ComValue.
        return visitCom(ctx);
    }

    private int visitNatExp(StaticImpParser.ExpContext ctx) {
        return ((NaturalValue) visit(ctx)).toJavaValue();
    }

    private boolean visitBoolExp(StaticImpParser.ExpContext ctx) {
        return ((BooleanValue) visit(ctx)).toJavaValue();
    }

    private ExpValue<?> visitExp(StaticImpParser.ExpContext ctx) {
        return (ExpValue<?>) visit(ctx);
    }

    private ComValue visitCom(StaticImpParser.ComContext ctx) {
        return (ComValue) visit(ctx);
    }

    @Override
    public ComValue visitSkip(StaticImpParser.SkipContext ctx) {
        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitDecl(StaticImpParser.DeclContext ctx) {
        switch (ctx.type.getType()) {
            case StaticImpParser.TBOOL : conf.put(ctx.ID().getText(), new BooleanValue(false)); break;
            case StaticImpParser.TNAT  : conf.put(ctx.ID().getText(), new NaturalValue(0));
        }

        return ComValue.INSTANCE;
    }

    @Override
    //======================================== DinamicSemantics: ND ====================================================
    public ComValue visitNd(StaticImpParser.NdContext ctx) {
        // Calculate a random 0 or 1.
        // If 1 then execute com(0), otherwise visit com(1) parsing tree.
        // "nd ( com , com )" is a com, in fact visitCom() return a ComValue.
        if(genRandom.nextBoolean()){
            return visitCom(ctx.com(0));
        }

        return visitCom(ctx.com(1));
    }

    @Override
    //======================================== DinamicSemantics: FOR ===================================================
    public ComValue visitFor(StaticImpParser.ForContext ctx) {
        // I have to do the assignment only once!!! So I'll use while construct...

        // Save the result of exp(0) parsing tree on ID.
        // The same code of visitAssign().
        conf.put(ctx.ID().getText(), visitExp(ctx.exp(0)));

        // Each time exp(1) evaluated is true, execute first com(1) and then com(0)
        while(visitBoolExp(ctx.exp(1))) {
            visit(ctx.com(1));
            visit(ctx.com(0));
        }

        // "for ( assign ; exp ; com ) { com }" is a com
        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitAssign(StaticImpParser.AssignContext ctx) {
        conf.put(ctx.ID().getText(), visitExp(ctx.exp()));
        return ComValue.INSTANCE;
    }

    @Override
    public ComValue visitOut(StaticImpParser.OutContext ctx) {
        System.out.println(visitExp(ctx.exp()));
        return ComValue.INSTANCE;
    }


    @Override
    public ComValue visitWhile(StaticImpParser.WhileContext ctx) {
        if (!visitBoolExp(ctx.exp()))
            return ComValue.INSTANCE;

        visit(ctx.com());
        return visitCom(ctx);
    }

    @Override
    public ComValue visitSeq(StaticImpParser.SeqContext ctx) {
        visit(ctx.com(0));
        visit(ctx.com(1));

        return ComValue.INSTANCE;
    }

    @Override
    //======================================== DinamicSemantics: IF ====================================================
    public ComValue visitIf(StaticImpParser.IfContext ctx) {
        // "if ( exp ) then { com } [elseif ( exp ) { com }]* [else ( exp ) { com }]?" is a com,
        // in fact I'll return a ComValue using visitCom(), or using his instance.

        // IF ... (ELSEIF ...)*
        // For each exp on this context, evaluate it and if true, execute the respective com.
        int i = 0;

        for(StaticImpParser.ExpContext exp: ctx.exp()){
            if(visitBoolExp(exp))
                return visitCom(ctx.com(i));

            i++;
        }

        // (ELSE ...)?
        // If every "IF ... (ELSEIF ...)*" exp conditions result false, the interpreter is here.
        // If there is "ELSE ..." in this context, then ctx.com(i) != null, so visit the respective "ELSE ..." com.
        // otherwise, if there isn't "ELSE ...", ctx.com(i) is null, so terminate it doing nothing.
        if( ctx.com(i) != null ){
            return visitCom(ctx.com(i));
        }
        else{
            return ComValue.INSTANCE;
        }

    }

    @Override
    public NaturalValue visitNat(StaticImpParser.NatContext ctx) {
        return new NaturalValue(Integer.parseInt(ctx.NAT().getText()));
    }

    @Override
    public BooleanValue visitBool(StaticImpParser.BoolContext ctx) {
        return new BooleanValue(Boolean.parseBoolean(ctx.BOOL().getText()));
    }

    @Override
    public ExpValue<?> visitParExp(StaticImpParser.ParExpContext ctx) {
        return visitExp(ctx.exp());
    }

    @Override
    public ExpValue<?> visitNot(StaticImpParser.NotContext ctx) {
        return new BooleanValue(!visitBoolExp(ctx.exp()));
    }

    @Override
    public NaturalValue visitPow(StaticImpParser.PowContext ctx) {
        int base = visitNatExp(ctx.exp(0));
        int exp = visitNatExp(ctx.exp(1));

        return new NaturalValue((int) Math.pow(base, exp));
    }

    @Override
    public NaturalValue visitDivMulMod(StaticImpParser.DivMulModContext ctx) {
        int left = visitNatExp(ctx.exp(0));
        int right = visitNatExp(ctx.exp(1));

        switch (ctx.op.getType()) {
            case StaticImpParser.DIV : return new NaturalValue(left / right);
            case StaticImpParser.MUL : return new NaturalValue(left * right);
            case StaticImpParser.MOD : return new NaturalValue(left % right);
        }

        return null; // dumb return (non-reachable code)
    }

    @Override
    public NaturalValue visitPlusMinus(StaticImpParser.PlusMinusContext ctx) {
        int left = visitNatExp(ctx.exp(0));
        int right = visitNatExp(ctx.exp(1));

        switch (ctx.op.getType()) {
            case StaticImpParser.PLUS  : return new NaturalValue(left + right);
            case StaticImpParser.MINUS : return new NaturalValue(Math.max(left - right, 0));
        }

        return null; // dumb return (non-reachable code)
    }

    @Override
    public BooleanValue visitCmpExp(StaticImpParser.CmpExpContext ctx) {
        int left = visitNatExp(ctx.exp(0));
        int right = visitNatExp(ctx.exp(1));

        switch (ctx.op.getType()) {
            case StaticImpParser.GEQ : return new BooleanValue(left >= right);
            case StaticImpParser.LEQ : return new BooleanValue(left <= right);
            case StaticImpParser.LT  : return new BooleanValue(left < right);
            case StaticImpParser.GT  : return new BooleanValue(left > right);
        }

        return null; // dumb return (non-reachable code)
    }

    @Override
    public BooleanValue visitEqExp(StaticImpParser.EqExpContext ctx) {
        ExpValue<?> left = visitExp(ctx.exp(0));
        ExpValue<?> right = visitExp(ctx.exp(1));

        switch (ctx.op.getType()) {
            case StaticImpParser.EQQ : return new BooleanValue(left.equals(right));
            case StaticImpParser.NEQ : return new BooleanValue(!left.equals(right));
        }

        return null; // dumb return (non-reachable code)
    }

    @Override
    public BooleanValue visitLogicExp(StaticImpParser.LogicExpContext ctx) {
        boolean left = visitBoolExp(ctx.exp(0));
        boolean right = visitBoolExp(ctx.exp(1));

        switch (ctx.op.getType()) {
            case StaticImpParser.AND : return new BooleanValue(left && right);
            case StaticImpParser.OR  : return new BooleanValue(left || right);
        }

        return null; // dumb return (non-reachable code)
    }

    @Override
    public ExpValue<?> visitId(StaticImpParser.IdContext ctx) {
        return conf.get(ctx.ID().getText());
    }
}

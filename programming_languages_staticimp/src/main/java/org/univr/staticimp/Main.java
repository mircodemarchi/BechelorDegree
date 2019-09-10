package org.univr.staticimp;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.univr.staticimp.antlr.StaticImpLexer;
import org.univr.staticimp.antlr.StaticImpParser;

import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String args[]) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(args[0]);
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());

        try {
            System.out.println(staticIntImp.visit(tree));
        }
        catch (StaticSemanticsException e) {
            System.err.println("----------------------------");
            System.err.println("|    Bad typed program!    |");
            System.err.println("----------------------------");
            System.exit(1);
        }

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);
    }
}

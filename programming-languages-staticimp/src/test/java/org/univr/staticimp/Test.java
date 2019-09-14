package org.univr.staticimp;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.univr.staticimp.antlr.StaticImpLexer;
import org.univr.staticimp.antlr.StaticImpParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class Test {

    private static final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private static final PrintStream originalOut = System.out;
    private static final PrintStream originalErr = System.err;

    @BeforeAll
    public static void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterAll
    public static void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @BeforeEach
    public void clearStreams() {
        outContent.reset();
        errContent.reset();
    }

    @org.junit.jupiter.api.Test
    void bad_do_while() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("bad-do-while");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());

        assertThrows(StaticSemanticsException.class, () -> staticIntImp.visit(tree));
    }

    @org.junit.jupiter.api.Test
    void bad_for() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("bad-for");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());

        assertThrows(StaticSemanticsException.class, () -> staticIntImp.visit(tree));
    }

    @org.junit.jupiter.api.Test
    void bad_for2() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("bad-for2");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());

        assertThrows(StaticSemanticsException.class, () -> staticIntImp.visit(tree));
    }

    @org.junit.jupiter.api.Test
    void bad_if() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("bad-if");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());

        assertThrows(StaticSemanticsException.class, () -> staticIntImp.visit(tree));
    }

    @org.junit.jupiter.api.Test
    void do_while() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("do-while");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertEquals("0123456789", outContent.toString().replaceAll("\\s", ""));
    }

    @org.junit.jupiter.api.Test
    void factorial() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("factorial");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertEquals("120", outContent.toString().replaceAll("\\s", ""));
    }

    @org.junit.jupiter.api.Test
    void fib() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("fib");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertEquals("55", outContent.toString().replaceAll("\\s", ""));
    }

    @org.junit.jupiter.api.Test
    void _for() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("for");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertEquals("0123456789", outContent.toString().replaceAll("\\s", ""));
    }

    @org.junit.jupiter.api.Test
    void guess() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("guess");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertTrue(outContent.toString().replaceAll("\\s", "").matches("\\d+"));
    }

    @org.junit.jupiter.api.Test
    void if_then() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("if-then");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertEquals("00", outContent.toString().replaceAll("\\s", ""));
    }

    @org.junit.jupiter.api.Test
    void if_then_elseif() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("if-then-elseif");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertEquals("12", outContent.toString().replaceAll("\\s", ""));
    }

    @org.junit.jupiter.api.Test
    void if_then_elseif_else() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("if-then-elseif-else");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertEquals("3", outContent.toString().replaceAll("\\s", ""));
    }

    @org.junit.jupiter.api.Test
    void nd() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("nd");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertTrue("0".equals(outContent.toString().replaceAll("\\s", "")) || "1".equals(outContent.toString().replaceAll("\\s", "")));
    }

    @org.junit.jupiter.api.Test
    void test_randomness() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("test-randomness");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertTrue(100 > Integer.parseInt(outContent.toString().replaceAll("\\s", "")));
    }

    @org.junit.jupiter.api.Test
    void weird_for() throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("weird-for");
        CharStream charStream = CharStreams.fromStream(inputStream);

        StaticImpLexer lexer = new StaticImpLexer(charStream);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        StaticImpParser parser = new StaticImpParser(tokenStream);

        parser.setErrorHandler(new DefaultErrorStrategy() {
            @Override
            public void recover(Parser recognizer, RecognitionException e) {
                throw e;
            }
        });

        ParseTree tree = parser.prog();

        StaticIntImp staticIntImp = new StaticIntImp(new StaticConf());
        staticIntImp.visit(tree);

        DynamicIntImp dynamicIntImp = new DynamicIntImp(new DynamicConf());
        dynamicIntImp.visit(tree);

        assertEquals("09", outContent.toString().replaceAll("\\s", ""));
    }
}

import org.junit.Test
import org.junit.Assert.*
//import kotlin.test.assertEquals
class TestTest {
    private val codes = arrayListOf(
        "public class Main {\n" +
                "\n" +
                "   public static void main(String[] args) {\n" +
                "\n" +
                "\n" +
                "\n" +
                "       System.out.println(20 + 20);\n" +
                "\n" +
                "\n" +
                "\n" +
                "   }\n" +
                "\n" +
                "}\n" +
                "\n",
        "public class Main {\n" +
                "\n" +
                "   public static void main(String[] args) {\n" +
                "\n" +
                "\n" +
                "\n" +
                "       System.out.println(\"20 + 20\");\n" +
                "\n" +
                "\n" +
                "\n" +
                "   }\n" +
                "\n" +
                "}",
        "public class Main {\n" +
                "\n" +
                "   public static void main(String[] args) {\n" +
                "\n" +
                "\n" +
                "\n" +
                "       int x = 20;\n" +
                "\n" +
                "       int y = 4;\n" +
                "\n" +
                "\n" +
                "\n" +
                "       int result = x * y;\n" +
                "\n" +
                "\n" +
                "\n" +
                "       System.out.println(result);\n" +
                "\n" +
                "\n" +
                "\n" +
                "   }\n" +
                "\n" +
                "}",
        "public class Main {\n" +
                "\n" +
                "   public static void main(String[] args) {\n" +
                "\n" +
                "  \n" +
                "\n" +
                "       int a = 5;\n" +
                "\n" +
                "       int b = 2;\n" +
                "\n" +
                "      \n" +
                "\n" +
                "       if(a > b && a * b != 10){\n" +
                "\n" +
                "      \n" +
                "\n" +
                "           System.out.println(\"真\");\n" +
                "\n" +
                "          \n" +
                "\n" +
                "       } else {\n" +
                "\n" +
                "      \n" +
                "\n" +
                "           System.out.println(\"偽\");\n" +
                "\n" +
                "          \n" +
                "\n" +
                "       }\n" +
                "\n" +
                "      \n" +
                "\n" +
                "   }\n" +
                "\n" +
                "  \n" +
                "\n" +
                "}",
        "public class Main {\n" +
                "\n" +
                "   public static void main(String[] args) {\n" +
                "\n" +
                "\n" +
                "\n" +
                "       int count = 3;\n" +
                "\n" +
                "      \n" +
                "\n" +
                "       for(int i = 0; i <= count; i++){\n" +
                "\n" +
                "      \n" +
                "\n" +
                "           System.out.println( i );\n" +
                "\n" +
                "          \n" +
                "\n" +
                "       }\n" +
                "\n" +
                "\n" +
                "\n" +
                "   }\n" +
                "\n" +
                "}"
    )
    @Test
    fun testLex(){
        assertEquals(
            arrayListOf("").drop(1),
            lex("{{}}")
        )
        assertEquals(
            arrayListOf(","),
            lex("{{,}}")
        )
        assertEquals(
            arrayListOf("1","!=","2"),
            lex("{{1!=2}")
        )
        assertEquals(
            arrayListOf("System.out.println","(","20", "+", "20",")",";"),
            lex(codes[0])
        )
        assertEquals(
            arrayListOf("System.out.println","(","\"","20", "+" ,"20","\"",")",";"),
            lex(codes[1])
        )
        assertEquals(
            arrayListOf("int","x","=","20",";",
                    "int","y", "=" ,"4",";" ,
                    "int","result", "=", "x" ,"*", "y",";" ,
                    "System.out.println","(","result",")",";"),
            lex(codes[2])
        )
        assertEquals(
            arrayListOf("int","a","=", "5",";",
                    "int","b", "=", "2",";",
                    "if","(","a", ">", "b" ,"&&", "a", "*", "b" ,"!=", "10",")","{" ,
                    "System.out.println","(","\"","真","\"",")",";" ,
                    "}","else","{" ,
                    "System.out.println","(","\"","偽","\"",")",";" ,
                    "}"),
            lex(codes[3])
        )
        assertEquals(
            arrayListOf("int", "count", "=", "3",";" ,
                    "for","(","int", "i", "=", "0",";", "i", "<=", "count",";", "i","++",")","{" ,
                    "System.out.println","(", "i",")",";" ,
                    "}"),
            lex(codes[4])
        )
    }
    @Test
    fun testToken(){
        assertEquals(
            arrayListOf(Token.Open,Token.Close),
            tokenize(lex("{{()}}"))
        )
        assertEquals(
            arrayListOf(Token.Print,
                Token.Open,Token.NumInt(20), Token.Oprator(Operator.Plus), Token.NumInt(20),
                Token.Close,Token.Semicolon),
            tokenize(lex(codes[0]))
        )
        assertEquals(
            //"if(1<2){return true} else {return false}"
            arrayListOf(Token.If, Token.Open ,
                Token.NumDouble(1.1) , Token.Oprator(Operator.Less) , Token.NumDouble(2.2),
                Token.Close,
                Token.OpenBra, Token.Return,Token.True,Token.CloseBra,
                Token.Else,
                Token.OpenBra, Token.Return,Token.False,Token.CloseBra
                ),
            tokenize(lex("{{" +
                    "if(1.1 < 2.2){return true} else {return false}" +
                    "}}"))
        )
    }
    @Test
    fun testGetPareClose(){
        assertEquals(
            1,
            getPareClose(
                arrayListOf(Token.Open , Token.Close),
                Token.Open,
                Token.Close
            )
        )
        assertEquals(
            3,
            getPareClose(
                arrayListOf(Token.Open ,Token.Open , Token.Close, Token.Close,Token.Open , Token.Close),
                Token.Open,
                Token.Close
            )
        )
    }
}
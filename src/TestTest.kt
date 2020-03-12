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
    fun testParser(){
        assertEquals(
            arrayListOf("").drop(1),
            parser("{{}}")
        )
        assertEquals(
            arrayListOf(","),
            parser("{{,}}")
        )
        assertEquals(
            arrayListOf("1","!=","2"),
            parser("{{1!=2}")
        )
        assertEquals(
            arrayListOf("System.out.println","(","20", "+", "20",")",";"),
            parser(codes[0])
        )
        assertEquals(
            arrayListOf("System.out.println","(","\"","20", "+" ,"20","\"",")",";"),
            parser(codes[1])
        )
        assertEquals(
            arrayListOf("int","x","=","20",";",
                    "int","y", "=" ,"4",";" ,
                    "int","result", "=", "x" ,"*", "y",";" ,
                    "System.out.println","(","result",")",";"),
            parser(codes[2])
        )
        assertEquals(
            arrayListOf("int","a","=", "5",";",
                    "int","b", "=", "2",";",
                    "if","(","a", ">", "b" ,"&&", "a", "*", "b" ,"!=", "10",")","{" ,
                    "System.out.println","(","\"","真","\"",")",";" ,
                    "}","else","{" ,
                    "System.out.println","(","\"","偽","\"",")",";" ,
                    "}"),
            parser(codes[3])
        )
        assertEquals(
            arrayListOf("int", "count", "=", "3",";" ,
                    "for","(","int", "i", "=", "0",";", "i", "<=", "count",";", "i","++",")","{" ,
                    "System.out.println","(", "i",")",";" ,
                    "}"),
            parser(codes[4])
        )
    }
}
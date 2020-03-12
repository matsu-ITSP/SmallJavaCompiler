class Main{}
    private val signs = arrayOf(
        '+','-','*','/','%',
        '&','|','!','=','<','>',
        ';',':','"','(',')','{','}'
    )
    private val twoSigns = arrayOf(
        "+=","-=","*=","/=","%=","++","--",
        "&&","||","!=","==","<=",">="
    )
    fun lex(item : String) : Token {
        return when(item){
            ";" -> Token.Semicolon
            "\"" -> Token.DQuatation
            "System.out.println" -> Token.Print
            "if" -> Token.If
            "else" -> Token.Else
            "for" -> Token.For
            "(" -> Token.Open
            ")" -> Token.Close
            "{" -> Token.OpenBra
            "}" -> Token.CloseBra
            //item を Int にして String に戻して同じになるなら Int
            item.toIntOrNull().toString() -> Token.NumInt(item.toInt())
            item.toDoubleOrNull().toString() -> Token.NumDouble(item.toDouble())

            "+" -> Token.Oprator(Operator.Plus)
            "-" -> Token.Oprator(Operator.Minus)
            "*" -> Token.Oprator(Operator.Mult)
            "/" -> Token.Oprator(Operator.Div)
            "%" -> Token.Oprator(Operator.Mod)
            "++" -> Token.Oprator(Operator.PP)
            "--" -> Token.Oprator(Operator.MM)
            "&&" -> Token.Oprator(Operator.And)
            "||" -> Token.Oprator(Operator.Or)
            "!" -> Token.Oprator(Operator.Not)
            "==" -> Token.Oprator(Operator.Equal)
            "!=" -> Token.Oprator(Operator.NotEqual)
            "<" -> Token.Oprator(Operator.Less)
            ">" -> Token.Oprator(Operator.Greater)
            "<=" -> Token.Oprator(Operator.LessEq)
            ">=" -> Token.Oprator(Operator.GreaterEq)
            "=" -> Token.Oprator(Operator.Assign)
            "+=" -> Token.Oprator(Operator.PlusAssign)
            "-=" -> Token.Oprator(Operator.MinusAssign)
            "*=" -> Token.Oprator(Operator.MultAssign)
            "/=" -> Token.Oprator(Operator.DivAssign)
            "%=" -> Token.Oprator(Operator.ModAssign)

            else -> Token.Text(item)
        }
    }
    /*
    fun lexes(items :List<String>) : List<Token>{

    }
     */
    /*
     main関数の中身だけ見る
        "public static void main(String[] args) {"より前は削除
            「最初の"{"より前を削除」を二回
        最後の"}"二つを削除
            「最後の"}"より後を削除」を二回
     */
    fun parser(item : String) : List<String>{
        var code = item
            .substringBeforeLast('}')
            .substringBeforeLast('}')
            .substringAfter('{')
            .substringAfter('{')
        //演算子があれば前後に空白を入れる
        for(item in signs){
            code = code.replace(item.toString() , " $item ")
        }
        //"==" -> "=  =" を戻す
        val twoSignsSpace = twoSigns.map{ it -> it[0] + "  " + it[1]}
        for(item in twoSignsSpace){
            code = code.replace(item , item.replace("  ",""))
        }
        //スペース区切り
        return code.split(' ','\n').filter { it != "" }
    }

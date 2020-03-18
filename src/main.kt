class Main {}

private val signs = arrayOf(
    '+', '-', '*', '/', '%',
    '&', '|', '!', '=', '<', '>',
    ';', ':', '"', '(', ')', '{', '}'
)
private val twoSigns = arrayOf(
    "+=", "-=", "*=", "/=", "%=", "++", "--",
    "&&", "||", "!=", "==", "<=", ">="
)

/*
 main関数の中身だけ見る
    "public static void main(String[] args) {"より前は削除
        「最初の"{"より前を削除」を二回
    最後の"}"二つを削除
        「最後の"}"より後を削除」を二回
 */
fun lex(item: String): List<String> {
    var code = item
        .substringBeforeLast('}')
        .substringBeforeLast('}')
        .substringAfter('{')
        .substringAfter('{')
    //演算子があれば前後に空白を入れる
    for (item in signs) {
        code = code.replace(item.toString(), " $item ")
    }
    //"==" -> "=  =" を戻す
    val twoSignsSpace = twoSigns.map { it[0] + "  " + it[1] }
    for (item in twoSignsSpace) {
        code = code.replace(item, item.replace("  ", ""))
    }
    //スペース区切り
    return code.split(' ', '\n').filter { it != "" }
}
fun tokenize(splitedCode : List<String>) : List<Token>{
    return splitedCode.map {
        when(it){
            ";" -> Token.Semicolon
            ":" -> Token.Colon
            "\"" -> Token.DQuatation
            "System.out.println" -> Token.Print
            "if" -> Token.If
            "else" -> Token.Else
            "switch"-> Token.Switch
            "case"-> Token.Case
            "default"-> Token.Default
            "for" -> Token.For
            "do" -> Token.Do
            "while" -> Token.While
            "return"-> Token.Return
            "break" -> Token.Break
            "continue" -> Token.Continue
            "true" -> Token.True
            "false" -> Token.False
            "(" -> Token.Open
            ")" -> Token.Close
            "{" -> Token.OpenBra
            "}" -> Token.CloseBra
            //item を Int にして String に戻して同じになるなら Int
            it.toIntOrNull().toString() -> Token.NumInt(it.toInt())
            it.toDoubleOrNull().toString() -> Token.NumDouble(it.toDouble())

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

            else -> Token.Text(it)
        }
    }
}
//頭のトークンと対応するとじかっこの位置を返す , 対応するかっこがなければ-1
fun getPareClose(tokens : List<Token> , openToken: Token, closeToken: Token , beginIndex:Int=0) : Int{
    var depth = 0
    for(i in beginIndex until tokens.count()){
        if(tokens[i] == openToken) depth++
        else if(tokens[i] == closeToken) depth--
        if(depth==0) return i
    }
    return -1
}
fun getIndexOfEndStatement(tokens: List<Token> , beginIndex: Int = 0) : Int{
    return when(tokens[beginIndex]){
        //Label , VarDeclare
        is Token.Text -> {
            if (tokens[1] == Token.Semicolon) //label
                getIndexOfEndStatement(tokens.drop(2))
            else //var_declare
                tokens.indexOf(Token.Semicolon)
        }
        is Token.OpenBra -> //statementblock
            getPareClose(tokens , Token.OpenBra , Token.CloseBra)
        is Token.If -> {
            if(tokens[1] != Token.Open)
                throw Exception("IF Statement but it is not if (")
            var index = getPareClose(tokens,Token.Open,Token.Close)
            index++
            if(tokens[index] != Token.OpenBra)
                throw Exception("IF Statement but it is not if (){")
            val indexOpenBra = index
            index = getPareClose(tokens , Token.OpenBra , Token.CloseBra , indexOpenBra)
            if(index+1 == tokens.count() || tokens[index+1] != Token.Else) {
                index
            } else if(tokens[index+1] == Token.Else) {
                index += 2
                if(tokens[index] != Token.OpenBra){
                    throw Exception("IF Statement but it is not if (){}else{}")
                }
                getPareClose(tokens , Token.OpenBra , Token.CloseBra , index)
            }else{
                throw Exception("IF Statement but it is not if(){}else{}")
            }
        }
        //"do" "{" <statement> "}"  "while"  "(" expression  ")"  ";"  .
        is Token.Do ->{
            if(tokens[1] != Token.OpenBra)
                throw Exception("Do Statement but do{")
            var index = getPareClose(tokens , Token.OpenBra , Token.CloseBra)
            if(tokens[index+1] != Token.While || tokens[index+2] != Token.Open)
                throw Exception("Do Statement but do{}while(")
            index += 2
            index = getPareClose(tokens , Token.Open , Token.Close , index)
            if(tokens[index+1] != Token.Semicolon)
                throw Exception("Do Statement but do{}while();")
            else index+1
        }
        //"while"  "(" expression  ")" "{" <statement> "}" .
        is Token.While ->{
            var index =1
            if(tokens[index] != Token.Open)
                throw Exception("While Statement but while(")
            index = getPareClose(tokens , Token.Open , Token.Close)
            index++
            if(tokens[index] != Token.OpenBra)
                throw Exception("While Statement but while(){")
            getPareClose(tokens , Token.OpenBra , Token.CloseBra , index)
        }
        is Token.For -> getIndexOfEndStatement(tokens , getPareClose(tokens , Token.Open , Token.Close)+1)
        is Token.Switch ->
            getPareClose(tokens , Token.OpenBra , Token.CloseBra , getPareClose(tokens , Token.Open , Token.Close))
        is Token.Return -> {
            tokens.indexOf(Token.Semicolon)
        }
        is Token.Break -> {
            tokens.indexOf(Token.Semicolon)
        }
        is Token.Continue -> {
            tokens.indexOf(Token.Semicolon)
        }
        is Token.Semicolon -> 1
        else -> tokens.indexOf(Token.Semicolon)
    }
}
fun parse(tokens : List<Token>) : Statement{
    fun parseExpression(tokens: List<Token>) : Expression{

    }
    fun parseForStatement(tokens: List<Token>) : ForStatement{

    }
    fun parseIdentifier(token: Token) : Identifier{

    }
    fun parseVarDeclare(tokens : List<Token>) : VarDeclaration{

    }
    fun parseStatementBlock(tokens : List<Token>) : List<Statement>{

    }

    fun parseStatement(tokens : List<Token>) : Statement{
        fun parseSwitchStatement(tokens: List<Token>) : SwitchStatement{
            fun parseSwitchSub(tokens: List<Token>) : SwitchSub{
                return when(tokens[0]){
                    Token.Case -> {
                        if(tokens[tokens.count()-1] != Token.Colon) throw Exception("Case statement but not end :")
                        SwitchSub.Case(parseExpression(tokens.subList(1,tokens.count()-2)))
                    }
                    Token.Default ->{
                        if(tokens[tokens.count()-1] != Token.Colon) throw Exception("Default statement but not end :")
                        SwitchSub.Default
                    }
                    else -> SwitchSub.State(parseStatement(tokens))
                }
            }
            var index = 0;
            if(tokens[index] != Token.Switch) throw Exception("SwitchStatement but not start Switch")
            index++
            if(tokens[index] != Token.Open) throw Exception("SwitchStatement but not 2nd (")
            index = getPareClose(tokens,Token.Open,Token.Close)
            val condExp = parseExpression(tokens.subList(2,index))
            index++
            if(tokens[index] != Token.OpenBra) throw Exception("SwitchStatement but after cond not {")
            index++
            var switchSubs : List<SwitchSub> = listOf<SwitchSub>()
            while(tokens[index] != Token.CloseBra){
                when(tokens[index]){

                }
            }
        }
        return when(tokens[0]){
            //Label , VarDeclare
            is Token.Text -> {
                if (tokens[1] == Token.Semicolon)
                    Statement.Label(parseIdentifier(tokens[0]), parseStatement(tokens.drop(2)))
                else Statement.VarDeclare(parseVarDeclare(tokens))
            }
            is Token.OpenBra -> Statement.StatementBlock(parseStatementBlock(tokens.subList(1,tokens.count()-2)))
            is Token.If -> {
                if(tokens[1] != Token.Open)
                    throw Exception("IF Statement but it is not if (")
                val indexPareClose = getPareClose(tokens,Token.Open,Token.Close)

                val remainTokens = tokens.drop(indexPareClose+1)
                if(remainTokens[0] != Token.OpenBra)
                    throw Exception("IF Statement but it is not if (){")
                val indexPareCloseBra = getPareClose(remainTokens , Token.OpenBra , Token.CloseBra)
                if(indexPareCloseBra+1 == remainTokens.count()) {
                    Statement.If(
                        parseExpression(tokens.subList(1, indexPareClose - 1)),
                        parseStatementBlock(remainTokens.subList(1, remainTokens.count() - 2)),
                        null
                    )
                } else if(remainTokens[indexPareCloseBra+1] == Token.Else) {
                    if(remainTokens[indexPareCloseBra+2] != Token.OpenBra || remainTokens[remainTokens.count()-1] != Token.CloseBra){
                        throw Exception("IF Statement but it is not if (){}else{}")
                    }
                    Statement.If(
                        parseExpression(tokens.subList(1, indexPareClose - 1)),
                        parseStatementBlock(remainTokens.subList(1, indexPareCloseBra-1)),
                        parseStatementBlock(remainTokens.subList(indexPareCloseBra+3 , remainTokens.count()-2))
                    )
                }else{
                    throw Exception("IF Statement but it is not if (){}else{}")
                }
            }
            //"do" "{" <statement> "}"  "while"  "(" expression  ")"  ";"  .
            is Token.Do ->{
                if(tokens[1] != Token.OpenBra)
                    throw Exception("Do Statement but do{")
                val indexCloseBra = getPareClose(tokens , Token.OpenBra , Token.CloseBra)
                if(tokens[indexCloseBra+1] != Token.While || tokens[indexCloseBra+2] != Token.Open ||
                   tokens[tokens.count()-2] != Token.Close || tokens[tokens.count()-1] != Token.Semicolon)
                    throw Exception("Do Statement but do {..} while (")

                Statement.Do(
                    parseExpression(tokens.subList(indexCloseBra+3 , tokens.count()-3)),
                    parseStatement(tokens.subList(2,indexCloseBra-1))
                )
            }
            //"while"  "(" expression  ")" "{" <statement> "}" .
            is Token.While ->{
                if(tokens[1] != Token.Open)
                    throw Exception("While Statement but while(")
                val indexClose = getPareClose(tokens , Token.Open , Token.Close)
                if(tokens[indexClose+1] != Token.OpenBra || tokens[tokens.count()-1] != Token.CloseBra)
                    throw Exception("Do Statement but do {..} while (")

                Statement.While(
                    parseExpression(tokens.subList(2,indexClose-1)),
                    parseStatement(tokens.subList(indexClose+2 , tokens.count()-2))
                )
            }
            is Token.For -> Statement.For(parseForStatement(tokens))
            is Token.Switch -> Statement.Switch(parseSwitchStatement(tokens))
            is Token.Return -> {
                if(tokens[tokens.count()-1] != Token.Semicolon)
                    throw Exception("return but no ;")
                if(tokens.count() == 2) {
                    Statement.Return(null)
                }else{
                    Statement.Return(parseExpression(tokens.subList(1,tokens.count()-2)))
                }
            }
            is Token.Break -> {
                if(tokens[tokens.count()-1] != Token.Semicolon)
                    throw Exception("break but no ;")
                if(tokens.count() == 2) {
                    Statement.Break(null)
                } else if (tokens.count() == 3){
                    Statement.Break(parseIdentifier(tokens[1]))
                } else {
                    throw Exception("break but 4 token")
                }
            }
            is Token.Continue -> {
                if(tokens[tokens.count()-1] != Token.Semicolon)
                    throw Exception("continue but no ;")
                if(tokens.count() == 2) {
                    Statement.Continue(null)
                } else if (tokens.count() == 3){
                    Statement.Continue(parseIdentifier(tokens[1]))
                } else {
                    throw Exception("break but 4 token")
                }
            }
            is Token.Semicolon -> Statement.Semicolon
            else -> Statement.Exp(parseExpression(tokens))
        }
    }
}
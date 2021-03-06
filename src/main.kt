import java.util.*

class Main {}

private val signs = arrayOf(
    '+', '-', '*', '/', '%',
    '&', '|', '!', '=', '<', '>',
    ';', ':', '"', '(', ')', '{', '}' , ','
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
    //{ x , " , a , b , " , y } -> { x , "ab" , y }
    fun stringnize(splitedCode: List<String>) : List<String>{
        fun margeString(code : List<String>) : String
                = code.fold(""){initial , value -> initial+value}
        fun f(remainCode : List<String>) : List<String>{
            return when {
                remainCode.count()==0 -> listOf()
                remainCode[0] != "\"" -> listOf(remainCode[0]).plus(f(remainCode.drop(1)))
                else -> {
                    val endpoint = remainCode.drop(1).indexOf("\"")+1
                    listOf(margeString(remainCode.subList( 0,endpoint+1 )))
                        .plus(f(remainCode.drop(endpoint+1)))
                }
            }
        }
        return f(splitedCode)
    }
    return stringnize(splitedCode).map {
        when(it){
            ";" -> Token.Semicolon
            ":" -> Token.Colon
            "," -> Token.Comma
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
            "null" -> Token.Null
            "public" -> Token.Public
            "protected" -> Token.Protected
            "private" -> Token.Private
            "static" -> Token.Static
            "final" -> Token.Final
            "native" -> Token.Native
            "synchronized" -> Token.Synchronized
            "abstract" -> Token.Abstract
            "threadsafe" -> Token.Threadsafe
            "transient" -> Token.Transient
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
            //"?"  -> Token.Oprator(Operator.Question)
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

            else -> {
                if (it.contains("\"")) Token.Str(it.drop(1).dropLast(1))
                else Token.Text(it)
            }
        }
    }
}
//頭のトークンと対応するとじかっこの位置を返す , 対応するかっこがなければ-1
fun getPareClose(tokens : List<Token> , openToken: Token, closeToken: Token , beginIndex:Int=0) : Int{
    var depth = 0
    for(i in beginIndex until tokens.count()){
        if(tokens[i] == openToken) depth++
        else if(depth == 0) continue;
        else if(tokens[i] == closeToken) depth--
        if(depth==0) return i
    }
    return -1
}
fun getPareCloseLists(tokens : List<Token> , openTokens: List<Token>, closeTokens: List<Token> , beginIndex:Int=0) : Int{
    var depth = 0
    for(i in beginIndex until tokens.count()){
        if(openTokens.contains(tokens[i])) depth++
        else if(closeTokens.contains(tokens[i])) depth--
        if(depth==0) return i
    }
    return -1
}
fun getIndexOfEndStatement(tokens: List<Token>, beginIndex: Int = 0) : Int{
    val droppedTokens = tokens.drop(beginIndex)
    return beginIndex + when(droppedTokens[0]){
        //Label , VarDeclare
        is Token.Text -> {
            if (droppedTokens[1] == Token.Semicolon) //label
                getIndexOfEndStatement(droppedTokens.drop(2))
            else //var_declare
                droppedTokens.indexOf(Token.Semicolon)
        }
        is Token.OpenBra -> //statementblock
            getPareClose(droppedTokens , Token.OpenBra , Token.CloseBra)
        is Token.If -> {
            if(droppedTokens[1] != Token.Open)
                throw Exception("IF Statement but it is not if (")
            var index = getPareClose(droppedTokens,Token.Open,Token.Close)
            index++
            if(droppedTokens[index] != Token.OpenBra)
                throw Exception("IF Statement but it is not if (){")
            val indexOpenBra = index
            index = getPareClose(droppedTokens , Token.OpenBra , Token.CloseBra , indexOpenBra)
            if(index+1 == droppedTokens.count() || droppedTokens[index+1] != Token.Else) {
                index
            } else if(droppedTokens[index+1] == Token.Else) {
                index += 2
                if(droppedTokens[index] != Token.OpenBra){
                    throw Exception("IF Statement but it is not if (){}else{}")
                }
                getPareClose(droppedTokens , Token.OpenBra , Token.CloseBra , index)
            }else{
                throw Exception("IF Statement but it is not if(){}else{}")
            }
        }
        //"do" "{" <statement> "}"  "while"  "(" expression  ")"  ";"  .
        is Token.Do ->{
            if(droppedTokens[1] != Token.OpenBra)
                throw Exception("Do Statement but do{")
            var index = getPareClose(droppedTokens , Token.OpenBra , Token.CloseBra)
            if(droppedTokens[index+1] != Token.While || droppedTokens[index+2] != Token.Open)
                throw Exception("Do Statement but do{}while(")
            index += 2
            index = getPareClose(droppedTokens , Token.Open , Token.Close , index)
            if(droppedTokens[index+1] != Token.Semicolon)
                throw Exception("Do Statement but do{}while();")
            else index+1
        }
        //"while"  "(" expression  ")" "{" <statement> "}" .
        is Token.While ->{
            var index =1
            if(droppedTokens[index] != Token.Open)
                throw Exception("While Statement but while(")
            index = getPareClose(droppedTokens , Token.Open , Token.Close)
            index++
            if(droppedTokens[index] != Token.OpenBra)
                throw Exception("While Statement but while(){")
            getPareClose(droppedTokens , Token.OpenBra , Token.CloseBra , index)
        }
        is Token.For -> getIndexOfEndStatement(droppedTokens , getPareClose(droppedTokens , Token.Open , Token.Close)+1)
        is Token.Switch ->
            getPareClose(droppedTokens , Token.OpenBra , Token.CloseBra , getPareClose(droppedTokens , Token.Open , Token.Close))
        is Token.Return -> {
            droppedTokens.indexOf(Token.Semicolon)
        }
        is Token.Break -> {
            droppedTokens.indexOf(Token.Semicolon)
        }
        is Token.Continue -> {
            droppedTokens.indexOf(Token.Semicolon)
        }
        is Token.Semicolon -> 1
        else -> droppedTokens.indexOf(Token.Semicolon)
    }
}
fun parse(tokens : List<Token>) : List<Statement>{

    fun parseExpression(tokens: List<Token>) : Expression{

        fun getPriority(op : Operator , isRight : Boolean = false) : Int{
            return when(op){
                Operator.Plus -> 6
                Operator.Minus -> if (isRight) 8 else 6
                Operator.MinusRight -> 8
                Operator.Mult -> 7
                Operator.Div -> 7
                Operator.Mod -> 7
                Operator.PP -> if (isRight) 8 else 9
                Operator.PPRight -> 8
                Operator.MM -> if (isRight) 8 else 9
                Operator.MMRight -> 8
                Operator.And -> 3
                Operator.Or -> 2
                Operator.Not -> 8
                Operator.Equal -> 4
                Operator.NotEqual -> 4
                Operator.Less -> 5
                Operator.Greater -> 5
                Operator.LessEq -> 5
                Operator.GreaterEq -> 5
                Operator.Assign -> 1
                Operator.PlusAssign -> 1
                Operator.MinusAssign -> 1
                Operator.MultAssign -> 1
                Operator.DivAssign -> 1
                Operator.ModAssign -> 1
                Operator.Open , Operator.Close-> throw Exception("it cant be ( or )")
            }
        }
        fun getArgument(op : Operator , isRight : Boolean = false) : Int{
            return when(op){
                Operator.Plus -> 2
                Operator.Minus -> if (isRight) 1 else 2
                Operator.MinusRight -> 1
                Operator.Mult -> 2
                Operator.Div -> 2
                Operator.Mod -> 2
                Operator.PP , Operator.PPRight-> 1
                Operator.MM , Operator.MMRight-> 1
                Operator.And -> 2
                Operator.Or -> 2
                Operator.Not -> 1
                Operator.Equal -> 2
                Operator.NotEqual -> 2
                Operator.Less -> 2
                Operator.Greater -> 2
                Operator.LessEq -> 2
                Operator.GreaterEq -> 2
                Operator.Assign -> 2
                Operator.PlusAssign -> 2
                Operator.MinusAssign -> 2
                Operator.MultAssign -> 2
                Operator.DivAssign -> 2
                Operator.ModAssign -> 2
                Operator.Open , Operator.Close-> throw Exception("it cant be ( or )")
            }
        }
        fun isRight(op : Operator , index : Int) : Boolean =
            (op is Operator.PP || op is Operator.MM || op is Operator.Minus) &&
                    (index == 0 || tokens[index-1] == Token.Open ||
                            (tokens[index-1] is Operator &&
                                    tokens[index-1] != Token.Oprator(Operator.PP) &&
                                    tokens[index-1] != Token.Oprator(Operator.MM)))

        val output : Stack<Expression> = Stack()
        val stackOp : Stack<Operator> = Stack()
        var index = 0
        fun calculate(){
            val op = stackOp.pop()
            when(getArgument(op)){
                1 -> output.push(Expression.Calculate(op,output.pop()))
                2 -> output.push(Expression.Calculate(op,output.pop(),output.pop()))
                3 -> output.push(Expression.Calculate(op,output.pop(),output.pop(),output.pop()))
            }
        }
        //https://ja.wikipedia.org/wiki/%E6%93%8D%E8%BB%8A%E5%A0%B4%E3%82%A2%E3%83%AB%E3%82%B4%E3%83%AA%E3%82%BA%E3%83%A0
        while(index < tokens.count()){
            val t = tokens[index]
            when(t){
                is Token.NumInt -> output.push(Expression.IntValue(t.num))
                is Token.NumDouble -> output.push(Expression.DoubleValue(t.num))
                is Token.Text -> output.push(Expression.Id(Identifier(t.text)))
                is Token.Null -> output.push(Expression.ExpNull)
                is Token.Str -> output.push(Expression.StringValue(t.str))

                is Token.Open -> stackOp.push(Operator.Open)
                is Token.Close ->{
                    while(stackOp.peek() != Operator.Open){
                        if(stackOp.count() == 0) throw Exception("there is ) but no (")
                        calculate()
                    }
                    stackOp.pop()
                }
                is Token.Oprator ->{
                    val op = when{
                        !isRight(t.op,index) -> t.op
                        t.op == Operator.Minus -> Operator.MinusRight
                        t.op == Operator.MM -> Operator.MMRight
                        t.op == Operator.PP -> Operator.PPRight
                        else -> throw Exception("something error on Operator in Expression")
                    }
                    //(最初にある || かっこの直後 || (8+-1の- && 8++-1は除く))は右結合
                    val isRight = isRight(op,index)
                    while(stackOp.count() > 0){
                        val top = stackOp.peek()
                        if(getPriority(op) < getPriority(top) || (!isRight && getPriority(op) == getPriority(top)))
                            calculate()
                        else break
                    }
                    stackOp.push(op)
                }
                else -> throw Exception("There is Expression but token is $t")
            }
            index++
        }
        while (stackOp.count() > 0){
            calculate()
        }
        if(output.count() != 1) throw Exception("")
        return output.pop()
    }
    fun parseVarDeclare(tokens : List<Token>) : VarDeclaration{
        fun parseVarDeclarator(tokens: List<Token>) : VariableDeclarator{
            var index = 0
            if(tokens[index] !is Token.Text) throw Exception("VarDeclarator but no text")
            val id = Identifier((tokens[index] as Token.Text).text)
            if(tokens.count() == 1) return VariableDeclarator(id,null)
            index++
            if(tokens[index] != Token.Oprator(Operator.Assign))
                throw Exception("VarDeclarator with 2 tokens but no assign")
            return VariableDeclarator(id , VariableInitializer.Exp(parseExpression(tokens.drop(2))))
        }
        val allModifiers = listOf<Token>(
            Token.Public , Token.Private , Token.Protected , Token.Static ,
            Token.Final , Token.Native , Token.Synchronized , Token.Abstract ,
            Token.Threadsafe , Token.Transient
        )

        val modifiers : MutableList<Modifier> = mutableListOf()
        var index = 0
        while(allModifiers.contains(tokens[index])){
            modifiers.add(
                when(tokens[index]){
                    Token.Public -> Modifier.Public
                    Token.Private -> Modifier.Private
                    Token.Protected -> Modifier.Protected
                    Token.Static -> Modifier.Static
                    Token.Final -> Modifier.Final
                    Token.Native -> Modifier.Native
                    Token.Synchronized -> Modifier.Synchronized
                    Token.Abstract -> Modifier.Abstract
                    Token.Threadsafe -> Modifier.Threadsafe
                    Token.Transient -> Modifier.Transient
                    else -> throw Exception("")
                }
            )
        }
        if(tokens[index] !is Token.Text) throw Exception("VarDeclaration but no type")
        val ty = Type((tokens[index] as Token.Text).text)
        index++
        val declarators : MutableList<VariableDeclarator> = mutableListOf()
        do {
            val commaIndex = tokens.indexOf(Token.Comma)-1
            val endIndex = if(commaIndex != -2) commaIndex else tokens.indexOf(Token.Semicolon)-1
            declarators.add(parseVarDeclarator(tokens.subList(index , endIndex+1)))
            index = endIndex + 1
        }while(tokens[index] == Token.Comma)
        if(tokens[index] != Token.Semicolon) throw Exception("VarDeclaration but no ;")
        return VarDeclaration(modifiers , ty , declarators)
    }

    fun parseIdentifier(token: Token) : Identifier =
        if (token is Token.Text) Identifier(token.text) else throw Exception("id but not text")

    fun parseStatement(tokens : List<Token>) : Statement{
        fun parseSwitchStatement(tokens: List<Token>) : SwitchStatement{
            /* fun parseSwitchSub(tokens: List<Token>) : SwitchSub{
                return when(tokens[0]){
                    Token.Case -> {
                        if(tokens[tokens.count()-1] != Token.Colon) throw Exception("Case statement but not end :")
                        SwitchSub.Case(parseExpression(tokens.subList(1,tokens.count()-1)))
                    }
                    Token.Default ->{
                        if(tokens[tokens.count()-1] != Token.Colon) throw Exception("Default statement but not end :")
                        SwitchSub.Default
                    }
                    else -> SwitchSub.State(parseStatement(tokens))
                }
            } */
            var index = 0;
            if(tokens[index] != Token.Switch) throw Exception("SwitchStatement but not start Switch")
            index++
            if(tokens[index] != Token.Open) throw Exception("SwitchStatement but not 2nd (")
            index = getPareClose(tokens,Token.Open,Token.Close)
            val condExp = parseExpression(tokens.subList(2,index+1))
            index++
            if(tokens[index] != Token.OpenBra) throw Exception("SwitchStatement but after cond not {")
            index++
            val switchSubs : MutableList<SwitchSub> = mutableListOf<SwitchSub>()
            poi@while(true){
                when(tokens[index]){
                    is Token.Case ->{
                        val indexOfCase = index
                        index = getPareCloseLists(
                            tokens ,
                            listOf(Token.Case),
                            listOf(Token.Colon),
                            index
                        )
                        switchSubs.add(SwitchSub.Case(parseExpression(tokens.subList(indexOfCase,index+1))))
                        index++
                    }
                    is Token.Default ->{
                        index++
                        if(tokens[index] != Token.Colon)
                            throw Exception("It is not Default:")
                        switchSubs.add(SwitchSub.Default)
                        index++
                    }
                    is Token.CloseBra -> break@poi;
                    else ->{
                        val indexOfStartStatement = index
                        index = getIndexOfEndStatement(tokens , indexOfStartStatement)
                        switchSubs.add(SwitchSub.State(parseStatement(tokens.subList(indexOfStartStatement , index+1))))
                        index++
                    }
                }
            }
            return SwitchStatement(condExp , switchSubs)
        }
        fun parseForStatement(tokens: List<Token>) : ForStatement{
            var index = 0;
            if(tokens[index] != Token.For) throw Exception("Statement for but no for")
            index++;
            if (tokens[index] != Token.Open) throw Exception("Statement for but no for(")
            if (!tokens.contains(Token.Semicolon)) throw Exception("Statement for but no ;")
            index=tokens.indexOf(Token.Semicolon)
            val varDeclare = if(index!=2) parseVarDeclare(tokens.subList(2,index+1)) else null
            index++
            val indexOfExp1 = index
            index += tokens.drop(index).indexOf(Token.Semicolon)
            if (index+1 == indexOfExp1) throw Exception("Statement for but no for(;;")
            val exp1 = if(index != indexOfExp1)parseExpression(tokens.subList(indexOfExp1,index)) else null
            val indexOfExp2 = index+1
            index = tokens.indexOf(Token.Close)
            if (index+1 == indexOfExp2) throw Exception("Statement for but no for(;;)")
            val exp2 = if (index != indexOfExp2)parseExpression(tokens.subList(indexOfExp2,index)) else null
            val statement = parseStatement(tokens.subList(index+1 , tokens.count()))
            return ForStatement(varDeclare , exp1,exp2,statement)
        }
        fun parseStatementBlock(tokens : List<Token>) : List<Statement>{
            val statements = mutableListOf<Statement>()
            if (tokens[0] != Token.OpenBra || tokens[tokens.count()-1] != Token.CloseBra)
                throw Exception("StatementBlock but not {}")
            if(tokens.count() == 2) return statements
            var startStatement = 1
            var endStatement = getIndexOfEndStatement(tokens,startStatement)
            while (true){
                statements.add(parseStatement(tokens.subList(startStatement,endStatement+1)))
                startStatement = endStatement+1
                if (tokens[startStatement] == Token.CloseBra) break
                endStatement = getIndexOfEndStatement(tokens,startStatement)
            }
            return statements
        }
        return when(tokens[0]){
            //Label , VarDeclare
            is Token.Text -> {
                if (tokens[1] == Token.Semicolon)
                    Statement.Label(parseIdentifier(tokens[0]), parseStatement(tokens.drop(2)))
                else Statement.VarDeclare(parseVarDeclare(tokens))
            }
            is Token.OpenBra -> Statement.StatementBlock(parseStatementBlock(tokens))
            is Token.If -> {
                var index = 1
                if(tokens[index] != Token.Open)
                    throw Exception("IF Statement but it is not if (")
                index = getPareClose(tokens,Token.Open,Token.Close)
                val exp = parseExpression(tokens.subList(2, index))
                index++
                if(tokens[index] != Token.OpenBra)
                    throw Exception("IF Statement but it is not if (){")
                val indexOpenBra = index
                index = getPareClose(tokens , Token.OpenBra , Token.CloseBra , index)
                if(index+1 == tokens.count()) {
                    Statement.If(
                        exp,
                        parseStatementBlock(tokens.subList(indexOpenBra , index+1)),
                        null
                    )
                } else if(tokens[index+1] == Token.Else) {
                    if(tokens[index+2] != Token.OpenBra || tokens[tokens.count()-1] != Token.CloseBra){
                        throw Exception("IF Statement but it is not if (){}else{}")
                    }
                    Statement.If(
                        exp,
                        parseStatementBlock(tokens.subList(indexOpenBra , index+1)),
                        parseStatementBlock(tokens.subList(index+2,tokens.count()))
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
                    parseExpression(tokens.subList(indexCloseBra+3 , tokens.count()-2)),
                    parseStatement(tokens.subList(2,indexCloseBra))
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
                    parseExpression(tokens.subList(2,indexClose)),
                    parseStatement(tokens.subList(indexClose+2 , tokens.count()-1))
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
                    Statement.Return(parseExpression(tokens.subList(1,tokens.count()-1)))
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
            is Token.Print -> {
                if(tokens[1] != Token.Open) throw Exception("Print but println(")
                if(tokens[tokens.count()-2] != Token.Close) throw Exception("Print but println()")
                if(tokens[tokens.count()-1] != Token.Semicolon) throw Exception("Print but println()")
                Statement.Print(parseExpression(tokens.subList(2,tokens.count()-2)))
            }
            else -> {
                if(tokens.last() != Token.Semicolon) throw Exception("Expression but no ;")
                Statement.Exp(parseExpression(tokens.dropLast(1)))
            }
        }
    }
    val statements = mutableListOf<Statement>()
    if(tokens.count() == 0) return statements
    var startStatement = 0
    var endStatement = getIndexOfEndStatement(tokens,startStatement)
    while (true){
        statements.add(parseStatement(tokens.subList(startStatement,endStatement+1)))
        startStatement = endStatement+1
        if (startStatement == tokens.count()) break
        endStatement = getIndexOfEndStatement(tokens,startStatement)
    }
    return statements
}
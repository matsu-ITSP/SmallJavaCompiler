sealed class StringExpression {
    data class PareExp(val exp1 : Expression , val sign : StrSign , val exp2 : Expression) : StringExpression()
/*
string_expression  =
( expression
    (  "+"
    /  "+="  )
expression  )  .
 */
}
sealed class StrSign{
    object Plus : StrSign()
    object PlusAssign : StrSign()
}
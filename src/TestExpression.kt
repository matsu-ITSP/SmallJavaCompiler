sealed class TestExpression {
    data class PareExp(val exp1 : Expression , val sign : TestSign , val exp2 : Expression) : TestExpression()
    /*
    testing_expression  =
 ( expression
    (  ">"
    /  "<"
    /  ">="
    /  "<="
    /  "=="
    /  "!="  )
expression  )  .
     */
}
sealed class TestSign{
    object Equal : TestSign()
    object NotEqual : TestSign()
    object Less : TestSign()
    object Greater : TestSign()
    object LessEq : TestSign()
    object GreaterEq : TestSign()
}
sealed class LogicExpression{
    data class NotExp(val exp : Expression) : LogicExpression()
    data class PareExp(val exp1: Expression , val sign : LogicSign , val exp2: Expression) : LogicExpression()
    data class Triple(val exp1: Expression , val exp2: Expression , val exp3: Expression) : LogicExpression()
    object Tr : LogicExpression()
    object Fls : LogicExpression()
/*
logical_expression  =
 (  "!" expression )
 /  ( expression
        (  "&&"
        / * "&&="
        /  "||"
        / * "||="
        / * "^"
        / * "^="
        / * (  "&"  "&"  )
        / * "||="
        / * "%"
        / * "%="  )
    expression  )
 /  ( expression  "?" expression  ":" expression )
 /  "true"
 /  "false"  .
 */
}
sealed class LogicSign {
    object And : LogicSign()
    object Or : LogicSign()
}

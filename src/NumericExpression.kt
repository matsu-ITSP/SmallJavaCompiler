sealed class NumericExpression {
    data class PrefixExp(val pre_sign : PreNumSign, val exp : Expression) : NumericExpression()
    data class SuffixExp(val exp : Expression , val suf_sign : SufNumSign) : NumericExpression()
    data class PareExpression(val exp1 : Expression, val sign : NumSign, val exp2 : Expression) : NumericExpression()
    /*
    numeric_expression  =
  (  (  "-"
     /  "++"
     /  "--"  )
  expression )
 /  ( expression
       (  "++"
       /  "--"  )  )
 /  ( expression
        (  "+"
        /  "+="
        /  "-"
        /  "-="
        /  "*"
        /  "*="
        /  "/"
        /  "/="
        /  "%"
        /  "%="  )
    expression  )  .
     */
}
sealed class PreNumSign{
    object Minus : PreNumSign()
    object PP : PreNumSign()
    object MM : PreNumSign()
    /*
    (  (  "-"
     /  "++"
     /  "--"  )
     */
}
sealed class SufNumSign{
    object PP : SufNumSign()
    object MM : SufNumSign()
    /*
    (
     /  "++"
     /  "--"  )
     */
}
sealed class NumSign{
    object Plus : NumSign()
    object Minus : NumSign()
    object Mult : NumSign()
    object Div : NumSign()
    object Mod : NumSign()
    object PlusAssign : NumSign()
    object MinusAssign : NumSign()
    object MultAssign : NumSign()
    object DivAssign : NumSign()
    object ModAssign : NumSign()
    /*
    (  "+"
        /  "+="
        /  "-"
        /  "-="
        /  "*"
        /  "*="
        /  "/"
        /  "/="
        /  "%"
        /  "%="  )
     */
}
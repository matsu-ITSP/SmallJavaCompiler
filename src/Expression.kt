sealed class Expression {
    data class NumExp(val num_exp : NumericExpression) : Expression()
    data class TestExp(val test_exp : TestExpression) : Expression()
    data class LogicExp(val logic_exp : LogicExpression) : Expression()
    data class StrExp(val str_exp : StringExpression) : Expression()
    object ExpNull : Expression()
    data class Id(val id : Identifier) : Expression()
    data class ParenthesisExp(val exp : Expression) : Expression()
    /*
    expression  =
   numeric_expression
 / testing_expression
 / logical_expression
 / string_expression
 / *bit_expression
 / *casting_expression
 / *creating_expression
 / *literal_expression
 /  "null"
 / * "super"
 / * "this"
 / identifier
 /  (  "(" expression  ")"  )
 / * ( expression (  (  "("  [ arglist ]  ")"  )
 /                  (  "[" expression  "]"  )
 /                  (  "." expression  )
 /                  (  "," expression  )
 /                  (  "instanceof"  ( class_name / interface_name )  )
 )  )  .
     */
}
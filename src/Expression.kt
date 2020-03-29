sealed class Expression {
    data class Calculate(
        val op : Operator ,
        val exp1: Expression ,
        val exp2: Expression? = null,
        val exp3: Expression? = null
    ) : Expression()
    object ExpNull : Expression()
    data class Id(val id : Identifier) : Expression()
    data class IntValue(val num : Int) : Expression()
    data class DoubleValue(val num : Double) : Expression()
    data class StringValue(val str : String) : Expression()
    /*
    expression  =
   numeric_expression
 / testing_expression
 / logical_expression
 / string_expression
 / *bit_expression
 / *casting_expression
 / *creating_expression
 / intValue
 / doubleValue
 / Text
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
sealed class VariableInitializer {
    data class Exp(val expression: Expression) : VariableInitializer()
    //data class Arr(val arr : List<VariableInitializer>)
/*
variable_initializer  =
expression
 / * (  "{"  [ variable_initializer
 <  "," variable_initializer  >  [  ","  ]  ]  "}"  )  .

 */
}
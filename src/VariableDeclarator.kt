data class VariableDeclarator (
    val id: Identifier,
    val init : VariableInitializer?
){
/*
variable_declarator  =
 identifier  <  "["  "]"  >  [  "=" variable_initializer ]  .
 */
}
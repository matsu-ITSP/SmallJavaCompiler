data class VarDeclaration(
    val modifiers: List<Modifier>?,
    val type: Type,
    val variable_declarators : List<VariableDeclarator>
){
/*
variable_declaration  =
 < modifier > type variable_declarator
 <  "," variable_declarator  >  ";"  .
 */
}

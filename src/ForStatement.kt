data class ForStatement(
    val varDeclaration: VarDeclaration?,
    val endCond : Expression?,
    val changeState : Expression?,
    val state : Statement
){

}
/*
for_statement =
 "for"  "("  ( variable_declaration  / * ( expression  ";"  )  /  ";"  )
 [ expression ]  ";"
 [ expression ]  ";"
 ")" statement  .

 */
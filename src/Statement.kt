sealed class Statement {
    data class VarDeclare(val var_declare : VarDeclaration) : Statement()
    data class Exp(val exp : Expression) : Statement()
    data class StatementBlock(val state : Statement) : Statement()
    data class If(val cond : Expression , val then_state : Statement , val else_state : Statement) : Statement()
    data class Do(val cond: Expression , val state: Statement) : Statement()
    data class While(val cond: Expression , val state: Statement) : Statement()
    data class For(val for_state : ForStatement) : Statement()
    data class Switch(val switch_state : SwitchStatement) : Statement()
    data class Return(val exp: Expression?) : Statement()
    data class Label(val id : Identifier , val state : Statement) : Statement()
    data class Break(val id : Identifier?) : Statement()
    data class Continue(val id: Identifier?) : Statement()
    object Semicolon : Statement()
/*
statement  =
variable_declaration
 /  ( expression  ";"  )
 /  ( statement_block )
 /  ( if_statement )
 /  ( do_statement )
 /  ( while_statement )
 /  ( for_statement )
 /  * ( try_statement )
 /  ( switch_statement )
 /  * (  "synchronized"  "(" expression  ")" statement  )
 /  (  "return"  [ expression ]  ";"  )
 /  * (  "throw" expression ";"  )
 /  ( identifier  ":" statement )
 /  (  "break"  [ identifier ]  ";"  )
 /  (  "continue"  [ identifier ]  ";"  )
 /  (  ";"  )  .

 if_statement =
 "if"  "(" expression  ")" statement
 [  "else" statement ]  .

do_statement =
 "do" statement  "while"  "(" expression  ")"  ";"  .

while_statement =
 "while"  "(" expression  ")" statement  .
 */
}
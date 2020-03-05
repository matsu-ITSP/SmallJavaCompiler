data class SwitchStatement(
    val cond : Expression,
    val sub: SwitchSub
){}
sealed class SwitchSub{
    data class Case(val expression: Expression) : SwitchSub()
    object Default : SwitchSub()
    data class State(val statement: Statement) : SwitchSub()
}
/*
switch_statement =
 "switch"  "(" expression  ")"  "{"
 <     (  "case" expression  ":"  )
    /  (  "default"  ":"  )
    /  statement >
 "}"  .
 */
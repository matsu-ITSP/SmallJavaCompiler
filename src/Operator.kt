sealed class Operator {
    object Plus : Operator()
    object Minus : Operator()
    object MinusRight : Operator()
    object Mult : Operator()
    object Div : Operator()
    object Mod : Operator()

    object PP : Operator()
    object PPRight : Operator()
    object MM : Operator()
    object MMRight : Operator()

    object And : Operator()
    object Or : Operator()
    object Not : Operator()
    object Equal : Operator()
    object NotEqual : Operator()
    //object Question : Operator()

    object Less : Operator()
    object Greater : Operator()
    object LessEq : Operator()
    object GreaterEq : Operator()

    object Assign : Operator()
    object PlusAssign : Operator()
    object MinusAssign : Operator()
    object MultAssign : Operator()
    object DivAssign : Operator()
    object ModAssign : Operator()

    //Expression用
    object Open : Operator()
    object Close : Operator()
}

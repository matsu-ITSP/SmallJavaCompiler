sealed class Token {
    object Semicolon : Token()
    object DQuatation : Token()
    object Colon : Token()
    object Print : Token()
    object If : Token()
    object Else : Token()
    object Switch : Token()
    object Case : Token()
    object Default : Token()
    object For : Token()
    object While : Token()
    object Do : Token()
    object Return : Token()
    object Break : Token()
    object Continue : Token()
    object True : Token()
    object False : Token()
    data class Oprator(val op : Operator) : Token()
    //data class Id(val name : String) : Token()
    object Open : Token()
    object Close : Token()
    object OpenBra : Token()
    object CloseBra : Token()
    data class NumInt(val num : Int) : Token()
    data class NumDouble(val num : Double) : Token()
    //data class Type(val ty : Type) : Token()
    data class Text(val text : String) : Token()
}
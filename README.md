Java の関数一つをコンパイルする。
簡単のため、一部の文を制限している。

if_statement =
 "if"  "(" expression  ")" statement
 [  "else" statement ]  .
 
 は以下のものに変更した
 
 if_statement =
  "if"  "(" expression  ")" statement_block
  [  "else" statement_block ]  .
  
  for,do,whileも同様に、statementが入るところはstatement_blockとしている。
  
  - 配列は未実装
  - 三項演算子は未実装
  - 文字列中の「"」は未対応
  - 文字列中の空白・改行は消える
  
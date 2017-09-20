Handles the lexical analysis, syntax analysis, semantic analysis, 
and generates intermediate code for [ICE](http://faculty.frostburg.edu/cosc/mohsen-chitsaz-home/ice/#1) 



The provided grammar: 

```
 	 access static void ID "(" identifier_list ")" "{"
 	 declarations
 	 compound_statement
		"}" "$"
	 access: public
       	| private
 	identifier_list: ID
                | identifier_list "," ID
                | %empty
 	declarations: declarations var identifier_list ":" type ";"
             | %empty
 	type: char
     	| int
	 compound_statement: "{" statement_list "}"
	 statement_list: statement
               | statement_list ";" statement
	 statement: lefthandside
         	 	| compound_statement
          	| get "(" ID ")"
         		 | put "(" ID ")"
        		  | if "(" expression ")" statement

 	lefthandside: ID "=" righthandside
 	righthandside: expression
 	expression: simple_expression
           	| simple_expression relop simple_expression

 	simple_expression: term
                  | simple_expression addop term
	 term: factor
    	 | term mulop factor
 	factor: ID
     	  | num
      	 | true
      	 | false
 	relop: ">"
   	  | ">="
   	   | "=="
    	  | "<="
    	  | "<"
    	  | "<>"
	 addop: "+"
   	   | "-"
 	mulop: "*"
    	  | "/"
    	  | "%"
	 factor: "literal"
```
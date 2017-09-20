 package compiler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import jdk.nashorn.internal.runtime.ParserException;

public class Parser {

    boolean yydebug;        //do I want debug output?
    int yynerrs;            //number of errors so far
    int yyerrflag;          //was there an error?
    int yychar;             //the current working character

//########## STATE STACK ##########
    final static int YYSTACKSIZE = 800;  //maximum stack size
    int statestk[] = new int[YYSTACKSIZE]; //state stack
    int stateptr;
    int stateptrmax;                     //highest index of stackptr
    int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################

    final void state_push(int state) {
        try {
            stateptr++;
            statestk[stateptr] = state;
        } catch (ArrayIndexOutOfBoundsException e) {
            int oldsize = statestk.length;
            int newsize = oldsize * 2;
            int[] newstack = new int[newsize];
            System.arraycopy(statestk, 0, newstack, 0, oldsize);
            statestk = newstack;
            statestk[stateptr] = state;
        }
    }

    final int state_pop() {
        return statestk[stateptr--];
    }

    final void state_drop(int cnt) {
        stateptr -= cnt;
    }

    final int state_peek(int relative) {
        return statestk[stateptr - relative];
    }
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################

    final boolean init_stacks() {
        stateptr = -1;
        val_init();
        return true;
    }
//########## SEMANTIC VALUES ##########

    String yytext;//user variable to return contextual strings
    Token yyval; //used to return semantic vals from action routines
    Token yylval; 
    Token valstk[];
    int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################

    void val_init() {
        valstk = new Token[YYSTACKSIZE];
        yyval = new Token();
        yylval = new Token();
        valptr = -1;
    }

    void val_push(Token val) {
        if (valptr >= YYSTACKSIZE) {
            return;
        }
        valstk[++valptr] = val;
    }

    Token val_pop() {
        if (valptr < 0) {
            return new Token();
        }
        return valstk[valptr--];
    }

    void val_drop(int cnt) {
        int ptr;
        ptr = valptr - cnt;
        if (ptr < 0) {
            return;
        }
        valptr = ptr;
    }

    Token val_peek(int relative) {
        int ptr;
        ptr = valptr - relative;
        if (ptr < 0) {
            return new Token();
        }
        return valstk[ptr];
    }

    final Token dup_yyval(Token val) {
        Token dup = new Token();
        dup.token = val.token;
        dup.sequence = val.sequence;
        return dup;
    }
//#### end semantic value section ####

//######ID STACK##################################
    String idtext;//user variable to return contextual strings
    String[] idstk;
    int idptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################

    void id_init() {
        idstk = new String[YYSTACKSIZE];
        idptr = -1;
    }

    String id_pop() {
        if (idptr < 0) {
            return "";
        }
        return idstk[idptr--];
    }

    String id_peek() {
        if (idptr < 0) {
            return "";
        }
        return idstk[idptr];
    }

    void id_push(String val) {
        if (idptr >= YYSTACKSIZE) {
            return;
        }
        idstk[++idptr] = val;
    }

    //######IF STACK##################################
    // String iftext;//user variable to return contextual strings
    int[] ifstk;
    int ifptr;
    int ifmax;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################

    void if_init() {
        ifstk = new int[YYSTACKSIZE];
        //yyval = new Token();
        //yylval = new Token();
        ifptr = -1;
        ifmax = -1;
    }

    int if_pop() {
        if (ifptr < 0) {
            return -1;
        }
        return ifstk[ifptr--];
    }

    int if_peek() {
        if (ifptr < 0) {
            return -1;
        }
        return ifstk[ifptr];
    }

    void if_push(int val) {
        if (ifptr >= YYSTACKSIZE) {
            return;
        }
        ifstk[++ifptr] = val;
    }

    public final static short YYERRCODE = 256;

    final static short yylhs[] = {-1,
        0, 1, 1, 2, 2, 2, 3, 5, 5, 4,
        6, 6, 7, 7, 7, 7, 7, 8, 10, 9,
        9, 11, 11, 13, 13, 15, 15, 15, 15, 12,
        12, 12, 12, 12, 12, 14, 14, 16, 16, 16,
        15,};

    final static short yylen[] = {2,
        12, 1, 1, 1, 3, 0, 6, 1, 1, 3,
        1, 3, 1, 1, 4, 4, 5, 3, 1, 1,
        3, 1, 3, 1, 3, 1, 1, 1, 1, 1,
        1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
        1,};

    final static short yydefred[] = {0,
        2, 3, 0, 0, 0, 0, 0, 0, 4, 0,
        0, 0, 0, 5, 0, 0, 0, 0, 0, 0,
        0, 0, 14, 0, 11, 13, 0, 0, 0, 0,
        0, 0, 10, 0, 0, 1, 26, 27, 28, 29,
        41, 19, 18, 0, 0, 24, 0, 0, 0, 12,
        8, 9, 0, 36, 37, 30, 31, 34, 33, 32,
        35, 0, 0, 38, 39, 40, 0, 0, 15, 16,
        7, 0, 0, 25, 17,};

    final static short yydgoto[] = {3,
        4, 10, 15, 23, 53, 24, 25, 26, 42, 43,
        44, 62, 45, 63, 46, 67,};

    final static short yysindex[] = {-241,
        0, 0, 0, -254, -250, -248, -225, -201, 0, -257,
        -189, -185, 0, 0, -233, -228, -201, -267, -192, -184,
        -183, -182, 0, -258, 0, 0, -214, -191, -220, -220,
        -180, -178, 0, -228, -208, 0, 0, 0, 0, 0,
        0, 0, 0, -219, -247, 0, -179, -177, -176, 0,
        0, 0, -203, 0, 0, 0, 0, 0, 0, 0,
        0, -220, -220, 0, 0, 0, -220, -228, 0, 0,
        0, -209, -247, 0, 0,};

    final static short yyrindex[] = {0,
        0, 0, 0, 0, 0, 0, 0, -256, 0, 0,
        0, 0, 0, 0, 0, 0, -198, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, -261, -262, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, -259, -235, 0, 0,};

    final static short yygindex[] = {0,
        0, 71, 0, 74, 0, 0, -34, 0, 60, 0,
        29, 0, 30, 0, 25, 0,};

    final static int YYTABLESIZE = 93;
    static short yytable[];

    static {
        yytable();
    }

    static void yytable() {
        yytable = new short[]{50,
            22, 20, 28, 21, 5, 11, 6, 22, 20, 6,
            21, 33, 7, 12, 6, 1, 2, 22, 22, 22,
            22, 22, 22, 22, 22, 22, 20, 23, 21, 34,
            16, 17, 19, 75, 23, 16, 8, 20, 21, 22,
            37, 64, 65, 66, 23, 23, 23, 23, 23, 23,
            23, 23, 23, 38, 39, 40, 12, 35, 41, 9,
            54, 55, 56, 57, 58, 59, 60, 61, 51, 52,
            54, 55, 6, 6, 13, 14, 29, 30, 31, 32,
            48, 36, 49, 68, 71, 69, 70, 27, 18, 47,
            72, 74, 73,};
    }
    static short yycheck[];

    static {
        yycheck();
    }

    static void yycheck() {
        yycheck = new short[]{34,
            263, 263, 270, 263, 259, 263, 263, 270, 270, 260,
            270, 270, 261, 271, 271, 257, 258, 280, 281, 282,
            283, 284, 285, 286, 287, 288, 288, 263, 288, 288,
            264, 265, 261, 68, 270, 264, 262, 266, 267, 268,
            261, 289, 290, 291, 280, 281, 282, 283, 284, 285,
            286, 287, 288, 274, 275, 276, 271, 272, 279, 261,
            280, 281, 282, 283, 284, 285, 286, 287, 277, 278,
            280, 281, 271, 272, 264, 261, 269, 262, 262, 262,
            261, 273, 261, 263, 288, 263, 263, 17, 15, 30,
            62, 67, 63,};
    }
    final static short YYFINAL = 3;
    final static short YYMAXTOKEN = 291;

//The following are now global, to aid in error reporting
    int yyn;       //next next thing to do
    int yym;       //
    int yystate;   //current parsing state from state table
    String yys;    //current token string

    int yyparse(String input) throws IOException {

        LexicalAnalyzer analyzer = new LexicalAnalyzer(input);
        analyzer.setTokens();
        SymbolTable table = new SymbolTable();
        //table.createTable(20);

        boolean doaction;
        boolean tableEntry = true;

        int location = 1;
        int line = 1;
        ArrayList<String> otherFile = new ArrayList<>();
        init_stacks();
        id_init();
        if_init();
        yynerrs = 0;
        yyerrflag = 0; 
        yychar = -1;          //impossible char forces a read
        yystate = 0;           //initial state
        state_push(yystate);  //save it
        val_push(yylval);     //save empty value
        File outFile = new File("output.jam");
        FileWriter fWriter = new FileWriter(outFile);
        boolean loop = true;
        boolean IDCHECK = false;
        try (PrintWriter pWriter = new PrintWriter(fWriter)) {
            while (loop) //until parsing is done, either correctly, or w/error
            {
                doaction = true;

                //#### NEXT ACTION (from reduction table)
                for (yyn = yydefred[yystate]; yyn == 0; yyn = yydefred[yystate]) {

                    if (yychar < 0) //we want a char?
                    {
                        Token info = analyzer.createToken();
                        yychar = info.token;  //get next token
                        yylval = info;

                        //#### ERROR CHECK ####
                        if (yychar < 0) //it it didn't work/error
                        {
                            yychar = 0;      //change it to default string (no -1!)

                        }
                    }//yychar<0
                    yyn = yysindex[yystate];  //get amount to shift by (shift index)
                    if ((yyn != 0) && (yyn += yychar) >= 0
                            && yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {

                        //#### NEXT STATE ####
                        yystate = yytable[yyn];//we are in a new state
                        state_push(yystate);   //save it
                        val_push(yylval);      //push our lval as the input for next rule
                        if (yystate == 13) {
                            yystate = 15;
                            state_push(yystate);
                        }
                        if (yystate == 17) {
                            IDCHECK = true;
                        }
                        yychar = -1;           //since we have 'eaten' a token, say we need another
                        if (yyerrflag > 0) //have we recovered an error?
                        {
                            --yyerrflag;        //give ourselves credit
                        }
                        doaction = false;        //but don't process yet
                        break;   //quit the yyn=0 loop
                    }

                    yyn = yyrindex[yystate];  //reduce
                    if ((yyn != 0) && (yyn += yychar) >= 0
                            && yyn <= YYTABLESIZE && yycheck[yyn] == yychar) {   //we reduced!

                        yyn = yytable[yyn];
                        doaction = true; //get ready to execute
                        break;         //drop down to actions
                    } else //ERROR RECOVERY
                    {
                        loop = false;
                        throw new ParserException(
                                "Parser Error");

                    }//end error recovery
                }//yyn=0 loop
                if (!doaction) //any reason not to proceed?
                {
                    continue;      //skip action
                }
                yym = yylen[yyn];          //get count of terminals on rhs

                if (yym > 0) //if count of rhs not 'nil'
                {
                    yyval = val_peek(yym - 1); //get current semantic value
                }
                yyval = dup_yyval(yyval); //duplicate yyval if Token is used as semantic value
                switch (yyn) {
//########## USER-SUPPLIED ACTIONS ##########
                    case 1: {
                        pWriter.println(line++ + " " + "hlt" + " ," + ",");
                        loop = false;
                    }
                    break;
                    case 4: {
                        if (IDCHECK) {
                            id_push(val_peek(0).sequence);
                            yyval = val_peek(0);
                        }
                        yyval = val_peek(0);
                    }
                    break;
                    case 5: {
                        id_push(val_peek(0).sequence);
                        yyval = val_peek(0);
                    }
                    break;
                    case 7: {
                        int counter = idptr;
                        int type = val_peek(1).token;
                        if (type == 277)//if type is char
                        {
                            for (int i = 0; i < idptr + 1; i++) {
                                if (!table.lookup(id_peek())) {
                                    Token toktemp = new Token(261, idstk[i], location++, '0');
                                    yyval = new Token(261, idstk[i]);
                                    table.insert(idstk[i], toktemp);

                                } else {
                                    yyval = new Token(261, idstk[i]);
                                }

                            }
                        } else if (type == 278)//type = int
                        {
                            for (int i = 0; i < idptr + 1; i++) {

                                if (!table.lookup(id_peek())) {
                                    Token toktemp = new Token(261, idstk[i], location++, '1');
                                    yyval = new Token(261, idstk[i]);
                                    table.insert(idstk[i], toktemp);
                                } else {
                                    yyval = new Token(261, idstk[i]);
                                }
                                
                                
                            }
                        }
                        id_init();
                    }
                    break;
                    case 10: {
                        //compound
                    }
                    break;
                    case 12: {

                    }
                    break;
                    case 15: {//GET
                        if (table.lookupDataType(val_peek(1).sequence) == '1') {
                            if (if_peek() == -1) {
                                pWriter.println(line++ + " " + "sys" + " " + "#" + "1" + "," + "," + table.lookupLocation(val_peek(1).sequence));
                            } else {
                                otherFile.add(line++ + " " + "sys" + " " + "#" + "1" + "," + "," + table.lookupLocation(val_peek(1).sequence));
                            }
                        } else if (table.lookupDataType(val_peek(1).sequence) == '0') {
                            if (if_peek() == -1) {
                                pWriter.println(line++ + " " + "sys" + " " + "#" + "1" + "," + "," + table.lookupLocation(val_peek(1).sequence));
                            } else {
                                otherFile.add(line++ + " " + "sys" + " " + "#" + "1" + "," + "," + table.lookupLocation(val_peek(1).sequence));
                            }
                        } else {
                            throw new ParserException(
                                    "Parser Error");
                        }
                    }
                    break;
                    case 16: //#line 33 "PUT"
                    {
                        //System.out.println("PUT");
                        if (table.lookupDataType(val_peek(1).sequence) == '1') {
                            if (if_peek() == -1) {
                                pWriter.println(line++ + " " + "sys" + " " + "#" + "-1" + "," + table.lookupLocation(val_peek(1).sequence) + ",");
                            } else {
                                otherFile.add(line++ + " " + "sys" + " " + "#" + "-1" + "," + table.lookupLocation(val_peek(1).sequence) + ",");
                            }
                        } else if (table.lookupDataType(val_peek(1).sequence) == '0') {
                            if (if_peek() == -1) {
                                pWriter.println(line++ + " " + "sys" + " " + "#" + "-2" + "," + table.lookupLocation(val_peek(1).sequence) + ",");
                            } else {
                                otherFile.add(line++ + " " + "sys" + " " + "#" + "-2" + "," + table.lookupLocation(val_peek(1).sequence) + ",");
                            }
                        } else {
                            throw new ParserException(
                                    "Parser Error");
                            
                             
                        }

                    }
                    break;
                    case 17: //#line 34 "if statement"
                    {
                        // IF STATEMENT
                        if (if_peek() != -1) {
                            int temp = if_pop();
                            if (if_peek() != -1) {

                                temp = temp - if_peek();
                                String tempLine = otherFile.get(temp);

                                otherFile.remove(temp);
                                otherFile.add(temp, tempLine.replaceAll(",replace", ",#" + (line - 1)));

                            }//if there are no more if statements, we will printout to file
                            else {
                                String tempLine = otherFile.get(0);
                                otherFile.remove(0);
                                otherFile.add(0, tempLine.replaceAll(",replace", ",#" + (line - 1)));
                                for (int i = 0; i < otherFile.size(); i++) {
                                    pWriter.println(otherFile.get(i));
                                }
                                otherFile.clear();
                            }
                        }
                    }
                    break;
                    case 18: //#line 37 "ID = righthandside"
                    {
                        if (if_peek() == -1) {
                            if (val_peek(0).token == 274) {//if num
                                if (table.lookupDataType(val_peek(2).sequence) == '1') {
                                    pWriter.println(line++ + " " + "sto" + " " + val_peek(0).location + "," + "," + table.lookupLocation(val_peek(2).sequence));
                                    yyval = new Token(261, "val_peek(2).sequence");
                                } else {
                                    throw new ParserException(
                                            "Parser Error");
                                }
                            } else if (val_peek(0).token == 261) {//if ID
                                if (table.lookupDataType(val_peek(0).sequence) == table.lookupDataType(val_peek(2).sequence)) {
                                    pWriter.println(line++ + " " + "sto" + " " + table.lookupLocation(val_peek(0).sequence) + "," + "," + table.lookupLocation(val_peek(2).sequence));
                                    yyval = new Token(261, "val_peek(2).sequence");
                                } else {
                                    throw new ParserException(
                                            "Parser Error");
                                }
                            } else if (val_peek(0).token == 279) {//if literal
                                if (table.lookupDataType(val_peek(2).sequence) == '0') {
                                    pWriter.println(line++ + " " + "sto" + " " + val_peek(0).location + "," + "," + table.lookupLocation(val_peek(2).sequence));
                                    yyval = new Token(261, "val_peek(2).sequence");
                                } else {
                                    throw new ParserException(
                                            "Parser Error");
                                }
                            } else {
                                throw new ParserException(
                                        "Parser Error");
                            }
                        } else {
                            if (val_peek(0).token == 274) {//if num

                                if (table.lookupDataType(val_peek(2).sequence) == '1') {
                                    otherFile.add(line++ + " " + "sto" + " " + val_peek(0).location + "," + "," + table.lookupLocation(val_peek(2).sequence));
                                    yyval = new Token(261, "val_peek(2).sequence");
                                } else {
                                    throw new ParserException(
                                            "Parser Error");
                                }
                            } else if (val_peek(0).token == 261) {//if ID
                                if (table.lookupDataType(val_peek(0).sequence) == table.lookupDataType(val_peek(2).sequence)) {
                                    otherFile.add(line++ + " " + "sto" + " " + table.lookupLocation(val_peek(0).sequence) + "," + "," + table.lookupLocation(val_peek(2).sequence));
                                    yyval = new Token(261, "val_peek(2).sequence");
                                } else {
                                    throw new ParserException(
                                            "Parser Error");
                                }
                            } else if (val_peek(0).token == 279) {//if char
                                if (table.lookupDataType(val_peek(2).sequence) == '0') {
                                    otherFile.add(line++ + " " + "sto" + " " + val_peek(0).location + "," + "," + table.lookupLocation(val_peek(2).sequence));
                                    yyval = new Token(261, "val_peek(2).sequence");
                                } else {
                                    throw new ParserException(
                                            "Parser Error");
                                }
                            } else {
                                throw new ParserException(
                                        "Parser Error");
                            }
                        }

                    }
                    break;
                    case 21: //#line 44 "simple relop simple"
                    {
                        if (val_peek(0).token == 274 || val_peek(0).token == 261) {//check if value is num or ID
                            if (val_peek(2).token == 274 || val_peek(2).token == 261) {//check if value is num or ID

                                int temp = val_peek(1).token;

                                if (temp == 282) {//check >
                                    otherFile.add(line++ + " " + "JLE" + " " + val_peek(2).location + "," + val_peek(0).location + "," + "replace");
                                    if_push(line);
                                } else if (temp == 283)//check if >=
                                {
                                    otherFile.add(line++ + " " + "JLT" + " " + val_peek(2).location + "," + val_peek(0).location + "," + "replace");
                                    if_push(line);
                                } else if (temp == 284) {//check ==
                                    otherFile.add(line++ + " " + "JNE" + " " + val_peek(2).location + "," + val_peek(0).location + "," + "replace");
                                    if_push(line);
                                    
                                } else if (temp == 285) {//check <=
                                    otherFile.add(line++ + " " + "JGT" + " " + val_peek(2).location + "," + val_peek(0).location + "," + "replace");
                                    if_push(line);
                                } else if (temp == 286) {//check <

                                    otherFile.add(line++ + " " + "JGE" + " " + val_peek(2).location + "," + val_peek(0).location + "," + "replace");
                                    if_push(line);
                                } else if (temp == 287) {//check <>
                                    otherFile.add(line++ + " " + "JEQ" + " " + val_peek(2).location + "," + val_peek(0).location + "," + "replace");
                                    if_push(line);
                                } else {
                                    throw new ParserException(
                                            "Parser Error");
                                }

                            }
                        }
                    }

                    break;

                    case 23: //#line 48 "simple addop term"
                    {
                        if (val_peek(0).token == 274 || val_peek(0).token == 261) {//check if value is num
                            if (val_peek(2).token == 274 || val_peek(2).token == 261) {//check if value is num
                                int temp = val_peek(1).token;
                                if (if_peek() == -1) {
                                    if (temp == 280) {//check if +
                                        // int result = Integer.parseInt(val_peek(2).sequence) + Integer.parseInt(val_peek(0).sequence);
                                        pWriter.println(line++ + " " + "add" + " " + val_peek(2).location + "," + val_peek(0).location + "," + location++);
                                        yyval = new Token(274, '1', location - 1); // token that is num which contains result as a string
                                    } else if (temp == 281)//check if -
                                    {
                                        pWriter.println(line++ + " " + "sub" + " " + val_peek(2).location + "," + val_peek(0).location + "," + location++);
                                        yyval = new Token(274, '1', location - 1);
                                    } else {
                                        throw new ParserException(
                                                "Parser Error");
                                    }
                                } else {
                                    if (temp == 280) {//check if +
                                        otherFile.add(line++ + " " + "add" + " " + val_peek(2).location + "," + val_peek(0).location + "," + location++);
                                        yyval = new Token(274, '1', location - 1); // token that is num which contains result as a string
                                    } else if (temp == 281)//check if -
                                    {
                                        otherFile.add(line++ + " " + "sub" + " " + val_peek(2).location + "," + val_peek(0).location + "," + location++);
                                        yyval = new Token(274, '1', location - 1);
                                    } else {
                                        throw new ParserException(
                                                "Parser Error");
                                    }
                                }
                            }
                        }
                    }

                    break;

                    case 25: //#line 52 "term mulop factor"
                    {
                        // "term mulop factor"
                        if (val_peek(0).token == 274 || val_peek(0).token == 261) {//check if value is num
                            if (val_peek(2).token == 274 || val_peek(2).token == 261) {//check if value is num
                                if (if_peek() == -1) {
                                    int temp = val_peek(1).token;
                                    if (temp == 289) {//check if +
                                        // int result = Integer.parseInt(val_peek(2).sequence) + Integer.parseInt(val_peek(0).sequence);
                                        pWriter.println(line++ + " " + "mul" + " " + val_peek(2).location + "," + val_peek(0).location + "," + location++);
                                        yyval = new Token(274, '1', location - 1); // token that is num which contains result as a string
                                    } else if (temp == 290)//check if -
                                    {
                                        pWriter.println(line++ + " " + "div" + " " + val_peek(2).location + "," + val_peek(0).location + "," + location++);
                                        yyval = new Token(274, '1', location - 1);
                                    } else if (temp == 291)//check if -
                                    {
                                        pWriter.println(line++ + " " + "mod" + " " + val_peek(2).location + "," + val_peek(0).location + "," + location++);
                                        yyval = new Token(274, '1', location - 1);
                                    } else {
                                        throw new ParserException(
                                                "Parser Error");
                                    }
                                } else {
                                    int temp = val_peek(1).token;
                                    if (temp == 289) {//check if +
                                        // int result = Integer.parseInt(val_peek(2).sequence) + Integer.parseInt(val_peek(0).sequence);
                                        otherFile.add(line++ + " " + "mul" + " " + val_peek(2).location + "," + val_peek(0).location + "," + location++);
                                        yyval = new Token(274, '1', location - 1); // token that is num which contains result as a string
                                    } else if (temp == 290)//check if -
                                    {
                                        otherFile.add(line++ + " " + "div" + " " + val_peek(2).location + "," + val_peek(0).location + "," + location++);
                                        yyval = new Token(274, '1', location - 1);
                                    } else if (temp == 291)//check if -
                                    {
                                        otherFile.add(line++ + " " + "mod" + " " + val_peek(2).location + "," + val_peek(0).location + "," + location++);
                                        yyval = new Token(274, '1', location - 1);
                                    } else {
                                        throw new ParserException(
                                                "Parser Error");
                                    }
                                }
                            }
                        }
                    }//extra
                    break;
                    case 19: //#line 41 "test.cup"
                    {
                        yyval = val_peek(0);
                    }
                    break;
                    case 20: //#line 44 "test.cup"
                    {
                        yyval = val_peek(0);
                    }
                    break;
                    case 22: //#line 48 "test.cup"
                    {
                        yyval = val_peek(0);
                    }
                    break;
                    case 24: //#line 52 "test.cup"
                    {
                        yyval = val_peek(0);
                    }
                    break;
                    case 26: {
                        if (table.lookup(val_peek(0).sequence)) {
                            yyval = table.lookupToken(val_peek(0).sequence);
                        }
                    }
                    break;
                    case 27: //#line 57 "spotted an int token"
                    {
                        if (if_peek() == -1) {
                            pWriter.println(line++ + " " + "sto" + " " + "#" + (Integer.parseInt(val_peek(0).sequence)) + "," + "," + location++);
                        } else {

                            otherFile.add(line++ + " " + "sto" + " " + "#" + (Integer.parseInt(val_peek(0).sequence)) + "," + "," + location++);
                        }
                        yyval = new Token(274, val_peek(0).sequence, location - 1);
                    }
                    break;
                    case 41: //#line 79 "spotted a character token "
                    {
                        if (if_peek() == -1) {
                            char tempC = val_peek(0).sequence.charAt(1);
                            pWriter.println(line++ + " " + "sto" + " " + "#" + ((int) tempC) + "," + "," + location++);
                        } else {
                            char tempC = val_peek(0).sequence.charAt(1);
                            otherFile.add(line++ + " " + "sto" + " " + "#" + ((int) tempC) + "," + "," + location++);
                        }
                        yyval = new Token(279, val_peek(0).sequence, location - 1);

                    }
                    break;
//########## END OF USER-SUPPLIED ACTIONS ##########
                }//switch
                //#### Now let's reduce... ####
                if (loop == false) {
                    break;
                }
                state_drop(yym);             //we just reduced yylen states
                yystate = state_peek(0);     //get new state
                val_drop(yym);               //corresponding value drop
                yym = yylhs[yyn];            //select next TERMINAL(on lhs)
                if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
                {
                    yystate = YYFINAL;         //explicitly say we're done
                    state_push(YYFINAL);       //and save it
                    val_push(yyval);           //also save the semantic value of parsing
                    if (yychar < 0) //we want another character?
                    {
                        Token temp = analyzer.createToken();
                        yychar = temp.token;      //get next character
                        if (yychar < 0) {
                            yychar = 0;  //clean, if necessary
                        }

                    }
                    if (yychar == 0) //Good exit (if lex returns 0 ;-)
                    {
                        break;                 //quit the loop--all DONE
                    }
                }//if yystate
                else //else not done yet
                {    //get next state and push, for next yydefred[]
                    yyn = yygindex[yym];      //find out where to go
                    if ((yyn != 0) && (yyn += yystate) >= 0
                            && yyn <= YYTABLESIZE && yycheck[yyn] == yystate) {
                        yystate = yytable[yyn]; //get new state
                    } else {
                        yystate = yydgoto[yym]; //else go to new defred
                    }
                    state_push(yystate);     //going again, so push state & val...
                    val_push(yyval);         //for next action
                }
            }//main loop
            pWriter.flush();
            return 0;
        }
    }

    public Parser() {
        //nothing to do
    }
}


/*
 *  This will be responsible for user variables/ information that is not part of the reserve words.  
 */
package compiler;

import java.util.*;

/**
 * this symbol table will be used for the compiler
 *
 * @author Benjamin Barton
 */
public class SymbolTable {

    HashMap<String,Token> symbolTable = new HashMap<>();
    /**
     * Insert an element into the symbolTable with symbol counting as key
     *
     * @param symbol
     * @param dataType
     *
     */
    public void insert(String symbol, Token dataType) {
        if (!lookup(symbol)) {
            symbolTable.put(symbol,dataType);
        }
    }

    /**
     *
     * @param symbol
     * @return
     */
    public boolean lookup(String symbol) {
        boolean result;
        result = symbolTable.containsKey(symbol);
        return result;
    }

    /**
     *
     * @param symbol
     * @return
     */
    public char lookupDataType(String symbol) {
        Token result = symbolTable.get(symbol);
        return result.dataType;
    }
    
    public Token lookupToken(String symbol){
        return symbolTable.get(symbol);
    }
    
    public int lookupLocation(String symbol){
        Token result = symbolTable.get(symbol);
        return result.location;
    }

}

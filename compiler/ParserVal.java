package compiler;

public class ParserVal {

    /**
     * integer value of this 'union'
     */
    public int ival;

    /**
     * object value of this 'union'
     */
    public Object obj;

    /**
     * Initialize me without a value
     */
    public ParserVal() {
    }

    /**
     * Initialize me as an int
     *
     * @param val
     */
    public ParserVal(int val) {
        ival = val;
    }

    /**
     * Initialize me as an Object
     *
     * @param val
     */
    public ParserVal(Object val) {
        obj = val;
    }
}//end class


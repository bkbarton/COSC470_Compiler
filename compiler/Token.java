/*
 * 
 */
package compiler;

/**
 *
 * @author Barton
 */
public class Token {

    public int token;
    public String sequence;
    public int location;
    public char dataType;
    
    public Token(int token, String sequence) {
        super();
        this.token = token;
        this.sequence = sequence;
    }

    public Token(int token, String sequence, int location){
        super();
        this.token = token;
        this.sequence = sequence;
        this.location = location;
    }
        public Token(int token, String sequence, int location, char dataType){
        super();
        this.token = token;
        this.sequence = sequence;
        this.location = location;
        this.dataType = dataType;
    }
    
        public Token (int token,char dataType, int location){
        super();
        this.token = token;
        this.location = location;
        this.dataType = dataType;
        }
    public Token() {
        token = 0;
        sequence = "-1";
    }
}

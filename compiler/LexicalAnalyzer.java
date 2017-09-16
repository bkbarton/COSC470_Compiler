 /*
 * This will be respobible for generating tokens based off input 
 */
package compiler;

import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.nashorn.internal.runtime.ParserException;

/**
 *
 * @author Benjamin
 */
public class LexicalAnalyzer {

    private LinkedList<TokenInfo> tokenInfos;
    public Token token;
    public String input;

    /**
     *
     * @param line
     */
    public LexicalAnalyzer(String line) {
        tokenInfos = new LinkedList<>();
        input = line;
        
    }

    /**
     *
     * @param regex
     * @param token
     */
    public void add(String regex, int token) {
        tokenInfos.add(new TokenInfo(Pattern.compile("^(" + regex + ")"), token));
    }

    /**
     *
     *
     * @return
     */
    public Token createToken() {
        
        boolean match = false;//was false
        for (TokenInfo info : tokenInfos) {
            Matcher m = info.regex.matcher(input);
            if (m.find()) {
               
                //make tokens for space, tabs and end of line, check them before breaking
                String tok = m.group().trim();
                token = (new Token(info.token, tok));
                input = m.replaceFirst("");
                if (!(token.token < 0)) {
                    match = true;
                    //System.out.println(token.token);
                    break;
                }

            }
        }
        if (!match) {
            throw new ParserException(
                    "Lexical Error");
        }

        return token;
    }

    public void setTokens() {
        add("[\\s]+", -1);
        add("[\\t]", -2);
        add("[\\n]", -3);
        
        add("public", 257);
        add("private", 258);

        add("static", 259);
        add("void", 260);

        add("[A-Z][A-Z0-9_]*", 261);

        add("\\(", 262);
        add("\\)", 263);

        add("\\{", 264);

        add("var", 265);

        add("if", 266);

        add("get", 267);
        add("put", 268);

     

        add("\\}", 270);
        add(",", 271);
        add(":", 272);

        add("\\$", 273);
        add("-?[0-9]+", 274);
        add("true", 275);
        add("false", 276);

        add("char", 277);
        add("int", 278);
        //literal
        add("'[\\x30-\\x39 | \\x41-\\x5A | \\x61-\\x7A]'", 279);

        add("\\+", 280);
        add("\\-", 281);

        add(">=", 283);
        add("<=", 285);
        add("<>", 287);
        add(">", 282);
        
        add("==", 284);
        
        add("<", 286);
       

        add(";", 288);

        add("\\*", 289);
        add("/", 290);
        add("\\%", 291);
        add("=", 269);
    }
}

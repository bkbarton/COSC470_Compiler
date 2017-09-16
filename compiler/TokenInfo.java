/*
 * 
 */
package compiler;

import java.util.regex.Pattern;

/**
 *
 * @author Barton
 */
public class TokenInfo {

    public final Pattern regex;
    public final int token;

    /**
     *
     * @param regex
     * @param token
     */
    public TokenInfo(Pattern regex, int token) {
        super();
        this.regex = regex;
        this.token = token;
    }

}

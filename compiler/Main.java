/*
 * 
 */
package compiler;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author Barton
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String fileName = "input.txt";
        if((args.length) != 0){
            fileName = args[0];
        }
        System.out.println(fileName);
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        String result = "";
        while (scanner.hasNextLine()) {
            String temp = "";
            temp = scanner.nextLine().trim();
           
            if (!temp.contains("//")) {
                result += temp.trim();
            }else{
                int right = temp.indexOf("//");
                temp = temp.substring(0, right);
                result += temp.trim();
            }
        }
       
        Parser parse = new Parser();
        parse.yyparse(result);

    }
}

package pt.uc.dei.student.others;

import java.util.HashMap;

public class Utilitary {
    public static HashMap<String,String> parseMessage(String msg){
        HashMap<String,String> hash = new HashMap<String,String>();
        String[] dividedMessage = msg.split(" ; ");
        for(String token : dividedMessage){
            String[] keyVal = token.split(" \\| ");
            if(keyVal.length == 2){
                hash.put(keyVal[0], keyVal[1]);
            }else{
                System.out.println("Error with tokens");
            }
        }
        return hash;
    }
}

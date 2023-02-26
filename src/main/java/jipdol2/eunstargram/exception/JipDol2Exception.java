package jipdol2.eunstargram.exception;

import java.util.HashMap;
import java.util.Map;

public abstract class JipDol2Exception extends RuntimeException{

    public final Map<String,String> validation = new HashMap<>();

    public JipDol2Exception(String message){
        super(message);
    }

    public JipDol2Exception(String message,Throwable cause){
        super(message,cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName,String message){
        validation.put(fieldName,message);
    }
}

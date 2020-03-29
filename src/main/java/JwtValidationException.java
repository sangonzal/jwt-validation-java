public class JwtValidationException extends RuntimeException {

    JwtValidationException(String message, Throwable ex){
        super(message, ex);
    }

    JwtValidationException(String message){
        super(message);
    }

}

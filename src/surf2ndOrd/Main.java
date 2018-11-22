
package surf2ndOrd;

/**
 * Exception class that throws when error in quadratic equation exist
 * @author alexa4
 */
class QuadrEqualException extends Exception{
    String error;
    QuadrEqualException(String s){
        error = s;
    }
    public String toString(){
        return error;
    }
}

public class Main {
    public static void main(String args[]){
        WelcomeFrame.go(args);
    }
}

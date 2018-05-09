
/**
 * Main file of creating project
 */ 
package surf2ndOrd;

import org.lwjgl.LWJGLException;

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
        WelcomeFramee.go(args);
    }
}

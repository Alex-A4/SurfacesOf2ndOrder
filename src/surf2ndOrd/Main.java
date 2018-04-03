
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
        WelcomeFrame wf = new WelcomeFrame();
        wf.setLocation(200, 200);
        wf.setVisible(true);
    }
}


/**
 * Examples of surface:
 * 1.(0f, 1f, -1f, 1f, 0f, 0f, 0f, 0f, 15f, 0f)
 * 2.(4f, 5f, 2f, 0f, 3f, 5f, 30f, 0f, 5f, -1f)
 * 3.(1f, 2f, -3, 0f, 0f, 0f, 2f, 8f, 18f, -54f)
 * 4.(4f, 7f, 1, 0f, 0f, 5f, 0f, 0f, 0f, -1f)
 * 5.(4f, 2f, 2f, 0f, 0f, 5f, 0f, 0f, 5f, -1f)
 * 6.(4f, -5f, 2f, 0f, 3f, 5f, 30f, 0f, 5f, -1f)
 * 7.(4f, 2f, 2f, 0f, 0f, 5f, 10f, 0f, 5f, -1f)
 * 
 * Examples of cutting plane:
 * (1, 1, -1, 0)
 */

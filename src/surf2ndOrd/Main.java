
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
      
       // For some tests 2D, comment 3 last lines
//        try{
//            CurvesOf2ndOrder curve;
//            curve = new CurvesOf2ndOrder(-5, 2, 1, 2, 0, 0, 5, 0, 2, -24);
//            curve.setPlaneCoef(1, 1, 1, 0);
//            curve.createDisplay();
//            curve.start();
//        }catch(LWJGLException ex){
//            System.out.println(ex);
//        }
        
        WelcomeFrame wf = new WelcomeFrame();
        wf.setLocation(200, 200);
        wf.setVisible(true);
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package surf2ndOrd;

import org.lwjgl.LWJGLException;

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
        try{
            //ax^2 + by^2 + cz^2 + d2xy + e2yz + f2xz + g2x + h2y + i2z + j = 0
            Surface2ndOrder s2 = new Surface2ndOrder(1f, 2f, -3, 0f, 0f, 0f, 2f, 8f, 18f, -54f);
            s2.setPlaneCoef(1, 1, -1, 0);
            s2.setDisplay();
            s2.start();
        }catch(LWJGLException e){
            e.printStackTrace();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
    }
}


/**
 * Examples:
 * 1.(0f, 1f, -1f, 1f, 0f, 0f, 0f, 0f, 15f, 0f)
 * 2.(4f, 5f, 2f, 0f, 3f, 5f, 30f, 0f, 5f, -1f)
 * 3.(1f, 2f, -3, 0f, 0f, 0f, 2f, 8f, 18f, -54f)
 * 4.(4f, 7f, 1, 0f, 0f, 5f, 0f, 0f, 0f, -1f)
 * 5.(4f, 2f, 2f, 0f, 0f, 5f, 0f, 0f, 5f, -1f)
 * 6.(4f, -5f, 2f, 0f, 3f, 5f, 30f, 0f, 5f, -1f)
 * 7.(4f, 2f, 2f, 0f, 0f, 5f, 10f, 0f, 5f, -1f)
 */

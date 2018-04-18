/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package surf2ndOrd;

/**
 *
 * @author alexa4
 */
public class Matrix {
    public Vector a1, a2, a3;
    
    Matrix(Vector a1, Vector a2, Vector a3){
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
    }
    
    public Matrix transposeMatrix(){
        Vector newa1 = new Vector(a1.getX(), a2.getX(), a3.getX());
        Vector newa2 = new Vector(a1.getY(), a2.getY(), a3.getY());
        Vector newa3 = new Vector(a1.getZ(), a2.getZ(), a3.getZ());
        return new Matrix(newa1, newa2, newa3);
    }
    
    /**
     * (Matr) * (This)
     * @param matr
     * @return 
     */
    public Matrix multiple(Matrix matr){
        Vector V1, V2, V3;
        
        V1 = new Vector(matr.a1.getX()*a1.getX() + matr.a2.getX()*a1.getY() + matr.a3.getX()*a1.getZ(),
                matr.a1.getY()*a1.getX() + matr.a2.getY()*a1.getY() + matr.a3.getY()*a1.getZ(),
                matr.a1.getZ()*a1.getX() + matr.a2.getZ()*a1.getY() + matr.a3.getZ()*a1.getZ()
        );
        
        V2 = new Vector(matr.a1.getX()*a2.getX() + matr.a2.getX()*a2.getY() + matr.a3.getX()*a2.getZ(),
                matr.a1.getY()*a2.getX() + matr.a2.getY()*a2.getY() + matr.a3.getY()*a2.getZ(),
                matr.a1.getZ()*a2.getX() + matr.a2.getZ()*a2.getY() + matr.a3.getZ()*a2.getZ()
        );
        
        V3 = new Vector(matr.a1.getX()*a3.getX() + matr.a2.getX()*a3.getY() + matr.a3.getX()*a3.getZ(),
                matr.a1.getY()*a3.getX() + matr.a2.getY()*a3.getY() + matr.a3.getY()*a3.getZ(),
                matr.a1.getZ()*a3.getX() + matr.a2.getZ()*a3.getY() + matr.a3.getZ()*a3.getZ()

        );
        
        return new Matrix(V1, V2, V3);
    }
}

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
public class Vector {
    private float x, y, z;
    
    Vector(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    /**
     * The vector multiple
     * @param vect vector to multiple
     * @return new Vector which orthogonal both of which are multiplied
     */
    public Vector vectorsMultiple(Vector vect){
        float x = this.y * vect.z - this.z * vect.y;
        float y = this.z * vect.x - this.x * vect.z;
        float z = this.x * vect.y - this.y * vect.x;
        return new Vector(x, y, z); 
    }
    
    /**
     * The vector multiple modulo
     * @param vect vector to multiple
     * @return Square of parallelogram
     */
    public float vectorsSquare(Vector vect){
        return this.vectorsMultiple(vect).vectorLength();
    }
    
    /**
     * @return the length of vector
     */
    public float vectorLength(){
        return (float)Math.sqrt(this.x*this.x + this.y*this.y + this.z*this.z);
    }
    
    /**
     * Division all coordinates of vector on his length
     */
    public void normirOfVect(){
        float len = this.vectorLength();
        this.x = x/len;
        this.y = y/len;
        this.z = z/len;
    }
    
    /**
     * @return this x
     */
    public float getX(){
        return this.x;
    }
    
    /**
     * @return this y
     */
    public float getY(){
        return this.y;
    }
    
    /**
     * @return this z
     */
    public float getZ(){
        return this.z;
    }
}

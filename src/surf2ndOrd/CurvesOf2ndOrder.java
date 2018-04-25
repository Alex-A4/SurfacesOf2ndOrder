
package surf2ndOrd;

import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 *
 * @author alexa4
 */
public class CurvesOf2ndOrder {
    //Coefficients of surface equation
    float a, b, c, d, e, f, g, h, i, j;
    //Displacement of the scene on coordinate axis
    private static float dx = 0f, dy = 0f, dz = -6f;
    //Coefficients of plane equation
    private static float pA, pB, pC, pD;
    //Lists of points of curve
    private static ArrayList <Point> curvePoints;
    //Display size
    private static final int width = 680, height = 480;
    //Range of values on axis
    private static float range = 10f;
    //Increment of a function
    private static float incrOfFunc = 0.005f;
        
    /**
     * Constructor with all coefficients of surface
     * @param a coefficient for x^2
     * @param b coefficient for y^2
     * @param c coefficient for z^2
     * @param d coefficient for x*y
     * @param e coefficient for y*z
     * @param f coefficient for x*z 
     * @param g coefficient for x
     * @param h coefficient for y
     * @param i coefficient for z
     * @param j free coefficient
     */
    CurvesOf2ndOrder(float a, float b, float c, float d, float e, float f,
            float g, float h, float i, float j){
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        this.g = g;
        this.h = h;
        this.i = i;
        this.j = j;
    }

       
    /**
     * creating display with base parameters for 3D graphic
     * @throws LWJGLException if display couldn't be create
     */
    public void createDisplay()throws LWJGLException{
        Display.setDisplayMode(new DisplayMode(this.width, this.height));
        Display.setLocation(0, 200);
        Display.setTitle("Curve of the 2nd order ");
        Display.create();
        
        curvePoints = new ArrayList<Point>();
        //planePoints = new ArrayList<Point>();
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        //очистка экрана в какой-то цвет
        glClearColor(1f, 1f, 1f, 1);
        //разрешить очистку буфера глубины
        glClearDepth(1.0);
        //тип текста глубины
        glDepthFunc(GL_LESS);
        //Улучшение вычислений перспективы
        glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); 
        glEnable(GL_DEPTH_TEST);    
        //разрешить плавное цветовое сглаживание
        glShadeModel(GL_SMOOTH);
        //выбор матрицы проекции
        gluPerspective(45, width/(height*1f), 0.001f, 100f);
        glMatrixMode(GL_MODELVIEW);
    }
    
    
    /**
     * Infinity cycle with drawing methods
     * @throws InterruptedException 
     */
    public void start(){
        calcNewCoefs();
        fillPointsCur();
        
        while(!Display.isCloseRequested()){
            processInput();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            
            glTranslatef(dx, dy, dz);
            glRotatef(90,1.0f,0.0f,0.0f);
            
            drawAxis();
            
            drawCurve();
            
            Display.sync(60);
            Display.update();
        }
        
        Display.destroy();
    }
    
    
    /**
     * Filling Lists of points after calculating them
     */
    private void fillPointsCur(){
        
        for (float x = -range; x <= range; x += incrOfFunc){
                try{
                    float y[] = solutOfQuadEquat2D(x);
                    if (y.length == 2){
                        CurvesOf2ndOrder.curvePoints.add(new Point(x,y[0], 0));
                        CurvesOf2ndOrder.curvePoints.add(new Point(x, y[1], 0));
                    } else if (y.length == 1)
                        CurvesOf2ndOrder.curvePoints.add(new Point(x, y[0], 0));
                } catch(QuadrEqualException e){
                }
            }
    }
    
    
    /**
     * Setting coefficients of cutting plane
     * @param a coefficient for X
     * @param b coefficient for Y
     * @param c coefficient for Z
     * @param d free coefficient
     */    
    public void setPlaneCoef(float a, float b, float c, float d){
        this.pA = a;
        this.pB = b; 
        this.pC = c;
        this.pD = d;
    }
    
    
    /**
     * Calculating new coefficients by transition matrix
     */
    private void calcNewCoefs(){
        Matrix strMatrix, invMatrix, matrixA;
        Vector V1, V2, V3, vectorA;
        
        V1 = new Vector (pA,pB,pC);
        V1.normirOfVect();
        
        V2 = V1.vectorsMultiple(new Vector (1, 0, 0));
        if (V2.vectorLength() == 0)
            V2 = V2.vectorsMultiple(new Vector(0, 1, 0));
        V2.normirOfVect();
        
        V3 = V1.vectorsMultiple(V2);
        V3.normirOfVect();
        
        strMatrix = new Matrix(V1, V2, V3);
        invMatrix = strMatrix.transposeMatrix();
        
        /**
         * Matrix A filling by numbers of surface
         * A D E
         * D B F
         * E F C
         */
        V1 = new Vector(a, d, e);
        V2 = new Vector(d, b, f);
        V3 = new Vector(e, f, c);
        matrixA = new Matrix(V1, V2, V3);
        //P^T*A*P - matrix with new coefficinents
        matrixA = strMatrix.multiple(matrixA.multiple(invMatrix));
        //vect * strMatrix - vector with new coefficients
        vectorA = strMatrix.multipleOnVector(new Vector(g, h, i));
        
        //Set new coefficients
        this.a = matrixA.a1.getX();
        this.b = matrixA.a2.getY();
        this.c = this.e = this.f = this.i = 0;
        this.d = matrixA.a2.getX();
        this.g = vectorA.getX();
        this.h = vectorA.getY();
    }
      
    
   /**
     * Drawing coordinate axis
     */
    private void drawAxis(){
        // x - blue, y - red, z - green
        glColor3f(0f, 1f, 0f);
        glBegin(GL_LINES);
        glVertex3f(0f, 0f, -4f);
        glVertex3f(0f, 0f, 4f); //z
        glEnd();
        
        glColor3f(0f, 0f, 1f);
        glBegin(GL_LINES);
        glVertex3f(-4f, 0f, 0f);
        glVertex3f(4f, 0f, 0f); //x
        glEnd();
    }
    
    
    private void drawCurve(){
        for (Point p: curvePoints)
            p.drawPoint();
    }
    
    
    /**
     * The solution of quadratic equation in 2D
     * y^2*(b + c*pB^2/pC^2 - pB*e/pC) + y*(d*x + h + (2pA*pB*x*c + 
     * + 2pD*pB*c)/pC^2 - (pA*x*e + pD*e + pB*x*f + pB*i)/pC) + (a*x^2 + g*x + j + 
     * + (pA^2*x^2*c + 2pD*pA*c + pD^2*c)/pC^2 - (pA*x^2*f + f*x*pD
     * + pA*x*i + pD*i)/pC) = 0
     * 
     * float A = b + c*pB*pB/(pC*pC) - pB*e/pC;
        float B = d*x + h + (2*pA*pB*x*c + 2*pD*pB*c)/(pC*pC) - 
                (pA*x*e + pD*e + pB*x*f + pB*i)/pC;
        float C = a*x*x + g*x + j + (pA*pA*x*x*c + 2*pD*pA*x*c + pD*pD*c)/(pC*pC)
                - (pA*x*x*f + f*x*pD + pA*x*i + pD*i)/pC;
     * @param x is abscissa
     * @return solution of quadratic equation
     */
    private float[] solutOfQuadEquat2D(float x) throws QuadrEqualException{
        float[] res;
        float A = b;
        float B = d*x + h;
        float C = a*x*x + g*x + j;
        
        if (A == 0 && B == 0)
            throw new QuadrEqualException("A and B is zero");
        if (A == 0){    
            res = new float[1];
            res[0] = -C/B;
        } else{
            float D = B*B - 4*A*C;
            if (D < 0)
                throw new QuadrEqualException("Discriminant less then zero");
            else if (D == 0){
                res = new float[1];
                res[0] = (-B)/(2*A);
            } else{
                res = new float[2];
                res[0] = (-B-(float)Math.sqrt(D))/(2*A);
                res[1] = (-B+(float)Math.sqrt(D))/(2*A);
            }
        }
        return res;
    }
    
    
    /**
     * Processing keyboard and mouse events
     */
    private void processInput(){
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
            dx += 0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
            dx -= 0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
            dy -= 0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            dy += 0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_O))
            dz += 0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_L))
            dz -= 0.1;
    }
   
    
    
    /**
     * x, y is coordinates of each point
     * drawPoint() is method to painting colorful point
     * callPoint() is method to return Vertex to drawing
     */
    class Point{
        private float x, y, z;
        
        private void drawPoint(){
            glColor3f(x/10, z/10, y/10); 
            glBegin(GL_POINTS);
            glVertex3f(x/2, z/2, y/2);
            glEnd();
        }
        
        Point(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    float a, b, d, g, h, j;
    //Displacement of the scene on coordinate axis
    private static float dx = 0f, dy = 0f;
    //Coefficients of plane equation
    private static float pA, pB, pC;
    //Lists of points of surface and plane
    private static ArrayList <Point> curvePoints;
    private static ArrayList <Point> planePoints;
    //Display size
    private static final int width = 680, height = 480;
    //Range of values on axis
    private static float range = 15f;
    //Increment of a function
    private static float incrOfFunc = 0.001f;
    
        
    /**
     * Constructor with all coefficients of surface
     * @param a coefficient for x^2
     * @param b coefficient for y^2
     * @param d coefficient for x*y 
     * @param g coefficient for x
     * @param h coefficient for y
     * @param j free coefficient
     */
    CurvesOf2ndOrder(float a, float b, float d, float g, float h, float j){
        this.a = a;
        this.b = b;
        this.d = d;
        this.g = g;
        this.h = h;
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
        planePoints = new ArrayList<Point>();
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
        
        fillPointsCur();
        
        while(!Display.isCloseRequested()){
            processInput();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            
            glTranslatef(dx, dy, -4f);
            
            drawAxis();
            
            drawCurve();
            
            glColor3f(0f, 0f, 0f);
            glBegin(GL_LINES);
            for (Point p: planePoints)
                p.callPoint();
            glEnd();
            
            Display.sync(60);
            Display.update();
        }
        
        Display.destroy();
    }
    
    /**
     * Filling Lists of points after calculating them
     */
    private void fillPointsCur(){
        float maxX, minX;
        maxX = -range;
        minX = range;
        for (float x = -range; x <= range; x += incrOfFunc){
                try{
                    float y[] = solutOfQuadEquat2D(x);
                    if (x < minX) minX = x;
                    else if (x > maxX) maxX = x;
                    if (y.length == 2){
                        CurvesOf2ndOrder.curvePoints.add(new Point(x,y[0]));
                        CurvesOf2ndOrder.curvePoints.add(new Point(x, y[1]));
                    } else if (y.length == 1)
                        CurvesOf2ndOrder.curvePoints.add(new Point(x, y[0]));
                } catch(QuadrEqualException e){
                }
            }
        
        //(maxX,maxY) -> (minX, maxY) -> (minX, minY) -> (maxX, minY)
        try{
            float yP = solutEquat2D(maxX);
            CurvesOf2ndOrder.planePoints.add(new CurvesOf2ndOrder.Point(maxX, yP));
            yP = solutEquat2D(minX);
            CurvesOf2ndOrder.planePoints.add(new CurvesOf2ndOrder.Point(minX, yP));
        }catch (NullPointerException ex){
            
        }
    }
    
    
    /**
     * Setting coefficients of cutting plane
     * @param a coefficient for X
     * @param b coefficient for Y
     * @param d free coefficient
     */    
    public void setPlaneCoef(float a, float b, float c){
        this.pA = a;
        this.pB = b; 
        this.pC = c;
    }
    
      
    /**
     * Drawing coordinate axis
     */
    private void drawAxis(){
        // x - blue, y - red, z - green
        glColor3f(0f, 1f, 0f);
        glBegin(GL_LINES);
        glVertex3f(0f, -3f,0f);
        glVertex3f(0f, 3f, 0f); //y
        glEnd();
        
        glColor3f(1f, 0f, 0f);
        glBegin(GL_LINES);
        glVertex3f(-3f, 0f, 0f);
        glVertex3f(3f, 0f, 0f); //x
        glEnd();
    }
    
    private void drawCurve(){
        for (Point p: curvePoints)
            p.drawPoint();
    }
    
    /**
     * The solution of quadratic equation in 2D
     * By^2 + y(2Dx + 2H) + (Ax^2 + 2Gx + J) = 0
     * @param x is abscissa
     * @return solution of quadratic equation
     */
    private float[] solutOfQuadEquat2D(float x) throws QuadrEqualException{
        float[] res;
        float A = this.b;
        float B = this.d*x + this.h;
        float C = this.a*x*x + this.g*x + this.j;
        
        if (A == 0 && B == 0)
            throw new QuadrEqualException("A and B is zero");
        if (A == 0){
            res = new float[1];
            res[0] = -C/B;
        } else if (B == 0){
            res = new float[2];
            res[0] = (float)Math.sqrt(C/A);
            res[1] = -(float)Math.sqrt(C/A);
        } else{
            float D = B*B - 4*A*C;
            if (D < 0)
                throw new QuadrEqualException("Discriminant less then zero");
            else if (D == 0){
                res = new float[1];
                res[0] = (-B-(float)Math.sqrt(D))/2*A;
            } else{
                res = new float[2];
                res[0] = (-B-(float)Math.sqrt(D))/2*A;
                res[1] = (-B+(float)Math.sqrt(D))/2*A;
            }
        }
        return res;
    }
    
    
    /**
 * The solution of equation in 2D
 * Ax + By + C = 0
 * y = -(Ax + C)/B
 * @param x is abscissa
 * @return coefficient y
 */
    private float solutEquat2D(float x) throws NullPointerException{
        float y = -(pA*x + pC) / pB;
        return y;
    }
    
     
    /**
     * Processing keyboard and mouse events
     */
    private void processInput(){
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
            dx +=0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
            dx -=0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
            dy -=0.1;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            dy +=0.1;
        /*if (Mouse.isInsideWindow()){
            float dw = Mouse.getDWheel()/100;
            if (dw < 0){
                dz += dw;
                if (Mouse.getX() > width/2)
                    dx += 0.3f;
                else dx -= 0.3f;
                if (Mouse.getY() > height/2)
                    dy += 0.3f;
                else dy -= 0.3f;
            }
            if (dw > 0){
                dz += dw;
                if (Mouse.getX() > width/2)
                    dx -= 0.3f;
                else dx += 0.3;
                if (Mouse.getY() > height/2)
                    dy -= 0.3f;
                else dy += 0.3f;
            }
        }*/
            
    }
   
    
    
    /**
     * x, y is coordinates of each point
     * drawPoint() is method to painting colorful point
     * callPoint() is method to return Vertex to drawing
     */
    class Point{
        private float x, y;
        private void drawPoint(){
            glColor3f(x/10,x/20+y/20, y/10); 
            glBegin(GL_POINTS);
            glVertex3f(x/10, y/10, 0f);
            glEnd();
        }
        private void callPoint(){
            glVertex3f(x/10, y/10, 0f);
        }
        Point(float x, float y){
            this.x = x;
            this.y = y;
        }
    }
    
}

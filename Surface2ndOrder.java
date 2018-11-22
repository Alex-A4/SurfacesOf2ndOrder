
/**
 * File contain class Surface2ndOrder that implements 3D rendering surface
 * of the 2-nd order and cutting plane
 */
package surf2ndOrd;

import java.util.ArrayList;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

/**
 * Class of surface of the 2-nd order that implements 3D rendering
 * @author alexa4
 */
public class Surface2ndOrder {
    //Displacement of the scene on coordinate axis
    private static float dx = -1.5f, dy = 0, dz = -6.0f;
    //Turns on axis
    private static float XAngle = 0f, YAngle = 0f;
    //Coefficients of sufrace equation
    private static float a, b, c, d, e, f, g, h, i, j;
    //Coefficients of plane equation
    private static float pA, pB, pC, pD;
    //Lists of points of surface and plane
    private static ArrayList <Point> surfPoints;
    private static ArrayList <Point> planePoints;
    //Display size
    private static final int width = 960, height = 680;
    //Range of values on axis
    private static float range = 10f;
    //Increment of a function
    private static float incrOfFunc = 0.05f;
    
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
    Surface2ndOrder(float a, float b, float c, float d, float e, 
            float f, float g, float h, float i, float j) {
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
     * creating display with base parameters for 3D graphic
     * @throws LWJGLException if display couldn't be create
     */
    public void createDisplay()throws LWJGLException{
        Display.setDisplayMode(new DisplayMode(this.width, this.height));
        Display.setTitle("Surfaces of the 2nd order ");
        Display.create();
        
        surfPoints = new ArrayList<Point>();
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
        
        fillPoints();
        
        while(!Display.isCloseRequested()){
            processInput();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glLoadIdentity();
            
            glTranslatef(dx, dy, dz);
            glRotatef(YAngle,0.0f,1.0f,0.0f);
            glRotatef(XAngle,1.0f,0.0f,0.0f);
            
            drawAxis();
            
            for (Point p: surfPoints)
                p.drawPoint();
            
            glColor3f(0.5f, 0.5f, 0.5f);
            glBegin(GL_QUADS);
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
    private void fillPoints(){
        float maxX, maxY, minX, minY;
        maxX = maxY = -range;
        minX = minY = range;
        for (float x = -range; x <= range; x += incrOfFunc)
            for (float y = -range; y <= range; y += incrOfFunc){
                try{
                    float z[] = solutOfQuadEquat3D(x, y);
                    if (x < minX) minX = x;
                    else if (x > maxX) maxX = x;
                    if (y < minY) minY = y;
                    else if (y > maxY) maxY = y;
                    if (z.length == 2){
                        surfPoints.add(new Point(x, y, z[0]));
                        surfPoints.add(new Point(x, y, z[1]));
                    } else if (z.length == 1)
                        surfPoints.add(new Point(x, y, z[0]));
                } catch(QuadrEqualException e){
                }
            }
        
        //(maxX,maxY) -> (minX, maxY) -> (minX, minY) -> (maxX, minY)
        float zP = solutEquat3D(maxX, maxY);
        planePoints.add(new Point(maxX, maxY, zP));
        zP = solutEquat3D(minX, maxY);
        planePoints.add(new Point(minX, maxY, zP));
        zP = solutEquat3D(minX, minY);
        planePoints.add(new Point(minX, minY, zP));
        zP = solutEquat3D(maxX, minY);
        planePoints.add(new Point(maxX, minY, zP));
    }
    
    /**
     * Drawing coordinate axis
     */
    private void drawAxis(){
        // x - blue, y - red, z - green
        glColor3f(0f, 1f, 0f);
        glBegin(GL_LINES);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 0f, 2f); //z
        glEnd();
        
        glColor3f(1f, 0f, 0f);
        glBegin(GL_LINES);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(0f, 2f, 0f); //y
        glEnd();
        
        glColor3f(0f, 0f, 1f);
        glBegin(GL_LINES);
        glVertex3f(0f, 0f, 0f);
        glVertex3f(2f, 0f, 0f); //x
        glEnd();
    }
    
    /**
     * The solution of quadratic equation in 3D
     * Cz^2 + z(Ey+Fx+I) + (Ax^2+By^2+Dxy+Gx+Hy+J) = 0
     * @param x is abscissa
     * @param y is ordinate
     * @return solution of quadratic equation
     */
    private float[] solutOfQuadEquat3D(float x,float y) throws QuadrEqualException{
        float[] res;
        float A = this.c;
        float B = this.e*y + this.f*x + this.i;
        float C = this.a*x*x + this.b*y*y + this.d*x*y + this.g*x + this.h*y + this.j;
        if (x == 2 && y == 2){
            System.out.println(A+" "+B+" "+C);
        }
            
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
                res[0] = (-B-(float)Math.sqrt(D))/(2*A);
            } else{
                res = new float[2];
                res[0] = (-B-(float)Math.sqrt(D))/(2*A);
                res[1] = (-B+(float)Math.sqrt(D))/(2*A);
            }
        }
        return res;
    }

    
    /**
 * The solution of equation in 3D
 * Cz + Ax + By + D = 0
 * z = -(Ax + By + D)/C
 * @param x is abscissa
 * @param y is ordinate 
 * @return coefficient z
 */
    private float solutEquat3D(float x, float y){
        float z = -(pA*x + pB*y + pD) / pC;
        return z;
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
        if (Mouse.isInsideWindow()){
            if (Mouse.isButtonDown(0))
            {
                YAngle += Mouse.getDX();
                XAngle +=Mouse.getDY();
            }
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
        }
            
    }
    
    /**
     * x, y, z is coordinates of each point
     * drawPoint() is method to painting colorful point
     * callPoint() is method to return Vertex to drawing
     */
    class Point{
        private float x, y, z;
        private void drawPoint(){
            glColor3f(x/10, z/10, y/10); 
            glBegin(GL_POINTS);
            glVertex3f(x/10, z/10, y/10);
            glEnd();
        }
        private void callPoint(){
            glVertex3f(x/10, z/10, y/10);
        }

        Point(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}

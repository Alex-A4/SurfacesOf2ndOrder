/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
 * @author alexa4
 */
public class Surface2ndOrder {
    //Смещение сцены по осям
    private static float dx = -1.5f, dy = 0, dz = -6.0f;
    //Повороты по осям
    private static float XAngle = 0f, YAngle = 0f;
    //Коэффициенты уравнения поверхности
    private static float a, b, c, d, e, f, g, h, i, j;
    //Коэффициенты уравнения плоскости
    private static float pA, pB, pC, pD;
    //Списки точек графика и плоскости
    private static ArrayList <Point> surfPoints;
    private static ArrayList <Point> planePoints;
    //Разрешение дисплея
    private static final int width = 960, height = 680;
    //Диапазон значений по осям
    private static float range = 15f;
    
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
    
    public void setPlaneCoef(float a, float b, float c, float d){
        this.pA = a;
        this.pB = b; 
        this.pC = c;
        this.pD = d;
    }
    
    public void setDisplay()throws LWJGLException{
        Display.setDisplayMode(new DisplayMode(width, height));
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
    
    public void start() throws InterruptedException{  
        
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
                p.drawPoint((byte)1);
            for (Point p: planePoints)
                p.drawPoint((byte)0);
            
            Display.sync(60);
            Display.update();
        }
        
        Display.destroy();
    }
    
    private void fillPoints(){
        for (float x = -range; x <= range; x += 0.01f)
            for (float y = -range; y <= range; y += 0.01f){
                try{
                    float z[] = solutOfQuadEquat(x, y);
                    if (z.length == 2){
                        surfPoints.add(new Point(x, y, z[0]));
                        surfPoints.add(new Point(x, y, z[1]));
                    } else if (z.length == 1)
                        surfPoints.add(new Point(x, y, z[0]));

                    float zP = solutEquat(x, y);
                    planePoints.add(new Point(x, y, zP));
                } catch(QuadrEqualException e){
                }
            }
    }
    
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
     * Cz^2 + z(2Ey+2Fx+2I) + (Ax^2+By^2+2Dxy+2Gx+2Hy+J) = 0
     * @param x is abscissa
     * @param y is ordinate
     * @return solution of quadratic equation
     */
    private float[] solutOfQuadEquat(float x,float y) throws QuadrEqualException{
        float[] res;
        float A = this.c;
        float B = this.e*y + this.f*x + this.i;
        float C = this.a*x*x + this.b*y*y + this.d*x*y + this.g*x + this.h*y + this.j;
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
 * Cz + Ax + By + D = 0
 * @param x 
 * @param y
 * @return coefficient z
 */
    private float solutEquat(float x, float y){
        float z = -(pA*x + pB*y + pD) / pC;
        return z;
    }
    
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
                YAngle -= Mouse.getDX();
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
    
    class Point{
        private float x, y, z;
        private void drawPoint(byte k){
            if (k == 1)
                glColor3f(x/10, z/10, y/10);
            else glColor3f(0f, 0f, 0f);
            
            glBegin(GL_POINTS);
            glVertex3f(x/10, z/10, y/10);
            glEnd();
        }
        Point(float x, float y, float z){
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
}

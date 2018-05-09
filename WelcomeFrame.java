package surf2ndOrd;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import java.util.ArrayList;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import org.lwjgl.LWJGLException;

/**
 * A window frame with user interface 
 * @author alexa4
 */
public class WelcomeFrame extends Application{
    private Surface2ndOrder surf;
    private CurvesOf2ndOrder curve;
    ArrayList<TextArea> surfTexts;
    ArrayList<TextArea> paneTexts;
    ArrayList<Label> surfLabels;
    ArrayList<Label> paneLabels;
    VBox layout;
    
    /**
     * Launching the window
     * @param args 
     */
    public static void go(String[] args){
        launch(args);
    }
    
    /**
     * Drawing the window
     * @param primaryStage is a current window 
     */
    public void start(Stage window){
        window.setTitle("Плоскости второго порядка");
        window.setResizable(false);
        initWindow();
        
        Button bCreate2D = new Button("Построить в проекции");
        bCreate2D.setMinSize(100, 50);
        bCreate2D.setOnAction(e -> {draw2D();});
        Button bCreate3D = new Button("Построить в 3D");
        bCreate3D.setMinSize(100, 50);
        bCreate3D.setOnAction(e->{draw3D();});

        HBox buttonBox = new HBox(25);
        buttonBox.getChildren().addAll(bCreate2D, bCreate3D);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        
        layout.getChildren().add(buttonBox);
        
        Scene scene = new Scene(layout, 820, 250);
        window.setScene(scene);
        window.show();
    }
    
    /**
     * Initializing window objects
     */
    private void initWindow(){
        initTextAreas();
        initLabels();
        
        HBox hbox1, hbox2;
        hbox1 = new HBox();
        for(int i = 0; i < 10; i++)
            hbox1.getChildren().addAll(surfTexts.get(i), surfLabels.get(i));
        
        hbox2 = new HBox();
        for(int i = 0; i < 4; i++)
            hbox2.getChildren().addAll(paneTexts.get(i), paneLabels.get(i));
        
        Label l1, l2, l3;
        l1 = new Label("Конструктор поверхностей второго порядка. Введите коэффициенты и нажмите кнопку \"Построить\"");
        l1.setFont(new Font(15));
        l1.setAlignment(Pos.CENTER_LEFT);
        l2 = new Label("Уравнение поверхности второго порядка:");
        l3 = new Label("Уравнение секущей плоскости:");
        
        layout = new VBox(15);
        layout.getChildren().addAll(l1, l2, hbox1, l3, hbox2);
    }
    
    /**
     * Initializing all 'TextArea'
     */
    private void initTextAreas(){
        surfTexts = new ArrayList<TextArea>();
        surfTexts.add(new TextArea("0"));
        surfTexts.add(new TextArea("0"));
        surfTexts.add(new TextArea("0"));
        surfTexts.add(new TextArea("0"));
        surfTexts.add(new TextArea("0"));
        surfTexts.add(new TextArea("0"));
        surfTexts.add(new TextArea("0"));
        surfTexts.add(new TextArea("0"));
        surfTexts.add(new TextArea("0"));
        surfTexts.add(new TextArea("0"));
        
        for(TextArea s: surfTexts){
            s.setMaxSize(10, 10);
            s.setFont(new Font(10));
            
        }
        
        paneTexts = new ArrayList<TextArea>();
        paneTexts.add(new TextArea("0"));
        paneTexts.add(new TextArea("0"));
        paneTexts.add(new TextArea("0"));
        paneTexts.add(new TextArea("0"));
        
        for(TextArea s: paneTexts){
            s.setMaxSize(10, 10);
            s.setFont(new Font(10));
        }
    }
    
    /**
     * Initializing all 'Label'
     */
    private void initLabels(){
        surfLabels = new ArrayList<Label>();
        surfLabels.add(new Label("x^2+"));
        surfLabels.add(new Label("y^2+"));
        surfLabels.add(new Label("z^2+"));
        surfLabels.add(new Label("xy+"));
        surfLabels.add(new Label("yz+"));
        surfLabels.add(new Label("xz+"));
        surfLabels.add(new Label("x+"));
        surfLabels.add(new Label("y+"));
        surfLabels.add(new Label("z+"));
        surfLabels.add(new Label("=0"));
        
        for(Label l: surfLabels){
            l.setFont(new Font(15));
        }
        
        paneLabels = new ArrayList<Label>();
        paneLabels.add(new Label("x+"));
        paneLabels.add(new Label("y+"));
        paneLabels.add(new Label("z+"));
        paneLabels.add(new Label("=0"));
        
        for(Label l: paneLabels){
            l.setFont(new Font(15));
        }
        
    }
    
    /**
     * Set parameters of surface and draw it
     */
    private void draw3D(){
        float a, b, c, d, e, f, g, h, i, j;
        float A, B, C, D;
        try{
            a = Float.parseFloat(surfTexts.get(0).getText());
            b = Float.parseFloat(surfTexts.get(1).getText());
            c = Float.parseFloat(surfTexts.get(2).getText());
            d = Float.parseFloat(surfTexts.get(3).getText());
            e = Float.parseFloat(surfTexts.get(4).getText());
            f = Float.parseFloat(surfTexts.get(5).getText());
            g = Float.parseFloat(surfTexts.get(6).getText());
            h = Float.parseFloat(surfTexts.get(7).getText());
            i = Float.parseFloat(surfTexts.get(8).getText());
            j = Float.parseFloat(surfTexts.get(9).getText());
            A = Float.parseFloat(paneTexts.get(0).getText());
            B = Float.parseFloat(paneTexts.get(1).getText());
            C = Float.parseFloat(paneTexts.get(2).getText());
            D = Float.parseFloat(paneTexts.get(3).getText());
            
            surf = new Surface2ndOrder(a, b ,c, d, e, f, g, h, i, j);
            surf.setPlaneCoef(A, B, C, D);
            surf.createDisplay();
            surf.start();
            
        }catch(NumberFormatException ex){
            callErrorAlert("Заполните все окна цифрами");
        } catch(LWJGLException ex){
            callErrorAlert("Ошибка при создании окна");
        }
    }
    
    /**
     * Set parameters of curve and draw it
     */
    private void draw2D(){
        float a, b, c, d, e, f, g, h, i, j;
        float A, B, C, D;
        try{
            a = Float.parseFloat(surfTexts.get(0).getText());
            b = Float.parseFloat(surfTexts.get(1).getText());
            c = Float.parseFloat(surfTexts.get(2).getText());
            d = Float.parseFloat(surfTexts.get(3).getText());
            e = Float.parseFloat(surfTexts.get(4).getText());
            f = Float.parseFloat(surfTexts.get(5).getText());
            g = Float.parseFloat(surfTexts.get(6).getText());
            h = Float.parseFloat(surfTexts.get(7).getText());
            i = Float.parseFloat(surfTexts.get(8).getText());
            j = Float.parseFloat(surfTexts.get(9).getText());
            A = Float.parseFloat(paneTexts.get(0).getText());
            B = Float.parseFloat(paneTexts.get(1).getText());
            C = Float.parseFloat(paneTexts.get(2).getText());
            D = Float.parseFloat(paneTexts.get(3).getText());
            
            curve = new CurvesOf2ndOrder(a, b, c, d, e, f, g, h, i, j);
            curve.setPlaneCoef(A, B, C, D);
            curve.createDisplay();
            curve.start();
            
        }catch(NumberFormatException ex){
            callErrorAlert("Заполните все окна цифрами");
        } catch(LWJGLException ex){
            callErrorAlert("Ошибка при создании окна");
        }
    }
    
    /**
     * Dropping an error message 
     * @param message is a text message which will show
     */
    private void callErrorAlert(String message){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.show();
    }
}

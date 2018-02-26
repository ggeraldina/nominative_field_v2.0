/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processArticle_v2;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Geraldina&Fennics
 */
public class Main extends Application {
    private Stage primaryStage;
    
     @Override
    public void start(Stage stage) throws Exception { 
        this.primaryStage = stage;
        this.primaryStage.setTitle("Analysis of the articles");
        // Устанавливаем иконку приложения.
        Image img = new Image("file:icon.png");
        stage.getIcons().add(img);
        Parent root = FXMLLoader.load(getClass().getResource("Articles2.fxml"));        
        Scene scene = new Scene(root);        
        primaryStage.setScene(scene);       
        primaryStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

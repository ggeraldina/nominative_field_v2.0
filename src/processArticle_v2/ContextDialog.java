/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processArticle_v2;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Geraldina&Fennics
 */
public class ContextDialog {
    static public FrequencyOccurrenceTerm curTerm;

    public ContextDialog(FrequencyOccurrenceTerm term) throws Exception {
        curTerm = term;
        Stage stage = new Stage();
        stage.setTitle("Context");
        // Устанавливаем иконку приложения.
        Image img = new Image("file:icon.png");
        stage.getIcons().add(img);
        Parent root = FXMLLoader.load(getClass().getResource("ContextDialog2.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processArticle_v2;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Geraldina&Fennics
 */
public class ContextDialogController implements Initializable {

    @FXML
    TextArea textField;

    @FXML
    Text textFile;

    @FXML
    Text textCount;

    ArrayList<Context> context;

    String termin;

    int count = 0;

    public ContextDialogController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        context = ContextDialog.curTerm.getContext();
        termin = ContextDialog.curTerm.getTerm();
        changeTextArea();
    }

    private void changeTextArea() { 
        int start = 0; 
        textCount.setText(Integer.toString(count + 1)); 
        textField.setText(context.get(count).getContext()); 
        for (String word : textField.getText().split("\\s")) { 
            if (termin.equals(word)) { 
                start = context.get(count).getContext().indexOf(termin, start); 
                textField.replaceText(start, start + termin.length(), termin.toUpperCase()); 
                textField.selectRange(start, start + termin.length()); 
            } 
            start += word.length()+ 1; 
        }; 
        textFile.setText(context.get(count).getFileName()); 
    }

    public void nextButtonHandle() {
        if (count < (context.size() - 1)) {
            count++;
        }
        changeTextArea();
    }

    public void prevButtonHandle() {
        if (count > 0) {
            count--;
        }
        changeTextArea();
    }

}

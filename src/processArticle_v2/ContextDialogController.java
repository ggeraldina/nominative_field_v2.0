/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processArticle_v2;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javafx.scene.control.Button;
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

    @FXML
    Button prevButton;

    @FXML
    Button nextButton;

    List<Context> context;

    String termin;

    int count = 0;

    public ContextDialogController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        context = ContextDialog.curTerm.getContext();
        termin = ContextDialog.curTerm.getTerm();
        deleteDoubles();
        changeTextArea();
    }

    private void deleteDoubles() {
        //Удаление дублей из списка
        Set<Context> hs = new LinkedHashSet<>(context);
        context.clear();
        context.addAll(hs);
    }

    //Найдем и выделим термин в контексте
    private void changeTextArea() {
        int start = 0, countTerm = 0;
        boolean finded = false;
        textCount.setText(Integer.toString(count + 1));
        textField.setText(context.get(count).getContext());
//        if ((termin.split("[ \\s]")).length == 2) {
//            countTerm++;
//            start = context.get(count).getContext().indexOf(termin, start);
//            if (start == -1) {
//                termin = termin.split(" ")[1] + " " + termin.split(" ")[0];
//                start = context.get(count).getContext().indexOf(termin, start);
//            }
//            textField.replaceText(start, start + termin.length(), termin.toUpperCase());
//            textField.selectRange(start, start + termin.length());
//            if (countTerm >= context.get(count).getNumTerm()) {
//                finded = true;
//            }
//        }
        for (String term : termin.split("[ \\s]")) {
            for (String word : textField.getText().split("[ \\s]")) {
                String onlyWord = word.replaceAll("^?[“:‘\"*\\d\\s ,.?!•►]+$?", "");
                if (term.equals(onlyWord)) {
                    countTerm++;
                    start = context.get(count).getContext().indexOf(term, start);
                    textField.replaceText(start, start + term.length(), term.toUpperCase());
                    textField.selectRange(start, start + term.length());
                    if (countTerm >= context.get(count).getNumTerm()) {
                        finded = true;
                        break;
                    }
                }
                start += word.length() + 1;
            }
            start = 0;            
        };
        textFile.setText(context.get(count).getFileName());
        //Добавим кнопкам дизаблед, если нельзя нажать
        updateButton();
    }

    public void updateButton() {
        prevButton.setDisable((count == 0));
        nextButton.setDisable((count == (context.size() - 1)));
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

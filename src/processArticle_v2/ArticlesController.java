/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processArticle_v2;

import java.io.File;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.DirectoryChooser;

/**
 *
 * @author Geraldina&Fennics
 */
public class ArticlesController {

    //
    // поля
    //    
    @FXML
    private MenuItem menuOpen0;

    @FXML
    private MenuItem menuOpen1;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField findAddTextField;

    @FXML
    private Button findAddButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button showContextButton;

    @FXML
    private Button deleteButton2;

    @FXML
    private Button addButton2;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private BarChart<String, Number> barChart;

    @FXML
    private TableView<FrequencyOccurrenceTerm> tableWords;

    @FXML
    private TableColumn<FrequencyOccurrenceTerm, String> columnTerm;

    @FXML
    private TableColumn<FrequencyOccurrenceTerm, String> columnFO0;

//    @FXML
//    private TableColumn<FrequencyOccurrenceTerm, String> columnFO1;
    @FXML
    private TableColumn<FrequencyOccurrenceTerm, String> columnIB0;

    @FXML
    private TableColumn<FrequencyOccurrenceTerm, Boolean> columnTermFromUsers;

    @FXML
    private TableView<FrequencyOccurrenceTerm> tableWords2;

    @FXML
    private TableColumn<FrequencyOccurrenceTerm, String> columnTerm_2;

    @FXML
    private TableColumn<FrequencyOccurrenceTerm, String> columnFO0_2;

//    @FXML
//    private TableColumn<FrequencyOccurrenceTerm, String> columnFO1_2;
    @FXML
    private TableColumn<FrequencyOccurrenceTerm, String> columnIB0_2;

    @FXML
    private TableColumn<FrequencyOccurrenceTerm, Boolean> columnTermFromUsers_2;

    // коллекция терминов
    private CollectionTerms collectionTerms = new CollectionTerms();
    // класс обработки файлов
    private ProcessingArticle article;
    // для отображения контекста
    private FrequencyOccurrenceTerm rowData;
    // список выбранных слов
    private ObservableList<FrequencyOccurrenceTerm> listWords2
            = FXCollections.observableArrayList();

    //
    // конструктор
    //
    public ArticlesController() {
    }

    public FrequencyOccurrenceTerm getRowData() {
        return rowData;
    }

    //
    // методы
    //    
    @FXML
    private void initialize() {
        xAxis.setLabel("");
        yAxis.setLabel("Number");
        // отображение контекста
        tableWords.setRowFactory(tv -> {
            TableRow<FrequencyOccurrenceTerm> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    //Текущий элемент
                    rowData = row.getItem();
                    try {
                        System.out.println(rowData);

                        new ContextDialog(rowData);
                    } catch (Exception e) {
                        System.out.println("Kek O.O" + e.getMessage());
                    }
                }
            });
            return row;
        });

    }

    // показать диаграмму для термина
    private void showDiagramm() {
        barChart.getData().clear();
        int j = 0;
        //barChart.setTitle(changedTerm.getTerm());
        XYChart.Series series0 = new XYChart.Series();
        //XYChart.Series series1 = new XYChart.Series();
        series0.setName(" The frequency of occurrence");
        //series1.setName(" Корпус 2");
        for (FrequencyOccurrenceTerm term : listWords2) {
            int frequencyOccurrence0 = term.getAllFrequencyOccurrence0();
            //int frequencyOccurrence1 = term.getAllFrequencyOccurrence1();
            String frTerm = term.getTerm();
//                    + "\n(near: " 
//                    + term.getFrequencyOccurrenceNear0() + ";\n[radius = 1]: " 
//                    + term.getFrequencyOccurrenceThroughOne0() + ")";
            XYChart.Data data0 = new XYChart.Data(frTerm, frequencyOccurrence0);
            //XYChart.Data data1 = new XYChart.Data(frTerm, frequencyOccurrence1);
            series0.getData().add(data0);
            //series1.getData().add(data1);
        }
        barChart.getData().addAll(series0/*, series1*/);
    }

    @FXML
    private void menuOpenAction0(ActionEvent event) {
        long start = System.currentTimeMillis(); 
        // поиск смысла жизни ... 
        String flagSeriesArticles = "zero";
        // "очистка" коллекции
        collectionTerms = new CollectionTerms();

        DirectoryChooser directChooser = new DirectoryChooser();
        File directoryRoot = directChooser.showDialog(null);
        if (directoryRoot.isDirectory()) {
            article = new ProcessingArticle(directoryRoot, collectionTerms);
            article.readDirectory(flagSeriesArticles);
        } else {
            showAlert("You made a mistake when selecting a folder\nNo files found.");
            return;
        }
        // удалить пользовательские и общеупотребительные слова из коллекции терминов
        article.deleteCommonWordsFromCollection(collectionTerms);
        collectionTerms.sort(flagSeriesArticles);
        // рассчитать индекс яркости
        collectionTerms.calculateIndexBrightness();
        createTable(); // создать таблицу
        tableWords.setItems(collectionTerms.getTermsData());

        createTable2(); // создать таблицу 2
        tableWords2.setItems(listWords2);
        long finish = System.currentTimeMillis(); 
        long timeConsumedMillis = finish - start;
        showAlert("Time of process: " + timeConsumedMillis + " ms");
    }

//    @FXML
//    private void menuOpenAction1(ActionEvent event) {
//        String flagSeriesArticles = "first";
//        
//        DirectoryChooser directChooser = new DirectoryChooser();
//        File directoryRoot = directChooser.showDialog(null);
//        if (directoryRoot.isDirectory()) {
//            article = new ProcessingArticle(directoryRoot, collectionTerms);
//            article.readDirectory(flagSeriesArticles);
//        } else {
//            showAlert("You made a mistake when selecting a folder\nNo files found.");
//            return;
//        }       
//        // удалить пользовательские и общеупотребительные слова из коллекции терминов
//        article.deleteCommonWordsFromCollection(collectionTerms);
//        collectionTerms.sort(flagSeriesArticles);
//        createTable(); // создать таблицу
//        tableWords.setItems(collectionTerms.getTermsData());
//        
//        createTable2(); // создать таблицу 2
//        tableWords2.setItems(listWords2);
//    }
    // создать таблицу
    private void createTable() {
        columnTerm.setCellValueFactory(new PropertyValueFactory("term"));
        columnFO0.setCellValueFactory(new PropertyValueFactory("allFrequencyOccurrence0"));
        // columnFO1.setCellValueFactory(new PropertyValueFactory("allFrequencyOccurrence1"));

        columnIB0.setCellValueFactory(new PropertyValueFactory("indexBrightness"));

        columnTermFromUsers.setCellValueFactory(new PropertyValueFactory("termFromUsers"));
        tableWords.getColumns().setAll(columnTerm, columnFO0/*, columnFO1*/, columnIB0, columnTermFromUsers);
    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {
        int selectedIndex = tableWords.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            FrequencyOccurrenceTerm newFrequency = tableWords.getItems().get(selectedIndex);
            String pathAdd = "Users words to delete.txt";
            String pathDelete = "Users words to add.txt";
            String word = newFrequency.getTerm();
            article.addWordInFile(pathAdd, word);
            article.deleteWordInFile(pathDelete, word);
            collectionTerms.deleteTerm(newFrequency);
        }
    }

    @FXML
    private void handleFindAddButton(ActionEvent event) {
        //Так будет лучше для всех
        String word = findAddTextField.getText();
        handleFindAdd(word);
    }

    private void handleFindAdd(String word) {
        //TODO: Переделать метод, для перевернутого словосочетания) А лучше взять метод readWordsNearby...
        String[] collocation = word.split(" ");
        String reverseWord = "";
        if(collocation.length == 2) { //Удалить после TODO
            reverseWord = collocation[1] + " " + collocation[0];
        }
        if (collectionTerms.haveTerm(word)) {
            FrequencyOccurrenceTerm term = collectionTerms.findTerm(word);
            selectTermInTable(term);
        } else {
            FrequencyOccurrenceTerm term;
            term = article.findUserTerm(word, collectionTerms, "zero");
            if(!"".equals(reverseWord)) { //Удалить после TODO
                    term = article.findUserTerm(reverseWord, collectionTerms, "zero");
            }
            if (term == null) {
                term = article.findUserTerm(word, collectionTerms, "first");
                if(!"".equals(reverseWord)) { //Удалить после TODO
                    term = article.findUserTerm(reverseWord, collectionTerms, "first");
                }
            }
            selectTermInTable(term);
            if (term != null) {
                String pathDelete = "Users words to delete.txt";
                String pathDelete2 = "Most common words.txt";
                String pathAdd = "Users words to add.txt";
                article.addWordInFile(pathAdd, word);
                article.deleteWordInFile(pathDelete, word);
                article.deleteWordInFile(pathDelete2, word);
            } else {
                showAlert("Not found!");
            }
        }
    }

    private void selectTermInTable(FrequencyOccurrenceTerm term) {
        tableWords.requestFocus();
        tableWords.getSelectionModel().select(null);
        tableWords.getSelectionModel().select(term);
        int t = tableWords.getSelectionModel().getSelectedIndex();
        tableWords.getFocusModel().focus(t, columnFO0);
        tableWords.scrollTo(t);
    }

    // создать таблицу с выбранными словами
    private void createTable2() {
        columnTerm_2.setCellValueFactory(new PropertyValueFactory("term"));
        columnFO0_2.setCellValueFactory(new PropertyValueFactory("allFrequencyOccurrence0"));
        //columnFO1_2.setCellValueFactory(new PropertyValueFactory("allFrequencyOccurrence1"));
        columnIB0_2.setCellValueFactory(new PropertyValueFactory("indexBrightness"));
        columnTermFromUsers_2.setCellValueFactory(new PropertyValueFactory("termFromUsers"));
        tableWords2.getColumns().setAll(columnTerm_2, columnFO0_2/*, columnFO1_2*/, columnIB0_2, columnTermFromUsers_2);
    }

    @FXML
    private void handleShowContextButton(ActionEvent event) {
        if (tableWords2.getItems().size() != 2) {
            showAlert("To find the context, the number of rows must be 2");
        } else {
            FrequencyOccurrenceTerm context = article.findContext(tableWords2.getItems().get(0).getTerm(), tableWords2.getItems().get(1).getTerm());
            try {
                new ContextDialog(context);
            } catch (Exception e) {
                System.out.println("Kek O.O" + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDeleteButton2(ActionEvent event) {
        int selectedIndex = tableWords2.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            FrequencyOccurrenceTerm term = tableWords2.getItems().get(selectedIndex);
            listWords2.remove(term);
        }
        showDiagramm();
    }

    @FXML
    private void handleAddButton2(ActionEvent event) {
        int selectedIndex = tableWords.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            FrequencyOccurrenceTerm term = tableWords.getItems().get(selectedIndex);
            listWords2.add(0, term);
        }
        showDiagramm();
    }

    public static void showAlert(String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }
}

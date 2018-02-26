/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processArticle_v2;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

/**
 * класс для обработки одного элемента (файла/ коллекции/ абзаца)
 *
 * @author Geraldina&Fennics
 */
public class ProcessingArticle {

    private static ArrayList<String> synonymsMigrant;
    private static ArrayList<String> mostCommonWords;
    private static ArrayList<String> usersWordsDelete;
    private static ArrayList<String> usersWordsAdd;
    private File directory;
    private CollectionTerms collectionTerms;
    // для отладки
    boolean flag = false;

    // конструктор
    //
    // заполняет поля коллекциями: 
    // синонимов слова; 
    // общеупотребительных слов, которые учитывать не нужно;
    // пользовательских слов, которые учитывать не нужно;
    // 
    ProcessingArticle(File directoryRoot, CollectionTerms collectionTerms1) {
        this();
        directory = directoryRoot;
        collectionTerms = collectionTerms1;
    }

    ProcessingArticle() {
        synonymsMigrant = new ArrayList<>();
        update(1, "Synonyms for the word.txt");
        mostCommonWords = new ArrayList<>();
        update(2, "Most common words.txt");
        usersWordsDelete = new ArrayList<>();
        update(3, "Users words to delete.txt");
        usersWordsAdd = new ArrayList<>();
        update(4, "Users words to add.txt");

    }

    /**
     * Считывает содержимое папки и обрабатывает каждый файл в ней
     *
     * @param flagSeriesArticles номер корпуса статей
     */
    public void readDirectory(String flagSeriesArticles) {
        //File[] files = directory.listFiles();
        //for (File direct : files) {
            File[] dir = directory.listFiles();
            for (File file : dir) {
                if (file.isFile()) {
                    processDocument(file, flagSeriesArticles); // обработать файл из папки
//            } else {
////                 ArticlesController.showAlert("Ooops, there was an error!\n"
////                         + "You made a mistake when selecting a folder\n"
////                         + "No files found.");
////                 return;
                }
            }
        //}
    }

    /**
     * Обрабатывает текущий файл
     *
     * @param file обрабатываемый файл
     * @param flagSeriesArticles номер корпуса статей
     */
    private void processDocument(File file, String flagSeriesArticles) {
        try {
            // Open the file and read its contents into the XWPFDocument object
            XWPFDocument docxFile = new XWPFDocument(OPCPackage.open(file.getAbsolutePath()));
            // Processing sentences
            List<XWPFParagraph> paragraphs = docxFile.getParagraphs();
            String fileName = file.getName();
            String paragraphCurrent;
            String result = "";
            // создать объект класса, помогающего обрабатывать статью
            //ProcessingArticle article = new ProcessingArticle();
            for (XWPFParagraph p : paragraphs) {
                paragraphCurrent = p.getText().toLowerCase();
                // добавить пользовательские слова в коллекцию и удалить их из абзаца после
                paragraphCurrent = addUsersWords(paragraphCurrent, collectionTerms, flagSeriesArticles, fileName);
                // прочитать и выбрать из абзаца нужные термины
                readWordsNearby(paragraphCurrent, collectionTerms, flagSeriesArticles, fileName);
            }
        } catch (Exception ex) {
//            ArticlesController.showAlert("Ooops, there was an error!\n"
//                         + "problem accessing file " + file.getAbsolutePath());                
//            ex.printStackTrace();
//            System.out.println("problem accessing file " + file.getAbsolutePath());
        }
    }

    /**
     * Находит общий контекст для слов
     *
     * @param
     */
    public FrequencyOccurrenceTerm findContext(String word1, String word2) {
        String phrase = word2 + " " + word1;
        FrequencyOccurrenceTerm onlyForContext = new FrequencyOccurrenceTerm(phrase);
        for (File file : directory.listFiles()) {
            if (file.isFile()) {
                try {
                    // Open the file and read its contents into the XWPFDocument object
                    XWPFDocument docxFile = new XWPFDocument(OPCPackage.open(file.getAbsolutePath()));
                    // Processing sentences
                    List<XWPFParagraph> paragraphs = docxFile.getParagraphs();
                    String paragraphCurrent;
                    // создать объект класса, помогающего обрабатывать статью
                    //ProcessingArticle article = new ProcessingArticle();
                    for (XWPFParagraph p : paragraphs) {
                        paragraphCurrent = p.getText().toLowerCase();
                        String[] sentences = paragraphCurrent.split("^?[.?!;]+[\\s]+$?");
                        for (String sentence : sentences) {
                            String[] words = sentence.trim().split("^'|\\s+'|'\\s+|'$|^?[“:\"* \\)\\(”\\d\\s,•►—&%$]+$?");
                            for (int i = 0; i < words.length; i++) {
                                if (word1.equalsIgnoreCase(words[i])) {
                                    if (i != 0 && i != 1) {
                                        if (words[i - 1].equalsIgnoreCase(word2) || words[i - 2].equalsIgnoreCase(word2)) {
                                            addContextForTerm(onlyForContext, paragraphCurrent, file.getName());
                                        }
                                    } else {
                                        if (i != 0 && (words[i - 1].equalsIgnoreCase(word2))) {
                                            addContextForTerm(onlyForContext, paragraphCurrent, file.getName());
                                        }
                                    }
                                    if (i != words.length - 1 && i != words.length - 2) {
                                        if (words[i + 1].equalsIgnoreCase(word2) || words[i + 2].equalsIgnoreCase(word2)) {
                                            addContextForTerm(onlyForContext, paragraphCurrent, file.getName());
                                        }
                                    } else {
                                        if (i != words.length - 1 && words[i + 1].equalsIgnoreCase(word2)) {
                                            addContextForTerm(onlyForContext, paragraphCurrent, file.getName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("problem accessing file " + file.getAbsolutePath());
                }
            }
        }
        return onlyForContext;
    }

    /**
     * Заполняет заданный список словами из файла
     *
     * @param field - номер списка для заполнения
     * @param path - путь к файлу
     */
    private static void update(int field, String path) {
        File fileSynonymsMigrant = new File(path);
        String element = "";
        try {
            Scanner in = new Scanner(fileSynonymsMigrant, "UTF-8");
            while (in.hasNextLine()) {
                element = in.nextLine();
                switch (field) {
                    case 1: {
                        synonymsMigrant.add(element);
                        break;
                    }
                    case 2: {
                        mostCommonWords.add(element);
                        break;
                    }
                    case 3: {
                        usersWordsDelete.add(element);
                        break;
                    }
                    case 4: {
                        usersWordsAdd.add(element);
                        break;
                    }
                }
            }
        } catch (IOException e) {
//            ArticlesController.showAlert("Ooops, there was an error!\n"
//                         + "Input file " + path + " error"); 
//            System.out.println("Input file " + path + " error");
        }
    }

    /**
     * Удаляет пользовательские и общеупотребительные слова из collectionTerms
     *
     * @param collectionTerms - коллекция терминов
     */
    public void deleteCommonWordsFromCollection(CollectionTerms collectionTerms) {
        boolean flagDelete = false;
        int length = collectionTerms.getTermsData().size();
        for (int i = 0; i < length; i++) {
            FrequencyOccurrenceTerm term = collectionTerms.getTermsData().get(i);
            String termCurrent = term.getTerm();
            for (String word : mostCommonWords) {
                if (word.equals(termCurrent)) {
                    collectionTerms.deleteTerm(term);
                    flagDelete = true;
                    i--;
                    length--;
                    break;
                }
            }
            if (!flagDelete) {
                for (String word : usersWordsDelete) {
                    if (word.equals(termCurrent)) {
                        collectionTerms.deleteTerm(term);
                        i--;
                        length--;
                        break;
                    }
                }
            }
            flagDelete = false;
        }
    }

    /**
     * Находит и добавяет в collectionTerms термины, находящиеся на расстоянии
     * 1-2 от "immigrant" и его синонимов
     *
     * @param paragraph - текущий обзац статьи
     * @param collectionTerms - коллекция терминов
     * @param flagSeriesArticles - номер корпуса статей
     * @param fileName - имя файла
     */
    public void readWordsNearby(String paragraph,
            CollectionTerms collectionTerms,
            String flagSeriesArticles,
            String fileName) {
        String[] sentences = paragraph.split("^?[.?!;]+[\\s]+$?");
        for (String sentence : sentences) {
            sentence = sentence.trim();
            String[] words = sentence.split("^'|\\s+'|'\\s+|'$|^?[“:\"* \\)\\(”\\d\\s,•►—&%$]+$?");
            for (String s : synonymsMigrant) {
//                    if(!flag){
//                    ArticlesController.showAlert("Добавил\n"
//                               + words[0]);
//                     flag = true;       
//                    }                
                if (sentence.contains(" " + s + " ")) {
                    for (int i = 0; i < words.length; i++) {
                        if (s.equalsIgnoreCase(words[i])) {
                            if ((i != 1) && (i != 0)) {
                                processWord(collectionTerms, words[i - 2], 1, flagSeriesArticles, sentence, fileName);
                                processWord(collectionTerms, words[i - 1], 0, flagSeriesArticles, sentence, fileName);
                            } else {
                                if (i != 0) {
                                    processWord(collectionTerms, words[i - 1], 0, flagSeriesArticles, sentence, fileName);
                                }
                            }
                            if ((i != words.length - 1)
                                    && (i != words.length - 2)) {
                                processWord(collectionTerms, words[i + 1], 0, flagSeriesArticles, sentence, fileName);
                                processWord(collectionTerms, words[i + 2], 1, flagSeriesArticles, sentence, fileName);
                            } else {
                                if (i != words.length - 1) {
                                    processWord(collectionTerms, words[i + 1], 0, flagSeriesArticles, sentence, fileName);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Добавляет в collectionTerms пользовательские термины и удаляет их из
     * обзаца
     *
     * @param paragraph - текущий обзац статьи
     * @param collectionTerms - коллекция терминов
     * @param flagSeriesArticles - номер корпуса статей
     * @param fileName - имя файла
     * @return обзац без пользовательских терминов
     */
    public String addUsersWords(
            String paragraph,
            CollectionTerms collectionTerms,
            String flagSeriesArticles,
            String fileName) {
        String paragraphNew = "";
        String[] words = paragraph.split("^?[“:\"*\\d\\s,•►]+$?");
        int found = 0;
        for (String wordUsers : usersWordsAdd) {
            String[] collocation = wordUsers.split(" ");
            for (int i = 0; i < words.length; i++) {
                if (words[i].matches(collocation[found] + "[!.?]?")) {
                    found++;
                    if (found == collocation.length) {
                        int wordFromUsers = -1; // -1 пользовательское слово                                         
                        processWord(collectionTerms, wordUsers, wordFromUsers, flagSeriesArticles, paragraph, fileName);
                        if (words[i].equals(wordUsers)) {
                            words[i] = "thisIsWord'sPlace";
                        } else {
                            words[i] = ".";
                        }
                        found = 0;
                    }

                } else {
                    found = 0;
                }
            }
        }
        for (String word : words) {
            paragraphNew += word;
            paragraphNew += " ";
        }
        return paragraphNew;
    }

    /**
     * Добавляет в список терминов введенное слово
     *
     * @param term - слово, которое необходимо найти в статьях
     * @param collectionTerms - коллекция терминов
     * @param flagSeriesArticles - номер корпуса статей
     * @return термин как объкт типа FrequencyOccurrenceTerm
     */
    public FrequencyOccurrenceTerm findUserTerm(String term,
            CollectionTerms collectionTerms,
            String flagSeriesArticles) {
        //term = term.replaceAll(" +", " ");
        String[] collocation = term.split("[ +]");
        int found;
        String textColloc = "";
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                try {
                    XWPFDocument docxFile = new XWPFDocument(OPCPackage.open(file.getAbsolutePath()));
                    List<XWPFParagraph> paragraphs = docxFile.getParagraphs();
                    String paragraphCurrent;
                    for (XWPFParagraph p : paragraphs) {
                        paragraphCurrent = p.getText().toLowerCase();
                        String[] words = paragraphCurrent.split("^?[“:‘\"*\\d\\s ,.?!•►]+$?");
                        found = 0;
                        for (int j = 0; j < words.length; j++) {
                            if (collocation.length == 1) {
                                if (words[j].equals(term)) {
                                    int wordFromUsers = -1; // -1 пользовательское слово
                                    processWord(collectionTerms, term, wordFromUsers, flagSeriesArticles, paragraphCurrent, file.getName());
                                    words[j] = "";
                                }

                            } else {

                                if (words[j].equals(collocation[found])) {
                                    textColloc += words[j] + " ";
                                    found++;
                                } else {
                                    found = 0;
                                }
                                if (found == collocation.length) {
                                    int wordFromUsers = -1; // -1 пользовательское слово
                                    processWord(collectionTerms, term, wordFromUsers, flagSeriesArticles, paragraphCurrent, file.getName());
                                    //words[j] = "";
                                    textColloc = "";
                                    found = 0;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
//                    ArticlesController.showAlert("Ooops, there was an error!\n"
//                         + "problem accessing file " + file.getAbsolutePath()); 
//                    ex.printStackTrace();
//                    System.out.println("problem accessing file " + file.getAbsolutePath());
                }
            } else {
//                ArticlesController.showAlert("You made a mistake when selecting a folder\nNo files found.");
            }
        }
        return collectionTerms.findTerm(term);
    }

    /**
     * Добавляет новый случай встречаемости термина
     *
     * @param collectionTerms - коллекция терминов
     * @param term1 - термин
     * @param distanceToTerm - расстояние до слова, если = -1, то это
     * пользовательское слово (расстояния нет)
     * @param flagSeriesArticles - номер корпуса статей
     * @param paragraph - контекст
     * @param fileName - имя файла *
     */
    private void processWord(CollectionTerms collectionTerms,
            String term1,
            int distanceToTerm,
            String flagSeriesArticles,
            String paragraph,
            String fileName) {
        term1 = term1.replaceAll(" +", " ");
        //String[] collocation = term1.split(" ");
        FrequencyOccurrenceTerm term;
        if (collectionTerms.haveTerm(term1)) {
            term = collectionTerms.findTerm(term1);
        } else {
            if (distanceToTerm == -1) {
                term = new FrequencyOccurrenceTerm(term1, true);
            } else {
                term = new FrequencyOccurrenceTerm(term1);
            }
            collectionTerms.addTerm(term);
        }
        switch (flagSeriesArticles) {
            case "zero":
                switch (distanceToTerm) {
                    case -1:
                    case 0:
                        term.frequencyOccurrenceNearIncrease0();
                        break;
                    case 1:
                        term.frequencyOccurrenceThroughOneIncrease0();
                        break;
                }
                term.allFrequencyOccurrenceIncrease0();
                break;
            case "first":
                switch (distanceToTerm) {
                    case -1:
                    case 0:
                        term.frequencyOccurrenceNearIncrease1();
                        break;
                    case 1:
                        term.frequencyOccurrenceThroughOneIncrease1();
                        break;
                }
                term.allFrequencyOccurrenceIncrease1();
                break;
        }
        addContextWord(collectionTerms, term1, paragraph, fileName);
        collectionTerms.numberContextsIncrease();
    }

    //Я создаю кучу классных методов, чтобы решить интерееееееснейшую задачу) (нет)
    private void addContextForTerm(FrequencyOccurrenceTerm termin,
            String context,
            String fileName) {
        try {
            termin.addContext(context, directory.getName() + ", " + fileName);
        } catch (Exception ex) {
            ArticlesController.showAlert("Ooops, there was an error!\n"
                    + ex.getLocalizedMessage());
        }
    }

    /**
     * добавить контекст
     *
     * @param collection - коллекция терминов
     * @param term - термин
     * @param context - контекст
     * @param fileName - имя файла *
     */
    private void addContextWord(CollectionTerms collection,
            String term,
            String context,
            String fileName) {
        try {
            FrequencyOccurrenceTerm termin = collection.findTerm(term);
            termin.addContext(context, directory.getName() + ", " + fileName);
        } catch (Exception ex) {
            ArticlesController.showAlert("Ooops, there was an error!\n"
                    + ex.getLocalizedMessage());
        }
    }

    /**
     * Добавляет слово в пользовательский файл
     *
     * @param path - путь к файлу
     * @param word - слово для добавления
     */
    public void addWordInFile(String path, String word) {
        try {
            FileWriter file = new FileWriter(path, true);
            file.append(word + "\n");
            file.flush();
            file.close();
        } catch (IOException ex) {
            ArticlesController.showAlert("Ooops, there was an error!\n"
                    + ex.getLocalizedMessage());
        }
    }

    /**
     * Удаляет слово из пользовательского файла
     *
     * @param path - путь к файлу
     * @param word - слово для удаления
     */
    public void deleteWordInFile(String path, String word) {
        String text = "";
        try {
            File file = new File(path);
            Scanner in = new Scanner(file, "UTF-8");
            while (in.hasNextLine()) {
                String str = in.nextLine();
                if (str.equals(word)) {
                    str = "";
                } else {
                    str += "\n";
                }
                text += str;
            }
        } catch (IOException e) {
            System.out.println("Input file " + path + " error");
        }
        try {
            
//            {   // Вариант записи кодировки "cp1251" (Windows)
//                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "cp1251"));
//                out.append(text);
//                out.close();
//            }
//            {   // Вариант записи кодировки "UTF-8"
//                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
//                out.append(text);
//                out.close();
//            }
            
//            // не работает import
//            File f = new File(path); 
//            FileUtils fu = FileUtils.writeStringToFile(f, text, "UTF-8");  
            
//            PrintWriter out = new java.io.PrintWriter(new java.io.File(path), "UTF-8");
//            out.print(text);
//            out.flush();
//            out.close();
            
//            OutputStreamWriter file = new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8);
           
            Writer out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(path), "UTF-8"));
            try {
                out.write(text);
            } finally {
                out.close();
            }
            
//            //  было
//            FileWriter file = new FileWriter(path, false);
//            file.write(text);
//            file.flush();
//            file.close();
        } catch (IOException ex) {
            ArticlesController.showAlert("Ooops, there was an error!\n"
                    + ex.getLocalizedMessage()
                    + "Output file " + path + " error");
        }
    }
}

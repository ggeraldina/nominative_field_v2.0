/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processArticle_v2;

import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * класс = термин + его общая встречаемость + коллекция встречаемости по годам
 * @author Geraldina&Fennics
 */
public class FrequencyOccurrenceTerm {
    private final BooleanProperty termFromUsers;
    private final StringProperty term;
    private ArrayList<IntegerProperty> allFrequencyOccurrence;
    private ArrayList<IntegerProperty> frequencyOccurrenceNear;
    private ArrayList<IntegerProperty> frequencyOccurrenceThroughOne;
    private List<Context> context;
    
    //Считаем номер добавляемого контекста, иначе не умею)
    private int countContext = -1;
    // индекс яркости = част. встречаем. / кол-во всех контекстов
    private final DoubleProperty indexBrightness;
    
    // конструкторы
    FrequencyOccurrenceTerm(String term1, boolean termFromUsers1){
        termFromUsers = new SimpleBooleanProperty(termFromUsers1);
        term = new SimpleStringProperty(term1);
        allFrequencyOccurrence = new ArrayList<>();
        
        allFrequencyOccurrence.add(0, new SimpleIntegerProperty(0));
        allFrequencyOccurrence.add(1, new SimpleIntegerProperty(0));
        frequencyOccurrenceNear = new ArrayList<>();
        
        frequencyOccurrenceNear.add(0, new SimpleIntegerProperty(0));
        frequencyOccurrenceNear.add(1, new SimpleIntegerProperty(0));
        
        frequencyOccurrenceThroughOne = new ArrayList<>();
        frequencyOccurrenceThroughOne.add(0, new SimpleIntegerProperty(0));
        frequencyOccurrenceThroughOne.add(1, new SimpleIntegerProperty(0));
        
        context = new ArrayList<>();
        indexBrightness = new SimpleDoubleProperty(0);
    } 
    FrequencyOccurrenceTerm(String term1){
        this(term1, false);
    }
    //
    // все методы для поля termFromUsers
    //
    public void setTermFromUsers(Boolean value) { termFromUsers.set(value); }
    public Boolean getTermFromUsers() { return termFromUsers.get(); }   
    public BooleanProperty termFromUsersProperty() { return termFromUsers; }
    //
    // все методы для поля term
    //
    public void setTerm(String value) { term.set(value); }
    public String getTerm() { return term.get(); }   
    public StringProperty termProperty() { return term; }
    //
    // все методы для счетчика общей встречаемости AllFrequencyOccurrence[0]
    //
    public void setAllFrequencyOccurrence0(int value) { allFrequencyOccurrence.get(0).set(value); }    
    public int getAllFrequencyOccurrence0() { return allFrequencyOccurrence.get(0).get(); } 
    public IntegerProperty allFrequencyOccurrenceProperty0() { return allFrequencyOccurrence.get(0); }
    // увеличить счетчик общей встречаемости на 1: AllFrequencyOccurrence[0]
    public void allFrequencyOccurrenceIncrease0(){
        int currentFO = allFrequencyOccurrence.get(0).get();
        allFrequencyOccurrence.get(0).set(++currentFO);
    }
    //
    // все методы для счетчика FrequencyOccurrenceNear[0]
    //
    public void setFrequencyOccurrenceNear0(int value) { frequencyOccurrenceNear.get(0).set(value); }
    public int getFrequencyOccurrenceNear0() { return frequencyOccurrenceNear.get(0).get(); }
    
    public IntegerProperty frequencyOccurrenceNearProperty0() { return frequencyOccurrenceNear.get(0); }
    /***
     * Увеличивает счётчик frequencyOccurrenceNear на единицу
     */
    public void frequencyOccurrenceNearIncrease0(){
        int currentFO = frequencyOccurrenceNear.get(0).get();
        frequencyOccurrenceNear.get(0).set(++currentFO);
    }
    //
    // все методы для счетчика FrequencyOccurrenceThroughOne[0]
    //
    public void setFrequencyOccurrenceThroughOne0(int value) { frequencyOccurrenceThroughOne.get(0).set(value); }
    public int getFrequencyOccurrenceThroughOne0() { return frequencyOccurrenceThroughOne.get(0).get(); }
    public IntegerProperty frequencyOccurrenceThroughOneProperty0() { return frequencyOccurrenceThroughOne.get(0); }
    // увеличить счетчик на 1
    public void frequencyOccurrenceThroughOneIncrease0(){
        int currentFO = frequencyOccurrenceThroughOne.get(0).get();
        frequencyOccurrenceThroughOne.get(0).set(++currentFO);
    } 
    
    //
    // все методы для счетчика общей встречаемости AllFrequencyOccurrence[1]
    //
    public void setAllFrequencyOccurrence1(int value) { allFrequencyOccurrence.get(1).set(value); }    
    public int getAllFrequencyOccurrence1() { return allFrequencyOccurrence.get(1).get(); } 
    public IntegerProperty allFrequencyOccurrenceProperty1() { return allFrequencyOccurrence.get(1); }
    // увеличить счетчик общей встречаемости на 1: AllFrequencyOccurrence[1]
    public void allFrequencyOccurrenceIncrease1(){
        int currentFO = allFrequencyOccurrence.get(1).get();
        allFrequencyOccurrence.get(1).set(++currentFO);
    } 
    //
    // все методы для счетчика FrequencyOccurrenceNear[1]
    //
    public void setFrequencyOccurrenceNear1(int value) { frequencyOccurrenceNear.get(1).set(value); }
    public int getFrequencyOccurrenceNear1() { return frequencyOccurrenceNear.get(1).get(); }
    // увеличить счетчик на 1
    public IntegerProperty frequencyOccurrenceNearProperty1() { return frequencyOccurrenceNear.get(1); }
     public void frequencyOccurrenceNearIncrease1(){
        int currentFO = frequencyOccurrenceNear.get(1).get();
        frequencyOccurrenceNear.get(1).set(++currentFO);
    }
     
    //
    // все методы для счетчика FrequencyOccurrenceThroughOne[1]
    //
    public void setFrequencyOccurrenceThroughOne1(int value) { frequencyOccurrenceThroughOne.get(1).set(value); }
    public int getFrequencyOccurrenceThroughOne1() { return frequencyOccurrenceThroughOne.get(1).get(); }
    public IntegerProperty frequencyOccurrenceThroughOneProperty1() { return frequencyOccurrenceThroughOne.get(1); }
    // увеличить счетчик на 1
    public void frequencyOccurrenceThroughOneIncrease1(){
        int currentFO = frequencyOccurrenceThroughOne.get(1).get();
        frequencyOccurrenceThroughOne.get(1).set(++currentFO);    
    }
    
    //
    // все методы для поля context
    //
    public void setContext(int value) { allFrequencyOccurrence.get(1).set(value); }    
    public List getContext() { return context; }
    
    public void addContext(String context, String fileName) {
        //Ищем существует ли такой контекст
        for(Context c: this.context) {
            //Если существует, увеличиваем ему счетчик слов
            if(c.getContext().equals(context) && c.getFileName().equals(fileName)) {
                this.context.get(countContext).upNumTerm();
                return;
            }
        }
        countContext++;
        this.context.add(new Context(context, fileName));
    }
    //
    // все методы для поля indexBrightness
    //
    public double getIndexBrightness() { return indexBrightness.get(); }
    public void setIndexBrightness(Double value) { indexBrightness.set(value); }
    public DoubleProperty indexBrightnessProperty() { return indexBrightness; } 
    
     
}
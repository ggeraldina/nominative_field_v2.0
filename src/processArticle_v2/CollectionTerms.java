/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processArticle_v2;

import java.util.Collections;
import java.util.Comparator;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * класс с коллекцией терминов (2 списка: обычный и "необычный" ObservableList)
 * @author Geraldina&Fennics
 */
public class CollectionTerms {
    
    private final ObservableList<FrequencyOccurrenceTerm> termsData;
    // кол-во контекстов св-во
    private IntegerProperty numberContexts; 
    
    
    // конструктор
    CollectionTerms() {
       termsData = FXCollections.observableArrayList();
       numberContexts = new SimpleIntegerProperty(0);
    }      
        
    /**
     * получить коллекцию терминов ObservableList
     * @return
     */
    public ObservableList<FrequencyOccurrenceTerm> getTermsData() {
        return termsData;
    }    
    
    /**
     * получить количество контекстов
     * @return
     */
    public int getNumberContexts() { return numberContexts.get(); }
    public void setNumberContexts(Integer value) { numberContexts.set(value); }
    public IntegerProperty numberContextsProperty() { return numberContexts; } 
    /**
     * увеличить количество контекстов
     * 
     */
    public void numberContextsIncrease() {
        numberContexts.set(numberContexts.get()+1);
    }
      
    
    /**
     * добавить новый термин 
     * @param term
     */
    public void addTerm(FrequencyOccurrenceTerm term){
        termsData.add(0, term);
    }
        
    /**
     * удалить термин
     * @param term
     */
    public void deleteTerm(FrequencyOccurrenceTerm term){
        termsData.remove(term);
    }
        
    /**
     * есть ли такой-то термин в коллекции ArrayList? (да / нет)
     * @param term1
     * @return
     */
    public boolean haveTerm(String term1){
        for(FrequencyOccurrenceTerm term: termsData){
            if (term1.equals(term.getTerm()))
                return true;
        }
        return false;
    }   
        
    /**
     * найти термин в коллекции ArrayList, если его нет, то null
     * @param term1
     * @return
     */
    public FrequencyOccurrenceTerm findTerm(String term1){
        for(FrequencyOccurrenceTerm term: termsData){
            if (term1.equals(term.getTerm()))
                return term;
        }
        return null;
    }       
    
    // составить красивую строку обо всех терминах
    @Override
    public String toString(){
        String result = "";        
        for(FrequencyOccurrenceTerm t: termsData){
            result = result + t.getTerm() + 
                    " (Встречается " + t.getAllFrequencyOccurrence0() + 
                    " / " + t.getAllFrequencyOccurrence1() + " раз) \n";          
        }        
         return result;
    }
        
   
    /**
     * отсортировать термины из ArrayList по частоте встречаемости
     * @param flagSeriesArticles
     */
    public void sort(String flagSeriesArticles){        
        switch(flagSeriesArticles){
            case "zero":                
                Collections.sort(termsData, new Comparator<FrequencyOccurrenceTerm>(){
                    @Override
                    public int compare(FrequencyOccurrenceTerm term1, FrequencyOccurrenceTerm term2) {
                        return term2.getAllFrequencyOccurrence0() - term1.getAllFrequencyOccurrence0();
                    }
                });
                break;
            case "first":                
                Collections.sort(termsData, new Comparator<FrequencyOccurrenceTerm>(){
                    @Override
                    public int compare(FrequencyOccurrenceTerm term1, FrequencyOccurrenceTerm term2) {
                        return term2.getAllFrequencyOccurrence1() - term1.getAllFrequencyOccurrence1();
                    }
                });
                break; 
        }
    }
    
    /**
     * рассчитать индекс яркости для каждого термина
     * 
     *
     */
    public void calculateIndexBrightness(){
        int countContext = 0;
        for (FrequencyOccurrenceTerm fr : termsData){
            if(fr.getContext() != null)
                countContext += fr.getAllFrequencyOccurrence0();
        }
        for (FrequencyOccurrenceTerm fr : termsData){
            int countFO = fr.getAllFrequencyOccurrence0();            
            double indexBrightness = (double)countFO/countContext;
            // округление
            indexBrightness = (double)(Math.round(indexBrightness * 100000.0) / 100000.0);
            fr.setIndexBrightness(indexBrightness);
        }
    }
}


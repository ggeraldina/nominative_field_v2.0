/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package processArticle_v2;

/**
 *
 * @author Geraldina&Fennics
 */
public class Context {

    private String context;
    private String fileName;
    private int numTerm;

    public Context(String context, String fileName) {
        this.context = context;
        this.fileName = fileName;
        this.numTerm = 1;
    }

    public void setContext(String context) {
        this.context = context;
    }
    public String getContext() {
        return context;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileName() {
        return fileName;
    }
    
   public void upNumTerm() {
        this.numTerm++;
    }
    public int getNumTerm() {
        return numTerm;
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.uwl.luci.mapping;

/**
 *
 * @author hyeokim
 */
public class DataCleaner {
    
    public static String cleanUcasCode(String original){
        String cleansed = original;
        if(original.contains(" ")){
            cleansed = original.substring(0, original.indexOf(" "));
        }
        
        
        return cleansed;
    }
    
    public static String cleanPostCode(String original){
        String cleansed = original;
        if(original.equals("TW8")){
            cleansed = "TW8 9GA";
        }
        
        return cleansed;
    }
    
    public static String cleanSubject(String original){
        String cleansed = original;
        if(original.contains("&")){
            cleansed = cleansed.replaceAll("&", "and");
        }
        if(original.contains(",")){
            cleansed = cleansed.replace(",", " and ");
        }
        if(original.contains(")")){
            cleansed = cleansed.replace(")", " ");
        }
        if(original.contains("(")){
            cleansed = cleansed.replace("(", " ");
        }
        
        return cleansed;
    }
    
    public static String getFirst4000Chars(String original){
        String cleansed = original;
        if(original.length()>4000){
            cleansed = original.substring(0,4000);
        }
        
        return cleansed;
    }
    
    public static void main(String args[])
    {
        String testUcasCode = "N420 BA/ACBF";
        String testSubjectCode = "Accountancy & Finance";
        String testIntervalCode = "1 semester (13-15 weeks)";
        
        
        System.out.println("[before: "+ testUcasCode + ", after:"+DataCleaner.cleanUcasCode(testUcasCode)+"]");
        System.out.println("[before: "+ testSubjectCode + ", after:"+DataCleaner.cleanSubject(testSubjectCode)+"]");
        //System.out.println("[before: "+ testIntervalCode + ", after:"+Hb2Xcri.getDurationIntervalCode(testIntervalCode)+"]");
        
                
                
    }
    
     
    public static void debug(String input){
        if(input != null && input.trim().equals(""))
            System.out.println("====>Address: ["+input+"]");
    }
}

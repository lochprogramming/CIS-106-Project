// **********************************************************
// Title: Credit Class
// File: Credit.java
// Author: Matt Lochman
// Description: Credit class; Is a subclass of Transaction
// **********************************************************

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.function.Predicate;
import javafx.beans.Observable;
import javafx.util.Callback;

public class Credit extends Transaction
{
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Fields

   //creation of Credit specific attribute variables.
   private String category;
   private String type; 
   private static ObservableList<String> creditCategories = 
         FXCollections.observableArrayList("Wages", "Capital Gains", "Other"); 
   private static ObservableList<String> creditTypes =  
         FXCollections.observableArrayList("Direct Deposit", "Cash Deposit", "Check Deposit", "Transfer", "Other");

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Constructors
  
   //Default Constructor with no information passed; sets values to defaults.
   public Credit() {
      super();
      this.category = "";
      this.type = "";
   }
   
   //Overloaded constructor for when the check number is given (for check Transaction).
   public Credit(boolean vis, String desc, int mon, int d, int y, double a, String cat, String t) {
      super(vis,desc,mon,d,y,a);
      this.category = cat;
      this.type = t;
   }

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Static Methods
  
   //Static Accessors
   public static int getCreditCategorySize() {
      return creditCategories.size();
   }
   public static String getCreditCategory(int i) {
      return creditCategories.get(i);
   }
   public static int getCreditTypeSize() {
      return creditTypes.size();
   }
   public static String getCreditType(int i) {
      return creditTypes.get(i);
   }
   public static ObservableList<String> getAllCreditCategories() { // Returns a copy of the list.
      return FXCollections.observableArrayList(creditCategories);
   }
   public static ObservableList<String> getAllCreditTypes() { // Returns a copy of the list.
      return FXCollections.observableArrayList(creditTypes);
   }
   
   //Static Mutators
   public static void addCreditCategory(String s) {
      creditCategories.add(s);
   }
   public static void removeCreditCategory(String s) {
      creditCategories.remove(s);
   }
   public static void clearCreditCategories() {
      creditCategories.clear();
   }
   public static void setDefaultCreditCategories() { // for setting them back to the original ones.
      creditCategories = FXCollections.observableArrayList("Wages", "Capital Gains", "Other");
   }
   public static void addCreditType(String s) {
      creditTypes.add(s);
   }
   public static void removeCreditType(String s) {
      creditTypes.remove(s);
   }
   public static void clearCreditTypes() {
      creditTypes.clear();
   }
   public static void setDefaultCreditTypes() { // for setting them back to the original ones.
      creditTypes = FXCollections.observableArrayList("Direct Deposit", "Cash Deposit", "Check Deposit", "Transfer", "Other");
   }
 
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////  
//Static Predicates and Predicate generators

   // for filtering out visible entries
   public static Predicate<Credit> creditVisibilityFilter() { 
      return new Predicate<Credit>() {
         public boolean test(Credit c) {
            return (c.getVisibility());
         }
      };
   }
   //produces a predicate that filters out the passed year.
   public static Predicate<Credit> creditYearPredicateGenerator(int year) { 
      return new Predicate<Credit>() {
             public boolean test(Credit c) {
                  return (c.getYear() == year);
             }
          };
   }
   //produces a predicate that filters out the passed month
   public static Predicate<Credit> creditMonthPredicateGenerator(String month) {
      return new Predicate<Credit>() {
          public boolean test(Credit c) {
               return (c.getMonthString().equals(month));
          }
      };
   }
   
   //Visibility Extractor for Observable Lists
   //Allows monitoring of property changes in visibility
   public static Callback<Credit, Observable[]> creditExtractor = (Credit c) -> {
      return new Observable[]{c.getVisibilityProperty()};
   };
      
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Non-static Methods
   
   //Accessor methods to retrieve the values of each attribute.
   public String getCategory() {
      return category;
   }
   public String getType() {
      return type;
   }
   public String getWriteString() { // Produces the write string for this entry when saving files.
      return super.getWriteString() + "," + getCategory() + "," + getType();
   }
   
   //Overwritten toString method to return a formatted string giving the object information for this Debit object.
   public String toString() {
      String result = "Credit Entry\n";
      result += "-------------------------------------\n";    
      result += super.toString();
      result += "Category:            " + getCategory() + "\n";
      result += "Type:                " + getType() + "\n";
      return result;   
   }
   
   //Mutator methods to set each of the attributes separately.
   public void setCategory(String cat) {
      category = cat;
   }
   public void setType(String t) {
      type = t;
   }
}//class
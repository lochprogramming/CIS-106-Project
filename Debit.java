// **********************************************************
// Title: Debit Class
// File: Debit.java
// Author: Matt Lochman
// Description: Debit class; Is a subclass of Transaction
// **********************************************************

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.function.Predicate;
import javafx.beans.Observable;
import javafx.util.Callback;

public class Debit extends Transaction
{      
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Fields
                             
   //creation of credit specific attribute variables.
   private String category;
   private String type; 
   private static ObservableList<String> debitCategories = 
         FXCollections.observableArrayList("Utilities", "Groceries", "Travel", "Other");
   private static ObservableList<String> debitTypes = 
         FXCollections.observableArrayList("Credit Card", "Check", "ATM", "Transfer", "Other"); 
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Constructors

   //Default Constructor with no information passed; sets values to defaults.
   public Debit() {
      super();
      this.category = "";
      this.type = "";
   }
   
   //Overloaded constructor for when the check number is given (for check Transaction).
   public Debit(boolean vis, String desc, int mon, int d, int y, double a, String cat, String t) {
      super(vis,desc,mon,d,y,a);
      this.category = cat;
      this.type = t;
   }

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Static Methods

   //Static accessors
   public static int getDebitCategorySize() {
      return debitCategories.size();
   }
   public static String getDebitCategory(int i) {
      return debitCategories.get(i);
   }
   public static int getDebitTypeSize() {
      return debitTypes.size();
   }
   public static String getDebitType(int i) {
      return debitTypes.get(i);
   }
   public static ObservableList<String> getAllDebitCategories() { // Returns a copy of the list.
      return FXCollections.observableArrayList(debitCategories);
   }
   public static ObservableList<String> getAllDebitTypes() { // Returns a copy of the list.
      return FXCollections.observableArrayList(debitTypes);
   }
   
   //Static Mutators
   public static void addDebitCategory(String s) {
      debitCategories.add(s);
   }
   public static void removeDebitCategory(String s) {
      debitCategories.remove(s);
   }
   public static void clearDebitCategories() {
      debitCategories.clear();
   }
   public static void setDefaultDebitCategories() {
      debitCategories = FXCollections.observableArrayList("Utilities", "Groceries", "Travel", "Other");
   }
   public static void addDebitType(String s) {
      debitTypes.add(s);
   }
   public static void removeDebitType(String s) {
      debitTypes.remove(s);
   }
   public static void clearDebitTypes() {
      debitTypes.clear();
   }
   public static void setDefaultDebitTypes() {
      debitTypes = FXCollections.observableArrayList("Credit Card", "Check", "ATM", "Transfer", "Other");
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Static Predicates and Predicate Generators

   //filters out visible debits
   public static Predicate<Debit> debitVisibilityFilter() {
      return new Predicate<Debit>() {
         public boolean test(Debit d) {
            return (d.getVisibility());
         }
      };
   }
   //returns a filter for the passed year
   public static Predicate<Debit> debitYearPredicateGenerator(int year) {
      return new Predicate<Debit>() {
         public boolean test(Debit d) {
            return (d.getYear() == year);
         }
      };
   }
   //returns a filter for the pasted month
   public static Predicate<Debit> debitMonthPredicateGenerator(String month) {
      return new Predicate<Debit>() {
         public boolean test(Debit d) {
            return (d.getMonthString().equals(month));
         }
      };
   }
      
   //Visibility Extractor for Observable Lists
   //Allows monitoring of property changes in visibility
   public static Callback<Debit, Observable[]> debitExtractor = (Debit d) -> {
      return new Observable[]{d.getVisibilityProperty()};
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
   public String getWriteString() {
      return super.getWriteString() + "," + getCategory() + "," + getType();
   }
   
   //Overwritten toString method to return a formatted string giving the object information for this Debit object.
   public String toString() {
      String result = "Debit Entry\n";
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
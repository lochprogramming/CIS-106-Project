// **********************************************************
// Title: Transaction Class
// File: Transaction.java
// Author: Matt Lochman
// Description: Transaction class for Major Project Assignment 2
// **********************************************************

import javafx.beans.property.SimpleBooleanProperty;

public class Transaction {
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Fields
   
   //Static string list for month to string conversion
   private final static String[] MONTHS = {"January", "February", "March", "April", "May", "June", "July", "August",  
                                    "September", "October", "November", "December"};
                                    
   //creation of attribute variables.
   private SimpleBooleanProperty visibility; 
   private String description;
   private int month, day, year;
   private double amount;

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Constructors  
   
   //Default Constructor with no information passed; sets values to defaults.
   public Transaction() {
      this.visibility = new SimpleBooleanProperty(true);
      this.description = "";
      this.month = 1;
      this.day = 1;
      this.year = 1900;
      this.amount = 0.0;
   }
   
   //Overloaded constructor for when the check number is given (for check Transaction).
   public Transaction(boolean vis, String desc, int mon, int d, int y, double a) {
      this.visibility = new SimpleBooleanProperty(vis);
      this.description = desc;
      this.month = mon;
      this.day = d;
      this.year = y;
      this.amount = a;
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Accessor Methods
      
   //Accessor methods to retrieve the values of each attribute.
   public String getDescription() {
      return description;
   }
   public int getMonth() {
      return month;
   }
   public int getDay() {
      return day;
   }
   public int getYear() {
      return year;
   }
   public String getDate() { //returns the date in MM/DD/YYYY form
      return String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + year;
   }
   public String getYearString() { // formats the year as a string
      return String.format("%d", year);
   }
   public String getMonthString() { // uses the static array to return the month as a string
      return MONTHS[month - 1];
   }
   public double getAmount() {
      return amount;
   }
   public String getAmountString() { // returns the amount as a formatted monetary string
      return String.format("$%,.2f",amount);
   }
   public boolean getVisibility() {
      return visibility.get();
   }
   public SimpleBooleanProperty getVisibilityProperty() {
      return visibility;
   }
   public String getWriteString() {
      return getVisibility() + "," + getDescription() + "," + getMonth() + "," + getDay() + "," + getYear() + "," + getAmount();
   }
   
   //toString method to return a formatted string giving the object information for this Debit object.
   public String toString() {
      String result = "Description:        " + getDescription() + "\n";
      result += "Date:                   " + getDate() + "\n";
      result += "Amount:             " + getAmountString() + "\n";
      return result;   
   }
   
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Mutator Methods 

   //Mutator methods to set each of the attributes separately.
   public void setDescription(String desc) {
      description = desc;
   }
   public void setMonth(int mon) {
      month = mon;
   }
   public void setDay(int d) {
      day = d;
   }
   public void setYear(int y) {
      year = y;
   }
   public void setAmount(double a) {
      amount = a;
   }
   public void setVisibility(boolean visible) {
      this.visibility.set(visible);
   }    
}//class
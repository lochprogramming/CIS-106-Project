// **********************************************************
// Title: Desscriptive Statistics Calculator
// File: StatCalculator.java
// Author: Matt Lochman
// Description: Class to provide statistics of values passed to it.
// **********************************************************

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.List;
import javafx.collections.transformation.FilteredList;

public class StatCalculator {
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Fields for statistics

   private double sum; // total of values
   private double aMean; // arithmetic mean
   private double sampStDev; // sample standard deviation
   private double popStDev; //population standard deviation
   private double min; //minimum value
   private double quartile1; //first quartile
   private double median; //median
   private double quartile3; // third quartile
   private double max; //max value
   private double IQR; // Inter-quartile range
   private ObservableList<Double> outliers = FXCollections.observableArrayList(); //list to hold outlier values.
   private ObservableList<Double> dataList= FXCollections.observableArrayList(); // List of values
   private int n; // sample size
   

////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Constructor
   public StatCalculator() {
      sum = 0.0;
      aMean = 0.0;
      sampStDev = 0.0;
      popStDev = 0.0;
      min = 0.0;
      quartile1 = 0.0;
      median = 0.0;
      quartile3 = 0.0;
      max = 0.0;
      IQR = 0.0;
      n = -1;
   }
   public StatCalculator(ObservableList<Double> data) {
      //Extract all of the amount values into a new list
      this(); // initialize all the values
      for (int i = 0; i < data.size(); i++)
         dataList.add(data.get(i));
      FXCollections.sort(dataList);
      n = dataList.size();
      calcSum();
      calcMean();
      calcStDev();
      calc5NumSum();
      calcOutliers();
   }
   
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Accessor Methods 

   public double getSum() {
      return sum;
   }
   public double getAMean() {
      return aMean;
   }
   public double getSampStDev() {
      return sampStDev;
   }
   public double getPopStDev() {
      return popStDev;
   }
   public double getMin() {
      return min;
   }
   public double getQuartile1() {
      return quartile1;
   }
   public double getMedian() {
      return median;
   }
   public double getQuartile3() {
      return quartile3;
   }
   public double getMax() {
      return max;
   }
   public double getIQR() {
      return IQR;
   }
   public ObservableList<Double> getOutliers() {
      return outliers;
   }
   
   //Standards toString method
   public String toString() {
      String result = "Sum: " + getSum();
      result += "\nArithmetic Mean: " + getAMean();
      result += "\nPop. St. Dev.: " + getPopStDev();
      if (n > 1)
         result += "\nSamp. St. Dev.: " + getSampStDev();
      result += "\nMinimum: " + getMin();
      result += "\nQuartile 1: " + getQuartile1();
      result += "\nMedian: " + getMedian();
      result += "\nQuartile 3: " + getQuartile3();
      result += "\nMaximum: " + getMax();
      result += "\nInterquartile Range: " + getIQR();
      if (getOutliers().size() > 0)
         result += "\nOutliers: " + getOutliers();
      return result;
   }
   
   //toString method that formats all entries as monetary values.
   public String toMoneyString() {
      String result = "Number of displayed records: " + n;
      result += "\nArithmetic Mean: " + String.format("$%,.2f",getAMean());
      result += "\nPop. St. Dev.: " + String.format("$%,.2f",getPopStDev());
      if (n > 1)
         result += "\nSamp. St. Dev.: " + String.format("$%,.2f",getSampStDev());
      result += "\nMinimum: " + String.format("$%,.2f",getMin());
      result += "\nQuartile 1: " + String.format("$%,.2f",getQuartile1());
      result += "\nMedian: " + String.format("$%,.2f",getMedian());
      result += "\nQuartile 3: " + String.format("$%,.2f",getQuartile3());
      result += "\nMaximum: " + String.format("$%,.2f",getMax());
      result += "\nInterquartile Range: " + String.format("$%,.2f",getIQR());
      if (getOutliers().size() > 0) 
         result += "\nOutliers:\n\t" + String.format("$%,.2f",getOutliers().get(0));
      if (getOutliers().size() > 1)
         for (int i = 1; i < outliers.size(); i++)
             result += ", " + String.format("$%,.2f",getOutliers().get(i));
      
      return result;
   }
  
////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////
//Stat Methods 
   private void calcSum() {
      for (int i = 0; i < dataList.size(); i++)
         sum += dataList.get(i);
   }
   private void calcMean() {
      aMean = sum / n;
   }
   private void calcStDev() {
      double squareDeviations = 0;
      for (int i = 0; i < dataList.size(); i++)
         squareDeviations += Math.pow(dataList.get(i) - aMean, 2);
      popStDev = Math.sqrt(squareDeviations / n);
      if (n > 1)
         sampStDev = Math.sqrt(squareDeviations / (n - 1));
   }
   private double calcMedian(List<Double> list) {
      if (list.size() % 2 == 1)
         return list.get(list.size() / 2);
      else
         return (list.get(list.size() / 2 - 1) + list.get(list.size() / 2)) / 2;
   }
   private void calc5NumSum() {
      min = dataList.get(0); // Minimum value is in the first position
      int L = (n == 1) ? 0 : dataList.size() / 2 - 1; // Location for the median
      quartile1 = calcMedian(dataList.subList(0, L + 1)); // find the median of the first half (excluding median)
      median = calcMedian(dataList.subList(0, dataList.size())); //Find the median of the whole data set
      quartile3 = calcMedian(dataList.subList(dataList.size() - 1 - L, dataList.size())); // find the median of the second half (excluding median)
      max = dataList.get(dataList.size() - 1); // Max is in the last position.
      IQR = quartile3 - quartile1;
   }
   private void calcOutliers() {
      double lowerFence = quartile1 - 1.5 * IQR;
      double upperFence = quartile3 + 1.5 * IQR;
      for (int i = 0; i < dataList.size(); i++) 
         if ((dataList.get(i) < lowerFence) || (dataList.get(i) > upperFence))
            outliers.add(dataList.get(i));
   }
}
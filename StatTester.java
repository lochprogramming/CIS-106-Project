import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class StatTester {
   public static void main(String[] args) {
      ObservableList<Double> testDataOdd1 = FXCollections.observableArrayList();
      ObservableList<Double> testDataOdd2 = FXCollections.observableArrayList();
      ObservableList<Double> testDataEven1 = FXCollections.observableArrayList();
      ObservableList<Double> testDataEven2 = FXCollections.observableArrayList();
      
      testDataOdd1.addAll(23.4, 24.5, 27.1, 23.2, 39.6, 23.0, 25.5, 27.444, 100.0, 101.0, -2.0);
      
      System.out.println("***************First Set****************\n" + new StatCalculator(testDataOdd1));
   }
}
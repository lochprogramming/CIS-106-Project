// **********************************************************
// Title: Type Management Controller
// File: TypeManagementController.java
// Author: Matt Lochman
// Description: Controller for Method Management stage.
// **********************************************************

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class TypeManagementController extends ManagementController {

   //Overwritten OK button method.
   @FXML
   public void okButtonAction(ActionEvent event) {
      Credit.clearCreditTypes();
      Debit.clearDebitTypes();
      for (int i = 0; i < getCreditItems().size(); i++)
         Credit.addCreditType(getCreditItems().get(i));
      for (int i = 0; i < getDebitItems().size(); i++)
         Debit.addDebitType(getDebitItems().get(i));
      cancelButtonAction(event);
   }
   
   //initialize the lists
   public void initialize() {
      setItemType("Type");
      getCreditItems().addAll(Credit.getAllCreditTypes());
      getDebitItems().addAll(Debit.getAllDebitTypes());
      getCreditListView().setItems(getCreditItems());
      getDebitListView().setItems(getDebitItems());
   }  
}
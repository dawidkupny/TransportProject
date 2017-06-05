package transport.project.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import transport.project.model.Contract;
import transport.project.model.Course;
import transport.project.util.DatabaseToolkit;

public class ManagerContractsTabController implements Initializable {
    final String ALL_DATA = "SELECT c.contract_id, c.contract_number, c.start_date, "
                          + "c.expiration_date, c.salary, d.driver_id, d.first_name, d.last_name "
                          + "FROM civil_contract c JOIN driver d ON (c.driver_driver_id = d.driver_id);";

    @FXML
    private TableView contractsTable;
    
    @FXML
    private TableColumn contractIdColumn;
     
    @FXML
    private TableColumn contractContractColumn; 
    
    @FXML
    private TableColumn contractDriverColumn; 
    
    @FXML
    private TableColumn contractStartDateColumn; 
    
    @FXML
    private TableColumn contractExpirationDateColumn; 
    
    @FXML
    private TableColumn contractSalaryColumn;
    
    private DatabaseToolkit databaseToolkit;
    
    private final int INSERT = 1; 
    private final int UPDATE = 2; 
    private final int SELECT = 3; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      contractIdColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      contractContractColumn.setCellValueFactory(new PropertyValueFactory<>("contractNumber"));
      contractDriverColumn.setCellValueFactory(new PropertyValueFactory<>("driverToString"));
      contractStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
      contractExpirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
      contractSalaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));
      
      databaseToolkit = DatabaseToolkit.getInstance();
        
      contractsTable.setItems(searchData(ALL_DATA));
    }    
    
    public ObservableList<Contract> searchData(String query) {
        ObservableList<Contract> observableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = databaseToolkit.executeQuery(query);
            
            int counter=0;
            while(resultSet.next()) {
                observableList.add(new Contract(++counter,
                        resultSet.getString("c.contract_number"),
                        resultSet.getDate("c.start_date"),
                        resultSet.getDate("c.expiration_date"),
                        resultSet.getBigDecimal("c.salary"),
                        (resultSet.getInt("d.driver_id") + ": " + resultSet.getString("d.first_name") 
                                + " " + resultSet.getString("d.last_name"))
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerDriversTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return observableList;
    }
    
    private void openWindow(int state, Contract contract) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transport/project/view/ManagerContractsManipulateDataView.fxml"));
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene((Pane) loader.load()));
            
            ManagerContractsManipulateDataController controller =
                    loader.<ManagerContractsManipulateDataController>getController();
            if(state==UPDATE) controller.initState(state,contract);
            else controller.initState(state);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ManagerVehiclesTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    public void add() {
        openWindow(INSERT,null);
    }
    
    @FXML
    public void remove() {
       String contractToRemove = showRemoveDialog();
       if(!contractToRemove.equals("")) {
          databaseToolkit.executeUpdate("DELETE FROM civil_contract WHERE contract_number=\""+contractToRemove+"\";");
          contractsTable.setItems(searchData(ALL_DATA));
       }
    }
    
    public String showRemoveDialog() {
        ArrayList<String> choices = new ArrayList();
        searchData(ALL_DATA).forEach(x -> choices.add(x.getContractNumber()));
        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Usuwanie umowy.");
        dialog.setHeaderText("Aby usunąć umowę z systemu, wybierz ją z poniższej listy. \n"
                + "TEJ OPERACJI NIE DA SIĘ COFNĄĆ!");
        dialog.setContentText("Umowa do usunięcia: ");
        Optional<String> optional = dialog.showAndWait();
        return optional.get();
        }
    
    

    @FXML
    public void update() {
        String contractNumber = showUpdateDialog();
        if(!contractNumber.equals(""))
        openWindow(UPDATE,searchData("SELECT c.contract_id, c.contract_number, c.start_date, " +
                                     "c.expiration_date, c.salary, d.driver_id, d.first_name, d.last_name " +
                                     "FROM civil_contract c JOIN driver d ON (c.driver_driver_id = d.driver_id) "
                                   + " WHERE contract_number=\""+contractNumber+"\";").get(0));
    }
    
      public String showUpdateDialog() {
        ArrayList<String> choices = new ArrayList();
        searchData(ALL_DATA).forEach(x -> choices.add(x.getContractNumber()));
        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Edytowanie umowy - zlecenie");
        dialog.setHeaderText("Aby edytować dane umowy w systemie, wybierz ją z poniższej listy. \n");
        dialog.setContentText("Umowa do modyfikacji: ");
        Optional<String> optional = dialog.showAndWait();
        return optional.get();
        
    }
      
    @FXML
    public void select() {
        openWindow(SELECT,null);
    }  
    
    
    @FXML
    private void refresh() {
        contractsTable.setItems(searchData(ALL_DATA));
    }
}

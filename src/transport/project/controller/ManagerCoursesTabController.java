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
import transport.project.model.Course;
import transport.project.util.DatabaseToolkit;

public class ManagerCoursesTabController implements Initializable {
    final String ALL_DATA = "SELECT c.order_id, c.distance, c.starting_point, c.destination, c.start_date, c.end_date, c.load_weight, c.cubature, c.incineration, " +
                            " v.registration_number, v.brand, v.model, " +
                            " d.driver_id, d.first_name, d.last_name " +
                            "FROM transport.`order` c " +
                            "JOIN transport.`vehicle` v ON (c.vehicle_registration_id = v.registration_id) " +
                            "JOIN transport.`driver` d ON (c.driver_driver_id = d.driver_id);   ";

    @FXML
    private TableView ordersTable;

    @FXML
    private TableColumn orderIdColumn;

    @FXML
    private TableColumn orderDistanceColumn;

    @FXML
    private TableColumn orderStartColumn;

    @FXML
    private TableColumn orderDestinationColumn;
    
    @FXML
    private TableColumn orderStartDateColumn;
    
    @FXML
    private TableColumn orderEndDateColumn;

    @FXML
    private TableColumn orderLoadColumn;

    @FXML
    private TableColumn orderCubatureColumn;

    @FXML
    private TableColumn orderVehicleColumn;

    @FXML
    private TableColumn orderDriverColumn;

    @FXML
    private TableColumn orderIncinerationColumn;

    private DatabaseToolkit databaseToolkit;

    private final int INSERT = 1; 
    private final int UPDATE = 2; 
    private final int SELECT = 3; 

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      orderIdColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
      orderDistanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
      orderStartColumn.setCellValueFactory(new PropertyValueFactory<>("startingPoint"));
      orderDestinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
      orderStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
      orderEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
      orderLoadColumn.setCellValueFactory(new PropertyValueFactory<>("loadWeight"));
      orderCubatureColumn.setCellValueFactory(new PropertyValueFactory<>("cubature"));
      orderVehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleToString"));
      orderDriverColumn.setCellValueFactory(new PropertyValueFactory<>("driverToString"));
      orderIncinerationColumn.setCellValueFactory(new PropertyValueFactory<>("incineration"));

      databaseToolkit = DatabaseToolkit.getInstance();

      ordersTable.setItems(searchData(ALL_DATA));
    }

    public ObservableList<Course> searchData(String query) {
         ObservableList<Course> observableList = FXCollections.observableArrayList();
        try {
            ResultSet resultSet = databaseToolkit.executeQuery(query);

            int counter=0;
            while(resultSet.next()) {
                observableList.add(new Course(resultSet.getInt("c.order_id"),
                        resultSet.getBigDecimal("c.distance"),
                        resultSet.getString("c.starting_point"),
                        resultSet.getString("c.destination"),
                        resultSet.getDate("c.start_date"),
                        resultSet.getDate("c.end_date"),
                        resultSet.getBigDecimal("c.load_weight"),
                        resultSet.getBigDecimal("c.cubature"),
                        resultSet.getBigDecimal("c.incineration"),
                        (resultSet.getString("v.registration_number") + ": " + (resultSet.getString("v.brand"))
                                + " " + (resultSet.getString("v.model"))),
                        (resultSet.getInt("d.driver_id") + ": " + resultSet.getString("d.first_name")
                                + " " + resultSet.getString("d.last_name"))
                ));
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManagerDriversTabController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return observableList;
    }
    
    private void openWindow(int state, Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/transport/project/view/ManagerCoursesManipulateDataView.fxml"));
            
            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setScene(new Scene((Pane) loader.load()));
            
            ManagerCoursesManipulateDataController controller =
                    loader.<ManagerCoursesManipulateDataController>getController();
            if(state==UPDATE) controller.initState(state,course);
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
       String orderToRemove = showRemoveDialog().split(":")[0];
       if(!orderToRemove.equals("")) {
          databaseToolkit.executeUpdate("DELETE FROM `order` WHERE order_id="+orderToRemove+";");
          ordersTable.setItems(searchData(ALL_DATA));
       }
    }
    
    public String showRemoveDialog() {
        ArrayList<String> choices = new ArrayList();
        searchData(ALL_DATA).forEach(x -> choices.add(x.getNumber()+": "+x.getStartingPoint()+" "
                                     +x.getDestination()+" "+x.getDriverToString()));
        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Usuwanie kursu.");
        dialog.setHeaderText("Aby usunąć kurs z systemu, wybierz go z poniższej listy. \n"
                + "TEJ OPERACJI NIE DA SIĘ COFNĄĆ!");
        dialog.setContentText("Kurs do usunięcia: ");
        Optional<String> optional = dialog.showAndWait();
        return optional.get();
        }

    @FXML
    public void update() {
        String courseId = showUpdateDialog();
        if(!courseId.equals(""))
        openWindow(UPDATE,searchData("SELECT c.order_id, c.distance, c.starting_point, c.destination, c.start_date, c.end_date, c.load_weight, c.cubature, c.incineration, " +
                                     " v.registration_number, v.brand, v.model, " +
                                     " d.driver_id, d.first_name, d.last_name " +
                                     "FROM transport.`order` c " +
                                     "JOIN transport.`vehicle` v ON (c.vehicle_registration_id = v.registration_id) " +
                                     "JOIN transport.`driver` d ON (c.driver_driver_id = d.driver_id)" +
                                     " WHERE order_id="+courseId+";").get(0));
    }
    
      public String showUpdateDialog() {
        ArrayList<String> choices = new ArrayList();
        searchData(ALL_DATA).forEach(x -> choices.add(x.getNumber()+": "+x.getStartingPoint()+"->"+x.getDestination()));
        ChoiceDialog<String> dialog = new ChoiceDialog<>("",choices);
        dialog.setTitle("Edytowanie kursu");
        dialog.setHeaderText("Aby edytować dane kursu w systemie, wybierz go z poniższej listy. \n");
        dialog.setContentText("Kurs do modyfikacji: ");
        Optional<String> optional = dialog.showAndWait();
        return optional.get().split(":")[0];
    }
      
    @FXML
    public void select() {
        openWindow(SELECT,null);
    }  
    
    @FXML
    private void refresh() {
        ordersTable.setItems(searchData(ALL_DATA));
    }
}

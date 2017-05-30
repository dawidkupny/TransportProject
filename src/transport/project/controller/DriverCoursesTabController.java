package transport.project.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import transport.project.model.Course;
import transport.project.util.DatabaseToolkit;

public class DriverCoursesTabController implements Initializable {

	private static final String ALL_RESULT = "SELECT o.distance, o.starting_point, o.destination, o.load_weight, o.cubature, o.description, o.incineration, v.brand FROM transport.order o JOIN transport.vehicle v WHERE v.registration_id = o.vehicle_registration_id";

	@FXML
    private TableView<Course> courseTable;

    @FXML
    private TableColumn<Course, String> vehicleColumn;

    @FXML
    private Button searchButton;

    @FXML
    private TableColumn<Course, BigDecimal> loadWeightColumn;

    @FXML
    private TableColumn<Course, BigDecimal> incinerationColumn;

    @FXML
    private TableColumn<Course, BigDecimal> cubatureColumn;

    @FXML
    private TableColumn<Course, String> destinationColumn;

    @FXML
    private TableColumn<Course, String> courseDescriptionColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableColumn<Course, Integer> numberColumn;

    @FXML
    private TableColumn<Course, BigDecimal> distanceColumn;

    @FXML
    private ComboBox<String> searchComboBox;

    @FXML
    private TableColumn<Course, String> startingPointColumn;

    @FXML
    private Label warningLabel;

    DatabaseToolkit dbToolkit = DatabaseToolkit.getInstance();
    ResultSet resultSet;

	private ObservableList<Course> data = FXCollections.observableArrayList();

	private ObservableList<String> criteria = FXCollections.observableArrayList(
			"Dystans",
			"Miejsce początkowe",
			"Miejsce docelowe",
			"Masa ładunku",
			"Rozmiar naczepy",
			"Il. zużyt. benzyny",
			"Ciężarówka");


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//dbToolkit.connect();
		dataBaseSearch(ALL_RESULT);
		searchComboBox.setItems(criteria);

		searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				data.clear();
				try {
					String parameter = searchByCriteria(searchComboBox.getValue());
					String userValue = searchTextField.getText();
					if(userValue.equals("")) {
						warningLabel.setText("Uzupelnij brakujace pola.");
					} else
						dataBaseSearchWithParameter(ALL_RESULT, parameter, userValue);
				}
				catch (NullPointerException e){
					warningLabel.setText("Wybierz kryterium wyszukiwania.");
				}
			}
		});
	}

	private void dataBaseSearch(String query) {
		resultSet = dbToolkit.executeQuery(query);
		data = search(resultSet, data);
		dbToolkit.disconnect();
		courseTable.setItems(data);
	}

	private void dataBaseSearchWithParameter(String query, String parameter, String userValue) {
		resultSet = dbToolkit.executeQuery(query+" AND "+parameter+" = '"+userValue+"';");
		data = search(resultSet, data);
		dbToolkit.disconnect();
		if(data.isEmpty()) {
			warningLabel.setText("Brak wyników");
		} else {
			warningLabel.setText("");
		}
		courseTable.setItems(data);
	}

	private ObservableList<Course> search(ResultSet resultSet, ObservableList<Course> data) {
		int counter = 1;
		try {
			while(resultSet.next()) {
				data.add(new Course(
						counter++,
						resultSet.getBigDecimal("distance"),
						resultSet.getString("starting_point"),
						resultSet.getString("destination"),
						resultSet.getBigDecimal("load_weight"),
						resultSet.getBigDecimal("cubature"),
						resultSet.getString("description"),
						resultSet.getBigDecimal("incineration"),
						resultSet.getString("brand")
						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
		distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
		startingPointColumn.setCellValueFactory(new PropertyValueFactory<>("startingPoint"));
		destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
		loadWeightColumn.setCellValueFactory(new PropertyValueFactory<>("loadWeight"));
		cubatureColumn.setCellValueFactory(new PropertyValueFactory<>("cubature"));
		courseDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		incinerationColumn.setCellValueFactory(new PropertyValueFactory<>("incineration"));
		vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));

		return data;
	}

	private String searchByCriteria(String comboBoxValue) {
		String parameter = null;
		switch(comboBoxValue) {
		case "Dystans":
			parameter = "o.distance";
			break;
		case "Miejsce początkowe":
			parameter = "o.starting_point";
			break;
		case "Miejsce docelowe":
			parameter = "o.destination";
			break;
		case "Masa ładunku":
			parameter = "o.load_weight";
			break;
		case "Rozmiar naczepy":
			parameter = "o.cubature";
			break;
		case "Il. zużyt. benzyny":
			parameter = "o.incineration";
			break;
		case "Ciężarówka":
			parameter = "v.brand";
			break;
		}
			return parameter;
	}
}

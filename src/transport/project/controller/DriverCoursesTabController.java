package transport.project.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import transport.project.model.Course;
import transport.project.util.DatabaseToolkit;

public class DriverCoursesTabController implements Initializable {

	private static final String ALL_RESULT = "SELECT o.distance, o.starting_point, o.destination, o.start_date, o.end_date, o.load_weight, o.cubature, o.incineration, " +
							"v.registration_number, v.brand, v.model, " +
                            "d.first_name, d.last_name " +
                            "FROM transport.order o " +
                            "JOIN transport.vehicle v ON (o.vehicle_registration_id = v.registration_id) " +
                            "JOIN transport.driver d ON (o.driver_driver_id = d.driver_id)";
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
    private TableColumn<Course, Date> startDateColumn;

    @FXML
    private TableColumn<Course, Date> endDateColumn;

    @FXML
    private TableColumn<Course, String> driverColumn;

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
    private DatePicker datePicker;

    @FXML
    private Label warningLabel;

    DatabaseToolkit dbToolkit = DatabaseToolkit.getInstance();
    ResultSet resultSet;

	private ObservableList<Course> data = FXCollections.observableArrayList();

	private ObservableList<String> criteria = FXCollections.observableArrayList(
			"Dystans",
			"Miejsce początkowe",
			"Miejsce docelowe",
			"Data rozpoczęcia",
			"Data zakończenia",
			"Masa ładunku",
			"Rozmiar naczepy",
			"Il. zużyt. benzyny",
			"Nr rejestracyjny");


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		dataBaseSearch(ALL_RESULT);
		searchComboBox.setItems(criteria);

		searchComboBox.valueProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				String parameter = searchComboBox.getSelectionModel().getSelectedItem();
				if(parameter.equals("Data rozpoczęcia") || parameter.equals("Data zakończenia")) {
					datePicker.setVisible(true);
					searchTextField.setVisible(false);
				} else {
					datePicker.setVisible(false);
					searchTextField.setVisible(true);
				}
			}
		});

		searchButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				data.clear();
				try {
					String parameter = searchByCriteria(searchComboBox.getValue());
					String userValue = (datePicker.isVisible()) ? datePicker.getValue().toString() : searchTextField.getText();
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
						resultSet.getDate("start_date"),
						resultSet.getDate("end_date"),
						resultSet.getBigDecimal("load_weight"),
						resultSet.getBigDecimal("cubature"),
						resultSet.getBigDecimal("incineration"),
						(resultSet.getString("v.registration_number") + " : " + (resultSet.getString("v.brand"))
                                + " " + (resultSet.getString("v.model"))),
                        (resultSet.getString("d.first_name")
                                + " " + resultSet.getString("d.last_name"))
						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
		distanceColumn.setCellValueFactory(new PropertyValueFactory<>("distance"));
		startingPointColumn.setCellValueFactory(new PropertyValueFactory<>("startingPoint"));
		destinationColumn.setCellValueFactory(new PropertyValueFactory<>("destination"));
		startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		endDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
		loadWeightColumn.setCellValueFactory(new PropertyValueFactory<>("loadWeight"));
		cubatureColumn.setCellValueFactory(new PropertyValueFactory<>("cubature"));
		incinerationColumn.setCellValueFactory(new PropertyValueFactory<>("incineration"));
		driverColumn.setCellValueFactory(new PropertyValueFactory<>("driverToString"));
		vehicleColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleToString"));

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
		case "Data rozpoczęcia":
			parameter = "o.start_date";
			break;
		case "Data zakończenia":
			parameter = "o.end_date";
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
		case "Nr rejestracyjny":
			parameter = "v.registration_number";
			break;
		}
			return parameter;
	}
}

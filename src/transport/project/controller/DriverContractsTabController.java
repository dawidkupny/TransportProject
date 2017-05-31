package transport.project.controller;

import java.math.BigInteger;
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
import transport.project.model.Contract;
import transport.project.util.DatabaseToolkit;

public class DriverContractsTabController implements Initializable{

	private final String ALL_RESULT = "SELECT contract_number, start_date, expiration_date, salary FROM transport.civil_contract";

    @FXML
    private Button searchButton;

    @FXML
    private TableColumn<Contract, Date> startDateColumn;

    @FXML
    private Label warningLabel;

    @FXML
    private TableColumn<Contract, String> contractNumberColumn;

    @FXML
    private TableColumn<Contract, BigInteger> salaryColumn;

    @FXML
    private TableColumn<Contract, Date> expirationDateColumn;

    @FXML
    private TextField searchTextField;

    @FXML
    private TableColumn<Contract, Integer> numberColumn;

    @FXML
    private ComboBox<String> searchComboBox;

    @FXML
    private TableView<Contract> contractTable;

    @FXML
    private DatePicker datePicker;

    DatabaseToolkit dbToolkit = DatabaseToolkit.getInstance();
    ResultSet resultSet;

    private ObservableList<Contract> data = FXCollections.observableArrayList();

    private ObservableList<String> criteria = FXCollections.observableArrayList(
			"Nr umowy",
			"Data rozpoczęcia",
			"Data zakończenia",
			"Wynagrodzenie"
			);

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
		contractTable.setItems(data);
	}

	private void dataBaseSearchWithParameter(String query, String parameter, String userValue) {
		resultSet = dbToolkit.executeQuery(query+" WHERE "+parameter+" = '"+userValue+"';");
		data = search(resultSet, data);
		dbToolkit.disconnect();
		if(data.isEmpty()) {
			warningLabel.setText("Brak wyników");
		} else {
			warningLabel.setText("");
		}
		contractTable.setItems(data);
	}

	private ObservableList<Contract> search(ResultSet resultSet, ObservableList<Contract> data) {
		int counter = 1;
		try {
			while(resultSet.next()) {
				data.add(new Contract(
						counter++,
						resultSet.getString("contract_number"),
						resultSet.getDate("start_date"),
						resultSet.getDate("expiration_date"),
						resultSet.getBigDecimal("salary"),""
						));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
		contractNumberColumn.setCellValueFactory(new PropertyValueFactory<>("contractNumber"));
		startDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
		expirationDateColumn.setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
		salaryColumn.setCellValueFactory(new PropertyValueFactory<>("salary"));

		return data;
	}

	private String searchByCriteria(String comboBoxValue) {
		String parameter = null;
		switch(comboBoxValue) {
		case "Nr umowy":
			parameter = "contract_number";
			break;
		case "Data rozpoczęcia":
			parameter = "start_date";
			break;
		case "Data zakończenia":
			parameter = "expiration_date";
			break;
		case "Wynagrodzenie":
			parameter = "salary";
			break;
		}
			return parameter;
	}
}

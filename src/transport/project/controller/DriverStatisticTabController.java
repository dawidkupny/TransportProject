package transport.project.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class DriverStatisticTabController implements Initializable{

    @FXML
    private Label lastNameLabel;

    @FXML
    private Label hireDateLabel;

    public Label getLastNameLabel() {
		return lastNameLabel;
	}

	public void setLastNameLabel(Label lastNameLabel) {
		this.lastNameLabel = lastNameLabel;
	}

	public Label getHireDateLabel() {
		return hireDateLabel;
	}

	public void setHireDateLabel(Label hireDateLabel) {
		this.hireDateLabel = hireDateLabel;
	}

	public Label getIdLabel() {
		return idLabel;
	}

	public void setIdLabel(Label idLabel) {
		this.idLabel = idLabel;
	}

	public Label getFirstNameLabel() {
		return firstNameLabel;
	}

	public void setFirstNameLabel(Label firstNameLabel) {
		this.firstNameLabel = firstNameLabel;
	}

	@FXML
    private Label idLabel;

    @FXML
    private Label firstNameLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub

	}

	public void setValues(String firstName, String lastName) {
		firstNameLabel.setText(firstName);
		lastNameLabel.setText(lastName);
	}
}

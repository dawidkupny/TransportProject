package transport.project.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class DriverMenuMainController implements Initializable{

	@FXML
	private DriverStatisticTabController driverStatisticTabController;

	@FXML
	private DriverMessagesTabController driverMessagesTabController;

	@FXML
	private DriverContractsTabController driverContractsTabController;

	@FXML
	private DriverCoursesTabController driverCoursesTabController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Label idLabel = driverStatisticTabController.getIdLabel();

		idLabel.setText("2");

		driverStatisticTabController.setValues("Andrzej", "Nowak");

	}

}

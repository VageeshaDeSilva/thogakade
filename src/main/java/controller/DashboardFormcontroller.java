package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DashboardFormcontroller {
    public AnchorPane mainFrame;
    public JFXButton btn_customer;
    public Label timeLabel;
    public JFXButton btn_items;
    public JFXButton placeOrderBtn;

    public void initialize(){
        calculateTime();
    }

    private void calculateTime() {
        Timeline timeline=new Timeline(new KeyFrame(
                Duration.ZERO,
                actionEvent -> {
                    timeLabel.setText(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME.ofPattern("hh:mm:ss")));
                }
        ),new KeyFrame(Duration.seconds(1)));

        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void customerBtnOnAction(ActionEvent actionEvent){
        Stage stage=(Stage) mainFrame.getScene().getWindow();
        try{
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/CustomerForm.fxml"))));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setTitle("Customer Form");
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void itemBtnOnAction(ActionEvent actionEvent) {
        Stage stage=(Stage) mainFrame.getScene().getWindow();
        try{
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/ItemForm.fxml"))));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setTitle("Item Form");
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void placeOrderBtnOnAction(ActionEvent actionEvent) {
        Stage stage=(Stage) mainFrame.getScene().getWindow();
        try{
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/PlaceOrderForm.fxml"))));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.setTitle("Place Order Form");
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}

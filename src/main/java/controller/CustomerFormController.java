package controller;

import bo.BoFactory;
import bo.custom.CustomerBo;
import bo.custom.impl.CustomerBoImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import dao.custom.CustomerDao;
import dao.util.BoType;
import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import dto.CustomerDto;
import dto.tm.CustomerTm;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.ResourceBundle;

public class CustomerFormController implements Initializable {

    public TableView<CustomerTm> CustomerTable;
    public AnchorPane customerFrame;
    public JFXButton backBtn;
    public ImageView backBtnImg;


    @FXML
    private JFXTextField CustomerID;

    @FXML
    private JFXTextField CustomerName;

    @FXML
    private JFXTextField CustomerAddress;

    @FXML
    private JFXTextField CustomerSalary;

    @FXML
    private JFXButton saveBtn;

    @FXML
    private JFXButton updateBtn;

    @FXML
    private TableColumn colID;

    @FXML
    private TableColumn colName;

    @FXML
    private TableColumn colAddress;

    @FXML
    private TableColumn colSalary;

    @FXML
    private TableColumn colOption;

    @FXML
    private JFXButton reloadBtn;

//    can hide object creation using factory design pattern
    private CustomerBo<CustomerDto> customerBo= BoFactory.getInstance().getBo(BoType.CUSTOMER);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new PropertyValueFactory<>("btn"));
        loadCustomerTable();

        CustomerTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldvalue, newValue) -> {
            setData(newValue);
        });
    }

    private void setData(CustomerTm newValue) {
        if(newValue!=null){
            CustomerID.setEditable(false);
            CustomerID.setText(newValue.getId());
            CustomerName.setText(newValue.getName());
            CustomerAddress.setText(newValue.getAddress());
            CustomerSalary.setText(String.valueOf(newValue.getSalary()));
        }

    }

    public void saveBtnOnAction(javafx.event.ActionEvent actionEvent) {
        try {
            boolean isSaved = customerBo.saveCustomer(new CustomerDto(
                    CustomerID.getText(),
                    CustomerName.getText(),
                    CustomerAddress.getText(),
                    Double.parseDouble(CustomerSalary.getText())
            ));
            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Customer Saved!").show();
                loadCustomerTable();
                clearTextFields();
            }

        } catch (SQLIntegrityConstraintViolationException ex){
            new Alert(Alert.AlertType.ERROR,"Duplicate Entry").show();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadCustomerTable() {
        ObservableList<CustomerTm> tmList = FXCollections.observableArrayList();

        try {
            List<CustomerDto> dtoList = customerBo.allCustomers();

            for (CustomerDto dto:dtoList) {
                JFXButton btn = new JFXButton("Delete");

                CustomerTm c = new CustomerTm(
                        dto.getId(),
                        dto.getName(),
                        dto.getAddress(),
                        dto.getSalary(),
                        btn
                );

                btn.setOnAction(actionEvent -> {
                    deleteCustomer(c.getId());
                });

                tmList.add(c);
            }

            CustomerTable.setItems(tmList);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void deleteCustomer(String id) {
        try {
            boolean isDeleted = customerBo.deleteCustomer(id);
            if (isDeleted){
                new Alert(Alert.AlertType.INFORMATION,"Customer Deleted!").show();
                loadCustomerTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBtnOnAction(javafx.event.ActionEvent actionEvent) {
        try {
            boolean isUpdated = customerBo.updateCustomer(new CustomerDto(CustomerID.getText(),
                    CustomerName.getText(),
                    CustomerAddress.getText(),
                    Double.parseDouble(CustomerSalary.getText())
            ));
            if (isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Customer Updated!").show();
                loadCustomerTable();
                clearTextFields();
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void reloadBtnOnAction(javafx.event.ActionEvent actionEvent) {
        loadCustomerTable();
        CustomerTable.refresh();
        clearTextFields();
    }

    private void clearTextFields() {
        CustomerID.clear();
        CustomerName.clear();
        CustomerAddress.clear();
        CustomerSalary.clear();
        CustomerID.setEditable(true);
    }


    public void backBtnOnAction(javafx.event.ActionEvent actionEvent) {
        Stage stage=(Stage) CustomerTable.getScene().getWindow();
        try{
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}


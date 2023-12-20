package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.CustomerDto;
import dto.ItemDto;
import dto.tm.CustomerTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import dto.tm.ItemTm;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemFormController {

    public JFXButton backBtn;
    public JFXButton searchBtn;
    public GridPane itemFormFrame;
    public JFXButton updateBtn;
    @FXML
    private JFXTextField itemCodeTextField;

    @FXML
    private JFXTextField descriptionTextField;

    @FXML
    private JFXTextField unitPriceTextField;

    @FXML
    private JFXTextField qtyTextField;

    @FXML
    private JFXTreeTableView<ItemTm> itemTable;

    @FXML
    private TreeTableColumn itemcodeCol;

    @FXML
    private TreeTableColumn descriptionCol;

    @FXML
    private TreeTableColumn unitPriceCol;

    @FXML
    private TreeTableColumn qtyCol;

    @FXML
    private TreeTableColumn optionCol;


    public void initialize(){
        itemcodeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        descriptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        unitPriceCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitPrice"));
        qtyCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        optionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));
        loadItemTable();

        itemTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldvalue, newValue) -> {
            setData(newValue.getValue());
        });
    }

    private void loadItemTable() {
        ObservableList<ItemTm> tmList = FXCollections.observableArrayList();
        String sql="select * from item";
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet result = stm.executeQuery(sql);

            while(result.next()){
                JFXButton btn=new JFXButton("Delete");
                ItemTm item=new ItemTm(
                        result.getString(1),
                        result.getString(2),
                        result.getDouble(3),
                        result.getInt(4),
                        btn
                );

                btn.setOnAction(actionEvent -> {
                    deleteItem(item.getCode());
                });

                tmList.add(item);
            }

            RecursiveTreeItem<ItemTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
            itemTable.setRoot(treeItem);
            itemTable.setShowRoot(false);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteItem(String itemCode) {
        String sql="DELETE FROM item WHERE code='"+itemCode+"'";
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            int rst = stm.executeUpdate(sql);
            if(rst>0){
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted !").show();
                loadItemTable();
            }
            else{
                new Alert(Alert.AlertType.ERROR,"Item Not Deleted !").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setData(ItemTm newValue) {
        if(newValue!=null){
            itemCodeTextField.setEditable(false);
            itemCodeTextField.setText(newValue.getCode());
            descriptionTextField.setText(newValue.getDescription());
            unitPriceTextField.setText(String.valueOf(newValue.getUnitPrice()));
            qtyTextField.setText(String.valueOf(newValue.getQty()));
        }
    }

    public void backBtnOnAction(javafx.event.ActionEvent actionEvent) {
        Stage stage = (Stage) itemTable.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveBtnOnAction(ActionEvent actionEvent) {
        ItemDto item=new ItemDto(itemCodeTextField.getText(),descriptionTextField.getText(),Double.parseDouble(unitPriceTextField.getText()),Integer.parseInt(qtyTextField.getText()));
        String sql="insert into item values(?,?,?,?)";
        try {
            PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
            pstm.setString(1,item.getCode());
            pstm.setString(2,item.getDescription());
            pstm.setDouble(3,item.getUnitPrice());
            pstm.setInt(4,item.getQty());
            int rst = pstm.executeUpdate();
            if(rst>0){
                new Alert(Alert.AlertType.INFORMATION,"Item Saved !").show();
                loadItemTable();
                clearTextFields();
            }

        } catch (ClassNotFoundException | SQLException e) {
            // throw new RuntimeException(e);
            new Alert(Alert.AlertType.ERROR,"Invalid Item !").show();
        }
    }

    private void clearTextFields() {
        itemCodeTextField.clear();
        descriptionTextField.clear();
        unitPriceTextField.clear();
        qtyTextField.clear();
    }

    public void updateBtnOnAction(ActionEvent actionEvent) {
        ItemDto item=new ItemDto(itemCodeTextField.getText(),descriptionTextField.getText(),Double.parseDouble(unitPriceTextField.getText()),Integer.parseInt(qtyTextField.getText()));
        String sql="UPDATE Customer SET description='"+item.getDescription()+"',unitPrice='"+item.getUnitPrice()+"',qtyOnHand='"+item.getQty()+"' where code='"+item.getCode()+"'";
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            int rst = stm.executeUpdate(sql);
            if(rst>0){
                new Alert(Alert.AlertType.INFORMATION,"Item Updated !").show();
                loadItemTable();
                clearTextFields();
            }

        } catch (ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Invalid Update !").show();
            throw new RuntimeException(e);

        }
    }

    public void searchBtnOnAction(ActionEvent actionEvent) {

    }
}

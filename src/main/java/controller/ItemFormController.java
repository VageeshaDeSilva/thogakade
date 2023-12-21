package controller;

import bo.BoFactory;
import bo.custom.ItemBo;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import bo.BoType;
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
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import dto.tm.ItemTm;

import java.io.IOException;
import java.sql.*;
import java.util.List;

public class ItemFormController {

    public JFXButton backBtn;
    public JFXButton searchBtn;
    public GridPane itemFormFrame;
    public JFXButton updateBtn;
    public JFXButton saveBtn;
    public JFXTextField searchItem;
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

    ItemBo<ItemDto> itemBo= BoFactory.getInstance().getBo(BoType.ITEM);

    public void initialize() throws SQLException, ClassNotFoundException {
        itemcodeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        descriptionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        unitPriceCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitPrice"));
        qtyCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("qtyOnHand"));
        optionCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));
        loadItemTable();

        itemTable.getSelectionModel().selectedItemProperty().addListener((observableValue, oldvalue, newValue) -> {
            setData(newValue.getValue());
        });
    }

    private void loadItemTable() throws SQLException, ClassNotFoundException {
            ObservableList<ItemTm> tmList = FXCollections.observableArrayList();

            List<ItemDto> dtoList = itemBo.allItems();

            for (ItemDto dto:dtoList) {
                JFXButton btn = new JFXButton("Delete");

                ItemTm tm = new ItemTm(
                        dto.getCode(),
                        dto.getDescription(),
                        dto.getUnitPrice(),
                        dto.getQtyOnHand(),
                        btn
                );

                btn.setOnAction(actionEvent -> {
                    try {
                        deleteItem(tm.getCode());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                });

                tmList.add(tm);
            }

        TreeItem<ItemTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
        itemTable.setRoot(treeItem);
        itemTable.setShowRoot(false);
    }

    private void setData(ItemTm newValue) {
        if(newValue!=null){
            itemCodeTextField.setEditable(false);
            itemCodeTextField.setText(newValue.getCode());
            descriptionTextField.setText(newValue.getDescription());
            unitPriceTextField.setText(String.valueOf(newValue.getUnitPrice()));
            qtyTextField.setText(String.valueOf(newValue.getQtyOnHand()));
        }
    }

    public void saveBtnOnAction() throws SQLException, ClassNotFoundException {
        try {
            boolean isSaved = itemBo.saveItem(new ItemDto(
                    itemCodeTextField.getText(),
                    descriptionTextField.getText(),
                    Double.parseDouble(unitPriceTextField.getText()),
                    Integer.parseInt(qtyTextField.getText())
            ));
            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Item Saved!").show();
                loadItemTable();
                clearTextFields();
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBtnOnAction() throws SQLException, ClassNotFoundException {
        try {
            boolean isUpdated = itemBo.updateItem(new ItemDto(
                    itemCodeTextField.getText(),
                    descriptionTextField.getText(),
                    Double.parseDouble(unitPriceTextField.getText()),
                    Integer.parseInt(qtyTextField.getText())
            ));
            if (isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Item Updated!").show();
                loadItemTable();
                clearTextFields();
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void deleteItem(String itemCode) throws SQLException, ClassNotFoundException {
        boolean isDeleted = itemBo.deleteItem(itemCode);
        try {
            if (isDeleted){
                new Alert(Alert.AlertType.INFORMATION,"Item Deleted!").show();
                loadItemTable();
            }else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong!").show();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void searchBtnOnAction(ActionEvent actionEvent) {
        String itemC = searchItem.getText();
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

    private void clearTextFields() {
        itemCodeTextField.clear();
        descriptionTextField.clear();
        unitPriceTextField.clear();
        qtyTextField.clear();
    }
}

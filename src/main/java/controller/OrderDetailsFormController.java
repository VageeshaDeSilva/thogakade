package controller;

import bo.custom.OrderDetailsBo;
import bo.custom.impl.OrderDetailsBoImpl;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dto.OrderDetailsDto;
import dto.OrderDtoNew;
import dto.tm.OrderDetailsTm;
import dto.tm.OrderTmNew;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class OrderDetailsFormController {

    public JFXTextField searchIdTxtField;
    public TreeTableColumn orderIdCol;
    public TreeTableColumn dateCol;
    public TreeTableColumn customerIdCol;
    public TreeTableColumn detailsOrderIdCol;
    public TreeTableColumn detailsItemCodeCol;
    public TreeTableColumn detailsQtyCol;
    public TreeTableColumn detailsPriceCol;
    public JFXButton reloadBtn;
    public JFXButton backBtn;
    public JFXTreeTableView detailTable;
    public JFXTreeTableView orderTable;

    OrderDetailsBo orderDetailsBo=new OrderDetailsBoImpl();
    public void initialize(){
        orderIdCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        dateCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("date"));
        customerIdCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("customerId"));

        detailsOrderIdCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("orderId"));
        detailsItemCodeCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("itemCode"));
        detailsQtyCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        detailsPriceCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitprice"));

        loadOrderTable();

        orderTable.setOnMouseClicked(event -> {
            if(event.getClickCount()==1&&(!orderTable.getSelectionModel().isEmpty())){
                TreeItem<OrderTmNew> item = (TreeItem<OrderTmNew>) orderTable.getSelectionModel().getSelectedItem();
                try {
                    ObservableList<OrderDetailsTm> tmList = FXCollections.observableArrayList();
                    List<OrderDetailsDto> dtoList = orderDetailsBo.getOrderDetails(item.getValue().getId());
                    for(OrderDetailsDto dto:dtoList){
                        tmList.add(
                                new OrderDetailsTm(
                                        dto.getOrderId(),
                                        dto.getItemCode(),
                                        dto.getQty(),
                                        dto.getUnitPrice()
                                )
                        );
                    }
//                    TreeItem<OrderDetailsTm> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
//                    orderTable.setRoot(treeItem);
//                    orderTable.setShowRoot(false);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch(ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        searchIdTxtField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                orderTable.setPredicate(new Predicate<TreeItem<OrderTmNew>>() {
                    @Override
                    public boolean test(TreeItem<OrderTmNew> treeItem) {
                        return treeItem.getValue().getId().contains(newValue);
                    }
                });
            }
        });

    }

    private void loadOrderTable() {
        ObservableList<OrderDtoNew> tmList = FXCollections.observableArrayList();
        try {
            List<OrderDtoNew> dtolist = orderDetailsBo.allOrders();
            for(OrderDtoNew order:dtolist){
                tmList.add(
                        new OrderDtoNew(
                                order.getOrderId(),
                                order.getDate(),
                                order.getCustomerId()
                        )
                );
            }
//            TreeItem<OrderDtoNew> treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
//            orderTable.setRoot(treeItem);
//            orderTable.setShowRoot(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void reloadBtnOnAction(ActionEvent actionEvent) {
    }

    public void backBtnOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) detailTable.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

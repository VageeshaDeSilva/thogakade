package controller;

import bo.BoFactory;
import bo.custom.CustomerBo;
import bo.custom.ItemBo;
import bo.custom.OrderBo;
import bo.custom.impl.OrderBoImpl;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import bo.BoType;
import dto.CustomerDto;
import dto.ItemDto;
import dto.OrderDetailsDto;
import dto.OrderDto;
import dto.tm.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import dao.custom.OrderDao;
import dao.custom.impl.OrderDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static javafx.beans.binding.Bindings.isEmpty;

public class PlaceOrderFormController {
    public JFXButton backBtn;
    public AnchorPane placeOrderFrame;
    public JFXComboBox customerIdComboBox;
    public JFXComboBox itemCodeComboBox;
    public JFXTextField nameTextField;
    public JFXTextField descriptionTextField;
    public JFXTextField unitPriceTextField;
    public JFXTextField qtyTextField;
    public JFXButton addToCartBtn;
    public JFXTreeTableView cartTable;
    public TreeTableColumn codeTabelCol;
    public TreeTableColumn descriptionTabelCol;
    public TreeTableColumn qtyTabelCol;
    public TreeTableColumn amountTabelCol;
    public TreeTableColumn optionTabelCol;
    public JFXButton placeOrderBtn;
    public Label orderIdLabel;
    public Label totalLabel;
    public Label idLabel;

    private List<CustomerDto> customers;
    private List<ItemDto> items;
    private double total = 0;

    private CustomerBo<CustomerDto> customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private ItemBo<ItemDto> itemBo = BoFactory.getInstance().getBo(BoType.ITEM);
    private OrderBo orderBo = new OrderBoImpl();

    private ObservableList<OrderTm> tmList = FXCollections.observableArrayList();

    public void initialize() throws SQLException, ClassNotFoundException {
        codeTabelCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        descriptionTabelCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("description"));
        qtyTabelCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        amountTabelCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("amount"));
        optionTabelCol.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

       String gId=orderBo.generateId();
        idLabel.setText(gId);
        loadCustomerIds();
        loadItemCodes();

        customerIdComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, id) -> {
            for (CustomerDto dto:customers) {
                if (dto.getId().equals(id)){
                    nameTextField.setText(dto.getName());
                }
            }
        });

        itemCodeComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, code) -> {
            for (ItemDto dto:items) {
                if (dto.getCode().equals(code)){
                    descriptionTextField.setText(dto.getDescription());
                    unitPriceTextField.setText(String.format("%.2f",dto.getUnitPrice()));
                }
            }
        });
    }

    private void loadItemCodes() {
        try {
            items = itemBo.allItems();
            ObservableList list = FXCollections.observableArrayList();
            for (ItemDto dto:items) {
                list.add(dto.getCode());
            }
            itemCodeComboBox.setItems(list);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomerIds() {
        try {
            customers = customerBo.allCustomers();
            ObservableList<Object> list = FXCollections.observableArrayList();
            for (CustomerDto dto:customers) {
                list.add(dto.getId());
            }
            customerIdComboBox.setItems(list);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void backBtnOnAction() {
        Stage stage = (Stage) placeOrderFrame.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/DashboardForm.fxml"))));
            stage.setResizable(false);
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void placeOrderBtnOnAction() throws SQLException, ClassNotFoundException {
        orderBo.generateId();
      if(!isEmpty()){
          List<OrderDetailsDto> list = new ArrayList<>();
          for (OrderTm tm:tmList) {
              list.add(new OrderDetailsDto(
                      orderIdLabel.getText(),
                      tm.getCode(),
                      tm.getQty(),
                      tm.getAmount()/tm.getQty()
              ));
          }

          OrderDto dto = new OrderDto(
                  orderIdLabel.getText(),
                  LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")),
                  customerIdComboBox.getValue().toString(),
                  list
          );


          if(!tmList.isEmpty()){
              boolean isSaved = orderBo.saveOrder(dto);
              if (isSaved){
                  new Alert(Alert.AlertType.INFORMATION, "Order Saved!").show();
                  setOrderId();
              }else{
                  new Alert(Alert.AlertType.ERROR, "Order not Saved!").show();
              }
          }
      }
      else {
          new Alert(Alert.AlertType.ERROR,"Empty field found").show();
      }
    }

    public boolean isEmpty(){
        if(qtyTextField.getText().isEmpty()||descriptionTextField.getText().isEmpty()||unitPriceTextField.getText().isEmpty()){
            return true;
        }else{
            return false;
        }
    }

    private void setOrderId() {
        try {
            orderIdLabel.setText(orderBo.generateId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    public void addToCartBtnOnAction() throws RuntimeException {
        JFXButton btn = new JFXButton("Delete");

        OrderTm tm = new OrderTm(
                itemCodeComboBox.getValue().toString(),
                descriptionTextField.getText(),
                Integer.parseInt(qtyTextField.getText()),
                Double.parseDouble(unitPriceTextField.getText())*Integer.parseInt(qtyTextField.getText()),
                btn
        );
        btn.setOnAction(actionEvent -> {
            tmList.remove(tm);
            total-=tm.getAmount();
            totalLabel.setText(String.format("%.2f",total));
            cartTable.refresh();
        });
        boolean isExist = false;
        for (OrderTm order:tmList) {
            if (order.getCode().equals(tm.getCode())){
                order.setQty(order.getQty()+tm.getQty());
                order.setAmount(order.getAmount()+tm.getAmount());
                isExist = true;
                total+= tm.getAmount();
            }
        }
        if (!isExist){
            tmList.add(tm);
            total+=tm.getAmount();
        }

        totalLabel.setText(String.format("%.2f",total));

        TreeItem treeItem = new RecursiveTreeItem<>(tmList, RecursiveTreeObject::getChildren);
        cartTable.setRoot(treeItem);
        cartTable.setShowRoot(false);
    }
}

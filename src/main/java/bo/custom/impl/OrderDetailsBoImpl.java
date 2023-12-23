package bo.custom.impl;

import bo.custom.OrderDetailsBo;
import dao.custom.OrderDao;
import dao.custom.OrderDetailsDao;
import dao.custom.impl.OrderDaoImpl;
import dao.custom.impl.OrderDetailsDaoImpl;
import dto.OrderDetailsDto;
import dto.OrderDtoNew;
import entity.OrderDetails;
import entity.Orders;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsBoImpl implements OrderDetailsBo {
    OrderDao orderDao = new OrderDaoImpl();
    OrderDetailsDao orderDetailsDo = new OrderDetailsDaoImpl();
    public List<OrderDetailsDto> getOrderDetails(String id) throws SQLException, ClassNotFoundException {
        List<OrderDetails> orderDetails= orderDetailsDo.getOrderDetails(id);
        List<OrderDetailsDto> dtoList = new ArrayList<>();
        for (OrderDetails OD:orderDetails){
            dtoList.add(
                    new OrderDetailsDto(
                            OD.getOrderId(),
                            OD.getItemCode(),
                            OD.getQty(),
                            OD.getUnitPrice()
                    )
            );
        }
        return dtoList;
    }
    public List<OrderDtoNew> allOrders() throws SQLException, ClassNotFoundException {
        List<Orders> orderslist=orderDao.getAll();
        List<OrderDtoNew> orderDetailsDtoslist = new ArrayList<>();
        for (Orders orders:orderslist){
            orderDetailsDtoslist.add(
                    new OrderDtoNew(
                            orders.getOrderId(),
                            orders.getDate(),
                            orders.getCustomerId()
                    )
            );
        }
        return orderDetailsDtoslist;
    }
}

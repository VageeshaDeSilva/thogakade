package bo.custom.impl;

import bo.custom.OrderBo;
import dao.custom.OrderDao;
import dao.custom.OrderDetailsDao;
import dao.custom.impl.OrderDaoImpl;
import dao.custom.impl.OrderDetailsDaoImpl;
import dto.OrderDto;
import dto.OrderDtoNew;
import entity.Orders;

import java.sql.SQLException;

public class OrderBoImpl implements OrderBo {
    private OrderDao orderDao = new OrderDaoImpl();
    OrderDetailsDao orderDetailsDao = new OrderDetailsDaoImpl();

    @Override
    public boolean saveOrder(OrderDto dto) throws SQLException, ClassNotFoundException {
        boolean isSaveorder=orderDao.save(
                new Orders(
                        dto.getOrderId(),
                        dto.getDate(),
                        dto.getCustId()
                )
        );
        if(isSaveorder) {
            boolean isSavedDetails=orderDetailsDao.saveOrderDetails(dto.getList());
            if(isSavedDetails){
                return true;
            }
        }
        return false;
    }

    @Override
    public String generateId() throws SQLException, ClassNotFoundException {
        try {
            OrderDtoNew dto = lastOrder();
            if (dto!=null){
                String id = dto.getOrderId();
                int num = Integer.parseInt(id.split("D")[1]);
                num++;
                return String.format("D%03d",num);
            }else{
                return "D001";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public OrderDtoNew lastOrder() throws SQLException, ClassNotFoundException {
        Orders order = orderDao.lastOrder();
        return new OrderDtoNew(
                order.getOrderId(),
                order.getDate(),
                order.getCustomerId()
        );
    }
}

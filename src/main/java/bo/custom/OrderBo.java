package bo.custom;

import bo.SuperBo;
import dto.OrderDto;
import dto.OrderDtoNew;

import java.sql.SQLException;

public interface OrderBo extends SuperBo {
    boolean saveOrder(OrderDto dto)throws SQLException, ClassNotFoundException;
    String generateId() throws SQLException, ClassNotFoundException;
    OrderDtoNew lastOrder() throws SQLException, ClassNotFoundException;
}

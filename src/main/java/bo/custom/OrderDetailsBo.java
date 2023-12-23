package bo.custom;

import dto.OrderDetailsDto;
import dto.OrderDtoNew;

import java.sql.SQLException;
import java.util.List;

public interface OrderDetailsBo {
    public List<OrderDetailsDto> getOrderDetails(String id) throws SQLException, ClassNotFoundException;
    public List<OrderDtoNew> allOrders() throws SQLException, ClassNotFoundException;
}

package dao.custom.impl;

import db.DBConnection;
import dto.OrderDto;
import dao.custom.OrderDetailsDao;
import dao.custom.OrderDao;
import entity.Orders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {

    OrderDetailsDao orderDetailsDao = new OrderDetailsDaoImpl();
    @Override
    public boolean save(Orders entity) throws SQLException, ClassNotFoundException {
        Connection connection=null;
        try {
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            String sql = "INSERT INTO orders VALUES(?,?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1, entity.getOrderId());
            pstm.setString(2, entity.getDate());
            pstm.setString(3, entity.getCustomerId());
            int result =pstm.executeUpdate();
            return result>0;
        }catch (SQLException | ClassNotFoundException ex){
            connection.rollback();
//            ex.printStackTrace();
        }finally {
            connection.setAutoCommit(true);
        }
        return false;
    }

    @Override
    public List<Orders> getAll() throws SQLException, ClassNotFoundException {
        List<Orders> list = new ArrayList<>();
        String sql = "select * from orders";
        PreparedStatement prstm  = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet result = prstm.executeQuery();
        while (result.next()){
            list.add(
                    new Orders(
                            result.getString(1),
                            result.getString(2),
                            result.getString(3)
                    )
            );
        }
        return list;
    }


    @Override
    public Orders lastOrder() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * FROM orders order BY id DESC LIMIT 1";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet = pstm.executeQuery();

        if (resultSet.next()){
            return new Orders(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3)
            );
        }

        return null;
    }

    @Override
    public boolean update(Orders entity) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        return false;
    }


}

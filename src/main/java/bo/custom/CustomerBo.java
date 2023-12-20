package bo.custom;

import bo.SuperBo;

import java.sql.SQLException;

public interface CustomerBo<T> extends SuperBo {
    boolean saveCustomer(T dto) throws SQLException, ClassNotFoundException;
}

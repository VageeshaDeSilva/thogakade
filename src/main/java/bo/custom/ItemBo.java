package bo.custom;

import bo.SuperBo;
import dto.ItemDto;

import java.sql.SQLException;
import java.util.List;

public interface ItemBo<T> extends SuperBo {
    boolean saveItem(T dto) throws SQLException, ClassNotFoundException;
    boolean updateItem(T dto) throws SQLException, ClassNotFoundException;
    boolean deleteItem(String id) throws SQLException, ClassNotFoundException;
    List<ItemDto> allItems() throws SQLException, ClassNotFoundException;

    ItemDto getItem(String code);

}

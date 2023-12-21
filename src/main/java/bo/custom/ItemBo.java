package bo.custom;

import bo.SuperBo;
import dto.ItemDto;

import java.util.List;

public interface ItemBo<T> extends SuperBo {
    boolean saveItem(T dto);
    boolean updateItem(T dto);
    boolean deleteItem(String id);
    List<ItemDto> allItems();
}

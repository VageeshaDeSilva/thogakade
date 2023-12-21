package bo.custom.impl;

import bo.custom.ItemBo;
import dto.ItemDto;

import java.util.List;

public class ItemBoImpl implements ItemBo {
    @Override
    public boolean saveItem(Object dto) {
        return false;
    }

    @Override
    public boolean updateItem(Object dto) {
        return false;
    }

    @Override
    public boolean deleteItem(String id) {
        return false;
    }

    @Override
    public List<ItemDto> allItems() {
        return null;
    }
}

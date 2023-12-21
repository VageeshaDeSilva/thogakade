package bo.custom.impl;

import bo.BoFactory;
import bo.BoType;
import bo.custom.ItemBo;
import dao.DaoFactory;
import dao.Daotype;
import dao.custom.ItemDao;
import dao.custom.impl.ItemDaoImpl;
import dto.CustomerDto;
import dto.ItemDto;
import entity.Customer;
import entity.Item;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBoImpl implements ItemBo<ItemDto> {

    private ItemDao itemDao=new ItemDaoImpl();
//            DaoFactory.getInstance().getDao(Daotype.ITEM);

    @Override
    public boolean saveItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.save(new Item(
                dto.getCode(),
                dto.getDescription(),
                dto.getUnitPrice(),
                dto.getQtyOnHand()
        ));
    }

    @Override
    public boolean updateItem(ItemDto dto) throws SQLException, ClassNotFoundException {
        return itemDao.update(new Item(
                dto.getCode(),
                dto.getDescription(),
                dto.getUnitPrice(),
                dto.getQtyOnHand()
        ));
    }

    @Override
    public boolean deleteItem(String id) throws SQLException, ClassNotFoundException {
        return itemDao.delete(id);
    }

    @Override
    public List<ItemDto> allItems() throws SQLException, ClassNotFoundException {
        List<Item> entityList = itemDao.getAll();
        List<ItemDto> list = new ArrayList<>();
        for(Item item:entityList){
            list.add(new ItemDto(
                    item.getCode(),
                    item.getDescription(),
                    item.getUnitPrice(),
                    item.getQtyOnHand()
            ));
        }
        return list;
    }

    @Override
    public ItemDto getItem(String id) {
        return null;
    }

}

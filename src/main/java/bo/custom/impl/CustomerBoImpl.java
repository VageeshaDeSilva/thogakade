package bo.custom.impl;

import bo.custom.CustomerBo;
import dao.CrudDao;
import dao.custom.CustomerDao;
import dao.custom.impl.CustomerDaoImpl;
import dto.CustomerDto;
import entity.Customer;

import java.sql.SQLException;

public class CustomerBoImpl implements CustomerBo<CustomerDto> {

    private CustomerDao customerDao=new CustomerDaoImpl();
    @Override
    public boolean saveCustomer(CustomerDto dto) throws SQLException, ClassNotFoundException {
        return customerDao.save(new Customer(
                dto.getId(),
                dto.getName(),
                dto.getAddress(),
                dto.getSalary()
        ));
    }
}

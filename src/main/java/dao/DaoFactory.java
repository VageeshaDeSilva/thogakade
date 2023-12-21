package dao;

import bo.SuperBo;
import dao.custom.impl.CustomerDaoImpl;
import dao.util.Daotype;

public class DaoFactory {
    private static DaoFactory daoFactory;

    private DaoFactory() {
    }

    public static DaoFactory getInstance(){
        return daoFactory!=null?daoFactory:(daoFactory=new DaoFactory());
    }

    public <T extends SuperDao>T getDao(Daotype type){
        switch (type){
            case CUSTOMER:return(T) new CustomerDaoImpl();
        }
        return null;
    }
}

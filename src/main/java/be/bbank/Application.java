package be.bbank;

import java.sql.SQLException;

import be.bbank.dao.BTitularDao;
import be.bbank.imp.BTitularDAOImpl;
import be.bbank.models.BTitular;

public class Application {

    public static void main(String[] args) throws SQLException {
        BTitular bTitular = new BTitular();
        bTitular.setFirstName("Van Bellinghen");
        bTitular.setLastName("Brian");
        bTitular.setBirthDate(java.sql.Date.valueOf("1997-09-08"));

        BTitularDao bTitularDao = new BTitularDAOImpl();
        bTitularDao.create(bTitular);

        /* 
        BBankAccount bUser = new BBankAccount();
        bUser.addTitular(bTitular);
        try {
            bUser.setBankAccount("BE12 3456 7891 1234");
        }  catch (Exception e) {
            e.printStackTrace();
        }
        bUser.setAccountType(EnumAccountType.COURANT);
        try {
            bUser.setBalance(bTitular, 1000);
        }  catch (Exception e) {
            e.printStackTrace();
        }
        
        BTitularDao bTitularDao = new BTitularDAOImpl();
        bTitularDao.create(bTitular);

        BBankAccountDao bBankAccountDAO = new BBankAccountDAOImpl();
        bBankAccountDAO.create(bUser);
        */
    }
}

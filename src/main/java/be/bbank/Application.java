package be.bbank;

import java.sql.SQLException;

import be.bbank.dao.BBankAccountDao;
import be.bbank.dao.BTitularDao;
import be.bbank.imp.BBankAccountDAOImpl;
import be.bbank.imp.BTitularDAOImpl;
import be.bbank.models.BBankAccount;
import be.bbank.models.BTitular;
import be.bbank.models.EnumAccountType;

public class Application {

    public static void main(String[] args) throws SQLException {
        BTitularDao bTitularDao = new BTitularDAOImpl();
        BBankAccountDao bBankAccountDAO = new BBankAccountDAOImpl();

        BTitular bTitular = new BTitular();
        bTitular.setFirstName("Van Bellinghen");
        bTitular.setLastName("Brian");
        bTitular.setBirthDate(java.sql.Date.valueOf("1997-09-08"));
        bTitularDao.create(bTitular);

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

        bBankAccountDAO.create(bUser);
    }
}

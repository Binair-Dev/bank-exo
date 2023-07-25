package be.bbank;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

public class BBankUtils {

    private static final EntityManager em = Persistence.createEntityManagerFactory("bbank").createEntityManager();

    public static int calculateDifferenceInYears(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        int yearDiff = cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR);

        if (cal2.get(Calendar.MONTH) < cal1.get(Calendar.MONTH)
                || (cal2.get(Calendar.MONTH) == cal1.get(Calendar.MONTH)
                && cal2.get(Calendar.DAY_OF_MONTH) < cal1.get(Calendar.DAY_OF_MONTH))) {
            yearDiff--;
        }

        return yearDiff;
    }

    public static EntityManager getEm() {
        return em;
    }
}

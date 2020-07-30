package conventionhub.DAO;

import conventionhub.pojos.ChitietHoinghi;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ChiTietHoinghiDAO {
    
    public static void saveOrUpdateChitiet(Session session, ChitietHoinghi ct){
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(ct);
            tx.commit(); // Flush happens automatically
        }
        catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace(System.err); // or display error message
        }
    }
    
}

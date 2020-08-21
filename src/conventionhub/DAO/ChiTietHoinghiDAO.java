package conventionhub.DAO;

import conventionhub.pojos.ChitietHoinghi;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ChiTietHoinghiDAO {
    
    public static void saveOrUpdateChitiet(Session session, ChitietHoinghi ct){
        session.saveOrUpdate(ct);
    }
    
}

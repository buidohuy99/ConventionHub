package conventionhub.DAO;

import conventionhub.pojos.TinhtrangxoaDiadiem;
import conventionhub.pojos.TinhtrangxoaHoinghi;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TinhTrangXoaHoinghiDAO {
    public static TinhtrangxoaHoinghi getTinhtrangxoa_forHoinghi(Session session, Integer mahn){
        if(session == null || mahn == null) return null;
        
        String Query = "from TinhtrangxoaHoinghi ttxoa where ttxoa.idtinhtrangxoaHoinghi = :mahn";
        Query findHN = session.createQuery(Query);
        findHN.setParameter("mahn", mahn);
        TinhtrangxoaHoinghi found = (TinhtrangxoaHoinghi) findHN.uniqueResult();
        
        return found;      
    }
    
    public static void addTinhtrangxoa(Session session, TinhtrangxoaHoinghi tt){
        if(session == null) return;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.save(tt);
            tx.commit(); // Flush happens automatically
        }
        catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace(System.err); // or display error message
        }
    }
    
    public static void updateTinhtrangxoa(Session session, TinhtrangxoaHoinghi tt){
        if(session == null) return;
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(tt);
            tx.commit(); // Flush happens automatically
        }
        catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace(System.err); // or display error message
        }
    }
}

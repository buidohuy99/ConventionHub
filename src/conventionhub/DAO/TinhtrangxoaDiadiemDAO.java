package conventionhub.DAO;

import conventionhub.pojos.TinhtrangxoaDiadiem;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TinhtrangxoaDiadiemDAO {
    public static TinhtrangxoaDiadiem getTinhtrangxoa_forDiadiem(Session session, Integer madd){
        if(session == null || madd == null) return null;
        
        String Query = "from TinhtrangxoaDiadiem ttxoa where ttxoa.madiadiem = :madd";
        Query findHN = session.createQuery(Query);
        findHN.setParameter("madd", madd);
        TinhtrangxoaDiadiem found = (TinhtrangxoaDiadiem) findHN.uniqueResult();
        
        return found;      
    }
    
    public static void addTinhtrangxoa(Session session, TinhtrangxoaDiadiem tt){
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
    
    public static void updateTinhtrangxoa(Session session, TinhtrangxoaDiadiem tt){
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

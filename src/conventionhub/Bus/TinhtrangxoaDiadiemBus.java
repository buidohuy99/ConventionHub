package conventionhub.Bus;

import conventionhub.DAO.TinhtrangxoaDiadiemDAO;
import conventionhub.pojos.Diadiem;
import conventionhub.pojos.TinhtrangxoaDiadiem;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;

public class TinhtrangxoaDiadiemBus {
    public static TinhtrangxoaDiadiem getTinhtrangxoa_forDiadiem(Integer madd){
        if(madd == null) return null;
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.getTransaction().begin();
        TinhtrangxoaDiadiem tt = TinhtrangxoaDiadiemDAO.getTinhtrangxoa_forDiadiem(session, madd);    
        session.getTransaction().commit();
        session.close();
        return tt;
    }
}

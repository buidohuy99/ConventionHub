package conventionhub.Bus;

import conventionhub.DAO.TinhTrangXoaHoinghiDAO;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.TinhtrangxoaHoinghi;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;

public class TinhTrangXoaHoinghiBus {
    public static TinhtrangxoaHoinghi getTinhtrangxoa_forHoinghi(Integer mahn){
        if(mahn == null) return null;
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.getTransaction().begin();
        TinhtrangxoaHoinghi tt = TinhTrangXoaHoinghiDAO.getTinhtrangxoa_forHoinghi(session, mahn);    
        session.getTransaction().commit();
        session.close();
        return tt;
    }
}

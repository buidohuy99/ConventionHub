package conventionhub.DAO;

import conventionhub.pojos.Dangkyhoinghi;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.User;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utils.HibernateUtils;

public class DangkyhoinghiDAO {
    
    public static enum SEARCH_TYPE{
        BY_MAHN,
        BY_NAME,
    }
    
    public static List<User> fetchAll_User_DangkyHN(Session session, Integer mahn){
        if(session == null) return null;
        
        String queryAll = "select h.user from Dangkyhoinghi h where h.id.hoinghidangky = :hn_id and h.daDuocDuyet = :tt";
        Query selectQuery = session.createQuery(queryAll);
        selectQuery.setParameter("hn_id", mahn);
        selectQuery.setParameter("tt", true);
        List<User> results = selectQuery.list();
        
        return results;
    }
    
    public static List<User> fetchAll_ChuaDuyetUser_DangkyHN(Session session, Integer mahn){
        if(session == null) return null;
        
        String queryAll = "select h.user from Dangkyhoinghi h where h.id.hoinghidangky = :hn_id and h.daDuocDuyet = :tt";
        Query selectQuery = session.createQuery(queryAll);
        selectQuery.setParameter("hn_id", mahn);
        selectQuery.setParameter("tt", false);
        List<User> results = selectQuery.list();
        
        return results;
    }
    
    public static List<Hoinghi> fetchAll_Hoinghi_OfUser(Session session, Integer iduser){
        if(session == null) return null;
        
        String queryAll = "select dk.hoinghi from Dangkyhoinghi dk join dk.hoinghi h left join h.tinhtrangxoaHoinghi ttxoa "
                + "where dk.id.user = :iduser and (ttxoa.tinhtrangxoa is null or ttxoa.tinhtrangxoa = :tt) order by h.maHn";
        Query selectQuery = session.createQuery(queryAll);
        selectQuery.setParameter("iduser", iduser);
        selectQuery.setParameter("tt", false);
        List<Hoinghi> results = selectQuery.list();
        
        return results;
    }
    
    public static List<Hoinghi> fetchAll_Hoinghi_OfUser_ByType_WithTypeParameter(Session session, Integer iduser, DangkyhoinghiDAO.SEARCH_TYPE typeSearch_type, Object parameter){
        if(session == null || parameter == null) return null;
        
        String queryAll;
        Query selectQuery;
        switch(typeSearch_type){
            case BY_NAME:
                if(!(parameter instanceof String)) return null;
                String likeStr = "%" + (String)parameter + "%";
                queryAll = "select dk.hoinghi from Dangkyhoinghi dk join dk.hoinghi h left join h.tinhtrangxoaHoinghi ttxoa "
                        + "where dk.id.user = :iduser and (ttxoa.tinhtrangxoa is null or ttxoa.tinhtrangxoa = :tt) and h.tenHn like :searchString";
                selectQuery = session.createQuery(queryAll);
                selectQuery.setParameter("searchString", likeStr);
                break;
            default:
                if(!(parameter instanceof Integer)) return null;
                Integer searchID = (Integer) parameter;
                queryAll = "select dk.hoinghi from Dangkyhoinghi dk join dk.hoinghi h left join h.tinhtrangxoaHoinghi ttxoa "
                        + "where dk.id.user = :iduser and (ttxoa.tinhtrangxoa is null or ttxoa.tinhtrangxoa = :tt) and h.maHn = :maHn";
                selectQuery = session.createQuery(queryAll);
                selectQuery.setParameter("maHn", searchID);
                break;
        }
        selectQuery.setParameter("iduser", iduser);
        selectQuery.setParameter("tt", false);
        List<Hoinghi> results = selectQuery.list();
        
        return results;
    }
    
    public static Boolean checkUser_ParticipateHoinghi(Session session, Integer iduser, Integer mahn){
        if(session == null) return null;
        
        String queryAll = "select h from Dangkyhoinghi h where h.id.user = :iduser and h.id.hoinghidangky = :mahn";
        Query selectQuery = session.createQuery(queryAll);
        selectQuery.setParameter("iduser", iduser);
        selectQuery.setParameter("mahn", mahn);
        Dangkyhoinghi result = (Dangkyhoinghi) selectQuery.uniqueResult();
        
        return result != null;
    }
    
    public static Dangkyhoinghi getDangkyhoinghi_ofUser_forHoinghi(Session session, Integer iduser, Integer mahn){
        if(session == null) return null;
        
        String queryAll = "select h from Dangkyhoinghi h where h.id.user = :iduser and h.id.hoinghidangky = :mahn";
        Query selectQuery = session.createQuery(queryAll);
        selectQuery.setParameter("iduser", iduser);
        selectQuery.setParameter("mahn", mahn);
        Dangkyhoinghi result = (Dangkyhoinghi) selectQuery.uniqueResult();
        
        return result;
    }
    
    public static void addDangKy(Session session, Dangkyhoinghi dk){
        session.save(dk);
    }
    
    public static void updateDangKy(Session session, Dangkyhoinghi dk){
        session.update(dk);
    }
    
    public static void removeDangKy(Session session, Dangkyhoinghi dk){
        session.delete(dk);
    }
}

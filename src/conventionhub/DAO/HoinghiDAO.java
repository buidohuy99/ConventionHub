package conventionhub.DAO;

import conventionhub.pojos.Diadiem;
import conventionhub.pojos.Hoinghi;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HoinghiDAO {
    
    public static List<Hoinghi> fetchAll_Hoinghi(Session session, int page, int pagesize, Date loadDate){
        if(session == null) return null;
        
        String queryAll = "from Hoinghi h where h.createdDate <= :loadDate";
        Query selectQuery = session.createQuery(queryAll);
        selectQuery.setFirstResult(pagesize*(page-1));
        selectQuery.setMaxResults(pagesize);
        selectQuery.setParameter("loadDate", loadDate);
        List<Hoinghi> results = selectQuery.list();
        
        return results;
    }
    
    public static Integer getTotalPage_Hoinghi(Session session, int pagesize, Date loadDate){
        if(session == null) return null;
        
        String countQ = "select count(h.maHn) from Hoinghi h where h.createdDate <= :loadDate";
        Query countQuery = session.createQuery(countQ);
        countQuery.setParameter("loadDate", loadDate);
        Long countedEntries = (Long) countQuery.uniqueResult();
        int lastPage = (int) (Math.ceil((float)countedEntries / (float)pagesize));
        
        return lastPage;
    }
    
     public static List<Hoinghi> fetchAll_Hoinghi_NotDeleted(Session session, int page, int pagesize, Date loadDate){
        if(session == null) return null;
        
        String queryAll = "select h "
                + "from Hoinghi h left join h.tinhtrangxoaHoinghi ttxoa "
                + "where h.createdDate <= :loadDate and ( ( ttxoa.tinhtrangxoa is null ) "
                + "or ( ( ttxoa.tinhtrangxoa is not null ) and ( "
                + "( ttxoa.tinhtrangxoa = :tt1 and ( ttxoa.latestModified = ttxoa.createdDate or ttxoa.latestModified <= :loadDate ) ) or "
                + "( ttxoa.tinhtrangxoa = :tt2 and ttxoa.latestModified > :loadDate ) ) ) ) "
                + "order by h.maHn asc";
        Query selectQuery = session.createQuery(queryAll);
        selectQuery.setFirstResult(pagesize*(page-1));
        selectQuery.setMaxResults(pagesize);
        selectQuery.setParameter("loadDate", loadDate);
        selectQuery.setParameter("tt1", false);
        selectQuery.setParameter("tt2", true);
        
        List<Hoinghi> results = selectQuery.list();
        
        return results;
    }
     
    public static Integer getTotalPage_Hoinghi_NotDeleted(Session session, int pagesize, Date loadDate){
        if(session == null) return null;
        
        String countQ = "select count(h.maHn) from Hoinghi h left join h.tinhtrangxoaHoinghi ttxoa "
                + "where ( h.createdDate <= :loadDate and ( ( ttxoa.tinhtrangxoa is null ) "
                + "or ( ( ttxoa.tinhtrangxoa is not null ) and "
                + "( ( ttxoa.tinhtrangxoa = :tt1 and ( ttxoa.latestModified = ttxoa.createdDate or ttxoa.latestModified <= :loadDate ) ) or "
                + "( ttxoa.tinhtrangxoa = :tt2 and ttxoa.latestModified > :loadDate ) ) ) ) )";
        Query countQuery = session.createQuery(countQ);
        countQuery.setParameter("loadDate", loadDate);
        countQuery.setParameter("tt1", false);
        countQuery.setParameter("tt2", true);
        Long countedEntries = (Long) countQuery.uniqueResult();
        int lastPage = (int) (Math.ceil((float)countedEntries / (float)pagesize));
        
        return lastPage;
    }
    
    public static Hoinghi getHoinghi_byMahn(Session session, Integer maHN){
        if(session == null || maHN == null) return null;
        
        String Query = "from Hoinghi h where h.maHn = :mahn";
        Query findHN = session.createQuery(Query);
        findHN.setParameter("mahn", maHN);
        Hoinghi found = (Hoinghi) findHN.uniqueResult();
        
        return found;
    }
    
    public static void saveOrUpdateHoinghi(Session session, Hoinghi hn){
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.saveOrUpdate(hn);
            tx.commit(); // Flush happens automatically
        }
        catch (RuntimeException e) {
            tx.rollback();
            e.printStackTrace(System.err);
            return;
        }
        if(hn.getChitietHoinghi() != null){
            ChiTietHoinghiDAO.saveOrUpdateChitiet(session, hn.getChitietHoinghi());
        }
    }
    
    public static Boolean checkIsThereHoinghi_AtDiadiem_fromDate_toDate(Session session, Diadiem dd, Date batdauDate, Date ketthucDate, Integer excludingMaHn){
        if(session == null) return null;
        Query query;
        String selectQ;
        if(excludingMaHn == null){
            selectQ = "select count(h.maHn) from Hoinghi h where h.diadiem = :madd "
                    + "and h.thoiDiemBatDau < :ketthucDate "
                    + "and h.thoiDiemKetThuc > :batdauDate";
            query = session.createQuery(selectQ);
            query.setParameter("madd", dd);
            query.setParameter("ketthucDate", ketthucDate);
            query.setParameter("batdauDate", batdauDate);
        }else{
            selectQ = "select count(h.maHn) from Hoinghi h where h.maHn != :excludingmaHn and h.diadiem = :madd "
                    + "and h.thoiDiemBatDau < :ketthucDate "
                    + "and h.thoiDiemKetThuc > :batdauDate";
            query = session.createQuery(selectQ);
            query.setParameter("madd", dd);
            query.setParameter("ketthucDate", ketthucDate);
            query.setParameter("batdauDate", batdauDate);
            query.setParameter("excludingmaHn", excludingMaHn);
        }
        Long output = (Long) query.uniqueResult();
        return output != null && output > 0;
    }
}

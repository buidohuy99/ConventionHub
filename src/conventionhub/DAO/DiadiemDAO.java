package conventionhub.DAO;

import conventionhub.pojos.Diadiem;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class DiadiemDAO {
    public static List<Diadiem> getAll_Diadiem(Session session){
        if(session == null) return null;
        String queryAll = "from Diadiem d";
        Query selectQuery = session.createQuery(queryAll);
        List<Diadiem> results = selectQuery.list();
        
        return results;
    }
    
    public static Diadiem getDiadiem(Session session, Integer maDiadiem){
        if(session == null) return null;
        String queryAll = "from Diadiem d where d.maDiaDiem = :madd";
        Query selectQuery = session.createQuery(queryAll);
        selectQuery.setParameter("madd", maDiadiem);
        Diadiem result = (Diadiem)selectQuery.uniqueResult();
        
        return result;
    }
    
    public static List<Diadiem> fetchAll_Diadiem(Session session, int page, int pagesize, Date loadDate){
        if(session == null) return null;
        
        String queryAll = "from Diadiem d where d.createdDate <= :loadDate";
        Query selectQuery = session.createQuery(queryAll);
        selectQuery.setFirstResult(pagesize*(page-1));
        selectQuery.setMaxResults(pagesize);
        selectQuery.setParameter("loadDate", loadDate);
        List<Diadiem> results = selectQuery.list();
        
        return results;
    }
    
    public static Integer getTotalPage_Diadiem(Session session, int pagesize, Date loadDate){
        if(session == null) return null;
        
        String countQ = "select count(d.maDiaDiem) from Diadiem d where d.createdDate <= :loadDate";
        Query countQuery = session.createQuery(countQ);
        countQuery.setParameter("loadDate", loadDate);
        Long countedEntries = (Long) countQuery.uniqueResult();
        int lastPage = (int) (Math.ceil((float)countedEntries / (float)pagesize));
        
        return lastPage;
    }
    
    public static void saveOrUpdateDiadiem(Session session, Diadiem dd){
        session.saveOrUpdate(dd);
    }
    
    public static Long maxNumberOfDangkyhoinghi_perHoinghi_withDiadiem(Session session, Integer maDiaDiem){
        if(session == null) return null;
        
        String query1 = "select new map(h.maHn as maHn, count(dk.id.user) as cnt) "
                + "from Dangkyhoinghi dk join dk.hoinghi h join h.diadiem dd "
                + "where dd.maDiaDiem = :madd "
                + "group by h.maHn order by cnt desc";
        Query q = session.createQuery(query1);
        q.setParameter("madd", maDiaDiem);
        q.setMaxResults(1);
        List<?> countedEntries = q.list();
        if(countedEntries.iterator().hasNext()){
            Map firstRow = (Map) countedEntries.iterator().next();
            return (Long) firstRow.get("cnt");
        }else
            return Long.valueOf(0);
    }
}

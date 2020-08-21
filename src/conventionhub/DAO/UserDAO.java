package conventionhub.DAO;

import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.User;
import java.util.Date;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import utils.HibernateUtils;

public class UserDAO {
    
    public static List<User> fetchAll_User(Session session, int page, int pagesize, Date loadDate){
        if(session == null) return null;
        
        String queryAll = "from User h where h.createdDate <= :loadDate";
        Query selectQuery = session.createQuery(queryAll);
        selectQuery.setFirstResult(pagesize*(page-1));
        selectQuery.setMaxResults(pagesize);
        selectQuery.setParameter("loadDate", loadDate);
        List<User> results = selectQuery.list();
        
        return results;
    }
    
    public static Integer getTotalPage_User(Session session, int pagesize, Date loadDate){
        if(session == null) return null;
        
        String countQ = "select count(h.iduser) from User h where h.createdDate <= :loadDate";
        Query countQuery = session.createQuery(countQ);
        countQuery.setParameter("loadDate", loadDate);
        Long countedEntries = (Long) countQuery.uniqueResult();
        int lastPage = (int) (Math.ceil((float)countedEntries / (float)pagesize));
        
        return lastPage;
    }
    
    public static User getActiveUser(Session session, String username){
        if(session == null) return null;
        
        String countQ = "from User h where h.username = :username and h.tinhTrangBlock = :tinhtrang";
        Query query = session.createQuery(countQ);
        query.setParameter("username", username);
        query.setParameter("tinhtrang", false);
        User found = (User) query.uniqueResult();
        
        return found;
    }
    
    public static void addUser(Session session, User user){
        session.save(user);
    }
    
    public static void updateUser(Session session, User user){
        session.update(user);
    }
    
}

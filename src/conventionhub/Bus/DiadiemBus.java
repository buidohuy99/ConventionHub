package conventionhub.Bus;

import conventionhub.DAO.DiadiemDAO;
import conventionhub.DAO.TinhtrangxoaDiadiemDAO;
import conventionhub.pojos.Diadiem;
import conventionhub.pojos.TinhtrangxoaDiadiem;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;

public class DiadiemBus {
    public static List<Diadiem> getAll_Diadiem(){
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.getTransaction().begin();
        List<Diadiem> output = DiadiemDAO.getAll_Diadiem(session);
        session.getTransaction().commit();
        session.close();
        return output;
    }
    
    public static Diadiem getDiadiem_ById(Integer madd){
        if(madd == null) return null;
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.getTransaction().begin();
        Diadiem output = DiadiemDAO.getDiadiem(session, madd);
        session.getTransaction().commit();
        session.close();
        return output;
    }
    
    public static ObservableList<Diadiem> getAll_Diadiem_AtPage(int page, int pagesize, Date loadDate){
        if(page < 1 || pagesize <= 0) return null;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        session.getTransaction().begin();
        int totalPages = DiadiemDAO.getTotalPage_Diadiem(session, pagesize, loadDate);
        if(page > totalPages) {
            session.getTransaction().rollback();
            session.close();
            return null;
        }

        List<Diadiem> dd = DiadiemDAO.fetchAll_Diadiem(session, page, pagesize, loadDate);
        ObservableList<Diadiem> output = FXCollections.observableList(dd);

        session.getTransaction().commit();
        session.close();
        return output;
    }
    
    public static Integer getTotalPages_Diadiem(int pagesize, Date loadDate){
        if(pagesize <= 0) return null;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        session.getTransaction().begin();
        
        int totalPages = DiadiemDAO.getTotalPage_Diadiem(session, pagesize, loadDate);

        session.getTransaction().commit();
        session.close();
        return totalPages;
    }
    
    public static class DiadiemBusException extends Exception{
        final String explanationString;
        public String getExplanationString(){
            return explanationString;
        }
        public DiadiemBusException(String message, String explanationString) {
            super(message);
            this.explanationString = explanationString;
        }
    }
    
    public static void saveOrUpdateDiadiem(Diadiem dd) throws DiadiemBusException{
        if(dd == null) return;
        Session session = HibernateUtils.getSessionFactory().openSession();
        try{
            session.getTransaction().begin();

            if(dd.getMaDiaDiem() != null){
                Long maxDk = DiadiemDAO.maxNumberOfDangkyhoinghi_perHoinghi_withDiadiem(session, dd.getMaDiaDiem());
                if(dd.getSucChua() < maxDk){
                    throw new DiadiemBusException("Sức chứa không đủ đáp ứng", "Sức chứa không đủ đáp ứng số người tham dự hội nghị");
                }
            }

            Date now = new Date();
            if(dd.getMaDiaDiem() == null) dd.setCreatedDate(now);
            dd.setLatestModified(now);
            DiadiemDAO.saveOrUpdateDiadiem(session, dd);
        } catch (DiadiemBusException ex){
            session.getTransaction().rollback();
            session.close();
            throw ex;
        } catch (Exception ex){
            ex.printStackTrace(System.err);
            session.getTransaction().rollback();
            session.close();
            return;
        } 
        session.getTransaction().commit();
        session.close();
    }
    
    public static void xoaDiadiem(Diadiem dd) throws DiadiemBusException{
        if(dd == null) return;
        Session session = HibernateUtils.getSessionFactory().openSession();
        session.getTransaction().begin();
        TinhtrangxoaDiadiem tt = TinhtrangxoaDiadiemDAO.getTinhtrangxoa_forDiadiem(session, dd.getMaDiaDiem());
        if(tt == null || !tt.isTinhtrangxoa()){
            if(dd.tinhtrangxoaProperty().get()){
                session.getTransaction().rollback();
                session.close();
                throw new DiadiemBusException("Lỗi phục hồi đối tượng chưa bị xóa", "Đối tượng này chưa bị xóa. Vui lòng refresh thông tin");
            }
            if(tt == null){
                Date d = new Date();
                TinhtrangxoaDiadiem ttNew = new TinhtrangxoaDiadiem(dd, true, d, d);
                TinhtrangxoaDiadiemDAO.addTinhtrangxoa(session, ttNew);
                session.getTransaction().commit();
                session.close();
            }else{
                tt.setTinhtrangxoa(true);
                tt.setLatestModified(new Date());
                TinhtrangxoaDiadiemDAO.updateTinhtrangxoa(session, tt);
                session.getTransaction().commit();
                session.close();
            }
        }else{
            if(!dd.tinhtrangxoaProperty().get()){
                session.getTransaction().rollback();
                session.close();
                throw new DiadiemBusException("Lỗi xóa đối tượng đã bị xóa", "Đối tượng này đã bị xóa rồi. Vui lòng refresh thông tin");
            }
            tt.setTinhtrangxoa(false);
            tt.setLatestModified(new Date());
            TinhtrangxoaDiadiemDAO.updateTinhtrangxoa(session, tt);
            session.getTransaction().commit();
            session.close();
        }
    }
}

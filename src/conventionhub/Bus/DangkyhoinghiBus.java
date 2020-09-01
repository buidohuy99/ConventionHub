package conventionhub.Bus;

import conventionhub.DAO.DangkyhoinghiDAO;
import conventionhub.pojos.Dangkyhoinghi;
import conventionhub.pojos.DangkyhoinghiId;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.User;
import java.util.Date;
import java.util.List;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;

public class DangkyhoinghiBus {
    private static Stage stage;
    
    public static enum SEARCH_HN_TYPE_OFUSER{
        BY_ID,
        BY_NAME,
    }
    
    public static Stage getDuyetDKStage(){
        if(stage == null) {
            stage = new Stage();
        }
        return stage;
    }
    
    public static List<User> getAll_Dangky_Hoinghi(Integer mahn){
        if(mahn == null) return null;
        
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        
        session.beginTransaction();
        List<User> results = DangkyhoinghiDAO.fetchAll_User_DangkyHN(session, mahn);
        session.getTransaction().commit();
        
        session.close();
        return results;
    }
    
    public static List<User> getAll_ChuaDuyetDangky_Hoinghi(Integer mahn){
        if(mahn == null) return null;
        
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        
        session.beginTransaction();
        List<User> results = DangkyhoinghiDAO.fetchAll_ChuaDuyetUser_DangkyHN(session, mahn);
        session.getTransaction().commit();
        
        session.close();
        return results;
    }
    
    public static List<Hoinghi> getAll_Hoinghi_OfUser(Integer iduser){
        if(iduser == null) return null;
        
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        
        session.beginTransaction();
        List<Hoinghi> results = DangkyhoinghiDAO.fetchAll_Hoinghi_OfUser(session, iduser);
        session.getTransaction().commit();
        
        session.close();
        return results;
    }
    
    public static List<Hoinghi> getAll_Hoinghi_OfUser_WithParameter(Integer iduser, SEARCH_HN_TYPE_OFUSER type, Object parameter) throws DangkyhoinghiBusException{
        if(iduser == null || type == null) return null;
        
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        
        session.beginTransaction();
        List<Hoinghi> results;
        switch(type){
            case BY_ID:
                if(!(parameter instanceof Integer)) throw new DangkyhoinghiBusException("Lỗi từ khóa tìm kiếm", "Từ khóa không phải là một số mã");
                results = DangkyhoinghiDAO.fetchAll_Hoinghi_OfUser_ByType_WithTypeParameter(session, iduser, DangkyhoinghiDAO.SEARCH_TYPE.BY_MAHN, parameter);
                break;
            case BY_NAME: default:
                if(!(parameter instanceof String)) throw new DangkyhoinghiBusException("Lỗi từ khóa tìm kiếm", "Từ khóa không phải là một chuỗi ký tự");
                results = DangkyhoinghiDAO.fetchAll_Hoinghi_OfUser_ByType_WithTypeParameter(session, iduser, DangkyhoinghiDAO.SEARCH_TYPE.BY_NAME, parameter);
                break;
        }
        session.getTransaction().commit();
        
        session.close();
        return results;
    }
    
    public static Boolean checkThamDu(Integer iduser, Integer maHN){
        if(iduser == null || maHN == null) return null;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();
        Boolean result = DangkyhoinghiDAO.checkUser_ParticipateHoinghi(session, iduser, maHN);
        session.getTransaction().commit();
        
        session.close();
        return result;
    }
    
    public static Dangkyhoinghi getDangky_ofUser_forHoinghi(Integer iduser, Integer maHN){
        if(iduser == null || maHN == null) return null;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        session.getTransaction().begin();
        Dangkyhoinghi result = DangkyhoinghiDAO.getDangkyhoinghi_ofUser_forHoinghi(session, iduser, maHN);
        session.getTransaction().commit();
        session.close();
        return result;
    }
    
    public static class DangkyhoinghiBusException extends Exception{
        final String explanationString;
        public String getExplanationString(){
            return explanationString;
        }
        public DangkyhoinghiBusException(String message, String explanationString) {
            super(message);
            this.explanationString = explanationString;
        }
    }
    
    public static void addDangky(User user, Hoinghi hn) throws DangkyhoinghiBusException{
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        session.beginTransaction();
        session.refresh(hn);
        
        Date now = new Date();
        try{
            if(now.after(hn.getThoiDiemBatDau())||now.equals(hn.getThoiDiemBatDau())){
                throw new DangkyhoinghiBusException("Lỗi đăng ký tham dự", "Hội nghị đã kết thúc");
            }
            if(hn.getTinhtrangxoaHoinghi() != null && hn.getTinhtrangxoaHoinghi().isTinhtrangxoa()){
                throw new DangkyhoinghiBusException("Lỗi đăng ký tham dự", "Không thể đăng ký hội nghị đã bị xóa");
            }
            if(hn.getDiadiem() == null || (hn.getDiadiem().getTinhtrangxoaDiadiem() != null && hn.getDiadiem().getTinhtrangxoaDiadiem().isTinhtrangxoa())){
                throw new DangkyhoinghiBusException("Lỗi đăng ký tham dự", "Địa điểm tổ chức không còn tồn tại! Xin liên hệ phía tổ chức");
            }
            if(hn.getDiadiem().getSucChua() < hn.getDangkyhoinghis().size() + 1){
                throw new DangkyhoinghiBusException("Lỗi đăng ký tham dự", "Hội nghị tạm thời không đủ sức chứa. Vui lòng trở lại sau");
            }

            Dangkyhoinghi newOne = new Dangkyhoinghi(new DangkyhoinghiId(user.getIduser(), hn.getMaHn()), hn, user, false, new Date());

            DangkyhoinghiDAO.addDangKy(session, newOne);
        } catch (DangkyhoinghiBusException ex){
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
    
    public static void removeDangky(Hoinghi hn, User user) throws DangkyhoinghiBusException{
        if(hn == null) return;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        try{
            session.getTransaction().begin();
            session.refresh(hn);
            if(hn.getThoiDiemBatDau().before(new Date())){
                throw new DangkyhoinghiBusException("Lỗi bỏ tham dự", "Hội nghị này đã/đang diễn ra");
            }
            Dangkyhoinghi temp = new Dangkyhoinghi();
            temp.setId(new DangkyhoinghiId(user.getIduser(), hn.getMaHn()));
            temp.setCreatedDate(new Date());

            DangkyhoinghiDAO.removeDangKy(session, temp);
        } catch (DangkyhoinghiBusException ex){
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
    
    public static void duyetDangky(Integer userid, Integer maHn) throws DangkyhoinghiBusException{
        if(userid == null || maHn == null) return;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        try{
            session.getTransaction().begin();
            Dangkyhoinghi result = DangkyhoinghiDAO.getDangkyhoinghi_ofUser_forHoinghi(session, userid, maHn);

            if(result == null){
                throw new DangkyhoinghiBusException("Không thể duyệt đăng ký không tồn tại", "Đăng ký này không còn tồn tại. Vui lòng refresh");  
            }
            if(result.isDaDuocDuyet()){
                throw new DangkyhoinghiBusException("Đăng ký này đã được duyệt", "Không thể duyệt đăng ký đã được duyệt. Vui lòng refresh");  
            }
            result.setDaDuocDuyet(true);

            DangkyhoinghiDAO.updateDangKy(session, result);
        } catch (DangkyhoinghiBusException ex){
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
}

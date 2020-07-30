package conventionhub.Bus;

import conventionhub.DAO.DangkyhoinghiDAO;
import conventionhub.pojos.Dangkyhoinghi;
import conventionhub.pojos.DangkyhoinghiId;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.User;
import java.util.Date;
import java.util.List;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;

public class DangkyhoinghiBus {
    private static Stage stage;
    
    public static Stage getDuyetDKStage(){
        if(stage == null) stage = new Stage();
        return stage;
    }
    
    public static List<User> getAll_Dangky_Hoinghi(Integer mahn){
        if(mahn == null) return null;
        
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        
        List<User> results = DangkyhoinghiDAO.fetchAll_User_DangkyHN(session, mahn);
        
        session.close();
        return results;
    }
    
    public static List<User> getAll_ChuaDuyetDangky_Hoinghi(Integer mahn){
        if(mahn == null) return null;
        
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        
        List<User> results = DangkyhoinghiDAO.fetchAll_ChuaDuyetUser_DangkyHN(session, mahn);
        
        session.close();
        return results;
    }
    
    public static List<Hoinghi> getAll_Hoinghi_OfUser(Integer iduser){
        if(iduser == null) return null;
        
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        
        List<Hoinghi> results = DangkyhoinghiDAO.fetchAll_Hoinghi_OfUser(session, iduser);
        
        session.close();
        return results;
    }
    
    public static Boolean checkThamDu(Integer iduser, Integer maHN){
        if(iduser == null || maHN == null) return null;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        Boolean result = DangkyhoinghiDAO.checkUser_ParticipateHoinghi(session, iduser, maHN);
        session.close();
        
        return result;
    }
    
    public static Dangkyhoinghi getDangky_ofUser_forHoinghi(Integer iduser, Integer maHN){
        if(iduser == null || maHN == null) return null;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        Dangkyhoinghi result = DangkyhoinghiDAO.getDangkyhoinghi_ofUser_forHoinghi(session, iduser, maHN);
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
        session.refresh(hn);
        
        Date now = new Date();
        if(now.after(hn.getThoiDiemBatDau())||now.equals(hn.getThoiDiemBatDau())){
            session.close();
            throw new DangkyhoinghiBusException("Lỗi đăng ký tham dự", "Hội nghị đã kết thúc");
        }
        if(hn.getTinhtrangxoaHoinghi() != null && hn.getTinhtrangxoaHoinghi().isTinhtrangxoa()){
            session.close();
            throw new DangkyhoinghiBusException("Lỗi đăng ký tham dự", "Không thể đăng ký hội nghị đã bị xóa");
        }
        if(hn.getDiadiem() == null || (hn.getDiadiem().getTinhtrangxoaDiadiem() != null && hn.getDiadiem().getTinhtrangxoaDiadiem().isTinhtrangxoa())){
            session.close();
            throw new DangkyhoinghiBusException("Lỗi đăng ký tham dự", "Địa điểm tổ chức không còn tồn tại! Xin liên hệ phía tổ chức");
        }
        if(hn.getDiadiem().getSucChua() < hn.getDangkyhoinghis().size() + 1){
            session.close();
            throw new DangkyhoinghiBusException("Lỗi đăng ký tham dự", "Hội nghị tạm thời không đủ sức chứa. Vui lòng trở lại sau");
        }
        
        Dangkyhoinghi newOne = new Dangkyhoinghi(new DangkyhoinghiId(user.getIduser(), hn.getMaHn()), hn, user, false, new Date());
        
        DangkyhoinghiDAO.addDangKy(session, newOne);
            
        session.close();
    }
    
    public static void removeDangky(Hoinghi hn, User user) throws DangkyhoinghiBusException{
        if(hn == null) return;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        session.refresh(hn);
        if(hn.getThoiDiemBatDau().before(new Date())){
            session.close();
            throw new DangkyhoinghiBusException("Lỗi bỏ tham dự", "Hội nghị này đã/đang diễn ra");
        }
        Dangkyhoinghi temp = new Dangkyhoinghi();
        temp.setId(new DangkyhoinghiId(user.getIduser(), hn.getMaHn()));
        temp.setCreatedDate(new Date());
        
        DangkyhoinghiDAO.removeDangKy(session, temp);
            
        session.close();
    }
    
    public static void duyetDangky(Integer userid, Integer maHn) throws DangkyhoinghiBusException{
        if(userid == null || maHn == null) return;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        
        Dangkyhoinghi result = DangkyhoinghiDAO.getDangkyhoinghi_ofUser_forHoinghi(session, userid, maHn);
        
        if(result == null){
            session.close();
            throw new DangkyhoinghiBusException("Không thể duyệt đăng ký không tồn tại", "Đăng ký này không còn tồn tại. Vui lòng refresh");  
        }
        if(result.isDaDuocDuyet()){
            session.close();
            throw new DangkyhoinghiBusException("Đăng ký này đã được duyệt", "Không thể duyệt đăng ký đã được duyệt. Vui lòng refresh");  
        }
        result.setDaDuocDuyet(true);
        
        DangkyhoinghiDAO.updateDangKy(session, result);
        
        session.close();
    }
}

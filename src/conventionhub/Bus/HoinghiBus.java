package conventionhub.Bus;

import conventionhub.DAO.HoinghiDAO;
import conventionhub.DAO.TinhTrangXoaHoinghiDAO;
import conventionhub.DAO.TinhtrangxoaDiadiemDAO;
import conventionhub.pojos.ChitietHoinghi;
import conventionhub.pojos.Diadiem;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.TinhtrangxoaDiadiem;
import conventionhub.pojos.TinhtrangxoaHoinghi;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utils.HibernateUtils;

public class HoinghiBus {
    
    public static class getAll_Hoinghi_AtPage extends Task<ObservableList<Hoinghi>>{

        public int page;
        public int pagesize;
        public Date loadDate;
        
        public getAll_Hoinghi_AtPage(int page, int pagesize, Date loadDate){
            this.page = page;
            this.pagesize = pagesize;
            this.loadDate = loadDate;
        }
        
        @Override
        protected ObservableList<Hoinghi> call() throws Exception {
            if(page < 1 || pagesize <= 0) return null;
            
            SessionFactory factory = HibernateUtils.getSessionFactory();
            Session session = factory.openSession();

            int totalPages = HoinghiDAO.getTotalPage_Hoinghi(session, pagesize, loadDate);
            if(page > totalPages) return null;
            

            List<Hoinghi> hoinghi = HoinghiDAO.fetchAll_Hoinghi(session, page, pagesize, loadDate);
            ObservableList<Hoinghi> output = FXCollections.observableList(hoinghi);

            session.close();
            return output;
        }
        
    }
    
    public static class getTotalPages_Hoinghi extends Task<Integer>{
        
        public int pagesize;
        public Date loadDate;
        
        public getTotalPages_Hoinghi(int pagesize, Date loadDate){
            this.pagesize = pagesize;
            this.loadDate = loadDate;
        }

        @Override
        protected Integer call() throws Exception {
            if(pagesize <= 0) return null;
            SessionFactory factory = HibernateUtils.getSessionFactory();
            Session session = factory.openSession();

            int totalPages = HoinghiDAO.getTotalPage_Hoinghi(session, pagesize, loadDate);

            session.close();

            return totalPages;
        }
    }
    
    public static class HoinghiBusException extends Exception{
        final String explanationString;
        public String getExplanationString(){
            return explanationString;
        }
        public HoinghiBusException(String message, String explanationString) {
            super(message);
            this.explanationString = explanationString;
        }
    }

    public static ObservableList<Hoinghi> getAll_Hoinghi_AtPage_NotDeleted(int page, int pagesize, Date loadDate){
        if(page < 1 || pagesize <= 0) return null;
            
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();

        int totalPages = HoinghiDAO.getTotalPage_Hoinghi_NotDeleted(session, pagesize, loadDate);
        if(page > totalPages) return null;


        List<Hoinghi> hoinghi = HoinghiDAO.fetchAll_Hoinghi_NotDeleted(session, page, pagesize, loadDate);
        ObservableList<Hoinghi> output = FXCollections.observableList(hoinghi);

        session.close();
        return output;
    }
    
    public static Integer getTotalPages_Hoinghi_NotDeleted(int pagesize, Date loadDate){
        if(pagesize <= 0) return null;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();

        int totalPages = HoinghiDAO.getTotalPage_Hoinghi_NotDeleted(session, pagesize, loadDate);

        session.close();

        return totalPages;
    }
    
    public static Hoinghi getChiTietHoinghi(Integer maHN){
        if(maHN == null) return null;
        
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        
        Hoinghi found = HoinghiDAO.getHoinghi_byMahn(session,maHN);
        
        session.close();
        return found;
    }
    
    public static void saveOrUpdateHoinghi(Hoinghi hn, String motaChiTietString) throws HoinghiBusException{
        if(hn == null) return;
        if(hn.getDiadiem() == null) return;
        Session session = HibernateUtils.getSessionFactory().openSession();
        Date TDBD = hn.getThoiDiemBatDau();
        Date TDKT = hn.getThoiDiemKetThuc();
        if(TDBD.before(new Date()) || TDKT.before(new Date()) || TDKT.equals(new Date())){
            session.close();
            throw new HoinghiBusException("Khung giờ không hợp lệ", "Không thể tổ chức hội nghị ở quá khứ");
        }
        
        Diadiem dd = hn.getDiadiem();
        session.refresh(dd);
        TinhtrangxoaDiadiem tt = TinhtrangxoaDiadiemDAO.getTinhtrangxoa_forDiadiem(session, hn.getDiadiem().getMaDiaDiem());
        if(tt != null && tt.isTinhtrangxoa()){
            session.close();
            throw new HoinghiBusException("Địa điểm bạn chọn đã bị xóa", "Vui lòng chọn lại địa điểm khác không bị xóa");
        }    
        Boolean check = HoinghiDAO.checkIsThereHoinghi_AtDiadiem_fromDate_toDate(session, hn.getDiadiem(), hn.getThoiDiemBatDau(), hn.getThoiDiemKetThuc(), hn.getMaHn());
        if(check == null){
            session.close();
            throw new HoinghiBusException("Lỗi truy xuất", "Vui lòng thử lại sau");
        }else if(check){
            session.close();
            throw new HoinghiBusException("Thời điểm và địa điểm không hợp lệ", "Đã có hội nghị khác tại địa điểm này trong khung giờ");
        }
        if(hn.getChitietHoinghi() == null){
            if(motaChiTietString != null && motaChiTietString.length() != 0)
                hn.setChitietHoinghi(new ChitietHoinghi(hn, motaChiTietString));
        }else{
            hn.getChitietHoinghi().setMotaChiTiet(motaChiTietString);
        }
        Date d = new Date();
        if(hn.getMaHn() == null) hn.setCreatedDate(d);
        hn.setLastestModified(d);
        HoinghiDAO.saveOrUpdateHoinghi(session, hn);
        session.close();
    }
    
    public static Boolean checkIsThereHoinghi_AtDiadiem_fromDate_toDate(Diadiem dd, Date batdauDate, Date ketthucDate, Integer excludeMaHn){
        if(dd == null || batdauDate == null || ketthucDate == null){
            return null;
        }
        Session session = HibernateUtils.getSessionFactory().openSession();
        Boolean check = HoinghiDAO.checkIsThereHoinghi_AtDiadiem_fromDate_toDate(session, dd, batdauDate, ketthucDate, excludeMaHn);
        session.close();
        return check;
    }
    
    public static void xoaHoinghi(Hoinghi hn) throws HoinghiBusException{
        if(hn == null) return;
        Session session = HibernateUtils.getSessionFactory().openSession();
        TinhtrangxoaHoinghi tt = TinhTrangXoaHoinghiDAO.getTinhtrangxoa_forHoinghi(session, hn.getMaHn());
        if(tt == null || !tt.isTinhtrangxoa()){
            if(hn.tinhtrangxoaProperty().get()){
                session.close();
                throw new HoinghiBusException("Lỗi phục hồi đối tượng chưa bị xóa", "Đối tượng này chưa bị xóa. Vui lòng refresh thông tin");
            }
            if(tt == null){
                Date d = new Date();
                TinhtrangxoaHoinghi ttNew = new TinhtrangxoaHoinghi(hn, true, d, d);
                TinhTrangXoaHoinghiDAO.addTinhtrangxoa(session, ttNew);
                session.close();
            }else{
                tt.setTinhtrangxoa(true);
                tt.setLatestModified(new Date());
                TinhTrangXoaHoinghiDAO.updateTinhtrangxoa(session, tt);
                session.close();
            }
        }else{
            if(!hn.tinhtrangxoaProperty().get()){
                session.close();
                throw new HoinghiBusException("Lỗi xóa đối tượng đã bị xóa", "Đối tượng này đã bị xóa rồi. Vui lòng refresh thông tin");
            }
            tt.setTinhtrangxoa(false);
            tt.setLatestModified(new Date());
            TinhTrangXoaHoinghiDAO.updateTinhtrangxoa(session, tt);
            session.close();
        }
    }
    
}

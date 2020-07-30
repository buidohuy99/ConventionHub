package conventionhub.pojos;
// Generated Jul 30, 2020, 1:16:07 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Hoinghi generated by hbm2java
 */
public class Hoinghi  implements java.io.Serializable {


     private Integer maHn;
     private Diadiem diadiem;
     private String tenHn;
     private String motaNgangon;
     private String hinhAnh;
     private Date thoiDiemBatDau;
     private Date thoiDiemKetThuc;
     private Date createdDate;
     private Date lastestModified;
     private ChitietHoinghi chitietHoinghi;
     private Set<Dangkyhoinghi> dangkyhoinghis = new HashSet<Dangkyhoinghi>(0);
     private TinhtrangxoaHoinghi tinhtrangxoaHoinghi;

     private BooleanProperty tinhtrangxoaBooleanProperty;
     public BooleanProperty tinhtrangxoaProperty(){
         if(tinhtrangxoaBooleanProperty == null) tinhtrangxoaBooleanProperty = new SimpleBooleanProperty();
         return tinhtrangxoaBooleanProperty;
     }
     
    public Hoinghi() {
    }

	
    public Hoinghi(Diadiem diadiem, String tenHn, String motaNgangon, Date thoiDiemBatDau, Date thoiDiemKetThuc, Date createdDate, Date lastestModified) {
        this.diadiem = diadiem;
        this.tenHn = tenHn;
        this.motaNgangon = motaNgangon;
        this.thoiDiemBatDau = thoiDiemBatDau;
        this.thoiDiemKetThuc = thoiDiemKetThuc;
        this.createdDate = createdDate;
        this.lastestModified = lastestModified;
    }
    public Hoinghi(Diadiem diadiem, String tenHn, String motaNgangon, String hinhAnh, Date thoiDiemBatDau, Date thoiDiemKetThuc, Date createdDate, Date lastestModified, ChitietHoinghi chitietHoinghi, Set<Dangkyhoinghi> dangkyhoinghis, TinhtrangxoaHoinghi tinhtrangxoaHoinghi) {
       this.diadiem = diadiem;
       this.tenHn = tenHn;
       this.motaNgangon = motaNgangon;
       this.hinhAnh = hinhAnh;
       this.thoiDiemBatDau = thoiDiemBatDau;
       this.thoiDiemKetThuc = thoiDiemKetThuc;
       this.createdDate = createdDate;
       this.lastestModified = lastestModified;
       this.chitietHoinghi = chitietHoinghi;
       this.dangkyhoinghis = dangkyhoinghis;
       this.tinhtrangxoaHoinghi = tinhtrangxoaHoinghi;
    }
   
    public Integer getMaHn() {
        return this.maHn;
    }
    
    public void setMaHn(Integer maHn) {
        this.maHn = maHn;
    }
    public Diadiem getDiadiem() {
        return this.diadiem;
    }
    
    public void setDiadiem(Diadiem diadiem) {
        this.diadiem = diadiem;
    }
    public String getTenHn() {
        return this.tenHn;
    }
    
    public void setTenHn(String tenHn) {
        this.tenHn = tenHn;
    }
    public String getMotaNgangon() {
        return this.motaNgangon;
    }
    
    public void setMotaNgangon(String motaNgangon) {
        this.motaNgangon = motaNgangon;
    }
    public String getHinhAnh() {
        return this.hinhAnh;
    }
    
    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }
    public Date getThoiDiemBatDau() {
        return this.thoiDiemBatDau;
    }
    
    public void setThoiDiemBatDau(Date thoiDiemBatDau) {
        this.thoiDiemBatDau = thoiDiemBatDau;
    }
    public Date getThoiDiemKetThuc() {
        return this.thoiDiemKetThuc;
    }
    
    public void setThoiDiemKetThuc(Date thoiDiemKetThuc) {
        this.thoiDiemKetThuc = thoiDiemKetThuc;
    }
    public Date getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public Date getLastestModified() {
        return this.lastestModified;
    }
    
    public void setLastestModified(Date lastestModified) {
        this.lastestModified = lastestModified;
    }
    public ChitietHoinghi getChitietHoinghi() {
        return this.chitietHoinghi;
    }
    
    public void setChitietHoinghi(ChitietHoinghi chitietHoinghi) {
        this.chitietHoinghi = chitietHoinghi;
    }
    public Set<Dangkyhoinghi> getDangkyhoinghis() {
        return this.dangkyhoinghis;
    }
    
    public void setDangkyhoinghis(Set<Dangkyhoinghi> dangkyhoinghis) {
        this.dangkyhoinghis = dangkyhoinghis;
    }
    public TinhtrangxoaHoinghi getTinhtrangxoaHoinghi() {
        return this.tinhtrangxoaHoinghi;
    }
    
    public void setTinhtrangxoaHoinghi(TinhtrangxoaHoinghi tinhtrangxoaHoinghi) {
        this.tinhtrangxoaHoinghi = tinhtrangxoaHoinghi;
    }




}


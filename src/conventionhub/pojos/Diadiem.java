package conventionhub.pojos;
// Generated Jul 30, 2020, 1:16:07 PM by Hibernate Tools 4.3.1


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * Diadiem generated by hbm2java
 */
public class Diadiem  implements java.io.Serializable {


     private Integer maDiaDiem;
     private String tenDiaDiem;
     private String diaChi;
     private int sucChua;
     private Date createdDate;
     private Date latestModified;
     private Set<Hoinghi> hoinghis = new HashSet<Hoinghi>(0);
     private TinhtrangxoaDiadiem tinhtrangxoaDiadiem;

     private BooleanProperty tinhtrangxoaBooleanProperty;
     public BooleanProperty tinhtrangxoaProperty(){
         if(tinhtrangxoaBooleanProperty == null) tinhtrangxoaBooleanProperty = new SimpleBooleanProperty();
         return tinhtrangxoaBooleanProperty;
     }
     
    public Diadiem() {
    }

	
    public Diadiem(String tenDiaDiem, String diaChi, int sucChua, Date createdDate, Date latestModified) {
        this.tenDiaDiem = tenDiaDiem;
        this.diaChi = diaChi;
        this.sucChua = sucChua;
        this.createdDate = createdDate;
        this.latestModified = latestModified;
    }
    public Diadiem(String tenDiaDiem, String diaChi, int sucChua, Date createdDate, Date latestModified, Set<Hoinghi> hoinghis, TinhtrangxoaDiadiem tinhtrangxoaDiadiem) {
       this.tenDiaDiem = tenDiaDiem;
       this.diaChi = diaChi;
       this.sucChua = sucChua;
       this.createdDate = createdDate;
       this.latestModified = latestModified;
       this.hoinghis = hoinghis;
       this.tinhtrangxoaDiadiem = tinhtrangxoaDiadiem;
    }
   
    public Integer getMaDiaDiem() {
        return this.maDiaDiem;
    }
    
    public void setMaDiaDiem(Integer maDiaDiem) {
        this.maDiaDiem = maDiaDiem;
    }
    public String getTenDiaDiem() {
        return this.tenDiaDiem;
    }
    
    public void setTenDiaDiem(String tenDiaDiem) {
        this.tenDiaDiem = tenDiaDiem;
    }
    public String getDiaChi() {
        return this.diaChi;
    }
    
    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    public int getSucChua() {
        return this.sucChua;
    }
    
    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }
    public Date getCreatedDate() {
        return this.createdDate;
    }
    
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    public Date getLatestModified() {
        return this.latestModified;
    }
    
    public void setLatestModified(Date latestModified) {
        this.latestModified = latestModified;
    }
    public Set<Hoinghi> getHoinghis() {
        return this.hoinghis;
    }
    
    public void setHoinghis(Set<Hoinghi> hoinghis) {
        this.hoinghis = hoinghis;
    }
    public TinhtrangxoaDiadiem getTinhtrangxoaDiadiem() {
        return this.tinhtrangxoaDiadiem;
    }
    
    public void setTinhtrangxoaDiadiem(TinhtrangxoaDiadiem tinhtrangxoaDiadiem) {
        this.tinhtrangxoaDiadiem = tinhtrangxoaDiadiem;
    }




}



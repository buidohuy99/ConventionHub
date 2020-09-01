package conventionhub.Bus;

import conventionhub.DAO.HoinghiDAO;
import conventionhub.DAO.UserDAO;
import conventionhub.pojos.Hoinghi;
import conventionhub.pojos.User;
import java.util.Date;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;
import utils.HibernateUtils;

public class UserBus {
    private static User currentUser;
    private static Stage LogInSignUpStage;
    
    public static class getAll_User_AtPage extends Task<ObservableList<User>>{

        public int page;
        public int pagesize;
        public Date loadDate;
        
        public getAll_User_AtPage(int page, int pagesize, Date loadDate){
            this.page = page;
            this.pagesize = pagesize;
            this.loadDate = loadDate;
        }
        
        @Override
        protected ObservableList<User> call() throws Exception {
            if(page < 1 || pagesize <= 0) return null;
            
            SessionFactory factory = HibernateUtils.getSessionFactory();
            Session session = factory.openSession();
            session.getTransaction().begin();

            int totalPages = UserDAO.getTotalPage_User(session, pagesize, loadDate);
            if(page > totalPages) {
                session.getTransaction().rollback();
                session.close();
                return null;
            }

            List<User> users = UserDAO.fetchAll_User(session, page, pagesize, loadDate);
            ObservableList<User> output = FXCollections.observableList(users);

            session.getTransaction().commit();
            session.close();
            return output;
        }
        
    }
    
    public static class getTotalPages_User extends Task<Integer>{
        
        public int pagesize;
        public Date loadDate;
        
        public getTotalPages_User(int pagesize, Date loadDate){
            this.pagesize = pagesize;
            this.loadDate = loadDate;
        }

        @Override
        protected Integer call() throws Exception {
            if(pagesize <= 0) return null;
            SessionFactory factory = HibernateUtils.getSessionFactory();
            Session session = factory.openSession();
            session.getTransaction().begin();

            int totalPages = UserDAO.getTotalPage_User(session, pagesize, loadDate);

            session.getTransaction().commit();
            session.close();
            return totalPages;
        }
    }
    
    public static class UserBusException extends Exception{
        private String explanationString;
        public UserBusException(String message, String explanationString) {
            super(message);
            this.explanationString = explanationString;
        }
        public String getExplanationString(){
            return explanationString;
        }
    }
    
    public static boolean checkUserAvailable(){
        return currentUser != null;
    }
    
    public static boolean checkUsernameAvailable(String username){
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        session.getTransaction().begin();
        User check = UserDAO.getActiveUser(session, username);
        session.getTransaction().commit();
        session.close();
        return check == null;
    }
    
    public static Stage getLogInSignUpStage(){
        if(LogInSignUpStage == null) {
            LogInSignUpStage = new Stage();
        }
        return LogInSignUpStage;
    }
    
    public static User getCurrentUser(){
        return currentUser;
    }
    
    public static void logoutCurrentUser(){
        currentUser = null;
    }
    
    
    
    public static void addUser(User user) throws UserBusException{
        if(user.getUsername().length() < 8 || user.getUsername().length() > 200){
            throw new UserBusException("Xảy ra lỗi khi thực hiện đăng ký", "Username phải từ 8 đến 200 ký tự");
        }
        if(user.getPassword().length() < 8 || user.getPassword().length() > 200){
            throw new UserBusException("Xảy ra lỗi khi thực hiện đăng ký", "Mật khẩu phải từ 8 đến 200 ký tự");
        }
        if(user.getEmail().length() < 8 || user.getEmail().length() > 200){
            throw new UserBusException("Xảy ra lỗi khi thực hiện đăng ký", "Email phải từ 8 đến 200 ký tự");
        }
        if(user.getTen() != null && user.getTen().length() > 24){
            throw new UserBusException("Xảy ra lỗi khi thực hiện đăng ký", "Tên không được dài quá 24 ký tự");
        }
        if(!UserBus.checkUsernameAvailable(user.getUsername())){
            throw new UserBusException("Xảy ra lỗi khi thực hiện đăng ký", "Username đã tồn tại");
        }
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        try{
            session.getTransaction().begin();
            UserDAO.addUser(session, user);
        } catch (Exception ex){
            ex.printStackTrace(System.err);
            session.getTransaction().rollback();
            session.close();
            return;
        } 
        session.getTransaction().commit();
        session.close();
    }
    
    public static User getUser(String username, String password){
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        session.getTransaction().begin();
        User check = UserDAO.getActiveUser(session, username);
        if(check == null){
            session.getTransaction().rollback();
            session.close();
            return null;
        }
        session.getTransaction().commit();
        session.close();
        if(BCrypt.checkpw(password, check.getPassword())){
            currentUser = check;
            return check;
        }
        return null;
    }
    
    private static void setNewUserInfo(User user){
        currentUser = user;
    }
    
    public static User refreshInfoCurrentUser(){
        if(currentUser == null) return null;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        session.getTransaction().begin();
        session.refresh(currentUser);
        session.getTransaction().commit();
        session.close();
        return currentUser;
    }
    
    public static void updateBlockUser(User user) throws UserBusException{
        if(user == null) return;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        try{
            session.getTransaction().begin();
            boolean ttblock = user.tinhtrangblockProperty().get();
            session.refresh(user);
            boolean ttNew = user.isTinhTrangBlock();
            if(!ttNew){
                if(ttblock){
                    throw new UserBusException("Lỗi bỏ chặn đối tượng chưa bị chặn", "Đối tượng này chưa bị chặn. Vui lòng refresh thông tin");  
                }
            }else{
                if(!ttblock){
                    throw new UserBusException("Lỗi chặn đối tượng đã bị chặn", "Đối tượng này đã bị chặn rồi. Vui lòng refresh thông tin");  
                }
            }
            user.setLatestModified(new Date());
            user.setTinhTrangBlock(!ttNew);
            UserDAO.updateUser(session, user);
        } catch (UserBusException ex){
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
    
    public static void suaDoiUser(User user, String newPW, String usertyped_oldPW) throws UserBusException{
        if(user == null) return;
        SessionFactory factory = HibernateUtils.getSessionFactory();
        Session session = factory.openSession();
        try{
            session.getTransaction().begin();

            String oldPW_hashed = user.getPassword();
            if(newPW == null || newPW.length() == 0){
                user.setLatestModified(new Date());
                setNewUserInfo(user);
                UserDAO.updateUser(session, user);
                session.getTransaction().commit();
                session.close();
                return;
            } 
            //Have changed password
            boolean check = BCrypt.checkpw(usertyped_oldPW, oldPW_hashed);   
            if(!check){
                throw new UserBusException("Xảy ra lỗi khi thực hiện thay đổi", "Mật khẩu cũ sai");
            }
            user.setPassword(BCrypt.hashpw(newPW, BCrypt.gensalt()));

            user.setLatestModified(new Date());
            setNewUserInfo(user);
            UserDAO.updateUser(session, user); 
        } catch (UserBusException ex){
            session.getTransaction().rollback();
            session.close();
            throw ex;
        } catch (HibernateException ex){
            ex.printStackTrace(System.err);
            session.getTransaction().rollback();
            session.close();
            return;
        } 
        session.getTransaction().commit();
        session.close();
    }
    
}

# ConventionHub
 JavaFX + Hibernate app to manage and join conventions.<br>
 Chương trình JavaFX + Hibernate để quản lý và tham dự hội nghị  

-----------------------------------------------------------------

  ### Chạy chương trình
   - Cài đặt **MySQL Server và Connector/J**, bật Server lên và chạy **script sql có trong folder scriptForDB**
   - MK cho các tài khoản trong DB là 12345678 (nguoidung1 đến nguoidung10 và cả TK admin abc123456)
   - **Vì ứng dụng truy cập vào DB dưới tài khoản root, mật khẩu 1234, cần phải có tài khoản như vậy đủ quyền thêm xóa sửa query các bảng trong CSDL**
   - Sau đó, cài **JDK từ 13 trở lên**. Cấu hình JAVA_HOME, Path,... đầy đủ (có thể reset lại máy nếu chưa chắc để tạo môi trường Java cho máy)
   - Bật MySQL Server, copy toàn bộ bên trong folder deploy của project ra đặt vào folder mình muốn (**gọi đường dẫn đến folder này là (1)**)
     1. Nếu máy là Windows, chạy file ConventionHub-windows.bat
     2. Nếu máy là macOS, .... (cập nhật sau)
     3. Nếu máy không nằm trong 2 loại trên hoặc có nhưng chưa có hướng dẫn ở trên. Mở terminal/shell/cmd/... của máy lên và chạy dòng lệnh sau:
     > java --add-opens java.base/java.lang=javassist -Djava.library.path="**{native-lib-folder}**" --module-path "**{current-folder}**/lib" --add-modules javafx.controls,javafx.fxml -jar **{current-folder}**/ConventionHub.jar
     - Thay các tham số (được in đậm, thay cả dấu ngoặc {})
       - **{current-folder}** : bằng **đường dẫn (1) ở bên trên** đã nhắc tới
       - **{native-lib-folder}** : bằng đường dẫn đến folder lưu các thư viện native của từng nền tảng (windows, mac,...). **Lưu ý**: **các folder lưu các thư viện native của các nền tảng** được cung cấp sẵn bên trong folder con lib của **đường dẫn (1) bên trên**, và **có tên là native-windows, native-mac,...**. Nếu **không tìm thấy** thư viện native được cung cấp sẵn như đã nói, của nền tảng hiện tại muốn chạy ứng dụng trên, có thể lên mạng tìm kiếm các thư viện native của **của các thư viện có trong folder librariesForProject của project** tải về.
   - Sử dụng chương trình...
  
-----------------------------------------------------------------

# Các thư viện để chạy ứng dụng, các file Jar cần thiết, IDE sử dụng để viết mã nguồn, ...
 - Connector đến MySQL <br>
 - Hibernate 4.3.x (JPA 2.1) **đã thay thế file jar jboss-transaction-api_1.2_spec có phiên bản 1.0.0 bằng phiên bản 1.1.1 Final được down trên mạng** <br> 
 - JavaFX 11.0.2 trở lên <br>
**->** Tẩt cả đều được cung cấp trong **folder librariesForProject của project**, có thể dùng để chạy mã nguồn ứng dụng từ IDE <br>
 - Mã nguồn được viết với IDE **Netbeans 11.0**, là project Ant

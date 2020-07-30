CREATE DATABASE  IF NOT EXISTS `quanlyhoinghi` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `quanlyhoinghi`;
-- MySQL dump 10.13  Distrib 8.0.18, for Win64 (x86_64)
--
-- Host: localhost    Database: quanlyhoinghi
-- ------------------------------------------------------
-- Server version	8.0.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chitiet_hoinghi`
--

DROP TABLE IF EXISTS `chitiet_hoinghi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chitiet_hoinghi` (
  `MaHN` int(10) unsigned NOT NULL,
  `Mota_ChiTiet` text NOT NULL,
  PRIMARY KEY (`MaHN`),
  UNIQUE KEY `MaHN_UNIQUE` (`MaHN`),
  CONSTRAINT `FK_CTHN_HN` FOREIGN KEY (`MaHN`) REFERENCES `hoinghi` (`MaHN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chitiet_hoinghi`
--

LOCK TABLES `chitiet_hoinghi` WRITE;
/*!40000 ALTER TABLE `chitiet_hoinghi` DISABLE KEYS */;
INSERT INTO `chitiet_hoinghi` VALUES (1,'Cơn thiếu máu não thoáng qua (Transient Ischemic Attack – TIA) là những rối loạn chức năng của hệ thần kinh trung ương khởi bệnh một cách đột ngột của các dấu hiệu của hệ thần kinh khu trú thoáng qua, thời gian kéo dài trong vài giây, vài phút, vài giờ, nhưng không để lại hậu quả gì. Tuy nhiên, đây lại là dấu hiệu báo động phải cảnh giác vì quá trình đột quỵ có thể xảy ra bất cứ lúc nào sau cơn thiếu máu thoáng qua. Với đột quỵ não nhẹ hoặc cơn thiếu máu não thoáng qua, đôi khi bệnh nhân thường bỏ qua các triệu chứng và việc chẩn đoán rất khó khăn.\n\nNếu không được chẩn đoán và xử trí kịp thời, tình trạng này có thể diễn tiến nặng hơn trở thành nhồi máu não để lại di chứng nặng nề về sau cũng như tỉ lệ tử vong cao\n\nVới các bệnh nhân bị thiếu máu não thoáng qua hoặc đột quỵ nhỏ, nguy cơ tái phát xảy ra trong 2 ngày đầu và những ngày tiếp theo là rất cao. Và khi xảy ra tái phát các triệu chứng của đột quỵ, có thể là 30% trong số đó có thể tử vong hoặc 40% có thể dẫn đến tái phát nặng. Bệnh nhân có thể vừa tái diễn đột quỵ nặng, vừa bị các biến cố tim mạch, nhồi máu cơ tim hoặc các biến cố ngoại vi khác\n\nViệc nhận biết sớm các triệu chứng và có phương thức điều trị kịp thời cơn thiếu máu não thoáng qua hoặc đột quỵ nhẹ đóng vai trò quan trọng trong việc phòng ngừa nguy cơ tái phát các biến cố xảy ra như đột quỵ, các biến cố tim mạch, nhồi máu cơ tim hoặc các biến cố mạch máu ngoại vi khác.'),(2,'Theo thống kê, có đến 62% dân số thế giới gặp phải các vấn đề tiêu hóa như táo bón, khó tiêu, tiêu chảy, đau dạ dày. Trong đó, tiêu chảy là một trong những bệnh thường gặp nhất ở trẻ em.\n\nNhằm cập nhật những giải pháp hiệu quả trong điều trị, phòng ngừa bệnh tiêu chảy và tình trạng rối loạn hệ vi sinh đường ruột của trẻ em, công ty TNHH Sanofi-Aventis Việt Nam đã tổ chức chuỗi Hội thảo khoa học “Điều trị và phòng ngừa tiêu chảy ở trẻ em ngày hè” - phối hợp cùng Hội Nhi khoa Việt Nam và Hội Vi sinh lâm sàng Tp.HCM\n\nBệnh tiêu chảy ở trẻ em thường có nguy cơ tăng cao vào mùa hè vì đây là thời điểm thuận lợi cho các mầm vi khuẩn sinh sôi, thức ăn và nguồn nước dễ bị nhiễm khuẩn, từ đó dễ khiến cho trẻ em bị rối loạn hệ vi sinh đường ruột, biểu hiện qua bệnh tiêu chảy.\n\nCác biện pháp được khuyến cáo để điều trị bệnh tiêu chảy bao gồm bù nước và điện giải cho cơ thể, bổ sung kẽm, sử dụng các loại thức ăn dạng lỏng dễ hấp thu và sử dụng kháng sinh theo chỉ định của bác sĩ. Ngoài ra, việc bổ sung các lợi khuẩn (probiotic) hay còn gọi là men vi sinh là biện pháp hỗ trợ được khuyến khích trong phác đồ điều trị cho trẻ.'),(3,'Nhiều người với triệu chứng của bệnh trầm cảm không tự nhận mình bị trầm cảm. Một số người không tự nhận thức những triệu chứng này, trong khi những người khác có thể gặp khó khăn trong việc thừa nhận mình bị trầm cảm. Đây có lẽ là một vấn đề tế nhị. Một cá nhân có thể cảm thấy thất bại hoặc rằng người khác sẽ đánh giá mình. Nhưng đây là những gì quý vị nên biết: đối với người chăm sóc, bệnh trầm cảm thường gặp nhiều hơn là quý vị nghĩ, và đó là một phản ứng bình thường cho một hoàn cảnh khó khăn. \n\nTrầm cảm là một bệnh lý vô cùng phức tạp với nhiều nghiên cứu đang được tiến hành để xác định chính xác nguyên nhân. Những nhân tố đóng góp đã biết bao gồm các đặc tính di truyền, mức độ nội tiết tố, các yếu tố kích phát từ môi trường, thuốc men, hệ quả của việc sống chung với một bệnh nặng, đau buồn và mất mát do cái chết của một người thân, trải qua việc bị hành hạ về thể chất hoặc cảm xúc, sống với một người trầm cảm nặng, và những nhân tố khác. Không phải ai cũng trải qua những cảm xúc tiêu cực đi đôi với bệnh trầm cảm. Nhưng chúng ta biết rằng trong nỗ lực để cung cấp sự chăm sóc tốt nhất cho một thành viên gia đình hoặc bạn bè, người chăm sóc thường hy sinh các nhu cầu về thể chất và cảm xúc của chính họ. Các khía cạnh phức tạp và đa dạng liên quan đến việc cung cấp sự chăm sóc có thể là gánh nặng ngay cả đối với người có khả năng nhất. Cảm xúc choáng ngợp, bồn chồn, lo âu, đau khổ, bi quan, tách biệt, kiệt sức—và đôi khi tội lỗi vì có những cảm xúc này—có thể làm nên gánh nặng.\n\nMọi người đều có suy nghĩ hoặc cảm xúc tiêu cực khi này khi khác, nhưng khi những cảm xúc này mạnh hơn và làm cho quý vị cạn kiệt sức lực, buồn rầu hoặc khó chịu đối với người thân, đó có thể là dấu hiệu cảnh báo cho bệnh trầm cảm. Những quan ngại về bệnh trầm cảm xuất hiện khi cảm giác trống rỗng và khóc lóc không hết, hoặc khi những cảm xúc tiêu cực này cứ đến liên tục.\n\nThật không may, cảm xúc trầm cảm thường được xem là dấu hiệu của sự yếu đuối hơn là dấu hiệu của một điều gì đó đã mất cân bằng. Những lời nhận xét của người khác như “vượt qua nó đi” hoặc “tại mình nghĩ vậy thôi” không hề có ích, và phản ánh một niềm tin rằng những mối quan ngại về sức khỏe là không có thật.'),(4,'Trong thời gian gần đây, giáo dục đại học Việt Nam nói riêng và châu Á nói chung đã phát triển mạnh mẽ, đạt được nhiều thành tựu đáng ghi nhận. Tuy nhiên, hệ thống giáo dục này cũng bộc lộ nhiều vấn đề cần nghiên cứu, giải quyết và đổi mới. Nổi bật là các vấn đề về chất lượng, tài chính và quản trị, đánh giá và kiểm định chất lượng, hội nhập và hợp tác quốc tế. Những vấn đề kể trên đều đóng vai trò quan trọng không chỉ đối với Việt Nam mà còn đối với các nước châu Á vì hầu hết các nước đều xem giáo dục đại học như là một chiến lược nhằm xây dựng một xã hội thông tin dựa trên nền tảng tri thức trong thế kỷ XXI. \n\nĐể trở thành một chiến lược hiệu quả phục vụ cho sự phát triển kinh tế - xã hội, giáo dục đại học cần phải thực hiện nhiều hơn vai trò kiến tạo tri thức mới, tiếp thu và ứng dụng các xu thế công nghệ, đào tạo nguồn nhân lực thông minh thông qua hoạt động giảng dạy, nghiên cứu khoa học và hợp tác quốc tế.\n\nHội thảo tập trung vào các nội dung sau:\n1. Thành tựu và hạn chế của đổi mới giáo dục đại học Việt Nam và châu Á;\n2. Tương quan so sánh giữa giáo dục đại học Việt Nam và châu Á;\n3. Chính sách, quản trị và tiếp cận giáo dục đại học Việt Nam và châu Á;\n4. Đánh giá, kiểm định và xếp hạng giáo dục đại học Việt Nam và châu Á;\n5. Hợp tác trong giáo dục đại học giữa Việt Nam và châu Á.'),(5,'Rối loạn lo âu là sự sợ hãi quá mức không có nguyên nhân do chủ quan của người bệnh và không thể giải thích được do một bệnh tâm thần khác hoặc do một bệnh cơ thể. Khi lo âu và sợ hãi quá mức ảnh hưởng nghiêm trọng đến cuộc sống, vẫn tiếp tục ngay cả khi mối lo thực tế đã kết thúc thì đó là bệnh lý.\n\nRối loạn lo âu thường gặp ở nữ giới nhiều hơn nam giới. Tuy nhiên, bệnh có thể xuất hiện ở mọi lứa tuổi. Nếu không kịp thời phát hiện và điều trị bệnh sẽ trở nên nguy hiểm và khó chữa hơn. Các dấu hiệu, triệu chứng nhận biết rối loại lo âu ở cơ thể người bao gồm:\n- Cảm thấy lo lắng, căng thẳng, bồn chồn.\n- Nhịp tim nhanh.\n- Thở nhanh.\n- Đổ mồ hôi nhiều.\n- Run sợ.\n- Cảm thấy mệt mỏi hoặc yếu đuối.\n- Khó tập trung hay có những suy nghĩ linh tinh.\n- Khó ngủ.\n- Rối loạn tiêu hóa. Gặp khó khăn trong kiểm soát lo lắng.\n- Né tránh những thứ gây ra sự lo lắng.\n\nBệnh có các biểu hiện về tâm trạng như luôn bất an, hồi hộp, còn thể chất thì hay run rẩy, căng cứng bắp thịt, vã mồ hôi, thắt ngực, nóng lưng, đau bụng, khó ngủ. Do các biểu hiện này bệnh nhân thường tìm đến các bác sĩ đa khoa để tìm các tổn thương thể chất cho đến khi không tìm được nguyên nhân thì mới tìm đến các bác sĩ tâm lý. Một trong các tiêu chuẩn chẩn đoán quan trọng là lo âu quá mức hàng ngày trong thời gian ít nhất 6 tháng.\n\nĐể hỗ trợ đối phó tốt đối với những y bác sĩ quan tâm vấn đề cũng như trang bị kiến thức phòng tránh bệnh cho các cá nhân quan tâm, Cty TNHH Alaphi tổ chức buổi hội thảo \"Điều trị bệnh rối loạn lo âu hiệu quả\" để chia sẻ chi tiết giải pháp của mình.'),(6,'Rối loạn lưỡng cực là chứng bệnh rối loạn tâm thần hay còn gọi là rối loạn hưng - trầm cảm, tình trạng tâm thần thay đổi thất thường khiến tâm trạng có thể đột ngột hưng phấn như phấn khích quá hoặc tăng động, nhiều lúc lại rơi vào trạng thái trầm cảm. Bệnh rối loạn lưỡng cực có tính chất chu kỳ, xen kẽ giữa trầm cảm và hưng phấn. Sự thất thường của trạng thái tâm lý người bệnh thường xuất hiện vài lần trong năm hoặc có thể nhiều lần trong tuần.\n\nChẩn đoán và điều trị rối loạn cảm xúc lưỡng cực luôn luôn là một thách thức lớn với giới y khoa. Với mong muốn mang đến những cập nhật kiến thức mới và cơ hội gặp gỡ, giao lưu cũng như trao đổi các kinh nghiệm trong quá trình điều trị rối loạn cảm xúc lưỡng cực, Sanofi rất hân hạnh được phối hợp cùng Bệnh viện Tâm Thần TP. HCM và Viện Sức khỏe Tâm Thần - Bệnh viện Bạch Mai Hà Nội tổ chức buổi Hội thảo khoa học “CẬP NHẬT CHẨN ĐOÁN & TỐI ƯU HÓA ĐIỀU TRỊ RỐI LOẠN CẢM XÚC LƯỠNG CỰC”.\n\nChương trình gồm 2 bài báo cáo chính là “CẬP NHẬT CHẨN ĐOÁN VÀ ĐIỀU TRỊ RỐI LOẠN CẢM XÚC LƯỠNG CỰC” và “TỐI ƯU HÓA ĐIỀU TRỊ RỐI LOẠN CẢM XÚC LƯỠNG CỰC”, tiếp nối chương trình với phần giới thiệu giải pháp điều trị mới với những đặc điểm thuận lợi, là thuốc ổn định khí sắc đầu tay và là nền tảng trong điều trị mọi thể Rối loạn lưỡng cực; cùng các phần tham luận đã nhận được sự tham gia đông đảo và nhiệt tình của các Bác sĩ và Dược sĩ.\n\nSanofi giới thiệu 1 giải pháp mới nhằm giúp các bệnh nhân trong cuộc chiến cân bằng và ổn định khí sắc trong Rối loạn cảm xúc lưỡng cực.'),(7,'Hội thảo được khoa CNTT và Công ty TNHH An phát tổ chức với mục tiêu cung cấp các kiến thức và công nghệ mới cho CBGV, sinh viên đang theo học chuyên ngành Công nghệ thông tin của Nhà trường. Thông qua chương trình Hội thảo giúp giảng viên và sinh viên khoa CNTT tiếp cận với các công nghệ mới về mạng, mạng không dây đồng thời cải tiến và nâng cao chất lượng giảng dạy các học phần về mạng, góp phần bồi dưỡng kỹ năng mạng cho sinh viên; Mở rộng và tăng cường quan hệ hợp tác với doanh nghiệp, góp phần xúc tiến cơ hội việc làm cho sinh viên của Nhà trường.\n\nChủ đề được các chuyên gia trao đổi trong hội thảo gồm: Giới thiệu và thao tác trên cáp mạng Dintek; Cách lựa chọn cáp tốt và thi công cáp đạt chất lượng cao; Hướng dẫn thi công hệ thống cáp cấu trúc đúng kỹ thuật; Thực hành trực tiếp trên bộ công cụ thi công cápDintek, thao tác bấm đầu mạng dây xuyên ezi-PLUG. Ngoài các lý thuyết được truyền đạt, trong 2 ngày hội thảo khoa CNTT bố trí 04 lớp sinh viên đại học chính quy khóa 9 tham gia trực tiếp thực hành cùng các chuyên gia của công ty.\n\nHy vọng với những kiến thức, kinh nghiệm trong triển khai thực tế của các chuyên gia, được thực hành và tiếp cận ngay trên các thiết bị mới của Dintek'),(8,'Hội nghị là  diễn đàn để các nhà khoa học và quản lý đóng góp ý kiến cho phương hướng, nhiệm vụ phát triển hoạt động đo lường trong giai đoạn công nghiệp hóa, hiện đại hóa đất nước và là hoạt động thiết thực nhằm đẩy mạnh công tác truyền thông về hoạt động đo lường của Đề án “Tăng cường, đổi mới hoạt động đo lường hỗ trợ doanh nghiệp Việt Nam nâng cao năng lực cạnh tranh và hội nhập quốc tế giai đoạn đến năm 2025, định hướng đến năm 2030”\n\nĐối tượng tham dự là cán bộ khoa học và quản lý trong lĩnh vực đo lường tại các Bộ, ngành, cơ sở sản xuất, kinh doanh, TRường Đại học, Viện nghiên cứu… trong nước và quốc tế; Cán bộ làm công tác đo lường, hiệu chuẩn, kiểm định, phương tiện đo trong hệ thống cơ quan quản lý Tiêu chuẩn Đo lường Chất lượng và Bộ, ngành; Nghiên cứu sinh, sinh viên các trường đại học có liên quan và quan tâm.'),(9,'Đại biểu khách mời gồm có: Bà Đào Kim Nguyễn Thụy Hằng – Đại học Quốc gia Thành phố Hồ Chí Minh, ông Nguyễn Minh Thiên Hoàng – Phó trưởng phòng Phòng Giáo dục Tiểu học – Sở GD&ĐT TP.HCM, ông Nguyễn Trọng Khiêm – Phó trưởng Phòng GD&ĐT quận Tân Phú cùng đại diện Ban Giám hiệu, giảng viên, giáo viên các trường đại học, THPT, THCS, phụ huynh và học sinh quận 5, quận 6, quận Tân Phú… tham dự.\n\nVề phía báo đài: Hội thảo nhận được sự quan tâm của báo đài như: Kênh Truyền hình Quốc hội, Báo Thanh niên, Báo Sài Gòn Giải phóng, Báo Tuổi trẻ, Báo Văn hóa, Báo Pháp luật Thành phố.v.v.\n\nVề phía Ban Tổ chức có: TS. Lê Chi Lan – Phó Hiệu trưởng Trường Đại học Sài Gòn, GS.TS. Huỳnh Văn Sơn – Phó Hiệu trưởng Trường Đại học Sư phạm TP.HCM, TS. Phạm Thị Thanh Tú – Hiệu trưởng Trường Tiểu học Thực hành Đại học Sài Gòn.v.v.\n\nHội thảo là diễn đàn trao đổi, thảo luận giữa các nhà khoa học, nhà quản lí giáo dục, giáo viên và chuyên gia trong nước xoay quanh vấn đề “Xây dựng hệ thống tiêu chí và phương pháp đánh giá giáo dục phổ thông Việt Nam”. Hội thảo tập trung thảo luận, phân tích 3 nhóm tiêu chí: đầu vào (input), đầu ra (output) và bối cảnh (context). Ba nhóm tiêu chí này là tổ hợp của 76 chỉ báo xoay quanh các nội dung gồm: hoạt động dạy, hoạt động học, năng lực của giáo viên, trang thiết bị, cơ sở vật chất… thể hiện trong một mô hình đã được kiểm định và xác thực khả năng dự báo, đánh giá chất lượng giáo dục phổ thông là đáng tin cậy.\n\nVới những thành quả tiếp cận ban đầu, Hội thảo hi vọng sẽ tiếp nhận nhiều thông tin cần thiết để hoàn thiện phương pháp và hệ thống tiêu chí đánh giá chất lượng giáo dục phổ thông. Đây sẽ là các thông tin tham chiếu đáng lưu ý với các cơ quan quản lý giáo dục nhà nước nhằm tiếp sức cho chiến lược đổi mới căn bản và toàn diện giáo dục Việt Nam.'),(10,'Đây là lần thứ hai Viện Tim tổ chức hội nghị này. Khác với hội nghị lần đầu sử dụng tiếng Việt, hội nghị lần thứ hai hoàn toàn sử dụng tiếng Anh với 8 diễn giả nước ngoài (Úc, Nhật, Trung Quốc, Singapore, Malaysia, Thái Lan… ) và một số diễn giả trong nước đến từ Viện Tim và các trung tâm tim mạch lớn.\n\nĐặc biệt GS Alain Carpentier, đồng sáng lập Viện Tim TP.HCM, đã xác nhận tham gia hội nghị.\n\nBốn chủ đề chính của hội nghị là: sửa van hai lá, điều trị rung nhĩ, phẫu thuật ít xâm lấn và phẫu thuật bệnh tim bẩm sinh phức tạp.\n\nTheo PGS – TS – BS Nguyễn Văn Phan, Phó giám đốc Viện Tim TP.HCM, mục đích chính của hội nghị là nhằm cập nhật những kiến thức và kỹ thuật mổ tim mới cho giới chuyên môn Việt Nam.\n\nNgoài phần trình bày cập nhật kiến thức mới và chia sẻ kinh nghiệm của diễn giả, hội nghị cũng dành phần lớn thời gian để thảo luận về những ca phẫu thuật điển hình được thực hiện tại Viện Tim. Hội nghị là cơ hội quý báu cho giới chuyên môn trong và ngoài nước giao lưu và học hỏi lẫn nhau');
/*!40000 ALTER TABLE `chitiet_hoinghi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dangkyhoinghi`
--

DROP TABLE IF EXISTS `dangkyhoinghi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dangkyhoinghi` (
  `user` int(10) unsigned NOT NULL,
  `hoinghidangky` int(10) unsigned NOT NULL,
  `DaDuocDuyet` tinyint(1) NOT NULL DEFAULT '0',
  `createdDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user`,`hoinghidangky`),
  KEY `FK_userhoinghi_hoinghi_idx` (`hoinghidangky`),
  CONSTRAINT `FK_dkhoinghi_hoinghi` FOREIGN KEY (`hoinghidangky`) REFERENCES `hoinghi` (`MaHN`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_dkhoinghi_user` FOREIGN KEY (`user`) REFERENCES `user` (`iduser`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dangkyhoinghi`
--

LOCK TABLES `dangkyhoinghi` WRITE;
/*!40000 ALTER TABLE `dangkyhoinghi` DISABLE KEYS */;
INSERT INTO `dangkyhoinghi` VALUES (1,1,0,'2020-07-30 09:26:16'),(1,4,1,'2019-07-23 19:34:43'),(2,1,0,'2020-07-30 11:48:57');
/*!40000 ALTER TABLE `dangkyhoinghi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `diadiem`
--

DROP TABLE IF EXISTS `diadiem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `diadiem` (
  `MaDiaDiem` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TenDiaDiem` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `DiaChi` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `SucChua` int(11) NOT NULL DEFAULT '0',
  `createdDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `latestModified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaDiaDiem`),
  UNIQUE KEY `MaDiaDiem_UNIQUE` (`MaDiaDiem`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `diadiem`
--

LOCK TABLES `diadiem` WRITE;
/*!40000 ALTER TABLE `diadiem` DISABLE KEYS */;
INSERT INTO `diadiem` VALUES (1,'Pavillon Tân Sơn Nhất','202 Hoàng Văn Thụ, Phường 9, Phú Nhuận, Thành phố Hồ Chí Minh',2,'2020-07-10 21:07:51','2020-07-30 13:51:06'),(2,'Metropole Wedding & Convention Center','216 Lý Chính Thắng, Phường 9, Quận 3, Thành phố Hồ Chí Minh',5,'2020-07-27 13:21:19','2020-07-29 20:13:03'),(3,'CAPELLA GALLERY HALL','24 Đường 3 Tháng 2, Phường 12, Quận 10, Thành phố Hồ Chí Minh',3,'2020-07-27 13:29:40','2020-07-29 20:18:05');
/*!40000 ALTER TABLE `diadiem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `hoinghi`
--

DROP TABLE IF EXISTS `hoinghi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `hoinghi` (
  `MaHN` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `TenHN` varchar(70) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `Mota_Ngangon` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `HinhAnh` varchar(260) DEFAULT NULL,
  `ThoiDiemBatDau` datetime NOT NULL,
  `ThoiDiemKetThuc` datetime NOT NULL,
  `DiaDiemToChuc` int(10) unsigned NOT NULL,
  `createdDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastestModified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`MaHN`),
  UNIQUE KEY `MaHoiNghi_UNIQUE` (`MaHN`),
  KEY `FK_HN_DiaDiem_idx` (`DiaDiemToChuc`),
  CONSTRAINT `FK_hoinghi_diadiem` FOREIGN KEY (`DiaDiemToChuc`) REFERENCES `diadiem` (`MaDiaDiem`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `hoinghi`
--

LOCK TABLES `hoinghi` WRITE;
/*!40000 ALTER TABLE `hoinghi` DISABLE KEYS */;
INSERT INTO `hoinghi` VALUES (1,'Tiếp cận và điều trị tích cực cho bệnh nhân thiếu máu não','Hội nghị chú trọng giải thích những nguy cơ tiềm ẩn đến từ thiếu máu não. Cụ thể là khả năng đột quỵ cao,...',NULL,'2020-12-11 12:00:00','2020-12-11 20:00:00',1,'2020-07-10 20:57:24','2020-07-30 09:26:01'),(2,'Điều trị và Phòng ngừa bệnh tiêu chảy ở trẻ em ngày hè','Bước vào mùa hè, với sự thay đổi của thời tiết và các thói quen sinh hoạt là thời điểm bệnh tiêu chảy gia tăng mạnh, gây ảnh hưởng đến sức khỏe của trẻ nếu không có biện pháp phòng ngừa và điều trị',NULL,'2020-12-11 12:00:00','2020-12-11 20:00:00',2,'2020-07-10 20:57:24','2020-07-29 15:53:48'),(3,'Nhận biết và điều trị bệnh trầm cảm','Viện Nghiên cứu ứng dụng khoa học tâm lý và giáo dục (Aripes) đã tổ chức Hội thảo chuyên đề \"Trầm cảm - Nhận biết và chiến lược điều trị\"',NULL,'2020-12-14 13:00:00','2020-12-14 17:00:00',1,'2020-07-10 23:14:25','2020-07-29 16:03:19'),(4,'Giáo dục đại học Việt Nam và châu Á: Tương quan và cơ hội hợp tác',' Hội thảo sẽ tạo ra một diễn đàn quốc tế để các cơ sở và tổ chức giáo dục, chuyên gia, nhà khoa học công bố các kết quả nghiên cứu mới, trao đổi học thuật, tìm kiếm cơ hội hợp tác',NULL,'2019-12-11 07:30:00','2019-12-11 15:30:00',1,'2020-07-10 23:17:24','2020-07-29 15:37:24'),(5,'Điều trị bệnh rối loạn lo âu hiệu quả','Hội chứng rối loạn lo âu hay rối nhiễu lo âu (tên tiếng Anh là: anxiety disorder) là rối loạn đặc trưng bởi sự lo lắng thái quá và căng thẳng thường xuyên mà không có lý do rõ ràng',NULL,'2020-12-14 08:00:00','2020-12-14 12:00:00',2,'2020-07-10 23:17:24','2020-07-29 15:20:13'),(6,'Cập nhật Chẩn đoán & Tối ưu hóa Điều trị Rối loạn Cảm xúc Lưỡng cực','Rối loạn cảm xúc lưỡng cực là một bệnh lý phức tạp, rất khó chẩn đoán điều trị. Nếu không được điều trị kịp thời đầy đủ thì có thể gây ra những hậu quả nặng cho bản thân người bệnh, gia đình và xã hội',NULL,'2020-12-11 07:15:00','2020-12-11 11:45:00',3,'2020-07-27 21:00:38','2020-07-29 15:08:30'),(7,'Một số giải pháp để nâng cao công nghệ mạng, mạng không dây','Công ty TNHH CNTT An Phát tổ chức Hội thảo với chủ đề “Một số giải pháp để nâng cao công nghệ mạng và mạng không dây cho sinh viên ngành Công nghệ thông tin”',NULL,'2020-12-11 12:30:00','2020-12-11 16:30:00',3,'2020-07-28 12:15:53','2020-07-29 15:51:59'),(8,'Hội nghị Khoa học Kỹ thuật Đo lường Toàn Quốc','Hội nghị được tổ chức, với mục tiêu nhằm tổng kết, đánh giá kết quả các hoạt động nghiên cứu, ứng dụng khoa học kỹ thuật đo lường phục vụ phát triển kinh tế đất nước, đời sống, an ninh, quốc phòng',NULL,'2020-07-29 13:00:00','2020-07-29 17:00:00',1,'2020-07-28 12:24:42','2020-07-29 15:53:11'),(9,'Đánh giá Tổng quát Chất lượng Giáo dục Phổ thông Việt Nam','Trường Đại học Sài Gòn phối hợp cùng Trường Đại học Sư phạm Thành phố Hồ Chí Minh tổ chức Hội thảo “Đánh giá tổng quát chất lượng giáo dục phổ thông Việt Nam – Tiếp cận và thách thức”',NULL,'2020-07-31 14:15:00','2020-07-31 17:15:00',3,'2020-07-28 16:36:07','2020-07-29 15:24:10'),(10,'Cập nhật những tiến bộ mới trong phẫu thuật tim','Hội nghị do Viện Tim TP.HCM và Hội Phẫu thuật Lồng ngực và Tim mạch Việt Nam đồng tổ chức',NULL,'2020-07-28 10:49:00','2020-07-28 16:49:00',2,'2020-07-28 19:50:33','2020-07-29 15:58:47');
/*!40000 ALTER TABLE `hoinghi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tinhtrangxoa_diadiem`
--

DROP TABLE IF EXISTS `tinhtrangxoa_diadiem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tinhtrangxoa_diadiem` (
  `madiadiem` int(10) unsigned NOT NULL,
  `tinhtrangxoa` tinyint(1) NOT NULL,
  `createdDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `latestModified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`madiadiem`),
  CONSTRAINT `ttxoa_diadiem` FOREIGN KEY (`madiadiem`) REFERENCES `diadiem` (`MaDiaDiem`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tinhtrangxoa_diadiem`
--

LOCK TABLES `tinhtrangxoa_diadiem` WRITE;
/*!40000 ALTER TABLE `tinhtrangxoa_diadiem` DISABLE KEYS */;
/*!40000 ALTER TABLE `tinhtrangxoa_diadiem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tinhtrangxoa_hoinghi`
--

DROP TABLE IF EXISTS `tinhtrangxoa_hoinghi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tinhtrangxoa_hoinghi` (
  `idtinhtrangxoa_hoinghi` int(10) unsigned NOT NULL,
  `tinhtrangxoa` tinyint(1) NOT NULL,
  `latestModified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createdDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`idtinhtrangxoa_hoinghi`),
  CONSTRAINT `TinhTrangXoaHN_Hoinghi` FOREIGN KEY (`idtinhtrangxoa_hoinghi`) REFERENCES `hoinghi` (`MaHN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tinhtrangxoa_hoinghi`
--

LOCK TABLES `tinhtrangxoa_hoinghi` WRITE;
/*!40000 ALTER TABLE `tinhtrangxoa_hoinghi` DISABLE KEYS */;
/*!40000 ALTER TABLE `tinhtrangxoa_hoinghi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `iduser` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Ten` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `TinhTrangBlock` tinyint(1) NOT NULL DEFAULT '0',
  `createdDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `latestModified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `isAdmin` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`iduser`),
  UNIQUE KEY `iduser_UNIQUE` (`iduser`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'Nguyễn Văn B','abc123456','$2a$10$PaAKA9bv63I41wjlEc/lvOFs6KJ8q5oaDr7guBdeih4iHaksMvfaO','banhthilonglanhlonglanhanhbinhminh@gmail.com',0,'2020-07-22 19:20:29','2020-07-30 10:13:05',1),(2,'Kim Dũng Việt','nguoidung1','$2a$10$OwfyfEAVOHlozWReOrUPM.35WWqMyylomUSi0gY43ctmf3Pp1G3o6','kd.viet@gmail.com',0,'2020-07-29 12:00:20','2020-07-30 10:14:36',0),(3,'Bành Hữu Trung','nguoidung2','$2a$10$MVVQRjWjNogo47QPBiZki.6IkY9.43ladrPTX1Toc3oRiGHYraqzW','bh.trung@gmail.com',0,'2020-07-29 12:01:01','2020-07-29 12:01:56',0),(4,'Phan Hữu Châu','nguoidung3','$2a$10$tbHZ6IG52Cmv9NOxLCDhkOH9dl/LfC7e.4nredr/XhHmmWgH1ixtq','ph.chau@gmail.com',0,'2020-07-29 12:02:44','2020-07-29 12:02:44',0),(5,'Phạm Hùng Dũng','nguoidung4','$2a$10$PSO8axcIlH1ZRYIJWevk2OnVrwRZ./Ll/K3pHOoAVUZ3q46K5l53e','ph.dung@gmail.com',0,'2020-07-29 12:04:03','2020-07-29 12:04:03',0),(6,'Đỗ Nguyên Lộc','nguoidung5','$2a$10$m3H/t12KvCWfaqdTW2PuUuGwOdj92aCQwfzLi/YYANaUU6otr.RaS','dn.loc97@gmail.com',0,'2020-07-29 12:04:47','2020-07-29 12:04:47',0),(7,'Huỳnh Kim Thảo','nguoidung6','$2a$10$nuMgEW2EkUYum/xdXTms6uQv56nokV5AuP7PvC5NwMIkvcmwk/hpS','huynh.kthao@gmail.com',0,'2020-07-29 12:09:03','2020-07-29 12:09:03',0),(8,'Hồ Diệu Thúy','nguoidung7','$2a$10$9iuasEG1QI/B2MkwEDcmo.7bzriouvyPYeG24XbrfQCS/NpTUcih6','h.dieuthuy@gmail.com',0,'2020-07-29 12:10:05','2020-07-29 12:10:05',0),(9,'Nguyễn Thanh Thu','nguoidung8','$2a$10$QXFbHEP5EAmXA7lInsMI/uPQ6A9XoVbuKroQCbjAm0kSc5cZHaZYO','ngthanhthu@gmail.com',0,'2020-07-29 12:11:05','2020-07-29 12:11:05',0),(10,'Lê Kim Ly','nguoidung9','$2a$10$J6EhUmfMQZ3A7lFDh1p1vOnre9VBvQcGVRZyqAfh1fkp.Lfu2Vg1O','lekim.ly@gmail.com',0,'2020-07-29 12:11:58','2020-07-29 12:11:58',0),(11,'Nguyễn Thùy Dung','nguoidung10','$2a$10$iQ0HWxZhVBUeTJ9JfsvBiuTnuQ9hVOrWTwRB7IjLBTKEN3vmnpiPS','ngt.dung@gmail.com',0,'2020-07-29 12:13:17','2020-07-29 12:13:17',0);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-30 16:01:57

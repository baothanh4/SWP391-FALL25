# ⚡ EV Service Center Maintenance Management System
**Phần mềm quản lý bảo dưỡng xe điện cho trung tâm dịch vụ**

---

## 🧭 Giới thiệu

**EV Service Center Maintenance Management System** là hệ thống quản lý bảo dưỡng xe điện toàn diện, phát triển bằng **Java Spring Boot 21**, hỗ trợ trung tâm dịch vụ EV trong:
- Quản lý khách hàng & xe điện
- Theo dõi lịch bảo dưỡng
- Phân công kỹ thuật viên
- Quản lý phụ tùng, tài chính
- Thanh toán online (QR / VNPay / eWallet)

---

## 👥 Vai trò hệ thống

| Role | Chức năng chính |
|------|------------------|
| **Customer** | Đặt lịch, xem nhắc nhở, thanh toán online, xem lịch sử bảo dưỡng |
| **Staff** | Tiếp nhận yêu cầu, quản lý lịch bảo dưỡng, cập nhật trạng thái |
| **Technician** | Thực hiện bảo dưỡng, cập nhật tiến độ, ghi nhận kết quả |
| **Admin** | Quản lý hệ thống, nhân sự, phụ tùng, tài chính, báo cáo |

---

## ⚙️ Chức năng chính

### 🔹 1. Dành cho Khách hàng (Customer)
- Nhắc nhở bảo dưỡng định kỳ (km / thời gian)
- Đặt lịch trực tuyến và nhận xác nhận qua email
- Thanh toán online: **QR Banking / VNPay / eWallet**
- Quản lý lịch sử & chi phí bảo dưỡng

### 🔹 2. Dành cho Trung tâm (Staff, Technician, Admin)
- Quản lý khách hàng, hồ sơ xe, và phụ tùng
- Phân công kỹ thuật viên & theo dõi tiến độ
- Báo giá – tạo hóa đơn – thanh toán
- Thống kê doanh thu, lợi nhuận, xu hướng lỗi EV
- Gợi ý AI về nhu cầu phụ tùng thay thế

---

## 🧩 Kiến trúc hệ thống

- **Backend:** Java 21 + Spring Boot 3.3
- **Database:** SQL Server / MySQL
- **Auth:** JWT + Role-based Access Control
- **Payment:** VNPay + VietQR
- **Mail:** JavaMailSender
- **Deploy:** AWS / Render / Railway

---

## 🧱 Cấu trúc thư mục backend
src/<br>
├── main/<br>
│ ├── java/<br>
│ │ └── com/example/SWP391_FALL25/<br>
│ │ ├── Config/ # Cấu hình bảo mật, JWT, WebSocket, Email...<br>
│ │ ├── Controller/ # Các REST API Controller<br>
│ │ ├── DTO/Auth/ # Các DTO liên quan đến xác thực & đăng nhập<br>
│ │ ├── Entity/ # Các entity ánh xạ bảng DB<br>
│ │ ├── Enum/ # Các Enum: Role, Status, PaymentType,...<br>
│ │ ├── ExceptionHandler/ # Xử lý ngoại lệ toàn cục<br>
│ │ ├── Repository/ # Các lớp giao tiếp DB (JPA)<br>
│ │ ├── Service/ # Xử lý logic nghiệp vụ<br>
│ │ ├── Utility/ # Các tiện ích (QR, Email, DateTime,…)<br>
│ │ └── Swp391Fall25Application.java # File khởi chạy chính<br>
│ └── resources/<br>
│ ├── static/ # CSS, JS, Image (nếu có)<br>
│ ├── templates/ # File template HTML (nếu dùng Thymeleaf)<br>
│ └── application.properties # Cấu hình hệ thống<br>
└── test/<br>


## ⚙️ Điều kiện tiên quyết

| Công cụ           | Phiên bản khuyến nghị | Mục đích |
|-------------------|------------------------|-----------|
| **Java JDK**      | 21+ | Ngôn ngữ chính |
| **Spring Boot**   | 3.3+ | Framework backend |
| **Maven**         | 3.9+ | Quản lý dependencies |
| **SQL ServerL**   | 8.0+ | Cơ sở dữ liệu |
| **Git**           | Mới nhất | Quản lý source code |
| **VNPay Sandbox** | - | Kiểm thử thanh toán |
| **IDE**           | IntelliJ IDEA | Phát triển & debug |

---

## ⚙️ Cấu hình trong `application.properties`

```properties
# ==============================
# DATABASE CONFIGURATION
# ==============================
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=database;encrypt=true;trustServerCertificate=true
spring.datasource.username=yourUserNameDatabase
spring.datasource.password=yourPassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# ==============================
# JWT CONFIGURATION
# ==============================
jwt.secret=YourSuperSecretKey123456
jwt.expiration=86400000

# ==============================
# MAIL CONFIGURATION
# ==============================
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# ==============================
# VNPay CONFIGURATION
# ==============================
vnpay.tmncode=YourTMNCode
vnpay.hashSecret=YourHashSecret
vnpay.payUrl=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.returnUrl=https://your-ngrok-url.ngrok-free.app/api/payment/vnpay-return
vnpay.ipnUrl=https://your-ngrok-url.ngrok-free.app/api/payment/vnpay-ipn

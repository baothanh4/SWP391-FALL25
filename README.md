# ⚡ EV Service Center Maintenance Management System
**Phần mềm quản lý bảo dưỡng xe điện cho trung tâm dịch vụ**

---

## 🧭 Giới thiệu

**EV Service Center Maintenance Management System** là một hệ thống quản lý bảo dưỡng xe điện toàn diện được phát triển bằng **Java Spring Boot 21**, hỗ trợ các trung tâm dịch vụ EV (Electric Vehicle) trong việc:
- Quản lý xe & khách hàng
- Theo dõi lịch bảo dưỡng
- Phân công kỹ thuật viên
- Quản lý phụ tùng, tài chính
- Thanh toán online (QR, VNPay, eWallet)

Hệ thống cung cấp các giao diện và API cho nhiều loại người dùng: **Customer**, **Staff**, **Technician**, và **Admin**.

---

## 👥 Vai trò hệ thống

| Role | Chức năng chính |
|------|------------------|
| **Customer** | Đặt lịch, xem nhắc nhở, thanh toán online, theo dõi lịch sử bảo dưỡng |
| **Staff** | Tiếp nhận và quản lý yêu cầu bảo dưỡng, lập lịch và theo dõi trạng thái dịch vụ |
| **Technician** | Thực hiện quy trình bảo dưỡng, cập nhật tiến độ và tình trạng xe |
| **Admin** | Quản lý hệ thống, nhân sự, phụ tùng, tài chính, báo cáo |

---

## ⚙️ Chức năng chính

### 1. Dành cho Khách hàng (Customer)
#### 🚗 Theo dõi xe & nhắc nhở
- Nhắc bảo dưỡng định kỳ theo km hoặc thời gian.
- Nhắc thanh toán/gia hạn gói dịch vụ.

#### 🧾 Đặt lịch dịch vụ
- Đặt lịch bảo dưỡng/sửa chữa trực tuyến.
- Chọn trung tâm & loại dịch vụ.
- Nhận thông báo trạng thái: **Pending → In Progress → Completed**.

#### 💳 Quản lý hồ sơ & chi phí
- Lưu lịch sử bảo dưỡng xe.
- Quản lý chi phí từng lần bảo dưỡng.
- Thanh toán online qua **QR Banking / VNPay / eWallet**.

---

### 2. Dành cho Trung tâm (Staff, Technician, Admin)
#### 👨‍🔧 Quản lý khách hàng & xe
- Hồ sơ khách hàng (Customer Profile) và xe (VIN, Model, History).
- Chat trực tuyến với khách hàng.

#### 📅 Quản lý lịch hẹn & dịch vụ
- Tiếp nhận & xác nhận yêu cầu.
- Lập lịch kỹ thuật viên.
- Quản lý phiếu tiếp nhận & checklist EV.

#### 🔧 Quản lý quy trình bảo dưỡng
- Theo dõi tiến độ từng xe.
- Cập nhật tình trạng xe thực tế.
- Ghi chú lỗi, hư hại, kết quả.

#### ⚙️ Quản lý phụ tùng
- Theo dõi lượng tồn kho.
- Kiểm soát tồn tối thiểu.
- **AI Gợi ý nhu cầu phụ tùng thay thế.**

#### 👩‍🏭 Quản lý nhân sự
- Phân công kỹ thuật viên theo ca/lịch.
- Theo dõi hiệu suất làm việc.
- Quản lý chứng chỉ chuyên môn.

#### 💰 Quản lý tài chính & báo cáo
- Quy trình: **Báo giá → Hóa đơn → Thanh toán (online/offline)**.
- Thống kê doanh thu, chi phí, lợi nhuận.
- Báo cáo dịch vụ phổ biến & xu hướng hỏng hóc EV.

---

## 🧩 Kiến trúc hệ thống

- **Backend:** Spring Boot 21 (Java 21)
- **Frontend:** ReactJS / NextJS (Tùy chọn)
- **Database:** MySQL / PostgreSQL
- **Authentication:** JWT + Role-based Access Control
- **Payment Integration:** VNPay + QR Payment
- **Notification:** JavaMailSender (Email Reminders)
- **Cloud Deploy:** AWS / Render / Railway (tùy chọn)

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
spring.datasource.username=sa
spring.datasource.password=12345
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
vnpay.tmncode=9E8978K7
vnpay.hashSecret=454U264QTTGJE2HTTPFFPAZ1GZE8Z3Y5
vnpay.payUrl=https://sandbox.vnpayment.vn/paymentv2/vpcpay.html
vnpay.returnUrl=https://your-ngrok-url.ngrok-free.app/api/payment/vnpay-return
vnpay.ipnUrl=https://your-ngrok-url.ngrok-free.app/api/payment/vnpay-ipn

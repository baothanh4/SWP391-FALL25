# ⚡ EV Service Center Maintenance Management System
---

## 🧭 Overview

**The EV Service Center Maintenance Management System is a complete backend platform built with Java Spring Boot 21, designed to help electric vehicle (EV) service centers manage operations efficiently.
- Streamline vehicle maintenance and scheduling
- Enable online booking and payments
- Track customer, staff, and technician activities
- Manage inventory and spare parts
- Provide financial and performance analytics

---

## 👥 Users Roles

| Role | Description                                                      |
|------|------------------------------------------------------------------|
| **Customer** | Book maintenance appointments, view reminders, make online payments, track maintenance history |
| **Staff** | Manage bookings, approve service requests, update maintenance progress   |
| **Technician** | Perform assigned maintenance tasks, log progress, update reports         |
| **Admin** | Manage users, vehicles, parts, and finances; generate system-wide reports       |

---

## ⚙️ Core Features

### 🔹 1. For Customers (Customer)
- Maintenance Reminders – Based on mileage or time intervals
- Online Booking – Receive confirmation via email
- Payment Options: **QR Banking / VNPay / eWallet**
- Service History – Track maintenance cost and details

### 🔹 2. For Service Center Staff (Staff, Technician, Admin)
- Manage customer profiles, vehicle records, and spare parts
- Assign technicians and monitor task progress
- Handle quotation, billing, and payments
- View financial statistics, profit trends, and EV issue reports
- Get AI-powered recommendations for part replacements

---

## 🧩 System Architecture

- **Backend:** Java 21 + Spring Boot 3.3
- **Database:** SQL Server
- **Auth:** JWT + Role-based Access Control
- **Payment:** VNPay + VietQR
- **Mail:** JavaMailSender
- **Deploy:** AWS

---

## 🧱 Cấu trúc thư mục backend
src/<br>
├── main/<br>
│ ├── java/<br>
│ │ └── com/example/SWP391_FALL25/<br>
│ │ ├── Config/<br> 
│ │ ├── Controller/<br>
│ │ ├── DTO/Auth/<br>
│ │ ├── Entity/<br>
│ │ ├── Enum/<br>
│ │ ├── ExceptionHandler/<br>
│ │ ├── Repository/<br>
│ │ ├── Service/<br>
│ │ ├── Utility/<br>
│ │ └── Swp391Fall25Application.java <br>
│ └── resources/<br>
│ ├── static/ <br>
│ ├── templates/ <br>
│ └── application.properties <br>
└── test/<br>


## ⚙️ Điều kiện tiên quyết

| Tool              | Recommended Version | Purpose                 |
|-------------------|---------------------|-------------------------|
| **Java JDK**      | 21+                 | Core language           |
| **Spring Boot**   | 3.3+                | Framework backend       |
| **Maven**         | 3.9+                | Dependencies management |
| **SQL Server**    | 8.0+                | Relational database     |
| **Git**           | Mới nhất            | Source code management  |
| **VNPay Sandbox** | -                   | Payment testing         |
| **IDE**           | IntelliJ IDEA       | Deployment & debug      |

---

## ⚙️ Configure in`application.properties`

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

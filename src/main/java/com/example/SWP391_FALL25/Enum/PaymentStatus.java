package com.example.SWP391_FALL25.Enum;

public enum PaymentStatus {
    QUOTATION,         // Báo giá (chưa duyệt)
    PENDING,           // Chờ thanh toán (đã duyệt)
    PROCESSING,        // Đang xử lý thanh toán
    COMPLETED,         // Đã thanh toán
    FAILED,            // Thanh toán thất bại
    REFUNDED,
    CANCELED,
    PAID
}

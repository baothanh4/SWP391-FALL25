package com.example.SWP391_FALL25.Utility;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class VNPayUtils {
    public static Map<String, String> getVNPayResponseParams(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> en = request.getParameterNames(); en.hasMoreElements(); ) {
            String fieldName = en.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }
        return fields;
    }

    public static boolean verifySignature(Map<String, String> fields, String receivedHash, String secretKey) {
        // 1️⃣ Lọc chỉ lấy các key bắt đầu bằng "vnp_"
        SortedMap<String, String> sorted = new TreeMap<>();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            if (entry.getKey().startsWith("vnp_")) {
                sorted.put(entry.getKey(), entry.getValue());
            }
        }

        // 2️⃣ Xóa 2 trường không dùng để hash
        sorted.remove("vnp_SecureHash");
        sorted.remove("vnp_SecureHashType");

        // 3️⃣ Ghép chuỗi đúng chuẩn VNPay
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                sb.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII))
                        .append("&");
            }
        }
        sb.deleteCharAt(sb.length() - 1); // bỏ dấu & cuối

        String signData = sb.toString();

        // 4️⃣ Tạo chữ ký
        String calculatedHash = HmacUtil.hmacSHA512(secretKey, signData);

        System.out.println("Data to hash: " + signData);
        System.out.println("Calculated hash: " + calculatedHash);
        System.out.println("Received hash: " + receivedHash);

        return calculatedHash.equalsIgnoreCase(receivedHash);
    }
}

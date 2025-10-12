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
        SortedMap<String, String> sorted = new TreeMap<>(fields);
        sorted.remove("vnp_SecureHash"); // loại bỏ hash khỏi dữ liệu
        fields.remove("vnp_SecureHashType");
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : sorted.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        sb.setLength(sb.length() - 1); // remove last &

        String signData = sb.toString();
        String calculatedHash = HmacUtil.hmacSHA512(secretKey, signData);

        System.out.println("Data to hash: " + signData);
        System.out.println("Calculated hash: " + calculatedHash);
        System.out.println("Received hash: " + receivedHash);

        return calculatedHash.equalsIgnoreCase(receivedHash);
    }


}

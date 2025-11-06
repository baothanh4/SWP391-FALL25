package com.example.SWP391_FALL25.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class QRServiceImpl implements QRService{
    @Value("${bank.id}")
    private String bankId;

    @Value("${bank.accountNo}")
    private String accountNo;

    @Value("${bank.accountName}")
    private String accountName;

    @Value("${bank.defaultContent}")
    private String defaultContent;

    @Override
    public String generateBankQr(Double amount, String content){
        double finalAmount=(amount!=null)?amount:0;
        String finalContent=(content!=null)?content:"";

        String encodedContent=finalContent.replace(" ","%20");
        String encodedName=accountName.replace(" ","%20");

        return String.format(
                "https://img.vietqr.io/image/%s-%s-compact2.png?amount=%.0f&addInfo=%s&accountName=%s",
                bankId, accountNo, finalAmount, encodedContent, encodedName
        );
    }
}

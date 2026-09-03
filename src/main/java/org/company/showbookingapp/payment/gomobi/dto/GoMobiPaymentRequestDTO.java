package org.company.showbookingapp.payment.gomobi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoMobiPaymentRequestDTO {

    private String amount;
    private String redirectUrl;
    private String sellerOrderNo;
    private String bankType;
    private String mid;
    private String buyerName;
    private String tid;
    private String merchantName;
    private String bank;
    private String service;
    private String email;
    private String subMID;
    private String checkSum;
}
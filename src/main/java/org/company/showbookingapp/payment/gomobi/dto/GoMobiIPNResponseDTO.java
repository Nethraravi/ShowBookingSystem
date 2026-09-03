package org.company.showbookingapp.payment.gomobi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoMobiIPNResponseDTO {

    private String fpx_fpxTxnId;
    private String fpx_sellerExOrderNo;
    private String fpx_sellerOrderNo;

    private String fpx_txnCurrency;
    private String fpx_txnAmount;

    private String fpx_buyerName;
    private String fpx_buyerBankId;

    private String fpx_debitAuthCode;
    private String fpx_debitAuthCodeString;

    private String fpx_creditAuthCode;
    private String fpx_creditAuthCodeString;

    private String bankName;
    private String tid;

    private String date;
    private String time;

    private String service;
    private String subMID;

    private String fpx_checkSum;
}
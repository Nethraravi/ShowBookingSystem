package org.company.showbookingapp.payment.gomobi.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoMobiStatusResponseDTO {

    private String responseCode;
    private String responseMessage;
    private String responseDescription;
    private ResponseData responseData;

    @Getter
    @Setter
    public static class ResponseData {

        private TransactionData forSettlement;
    }

    @Getter
    @Setter
    public static class TransactionData {

        private String trxId;
        private String mid;
        private String tid;
        private String status;
        private String date;
        private String time;
        private String stan;
        private String rrn;
        private String latitude;
        private String longitude;
        private String amount;
        private String invoiceId;
        private String txnId;
        private String txnType;
        private String hostType;
        private String aidResponse;
    }
}
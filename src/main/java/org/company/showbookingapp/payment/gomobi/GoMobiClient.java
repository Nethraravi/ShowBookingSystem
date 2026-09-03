package org.company.showbookingapp.payment.gomobi;

import lombok.Getter;
import org.company.showbookingapp.payment.gomobi.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Getter
@Component
public class GoMobiClient {

    private final RestClient restClient;

    @Value("${gomobi.submid}")
    private String subMID;

    @Value("${gomobi.merchant-name}")
    private String merchantName;

    @Value("${gomobi.service}")
    private String service;

    @Value("${gomobi.bank-type}")
    private String bankType;

    @Value("${gomobi.payment-url}")
    private String paymentUrl;

    @Value("${gomobi.mid}")
    private String mid;

    @Value("${gomobi.tid}")
    private String tid;

    @Value("${gomobi.moto-api-key}")
    private String motoApiKey;

    @Value("${gomobi.login-id}")
    private String loginId;

    @Value("${gomobi.status-url}")
    private String statusUrl;

    public GoMobiClient(@Value("${gomobi.base-url}") String baseUrl)
    {
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public ResponseEntity<GoMobiBankResponseDTO> getAvailableBanks() {

        GoMobiBankRequestDTO request = new GoMobiBankRequestDTO("FULL_LIST");

        return restClient.post()
                .body(request)
                .retrieve()
                .toEntity(GoMobiBankResponseDTO.class);
    }

    public ResponseEntity<String> initiateDeposit(GoMobiPaymentRequestDTO request) {

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("amount", request.getAmount());
        formData.add("redirectUrl", request.getRedirectUrl());
        formData.add("sellerOrderNo", request.getSellerOrderNo());
        formData.add("bankType", request.getBankType());
        formData.add("mid", request.getMid());
        formData.add("buyerName", request.getBuyerName());
        formData.add("tid", request.getTid());
        formData.add("merchantName", request.getMerchantName());
        formData.add("bank", request.getBank());
        formData.add("service", request.getService());
        formData.add("email", request.getEmail());
        formData.add("subMID", request.getSubMID());
        formData.add("checkSum", request.getCheckSum());

        return restClient.post()
                .uri(paymentUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(formData)
                .exchange((requestMessage, response) -> {

                    System.out.println("GoMobi HTTP Status: "
                            + response.getStatusCode());

                    String body = new String(
                            response.getBody().readAllBytes(),
                            java.nio.charset.StandardCharsets.UTF_8
                    );

                    System.out.println("GoMobi Response Body:");
                    System.out.println(body);

                    return ResponseEntity
                            .status(response.getStatusCode())
                            .body(body);
                });
    }

    public ResponseEntity<GoMobiStatusResponseDTO> checkPaymentStatus(String searchKey) {

        GoMobiStatusRequestDTO request = new GoMobiStatusRequestDTO(
                "EXTERNAL_TXN_HISTORY",
                motoApiKey,
                loginId,
                "FPX",
                searchKey
        );

        System.out.println("GoMobi Status Request:");
        System.out.println("service = " + request.getService());
        System.out.println("motoApiKey = " + request.getMotoApiKey());
        System.out.println("loginId = " + request.getLoginId());
        System.out.println("trxType = " + request.getTrxType());
        System.out.println("searchKey = " + request.getSearchKey());

        ResponseEntity<GoMobiStatusResponseDTO> response = restClient.post()
                .uri(statusUrl)
                .body(request)
                .retrieve()
                .toEntity(GoMobiStatusResponseDTO.class);

        System.out.println("GoMobi Status Response: " + response.getBody());

        return response;
    }

    public String generateCheckSum(
            String amount,
            String sellerOrderNo,
            String subMID) {

        String input = amount + "|" + sellerOrderNo + "|" + subMID;

        return GoMobiChecksumUtil.encryptPayload(
                input,
                mid,
                tid
        );
    }
}
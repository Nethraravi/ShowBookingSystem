package org.company.showbookingapp.payment.gomobi;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GoMobiProperties {
    @Value("${gomobi.base-url}")
    private String baseUrl;

    @Value("${gomobi.payment-url}")
    private String paymentUrl;

    @Value("${gomobi.mid}")
    private String mid;

    @Value("${gomobi.tid}")
    private String tid;

    @Value("${gomobi.merchant-name}")
    private String merchantName;

    @Value("${gomobi.service}")
    private String service;

    @Value("${gomobi.bank-type}")
    private String bankType;

    @Value("${gomobi.sub-mid}")
    private String subMid;

}
package org.company.showbookingapp.payment.gomobi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties
public class GoMobiBankResponseDTO {

    private GoMobiBankTypeDataDTO responseDataBT;

    private String responseCode;
    private String responseMessage;
    private String responseDescription;
    private GoMobiBankDataDTO responseDataB2C;
    private GoMobiBankDataDTO responseDataB2B;
}
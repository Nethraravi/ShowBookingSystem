package org.company.showbookingapp.payment.gomobi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoMobiBankDTO {

    @JsonProperty("ddbankCode")
    private String ddBankCode;

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("BankCode")
    private String bankCode;

    @JsonProperty("BankName")
    private String bankName;

    @JsonProperty("BankDisplayName")
    private String bankDisplayName;

    @JsonProperty("Logo")
    private String logo;

    @JsonProperty("LastUpdatedOn")
    private String lastUpdatedOn;

    @JsonProperty("Active")
    private String active;
}
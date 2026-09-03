package org.company.showbookingapp.payment.gomobi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GoMobiBankTypeDTO {

    @JsonProperty("ID")
    private Integer id;

    @JsonProperty("CreatedOn")
    private Long createdOn;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Active")
    private String active;

    @JsonProperty("BankTypeCode")
    private String bankTypeCode;
}
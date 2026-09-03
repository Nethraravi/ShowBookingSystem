package org.company.showbookingapp.payment.gomobi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoMobiStatusRequestDTO {

    private String service;
    private String motoApiKey;
    private String loginId;
    private String trxType;
    private String searchKey;
}
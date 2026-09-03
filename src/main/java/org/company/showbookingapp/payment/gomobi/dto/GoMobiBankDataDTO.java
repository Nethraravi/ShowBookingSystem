package org.company.showbookingapp.payment.gomobi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class GoMobiBankDataDTO {

    private List<GoMobiBankDTO> bankList;
}
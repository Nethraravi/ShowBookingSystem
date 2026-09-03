package org.company.showbookingapp.payment.gomobi;

import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.payment.gomobi.dto.GoMobiBankResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/gomobi")
@RequiredArgsConstructor
public class GoMobiTestController {

    private final GoMobiClient goMobiClient;
    private final GoMobiChecksumService goMobiChecksumService;

    @GetMapping("/banks")
    public ResponseEntity<GoMobiBankResponseDTO> getBanks() {
        return goMobiClient.getAvailableBanks();
    }

    @GetMapping("/checksum")
    public ResponseEntity<String> generateCheckSum() {

        String checksum = goMobiClient.generateCheckSum(
                "5.00",
                "INV001",
                "201100000012450"
        );

        return ResponseEntity.ok(checksum);
    }
}

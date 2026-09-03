package org.company.showbookingapp.show;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ShowResponseDTO {

    private Long id;
    private Long movieId;
    private String movieTitle;
    private Long screenId;
    private String screenName;
    private Long venueId;
    private String venueName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime screenAvailableTime;
}
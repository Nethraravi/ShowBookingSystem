package org.company.showbookingapp.show;

import org.springframework.stereotype.Component;

@Component
public class ShowMapper {

    public ShowResponseDTO toResponse(Show show) {

        ShowResponseDTO response = new ShowResponseDTO();

        response.setId(show.getId());

        response.setMovieId(show.getMovie().getId());
        response.setMovieTitle(show.getMovie().getTitle());

        response.setScreenId(show.getScreen().getId());
        response.setScreenName(show.getScreen().getName());

        response.setVenueId(show.getScreen().getVenue().getId());
        response.setVenueName(show.getScreen().getVenue().getName());

        response.setStartTime(show.getStartTime());
        response.setEndTime(show.getEndTime());
        response.setScreenAvailableTime(show.getScreenAvailableTime());

        return response;
    }
}
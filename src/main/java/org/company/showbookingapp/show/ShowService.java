package org.company.showbookingapp.show;

import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.exception.ResourceNotFoundException;
import org.company.showbookingapp.movie.entity.Movie;
import org.company.showbookingapp.movie.repository.MovieRepository;
import org.company.showbookingapp.screen.entity.Screen;
import org.company.showbookingapp.screen.repository.ScreenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowService {
    private final ShowRepository showRepository;
    private final MovieRepository movieRepository;
    private final ScreenRepository screenRepository;
    private final ShowMapper showMapper;

    public ShowResponseDTO createShow(CreateShowRequestDTO request) {

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() -> new RuntimeException("Screen not found"));

        //calculating show endtime
        LocalDateTime endTime = request.getStartTime().plusMinutes(movie.getDuration());

        //adding interval time
        LocalDateTime screenFreeTime = endTime.plusMinutes(screen.getIntervalDuration());

        //calculating screen availability
        LocalDateTime screenAvailableTime = screenFreeTime.plusMinutes(screen.getBufferDuration());

        // Check for overlapping show
        boolean overlappingShow = showRepository.existsOverlappingShow(request.getScreenId(),request.getStartTime(),screenAvailableTime);

        if (overlappingShow) {
            throw new IllegalArgumentException("Screen already has a show during this time");
        }

        // Create and save Show
        Show show = new Show();

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(request.getStartTime());
        show.setEndTime(endTime);
        show.setScreenAvailableTime(screenAvailableTime);

        Show savedShow = showRepository.save(show);
        return showMapper.toResponse(savedShow);
    }


    public List<ShowResponseDTO> getAllShows() {

        return showRepository.findAll()
                .stream()
                .map(showMapper::toResponse)
                .toList();
    }

    public ShowResponseDTO getShowById(Long id) {

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        return showMapper.toResponse(show);
    }

    public void deleteShow(Long id) {

        Show show = showRepository.findById(id)
                .orElseThrow(() ->new ResourceNotFoundException("Show not found"));

        showRepository.delete(show);
    }

    public ShowResponseDTO updateShow(Long id, UpdateShowRequestDTO request) {

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found"));

        Movie movie = movieRepository.findById(request.getMovieId())
                .orElseThrow(() ->new ResourceNotFoundException("Movie not found"));

        Screen screen = screenRepository.findById(request.getScreenId())
                .orElseThrow(() ->new ResourceNotFoundException("Screen not found"));

        LocalDateTime endTime =request.getStartTime().plusMinutes(movie.getDuration());

        LocalDateTime screenFreeTime =endTime.plusMinutes(screen.getIntervalDuration());

        LocalDateTime screenAvailableTime =screenFreeTime.plusMinutes(screen.getBufferDuration());

        boolean overlappingShow =
                showRepository.existsOverlappingShowExcludingId(
                        screen.getId(),
                        request.getStartTime(),
                        screenAvailableTime,
                        id
                );

        if (overlappingShow) {
            throw new IllegalArgumentException("Screen already has a show during this time");
        }

        show.setMovie(movie);
        show.setScreen(screen);
        show.setStartTime(request.getStartTime());
        show.setEndTime(endTime);
        show.setScreenAvailableTime(screenAvailableTime);

        Show updatedShow = showRepository.save(show);

        return showMapper.toResponse(updatedShow);
    }
}

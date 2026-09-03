package org.company.showbookingapp.movie.service;

import lombok.RequiredArgsConstructor;
import org.company.showbookingapp.exception.DuplicateResourceException;
import org.company.showbookingapp.exception.ResourceNotFoundException;
import org.company.showbookingapp.movie.entity.Movie;
import org.company.showbookingapp.movie.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public Movie createMovie(Movie movie)
    {
        if(movieRepository.existsByTitleAndReleaseDate(movie.getTitle(), movie.getReleaseDate()))
        {
            throw new DuplicateResourceException("Movie with the same title and release date already exists");
        }
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies()
    {
        return movieRepository.findAll();
    }

    public Movie updateMovie(Long id, Movie movie)
    {
        Movie existingMovie = movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: "+id));

        if(movieRepository.existsByTitleAndReleaseDateAndIdNot(movie.getTitle(),movie.getReleaseDate(),id))
        {
            throw new DuplicateResourceException("Movie with the same title and release date cannot co-exist.");
        }

        existingMovie.setTitle(movie.getTitle());
        existingMovie.setDescription(movie.getDescription());
        existingMovie.setDuration(movie.getDuration());
        existingMovie.setLanguage(movie.getLanguage());
        existingMovie.setGenre(movie.getGenre());
        existingMovie.setReleaseDate(movie.getReleaseDate());
        existingMovie.setStatus(movie.getStatus());

        return movieRepository.save(existingMovie);
    }

    public void deleteMovie(Long id)
    {
        Movie movie = movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: "+id));
        movieRepository.delete(movie);
    }
}

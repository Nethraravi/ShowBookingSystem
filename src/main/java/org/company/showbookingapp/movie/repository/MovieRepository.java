package org.company.showbookingapp.movie.repository;

import org.company.showbookingapp.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    boolean existsByTitleAndReleaseDate(String title, LocalDate releaseDate);

    boolean existsByTitleAndReleaseDateAndIdNot(String title, LocalDate releaseDate, Long movieId);
}

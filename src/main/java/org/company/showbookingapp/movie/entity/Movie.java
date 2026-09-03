package org.company.showbookingapp.movie.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.showbookingapp.movie.enums.MovieStatus;

import java.time.LocalDate;

@Entity
@Table(name="movies")
@Getter
@Setter
@NoArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Movie title is required")
    private String title;

    @NotBlank(message = "Movie description is required")
    private String description;

    @NotNull(message = "Movie duration is required")
    @Min(value = 60, message = "Movie duration must be at least 1hr")
    private Integer duration;

    @NotBlank(message = "Movie language is required")
    private String language;

    @NotBlank(message = "Movie genre is required")
    private String genre;

    @NotNull(message = "Movie release date is required")
    private LocalDate releaseDate;

    @NotNull(message = "Movie status is required")
    @Enumerated(EnumType.STRING)
    private MovieStatus status;
}

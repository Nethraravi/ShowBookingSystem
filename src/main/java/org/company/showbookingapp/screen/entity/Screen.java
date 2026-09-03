package org.company.showbookingapp.screen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.company.showbookingapp.venue.entity.Venue;

@Entity
@Table(name = "screens")
@Getter
@Setter
@NoArgsConstructor
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Screen name is required")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private Venue venue;

    @NotNull(message = "Interval duration is required")
    @Min(value = 0, message = "Interval duration cannot be negative")
    private Integer intervalDuration;

    @NotNull(message = "Buffer duration is required")
    @Min(value = 0, message = "Buffer duration cannot be negative")
    private Integer bufferDuration;
}

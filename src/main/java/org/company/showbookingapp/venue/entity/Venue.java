package org.company.showbookingapp.venue.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.company.showbookingapp.venue.enums.VenueStatus;

@Entity
@Table(name = "venues")
@Getter
@Setter
public class Venue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Venue name is required")
    private String name;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "City is required")
    private String city;
    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    private VenueStatus status;

}

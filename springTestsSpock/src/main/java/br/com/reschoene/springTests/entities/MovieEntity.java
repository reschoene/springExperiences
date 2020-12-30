package br.com.reschoene.springTests.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter @Setter
@Table(name="Movies")
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "The movie title cannot be empty")
    private String title;
    private Date releaseDate;
}

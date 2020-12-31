package br.com.reschoene.springtests.dto;

import br.com.reschoene.springtests.entities.MovieEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class MovieDTO {
    private long id;

    @NotEmpty(message = "The movie title cannot be empty")
    private String title;

    private Date releaseDate;

    public static MovieDTO fromEntity(MovieEntity movieEntity){
        return new ModelMapper().map(movieEntity, MovieDTO.class);
    }

    public MovieEntity toEntity(){
        return new ModelMapper().map(this, MovieEntity.class);
    }
}

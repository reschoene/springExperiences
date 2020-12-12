package br.com.reschoene.JPATests.enitities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Estante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String codigoEstante;

    @OneToMany(mappedBy = "estante")
    private List<Livro> livros = new ArrayList<Livro>();
}

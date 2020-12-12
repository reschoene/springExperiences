package br.com.reschoene.JPATests.enitities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/*
Optei por nao usar @Data pois o mesmo tem a implementacao de ToString que
utiliza todos os membros e como meu Livro tem um atributo Estante, entidade a qual
tambem referia livros, pode dar StackOverFlow devido ao ToString das duas entidades ficar em looping
No meu caso em questao, nao estou mais precisando usar o ToString de ambos, mas caso for necessario, do
jeito que esta nao tem este risco mais
 */

@Entity
@Getter @Setter @NoArgsConstructor
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String titulo;

    @ManyToOne
    @JoinColumn(name="idEstante")
    private Estante estante;

    @OneToOne
    private Autor autor;
}

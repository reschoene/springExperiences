package br.com.reschoene.JPATests.services;

import br.com.reschoene.JPATests.enitities.Autor;
import br.com.reschoene.JPATests.enitities.Estante;
import br.com.reschoene.JPATests.enitities.Livro;
import br.com.reschoene.JPATests.repositories.AutorRepository;
import br.com.reschoene.JPATests.repositories.EstanteRepository;
import br.com.reschoene.JPATests.repositories.LivroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class DbService {
    private final AutorRepository autorRepository;
    private final LivroRepository livroRepository;
    private final EstanteRepository estanteRepository;

    @Transactional
    public void populateDb(){
        var autorRenato = new Autor();
        autorRenato.setNome("Renato");

        var autoraVerona = new Autor();
        autoraVerona.setNome("Verona");

        var livro1 = new Livro();
        livro1.setTitulo("Narnia");
        livro1.setAutor(autorRenato);

        var livro2 = new Livro();
        livro2.setTitulo("Senhor dos Anéis");
        livro2.setAutor(autoraVerona);

        var estante = new Estante();
        estante.setCodigoEstante("codABCCX");
        estante.getLivros().add(livro1);
        estante.getLivros().add(livro2);

        livro1.setEstante(estante);
        livro2.setEstante(estante);

        autorRepository.save(autorRenato);
        autorRepository.save(autoraVerona);

        estanteRepository.save(estante);

        livroRepository.save(livro1);
        livroRepository.save(livro2);
    }

    @Transactional
    public void listDb(){
        System.out.println("");
        System.out.println("Inicio da Listagem");
        System.out.println("");

        var estantes = estanteRepository.findAll();
        for (var e : estantes){
            System.out.println("Estante " + e.getId() + " - " + e.getCodigoEstante() + " ---------------------");
            for (var l : e.getLivros()){
                System.out.println("  -> Livro " + l.getTitulo() + " - " + "Autor(a) " + l.getAutor().getNome());
            }
            System.out.println("---------------------------------------------");
            System.out.println("");
        }

        System.out.println("");
        System.out.println("Fim da listagem");
        System.out.println("");
    }
}

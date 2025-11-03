package ma.emsi.tpgraphql;

import ma.emsi.tpgraphql.enums.Genre;
import ma.emsi.tpgraphql.model.Centre;
import ma.emsi.tpgraphql.model.Etudiant;
import ma.emsi.tpgraphql.repositories.CentreRepository;
import ma.emsi.tpgraphql.repositories.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TpGraphQlApplication implements CommandLineRunner {
    @Autowired
    EtudiantRepository etudiantRepository;
    @Autowired
    CentreRepository centreRepository;

    public static void main(String[] args) {
        SpringApplication.run(TpGraphQlApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Centre centre1 = Centre.builder()
                .nom("Centre 1")
                .adresse("Maarif")
                .build();
        centreRepository.save(centre1);
        Centre centre2 = Centre.builder()
                .nom("Centre 2")
                .adresse("Roudani")
                .build();
        centreRepository.save(centre2);
        Etudiant etudiant1 = Etudiant.builder()
                .nom("Elyamani")
                .prenom("Hajar")
                .genre(Genre.FEMME)
                .centre(centre1)
                .build();
        etudiantRepository.save(etudiant1);
        Etudiant etudiant2 = Etudiant.builder()
                .nom("Hachem")
                .prenom("Mehdi")
                .genre(Genre.HOMME)
                .centre(centre1)
                .build();
        etudiantRepository.save(etudiant2);
        Etudiant etudiant3 = Etudiant.builder()
                .nom("Elyamani")
                .prenom("Hind")
                .genre(Genre.FEMME)
                .centre(centre2)
                .build();
        etudiantRepository.save(etudiant3);
    }



}

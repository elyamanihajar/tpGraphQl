package ma.emsi.tpgraphql.controllers;

import ma.emsi.tpgraphql.dtos.EtudiantDTO;
import ma.emsi.tpgraphql.model.Centre;
import ma.emsi.tpgraphql.model.Etudiant;
import ma.emsi.tpgraphql.repositories.CentreRepository;
import ma.emsi.tpgraphql.repositories.EtudiantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.List;

@Controller
public class EtudiantController {
    @Autowired
    EtudiantRepository etudiantRepository;
    @Autowired
    private CentreRepository centreRepository;

    final Sinks.Many<Etudiant> sink = Sinks.many().multicast().onBackpressureBuffer();
    final Sinks.Many<String> sink2 = Sinks.many().multicast().onBackpressureBuffer();

    //    ------------Etudiants----------------------
    @QueryMapping
    public List<Etudiant> findAllEtudiants() {
        return etudiantRepository.findAll();
    }

    @QueryMapping
    public Etudiant findEtudiantById(@Argument Long id) {
        return etudiantRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("Etudiant with id %d not found", id))
        );
    }

    @MutationMapping
    public Etudiant addEtudiant(@Argument EtudiantDTO etudiantDto) {
        if (centreRepository.findById(etudiantDto.centreId()).isPresent()) {
            Centre centre = centreRepository.findById(etudiantDto.centreId()).get();
            Etudiant etudiant = new Etudiant();
            etudiant.setNom(etudiantDto.nom());
            etudiant.setPrenom(etudiantDto.prenom());
            etudiant.setGenre(etudiantDto.genre());
            etudiant.setCentre(centre);

            etudiantRepository.save(etudiant);
            sink.tryEmitNext(etudiant);
            return etudiant;
        }
        return null;

    }

    @MutationMapping
    public Etudiant updateEtudiant(@Argument Long id, @Argument EtudiantDTO etudiant) {
        Centre centre = centreRepository.findById(etudiant.centreId()).orElse(null);
        Etudiant et = null;
        if (etudiantRepository.findById(id).isPresent()) {
            et = etudiantRepository.findById(id).get();
            et.setNom(etudiant.nom());
            et.setPrenom(etudiant.prenom());
            et.setGenre(etudiant.genre());
            et.setCentre(centre);
            return etudiantRepository.save(et);
        }
        return et;
    }

    @MutationMapping
    public String deleteEtudiant(@Argument Long id) {
        if (etudiantRepository.findById(id).isPresent()) {
            etudiantRepository.deleteById(id);
            String msg="Etudiant with id " + id + " was deleted";
            sink2.tryEmitNext(msg);
            return String.format("L'étudiant %s bien supprimé ", id);
        }
        return String.format("L'étudiant %s n'exite pas ", id);
    }

    //    ------------Centres----------------------
    @QueryMapping
    public List<Centre> centres() {
        return centreRepository.findAll();
    }

    @QueryMapping
    public Centre getCentreById(@Argument Long id) {
        return centreRepository.findById(id).orElseThrow(
                () -> new RuntimeException(String.format("Centre %s non trouvé ", id))
        );
    }

    //   -------------Flux Sink----------------
    @SubscriptionMapping
    public Flux<Etudiant> etudiantAdded() {
        return sink.asFlux();
    }

    @SubscriptionMapping
    public  Flux<String> etudiantDeleted() {
        return sink2.asFlux();
    }
}

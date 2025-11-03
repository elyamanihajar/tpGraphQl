package ma.emsi.tpgraphql.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.emsi.tpgraphql.enums.Genre;
import org.antlr.v4.runtime.misc.NotNull;

@Entity
@Data @NoArgsConstructor
@AllArgsConstructor @Builder @Table(name="etudiants")
public class Etudiant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nom_etudiant", nullable=false)
    private String nom;
    @Column(name="prenom_etudiant")
    private String prenom;
    @Enumerated(EnumType.STRING)
    private Genre genre;

    @ManyToOne
    @NotNull
    @JoinColumn(name="centre_id")
    Centre centre;
}

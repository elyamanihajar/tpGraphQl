package ma.emsi.tpgraphql.dtos;

import ma.emsi.tpgraphql.enums.Genre;

public record EtudiantDTO(String nom, String prenom, Genre genre,Long centreId) {

}

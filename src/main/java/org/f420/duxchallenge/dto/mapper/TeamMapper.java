package org.f420.duxchallenge.dto.mapper;

import org.f420.duxchallenge.dto.TeamDTO;
import org.f420.duxchallenge.model.Team;

public class TeamMapper {

    public static TeamDTO toTeamDTO(Team source) {
        return TeamDTO.builder()
                .id(source.getId())
                .nombre(source.getNombre())
                .pais(source.getPais())
                .liga(source.getLiga())
                .build();
    }
}

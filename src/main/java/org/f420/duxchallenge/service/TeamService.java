package org.f420.duxchallenge.service;

import lombok.NonNull;
import org.f420.duxchallenge.dao.TeamDAO;
import org.f420.duxchallenge.dto.PaginatedResponse;
import org.f420.duxchallenge.dto.TeamDTO;
import org.f420.duxchallenge.dto.mapper.TeamMapper;
import org.f420.duxchallenge.exceptions.ApiException;
import org.f420.duxchallenge.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.f420.duxchallenge.dao.custom.TeamSpecification.buildSpecificationFromFilter;
import static org.f420.duxchallenge.enums.ErrorMessage.ENTITY_NOT_FOUND;

@Service
public class TeamService {
    private final TeamDAO teamDAO;

    public TeamService(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }


    @Transactional
    public TeamDTO save(TeamDTO teamDTO) {

        Team newTeam = Team.builder()
                .nombre(teamDTO.getNombre())
                .pais(teamDTO.getPais())
                .liga(teamDTO.getLiga())
                .deleted(false)
                .build();

        return TeamMapper.toTeamDTO(teamDAO.save(newTeam));
    }

    @Transactional
    public void update(Long id, TeamDTO teamDTO) {
        if (id == null) {
            return;
        }
        Team team = teamDAO.findById(id)
                .orElseThrow(
                        () -> new ApiException(ENTITY_NOT_FOUND, id)
                );

        team.setNombre(Optional.ofNullable(teamDTO.getNombre()).orElse(team.getNombre()));
        team.setPais(Optional.ofNullable(teamDTO.getPais()).orElse(team.getPais()));
        team.setLiga(Optional.ofNullable(teamDTO.getLiga()).orElse(team.getLiga()));

    }

    @Transactional(readOnly = true)
    public PaginatedResponse<TeamDTO> findAll(Pageable pageable) {
        Page<Team> response = teamDAO.findAllByDeletedIsFalse(pageable);
        List<TeamDTO> responseDTO = response.getContent().stream().map(TeamMapper::toTeamDTO).toList();
        return new PaginatedResponse<>(response, responseDTO);
    }

    @Transactional(readOnly = true)
    public TeamDTO findById(@NonNull Long id) {
        Optional<Team> team = teamDAO.findById(id);
        return team.map(TeamMapper::toTeamDTO).orElseThrow(
                () -> new ApiException(ENTITY_NOT_FOUND, id)
        );
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<TeamDTO> searchBy(TeamDTO filter, Pageable pageable) {
        Specification<Team> specification = buildSpecificationFromFilter(filter);
        Page<Team> response = teamDAO.findAll(specification, pageable);
        List<TeamDTO> responseDTO = response.getContent().stream().map(TeamMapper::toTeamDTO).toList();
        return new PaginatedResponse<>(response, responseDTO);
    }
}

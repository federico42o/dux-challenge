package org.f420.duxchallenge.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.f420.duxchallenge.dao.TeamDAO;
import org.f420.duxchallenge.dto.PaginatedResponse;
import org.f420.duxchallenge.dto.TeamDTO;
import org.f420.duxchallenge.dto.mapper.TeamMapper;
import org.f420.duxchallenge.exceptions.ApiException;
import org.f420.duxchallenge.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static org.f420.duxchallenge.dao.custom.TeamSpecification.buildSpecificationFromFilter;
import static org.f420.duxchallenge.enums.ErrorMessage.*;

@Service
@Slf4j
public class TeamService {
    private final TeamDAO teamDAO;

    public TeamService(TeamDAO teamDAO) {
        this.teamDAO = teamDAO;
    }


    @Transactional
    public TeamDTO save(TeamDTO teamDTO) {
        Optional<Team> existing = teamDAO.findByNombreAndLigaAndPais(
                teamDTO.getNombre(),
                teamDTO.getLiga(),
                teamDTO.getPais());
        log.info("newTeam: {}, existing team: {}",teamDTO, existing);
        if (existing.isPresent()) {
            Team existingTeam = existing.get();
            if (existingTeam.getDeleted()) {
                existingTeam.setDeleted(false);
                return TeamMapper.toTeamDTO(teamDAO.save(existingTeam));
            } else {
                throw new ApiException(ENTITY_ALREADY_EXISTS);
            }
        }

        Team newTeam = Team.builder()
                .nombre(teamDTO.getNombre())
                .pais(teamDTO.getPais())
                .liga(teamDTO.getLiga())
                .deleted(false)
                .build();

        return TeamMapper.toTeamDTO(teamDAO.save(newTeam));
    }

    @Transactional
    public TeamDTO update(Long id, TeamDTO teamDTO) {
        if (id == null) {
            throw new ApiException(ID_REQUIRED);
        }
        Team team = teamDAO.findByIdAndDeletedIsFalse(id)
                .orElseThrow(
                        () -> new ApiException(ENTITY_NOT_FOUND, id)
                );

        team.setNombre(Optional.ofNullable(teamDTO.getNombre()).orElse(team.getNombre()));
        team.setPais(Optional.ofNullable(teamDTO.getPais()).orElse(team.getPais()));
        team.setLiga(Optional.ofNullable(teamDTO.getLiga()).orElse(team.getLiga()));
        return TeamMapper.toTeamDTO(teamDAO.save(team));
    }

    @Transactional(readOnly = true)
    public PaginatedResponse<TeamDTO> findAll(Pageable pageable) {
        Page<Team> response = teamDAO.findAllByDeletedIsFalse(pageable);
        List<TeamDTO> responseDTO = response.getContent().stream().map(TeamMapper::toTeamDTO).toList();
        return new PaginatedResponse<>(response, responseDTO);
    }

    @Transactional(readOnly = true)
    public TeamDTO findById(@NonNull Long id) {
        Optional<Team> team = teamDAO.findByIdAndDeletedIsFalse(id);
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

    @Transactional
    public void delete(Long id) {
        if (id == null) {
            return;
        }
        Team team = teamDAO.findByIdAndDeletedIsFalse(id)
                .orElseThrow(
                        () -> new ApiException(ENTITY_NOT_FOUND, id)
                );
        team.setDeleted(true);
        teamDAO.save(team);
    }
}

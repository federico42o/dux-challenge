package org.f420.duxchallenge.service;

import lombok.extern.slf4j.Slf4j;
import org.f420.duxchallenge.dao.TeamDAO;
import org.f420.duxchallenge.dto.PaginatedResponse;
import org.f420.duxchallenge.dto.TeamDTO;
import org.f420.duxchallenge.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.f420.duxchallenge.service.TeamServiceTest.findAllTeamsFromJson;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@Slf4j
class TeamServiceSpecTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamDAO teamDAO;
    List<Team> teams;
    @BeforeEach
    void setup() {
        teams = findAllTeamsFromJson();
        teamDAO.saveAll(teams);
    }

    @Test
    void shouldSearchByNombre() {
        TeamDTO filter = new TeamDTO();
        filter.setNombre("Real Madrid");

        Pageable pageable = PageRequest.of(0, 10);

        PaginatedResponse<TeamDTO> result = teamService.searchBy(filter, pageable);

        assertEquals(1, result.getData().size());
        assertEquals(teams.get(0).getNombre(), result.getData().get(0).getNombre());
    }

    @Test
    void shouldSearchByPais() {
        TeamDTO filter = new TeamDTO();
        String pais = "España";
        filter.setPais(pais);

        Pageable pageable = PageRequest.of(0, 10);

        PaginatedResponse<TeamDTO> result = teamService.searchBy(filter, pageable);
        log.info("filter: {}, teams: {}",filter, result);
        assertEquals(3, result.getData().size()); // Real Madrid, Barcelona, Atlético Madrid
        assertEquals(pais, result.getData().get(0).getPais());
    }

    @Test
    void shouldReturnNothing() {
        TeamDTO filter = new TeamDTO();
        filter.setPais("África");

        Pageable pageable = PageRequest.of(0, 10);

        PaginatedResponse<TeamDTO> result = teamService.searchBy(filter, pageable);
        log.info("filter: {}, teams: {}",filter, result);
        assertEquals(0, result.getData().size());
    }
}

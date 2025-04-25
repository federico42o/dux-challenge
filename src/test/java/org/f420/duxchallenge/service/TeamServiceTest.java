package org.f420.duxchallenge.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.f420.duxchallenge.dao.TeamDAO;
import org.f420.duxchallenge.dto.PaginatedResponse;
import org.f420.duxchallenge.dto.TeamDTO;
import org.f420.duxchallenge.exceptions.ApiException;
import org.f420.duxchallenge.model.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
class TeamServiceTest {

    @InjectMocks
    private TeamService teamService;
    @Spy
    private TeamDAO teamDAO;
    private static final ObjectMapper objectMapper = JsonMapper.builder()
            .findAndAddModules()
            .build();
    public static final TypeReference<List<Map<String, Object>>> listMapTypeRef = new TypeReference<>() {
    };


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Team defaultTeam = new Team();
        defaultTeam.setId(1L);
        defaultTeam.setNombre("Real Madrid");
        defaultTeam.setLiga("La Liga");
        defaultTeam.setPais("España");
        defaultTeam.setDeleted(true);
    }


    private static Stream<Arguments> providerForSave() {
        return Stream.of(
                Arguments.of(
                        new Team(1L, "Real Madrid", "España", "La Liga", false),
                        new TeamDTO(1L, "Real Madrid", "España", "La Liga")),
                Arguments.of(
                        new Team(2L, "Barcelona", "España", "La Liga", false),
                        new TeamDTO(2L, "Barcelona", "España", "La Liga")
                )
        );
    }

    private static Stream<Arguments> providerForUpdate() {
        return Stream.of(
                Arguments.of(
                        new Team(1L, "Real Madrid", "España", "La Liga", false),
                        new TeamDTO(1L, "Real Madrid de Andorra", "Andorra", "La Liga"),
                        new Team(1L, "Real Madrid de Andorra", "Andorra", "La Liga", false)),
                Arguments.of(
                        new Team(2L, "Barcelona", "España", "La Liga", false),
                        new TeamDTO(2L, "Barcelona Sporting Club", "Ecuador", "Serie A de Ecuador"),
                        new Team(2L, "Barcelona Sporting Club", "Ecuador", "Serie A de Ecuador", false)
                ),
                Arguments.of(
                        new Team(2L, "Barcelona", "España", "La Liga", false),
                        new TeamDTO(2L, null, "Ecuador", "Serie A de Ecuador"),
                        new Team(2L, "Barcelona", "Ecuador", "Serie A de Ecuador", false)
                ),
                Arguments.of(
                        new Team(2L, "Barcelona", "España", "La Liga", false),
                        new TeamDTO(2L, null, null, "Serie A de Ecuador"),
                        new Team(2L, "Barcelona", "España", "Serie A de Ecuador", false)
                )
        );
    }

    @ParameterizedTest(name = "[{index}]: Guardando equipo")
    @DisplayName("teamService.save")
    @MethodSource("providerForSave")
    void shouldSave(Team entity, TeamDTO expected) {

        when(teamDAO.save(any())).thenReturn(entity);

        TeamDTO savedTeamDTO = teamService.save(expected);

        assertNotNull(savedTeamDTO);
        assertEquals(expected.getId(), savedTeamDTO.getId());
        assertEquals(expected.getNombre(), savedTeamDTO.getNombre());
        assertEquals(expected.getPais(), savedTeamDTO.getPais());
        assertEquals(expected.getLiga(), savedTeamDTO.getLiga());

        verify(teamDAO, times(1)).save(any(Team.class));
    }

    @ParameterizedTest(name = "[{index}]: Actualizando equipo")
    @DisplayName("teamService.update")
    @MethodSource("providerForUpdate")
    void shouldUpdate(Team originalEntity, TeamDTO updateRequest, Team expected) {
        when(teamDAO.findById(any())).thenReturn(Optional.of(originalEntity));
        teamService.update(originalEntity.getId(), updateRequest);

        assertEquals(updateRequest.getId(), expected.getId());
        if (updateRequest.getNombre() != null && !updateRequest.getNombre().isEmpty()) {
            assertEquals(updateRequest.getNombre(), expected.getNombre());
        } else {
            assertEquals(originalEntity.getNombre(), expected.getNombre());
        }
        if (updateRequest.getPais() != null && !updateRequest.getPais().isEmpty()) {
            assertEquals(updateRequest.getPais(), expected.getPais());
        } else {
            assertEquals(originalEntity.getPais(), expected.getPais());
        }
        if (updateRequest.getLiga() != null && !updateRequest.getLiga().isEmpty()) {
            assertEquals(updateRequest.getLiga(), expected.getLiga());
        } else {
            assertEquals(originalEntity.getLiga(), expected.getLiga());
        }

        verify(teamDAO, times(1)).save(any(Team.class));
    }

    @Test
    void shouldNotUpdateAndThrowApiException() {
        Long invalidId = -999L;
        when(teamDAO.findById(invalidId)).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> teamService.update(invalidId, any()));
    }
    @Test
    @DisplayName("teamService.findAll")
    void findAll() {

        List<Team> teams = findAllTeamsFromJson();
        Pageable pageable = PageRequest.of(0, 5);
        PageImpl<Team> page = new PageImpl<>(teams, pageable, teams.size());
        when(teamDAO.findAllByDeletedIsFalse(pageable)).thenReturn(page);
        PaginatedResponse<TeamDTO> response = teamService.findAll(pageable);
        assertTrue(response.getLimit() <= 5);
        assertEquals(teams.size(), response.getCount());
        assertEquals(teams.get(0).getNombre(), response.getData().get(0).getNombre());
        assertEquals(teams.get(2).getPais(), response.getData().get(2).getPais());
        assertEquals(teams.get(4).getLiga(), response.getData().get(4).getLiga());
        verify(teamDAO, times(1)).findAllByDeletedIsFalse(pageable);

    }

    @Test
    void shouldFindById() {
        Team team = new Team(1L, "Real Madrid", "España", "La Liga", false);
        TeamDTO teamDTO = new TeamDTO(1L, "Real Madrid", "España", "La Liga");

        when(teamDAO.findById(1L)).thenReturn(Optional.of(team));

        TeamDTO result = teamService.findById(1L);

        assertEquals(teamDTO.getId(), result.getId());
        assertEquals(teamDTO.getNombre(), result.getNombre());
        assertEquals(teamDTO.getPais(), result.getPais());
        assertEquals(teamDTO.getLiga(), result.getLiga());

        verify(teamDAO, times(1)).findById(1L);
    }

    @Test
    void shouldThrowApiExceptionWhenTeamIsDeleted() {
        Team team = new Team(1L, "Real Madrid", "España", "La Liga", true);

        when(teamDAO.findById(1L)).thenReturn(Optional.of(team));

        assertThrows(ApiException.class, () -> teamService.findById(1L));

        verify(teamDAO, times(1)).findById(1L);
    }

    @Test
    void shouldThrowApiExceptionWhenIdIsNotFound() {
        Long invalidId = 999L;

        when(teamDAO.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> teamService.findById(invalidId));

        verify(teamDAO, times(1)).findById(invalidId);
    }

    public static List<Team> findAllTeamsFromJson() {
        //Se deberia mover a ConvertUtils
        String teamsJsonUrl = "src/test/java/org/f420/duxchallenge/resources/teams.json";
        String content;
        try {
            content = Files.readString(Paths.get(teamsJsonUrl));
        } catch (IOException e) {
            log.error("Error procesando el json");
            log.error(e.getMessage(), e);
            return Collections.emptyList();
        }
        content = content.substring(content.indexOf("["));

        try {
            return objectMapper.readValue(content, listMapTypeRef).stream().map(
                    list -> {
                        try {
                            return objectMapper.readValue(objectMapper.writeValueAsString(list), Team.class);
                        } catch (JsonProcessingException e) {
                            log.error("Error procesando el json");
                            log.error(e.getMessage(), e);

                        }
                        return null;
                    }).filter(Objects::nonNull).toList();

        } catch (JsonProcessingException e) {
            log.error("Error procesando el json");
            log.error(e.getMessage(), e);
        }
        return Collections.emptyList();
    }
}
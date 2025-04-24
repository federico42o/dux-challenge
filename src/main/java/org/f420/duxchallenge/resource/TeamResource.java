package org.f420.duxchallenge.resource;

import jakarta.validation.Valid;
import org.f420.duxchallenge.dto.PaginatedResponse;
import org.f420.duxchallenge.dto.TeamDTO;
import org.f420.duxchallenge.resource.common.AbstractResource;
import org.f420.duxchallenge.service.TeamService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("teams")
public class TeamResource extends AbstractResource {

    private final TeamService teamService;

    public TeamResource(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<TeamDTO> save(@Valid @RequestBody TeamDTO teamDto) {
        return response(teamService.save(teamDto));
    }
    @GetMapping
    public PaginatedResponse<TeamDTO> findAll(Pageable pageable) {
        return teamService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public TeamDTO findById(@PathVariable Long id) {
        return teamService.findById(id);
    }

    @GetMapping("search")
    public PaginatedResponse<TeamDTO> searchBy(Pageable pageable,
                                               TeamDTO filter) {
        return teamService.searchBy(filter, pageable);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody TeamDTO updateRequest) {
        teamService.update(id, updateRequest);
        return emptyResponse();
    }


}

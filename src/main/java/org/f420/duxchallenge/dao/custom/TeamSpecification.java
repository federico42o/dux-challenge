package org.f420.duxchallenge.dao.custom;

import org.f420.duxchallenge.dto.Criteria;
import org.f420.duxchallenge.dto.TeamDTO;
import org.f420.duxchallenge.model.Team;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static org.f420.duxchallenge.dao.custom.SpecificationBuilder.buildSpecification;
import static org.f420.duxchallenge.enums.Comparator.EQUALS;
import static org.f420.duxchallenge.enums.Comparator.ILIKE;

public class TeamSpecification {

    public static Specification<Team> buildSpecificationFromFilter(TeamDTO filter) {
        List<Specification<Team>> specs = new ArrayList<>();

        if (filter.getNombre() != null && !filter.getNombre().isBlank()) {
            specs.add(buildSpecification(new Criteria("nombre", ILIKE, filter.getNombre())));
        }

        if (filter.getPais() != null && !filter.getPais().isBlank()) {
            specs.add(buildSpecification(new Criteria("pais", ILIKE, filter.getPais())));
        }

        if (filter.getLiga() != null && !filter.getLiga().isBlank()) {
            specs.add(buildSpecification(new Criteria("liga", ILIKE, filter.getLiga())));
        }

        // * default
        specs.add(buildSpecification(new Criteria("deleted", EQUALS, Boolean.FALSE.toString())));

        return specs.stream().reduce(Specification::and).orElse(null);
    }
}

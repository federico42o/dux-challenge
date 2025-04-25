package org.f420.duxchallenge.dao;


import org.f420.duxchallenge.dao.common.BaseRepository;
import org.f420.duxchallenge.model.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface TeamDAO extends BaseRepository<Team, Long> {
    Page<Team> findAllByDeletedIsFalse(Pageable pageable);
    Optional<Team> findByIdAndDeletedIsFalse(Long id);
    Optional<Team> findByNombreAndLigaAndPais(String nombre, String liga, String pais);
}

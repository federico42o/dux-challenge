package org.f420.duxchallenge.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(
        name = "team",
        indexes = {
                @Index(columnList = "nombre"),
                @Index(columnList = "pais")
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_name_country_league", columnNames = {"nombre", "pais","liga"}),
        }
)
@SequenceGenerator(name = "team_seq", sequenceName = "team_seq", allocationSize = 1)
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_seq")
    @Column(name = "TEAM_ID")
    private Long id;

    private String nombre;

    private String pais;

    private String liga;

    private Boolean deleted;
}

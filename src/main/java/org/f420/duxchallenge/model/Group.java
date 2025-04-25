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
@Table(name = "groups")
@SequenceGenerator(name = "group_seq", sequenceName = "group_seq", allocationSize = 1)
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "group_seq")
    @Column(name = "GROUP_ID")
    private Long id;
    private String name;
}

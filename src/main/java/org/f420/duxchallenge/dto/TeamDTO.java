package org.f420.duxchallenge.dto;

import lombok.*;
import org.f420.duxchallenge.validation.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TeamDTO {

    private Long id;
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String pais;
    @NotEmpty
    private String liga;
}

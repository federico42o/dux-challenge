package org.f420.duxchallenge.dto;

import lombok.*;
import org.f420.duxchallenge.enums.Comparator;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Criteria {
    private String field;
    private Comparator comparator;
    private String value;
}

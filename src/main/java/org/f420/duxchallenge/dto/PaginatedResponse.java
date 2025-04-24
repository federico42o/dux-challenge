package org.f420.duxchallenge.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {

    private Long offset;
    private Integer limit;
    private Long count;
    private List<T> data;

    public PaginatedResponse(Page<?> page, List<T> data) {
        this.offset = page.getPageable().getOffset();
        this.limit = page.getPageable().getPageSize();
        this.data = data;
        this.count = page.getTotalElements();
    }
}

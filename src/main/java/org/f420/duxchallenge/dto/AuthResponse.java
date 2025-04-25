package org.f420.duxchallenge.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AuthResponse {
    private String token;
}

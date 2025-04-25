package org.f420.duxchallenge.dao;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class LoginRequestDTO {
    private String username;
    private String password;
}

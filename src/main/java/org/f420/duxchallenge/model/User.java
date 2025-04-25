package org.f420.duxchallenge.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "users")
@SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
public class User implements UserDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @Column(name = "USER_ID")
    private Long id;

    private String username;
    private String password;
    private String fullName;
    private String dni;
    private Boolean isEnabled;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private Set<Group> groups;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.groups.stream()
                .map(group -> new SimpleGrantedAuthority(group.getName()))
                .collect(Collectors.toSet());

    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isEnabled;
    }
}


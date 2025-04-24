package org.f420.duxchallenge.enums;

import lombok.Getter;

@Getter
public enum Comparator {
    EQUALS("equals"),
    LIKE("like"),
    ILIKE("ilike"),
    ;
    private final String name;

    Comparator(String name) {
        this.name = name;
    }
}

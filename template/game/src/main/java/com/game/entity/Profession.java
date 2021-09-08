package com.game.entity;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;


public enum Profession {
    WARRIOR,
    ROGUE,
    SORCERER,
    CLERIC,
    PALADIN,
    NAZGUL,
    WARLOCK,
    DRUID;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.game.entity;

public enum Race {
    HUMAN,
    DWARF,
    ELF,
    GIANT,
    ORC,
    TROLL,
    HOBBIT;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
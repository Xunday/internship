package com.game.entity;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @Column
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Long id;

    @NonNull
    @Column(name = "name", length = 12, nullable = false)
    private String name;

    @NonNull
    @Column(name = "title", length = 30, nullable = false)
    private String title;

    @NonNull
    @Column(name = "race", nullable = false)
    @Enumerated(EnumType.STRING)
    private Race race;

    @NonNull
    @Column(name = "profession", nullable = false)
    @Enumerated(EnumType.STRING)
    private Profession profession;

    @NonNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "birthday", nullable = false)
    private Date birthday;

    @NonNull
    @Column(name = "banned", nullable = false)
    private Boolean banned;

    @NonNull
    @Column(name = "experience", nullable = false)
    private Integer experience;

    @NonNull
    @Column(name = "level")
    private Integer level;

    @NonNull
    @Column(name = "untilNextLevel")
    private Integer untilNextLevel;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public Race getRace() {
        return race;
    }

    public void setRace(@NonNull Race race) {
        this.race = race;
    }

    @NonNull
    public Profession getProfession() {
        return profession;
    }

    public void setProfession(@NonNull Profession profession) {
        this.profession = profession;
    }

    @NonNull
    public Integer getExperience() {
        return experience;
    }

    public void setExperience(@NonNull Integer experience) {
        this.experience = experience;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getUntilNextLevel() {
        return untilNextLevel;
    }

    public void setUntilNextLevel(Integer untilNextLevel) {
        this.untilNextLevel = untilNextLevel;
    }

    @NonNull
    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(@NonNull Date birthday) {
        this.birthday = birthday;
    }

    @NonNull
    public Boolean getBanned() {
        return banned;
    }

    public void setBanned(@NonNull Boolean banned) {
        this.banned = banned;
    }
}

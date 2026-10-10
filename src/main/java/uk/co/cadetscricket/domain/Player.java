package uk.co.cadetscricket.domain;

import jakarta.persistence.*;
import uk.co.cadetscricket.common.Gender;

/**
 * Entity representing a player in cadets cricket club, Teams will have multple players and players can be in multiple teams.
 * All players will have unique Id, first name and initial, with an optional last name and gender
 */
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INTEGER")
    private Long playerId;
    private String firstName;
    private String surname;
    private String initial;
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public Player() {
    }

    public Player(String firstName, String surname, String initial, Gender gender) {
        this.firstName = firstName;
        this.surname = surname;
        this.initial = initial;
        this.gender = gender;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getInitial() {
        return initial;
    }

    public void setInitial(String initial) {
        this.initial = initial;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Transient
    public String getDisplayName() {
        if (surname == null || surname.isBlank()) {
            return firstName + (initial == null || initial.isBlank() ? "" : " " + initial);
        }

        return firstName + " " + surname;
    }

}
package at.fhv.teame.domain;

import java.time.LocalDate;

import lombok.Getter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
public class Album {
    @Id
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private String label;
    @Column
    private int amountOfSongs;
    @Column
    private String genre;

    //required for Hibernate
    protected Album(){}

    public Album (String name, String label, String artist, LocalDate release, int amountOfSongs, String genre){
        this.name = name;
        this.label = label;
        this.amountOfSongs = amountOfSongs;
        this.genre = genre;
    }
}

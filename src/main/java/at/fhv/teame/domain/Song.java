package at.fhv.teame.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
public class Song {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String title;
    @Column
    private String artist;
    @Column
    private LocalDate release;

    //required for Hibernate
    protected Song() {}

    public Song(String title, String artist, LocalDate release) {
        this.title = title;
        this.artist = artist;
        this.release = release;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public LocalDate getRelease() {
        return release;
    }
}

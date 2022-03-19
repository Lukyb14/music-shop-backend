package at.fhv.teame.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Song {

    @Id
    @Column
    private Long id;
    @Column
    private String title;
    @ManyToOne
    private Album album;
    @Column
    private String artist;
    @Column
    private LocalDate release;

    //required for Hibernate
    protected Song() {}

    public Song(String title, String artist, LocalDate release) {
        this.title = title;
        //this.album = album;
        this.artist = artist;
        this.release = release;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

//    public Album getAlbum() {
//        return album;
//    }

    public String getArtist() {
        return artist;
    }

    public LocalDate getRelease() {
        return release;
    }
}

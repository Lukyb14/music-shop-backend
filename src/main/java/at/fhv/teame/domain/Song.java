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
    @Column
    private String recording;

    //required for Hibernate
    protected Song() {}

    public Song(String title, Album album, String artist, LocalDate release, String recording) {
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.release = release;
        this.recording = recording;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Album getAlbum() {
        return album;
    }

    public String getArtist() {
        return artist;
    }

    public LocalDate getRelease() {
        return release;
    }

    public String getRecording() {
        return recording;
    }
}

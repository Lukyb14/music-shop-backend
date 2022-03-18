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
    private String recordLabel;
    @Column
    private String artist;
    @Column
    private LocalDate publishDate;

    //required for Hibernate
    protected Song() {

    }

    public Song(String title, String recordLabel, String artist, LocalDate publishDate) {
        this.title = title;
        this.recordLabel = recordLabel;
        this.artist = artist;
        this.publishDate = publishDate;
    }

    public Album getAlbum() {
        return album;
    }
}

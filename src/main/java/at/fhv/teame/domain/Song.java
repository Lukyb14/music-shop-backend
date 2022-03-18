package at.fhv.teame.domain;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;

@Entity
@Getter
public class Song {

    @Id
    @Column
    private Long id;
    @Column
    private String title;
    @Column
    private String artist;
    @Column
    private LocalDate release;
    @Column
    private String recording;

    //required for Hibernate
    protected Song() {}

    public Song(String title, String recordLabel, String artist, LocalDate publishDate, String genre) {
        this.title = title;
        this.artist = artist;
        this.release = release;
    }
}

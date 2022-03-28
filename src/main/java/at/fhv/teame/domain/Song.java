package at.fhv.teame.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Song {

    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String title;
    @ManyToOne
    private Album album;
    @Column
    private LocalDate release;

    //required for Hibernate
    protected Song() {}

    public Song(String title, LocalDate release) {
        this.title = title;
        this.release = release;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getRelease() {
        return release;
    }
}

package at.fhv.teame.domain.model.soundcarrier;

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
    @Column
    private String duration;

    //required for Hibernate
    protected Song() {}

    public Song(String title, LocalDate release, String duration) {
        this.title = title;
        this.release = release;
        this.duration = duration;
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

    public String getDuration() {
        return duration;
    }
}

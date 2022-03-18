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


}

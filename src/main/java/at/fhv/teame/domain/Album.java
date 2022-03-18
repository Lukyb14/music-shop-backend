package at.fhv.teame.domain;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Album {
    @Id
    @Column
    private Long id;
    @Column
    private String name;
    @Column
    private String label;
    @Column
    private LocalDate release;
    @OneToMany(mappedBy = "album")
    private List<Song> songs;
    @Column
    private String genre;

    //required for Hibernate
    protected Album(){
        songs = new java.util.ArrayList<>();
    }

    public Album (String name, String label, LocalDate release, List<Song> songs, String genre){
        this.name = name;
        this.label = label;
        this.release = release;
        this.songs = songs;
        this.genre = genre;
    }


}

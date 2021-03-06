package at.fhv.teame.domain.model.soundcarrier;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
public class Album {
    @Id
    @GeneratedValue
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
    @Column
    private String artist;

    //required for Hibernate
    protected Album(){
        songs = new java.util.ArrayList<>();
    }

    public Album (String name, String label, LocalDate release, List<Song> songs, String genre, String artist){
        this.name = name;
        this.label = label;
        this.release = release;
        this.songs = songs;
        this.genre = genre;
        this.artist = artist;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public LocalDate getRelease() {
        return release;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public String getGenre() {
        return genre;
    }

    public String getArtist() {
        return artist;
    }

    public void setSong(List<Song> songs) {
        this.songs = songs;
    }
}

package at.fhv.teame.domain.model.onlineshop;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class DigitalSong {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String artist;

    @Column
    private String title;

    @Column
    private String genre;

    @Column
    private String duration;

    @Column
    private LocalDate releaseDate;

    @Column
    private String mp3File;

    @Column
    private String coverFile;

    // Required for hibernate
    protected DigitalSong() {}

    public DigitalSong(String artist, String title, String genre, String duration,
                       LocalDate releaseDate, String mp3File, String coverFile) {
        this.artist = artist;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate;
        this.mp3File = mp3File;
        this.coverFile = coverFile;
    }

    public Long getId() {
        return id;
    }

    public String getArtist() {
        return artist;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public String getDuration() {
        return duration;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getMp3File() {
        return mp3File;
    }

    public BigDecimal getPrice() {
        return null;
    }

    public String getCoverFile() {
        return coverFile;
    }
}

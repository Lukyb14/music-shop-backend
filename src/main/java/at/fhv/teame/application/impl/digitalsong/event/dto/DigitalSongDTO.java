package at.fhv.teame.application.impl.digitalsong.event.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class DigitalSongDTO implements Serializable {

    private Long songId;

    private String artist;

    private String title;

    private String genre;

    private String duration;

    private String releaseDate;

    private String mp3File;

    private String coverFile;

    private String price;

    public DigitalSongDTO(Long songId, String artist, String title, String genre, String duration, LocalDate releaseDate, String mp3File, String coverFile, BigDecimal price) {
        this.songId = songId;
        this.artist = artist;
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.releaseDate = releaseDate.toString();
        this.mp3File = mp3File;
        this.coverFile = coverFile;
        this.price = price.toString();
    }

    public Long getSongId() {
        return songId;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getMp3File() {
        return mp3File;
    }

    public String getCoverFile() {
        return coverFile;
    }

    public String getPrice() {
        return price;
    }
}

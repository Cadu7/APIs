package br.com.api.apispring.domain.dto;

import br.com.api.apispring.domain.model.Music;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MusicDTO {
    
    private String name;
    private String album;
    private String author;
    private LocalDateTime release;
    
    public MusicDTO(Music music) {
        this.name = music.getName();
        this.album = music.getAlbum();
        this.author = music.getAuthor();
        this.release = music.getRelease();
    }
}

package seleksi.labpro.owca.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.respository.FilmRepository;
import seleksi.labpro.owca.service.FilmService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final Path mediaRoot = Paths.get("media");

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        return filmRepository.findById(id);
    }

    @Override
    public Film createFilm(Film film, MultipartFile video, MultipartFile coverImage) throws IOException {
        // Save video file
        String videoFileName = saveFile(video, "videos");
        film.setVideoUrl(videoFileName);

        // Save cover image file (nullable)
        if (coverImage != null && !coverImage.isEmpty()) {
            String coverImageFileName = saveFile(coverImage, "images");
            film.setCoverImageUrl(coverImageFileName);
        }

        film.setCreated_at(OffsetDateTime.now());
        film.setUpdated_at(OffsetDateTime.now());

        return filmRepository.save(film);
    }

    private String saveFile(MultipartFile file, String subdirectory) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }

        Path dir = mediaRoot.resolve(subdirectory);
        Files.createDirectories(dir);
        Path filePath = dir.resolve(file.getOriginalFilename());
        Files.write(filePath, file.getBytes());

        return filePath.toString();
    }

    @Override
    public Film updateFilm(Long id, Film updatedFilm, MultipartFile video, MultipartFile coverImage) throws IOException {
        Optional<Film> optionalFilm = filmRepository.findById(id);

        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();

            // Update fields
            film.setTitle(updatedFilm.getTitle());
            film.setDescription(updatedFilm.getDescription());
            film.setDirector(updatedFilm.getDirector());
            film.setReleaseYear(updatedFilm.getReleaseYear());
            film.setGenres(updatedFilm.getGenres());
            film.setPrice(updatedFilm.getPrice());
            film.setDuration(updatedFilm.getDuration());
            film.setUpdated_at(OffsetDateTime.now());

            // Replace video file if a new one is uploaded
            if (video != null && !video.isEmpty()) {
                deleteFile(film.getVideoUrl());
                String videoFileName = saveFile(video, "videos");
                film.setVideoUrl(videoFileName);
            }

            // Replace cover image file if a new one is uploaded
            if (coverImage != null && !coverImage.isEmpty()) {
                deleteFile(film.getCoverImageUrl());
                String coverImageFileName = saveFile(coverImage, "images");
                film.setCoverImageUrl(coverImageFileName);
            }

            return filmRepository.save(film);
        }

        return null;
    }

    private void deleteFile(String filePath) {
        if (filePath != null) {
            try {
                Files.deleteIfExists(Paths.get(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Film deleteFilmById(Long id) {
        Optional<Film> optionalFilm = filmRepository.findById(id);

        if (optionalFilm.isPresent()) {
            Film film = optionalFilm.get();
            // Delete video and cover image files
            deleteFile(film.getVideoUrl());
            deleteFile(film.getCoverImageUrl());

            film.getGenres().clear();
            filmRepository.save(film);

            filmRepository.deleteById(id);

            return optionalFilm.get();
        }

        return null;
    }
}

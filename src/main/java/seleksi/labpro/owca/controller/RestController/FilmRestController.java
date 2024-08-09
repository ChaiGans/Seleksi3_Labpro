package seleksi.labpro.owca.controller.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.model.response.*;
import seleksi.labpro.owca.model.response.FilmResponse.DeleteFilmResponse;
import seleksi.labpro.owca.model.response.FilmResponse.FilmResponse;
import seleksi.labpro.owca.model.response.FilmResponse.GetAllFilmResponse;
import seleksi.labpro.owca.service.FilmService;
import seleksi.labpro.owca.utils.FileUtils;
import seleksi.labpro.owca.utils.S3Utils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmRestController {
    private final FilmService filmService;

    @GetMapping
    public ResponseEntity<RestResponse<List<GetAllFilmResponse>>> getAllFilms() {
        List<Film> films = filmService.getAllFilms();

        List<GetAllFilmResponse> filmsResponse = films.stream()
                .map(film ->
                {
                    return GetAllFilmResponse.builder()
                            .id(String.valueOf(film.getId()))
                            .title(film.getTitle())
                            .director(film.getDirector())
                            .release_year(film.getReleaseYear())
                            .genre(film.getGenres())
                            .price(film.getPrice())
                            .duration(film.getDuration())
                            .cover_image_url(film.getCoverImageUrl())
                            .updated_at(String.valueOf(film.getUpdated_at()))
                            .created_at(String.valueOf(film.getCreated_at()))
                            .build();
                })
                .collect(Collectors.toList());

        RestResponse response = RestResponse
                .builder()
                .status(Status.success)
                .message("All film data")
                .data(filmsResponse)
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<?>> getFilmById(@PathVariable Long id) throws IOException {
        Optional<Film> film = filmService.getFilmById(id);

        if (film.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    RestResponse
                            .builder()
                            .status(Status.error)
                            .message("Film not found")
                            .data(null)
                            .build()
            );
        }

        Film foundFilm = film.get();

        FilmResponse filmResponse = FilmResponse
                .builder()
                .id(String.valueOf(id))
                .title(foundFilm.getTitle())
                .description(foundFilm.getDescription())
                .director(foundFilm.getDirector())
                .release_year(foundFilm.getReleaseYear())
                .genre(foundFilm.getGenres())
                .price(foundFilm.getPrice())
                .duration(foundFilm.getDuration())
                .video_url(S3Utils.generatePresignedUrl(foundFilm.getVideoUrl(), filmService.getBucketName(), filmService.getS3Client()))
                .cover_image_url(S3Utils.generatePresignedUrl(foundFilm.getCoverImageUrl(), filmService.getBucketName(), filmService.getS3Client()))
                .created_at(String.valueOf(foundFilm.getCreated_at()))
                .updated_at(String.valueOf(foundFilm.getUpdated_at()))
                .build();

        return ResponseEntity.ok(RestResponse
                .builder()
                        .status(Status.success)
                        .message("Found film")
                        .data(filmResponse)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestResponse<?>> updateFilmById(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("director") String director,
            @RequestParam("release_year") Integer releaseYear,
            @RequestParam("genre") List<String> genres,
            @RequestParam("price") Integer price,
            @RequestParam("duration") Integer duration,
            @RequestParam(value = "video", required = false) MultipartFile videoBinaryFile,
            @RequestParam(value = "cover_image", required = false) MultipartFile coverImageBinaryFile
    ) throws IOException {
        Optional<Film> foundFilm = filmService.getFilmById(id);

        if (foundFilm.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    RestResponse
                            .builder()
                            .status(Status.error)
                            .message("Film not found")
                            .data(null)
                            .build()
            );
        }

        Film updatedFilm = filmService.updateFilm(
                id,
                Film
                        .builder()
                        .title(title)
                        .description(description)
                        .director(director)
                        .releaseYear(releaseYear)
                        .genres(genres)
                        .price(price)
                        .duration(duration)
                        .build(),
                videoBinaryFile,
                coverImageBinaryFile
        );

        return ResponseEntity.ok(RestResponse
                .builder()
                .status(Status.success)
                .message("Delete success")
                .data(FilmResponse
                        .builder()
                        .id(String.valueOf(updatedFilm.getId()))
                        .title(updatedFilm.getTitle())
                        .description(updatedFilm.getDescription())
                        .director(updatedFilm.getDirector())
                        .release_year(updatedFilm.getReleaseYear())
                        .genre(updatedFilm.getGenres())
                        .price(updatedFilm.getPrice())
                        .duration(updatedFilm.getDuration())
                        .video_url(S3Utils.generatePresignedUrl(updatedFilm.getVideoUrl(), filmService.getBucketName(), filmService.getS3Client()))
                        .created_at(String.valueOf(updatedFilm.getCreated_at()))
                        .updated_at(String.valueOf(updatedFilm.getUpdated_at()))
                        .build())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<?>> deleteFilmById(
            @PathVariable("id") Long id
    ) {
        Optional<Film> foundFilm = filmService.getFilmById(id);

        if (foundFilm.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                RestResponse
                        .builder()
                        .status(Status.error)
                        .message("Film not found")
                        .data(null)
                        .build()
            );
        }

        List<String> genres = foundFilm.get().getGenres();

        DeleteFilmResponse filmResponse = DeleteFilmResponse
                .builder()
                .id(String.valueOf(id))
                .title(foundFilm.get().getTitle())
                .description(foundFilm.get().getDescription())
                .director(foundFilm.get().getDirector())
                .release_year(foundFilm.get().getReleaseYear())
                .genre(genres.stream().collect(Collectors.toList()))
                .video_url(foundFilm.get().getVideoUrl())
                .created_at(String.valueOf(foundFilm.get().getCreated_at()))
                .updated_at(String.valueOf(foundFilm.get().getUpdated_at()))
                .build();

        Film deletedFilm = filmService.deleteFilmById(id);

        return ResponseEntity.ok(RestResponse
                .builder()
                .status(Status.success)
                .message("Delete success")
                .data(filmResponse)
                .build());
    }

    @PostMapping
    public ResponseEntity<RestResponse<FilmResponse>> createFilm(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("director") String director,
            @RequestParam("release_year") Integer releaseYear,
            @RequestParam("genre") List<String> genre,
            @RequestParam("price") Integer price,
            @RequestParam("duration") Integer duration,
            @RequestParam("video") MultipartFile video,
            @RequestParam("cover_image") MultipartFile coverImage
    ) throws IOException {
        Film filmRequest = Film.builder()
                .title(title)
                .description(description)
                .director(director)
                .releaseYear(releaseYear)
                .genres(genre)
                .price(price)
                .duration(duration)
                .build();

        Film newFilm = filmService.createFilm(filmRequest, video, coverImage);

        RestResponse<FilmResponse> response = RestResponse.<FilmResponse>builder()
                .status(Status.success)
                .message("Created new film")
                .data(FilmResponse.builder()
                        .id(String.valueOf(newFilm.getId()))
                        .title(newFilm.getTitle())
                        .description(newFilm.getDescription())
                        .director(newFilm.getDirector())
                        .release_year(newFilm.getReleaseYear())
                        .genre(newFilm.getGenres())
                        .price(newFilm.getPrice())
                        .duration(newFilm.getDuration())
                        .video_url(S3Utils.generatePresignedUrl(newFilm.getVideoUrl(), filmService.getBucketName(), filmService.getS3Client()))
                        .cover_image_url(S3Utils.generatePresignedUrl(newFilm.getCoverImageUrl(), filmService.getBucketName(), filmService.getS3Client()))
                        .created_at(String.valueOf(newFilm.getCreated_at()))
                        .updated_at(String.valueOf(newFilm.getUpdated_at()))
                        .build())
                .build();

        return ResponseEntity.ok(response);
    }
}
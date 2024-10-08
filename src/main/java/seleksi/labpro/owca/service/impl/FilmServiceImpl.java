package seleksi.labpro.owca.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import seleksi.labpro.owca.entity.Film;
import seleksi.labpro.owca.entity.User;
import seleksi.labpro.owca.respository.FilmRepository;
import seleksi.labpro.owca.respository.ReviewRepository;
import seleksi.labpro.owca.service.FilmService;
import seleksi.labpro.owca.utils.S3Utils;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    private final AmazonS3 s3Client;
    private final String bucketName = "labpro-elbert";
    private final ReviewRepository reviewRepository;

    public FilmServiceImpl(FilmRepository filmRepository, AmazonS3 s3Client, ReviewRepository reviewRepository) {
        this.filmRepository = filmRepository;
        this.s3Client = s3Client;
        this.reviewRepository = reviewRepository;
    }

    public AmazonS3 getS3Client() {
        return s3Client;
    }

    public String getBucketName() {
        return bucketName;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = filmRepository.findAll();

        // Modify the Film objects in place and collect them back into a list
        films = films.stream().map(film -> {
            film.setCoverImageUrl(S3Utils.generatePresignedUrl(film.getCoverImageUrl(), bucketName, s3Client));
            film.setVideoUrl(S3Utils.generatePresignedUrl(film.getVideoUrl(), bucketName, s3Client));
            return film;
        }).collect(Collectors.toList());

        return films;
    }

    @Override
    public Optional<Film> getFilmById(Long id) {
        Optional<Film> film = filmRepository.findById(id);

        return film;
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

        // Initial filename without extension
        String originalFilename = file.getOriginalFilename();
        String baseName = originalFilename.contains(".") ? originalFilename.substring(0, originalFilename.lastIndexOf('.')) : originalFilename;
        String extension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf('.')) : "";

        String key = subdirectory + "/" + originalFilename;
        int counter = 1;

        // Check if a file with the same key already exists in the bucket
        while (s3Client.doesObjectExist(bucketName, key)) {
            // Increment the identifier and generate a new key
            key = subdirectory + "/" + baseName + "_" + counter + extension;
            counter++;
        }

        // Upload the file to S3 with the final unique key
        s3Client.putObject(bucketName, key, file.getInputStream(), new ObjectMetadata());

        return key;
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

    public void deleteFile(String key) {
        if (key != null) {
            s3Client.deleteObject(bucketName, key);
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
            film.setVideoUrl("deleted");
            film.setCoverImageUrl("deleted");

            if (!film.getUsers().isEmpty()) {
                film.getUsers().forEach(user -> user.getOwnedFilms().remove(film));
                film.getUsers().clear();
            }

            if (!film.getWishlistedBy().isEmpty()) {
                film.getWishlistedBy().forEach(user -> user.getWishlistFilms().remove(film));
                film.getWishlistedBy().clear();
            }

            if (!film.getReviews().isEmpty()) {
                film.getReviews().forEach(review -> reviewRepository.deleteById(review.getId()));
                film.getReviews().clear();
            }

            film.getGenres().clear();

            filmRepository.save(film);

            filmRepository.deleteById(id);

            return optionalFilm.get();
        }

        return null;
    }

    @Override
    public List<Film> findByQueryTitle(String query) {
        List<Film> foundFilms = filmRepository.findUsersByTitleContains(query);

        return foundFilms;
    }

    @Override
    public Boolean isBought(Long id, User user) {
        Optional<Film> foundFilm = filmRepository.findById(id);

        if (foundFilm.isEmpty()) {
            return Boolean.FALSE;
        }

        return foundFilm.get().getUsers().contains(user);
    }
}

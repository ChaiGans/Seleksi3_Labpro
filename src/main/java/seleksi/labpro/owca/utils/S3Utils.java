package seleksi.labpro.owca.utils;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import java.net.URL;
import java.util.Date;

public class S3Utils {
    public static String generatePresignedUrl(String key, String bucketName, AmazonS3 s3Client) {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += 1000 * 60 * 60;
        expiration.setTime(expTimeMillis);

        // Generate the pre-signed URL
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, key)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);

        return url.toString();
    }

    public static String getPreSignedUrlForVideo(String keyPath, String bucketName, AmazonS3 s3Client) {
        return S3Utils.generatePresignedUrl(keyPath, bucketName, s3Client);
    }
}

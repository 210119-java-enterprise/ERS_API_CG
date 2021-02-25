package com.revature.util;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * This class reads files from the remote S3 bucket and stores that
 * information into the reader
 */
public class S3BucketReader {

    private static final Logger logger = LogManager.getLogger(S3BucketReader.class);

    private static String url;
    private static String username;
    private static String password;
    private static String aesPrivateKey;

    /*
        Static block to initialize all of the values stored within
        the S3 bucket
     */
    static{
        String aesSecretKeyKey = "aes secret key.txt";
        String propertiesKey = "application.properties";
        String bucketName = "ers-api-files";

        AWSCredentials creds = new BasicAWSCredentials("AKIA6RKVIOO76EDFWA44", "igoc22I5nje18FhXQTNlYF7hrh++thnVkxKCgWMw");
        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(creds))
                .withRegion(Regions.US_EAST_2)
                .build();

        logger.info("Trying to connect to S3 Bucket");

        try (S3Object fullObject = s3Client.getObject(new GetObjectRequest(bucketName, aesSecretKeyKey))) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(fullObject.getObjectContent()));
            String line = null;
            aesPrivateKey = reader.readLine();
            logger.info("Grabbed the AESPrivateKey from the S3 bucket");
        } catch (SdkClientException | IOException e) {
            logger.error(e.getMessage());
        }

        try (S3Object fullObject = s3Client.getObject(new GetObjectRequest(bucketName, propertiesKey))) {
            int count = 3;
            BufferedReader reader = new BufferedReader(new InputStreamReader(fullObject.getObjectContent()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                switch(count){
                    case 3:
                        url = line.substring(line.lastIndexOf('=') + 1);
                        break;
                    case 2:
                        username = line.substring(line.lastIndexOf('=') + 1);
                        break;
                    case 1:
                        password = line.substring(line.lastIndexOf('=') + 1);
                        break;
                }
                count--;
            }
            logger.info("Grabbed the database connection information from the S3 bucket");
        } catch (SdkClientException | IOException e) {
            logger.error(e.getMessage());
        }
    }

    /**
     * Gets the URL of the database in the form of a string
     * @return the string representation of the url of the database
     */
    public static String getUrl() {
        return url;
    }

    /**
     * Gets the username used for logging into the database account
     * @return the string representation of the username
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Gets the password for logging into the database account
     * @return the string representation of the password
     */
    public static String getPassword() {
        return password;
    }

    /**
     * Gets the private AES key that encrypts passwords
     * @return the string representation of the private AES key
     */
    public static String getAesPrivateKey() {
        return aesPrivateKey;
    }
}

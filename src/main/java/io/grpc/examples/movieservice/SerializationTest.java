package io.grpc.examples.movieservice;

import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Created by thuy on 26/05/16.
 */
public class SerializationTest {
    private static final Logger logger = Logger.getLogger(MovieServiceClient.class.getName());
    private FileHandler fh = null;

    private List<Movie> movies;
    private MoviesInTheaterResponse response;


    public SerializationTest() throws IOException{
        movies = MovieServiceUtil.parseMovies(MovieServiceUtil.getDefaultMoviesFile());

        // build movie list
        MoviesInTheaterResponse.Builder builder = MoviesInTheaterResponse.newBuilder();
        for (Movie movie : movies) {
            builder.addMovies(movie);
        }

        response = builder.build();

        SimpleDateFormat format = new SimpleDateFormat("MM_dd_yyyy_HHmmss");
        try {
            String dir = Paths.get("").toAbsolutePath().toString() + "//Logging";
            File directory = new File(dir);

            if (!directory.exists()) {
                directory.mkdir();
            }

            fh = new FileHandler(dir + "//Grpc_Serialization_Log_"
                    + format.format(Calendar.getInstance().getTime()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        fh.setLevel(Level.INFO);
        logger.addHandler(fh);
    }

    private static void info(String msg, Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    public void testSerialization(int n)  throws IOException{

        long totalSerializationTime = 0;
        long totalDeserializationTime = 0;

        byte[] bytes =  response.toByteArray();
        info("Serialized Size: {0} bytes", bytes.length);

        for (int i=0; i<n; i++) {
            long startSer = System.nanoTime();
            byte[] serializedData = response.toByteArray();
            long endSer = System.nanoTime();
            totalSerializationTime += endSer - startSer;

            try {
                long startDeser = System.nanoTime();
                MoviesInTheaterResponse deserializedData = MoviesInTheaterResponse.parseFrom(serializedData);
                long endDeser = System.nanoTime();
                totalDeserializationTime += endDeser - startDeser;
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, "Exception while deserializing data: ", e);
                info("Strack trace: {0}", e.getStackTrace());
                return;
            }
        }

        info("Total serialization time: {0}", totalSerializationTime);
        info("Average serialization time: {0}", totalSerializationTime/(float)n);

        info("Total de-serialization time: {0}", totalDeserializationTime);
        info("Average de-serialization time: {0}", totalDeserializationTime/(float)n);

    }

    public static void main(String[] args) throws InterruptedException {
        int iterations = 3; // default
        Scanner scanner = new Scanner(System.in);
        System.out.println("Number of Iterations: ");
        iterations = scanner.nextInt();
        System.out.println("Press any key to conitnue ... ");
        scanner.nextLine();
        info("GRPC SERIALIZATION TESTS: ");

        try {
            SerializationTest testClient = new SerializationTest();

            for (int i=0; i<iterations; i++) {
                info("***********************    ITERATION {0}    ***********************", i);
                testClient.testSerialization(100000);
            }
        } catch (RuntimeException e) {
            logger.log(Level.SEVERE, "Runtime exception: {0}", e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IO exception: {0}", e);
        }

        info("TEST COMPLETED!");
    }
}

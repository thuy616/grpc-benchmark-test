package io.grpc.examples.movieservice;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by thuy on 18/05/16.
 */
public class MovieServiceUtil {

    public static URL getDefaultMoviesFile() {
        return MovieServiceServer.class.getResource("movie_service_db.json");
    }

    /**
     * Parses the JSON input file containing the list of movies
     */
    public static List<Movie> parseMovies(URL file) throws IOException {
        InputStream input = file.openStream();
        try {
            Reader reader = new InputStreamReader(input);
            try {
                MovieDatabase.Builder database = MovieDatabase.newBuilder();
                JsonFormat.parser().merge(reader, database);
                return database.getMoviesList();
            } finally {
                reader.close();
            }
        } finally {
            input.close();
        }
    }

    /**
     * Indicates if the movie exists (i.e. has a valid id)
     * @param movie
     * @return
     */
    public static boolean exists(Movie movie) {
        return movie!=null && movie.getId() != 0;
    }

    public static String deserializeMovie(Movie movie) throws InvalidProtocolBufferException {
       return JsonFormat.printer().print(movie);
    }

}

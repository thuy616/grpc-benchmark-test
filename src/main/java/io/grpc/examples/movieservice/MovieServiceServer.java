package io.grpc.examples.movieservice;

import com.google.protobuf.Empty;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Created by thuy on 18/05/16.
 */
public class MovieServiceServer {

    private static final Logger logger = Logger.getLogger(MovieServiceServer.class.getName());

    private final int port;
    private final Server server;

    public MovieServiceServer(int port) throws IOException {
        this(port, MovieServiceUtil.getDefaultMoviesFile());
    }

    /** Create a Movie server listening on {@code port} using {@code moviesFile} database. */
    public MovieServiceServer(int port, URL moviesFile) throws IOException {
        this(ServerBuilder.forPort(port), port, MovieServiceUtil.parseMovies(moviesFile));
    }

    /** Create a RouteGuide server using serverBuilder as a base and features as data. */
    public MovieServiceServer(ServerBuilder<?> serverBuilder, int port, Collection<Movie> movies) {
        this.port = port;
        server = serverBuilder.addService(new MovieServiceImpl(movies))
                .build();
    }

    /** Start serving requests. */
    public void start() throws IOException {
        server.start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                // Use stderr here since the logger may has been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down");
                MovieServiceServer.this.stop();
                System.err.println("*** server shut down");
            }
        });
    }

    /** Stop serving requests and shutdown resources. */
    public void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main method.  This comment makes the linter happy.
     */
    public static void main(String[] args) throws Exception {
        MovieServiceServer server = new MovieServiceServer(8980);
        server.start();
        server.blockUntilShutdown();
    }

    private static class MovieServiceImpl extends MovieServiceGrpc.AbstractMovieService {
        private final Collection<Movie> movies;

        MovieServiceImpl(Collection<Movie> movies) {
            this.movies = movies;
        }

        @Override
        public void getMovieDetails(MovieRequest request, StreamObserver<Movie> responseObserver) {

            responseObserver.onNext(getMovie(request.getId()));
            responseObserver.onCompleted();
        }

        @Override
        public void listAllMovies(Empty request, StreamObserver<MoviesInTheaterResponse> responseObserver) {
            MoviesInTheaterResponse res = MoviesInTheaterResponse.newBuilder().build();
            res.getMoviesList().addAll(movies);
            responseObserver.onNext(res);
            responseObserver.onCompleted();
        }

        @Override
        public void listAllMoviesServerStreaming(Empty request, StreamObserver<Movie> responseObserver) {
            for (Movie movie: movies) {
                responseObserver.onNext(movie);
            }
            responseObserver.onCompleted();

        }

        @Override
        public void listMoviesServerToClientStreaming(MoviesInTheaterRequest request, StreamObserver<Movie> responseObserver) {
            Movie movie;
            for (long id: request.getIdsList()) {
                movie = getMovie(id);
                responseObserver.onNext(movie);

            }
            responseObserver.onCompleted();
        }

        private Movie getMovie(long id) {
            for (Movie movie: movies) {
                if (movie.getId() == id) {
                    return movie;
                }
            }

            // mo movie found
            return null;
        }
    }
    
}


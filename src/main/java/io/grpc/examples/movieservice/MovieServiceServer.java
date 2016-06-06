package io.grpc.examples.movieservice;

import com.google.protobuf.Empty;
import com.sun.javafx.binding.Logging;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;


/**
 * Created by thuy on 18/05/16.
 */
public class MovieServiceServer {

    private static Logger logger = LoggerFactory.getLogger(MovieServiceServer.class.getName());

    private final int port;
    private final Server server;
    private FileHandler fh = null;

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
        try {
            MovieServiceServer server = new MovieServiceServer(8980);
            server.start();
            server.blockUntilShutdown();
        } catch (RuntimeException e) {
            logger.info(String.format("Runtime exception: {0} ", e.getMessage()));
        } catch (Exception e) {
            logger.info(String.format("Unknown exception: {0} ", e.getMessage()));
        }
    }

    private static class MovieServiceImpl extends MovieServiceGrpc.AbstractMovieService {
        private final Collection<Movie> movies;

        MovieServiceImpl(Collection<Movie> movies) {
            this.movies = movies;
        }

        @Override
        public void getMovieDetails(MovieRequest request, StreamObserver<Movie> responseObserver) {
            try {
                responseObserver.onNext(getMovie(request.getId()));
                responseObserver.onCompleted();
            }catch(RuntimeException e) {
                logger.error(String.format("Runtime exception: {0} ", e.getMessage()));
            }
        }

        @Override
        public void listAllMovies(Empty request, StreamObserver<MoviesInTheaterResponse> basicResponseObserver) {
            try {
                MoviesInTheaterResponse.Builder res = MoviesInTheaterResponse.newBuilder();
                for (Movie movie : movies) {
                    res.addMovies(movie);
                }

                final ServerCallStreamObserver responseObserver = (ServerCallStreamObserver)basicResponseObserver;
                responseObserver.setOnReadyHandler(new Runnable() {
                    @Override
                    public void run() {
                        while(responseObserver.isReady()) {
                            responseObserver.onNext(res.build());
                            responseObserver.onCompleted();
                        }
                    }
                });
            }catch(RuntimeException e) {
                logger.info(String.format("####### Runtime exception: {0} ", e.getMessage()));
            }
        }

        @Override
        public void listAllMoviesServerStreaming(Empty request, StreamObserver<Movie> basicResponseObserver) {
            final ServerCallStreamObserver responseObserver = (ServerCallStreamObserver)basicResponseObserver;
            responseObserver.setOnReadyHandler(new Runnable() {
                @Override
                public void run() {
                    while(responseObserver.isReady()) {
                        for (Movie movie: movies) {
                            responseObserver.onNext(movie);
                        }
                        responseObserver.onCompleted();
                    }
                }
            });

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


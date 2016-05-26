package io.grpc.examples.movieservice;

import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Created by thuy on 18/05/16.
 */
public class MovieServiceClient {
    private static final Logger logger = Logger.getLogger(MovieServiceClient.class.getName());
    private FileHandler fh = null;

    private final ManagedChannel channel;
    private final MovieServiceGrpc.MovieServiceStub asyncStub;
    private final MovieServiceGrpc.MovieServiceFutureStub syncNonBlockingStub;
    private final MovieServiceGrpc.MovieServiceBlockingStub blockingStub;

    public static long start;
    public static long end;

    public static int count;
    public int counter;

    public MovieServiceClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));

        SimpleDateFormat format = new SimpleDateFormat("MM_dd_yyyy_HHmmss");
        try {
            String dir = Paths.get("").toAbsolutePath().toString() + "//Logging";
            File directory = new File(dir);

            if (!directory.exists()) {
                directory.mkdir();
            }

            fh = new FileHandler(dir + "//Client_Log_"
                    + format.format(Calendar.getInstance().getTime()) + ".log");
        } catch (Exception e) {
            e.printStackTrace();
        }

        fh.setFormatter(new SimpleFormatter());
        fh.setLevel(Level.INFO);
        logger.addHandler(fh);
    }

    public MovieServiceClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = MovieServiceGrpc.newBlockingStub(channel);
        syncNonBlockingStub = MovieServiceGrpc.newFutureStub(channel);
        asyncStub = MovieServiceGrpc.newStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private static void info(String msg, Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private Movie getMovieBlockingStub(MovieRequest request) {
        Movie movie;
        try {
            movie = blockingStub.getMovieDetails(request);
            return movie;
        } catch (StatusRuntimeException e) {
            info(String.format("RPC failed: {0} - {1}", e.getMessage(), e.getStackTrace()));
            return null;
        }
    }

    private MoviesInTheaterResponse getAllMoviesUnaryBlockingStub(Empty request, int count) {
        MoviesInTheaterResponse res = null;
        try {
            long start = System.nanoTime();
            for (int i = 0; i < count; i++) {
                res = blockingStub.listAllMovies(request);
            }
            long end = System.nanoTime();
            logTransmissionTime(end - start);
        } catch (StatusRuntimeException e) {
            info("RPC failed: {0} - {1}", e.getMessage(), e.getStackTrace());
            return null;
        }

        return res;
    }

    private void getAllMoviesServerStreamingBlockingStub(Empty request, int count) {

        MoviesInTheaterResponse.Builder builder = MoviesInTheaterResponse.newBuilder();
        Iterator<Movie> movies = null;
        long start = System.nanoTime();
        for (int i = 0; i < count; i++) {
            try {
                movies = blockingStub.listAllMoviesServerStreaming(request);

            } catch (StatusRuntimeException e) {
                info(String.format("RPC failed: {0} - {1}", e.getMessage(), e.getStackTrace()));
                return;
            }
            StringBuilder responseLog = new StringBuilder("Result: ");
            while (movies.hasNext()) {
                Movie m = movies.next();
                responseLog.append(m); // consume response
            }
        }
        long end = System.nanoTime();
        logTransmissionTime(end - start);

    }

    private void allMoviesServerStreamingAsyncStub(Empty request, AtomicReference<MoviesInTheaterResponse.Builder> builder) {

        MoviesInTheaterResponse.Builder res = builder.get();
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<Movie> responseObserver = new StreamObserver<Movie>() {
            @Override
            public void onNext(Movie value) {
//                info("Consuming response - Get Movie with title: {0}", value.getTitle());
                res.addMovies(value);
            }

            @Override
            public void onError(Throwable t) {
                Status status = Status.fromThrowable(t);
                info(String.format("getAllMoviesServerStreamingAsyncStub Failed: {0}", status));
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
//                info("Finsihed getAllMoviesServerStreamingAsyncStub ResponseObserver completed");
                finishLatch.countDown();
                counter = counter + 1;
//                info("processed {0}", counter);
                if (counter == count) {
                    end = System.nanoTime();
                    info("FINISH SET --- ");
                    logTransmissionTime(end - start);
                    // reset
                    counter = 0;
                    count = 0;
                }
            }
        };
//        info("MoviesInTheaterRequest sent");

        asyncStub.listAllMoviesServerStreaming(request, responseObserver);
        while (finishLatch.getCount() != 0) {
            try {
                finishLatch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                logger.severe(e.getMessage() + " - " + e.getStackTrace());
            }
        }
    }


    /**
     * SCENARIO 3
     * ServerStreaming Async Client
     */
    public void benchmarkServerStreamingAsync(int count) {
        Empty request = Empty.getDefaultInstance();
        AtomicReference<MoviesInTheaterResponse.Builder> builder = new AtomicReference<MoviesInTheaterResponse.Builder>(MoviesInTheaterResponse.newBuilder());
//        long totalTransmissionTime = 0;
        start = System.nanoTime();
        this.count = count;
        for (int i = 0; i < count; i++) {
//            info("Request #{0} sent...", i);
            builder.get().clearMovies();
            this.allMoviesServerStreamingAsyncStub(request, builder);
        }
        while (this.count != 0) {
            // block main thread to wait for all requests to be processed
        }

        info("Reponse serialized size: {0}", builder.get().build().getSerializedSize());

    }


    /**
     * RUN ALL TESTS
     */
    private void runTests(int mode, int calls, int iterations) {

        info("===========     START : {0} CALLS   =============", calls);

        for (int i = 0; i < iterations; i++) {
            switch (mode) {
                case 1:
                    info("### TEST SCENARIO 1 ### UNARY ### BLOCKING ###");
                    getAllMoviesUnaryBlockingStub(Empty.getDefaultInstance(), calls);
                    break;
                case 2:
                    info("### TEST SCENARIO 2 ### SERVER STREAMING ### BLOCKING ###");
                    getAllMoviesServerStreamingBlockingStub(Empty.getDefaultInstance(), calls);
                    break;
                case 3:
                    info("### TEST SCENARIO 3 ### SERVER STREAMING ### ASYNC ### 1 CALL");
                    benchmarkServerStreamingAsync(1);
                    break;
                default:
                    info("Wrong input");
                    break;
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        int iterations = 10; // default
        Scanner scanner = new Scanner(System.in);
        System.out.println("Usage: 1 = Unary Blocking, 2 = Streaming Blocking, 3 = Streaming non-blocking");
        int mode = scanner.nextInt();

        System.out.println("Press any key to continue ... ");
        scanner.nextLine();

        info("BENCHMARK TEST CLIENT: ");

        MovieServiceClient client = new MovieServiceClient("localhost", 8980);
        try {
            client.runTests(mode, 1, iterations);
            client.runTests(mode, 1000, iterations);
            client.runTests(mode, 10000, iterations);
            client.runTests(mode, 100000, iterations);
        } catch (RuntimeException e) {
            logger.severe("" + e.getMessage() + " - " + e.getStackTrace());
        } finally {
            client.shutdown();
        }

        info("TEST COMPLETED!");
    }

    private static void logTransmissionTime(long value) {
        info("{0}", value, value / (double) 1000000);
    }


}

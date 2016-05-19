package io.grpc.examples.movieservice;

import com.google.common.primitives.Longs;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.protobuf.Empty;
import com.google.protobuf.InvalidProtocolBufferException;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import io.grpc.stub.StreamObserver;


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
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null;
        }
    }

    private MoviesInTheaterResponse getAllMoviesUnaryBlockingStub(Empty request, int count) {
        MoviesInTheaterResponse res = null;
        try {
            long start = System.nanoTime();
            for (int i=0; i<count; i++) {
                res = blockingStub.listAllMovies(request);
            }
            long end = System.nanoTime();
            logTransmissionTime(end-start);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null;
        }
        info("Response Serialized Size: {0}", res.getSerializedSize());

        return res;
    }

    private void getAllMoviesServerStreamingBlockingStub(Empty request, int count) {
//        long[] ids = new long[] {
//                271110, 246655, 293660, 209112, 153518, 303858, 332411, 325133, 383121, 290250,
//                398051, 254302, 301608, 360365, 241259, 68735, 291805, 308531, 244786, 278,
//                238, 157336, 140420, 77338, 264644, 240, 12477, 637, 293299, 550, 424, 155,
//                118340, 680
//        };
        MoviesInTheaterResponse.Builder builder = MoviesInTheaterResponse.newBuilder();
        Iterator<Movie> movies = null;
        try {
            long start = System.nanoTime();
            for (int i=0; i<count; i++) {
                movies = blockingStub.listAllMoviesServerStreaming(request);
            }
            long end = System.nanoTime();
            logTransmissionTime(end-start);
            while(movies.hasNext()) {
                builder.addMovies(movies.next());
            }
        } catch (StatusRuntimeException e) {
            logger.log(Level.SEVERE, "RPC failed: {0}", e.getStatus());
            return;
        }
        info("Response Deserialized Size: {0}", builder.build().getSerializedSize());

    }

    private long allMoviesServerStreamingAsyncStub(Empty request, AtomicReference<MoviesInTheaterResponse.Builder> builder) {
        final long[] startTime = {0};
        final long[] endTime = {0};
        final long[] transmissionTime = {0};
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
                logger.log(Level.WARNING, "getAllMoviesServerStreamingAsyncStub Failed: {0}", status);
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
//                info("Finsihed getAllMoviesServerStreamingAsyncStub ResponseObserver completed");
                endTime[0] = System.nanoTime();
                transmissionTime[0] = endTime[0] - startTime[0];
//                info("Transmission time: {0}", transmissionTime);
                finishLatch.countDown();
            }
        };
//        info("MoviesInTheaterRequest sent");
        startTime[0] = System.nanoTime();
        asyncStub.listAllMoviesServerStreaming(request, responseObserver);
        while(finishLatch.getCount()!=0) {
            try {
                finishLatch.await(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (finishLatch.getCount() == 0) {
//            info("finishLatch count == 0");
            return transmissionTime[0];
        }
        return transmissionTime[0];
    }


    /**
     * SCENARIO 3
     * ServerStreaming Async Client
     */
    public void benchmarkServerStreamingBlocking(int count) {
        Empty request = Empty.getDefaultInstance();
        AtomicReference<MoviesInTheaterResponse.Builder> builder = new AtomicReference<MoviesInTheaterResponse.Builder>(MoviesInTheaterResponse.newBuilder());
        long totalTransmissionTime = 0;
        for (int i=0; i<count; i++) {
            builder.get().clearMovies();
            totalTransmissionTime += this.allMoviesServerStreamingAsyncStub(request, builder);
        }
        logTransmissionTime(totalTransmissionTime);
        info("Reponse serialized size: {0}", builder.get().build().getSerializedSize());

    }


    /**
     * RUN ALL TESTS
     */
    private void runAllTests() {

        info("===========     START     =============");

        info("");
        info("### TEST 1 ### UNARY ### BLOCKING ### 1 CALL");
        getAllMoviesUnaryBlockingStub(Empty.getDefaultInstance(), 1);

//        info("### TEST 2 ### UNARY ### BLOCKING ### 1,000 CALLS");
//        getAllMoviesUnaryBlockingStub(Empty.getDefaultInstance(), 1000);
//
//        info("### TEST 3 ### UNARY ### BLOCKING ### 10,000 CALLS");
//        getAllMoviesUnaryBlockingStub(Empty.getDefaultInstance(), 10000);
//
//        info("### TEST 4 ### UNARY ### BLOCKING ### 100,000 CALLS");
//        getAllMoviesUnaryBlockingStub(Empty.getDefaultInstance(), 100000);

        info("");
        info("### TEST 5 ### SERVER STREAMING ### BLOCKING ### 1 CALL");
        getAllMoviesServerStreamingBlockingStub(Empty.getDefaultInstance(), 1);

//        info("### TEST 6 ### SERVER STREAMING ### BLOCKING ### 1,000 CALL");
//        getAllMoviesServerStreamingBlockingStub(Empty.getDefaultInstance(), 1000);
//
//        info("### TEST 7 ### SERVER STREAMING ### BLOCKING ### 10,000 CALL");
//        getAllMoviesServerStreamingBlockingStub(Empty.getDefaultInstance(), 10000);
//
//        info("### TEST 8 ### SERVER STREAMING ### BLOCKING ### 100,000 CALL");
//        getAllMoviesServerStreamingBlockingStub(Empty.getDefaultInstance(), 100000);

        info("");
        info("### TEST 9 ### SERVER STREAMING ### ASYNC ### 1 CALL");
        benchmarkServerStreamingBlocking(1);

//        info("### TEST 10 ### SERVER STREAMING ### ASYNC ### 1,000 CALL");
//        benchmarkServerStreamingBlocking(1000);
//
//        info("### TEST 11 ### SERVER STREAMING ### ASYNC ### 10,000 CALL");
//        benchmarkServerStreamingBlocking(10000);
//
//        info("### TEST 12 ### SERVER STREAMING ### ASYNC ### 100,000 CALL");
//        benchmarkServerStreamingBlocking(100000);

    }


    public static void main(String[] args) throws InterruptedException {
        info("BENCHMART TEST CLIENT: ");
        List<Movie> movies;

        try {
            movies = MovieServiceUtil.parseMovies(MovieServiceUtil.getDefaultMoviesFile());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        int testCase = 0;

        if (args.length > 0) {
            try {
                testCase = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        MovieServiceClient client = new MovieServiceClient("localhost", 8980);
        try {
            switch (testCase) {
                default:
                    client.runAllTests();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.shutdown();
        }

        info("TEST COMPLETED!");
    }

    private static void logTransmissionTime(long value) {
        info("Transmission time: {0} nanoseconds ===============  {1} milliseconds ", value, value/(double)1000000);

    }


}

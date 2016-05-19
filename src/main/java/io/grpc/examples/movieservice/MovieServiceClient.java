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

    private MoviesInTheaterResponse getAllMoviesBlockingStub(Empty request) {
        MoviesInTheaterResponse res = null;
        try {
            res = blockingStub.listAllMovies(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return null;
        }
        return res;
    }

    private void getMoviesServerStreamingAsyncStub(MoviesInTheaterRequest request, AtomicReference<MoviesInTheaterResponse> re) {
//        long[] ids = new long[] {
//                271110, 246655, 293660, 209112, 153518, 303858, 332411, 325133, 383121, 290250,
//                398051, 254302, 301608, 360365, 241259, 68735, 291805, 308531, 244786, 278,
//                238, 157336, 140420, 77338, 264644, 240, 12477, 637, 293299, 550, 424, 155,
//                118340, 680
//        };
        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<Movie> responseObserver = new StreamObserver<Movie>() {
            @Override
            public void onNext(Movie value) {
//                info("Consuming response - Get Movie with title: {0}", value.getTitle());
                re.get().getMoviesList().add(value);
            }

            @Override
            public void onError(Throwable t) {
                Status status = Status.fromThrowable(t);
                logger.log(Level.WARNING, "getAllMoviesServerStreamingAsyncStub Failed: {0}", status);
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                info("Finsihed getAllMoviesServerStreamingAsyncStub");
                finishLatch.countDown();
            }
        };
        info("MoviesInTheaterRequest sent");
        asyncStub.listMoviesServerToClientStreaming(request, responseObserver);
        if (finishLatch.getCount() == 0) {
            return;
        }

    }

    private void allMoviesServerStreamingAsyncStub(Empty request, AtomicReference<MoviesInTheaterResponse> re) {

        final CountDownLatch finishLatch = new CountDownLatch(1);
        StreamObserver<Movie> responseObserver = new StreamObserver<Movie>() {
            @Override
            public void onNext(Movie value) {
//                info("Consuming response - Get Movie with title: {0}", value.getTitle());
                re.get().getMoviesList().add(value);
            }

            @Override
            public void onError(Throwable t) {
                Status status = Status.fromThrowable(t);
                logger.log(Level.WARNING, "getAllMoviesServerStreamingAsyncStub Failed: {0}", status);
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                info("Finsihed getAllMoviesServerStreamingAsyncStub");
                finishLatch.countDown();
            }
        };
        info("MoviesInTheaterRequest sent");
        asyncStub.listAllMoviesServerStreaming(request, responseObserver);
        if (finishLatch.getCount() == 0) {
            return;
        }

    }

    /** TEST CASE 1 **/
    public void benchmarkUnaryBlocking1() {
        Empty request = Empty.newBuilder().build();
        info("Request serialized size: {0}", request.getSerializedSize());

        long startTime = System.nanoTime();
        MoviesInTheaterResponse res = this. getAllMoviesBlockingStub(request);
        long endTime = System.nanoTime();

        info("Transmission time: {0}", endTime - startTime);
        info("Response serialized size: {0}", res.getSerializedSize());
        //TODO: response deserialization time
        // deserialize Movie to json
//        try {
//            long startDeserialization = System.nanoTime();
//            String json = MovieServiceUtil.deserializeMovie(movie);
//            long endDeserialization = System.nanoTime();
//            info("Response deserialization time: {0}", endDeserialization - startDeserialization);
////            info("Response deserialized size: {0}", ObjectSizeAgent.getObjectSize(json));
//        } catch (InvalidProtocolBufferException e) {
//            logger.log(Level.WARNING, "InvalidProtocolBufferException: {0}", e.getMessage());
//        }
    }

    /** TEST CASE 2 **/
    public void benchmarkUnaryBlocking1000() {

        Empty request = Empty.newBuilder().build();
        info("Request serialized size: {0}", request.getSerializedSize());

        long startTime = System.nanoTime();
        MoviesInTheaterResponse res = MoviesInTheaterResponse.newBuilder().build();
        for (int i=0; i<1000; i++) {
            res = this. getAllMoviesBlockingStub(request);
        }
        long endTime = System.nanoTime();

        info("Benchmark 1000: {0}", endTime - startTime);
        info("Response serialized size: {0}", res.getSerializedSize());

        // TODO: response deserialization time
    }


    /**
     * Test case 3
     */
    public void benchmarkUnaryBlocking10000() {
        Empty request = Empty.newBuilder().build();
        info("Request serialized size: {0}", request.getSerializedSize());

        long startTime = System.nanoTime();
        MoviesInTheaterResponse res = MoviesInTheaterResponse.newBuilder().build();
        for (int i=0; i<10000; i++) {
            res = this. getAllMoviesBlockingStub(request);
        }
        long endTime = System.nanoTime();

        info("Benchmark 1000: {0}", endTime - startTime);
        info("Response serialized size: {0}", res.getSerializedSize());

        // TODO: response deserialization time
    }

    /**
     * Test case 4
     */
    public void benchmarkUnaryBlocking100000() {
        Empty request = Empty.newBuilder().build();
        info("Request serialized size: {0}", request.getSerializedSize());

        long startTime = System.nanoTime();
        MoviesInTheaterResponse res = MoviesInTheaterResponse.newBuilder().build();
        for (int i=0; i<100000; i++) {
            res = this. getAllMoviesBlockingStub(request);
        }
        long endTime = System.nanoTime();

        info("Benchmark 1000: {0}", endTime - startTime);
        info("Response serialized size: {0}", res.getSerializedSize());

        // TODO: response deserialization time
    }

    /** TEST CASE 5 **/
    public void benchmarkAsync1() {
        Empty request = Empty.getDefaultInstance();

        info("Request serialized size: {0}", request.getSerializedSize());
        AtomicReference<MoviesInTheaterResponse> re = new AtomicReference<MoviesInTheaterResponse>(MoviesInTheaterResponse.getDefaultInstance());
        long startTime = System.nanoTime();
        this.allMoviesServerStreamingAsyncStub(request, re);
        long endTime = System.nanoTime();

        info("Transmission time {0}:", endTime - startTime);
        info("Reponse serialized size: {0}", re.get().getSerializedSize());
    }

    /** TEST CASE 6 **/
    public void benchmarkAsync1000() {
        Empty request = Empty.getDefaultInstance();

        info("Request serialized size: {0}", request.getSerializedSize());
        AtomicReference<MoviesInTheaterResponse> re = new AtomicReference<MoviesInTheaterResponse>(MoviesInTheaterResponse.getDefaultInstance());
        long startTime = System.nanoTime();
        for (int i=0; i<1000; i++) {
            re.get().getMoviesList().clear();
            this.allMoviesServerStreamingAsyncStub(request, re);
        }
        long endTime = System.nanoTime();

        info("Transmission time {0}:", endTime - startTime);
        info("Reponse serialized size: {0}", re.get().getSerializedSize());
    }

    /** TEST CASE 7 **/
    public void benchmarkAsync10000() {
        Empty request = Empty.getDefaultInstance();

        info("Request serialized size: {0}", request.getSerializedSize());
        AtomicReference<MoviesInTheaterResponse> re = new AtomicReference<MoviesInTheaterResponse>(MoviesInTheaterResponse.getDefaultInstance());
        long startTime = System.nanoTime();
        for (int i=0; i<10000; i++) {
            re.get().getMoviesList().clear();
            this.allMoviesServerStreamingAsyncStub(request, re);
        }
        long endTime = System.nanoTime();

        info("Transmission time {0}:", endTime - startTime);
        info("Reponse serialized size: {0}", re.get().getSerializedSize());
    }

    /** TEST CASE 8 **/
    public void benchmarkAsync100000() {
        Empty request = Empty.getDefaultInstance();

        info("Request serialized size: {0}", request.getSerializedSize());
        AtomicReference<MoviesInTheaterResponse> re = new AtomicReference<MoviesInTheaterResponse>(MoviesInTheaterResponse.getDefaultInstance());
        long startTime = System.nanoTime();
        for (int i=0; i<100000; i++) {
            re.get().getMoviesList().clear();
            this.allMoviesServerStreamingAsyncStub(request, re);
        }
        long endTime = System.nanoTime();

        info("Transmission time {0}:", endTime - startTime);
        info("Reponse serialized size: {0}", re.get().getSerializedSize());
    }

    /**
     * RUN ALL TESTS
     */
    private void runAllTests() {

        /** SCENARIO 1 **/

        info("### TEST 1 ### UNARY ### BLOCKING ### 1 CALL");
        benchmarkUnaryBlocking1();

        info("### TEST 2 ### UNARY ### BLOCKING ### 1,000 CALLS");
        benchmarkUnaryBlocking1000();

        info("### TEST 3 ### UNARY ### BLOCKING ### 10,000 CALLS");
        benchmarkUnaryBlocking10000();

        info("### TEST 4 ### UNARY ### BLOCKING ### 100,000 CALLS");
        benchmarkUnaryBlocking100000();

        info("### TEST 5 ### SERVER STREAMING ### ASYNC ### 1 CALL");
        benchmarkAsync1();

        info("### TEST 6 ### SERVER STREAMING ### ASYNC ### 1000 CALL");
        benchmarkAsync1000();

        info("### TEST 7 ### SERVER STREAMING ### ASYNC ### 10000 CALL");
        benchmarkAsync10000();

        info("### TEST 8 ### SERVER STREAMING ### ASYNC ### 100000 CALL");
        benchmarkAsync100000();
    }




    public static void main(String[] args) throws InterruptedException {
        info("BENCHMART TEST CLIENT: ");
        List<Movie> movies;

        try {
            movies = MovieServiceUtil.parseMovies(MovieServiceUtil.getDefaultMoviesFile());
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }

        int testCase = 0;

        if (args.length>0) {
            try {
                testCase = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        MovieServiceClient client = new MovieServiceClient("localhost", 8980);
        try {
            switch (testCase) {
                case 1:
                    info("### TEST 1 ### UNARY ### BLOCKING ### 1 CALL");
                    client.benchmarkUnaryBlocking1();
                    break;
                case 2:
                    info("### TEST 2 ### UNARY ### BLOCKING ### 1,000 CALLS");
                    client.benchmarkUnaryBlocking1000();
                    break;
                case 3:
                    info("### TEST 3 ### UNARY ### BLOCKING ### 10,000 CALLS");
                    client.benchmarkUnaryBlocking10000();
                    break;
                case 4:
                    info("### TEST 4 ### UNARY ### BLOCKING ### 100,000 CALLS");
                    client.benchmarkUnaryBlocking100000();
                    break;
                default:
                    client.runAllTests();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        info("TEST COMPLETED!");
    }




}

package io.grpc.examples.movieservice;

import com.google.common.util.concurrent.SettableFuture;
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
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
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

    private final BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<>();
    private static final ExecutorService pool = Executors.newFixedThreadPool(200);
    private WorkerConsumer consumer;
    //    private List<Future<Integer>> futures = new ArrayList<>();
    private final CompletionService<Integer> completionService = new ExecutorCompletionService<>(pool);
    private AtomicInteger counter = new AtomicInteger(0);

    public MovieServiceClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true));

        SimpleDateFormat format = new SimpleDateFormat("MM_dd_yyyy_HHmmss");
        try {
            String dir = Paths.get("").toAbsolutePath().toString() + "//Logging";
            File directory = new File(dir);

            if (!directory.exists()) {
                directory.mkdir();
            }

            fh = new FileHandler(dir + "//Grpc_Client_Log_"
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
        consumer = new WorkerConsumer(blockingQueue);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    private static class WorkerConsumer implements Runnable {
        protected BlockingQueue q = null;

        public WorkerConsumer(BlockingQueue q) {
            this.q = q;
        }

        public void run() {
            try {
                Runnable task = (Runnable) q.take();
                pool.execute(task);
//                info("[Worker] Consumed, q size {0}", q.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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
            info(String.format("RPC failed: {0} - {1}", e.getMessage()));
            return null;
        }
    }

    /**
     * Simple RPC call, unary from server
     * returns all movies wrapped in a single MoviesInTheaterResponse object
     *
     * @param request
     * @param count
     * @return
     */
    private long getAllMoviesUnaryBlockingStub(Empty request, int count) {
        MoviesInTheaterResponse res = null;
        long elapsed = 0;
        try {
            long start = System.nanoTime();
            for (int i = 0; i < count; i++) {
                res = blockingStub.listAllMovies(request);
            }
            for (Movie m : res.getMoviesList()) {
                // consume
            }
            long end = System.nanoTime();
            elapsed = end - start;
            logTransmissionTime(elapsed);
        } catch (StatusRuntimeException e) {
            info("RPC failed: {0} - {1}", e.getMessage());
            return 0;
        }

        return elapsed;
    }

    /**
     * Calling a server-side streaming RPC, very similar to simple RPC,
     * except instead of returning a single MoviesInTheaterResponse object,
     * the method returns an Iterator that the client can use to read all the returned Movies
     *
     * @param request
     * @return
     */
    private void allMoviesServerStreamingBlockingStub(Empty request) {

        Iterator<Movie> iter = blockingStub.listAllMoviesServerStreaming(request);

        // silently consume to response, we don't need actual consumption for benchmarking
        completionService.submit(new Callable<Integer>() {
            public Integer call() {
                while (iter.hasNext()) {
                    Movie m = iter.next();
                }
                return 1;
            }
        });
    }

    private long benchmarkServerStreamingBlockingStub(Empty request, int count) {
        long start = System.nanoTime();
        // send all N requests
        for (int i = 0; i < count; i++) {
            allMoviesServerStreamingBlockingStub(request);
        }
        long end = System.nanoTime();
        logTransmissionTime(end - start);

        // wait for N requests to be consumed
        int consumed = 0;
        boolean errors = false;
        info("Waiting to consumed results before sending next batch of requests");
        while (consumed < count && !errors) {
            try {
                Future<Integer> futures = completionService.take();
                int isDone = futures.get();
                consumed++;
            } catch (InterruptedException e) {
                errors = true;
                e.printStackTrace();
            } catch (ExecutionException e) {
                errors = true;
                e.printStackTrace();
            }
        }

        return end - start;
    }

    private long allMoviesServerStreamingNonBlocking(Empty request) {
//        final SettableFuture<Void> finishFuture = SettableFuture.create();
        final CountDownLatch finishLatch = new CountDownLatch(1);
//        List<Movie> movieResponses = new ArrayList<>();
        StreamObserver<Movie> responseObserver = new StreamObserver<Movie>() {
            @Override
            public void onNext(Movie value) {
                // consume movie on by one when it arrives
                // do something with the movie here
//                info("consumed");
            }

            @Override
            public void onError(Throwable t) {
                Status status = Status.fromThrowable(t);
                logger.log(Level.WARNING, "RecordRoute Failed: {0}", status);
                finishLatch.countDown();
            }

            @Override
            public void onCompleted() {
                info("Finished Get Movies Async Server Streaming request #{0}", counter.incrementAndGet());
                finishLatch.countDown();
            }
        };
        long start = System.nanoTime();
        asyncStub.listAllMoviesServerStreaming(request, responseObserver);
        long end = System.nanoTime();
        //Receiving happens asynchronously
        try {
            finishLatch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return end - start;
    }

    private long benchmarkServerStreamingAsyncStub(Empty request, int count) {
        long transmissionTime = 0;
//        long start = System.nanoTime();
        // send all N requests
        for (int i = 0; i < count; i++) {
            transmissionTime += allMoviesServerStreamingNonBlocking(request);
        }
//        long end = System.nanoTime();
        logTransmissionTime(transmissionTime);
//        logger.log(Level.INFO, "Total time including setting up response stream observer: {0}", end - start);
        return transmissionTime;
    }


    /**
     * RUN ALL TESTS
     */
    private void runTests(int mode, int calls, int iterations) throws InterruptedException {

        info("===========     START : {0} CALLS   =============", calls);
        long totalElapsed = 0;
        for (int i = 0; i < iterations; i++) {
            switch (mode) {
                case 1:

                    info("===========     ITERATION : {0}    =============", i);
                    info("### TEST SCENARIO 1 ### UNARY ### BLOCKING ###");
                    totalElapsed += getAllMoviesUnaryBlockingStub(Empty.getDefaultInstance(), calls);
                    break;
                case 2:
                    info("===========     ITERATION : {0}    =============", i);
                    info("### TEST SCENARIO 2 ### SERVER STREAMING ### BLOCKING ###");
                    totalElapsed += benchmarkServerStreamingBlockingStub(Empty.getDefaultInstance(), calls);
                    break;
                case 3:
                    info("===========     ITERATION : {0}    =============", i);
                    info("### TEST SCENARIO 2 ### SERVER STREAMING ### NON-BLOCKING ###");
                    totalElapsed += benchmarkServerStreamingAsyncStub(Empty.getDefaultInstance(), calls);
                    break;
                default:
                    info("Wrong input");
                    break;
            }
            Thread.sleep(10000);
        }

        info("AVERAGE TIME for mode {0} - {1} calls: {2}", mode, calls, totalElapsed / (float) iterations);
        Thread.sleep(60 * 1000);

    }

    public static void main(String[] args) {
        int iterations = 5; // default
        Scanner scanner = new Scanner(System.in);
        MovieServiceClient client = new MovieServiceClient("localhost", 8980);
        while (scanner.nextLine() != "q") {
            System.out.println("Enter mode: 1 = sync, 2 = async-blocking client, 3 = async - nonblocking client");
            int mode = scanner.nextInt();

            info("BENCHMARK TEST CLIENT: ");

            try {
                info("IGNORE -------------- Force Handshaking request time ----------- ");
                client.runTests(mode, 1, iterations);
                info("IGNORE -------------- END  Handshaking request time ----------- ");
                client.runTests(mode, 1, iterations);
                client.runTests(mode, 1000, iterations);
                client.runTests(mode, 10000, iterations);
                client.runTests(mode, 100000, 1);


            } catch (RuntimeException e) {
                logger.severe("" + e.getMessage());
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            info("TEST COMPLETED!");

            try {
                client.shutdown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void logTransmissionTime(long value) {
        info("Transmission time: {0}", value, value / (double) 1000000);
    }


}

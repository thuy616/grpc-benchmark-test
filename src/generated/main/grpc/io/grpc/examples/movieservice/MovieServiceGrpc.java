package io.grpc.examples.movieservice;

import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;

/**
 * <pre>
 * The greeting service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 0.14.0)",
    comments = "Source: movie_service.proto")
public class MovieServiceGrpc {

  private MovieServiceGrpc() {}

  public static final String SERVICE_NAME = "movieservice.MovieService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<io.grpc.examples.movieservice.MovieRequest,
      io.grpc.examples.movieservice.Movie> METHOD_GET_MOVIE_DETAILS =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "movieservice.MovieService", "GetMovieDetails"),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.movieservice.MovieRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.movieservice.Movie.getDefaultInstance()));
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      io.grpc.examples.movieservice.MoviesInTheaterResponse> METHOD_LIST_ALL_MOVIES =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.UNARY,
          generateFullMethodName(
              "movieservice.MovieService", "ListAllMovies"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.google.protobuf.Empty.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.movieservice.MoviesInTheaterResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<com.google.protobuf.Empty,
      io.grpc.examples.movieservice.Movie> METHOD_LIST_ALL_MOVIES_SERVER_STREAMING =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
          generateFullMethodName(
              "movieservice.MovieService", "ListAllMoviesServerStreaming"),
          io.grpc.protobuf.ProtoUtils.marshaller(com.google.protobuf.Empty.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.movieservice.Movie.getDefaultInstance()));
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<io.grpc.examples.movieservice.MoviesInTheaterRequest,
      io.grpc.examples.movieservice.Movie> METHOD_LIST_MOVIES_SERVER_TO_CLIENT_STREAMING =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING,
          generateFullMethodName(
              "movieservice.MovieService", "ListMoviesServerToClientStreaming"),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.movieservice.MoviesInTheaterRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.movieservice.Movie.getDefaultInstance()));
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<io.grpc.examples.movieservice.MovieRequest,
      io.grpc.examples.movieservice.MoviesInTheaterResponse> METHOD_LIST_MOVIES_CLIENT_TO_SERVER_STREAMING =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.CLIENT_STREAMING,
          generateFullMethodName(
              "movieservice.MovieService", "ListMoviesClientToServerStreaming"),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.movieservice.MovieRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.movieservice.MoviesInTheaterResponse.getDefaultInstance()));
  @io.grpc.ExperimentalApi
  public static final io.grpc.MethodDescriptor<io.grpc.examples.movieservice.MovieRequest,
      io.grpc.examples.movieservice.Movie> METHOD_LIST_MOVIES_BIDIRECTIONAL_STREAMING =
      io.grpc.MethodDescriptor.create(
          io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING,
          generateFullMethodName(
              "movieservice.MovieService", "ListMoviesBidirectionalStreaming"),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.movieservice.MovieRequest.getDefaultInstance()),
          io.grpc.protobuf.ProtoUtils.marshaller(io.grpc.examples.movieservice.Movie.getDefaultInstance()));

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static MovieServiceStub newStub(io.grpc.Channel channel) {
    return new MovieServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static MovieServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new MovieServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary and streaming output calls on the service
   */
  public static MovieServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new MovieServiceFutureStub(channel);
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static interface MovieService {

    /**
     * <pre>
     * Simple Unary RPC
     * Get details for a movie
     * </pre>
     */
    public void getMovieDetails(io.grpc.examples.movieservice.MovieRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver);

    /**
     * <pre>
     * Simple Unary RPC
     * Get a list of movies
     * big result
     * </pre>
     */
    public void listAllMovies(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MoviesInTheaterResponse> responseObserver);

    /**
     */
    public void listAllMoviesServerStreaming(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver);

    /**
     * <pre>
     * A server-to-client Streaming RPC
     * accepts a request
     * Obtains the list of movies
     * Results are streamed rather than returned at once
     * </pre>
     */
    public void listMoviesServerToClientStreaming(io.grpc.examples.movieservice.MoviesInTheaterRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver);

    /**
     * <pre>
     * A client-to-server Streaming RPC
     * returns all results at once when the requests are all received
     * </pre>
     */
    public io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MovieRequest> listMoviesClientToServerStreaming(
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MoviesInTheaterResponse> responseObserver);

    /**
     */
    public io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MovieRequest> listMoviesBidirectionalStreaming(
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver);
  }

  @io.grpc.ExperimentalApi
  public static abstract class AbstractMovieService implements MovieService, io.grpc.BindableService {

    @java.lang.Override
    public void getMovieDetails(io.grpc.examples.movieservice.MovieRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_GET_MOVIE_DETAILS, responseObserver);
    }

    @java.lang.Override
    public void listAllMovies(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MoviesInTheaterResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_ALL_MOVIES, responseObserver);
    }

    @java.lang.Override
    public void listAllMoviesServerStreaming(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_ALL_MOVIES_SERVER_STREAMING, responseObserver);
    }

    @java.lang.Override
    public void listMoviesServerToClientStreaming(io.grpc.examples.movieservice.MoviesInTheaterRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LIST_MOVIES_SERVER_TO_CLIENT_STREAMING, responseObserver);
    }

    @java.lang.Override
    public io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MovieRequest> listMoviesClientToServerStreaming(
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MoviesInTheaterResponse> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_LIST_MOVIES_CLIENT_TO_SERVER_STREAMING, responseObserver);
    }

    @java.lang.Override
    public io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MovieRequest> listMoviesBidirectionalStreaming(
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver) {
      return asyncUnimplementedStreamingCall(METHOD_LIST_MOVIES_BIDIRECTIONAL_STREAMING, responseObserver);
    }

    @java.lang.Override public io.grpc.ServerServiceDefinition bindService() {
      return MovieServiceGrpc.bindService(this);
    }
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static interface MovieServiceBlockingClient {

    /**
     * <pre>
     * Simple Unary RPC
     * Get details for a movie
     * </pre>
     */
    public io.grpc.examples.movieservice.Movie getMovieDetails(io.grpc.examples.movieservice.MovieRequest request);

    /**
     * <pre>
     * Simple Unary RPC
     * Get a list of movies
     * big result
     * </pre>
     */
    public io.grpc.examples.movieservice.MoviesInTheaterResponse listAllMovies(com.google.protobuf.Empty request);

    /**
     */
    public java.util.Iterator<io.grpc.examples.movieservice.Movie> listAllMoviesServerStreaming(
        com.google.protobuf.Empty request);

    /**
     * <pre>
     * A server-to-client Streaming RPC
     * accepts a request
     * Obtains the list of movies
     * Results are streamed rather than returned at once
     * </pre>
     */
    public java.util.Iterator<io.grpc.examples.movieservice.Movie> listMoviesServerToClientStreaming(
        io.grpc.examples.movieservice.MoviesInTheaterRequest request);
  }

  /**
   * <pre>
   * The greeting service definition.
   * </pre>
   */
  public static interface MovieServiceFutureClient {

    /**
     * <pre>
     * Simple Unary RPC
     * Get details for a movie
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.movieservice.Movie> getMovieDetails(
        io.grpc.examples.movieservice.MovieRequest request);

    /**
     * <pre>
     * Simple Unary RPC
     * Get a list of movies
     * big result
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.movieservice.MoviesInTheaterResponse> listAllMovies(
        com.google.protobuf.Empty request);
  }

  public static class MovieServiceStub extends io.grpc.stub.AbstractStub<MovieServiceStub>
      implements MovieService {
    private MovieServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MovieServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MovieServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MovieServiceStub(channel, callOptions);
    }

    @java.lang.Override
    public void getMovieDetails(io.grpc.examples.movieservice.MovieRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_GET_MOVIE_DETAILS, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void listAllMovies(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MoviesInTheaterResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LIST_ALL_MOVIES, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void listAllMoviesServerStreaming(com.google.protobuf.Empty request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_LIST_ALL_MOVIES_SERVER_STREAMING, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public void listMoviesServerToClientStreaming(io.grpc.examples.movieservice.MoviesInTheaterRequest request,
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver) {
      asyncServerStreamingCall(
          getChannel().newCall(METHOD_LIST_MOVIES_SERVER_TO_CLIENT_STREAMING, getCallOptions()), request, responseObserver);
    }

    @java.lang.Override
    public io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MovieRequest> listMoviesClientToServerStreaming(
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MoviesInTheaterResponse> responseObserver) {
      return asyncClientStreamingCall(
          getChannel().newCall(METHOD_LIST_MOVIES_CLIENT_TO_SERVER_STREAMING, getCallOptions()), responseObserver);
    }

    @java.lang.Override
    public io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MovieRequest> listMoviesBidirectionalStreaming(
        io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(METHOD_LIST_MOVIES_BIDIRECTIONAL_STREAMING, getCallOptions()), responseObserver);
    }
  }

  public static class MovieServiceBlockingStub extends io.grpc.stub.AbstractStub<MovieServiceBlockingStub>
      implements MovieServiceBlockingClient {
    private MovieServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MovieServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MovieServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MovieServiceBlockingStub(channel, callOptions);
    }

    @java.lang.Override
    public io.grpc.examples.movieservice.Movie getMovieDetails(io.grpc.examples.movieservice.MovieRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_GET_MOVIE_DETAILS, getCallOptions(), request);
    }

    @java.lang.Override
    public io.grpc.examples.movieservice.MoviesInTheaterResponse listAllMovies(com.google.protobuf.Empty request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LIST_ALL_MOVIES, getCallOptions(), request);
    }

    @java.lang.Override
    public java.util.Iterator<io.grpc.examples.movieservice.Movie> listAllMoviesServerStreaming(
        com.google.protobuf.Empty request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_LIST_ALL_MOVIES_SERVER_STREAMING, getCallOptions(), request);
    }

    @java.lang.Override
    public java.util.Iterator<io.grpc.examples.movieservice.Movie> listMoviesServerToClientStreaming(
        io.grpc.examples.movieservice.MoviesInTheaterRequest request) {
      return blockingServerStreamingCall(
          getChannel(), METHOD_LIST_MOVIES_SERVER_TO_CLIENT_STREAMING, getCallOptions(), request);
    }
  }

  public static class MovieServiceFutureStub extends io.grpc.stub.AbstractStub<MovieServiceFutureStub>
      implements MovieServiceFutureClient {
    private MovieServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private MovieServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected MovieServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new MovieServiceFutureStub(channel, callOptions);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.movieservice.Movie> getMovieDetails(
        io.grpc.examples.movieservice.MovieRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_GET_MOVIE_DETAILS, getCallOptions()), request);
    }

    @java.lang.Override
    public com.google.common.util.concurrent.ListenableFuture<io.grpc.examples.movieservice.MoviesInTheaterResponse> listAllMovies(
        com.google.protobuf.Empty request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LIST_ALL_MOVIES, getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_MOVIE_DETAILS = 0;
  private static final int METHODID_LIST_ALL_MOVIES = 1;
  private static final int METHODID_LIST_ALL_MOVIES_SERVER_STREAMING = 2;
  private static final int METHODID_LIST_MOVIES_SERVER_TO_CLIENT_STREAMING = 3;
  private static final int METHODID_LIST_MOVIES_CLIENT_TO_SERVER_STREAMING = 4;
  private static final int METHODID_LIST_MOVIES_BIDIRECTIONAL_STREAMING = 5;

  private static class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final MovieService serviceImpl;
    private final int methodId;

    public MethodHandlers(MovieService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_MOVIE_DETAILS:
          serviceImpl.getMovieDetails((io.grpc.examples.movieservice.MovieRequest) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie>) responseObserver);
          break;
        case METHODID_LIST_ALL_MOVIES:
          serviceImpl.listAllMovies((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MoviesInTheaterResponse>) responseObserver);
          break;
        case METHODID_LIST_ALL_MOVIES_SERVER_STREAMING:
          serviceImpl.listAllMoviesServerStreaming((com.google.protobuf.Empty) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie>) responseObserver);
          break;
        case METHODID_LIST_MOVIES_SERVER_TO_CLIENT_STREAMING:
          serviceImpl.listMoviesServerToClientStreaming((io.grpc.examples.movieservice.MoviesInTheaterRequest) request,
              (io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_LIST_MOVIES_CLIENT_TO_SERVER_STREAMING:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.listMoviesClientToServerStreaming(
              (io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.MoviesInTheaterResponse>) responseObserver);
        case METHODID_LIST_MOVIES_BIDIRECTIONAL_STREAMING:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.listMoviesBidirectionalStreaming(
              (io.grpc.stub.StreamObserver<io.grpc.examples.movieservice.Movie>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static io.grpc.ServerServiceDefinition bindService(
      final MovieService serviceImpl) {
    return io.grpc.ServerServiceDefinition.builder(SERVICE_NAME)
        .addMethod(
          METHOD_GET_MOVIE_DETAILS,
          asyncUnaryCall(
            new MethodHandlers<
              io.grpc.examples.movieservice.MovieRequest,
              io.grpc.examples.movieservice.Movie>(
                serviceImpl, METHODID_GET_MOVIE_DETAILS)))
        .addMethod(
          METHOD_LIST_ALL_MOVIES,
          asyncUnaryCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              io.grpc.examples.movieservice.MoviesInTheaterResponse>(
                serviceImpl, METHODID_LIST_ALL_MOVIES)))
        .addMethod(
          METHOD_LIST_ALL_MOVIES_SERVER_STREAMING,
          asyncServerStreamingCall(
            new MethodHandlers<
              com.google.protobuf.Empty,
              io.grpc.examples.movieservice.Movie>(
                serviceImpl, METHODID_LIST_ALL_MOVIES_SERVER_STREAMING)))
        .addMethod(
          METHOD_LIST_MOVIES_SERVER_TO_CLIENT_STREAMING,
          asyncServerStreamingCall(
            new MethodHandlers<
              io.grpc.examples.movieservice.MoviesInTheaterRequest,
              io.grpc.examples.movieservice.Movie>(
                serviceImpl, METHODID_LIST_MOVIES_SERVER_TO_CLIENT_STREAMING)))
        .addMethod(
          METHOD_LIST_MOVIES_CLIENT_TO_SERVER_STREAMING,
          asyncClientStreamingCall(
            new MethodHandlers<
              io.grpc.examples.movieservice.MovieRequest,
              io.grpc.examples.movieservice.MoviesInTheaterResponse>(
                serviceImpl, METHODID_LIST_MOVIES_CLIENT_TO_SERVER_STREAMING)))
        .addMethod(
          METHOD_LIST_MOVIES_BIDIRECTIONAL_STREAMING,
          asyncBidiStreamingCall(
            new MethodHandlers<
              io.grpc.examples.movieservice.MovieRequest,
              io.grpc.examples.movieservice.Movie>(
                serviceImpl, METHODID_LIST_MOVIES_BIDIRECTIONAL_STREAMING)))
        .build();
  }
}

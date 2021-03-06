// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: movie_service.proto

package io.grpc.examples.movieservice;

public final class MovieServiceProto {
  private MovieServiceProto() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_movieservice_MoviesInTheaterRequest_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_movieservice_MoviesInTheaterRequest_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_movieservice_MoviesInTheaterResponse_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_movieservice_MoviesInTheaterResponse_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_movieservice_MovieRequest_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_movieservice_MovieRequest_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_movieservice_MovieDatabase_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_movieservice_MovieDatabase_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_movieservice_Movie_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_movieservice_Movie_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_movieservice_Movie_Collection_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_movieservice_Movie_Collection_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_movieservice_Movie_Genre_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_movieservice_Movie_Genre_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_movieservice_Movie_ProductionCompany_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_movieservice_Movie_ProductionCompany_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_movieservice_Movie_ProductionCountry_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_movieservice_Movie_ProductionCountry_fieldAccessorTable;
  static com.google.protobuf.Descriptors.Descriptor
    internal_static_movieservice_Movie_SpokenLanguage_descriptor;
  static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_movieservice_Movie_SpokenLanguage_fieldAccessorTable;

  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n\023movie_service.proto\022\014movieservice\032\033goo" +
      "gle/protobuf/empty.proto\"%\n\026MoviesInThea" +
      "terRequest\022\013\n\003ids\030\001 \003(\004\">\n\027MoviesInTheat" +
      "erResponse\022#\n\006movies\030\001 \003(\0132\023.movieservic" +
      "e.Movie\"\032\n\014MovieRequest\022\n\n\002id\030\001 \001(\004\"4\n\rM" +
      "ovieDatabase\022#\n\006movies\030\001 \003(\0132\023.movieserv" +
      "ice.Movie\"\275\007\n\005Movie\022\r\n\005adult\030\001 \001(\010\022\025\n\rba" +
      "ckdrop_path\030\002 \001(\t\022=\n\025belongs_to_collecti" +
      "on\030\003 \001(\0132\036.movieservice.Movie.Collection" +
      "\022\016\n\006budget\030\004 \001(\004\022)\n\006genres\030\005 \003(\0132\031.movie",
      "service.Movie.Genre\022\020\n\010homepage\030\006 \001(\t\022\n\n" +
      "\002id\030\007 \001(\004\022\017\n\007imdb_id\030\010 \001(\t\022\031\n\021original_l" +
      "anguage\030\t \001(\t\022\026\n\016original_title\030\n \001(\t\022\020\n" +
      "\010overview\030\013 \001(\t\022\022\n\npopularity\030\014 \001(\001\022\023\n\013p" +
      "oster_path\030\r \001(\t\022C\n\024production_companies" +
      "\030\016 \003(\0132%.movieservice.Movie.ProductionCo" +
      "mpany\022C\n\024production_countries\030\017 \003(\0132%.mo" +
      "vieservice.Movie.ProductionCountry\022\024\n\014re" +
      "lease_date\030\020 \001(\t\022\017\n\007revenue\030\021 \001(\004\022\017\n\007run" +
      "time\030\022 \001(\005\022<\n\020spoken_languages\030\023 \003(\0132\".m",
      "ovieservice.Movie.SpokenLanguage\022\016\n\006stat" +
      "us\030\024 \001(\t\022\017\n\007tagline\030\025 \001(\t\022\r\n\005title\030\026 \001(\t" +
      "\022\r\n\005video\030\027 \001(\010\022\024\n\014vote_average\030\030 \001(\001\022\022\n" +
      "\nvote_count\030\031 \001(\005\032R\n\nCollection\022\n\n\002id\030\001 " +
      "\001(\004\022\014\n\004name\030\002 \001(\t\022\023\n\013poster_path\030\003 \001(\t\022\025" +
      "\n\rbackdrop_path\030\004 \001(\t\032!\n\005Genre\022\n\n\002id\030\001 \001" +
      "(\005\022\014\n\004name\030\002 \001(\t\032-\n\021ProductionCompany\022\014\n" +
      "\004name\030\001 \001(\t\022\n\n\002id\030\002 \001(\005\0325\n\021ProductionCou" +
      "ntry\022\022\n\niso_3166_1\030\001 \001(\t\022\014\n\004name\030\002 \001(\t\0321" +
      "\n\016SpokenLanguage\022\021\n\tiso_639_1\030\001 \001(\t\022\014\n\004n",
      "ame\030\002 \001(\t2\242\004\n\014MovieService\022D\n\017GetMovieDe" +
      "tails\022\032.movieservice.MovieRequest\032\023.movi" +
      "eservice.Movie\"\000\022P\n\rListAllMovies\022\026.goog" +
      "le.protobuf.Empty\032%.movieservice.MoviesI" +
      "nTheaterResponse\"\000\022O\n\034ListAllMoviesServe" +
      "rStreaming\022\026.google.protobuf.Empty\032\023.mov" +
      "ieservice.Movie\"\0000\001\022b\n!ListMoviesServerT" +
      "oClientStreaming\022$.movieservice.MoviesIn" +
      "TheaterRequest\032\023.movieservice.Movie\"\0000\001\022" +
      "j\n!ListMoviesClientToServerStreaming\022\032.m",
      "ovieservice.MovieRequest\032%.movieservice." +
      "MoviesInTheaterResponse\"\000(\001\022Y\n ListMovie" +
      "sBidirectionalStreaming\022\032.movieservice.M" +
      "ovieRequest\032\023.movieservice.Movie\"\000(\0010\001B9" +
      "\n\035io.grpc.examples.movieserviceB\021MovieSe" +
      "rviceProtoP\001\242\002\002MSb\006proto3"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
        new com.google.protobuf.Descriptors.FileDescriptor.    InternalDescriptorAssigner() {
          public com.google.protobuf.ExtensionRegistry assignDescriptors(
              com.google.protobuf.Descriptors.FileDescriptor root) {
            descriptor = root;
            return null;
          }
        };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          com.google.protobuf.EmptyProto.getDescriptor(),
        }, assigner);
    internal_static_movieservice_MoviesInTheaterRequest_descriptor =
      getDescriptor().getMessageTypes().get(0);
    internal_static_movieservice_MoviesInTheaterRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_movieservice_MoviesInTheaterRequest_descriptor,
        new java.lang.String[] { "Ids", });
    internal_static_movieservice_MoviesInTheaterResponse_descriptor =
      getDescriptor().getMessageTypes().get(1);
    internal_static_movieservice_MoviesInTheaterResponse_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_movieservice_MoviesInTheaterResponse_descriptor,
        new java.lang.String[] { "Movies", });
    internal_static_movieservice_MovieRequest_descriptor =
      getDescriptor().getMessageTypes().get(2);
    internal_static_movieservice_MovieRequest_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_movieservice_MovieRequest_descriptor,
        new java.lang.String[] { "Id", });
    internal_static_movieservice_MovieDatabase_descriptor =
      getDescriptor().getMessageTypes().get(3);
    internal_static_movieservice_MovieDatabase_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_movieservice_MovieDatabase_descriptor,
        new java.lang.String[] { "Movies", });
    internal_static_movieservice_Movie_descriptor =
      getDescriptor().getMessageTypes().get(4);
    internal_static_movieservice_Movie_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_movieservice_Movie_descriptor,
        new java.lang.String[] { "Adult", "BackdropPath", "BelongsToCollection", "Budget", "Genres", "Homepage", "Id", "ImdbId", "OriginalLanguage", "OriginalTitle", "Overview", "Popularity", "PosterPath", "ProductionCompanies", "ProductionCountries", "ReleaseDate", "Revenue", "Runtime", "SpokenLanguages", "Status", "Tagline", "Title", "Video", "VoteAverage", "VoteCount", });
    internal_static_movieservice_Movie_Collection_descriptor =
      internal_static_movieservice_Movie_descriptor.getNestedTypes().get(0);
    internal_static_movieservice_Movie_Collection_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_movieservice_Movie_Collection_descriptor,
        new java.lang.String[] { "Id", "Name", "PosterPath", "BackdropPath", });
    internal_static_movieservice_Movie_Genre_descriptor =
      internal_static_movieservice_Movie_descriptor.getNestedTypes().get(1);
    internal_static_movieservice_Movie_Genre_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_movieservice_Movie_Genre_descriptor,
        new java.lang.String[] { "Id", "Name", });
    internal_static_movieservice_Movie_ProductionCompany_descriptor =
      internal_static_movieservice_Movie_descriptor.getNestedTypes().get(2);
    internal_static_movieservice_Movie_ProductionCompany_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_movieservice_Movie_ProductionCompany_descriptor,
        new java.lang.String[] { "Name", "Id", });
    internal_static_movieservice_Movie_ProductionCountry_descriptor =
      internal_static_movieservice_Movie_descriptor.getNestedTypes().get(3);
    internal_static_movieservice_Movie_ProductionCountry_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_movieservice_Movie_ProductionCountry_descriptor,
        new java.lang.String[] { "Iso31661", "Name", });
    internal_static_movieservice_Movie_SpokenLanguage_descriptor =
      internal_static_movieservice_Movie_descriptor.getNestedTypes().get(4);
    internal_static_movieservice_Movie_SpokenLanguage_fieldAccessorTable = new
      com.google.protobuf.GeneratedMessage.FieldAccessorTable(
        internal_static_movieservice_Movie_SpokenLanguage_descriptor,
        new java.lang.String[] { "Iso6391", "Name", });
    com.google.protobuf.EmptyProto.getDescriptor();
  }

  // @@protoc_insertion_point(outer_class_scope)
}

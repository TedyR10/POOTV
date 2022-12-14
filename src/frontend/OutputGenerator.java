package frontend;

import backend.Movie;
import backend.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;

abstract class Output {
    public abstract ObjectNode generateOutput(ObjectMapper objectMapper,
                                              ObjectNode node, User user, ArrayList<Movie> movies,
                                              Movie movie);

    public ObjectNode userCreator(final ObjectMapper objectMapper, final ObjectNode node,
                                  final User user,
                                  final ArrayList<Movie> movies, final Movie movie) {
        ObjectNode userOut = objectMapper.createObjectNode();
        ObjectNode credentials = objectMapper.createObjectNode();
        credentials.put("name", user.getName());
        credentials.put("password", user.getPassword());
        credentials.put("accountType", user.getAccountType());
        credentials.put("country", user.getCountry());
        String balance = Integer.toString(user.getBalance());
        credentials.put("balance", balance);
        userOut.set("credentials", credentials);
        userOut.put("tokensCount", user.getTokens());
        userOut.put("numFreePremiumMovies", user.getNumFreePremiumMovies());
        userOut.putPOJO("purchasedMovies", user.getPurchasedMovies());
        userOut.putPOJO("watchedMovies", user.getWatchedMovies());
        userOut.putPOJO("likedMovies", user.getLikedMovies());
        userOut.putPOJO("ratedMovies", user.getRatedMovies());
        return userOut.deepCopy();
    }

    public ObjectNode movieCreator(final ObjectMapper objectMapper, final ObjectNode node,
                                   final User user,
                                   final ArrayList<Movie> movies, final Movie movie) {
        return node;
    }
}
class GeneralOutput extends Output {
    @Override
    public ObjectNode generateOutput(final ObjectMapper objectMapper,
                                     final ObjectNode node, final User user,
                                     final ArrayList<Movie> movies, final Movie movie) {
        node.put("error", "Error");
        ArrayList<String> moviesOut = new ArrayList<String>();
        node.putPOJO("currentMoviesList", moviesOut);
        node.putNull("currentUser");
        return node.deepCopy();
    }
}
class UserOutput extends Output {

    @Override
    public ObjectNode generateOutput(final ObjectMapper objectMapper,
                                     final ObjectNode node, final User user,
                                     final ArrayList<Movie> movies, final Movie movie) {
        node.putNull("error");
        ArrayList<String> moviesOut = new ArrayList<String>();
        node.putPOJO("currentMoviesList", moviesOut);
        ObjectNode userOut = userCreator(objectMapper, node, user, movies, movie);
        node.set("currentUser", userOut);
        return node.deepCopy();
    }
}
class Movies extends Output {

    @Override
    public ObjectNode generateOutput(final ObjectMapper objectMapper,
                                     final ObjectNode node, final User user,
                                     final ArrayList<Movie> movies, final Movie movie) {
        node.putNull("error");
        node.putPOJO("currentMoviesList", movies);
        ObjectNode userOut = userCreator(objectMapper, node, user, movies, movie);
        node.set("currentUser", userOut);
        return node.deepCopy();
    }
}

class Details extends Output {

    @Override
    public ObjectNode generateOutput(final ObjectMapper objectMapper,
                                     final ObjectNode node, final User user,
                                     final ArrayList<Movie> movies, final Movie movie) {
        node.putNull("error");
        node.putPOJO("currentMoviesList", movie);
        ObjectNode userOut = userCreator(objectMapper, node, user, movies, movie);
        node.set("currentUser", userOut);
        return node.deepCopy();
    }
}

final class OutputFactory {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static ObjectNode node = objectMapper.createObjectNode();

    private OutputFactory() {
    }

    public enum OutputType {
        General, User, Movies, Details
    }
    public static ObjectNode createOutput(final OutputType outputType, final User user,
                                          final ArrayList<Movie> movies, final Movie movie) {
        switch (outputType) {
            case General:   return
                    new GeneralOutput().generateOutput(objectMapper, node, user, movies, movie);
            case User:
                return new UserOutput().generateOutput(objectMapper, node, user, movies, movie);
            case Movies:
                return new Movies().generateOutput(objectMapper, node, user, movies, movie);
            case Details:
                return new Details().generateOutput(objectMapper, node, user, movies, movie);
            default:    return null;
        }
    }
}

public class OutputGenerator {

    private String outputType;
    private User user;
    private ArrayList<Movie> movies;
    private Movie movie;

    public OutputGenerator(final String type, final User outUser,
                           final ArrayList<Movie> outMovies, final Movie outMovie) {
        this.outputType = type;
        this.user = outUser;
        this.movies = outMovies;
        this.movie = outMovie;
    }

    /**
     *
     * @return node output
     */
    public ObjectNode outputCreator() {
        switch (this.outputType) {
            case "General":
                return OutputFactory.createOutput(
                        OutputFactory.OutputType.General, user, movies, movie);
            case "User":
                return OutputFactory.createOutput(
                        OutputFactory.OutputType.User, user, movies, movie);
            case "Movies":
                return OutputFactory.createOutput(
                        OutputFactory.OutputType.Movies, user, movies, movie);
            case "Details":
                return OutputFactory.createOutput(
                        OutputFactory.OutputType.Details, user, movies, movie);
            default:
                return null;
        }
    }
}

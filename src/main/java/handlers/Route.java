package handlers;

import java.util.function.Predicate;

public record Route(
    Predicate<String> match,
    Handler handler
) {

}

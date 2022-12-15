package frontend;

import singleton.MoviesDatabase;
import singleton.UsersDatabase;
import com.fasterxml.jackson.databind.node.ArrayNode;
import input.ActionsInput;
import session.Session;
import visitor.PageVisitor;
import visitor.Visitable;

public class ChangePage implements Visitable {

    public ChangePage() {
    }
    /**
     * for coding style
     */
    public void accept(final PageVisitor v, final ActionsInput action,
                       final Session session, final UsersDatabase usersDatabase,
                       final MoviesDatabase moviesDatabase, final ArrayNode output) {
        v.visit(this, action, session, usersDatabase, moviesDatabase, output);
    }
}

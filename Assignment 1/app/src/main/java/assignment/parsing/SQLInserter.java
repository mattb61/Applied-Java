package assignment.parsing;

import java.sql.SQLException;

public interface SQLInserter<O> {
    void accept(O o, int i) throws SQLException;
}

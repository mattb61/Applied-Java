package pkm_data_installer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class ReleaseTablePushLogic implements TablePushLogic{

    @Override
    public void configure(PreparedStatement stmt, Map<String, Object> dataSource) throws SQLException {
                String name = (String)dataSource.get("name");
                int gen = (Integer)dataSource.get("gen");
                stmt.setString(1, name);
                stmt.setInt(2, gen);
    }

}

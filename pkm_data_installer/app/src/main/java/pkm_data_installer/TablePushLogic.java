package pkm_data_installer;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public interface TablePushLogic {
    void configure(PreparedStatement stmt, Map<String, Object> dataSource) throws SQLException;
}

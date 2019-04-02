package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.utils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class UsefulFunctions {
    public static boolean hasColumn(ResultSet resultSet, String columnName) throws SQLException {
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int columnCount = resultSetMetaData.getColumnCount();
        for (int currentColumn = 1; currentColumn <= columnCount; currentColumn++) {
            if (columnName.equals(resultSetMetaData.getColumnName(currentColumn)))
                return true;
        }
        return false;
    }
}

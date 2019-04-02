package nl.han.ica.oose.dea.joephoffland.Spotitube.datasource.utils;

import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsefulFunctionsTest {

    @Test
    public void hasColumn() {
        ResultSet resultSet = mock(ResultSet.class);
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        try {
            when(resultSet.getMetaData()).thenReturn(resultSetMetaData);
            when(resultSetMetaData.getColumnCount()).thenReturn(0);

            String columnName = "test";

            boolean hasColumn = UsefulFunctions.hasColumn(resultSet, columnName);
            assertEquals(false, hasColumn);

            when(resultSetMetaData.getColumnCount()).thenReturn(1);
            when(resultSetMetaData.getColumnName(1)).thenReturn(columnName);

            hasColumn = UsefulFunctions.hasColumn(resultSet, columnName);
            assertEquals(true, hasColumn);
        } catch(SQLException e) {
            fail();
        }

    }
}
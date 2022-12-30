package net.centralfloridaattorney.h2tool;

import net.centralfloridaattorney.toolbag.DataTool;
import org.h2.jdbc.JdbcSQLNonTransientConnectionException;
import org.h2.tools.DeleteDbFiles;

import javax.sound.midi.Soundbank;
import java.sql.*;
import java.util.Arrays;

public class H2Tool {
    static final String JDBC_USERNAME = "";
    static final String JDBC_PASSWORD = "";
    //String TABLE_NAME = "h2Tool";
    //String TABLE_NAME = "h2Tool";
    private static H2Tool instance;
    protected String JDBC_URL = "jdbc:h2:file:~/h2/h2Tool";
    String tableName;
    String[] keys;

    /**
     * H2Tool is a tool to operate H2 databases in java. to start workbench: ~/h2/h2/bin/h2.sh
     */
    protected H2Tool() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.tableName = "h2Tool";
        //init_destructive();
    }

    public static void main(String[] args) {
        // if main does not start then ...
        H2Tool h2Tool = H2Tool.getInstance();
        h2Tool.put(1, "testValue", "testKey");
        h2Tool.get(1, "testValue");
    }

    public static synchronized H2Tool getInstance() {
        if (instance == null) {
            instance = new H2Tool();
        }
        return instance;
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

    /**
     * H2Tool.init_destructive() deletes and recreates the h2tool database
     */
    public void init_destructive() {
        DeleteDbFiles.execute("~/h2", "h2Tool", true);
        keys = new String[1];
        keys[0] = "INDEX";
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection(JDBC_URL);
            Statement stat = conn.createStatement();
            stat.execute("drop table if exists " + tableName);
            stat.execute("create table " + tableName + " (index int auto_increment, primary key (index))");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    private void addColumn(String tableName, String columnName) throws JdbcSQLNonTransientConnectionException {

        if (!DataTool.isWordInRow(getKeys(), columnName)) {
            try {
                if (Integer.parseInt(columnName) > 0) {
                    columnName = "_" + columnName;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("was not a number");
                //nfe.printStackTrace();
                //columnName = columnName;
            }

            //String[] columns = getKeys();
            String sql_alter_table = "ALTER TABLE " + tableName + " ADD COLUMN " + columnName + " Clob;";
            Connection connection = getConnection();
            Statement statement;
            try {
                statement = connection.createStatement();
                statement.executeUpdate(sql_alter_table);
                connection.close();
                keys = Arrays.copyOf(keys, keys.length+1);
                keys[keys.length-1] = columnName.toUpperCase();

            } catch (SQLException e) {
                printSQLException(e);
            }
        }
    }

    public String get(int index, String key) {
        //index is used to get a particular row
        //key is the column name
        //return value is from row and column
        String value = key + ":default";
        if (isColumnExist(key)) {
            try {
                Class.forName("org.h2.Driver");
                //Connection conn = DriverManager.getConnection(JDBC_URL);
                String sql_select_row_1 = "select * from  " + tableName + " where index = " + index;
                String sql_select_row_2 = "select * from  " + tableName + " where index = " + index;
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(sql_select_row_1);

                while (resultSet.next()) {
                    value = resultSet.getString(key);
                    //System.out.println(value);

                }
                connection.close();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                printSQLException(e);
            }
        }
        return value;
    }

    public int getIndex(String key, String value) {
        //index is used to get a particular row
        //key is the column name
        //return value is from row and column
        //String value = "default";
        //TODO fix H2Tool.getIndex()
        int index = -1;
        String[] keys = getKeys();
        if (!DataTool.isWordInRow(keys, key)) {
            put(1, key, "default");
        }
        try {
            Class.forName("org.h2.Driver");
            //Connection conn = DriverManager.getConnection(JDBC_URL);
            //Statement stat = conn.createStatement();
            Statement stat = getConnection().createStatement();
            String sqlQuery = "SELECT * FROM " + tableName + " WHERE " + key + " = '" + value + "'";
            try (ResultSet rs = stat.executeQuery(sqlQuery)) {

                while (rs.next()) {
                    if (rs.wasNull()) {
                        return -1;
                    }
                    String thisValue = rs.getString(key);
                    if (thisValue.equals(value)) {
                        //return index;
                        return rs.getInt("index");
                    }
                    //System.out.println(value);
                }
                index++;
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            printSQLException(e);
        }
        return index;
    }

    public String[][] getValues(boolean withHeader) {
        String[][] dataArray = null;
        int numberRows = getNumberRows();

        try{
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int numberColumns = resultSetMetaData.getColumnCount();
            dataArray = new String[numberRows][numberColumns];
            //System.out.println("Values From Table: " + tableName);
            int rowCount = 0;
            while (resultSet.next()) {
                for (int i = 0; i < numberColumns; i++) {
                    //if (i > 1) System.out.print(",  ");
                    String columnValue = resultSet.getString(i+1);
                    dataArray[rowCount][i] = columnValue;
                    //System.out.print(columnValue + " " + resultSetMetaData.getColumnName(i));
                }
                rowCount++;
            }
            connection.close();
            if (withHeader) {
                String[][] newDataArray = new String[dataArray.length + 1][];
                newDataArray[0] = getKeys();
                System.arraycopy(dataArray, 0, newDataArray, 1, dataArray.length);
                dataArray = newDataArray;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return dataArray;
    }

    private int getNumberRows() {
        int numberRows = 0;
        String sql_statement = "SELECT * FROM " + tableName;
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(sql_statement);
            resultSet.last();
            numberRows = resultSet.getRow();
            System.out.println("Number Rows: " + numberRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return numberRows;
    }

    public String[] getKeys() {
        return keys;
        /**
        String sql_show_columns = "SELECT * FROM " + tableName;
        String[] columnNames = null;
        String colName;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(sql_show_columns);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            columnNames = new String[columnCount];
            for (int i = 1; i < columnCount + 1; i++) {
                if (null == rsmd.getColumnName(i)) {
                    colName = "default";
                } else {
                    colName = rsmd.getColumnName(i);
                }
                columnNames[i - 1] = colName;
            }
            connection.close();

        } catch (SQLException e) {
            printSQLException(e);
        }
        return columnNames;
        **/

    }

    public Connection getConnection() throws JdbcSQLNonTransientConnectionException {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD);
        } catch (SQLException e) {
            printSQLException(e);
        }
        return connection;
    }

    public void insertRecord(String key, String value) {
        int newIndex = getNumberRows() + 1;
        String sql_insert_record = "INSERT INTO " + tableName + "(index, " + key + ") VALUES( " + newIndex + ", '" + value + "');";
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql_insert_record);
            connection.close();
        } catch (SQLException jdbcE) {
            jdbcE.printStackTrace();
        }
    }

    public boolean isColumnExist(String key) {
        String[] keys = getKeys();
        return DataTool.isWordInRow(keys, key);
    }

    public void put(int index, String key, String value) {
        //REM H2 SQL index begins at 1
        if (!isColumnExist(key)) {
            try {
                addColumn(tableName, key);
            } catch (JdbcSQLNonTransientConnectionException e) {
                e.printStackTrace();
            }
        }
        if (index > getNumberRows()) {
            insertRecord(key, value);
        } else {
            updateRecord(index, key, value);
        }
    }

    private void updateRecord(int index, String key, String value) {
        String sql_update_value = "UPDATE " + tableName + " SET " + key + " = '" + value + "' WHERE INDEX = " + index + ";";
        String sql_update_value_2 = "UPDATE CUSTOMERS SET ADDRESS = 'Pune' WHERE ID = 6;";
        Statement statement;
        try {
            statement = getConnection().createStatement();
            statement.executeUpdate(sql_update_value);
            statement.close();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void deleteIndex(int h2Index) {

        String sql_update_value = "DELETE FROM " + tableName + " WHERE index = " + h2Index + ";";
        String sql_update_value_2 = "UPDATE CUSTOMERS SET ADDRESS = 'Pune' WHERE ID = 6;";

        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql_update_value);
            connection.close();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void put(String[][] dataArray) {
        //the first row is the header
        String[] theseKeys = dataArray[0];
        int numRows = dataArray.length-1;
        for(int i = 0;i<numRows;i++){
            for(int j = 0; j<theseKeys.length;j++){
                put(i+1, theseKeys[j], dataArray[i+1][j]);
            }
        }
    }
}

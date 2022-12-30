package net.centralfloridaattorney.examples;


import net.centralfloridaattorney.h2tool.H2Tool;
import net.centralfloridaattorney.toolbag.FileTool;

public class CSVLoader {
    protected static String JDBC_URL = "jdbc:h2:file:~/h2/dataCSV1";

    public static void main(String[] args) {
        String[][] csvStringArray = FileTool.getCSV("TSLA.csv");
        H2Tool h2Tool = H2Tool.getInstance();
        h2Tool.init_destructive();
        h2Tool.put(csvStringArray);
        //h2Tool.init_destructive();
        System.out.println(csvStringArray[0][0]);
    }
}

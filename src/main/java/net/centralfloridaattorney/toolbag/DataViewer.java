package net.centralfloridaattorney.toolbag;

import net.centralfloridaattorney.h2tool.H2Tool;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

public class DataViewer extends JPanel {
    //
    private JTable table;
    private static final String FILE_PATH = "data/csv/TSLA.csv";
    protected static String JDBC_URL = "jdbc:h2:file:~/h2/dataViewer";

    //private static String filePath = "python_commands.csv";

    private static String[] colNames;

    private static String[][] getH2ModelArray(){
        H2Tool h2ESpeak = H2Tool.getInstance();
        //colNames = h2ESpeak.getKeys();
        String[][] dataArray = h2ESpeak.getValues(true);
        String[][] modelArray = new String[dataArray.length][colNames.length];
        modelArray[0] = colNames;
        modelArray = DataTool.appendArrayToArray(modelArray, dataArray);
        return dataArray;
    }

    public DataViewer(String[][] fileArray) {
        super(new BorderLayout(3, 3));
        //String[][] fileArray = FileTool.get2DArray(filePath);
        this.colNames = fileArray[0];
        this.table = new JTable(new ModelMaker());
        this.table.setPreferredScrollableViewportSize(new Dimension(700, 70));
        this.table.setFillsViewportHeight(true);
        JPanel ButtonOpen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(ButtonOpen, BorderLayout.SOUTH);
        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        // Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.CENTER);
        // add a nice border
        setBorder(new EmptyBorder(5, 5, 5, 5));
        //Remove the first row from fileArray because it should be colNames
        String[][] modelData = DataTool.removeRow(fileArray, 0);
        setModel(modelData);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        //String[][] dataArray = FileTool.getCSV(FILE_PATH);
        //String[][] modelDataArray = DataViewer.getH2ModelArray();
        H2Tool h2ESpeak = H2Tool.getInstance();
        String[][] dataArray = h2ESpeak.getValues(true);
        DataViewer dataViewer = new DataViewer(dataArray);
        //dataViewer.setColumnNames(modelDataArray[0]);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                dataViewer.createAndShowGUI(dataArray);
            }
        });
        System.out.println("Finished Main!");
    }

    private  void setColumnNames(String[] newColNames) {
        this.colNames = newColNames;
    }

    public static void createAndShowGUI(String[][] dataArray) {
        // Create and set up the window.
        //String[][] fileData = FileTool.get2DStringArray(filePath, 2);
        JFrame frame = new JFrame("DataViewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create and set up the content pane.
        DataViewer newContentPane = new DataViewer(dataArray);
        frame.setContentPane(newContentPane);
        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void createAndShowGUI() {
        // Create and set up the window.
        //String[][] fileData = FileTool.get2DStringArray(filePath, 2);
        JFrame frame = new JFrame("DataViewer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Create and set up the content pane.
        DataViewer newContentPane = new DataViewer(FILE_PATH);
        frame.setContentPane(newContentPane);
        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public DataViewer() {
        super(new BorderLayout(3, 3));

    }

    public void init(){
        this.table = new JTable(new ModelMaker());
        this.table.setPreferredScrollableViewportSize(new Dimension(700, 70));
        this.table.setFillsViewportHeight(true);
        JPanel ButtonOpen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(ButtonOpen, BorderLayout.SOUTH);
        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        // Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.CENTER);
        // add a nice border
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setModel(FILE_PATH);
    }

    private void setModel(String[][] fileArray) {
        //String[][] fileArray = FileTool.get2DArray(filePath);
        //colNames = fileArray[0];
        ModelMaker newModel = new ModelMaker();
        this.table.setModel(newModel);
        //String[] csvString = FileTool.getFileString(filePath).split("/n");
        //String[] stringArray = csvString[0].split(",");
        //String[] fileArray = FileTool.get1DArray(filePath);
        //int numCols = fileArray.length;
        //ArrayList<String[]> modelData = FileTool.readCSVfile(filePath, numCols);
        ArrayList<String[]> modelData = new ArrayList<>();
        for(String[] row:fileArray){
            modelData.add(row);
        }
        newModel.AddCSVData(modelData);
        System.out.println("Rows: " + newModel.getRowCount());
        System.out.println("Cols: " + newModel.getColumnCount());
    }

    private void setModel(String filePath) {
        String[][] fileArray = FileTool.get2DArray(filePath);
        //colNames = fileArray[0];
        ModelMaker newModel = new ModelMaker();
        this.table.setModel(newModel);
        //String[] csvString = FileTool.getFileString(filePath).split("/n");
        //String[] stringArray = csvString[0].split(",");
        //String[] fileArray = FileTool.get1DArray(filePath);
        //int numCols = fileArray.length;
        //ArrayList<String[]> modelData = FileTool.readCSVfile(filePath, numCols);
        ArrayList<String[]> modelData = new ArrayList<>();
        for(String[] row:fileArray){
            modelData.add(row);
        }
        newModel.AddCSVData(modelData);
        System.out.println("Rows: " + newModel.getRowCount());
        System.out.println("Cols: " + newModel.getColumnCount());
    }

    public DataViewer(String filePath) {
        super(new BorderLayout(3, 3));
        String[][] fileArray = FileTool.get2DArray(filePath);
        //colNames = fileArray[0];
        this.table = new JTable(new ModelMaker());
        this.table.setPreferredScrollableViewportSize(new Dimension(700, 70));
        this.table.setFillsViewportHeight(true);
        JPanel ButtonOpen = new JPanel(new FlowLayout(FlowLayout.CENTER));
        add(ButtonOpen, BorderLayout.SOUTH);
        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);
        // Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.CENTER);
        // add a nice border
        setBorder(new EmptyBorder(5, 5, 5, 5));
        setModel(filePath);
    }


    // Method for reading CSV file
    public class CSVFile {
        //
        private final ArrayList<String[]> Rs = new ArrayList<String[]>();
        private String[] OneRow;

        public ArrayList<String[]> ReadCSVfile(String DataFile) {
            String fullFilePath = FileTool.getFullFilePath(DataFile);
            try {
                BufferedReader brd = new BufferedReader(new FileReader(DataFile));
                while (brd.ready()) {
                    String st = brd.readLine();
                    OneRow = st.split(",|\\s|;");
                    Rs.add(OneRow);
                    System.out.println(Arrays.toString(OneRow));
                } // end of while
            } // end of try
            catch (Exception e) {
                String errmsg = e.getMessage();
                System.out.println("File not found:" + errmsg);
            } // end of Catch
            return Rs;
        }// end of ReadFile method
    }// end of CSVFile class

    class ModelMaker extends AbstractTableModel {
        //
        private final String[] columnNames = colNames;
        private ArrayList<String[]> modelData = new ArrayList<String[]>();

        public void AddCSVData(ArrayList<String[]> newData) {
            this.modelData = newData;
            this.fireTableDataChanged();
        }

        @Override
        public int getColumnCount() {
            return columnNames.length;// length;
        }

        @Override
        public String getColumnName(int col) {
            return columnNames[col];
        }

        @Override
        public int getRowCount() {
            return modelData.size();
        }

        @Override
        public Object getValueAt(int row, int col) {
            return modelData.get(row)[col];
        }
    }
}
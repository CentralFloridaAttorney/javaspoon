package net.centralfloridaattorney.toolbag;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


import java.io.FileInputStream;

public class FileTool {
    public static final String RESOURCE_PATH = "/src/main/resources/";
    public static final String PROJECT_PATH = "/home/overlordx/IdeaProjects/JavaSpeaker/src/main/resources/";
    //public static final String PROJECT_PATH = FileTool.class.getClassLoader().getResource("").getPath();
    private static final long DELAY_SAVE_FILE = 50;
    private static final String DEFAULT_TAB_FILE = "{fileName\t$FileName\nbasePath\t$BasePath\nlastPutTime\t$LastPutTime\nfilePath\t$FilePath\nprimaryKey\t$PrimaryKey}";
    private static final String DEFAULT_FILE_PATH = "/csv/credit_card_applications.csv";
    private static final String COMMA_DELIMITER = ",";
    //static CustomThreadPool customThreadPool = new CustomThreadPool(1000);
    private static String filePath;
    private static String tabFile;

    public FileTool() {
        //customThreadPool = new CustomThreadPool(50);
    }

    public static void main(String[] args) {
        String[] stringArray = FileTool.get1DArray(DEFAULT_FILE_PATH);
        System.out.println(stringArray[0]);

        String fileName = FileTool.getFileName(DEFAULT_FILE_PATH);
        String fileString = FileTool.getFileString(PROJECT_PATH + DEFAULT_FILE_PATH);
        System.out.println(fileString);
        System.out.println("Finished");
    }

    public static File getFile(String filePath) {
        File file = new File(getFullFilePath(filePath));
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String getFileString(String filePath) {
        //System.out.println();
        String content = "default";
        //File file = new File(RELATIVE_DIR + filePath);
        String fullFilePath = getFullFilePath(filePath);
        File file = new File(fullFilePath);
        if (file.exists()) {
            file.getParentFile().mkdirs();
            FileReader reader = null;

            try {
                reader = new FileReader(file);
                char[] chars = new char[(int) file.length()];
                reader.read(chars);
                content = new String(chars);
                reader.close();
            } catch (IOException var13) {
                var13.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException var12) {
                        var12.printStackTrace();
                    }
                }

            }
        }
        return content;
    }

    public static String getFullFilePath(String filePath) {
        File file = new File("");
        String absolutePath = file.getAbsolutePath();
        if (!filePath.startsWith(absolutePath)&&!filePath.startsWith("/home/")) filePath = absolutePath + RESOURCE_PATH + filePath;
        //Absolute file paths beginning with 'C' or 'D' require substitution of '/' with '\\'
        if (filePath.startsWith("C") || filePath.startsWith("D")) filePath = filePath.replace('\\', '/');
        filePath = filePath.replace("\n", "");
        filePath = filePath.replace("\r", "");
        File newFile = new File(filePath);
        if (!newFile.getParentFile().exists())
            newFile.getParentFile().mkdirs();
        if (!newFile.exists()) {
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return filePath;
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        FileTool.filePath = filePath;
    }

    public static String getTabFile() {
        return tabFile;
    }

    public static void setTabFile(String tabFile) {
        FileTool.tabFile = tabFile;
    }


    public static File getStaticFile(String fileName) {
        String fullFilePath = getFullFilePath(fileName);
        File staticFile = new File(fullFilePath);
        return staticFile;
    }

    public static void deleteDirFiles(String directoryPath) {
        File file = getStaticFile(directoryPath);
        File[] files = file.listFiles();
        if (files != null) {
            for (final File thisFile : files) {
                deleteDir(thisFile);
            }
        }
        file.delete();

    }

    public static void deleteDir(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (final File file : files) {
                deleteDir(file);
            }
        }
        dir.delete();
    }

    public static void saveFileDelayed(String filePath, String tabFile) {
        setFilePath(getFullFilePath(filePath));
        setTabFile(tabFile);
        //TODO Messages probably need to be placed in a thread pool.
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.submit(FileTool::saveFileDelayedExecutor);
    }

    private static void saveFileDelayedExecutor() {
        try {
            Thread.sleep(DELAY_SAVE_FILE);
            String fullFilePath = getFullFilePath(filePath);
            File file = new File(fullFilePath);
            if (!file.exists()) {
                //file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                PrintWriter pr = new PrintWriter(fullFilePath);
                pr.print(tabFile);
                pr.close();
            } catch (Exception var5) {
                var5.printStackTrace();
                //System.out.println("No such file exists.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static String[] get1DArray(String filePath) {
        String[] stringArray = {"default"};
        String text = FileTool.getFileString(filePath);
        stringArray = text.split("\n");
        return stringArray;
    }

    public static void saveFile(String filePath, String stringValue) {
        FileWriter fileWriter = new FileWriter(filePath, stringValue, false);
        fileWriter.run();
        //customThreadPool.execute(fileWriter);
    }

    public static void saveFile(String filePath, String[] stringArray) {
        for (String text : stringArray) {
            FileWriter fileWriter = new FileWriter(filePath, text, true);
            fileWriter.run();
        }
    }

    public static void saveFile(String filePath, String[][] stringArray) {
        String tempValue;
        for (int i = 0; i < stringArray.length; i++) {
            tempValue = "";
            for (int j = 0; j < stringArray[i].length; j++) {
                tempValue = tempValue + stringArray[i][j].trim();
                if (j < stringArray[i].length - 1) {
                    tempValue = tempValue + "\t";
                }
            }
            FileWriter fileWriter = new FileWriter(filePath, tempValue, true);
            fileWriter.run();
            //customThreadPool.execute(fileWriter);
        }
    }

    public static String[][] get2DArray(String filePath) {
        String fileString = FileTool.getFileString(filePath);
        String[] rows = fileString.split("\n");
        int maxCols = DataTool.getMaxCols(rows);
        String[][] stringArray = new String[rows.length][maxCols];
        String[] row;
        for (int i = 0; i < rows.length; i++) {
            if (rows[i].contains("\t")) {
                row = rows[i].split("\t");
                for (int j = 0; j < maxCols; j++) {
                    if (j < row.length) {
                        stringArray[i][j] = row[j];
                    } else {
                        stringArray[i][j] = "default";
                    }
                }
            } else if (rows[i].contains(",")) {
                row = rows[i].split(",");
                for (int j = 0; j < maxCols; j++) {
                    if (j < row.length) {
                        stringArray[i][j] = row[j];
                    } else {
                        stringArray[i][j] = "default";
                    }
                }
            }
        }
        return stringArray;
    }

    public static void saveFile(String filePath, String[][] stringArray, boolean append) {
        String tempValue;
        for (int i = 0; i < stringArray.length; i++) {
            tempValue = "";
            for (int j = 0; j < stringArray[i].length; j++) {
                tempValue = tempValue + stringArray[i][j].trim();
                if (j < stringArray[i].length - 1) {
                    tempValue = tempValue + "\t";
                }
            }
            FileWriter fileWriter = new FileWriter(filePath, tempValue, append);
            fileWriter.run();
        }
    }

    public static void saveFile(String filePath, String[] stringArray, boolean append) {
        String tempValue = "";
        for (int i = 0; i < stringArray.length; i++) {
            tempValue = tempValue + stringArray[i].trim();
            if (i < stringArray.length - 1) {
                tempValue = tempValue + "\t";
            }
        }
        FileWriter fileWriter = new FileWriter(filePath, tempValue, append);
        fileWriter.run();
        //customThreadPool.execute(fileWriter);
    }

    public static String[] getPathNames(String dirPath) {
        String[] pathnames;

        // Creates a new File instance by converting the given pathname string
        // into an abstract pathname
        File f = new File(dirPath);

        // Populates the array with names of files and directories
        pathnames = f.list();

        // For each pathname in the pathnames array
        for (String pathname : pathnames) {
            // Print the names of files and directories
            System.out.println(pathname);
        }
        return pathnames;
    }

    public static String getFileName(String filePath) {
        int endPos = filePath.length();
        for (int currentPos = endPos - 1; currentPos > 0; currentPos--) {
            char character = filePath.charAt(currentPos);
            if (character == '/') {
                filePath = filePath.substring(currentPos + 1, endPos);
                return filePath;
            }
        }
        return filePath;
    }

    public static String[] getAllFileNames(String dir) {
        String fullFilePath = getFullFilePath(dir);
        File fileDir = new File(fullFilePath);
        File[] files = fileDir.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        String[] fileNames = new String[1];

        if (files != null) {
            fileNames = new String[files.length];
            int count = 0;
            for (File thisFile : files) {
                fileNames[count++] = thisFile.getPath();
            }
        }
        return fileNames;
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        Path path = file.toPath();
        try {
            if (file.exists()) {
                Files.delete(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[][] getCSV(String filePath) {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(getFullFilePath(filePath)))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int rows = records.size();
        String[] columnNames = String.valueOf(records.get(0)).split(",");
        int numCols = columnNames.length;
        String[][] dataArray = new String[rows][numCols];
        for(int i = 0; i<rows;i++){
            String row = String.valueOf(records.get(i));
            row = row.replace("[", "");
            row = row.replace("]", "");
            String[] values = row.split(",");
            for(int j = 0; j<numCols;j++){
                if(j<values.length){
                    dataArray[i][j] = values[j];
                } else {
                    dataArray[i][j] = "default";
                }
            }
        }
        return dataArray;
    }

    public static String getBasePath(String filePath) {
        filePath = getFullFilePath(filePath);
        File file = new File(filePath);
        String path = file.toPath().getParent().toString();
        return path;
    }
}
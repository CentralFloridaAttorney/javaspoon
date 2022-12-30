package net.centralfloridaattorney.toolbag;


import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class FileWriter implements Runnable {
    public static final String PROJECT_PATH = "/src/main/resources/";
    private volatile static FileWriter thisFileWriter;
    private volatile String filePath;
    private volatile String fileString;
    private volatile boolean append = false;

    public FileWriter(String filePath, String text, boolean append) {
        thisFileWriter = this;
        this.filePath = getFullFilePath(filePath);
        this.fileString = text;
        this.append = append;
    }
    //private static final GlobalThreadPool globalThreadPool = GlobalThreadPool.getInstance();

    public FileWriter(String filePath, String fileString) {
        thisFileWriter = this;
        this.filePath = getFullFilePath(filePath);
        this.fileString = fileString;
    }

    public static void main(String[] args) {
        FileWriter fileWriter = new FileWriter("asdf", "asdfasdfasdfasdf");
        fileWriter.run();
        fileWriter = new FileWriter("asdf", "qwerqwerqwerqwer", true);
        fileWriter.run();
    }

    private synchronized static void writeFile() {
        File file = new File(thisFileWriter.getFilePath());
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NullPointerException npe) {
                System.out.println("FileWriter.writeFile(): " + npe);

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            PrintWriter pr = new PrintWriter(file);
            String fileString = thisFileWriter.getFileString();
            pr.print(fileString);
            pr.close();
        } catch (Exception var5) {
            var5.printStackTrace();
            //System.out.println("No such file exists.");
        }
    }

    private synchronized static String getFullFilePath(String filePath) {
        File file = new File("");
        String absolutePath = file.getAbsolutePath();
        if (!filePath.startsWith(absolutePath)) filePath = absolutePath + PROJECT_PATH + filePath;
        //Absolute file paths beginning with 'C' or 'D' require substitution of '/' with '\\'
        if (filePath.startsWith("C") || filePath.startsWith("D")) filePath = filePath.replace('\\', '/');
        filePath = filePath.replace("\n", "");
        filePath = filePath.replace("\r", "");
        return filePath;
    }

    private synchronized String getFileString() {
        return this.fileString;
    }

    private synchronized String getFilePath() {
        return this.filePath;
    }

    @Override
    public void run() {
        if (append) {
            appendFile();
        } else {
            writeFile();
        }
    }

    private void appendFile() {
        try {
            Files.write(Paths.get(filePath), fileString.getBytes(), StandardOpenOption.APPEND);
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
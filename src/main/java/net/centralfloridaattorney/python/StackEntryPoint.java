package net.centralfloridaattorney.python;

import net.centralfloridaattorney.toolbag.FileTool;
import py4j.GatewayServer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static net.centralfloridaattorney.GLOBAL_VARIABLES.*;

public class StackEntryPoint implements Runnable {
    private Stack stack;
    private PythonLoader pythonLoader;

    @Override
    public void run() {
        int count = 0;
        while (true) {
            try {
                Thread.sleep(1000);
                List<String> stackList = stack.getInternalList();
                try {
                    //pythonMessage = stack.pop();
                } catch (Error e) {

                }
                System.out.println("StackEntryPoint.run() Count: "+count++);
                for (String value : stackList) {
                    System.out.println(value);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //This code appears to be unused, but is called by py4j in IPC.py
    public Stack getStack() {
        if (stack.isEmpty()) {
            stack.push(DEFAULT_VALUE);
        }
        return stack;
    }

    public String getStackString() {
        String stackString = DEFAULT_VALUE;
        List<String> stackList = stack.getInternalList();
        if (!stackList.equals(null)) {
            stackString = "";
            for (String value : stackList) {
                stackString = stackString + value + "\n";
            }
        }
        return stackString;
    }

    //If stack isEmpty then value is added and returns true
    public boolean put(String value) {
        return stack.push(value);
    }

    public static void main(String[] args) {
        StackEntryPoint stackEntryPoint = new StackEntryPoint();
        stackEntryPoint.run();
        System.out.println("Gateway Server Started");
    }

    public int getCurrentIndex() {
        return pythonLoader.currentIndex;
    }

    public String getConsole() {
        String lines = DEFAULT_VALUE;
        try {
            ProcessBuilder pb = new ProcessBuilder(Arrays.asList(PYTHON_PATH, PYTHON_FILE));
            Process p = pb.start();

            BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            lines = "";
            System.out.println("Running Python starts: " + line);
            int exitCode = p.waitFor();
            System.out.println("Exit Code : " + exitCode);
            line = bfr.readLine();
            System.out.println("First Line: " + line);
            while ((line = bfr.readLine()) != null) {
                lines = lines + line + "\n";
                System.out.println("Python Output: " + line);


            }


        } catch (Exception e) {
            System.out.println(e);
        }
        return lines;
    }

    public void loadPythonFile(String pythonPath) {
        pythonLoader.loadFile(pythonPath);
    }

    public String execute() {
        String command = DEFAULT_VALUE;
        if (stack.isEmpty()) {
            command = getNextCommand();
            stack.push(command);
        }
        return command;
    }

    private String getNextCommand() {
        if (stack.isEmpty()) {
            String nextCommand = pythonLoader.getNextCommand();
            stack.push(nextCommand);
            return nextCommand;
        } else {
            return DEFAULT_VALUE;
        }
    }

    public StackEntryPoint() {
        pythonLoader = new PythonLoader(PYTHON_FILE);
        GatewayServer gatewayServer = new GatewayServer(this);
        gatewayServer.start();
        stack = new Stack();
        stack.push("print('I love John M. Iriye, Esq.')");
    }

    private class PythonLoader {
        String[] fileArray;
        private int currentIndex;

        public void loadFile(String filePath) {
            init(filePath);
        }

        public String getNextCommand() {
            if (stack.isEmpty()) {
                String command = fileArray[currentIndex++];
                if (null==command) {
                    command = "print('John Matthew Iriye is Awesome!')";
                }
                return command;
            } else {
                return DEFAULT_VALUE;
            }
        }

        private void init(String filePath) {
            //fileArray = FileTool.getCSVString(filePath);
            fileArray = FileTool.getFileString(filePath).split(" ");
            currentIndex = 1;
        }

        public PythonLoader(String filePath) {
            if (null!=filePath) {
                filePath = PYTHON_FILE;
            }
            init(filePath);
            System.out.println("Finished PythonLoader()");
        }
    }

	public String[] getCommands() {
		return pythonLoader.fileArray;
	}
}
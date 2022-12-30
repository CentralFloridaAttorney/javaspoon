package net.centralfloridaattorney;

import net.centralfloridaattorney.python.PythonJavaBridgeGUI;
import net.centralfloridaattorney.python.StackEntryPoint;

import javax.swing.JFrame;

public class PythonJavaBridgeMain implements Runnable {
    private static PythonJavaBridgeGUI pythonJavaBridgeGUI;
    private StackEntryPoint stackEntryPoint;

    @Override
    public void run() {
        int count = 0;
        while (true) {
            try {
                System.out.println("MiniMain.run() count: " + count);
                Thread.sleep(1000);
                count++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void execute(String message) {
        //boolean isMessageSet = stackEntryPoint.put(message);
    }

    public static void main(String[] args) {
        PythonJavaBridgeMain miniMain = new PythonJavaBridgeMain();
        miniMain.run();
    }

    public String getStack() {
        //Stack stack = stackEntryPoint.getStack();
        String stackString = stackEntryPoint.getStackString();
        return stackString;
    }

    public String getConsole() {
        String consoleString = stackEntryPoint.getConsole();
        return consoleString;
    }

    public String[] getCommands() {
        String[] commands = stackEntryPoint.getCommands();
        return commands;
    }

    public void loadPythonFile(String pythonPath) {
        //pythonLoader.loadFile(pythonPath);
        stackEntryPoint.loadPythonFile(pythonPath);
        System.out.println("PythonJavaBridge.loadPythonFile(pythonPath) Finished!");
    }

    public String execute() {
        //Update currentIndex
        int currentIndex = stackEntryPoint.getCurrentIndex();
        pythonJavaBridgeGUI.setCurrentIndex(String.valueOf(currentIndex));
        //Execute the command
        String commandString = stackEntryPoint.execute();
        return commandString;
    }

    private void setCentralGUI(PythonJavaBridgeGUI pythonGUIX) {
        pythonJavaBridgeGUI = pythonGUIX;
    }

    public PythonJavaBridgeMain() {
        stackEntryPoint = new StackEntryPoint();
        new Thread(new Runnable() {
            @Override
            public void run() {
                stackEntryPoint.run();
            }
        });
        pythonJavaBridgeGUI = new PythonJavaBridgeGUI();
        setCentralGUI(pythonJavaBridgeGUI);
        pythonJavaBridgeGUI.setMain(this);
        JFrame frame = new JFrame("CentralFloridaAttorney.net");
        frame.setContentPane(pythonJavaBridgeGUI.getJpContent());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public PythonJavaBridgeMain(PythonJavaBridgeGUI pythonJavaBridgeGUI) {
        stackEntryPoint = new StackEntryPoint();
        new Thread(new Runnable() {
            @Override
            public void run() {
                stackEntryPoint.run();
            }
        });
        pythonJavaBridgeGUI = new PythonJavaBridgeGUI();
        setCentralGUI(pythonJavaBridgeGUI);
        pythonJavaBridgeGUI.setMain(this);
        JFrame frame = new JFrame("CentralFloridaAttorney.net");
        frame.setContentPane(pythonJavaBridgeGUI.getJpContent());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}

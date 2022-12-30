package net.centralfloridaattorney.python;

import net.centralfloridaattorney.PythonJavaBridgeMain;
import net.centralfloridaattorney.gui.JPValueEditor;
import net.centralfloridaattorney.toolbag.FileTool;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.Arrays;

public class PythonJavaBridgeGUI implements Runnable{
    private JTabbedPane jtpCentralGUI;
    private JButton toggleIsIteratingButton;
    private JPanel jpContent;
    private JPanel jpModelMaker;
    private JButton setImageButton;
    private JButton toggleIsFittingAButton;
    private JButton toggleIsDrawingModelAButton;
    private JButton startUIButton;
    private JTextField jtfLearningRate;
    private JButton newModelAButton;
    private JCheckBox backPropagateCheckBox;
    private JCheckBox preTrainCheckBox;
    private JTextField jtfHiddenNodes;
    private JTextField jtfLargeNodes;
    private JTextField jtfSmallNodes;
    private JTextField jtfInputs;
    private JTextField jtfOutputs;
    private JTextField jtfBottleneck;
    private JTextField jtfSeed;
    private JTextField jtfMomentum;
    private JTextField jtfWeightInit;
    private JTextField jtfActivation;
    private JTextField jtfLossFunction;
    private JButton updateGUIButton;
    private JPanel jpDataSet;
    private JButton setBufferedImageButton;
    private JButton loadImageButton;
    private JPanel jpModelA;
    private JTextField jtfModelAPath;
    private JPanel jpModelAControls;
    private JPanel jpModelB;
    private JPanel jpModelBControls;
    private JTextField jtfModelBPath;
    private JTextField jtfImagePath;
    private JPanel jpMain;
    private JPanel jpMainControls;
    private JPanel jpModelMakerControls;
    private JPanel jpContexts;
    private JPanel jpContextsControlls;
    private JTextField jtfContextFilePath;
    private JTextField jtfZoom;
    private JButton newModelBButton;
    private JButton toggleIsFittingBButton;
    private JButton toggleIsDrawingModelBButton;
    private JButton startUIBButton;
    private JPanel jpPython;
    private JButton jbtnPush;
    private JTextField jtfPush;
    private JButton jbtnGetMessage;
    private JTextField jtfPop;
    private JButton jbtnGetStack;
    private JTextArea jtaStack;
    private JButton jbtnGetConsole;
    private JTextArea jtaCommands;
    private JButton jbtnLoadCommands;
    private JButton jbtnExecute;
    private JTextField jtfExecute;
    private JTextField jtfLoadPython;
    private JButton jbtnLoadPythonFile;
    private JButton jbtnExecuteCommands;
    private JTextField jtfCurrentIndex;
    private JList jlCommands;
    private JTextField jtfSteps;
    private JPValueEditor jpveCommands;
    private PythonJavaBridgeMain main;

    public JCheckBox getBackPropagateCheckBox() {
        return backPropagateCheckBox;
    }

    public JPanel getJpContent() {
        return jpContent;
    }

    public JTextField getJtfZoom() {
        return jtfZoom;
    }


    public void setMain(PythonJavaBridgeMain miniMainX) {
        main = miniMainX;

    }


    public static void main(String[] args) {
        JFrame frame = new JFrame("CentralFloridaAttorney.net");
        frame.setContentPane(new PythonJavaBridgeGUI().jpContent);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public void setCurrentIndex(String valueOf) {
        jtfCurrentIndex.setText(valueOf);
    }

    @Override
    public void run() {
        int count = 0;
        while(true){
            try {
                System.out.println("CentralGui.run() count: "+count);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
        }
    }

    private void createUIComponents(){
        String[] data = {"one", "two", "three", "four"};
        jlCommands = new JList(data);
    }

    private void setTextFieldDir(JTextField jTextField, String baseDir) {
        JFileChooser jFileChooser = new JFileChooser(baseDir);
        jFileChooser.showOpenDialog(jpContent);
        jTextField.setText(jFileChooser.getSelectedFile().toString());
    }

    private void init(){

    }

    public PythonJavaBridgeGUI() {
        init();

        jbtnGetMessage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //String message = miniMain.getMessage();
                //jtfPop.setText(message);
            }
        });
        jbtnGetStack.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String stack = main.getStack();
                jtaStack.setText(stack);
            }
        });
        jbtnGetConsole.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String consoleString = main.getConsole();
                jtaCommands.setText(consoleString);
            }
        });
        jbtnLoadCommands.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String[] commands = main.getCommands();
                jlCommands.setListData(commands);
                String commandString = Arrays.toString(commands);
                jtaCommands.setText(commandString);
            }
        });
        jbtnExecute.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //String commandString = miniMain.getNextCommand();
                String commandString = main.execute();

                jtfExecute.setText(commandString);
                //miniMain.execute(commandString);
            }
        });
        jtfLoadPython.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //final String filePath = "D:\\Projects\\_Quiskit\\src\\main\\resources\\Python";
                URL url = getClass().getResource("/");
                File file = new File(url.getPath());
                setTextFieldDir((JTextField) e.getSource(), file.getPath());
            }
        });
        jbtnLoadPythonFile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                String pythonPath = jtfLoadPython.getText();
                main.loadPythonFile(pythonPath);
                /**
                if (pythonPath.contains("D:")) {
                    miniMain.loadPythonFile(pythonPath);
                }
                **/
            }
        });
        jbtnExecuteCommands.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                //miniMain.doAllCommands();
            }
        });
		jbtnPush.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				String message = jtfPush.getText();
				main.execute(message);
			}
		});
	}

	private void updateGUI(){

    }
}

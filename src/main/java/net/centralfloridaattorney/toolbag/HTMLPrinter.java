package net.centralfloridaattorney.toolbag;
import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;


public class HTMLPrinter extends JPanel {
    static String htmlString = "<html>\n"
            + "<body>\n"
            + "<h1>IN THE CIRCUIT COURT OF SEMINOLE COUNTY, FLORIDA, IN AND FOR THE EIGHTEENTH JUDICIAL CIRCUIT </h1>\n"
            + "<h2>Landlord v. Tenant</h2>\n"
            + "<p>3-DAY NOTICE</p>\n"
            + "<p><a href=\"http://www.conversex.com/\">ConversEx</a></p>\n"
            + "</body>\n";
    private int pageWidth = 612;
    private int pageHeight = 828;
    private String pdfFilePath = "html/pdfs/Reply.pdf";

    public static void main(String[] args) {
        // create some simple html as a string

        new HTMLPrinter(htmlString);
    }

    public HTMLPrinter(String htmlString) {
        SwingUtilities.invokeLater(() -> {
            // create jeditorpane
            JEditorPane jEditorPane = new JEditorPane();

            // make it read-only
            jEditorPane.setEditable(false);

            // create a scrollpane; modify its attributes as desired
            JScrollPane scrollPane = new JScrollPane(jEditorPane);

            // add an html editor kit
            HTMLEditorKit kit = new HTMLEditorKit();
            jEditorPane.setEditorKit(kit);

            // add some styles to the html
            StyleSheet styleSheet = kit.getStyleSheet();
            styleSheet.addRule("body {color:black; font-family:times; margin: 4px; }");
            styleSheet.addRule("h1 {color: black;}");
            styleSheet.addRule("h2 {color: black;}");
            styleSheet.addRule("pre {font : 10px monaco; color : black; background-color : #fafafa; }");


            // create a document, set it on the jeditorpane, then add the html
            Document doc = kit.createDefaultDocument();
            jEditorPane.setDocument(doc);
            jEditorPane.setText(htmlString);

            // now add it all to a frame
            JFrame j = new JFrame("HtmlEditorKit Test");
            j.getContentPane().add(scrollPane, BorderLayout.CENTER);

            // make it easy to close the application
            j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // display the frame
            j.setSize(new Dimension(pageWidth, pageHeight));

            Action printAction = new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Save Action Button Pressed");
                    try {
                        jEditorPane.print();
                    } catch (PrinterException e1) {
                        e1.printStackTrace();
                    }
                }

                {
                    putValue(Action.NAME, "Print");
                }
            };

            JButton printButton = new JButton(printAction);
            j.add(printButton, BorderLayout.SOUTH);

            // pack it, if you prefer
            //j.pack();

            // center the jframe, then make it visible
            j.setLocationRelativeTo(null);
            j.setVisible(true);
        });
    }
}
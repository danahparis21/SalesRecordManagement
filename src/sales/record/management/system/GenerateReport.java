package sales.record.management.system;
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;
import java.util.Date;

public class GenerateReport {

    public GenerateReport(double totalRevenue, String topProduct, int topProductUnits, String lowStockAlert,
                      double weeklyRevenue, String topSalesperson, double totalSales) {
        JFrame reportFrame = new JFrame("Sales Report - Grocery Store");
        reportFrame.setSize(500, 700);
        reportFrame.setLayout(null);

        JTextArea reportDetails = new JTextArea();
        reportDetails.setEditable(false);
        reportDetails.setFont(new Font("Helvetica", Font.PLAIN, 14));
        reportDetails.setBounds(20, 20, 440, 620);

        // Start building the report text
        StringBuilder reportText = new StringBuilder();
        reportText.append("\t----- Grocery Store - Sales Report -----\n\n")
                .append(" Sales Dashboard:\n")
                .append(" Total Revenue: ₱").append(String.format("%.2f", totalRevenue)).append("\n")
                .append(" Top Product: ").append(topProduct).append(" (").append(topProductUnits).append(" units)\n")
                .append(" Low Stock Alert: ").append(lowStockAlert).append("\n\n");

      
        reportText.append(" Top Salesperson:\n")
                .append(" Name: ").append(topSalesperson).append("\n");
                

        reportText.append("\tThank you for reviewing the report!\n");

        reportDetails.setText(reportText.toString());

        JScrollPane scrollPane = new JScrollPane(reportDetails);
        scrollPane.setBounds(20, 20, 440, 620);

        reportFrame.add(scrollPane);

        JButton printButton = new JButton("Print Report");
        printButton.setBounds(180, 650, 120, 30);
        printButton.addActionListener(e -> printReport(reportText.toString()));  
        reportFrame.add(printButton);

        reportFrame.setVisible(true);
        reportFrame.setSize(500, 750);
    }


    private void printReport(String reportContent) {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
                if (page > 0) {
                    return NO_SUCH_PAGE; 
                }

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pf.getImageableX(), pf.getImageableY());
                g2d.setFont(new Font("Serif", Font.PLAIN, 12));
                FontMetrics metrics = g2d.getFontMetrics();

                
                int lineHeight = metrics.getHeight();
                int margin = 20;
                int x = margin;
                int y = margin + lineHeight;

                String[] lines = reportContent.split("\n");  
                for (String line : lines) {
                    if (metrics.stringWidth(line) > pf.getImageableWidth() - 2 * margin) {
                        String[] wrappedLines = wrapText(line, g2d, pf.getImageableWidth() - 2 * margin);
                        for (String wrappedLine : wrappedLines) {
                            g2d.drawString(wrappedLine, x, y);
                            y += lineHeight; 
                        }
                    } else {
                        g2d.drawString(line, x, y);
                        y += lineHeight;  
                    }

                  
                    if (y > pf.getImageableHeight() - lineHeight) {
                        return PAGE_EXISTS;
                    }
                }

                return PAGE_EXISTS;
            }
        });

        if (printerJob.printDialog()) {
            try {
                printerJob.print();
            } catch (PrinterException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error printing the report.");
            }
        }
    }

   
    private String[] wrapText(String text, Graphics2D g2d, double pageWidth) {
        FontMetrics metrics = g2d.getFontMetrics();
        int lineLength = (int) (pageWidth / metrics.charWidth('M'));  
        String[] words = text.split(" ");
        StringBuilder currentLine = new StringBuilder();
        StringBuilder wrappedText = new StringBuilder();

        for (String word : words) {
            if (metrics.stringWidth(currentLine.toString() + " " + word) <= pageWidth) {
                if (currentLine.length() > 0) {
                    currentLine.append(" ");
                }
                currentLine.append(word);
            } else {
                wrappedText.append(currentLine).append("\n");
                currentLine = new StringBuilder(word);
            }
        }

     
        if (currentLine.length() > 0) {
            wrappedText.append(currentLine);
        }

        return wrappedText.toString().split("\n");
    }
}


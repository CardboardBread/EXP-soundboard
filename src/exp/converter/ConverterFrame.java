 package exp.converter;
 
 import exp.gui.SoundboardFrame;
 import exp.soundboard.Utils;
 import it.sauronsoftware.jave.EncoderProgressListener;
 import it.sauronsoftware.jave.MultimediaInfo;
 import java.awt.Container;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.io.File;
 import java.io.PrintStream;
 import javax.swing.ButtonGroup;
 import javax.swing.JButton;
 import javax.swing.JFileChooser;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JOptionPane;
 import javax.swing.JRadioButton;
 import javax.swing.JSeparator;
 import javax.swing.filechooser.FileFilter;
 import net.miginfocom.swing.MigLayout;
 
 
 
 
 
 
 
 
 
 
 
 
 public class ConverterFrame
   extends JFrame
 {
   private static final long serialVersionUID = -6720455160041920802L;
   private File[] inputfiles;
   private File outputfile;
   private JLabel inputFileLabel;
   private JLabel outputFileLabel;
   private JButton changeOutputButton;
   private JRadioButton mp3RadioButton;
   private JRadioButton wavRadioButton;
   private JLabel encodingProgressLabel;
   private JLabel encodingMessageLabel;
   private JButton convertButton;
   
   public ConverterFrame()
   {
     setResizable(false);
     setDefaultCloseOperation(2);
     setTitle("EXP soundboard : Audio Converter");
     setIconImage(SoundboardFrame.icon);
     
     JLabel lblInputFile = new JLabel("Input Files:");
     
     this.inputFileLabel = new JLabel("none selected");
     
     JButton selectInputButton = new JButton("Select");
     selectInputButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         JFileChooser fc = Utils.getFileChooser();
         fc.setFileFilter(null);
         fc.setMultiSelectionEnabled(true);
         int session = fc.showDialog(null, "Select");
         if (session == 0) {
           ConverterFrame.this.inputfiles = fc.getSelectedFiles();
           if (ConverterFrame.this.inputfiles.length > 1) {
             ConverterFrame.this.outputfile = ConverterFrame.this.inputfiles[0];
             ConverterFrame.this.inputFileLabel.setText("Multiple files");
             ConverterFrame.this.renameOutputForFormat();
             ConverterFrame.this.outputFileLabel.setText(ConverterFrame.this.outputfile.getAbsolutePath());
           } else {
             ConverterFrame.this.outputfile = ConverterFrame.this.inputfiles[0];
             ConverterFrame.this.renameOutputForFormat();
             ConverterFrame.this.inputFileLabel.setText(ConverterFrame.this.inputfiles[0].getAbsolutePath());
             ConverterFrame.this.outputFileLabel.setText(ConverterFrame.this.outputfile.getAbsolutePath());
           }
           ConverterFrame.this.changeOutputButton.setEnabled(true);
           ConverterFrame.this.convertButton.setEnabled(true);
         }
         fc.setMultiSelectionEnabled(false);
         ConverterFrame.this.pack();
       }
       
     });
     JSeparator separator = new JSeparator();
     
     JLabel lblOutputFile = new JLabel("Output File:");
     
     this.outputFileLabel = new JLabel("none selected");
     
     this.changeOutputButton = new JButton("Change");
     this.changeOutputButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         if ((ConverterFrame.this.inputfiles != null) && (ConverterFrame.this.outputfile != null)) {
           JFileChooser fc = Utils.getFileChooser();
           fc.setMultiSelectionEnabled(false);
           if (ConverterFrame.this.inputfiles.length > 1) {
             fc.setFileFilter(new FileFilter()
             {
               public boolean accept(File f) {
                 return f.isDirectory();
               }
               
               public String getDescription() {
                 return "Folders only";
               }
             });
             fc.setSelectedFile(ConverterFrame.this.outputfile);
             fc.setFileSelectionMode(1);
           } else {
             fc.setFileFilter(null);
             fc.setSelectedFile(ConverterFrame.this.outputfile);
           }
           int session = fc.showSaveDialog(null);
           if (session == 0) {
             ConverterFrame.this.outputfile = fc.getSelectedFile();
             System.out.println("change: " + ConverterFrame.this.outputfile.getAbsolutePath());
             if (ConverterFrame.this.inputfiles.length < 2) {
               ConverterFrame.this.renameOutputForFormat();
             } else {
               ConverterFrame.this.outputFileLabel.setText(ConverterFrame.this.outputfile.getAbsolutePath());
             }
           }
           fc.setFileSelectionMode(0);
           ConverterFrame.this.pack();
         }
       }
     });
     this.changeOutputButton.setEnabled(false);
     
     JLabel lblOutputFormat = new JLabel("Output Format:");
     
     this.mp3RadioButton = new JRadioButton("MP3");
     this.mp3RadioButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         ConverterFrame.this.renameOutputForFormat();
       }
     });
     this.mp3RadioButton.setSelected(true);
     
     this.wavRadioButton = new JRadioButton("WAV");
     this.wavRadioButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         ConverterFrame.this.renameOutputForFormat();
       }
       
     });
     ButtonGroup buttonGroup = new ButtonGroup();
     buttonGroup.add(this.mp3RadioButton);
     buttonGroup.add(this.wavRadioButton);
     
 
     this.convertButton = new JButton("Convert");
     this.convertButton.setEnabled(false);
     this.convertButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         ConverterFrame.this.convertAction();
       }
       
     });
     JSeparator separator_1 = new JSeparator();
     
     JLabel lblEncodingProgress = new JLabel("Encoding Progress:");
     
     JLabel lblEncodingMessages = new JLabel("Encoding Messages:");
     
     this.encodingProgressLabel = new JLabel("0%");
     
     this.encodingMessageLabel = new JLabel("");
     getContentPane().setLayout(new MigLayout("", "[45px][2px][14px][2px][34px][1px][33px][222px][71px]", "[14px][23px][2px][14px][23px][14px][23px][2px][14px][14px]"));
     getContentPane().add(separator_1, "cell 0 7 9 1,growx,aligny top");
     getContentPane().add(separator, "cell 0 2 9 1,growx,aligny top");
     getContentPane().add(lblInputFile, "cell 0 0 3 1,alignx left,aligny top");
     getContentPane().add(this.inputFileLabel, "cell 4 0 3 1,alignx right,aligny top");
     getContentPane().add(selectInputButton, "cell 0 1 3 1,alignx left,aligny top");
     getContentPane().add(lblOutputFile, "cell 0 3 3 1,alignx left,aligny top");
     getContentPane().add(this.outputFileLabel, "cell 4 3 3 1,alignx left,aligny top");
     getContentPane().add(this.changeOutputButton, "cell 0 4 5 1,alignx left,aligny top");
     getContentPane().add(lblOutputFormat, "cell 0 5 5 1,alignx left,aligny top");
     getContentPane().add(this.mp3RadioButton, "cell 0 6,alignx left,aligny top");
     getContentPane().add(this.wavRadioButton, "cell 2 6 3 1,alignx left,aligny top");
     getContentPane().add(this.convertButton, "cell 8 6,alignx left,aligny top");
     getContentPane().add(lblEncodingProgress, "cell 0 8 5 1,alignx right,aligny top");
     getContentPane().add(this.encodingProgressLabel, "cell 6 8,alignx left,aligny top");
     getContentPane().add(lblEncodingMessages, "cell 0 9 5 1,alignx right,aligny top");
     getContentPane().add(this.encodingMessageLabel, "cell 6 9,alignx left,aligny top");
     
     pack();
     setLocationRelativeTo(null);
     setVisible(true);
   }
   
 
 
 
 
   private void convertAction()
   {
     boolean cont = true;
     if (this.outputfile.equals(this.inputfiles[0])) {
       JOptionPane.showMessageDialog(null, "Input and output files cannot be the same", "File parsing error", 1);
       cont = false;
     }
     if ((cont) && (this.outputfile.exists())) {
       int session = JOptionPane.showConfirmDialog(null, "Output file already exists. Overwrite?", 
         "Overwrite confirmation", 1);
       if (session != 0)
       {
 
         cont = false;
       }
     }
     
     if (cont) {
       if (this.mp3RadioButton.isSelected()) {
         if (this.inputfiles.length > 1) {
           AudioConverter.batchConvertToMP3(this.inputfiles, this.outputfile, new ConvertProgressListener(null));
         } else {
           AudioConverter.convertToMP3(this.inputfiles[0], this.outputfile, new ConvertProgressListener(null));
         }
       } else if (this.wavRadioButton.isSelected()) {
         if (this.inputfiles.length > 1) {
           AudioConverter.batchConvertToWAV(this.inputfiles, this.outputfile, new ConvertProgressListener(null));
         } else {
           AudioConverter.convertToWAV(this.inputfiles[0], this.outputfile, new ConvertProgressListener(null));
         }
       }
       this.convertButton.setEnabled(false);
     }
   }
   
 
 
   private void renameOutputForFormat()
   {
     if (this.inputfiles.length > 1) {
       if (!this.outputfile.isDirectory()) {
         String inputabs = this.inputfiles[0].getAbsolutePath();
         int slash = inputabs.lastIndexOf(File.separator);
         String output = inputabs.substring(0, slash + 1) + "Converted";
         this.outputfile = new File(output);
         this.outputFileLabel.setText(this.outputfile.getAbsolutePath());
       }
     } else {
       String outputfileabs = this.outputfile.getAbsolutePath();
       int period = outputfileabs.lastIndexOf('.');
       if (period > 0) {
         outputfileabs = outputfileabs.substring(0, period);
         if (this.mp3RadioButton.isSelected()) {
           outputfileabs = outputfileabs + ".mp3";
         } else if (this.wavRadioButton.isSelected()) {
           outputfileabs = outputfileabs + ".wav";
         }
       }
       this.outputfile = new File(outputfileabs);
       this.outputFileLabel.setText(this.outputfile.getAbsolutePath());
     }
   }
   
 
 
 
 
   private class ConvertProgressListener
     implements EncoderProgressListener
   {
     int current = 1;
     
     private ConvertProgressListener() {}
     
     public void message(String m) { if ((ConverterFrame.this.inputfiles.length > 1) && 
         (this.current < ConverterFrame.this.inputfiles.length)) {
         ConverterFrame.this.encodingMessageLabel.setText(this.current + "/" + ConverterFrame.this.inputfiles.length);
       }
     }
     
 
     public void progress(int p)
     {
       float progress = p / 10;
       ConverterFrame.this.encodingProgressLabel.setText(progress + "%");
       if (p >= 1000) {
         if (ConverterFrame.this.inputfiles.length > 1) {
           this.current += 1;
           if (this.current > ConverterFrame.this.inputfiles.length) {
             ConverterFrame.this.encodingMessageLabel.setText("Encoding Complete!");
             ConverterFrame.this.convertButton.setEnabled(true);
           }
         }
         else if (p == 1001) {
           ConverterFrame.this.encodingMessageLabel.setText("Encoding Failed!");
           ConverterFrame.this.convertButton.setEnabled(true);
         } else {
           ConverterFrame.this.encodingMessageLabel.setText("Encoding Complete!");
           ConverterFrame.this.convertButton.setEnabled(true);
         }
       }
     }
     
     public void sourceInfo(MultimediaInfo m) {}
   }
 }

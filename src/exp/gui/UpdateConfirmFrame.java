 package exp.gui;
 
 import java.awt.Container;
 import java.awt.Desktop;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.io.IOException;
 import java.net.URI;
 import java.net.URISyntaxException;
 import javax.swing.GroupLayout;
 import javax.swing.GroupLayout.Alignment;
 import javax.swing.GroupLayout.ParallelGroup;
 import javax.swing.GroupLayout.SequentialGroup;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JScrollPane;
 import javax.swing.JTextPane;
 import javax.swing.LayoutStyle.ComponentPlacement;
 
 
 
 
 
 
 public class UpdateConfirmFrame
   extends JFrame
 {
   private static final long serialVersionUID = -6700862565543741036L;
   private static final String url = "https://sourceforge.net/projects/expsoundboard/";
   private JTextPane textPane;
   
   public UpdateConfirmFrame(String updateNotes)
   {
     setResizable(false);
     setDefaultCloseOperation(2);
     setTitle("Update Available!");
     
     JLabel lblSoundboardUpdateAvailable = new JLabel("EXP Soundboard Update Available");
     
     JScrollPane scrollPane = new JScrollPane();
     
     JButton btnClose = new JButton("Close");
     btnClose.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         UpdateConfirmFrame.this.dispose();
       }
       
     });
     JButton btnGetUpdate = new JButton("Get Update");
     btnGetUpdate.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         try {
           Desktop.getDesktop().browse(new URI("https://sourceforge.net/projects/expsoundboard/"));
         } catch (IOException e1) {
           e1.printStackTrace();
         } catch (URISyntaxException e1) {
           e1.printStackTrace();
         }
         UpdateConfirmFrame.this.dispose();
       }
       
     });
     final JCheckBox chckbxCheckForUpdates = new JCheckBox("Check for Updates on launch");
     chckbxCheckForUpdates.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         SoundboardFrame.updateCheck = !SoundboardFrame.updateCheck;
         chckbxCheckForUpdates.setSelected(SoundboardFrame.updateCheck);
       }
     });
     chckbxCheckForUpdates.setSelected(SoundboardFrame.updateCheck);
     GroupLayout groupLayout = new GroupLayout(getContentPane());
     groupLayout.setHorizontalGroup(
       groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(groupLayout.createSequentialGroup()
       .addContainerGap()
       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
       .addComponent(scrollPane, -1, 480, 32767)
       .addComponent(lblSoundboardUpdateAvailable)
       .addGroup(groupLayout.createSequentialGroup()
       .addComponent(chckbxCheckForUpdates)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 161, 32767)
       .addComponent(btnGetUpdate)
       .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
       .addComponent(btnClose)))
       .addContainerGap()));
     
     groupLayout.setVerticalGroup(
       groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(groupLayout.createSequentialGroup()
       .addContainerGap()
       .addComponent(lblSoundboardUpdateAvailable)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addComponent(scrollPane, -2, 124, -2)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
       .addComponent(btnClose)
       .addComponent(chckbxCheckForUpdates)
       .addComponent(btnGetUpdate))
       .addContainerGap(78, 32767)));
     
 
     this.textPane = new JTextPane();
     this.textPane.setEditable(false);
     this.textPane.setText(updateNotes);
     scrollPane.setViewportView(this.textPane);
     getContentPane().setLayout(groupLayout);
     pack();
     setLocationRelativeTo(null);
     setVisible(true);
   }
 }

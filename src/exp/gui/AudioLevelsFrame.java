 package exp.gui;
 
 import exp.soundboard.AudioManager;
 import exp.soundboard.Utils;
 import java.awt.Color;
 import java.awt.Container;
 import javax.swing.GroupLayout;
 import javax.swing.GroupLayout.Alignment;
 import javax.swing.GroupLayout.ParallelGroup;
 import javax.swing.GroupLayout.SequentialGroup;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JSeparator;
 import javax.swing.JSlider;
 import javax.swing.LayoutStyle.ComponentPlacement;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 
 
 
 
 
 
 public class AudioLevelsFrame
   extends JFrame
 {
   private static AudioLevelsFrame instance = null;
   private static final long serialVersionUID = 464347549019590824L;
   private JSlider primarySlider;
   private JSlider secondarySlider;
   private JSlider micinjectorSlider;
   
   private AudioLevelsFrame()
   {
     setTitle("Audio Gain Controls");
     setResizable(false);
     setDefaultCloseOperation(2);
     setIconImage(SoundboardFrame.icon);
     
     JLabel lblPrimaryOutputGain = new JLabel("Primary Output Gain:");
     
     int primaryGain = (int)AudioManager.getFirstOutputGain();
     int secondaryGain = (int)AudioManager.getSecondOutputGain();
     int micInjectorGain = (int)Utils.getMicInjectorGain();
     
     this.primarySlider = new JSlider();
     this.primarySlider.addChangeListener(new ChangeListener() {
       public void stateChanged(ChangeEvent e) {
         if (!AudioLevelsFrame.this.primarySlider.getValueIsAdjusting()) {
           float gain = AudioLevelsFrame.this.primarySlider.getValue();
           AudioManager.setFirstOutputGain(gain);
         }
       }
     });
     this.primarySlider.setMajorTickSpacing(6);
     this.primarySlider.setPaintLabels(true);
     this.primarySlider.setPaintTicks(true);
     this.primarySlider.setSnapToTicks(true);
     this.primarySlider.setMinorTickSpacing(1);
     this.primarySlider.setValue(0);
     this.primarySlider.setMinimum(-66);
     this.primarySlider.setMaximum(6);
     
     JSeparator separator = new JSeparator();
     separator.setForeground(Color.BLACK);
     
     JLabel lblSecondaryOutputGain = new JLabel("Secondary Output Gain:");
     
     this.secondarySlider = new JSlider();
     this.secondarySlider.addChangeListener(new ChangeListener() {
       public void stateChanged(ChangeEvent arg0) {
         if (!AudioLevelsFrame.this.secondarySlider.getValueIsAdjusting()) {
           float gain = AudioLevelsFrame.this.secondarySlider.getValue();
           AudioManager.setSecondOutputGain(gain);
         }
       }
     });
     this.secondarySlider.setValue(0);
     this.secondarySlider.setSnapToTicks(true);
     this.secondarySlider.setPaintTicks(true);
     this.secondarySlider.setPaintLabels(true);
     this.secondarySlider.setMinorTickSpacing(1);
     this.secondarySlider.setMinimum(-66);
     this.secondarySlider.setMaximum(6);
     this.secondarySlider.setMajorTickSpacing(6);
     
     JSeparator separator_1 = new JSeparator();
     separator_1.setForeground(Color.BLACK);
     
     JLabel lblMicInjectorGain = new JLabel("Mic Injector Gain:");
     
     this.micinjectorSlider = new JSlider();
     this.micinjectorSlider.addChangeListener(new ChangeListener() {
       public void stateChanged(ChangeEvent arg0) {
         if (!AudioLevelsFrame.this.micinjectorSlider.getValueIsAdjusting()) {
           float gain = AudioLevelsFrame.this.micinjectorSlider.getValue();
           Utils.setMicInjectorGain(gain);
         }
       }
     });
     this.micinjectorSlider.setValue(0);
     this.micinjectorSlider.setSnapToTicks(true);
     this.micinjectorSlider.setPaintTicks(true);
     this.micinjectorSlider.setPaintLabels(true);
     this.micinjectorSlider.setMinorTickSpacing(1);
     this.micinjectorSlider.setMinimum(-66);
     this.micinjectorSlider.setMaximum(6);
     this.micinjectorSlider.setMajorTickSpacing(6);
     GroupLayout groupLayout = new GroupLayout(getContentPane());
     groupLayout.setHorizontalGroup(
       groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(groupLayout.createSequentialGroup()
       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(groupLayout.createSequentialGroup()
       .addContainerGap()
       .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addComponent(separator, -1, 424, 32767)
       .addComponent(this.primarySlider, -1, 424, 32767)
       .addComponent(lblPrimaryOutputGain)
       .addComponent(lblSecondaryOutputGain)
       .addComponent(this.secondarySlider, -2, 424, -2)))
       .addGroup(groupLayout.createSequentialGroup()
       .addGap(11)
       .addComponent(separator_1, -1, 423, 32767))
       .addGroup(groupLayout.createSequentialGroup()
       .addContainerGap()
       .addComponent(lblMicInjectorGain))
       .addGroup(groupLayout.createSequentialGroup()
       .addContainerGap()
       .addComponent(this.micinjectorSlider, -2, 424, -2)))
       .addContainerGap()));
     
     groupLayout.setVerticalGroup(
       groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
       .addGroup(groupLayout.createSequentialGroup()
       .addContainerGap()
       .addComponent(lblPrimaryOutputGain)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addComponent(this.primarySlider, -2, -1, -2)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addComponent(separator, -2, 2, -2)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addComponent(lblSecondaryOutputGain)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addComponent(this.secondarySlider, -2, 45, -2)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addComponent(separator_1, -2, 2, -2)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addComponent(lblMicInjectorGain)
       .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
       .addComponent(this.micinjectorSlider, -2, 45, -2)
       .addContainerGap(38, 32767)));
     
     getContentPane().setLayout(groupLayout);
     
     this.primarySlider.setValue(primaryGain);
     this.secondarySlider.setValue(secondaryGain);
     this.micinjectorSlider.setValue(micInjectorGain);
     
     pack();
     setVisible(true);
   }
   
   public void dispose()
   {
     super.dispose();
     instance = null;
   }
   
 
 
 
   public static AudioLevelsFrame getInstance()
   {
     if (instance == null) {
       instance = new AudioLevelsFrame();
     } else {
       instance.setVisible(true);
       instance.requestFocus();
     }
     return instance;
   }
 }

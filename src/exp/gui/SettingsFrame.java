 package exp.gui;
 
 import exp.soundboard.MicInjector;
 import exp.soundboard.UpdateChecker;
 import exp.soundboard.Utils;
 import java.awt.Color;
 import java.awt.Container;
 import java.awt.Desktop;
 import java.awt.Font;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.FocusAdapter;
 import java.awt.event.FocusEvent;
 import java.awt.event.ItemEvent;
 import java.awt.event.ItemListener;
 import java.awt.event.KeyEvent;
 import java.awt.event.KeyListener;
 import java.awt.event.MouseAdapter;
 import java.awt.event.MouseEvent;
 import java.awt.event.WindowEvent;
 import java.awt.event.WindowFocusListener;
 import java.io.IOException;
 import java.io.PrintStream;
 import java.net.URI;
 import java.net.URISyntaxException;
 import java.util.ArrayList;
 import java.util.HashSet;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JComboBox;
 import javax.swing.JComponent;
 import javax.swing.JFormattedTextField;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JSeparator;
 import javax.swing.JSpinner;
 import javax.swing.JTextField;
 import javax.swing.SpinnerNumberModel;
 import javax.swing.SwingUtilities;
 import javax.swing.event.ChangeEvent;
 import javax.swing.event.ChangeListener;
 import javax.swing.text.DefaultFormatter;
 import net.miginfocom.swing.MigLayout;
 import org.jnativehook.GlobalScreen;
 import org.jnativehook.keyboard.NativeKeyEvent;
 import org.jnativehook.keyboard.NativeKeyListener;
 
 
 
 
 
 
 
 
 
 public class SettingsFrame
   extends JFrame
 {
   public static SettingsFrame instance = null;
   private static final long serialVersionUID = -4758092886690912749L;
   private JTextField stopAllTextField;
   private StopKeyNativeKeyInputGetter stopKeyInputGetter;
   private ModSpeedKeyNativeKeyInputGetter slowKeyInputGetter;
   private IncKeyNativeKeyInputGetter incKeyInputGetter;
   private DecKeyNativeKeyInputGetter decKeyInputGetter;
   private PttKeysNativeKeyInputGetter pttKeysInputGetter;
   private OverlapSwitchNativeKeyInputGetter fOverlapKeyInputGetter;
   private JComboBox<String> micComboBox;
   private JComboBox<String> vacComboBox;
   private JTextField slowKeyTextField;
   private JSpinner modSpeedSpinner;
   private JTextField incModSpeedHotKeyTextField;
   private JTextField decModSpeedHotKeyTextField;
   private JTextField pttKeysTextField;
   private JCheckBox fOverlapClipsCheckbox;
   private JTextField fOverlapHotkeyTextField;
   
   private SettingsFrame() {
     getContentPane().addMouseListener(new MouseAdapter()
     {
       public void mousePressed(MouseEvent arg0) {
         SettingsFrame.this.focusLostOnItems();
       }
     });
     addWindowFocusListener(new WindowFocusListener() {
       public void windowGainedFocus(WindowEvent arg0) {}
       
       public void windowLostFocus(WindowEvent arg0) {
         SettingsFrame.this.focusLostOnItems();
       }
     });
     setResizable(false);
     
     this.stopKeyInputGetter = new StopKeyNativeKeyInputGetter(null);
     this.slowKeyInputGetter = new ModSpeedKeyNativeKeyInputGetter(null);
     this.incKeyInputGetter = new IncKeyNativeKeyInputGetter(null);
     this.decKeyInputGetter = new DecKeyNativeKeyInputGetter(null);
     this.pttKeysInputGetter = new PttKeysNativeKeyInputGetter(null);
     this.fOverlapKeyInputGetter = new OverlapSwitchNativeKeyInputGetter(null);
     
     setDefaultCloseOperation(2);
     setTitle("Settings");
     
     JLabel lblstopAllSounds = new JLabel("'Stop All Sounds' hotkey:");
     lblstopAllSounds.setFont(new Font("Tahoma", 1, 11));
     
     this.stopAllTextField = new JTextField();
     this.stopAllTextField.addFocusListener(new FocusAdapter()
     {
       public void focusLost(FocusEvent arg0) {
         GlobalScreen.getInstance().removeNativeKeyListener(SettingsFrame.this.stopKeyInputGetter);
         SettingsFrame.this.stopAllTextField.setBackground(Color.WHITE);
       }
     });
     this.stopAllTextField.setEditable(false);
     this.stopAllTextField.addMouseListener(new MouseAdapter()
     {
       public void mousePressed(MouseEvent arg0) {
         SettingsFrame.this.stopAllTextField.setBackground(Color.CYAN);
         GlobalScreen.getInstance().addNativeKeyListener(SettingsFrame.this.stopKeyInputGetter);
       }
     });
     this.stopAllTextField.setColumns(10);
     
     final JCheckBox chckbxCheckForUpdate = new JCheckBox("Check for update on launch.");
     chckbxCheckForUpdate.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         SoundboardFrame.updateCheck = !SoundboardFrame.updateCheck;
         chckbxCheckForUpdate.setSelected(SoundboardFrame.updateCheck);
       }
     });
     chckbxCheckForUpdate.setSelected(SoundboardFrame.updateCheck);
     final JButton btnCheckForUpdate = new JButton("Check for Update");
     btnCheckForUpdate.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         if (UpdateChecker.isUpdateAvailable()) {
           SwingUtilities.invokeLater(new UpdateChecker());
         } else {
           btnCheckForUpdate.setText("No Updates");
         }
         
       }
     });
     JLabel lblExpenosa = new JLabel(" © Expenosa. 2014.");
     
     JButton btnProjectWebsite = new JButton("Project Website");
     btnProjectWebsite.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         try {
           Desktop.getDesktop().browse(new URI("https://sourceforge.net/projects/expsoundboard/"));
         } catch (IOException e1) {
           e1.printStackTrace();
         } catch (URISyntaxException e1) {
           e1.printStackTrace();
         }
         
       }
     });
     JSeparator separator = new JSeparator();
     separator.setForeground(Color.BLACK);
     
     JSeparator separator_1 = new JSeparator();
     separator_1.setForeground(Color.BLACK);
     
     JLabel lblMicInjectorSettings = new JLabel("Mic Injector settings:");
     lblMicInjectorSettings.setForeground(Color.RED);
     
     JLabel lblMicrophone = new JLabel("Microphone:");
     
     this.micComboBox = new JComboBox();
     
 
     JLabel lblVirtualAudioCable = new JLabel("Virtual Audio Cable:");
     
     this.vacComboBox = new JComboBox();
     
     JLabel lblUseMicInjector = new JLabel("*Use Mic Injector when your using a virtual audio cable as your input on other software.");
     lblUseMicInjector.setFont(new Font("Tahoma", 2, 10));
     
     JLabel lblVersion = new JLabel("Version: 0.5");
     
     JLabel lblhalfSpeedPlayback = new JLabel("'Modified playback speed' combo key:");
     
     this.slowKeyTextField = new JTextField();
     this.slowKeyTextField.addFocusListener(new FocusAdapter()
     {
       public void focusLost(FocusEvent e) {
         GlobalScreen.getInstance().removeNativeKeyListener(SettingsFrame.this.slowKeyInputGetter);
         SettingsFrame.this.slowKeyTextField.setBackground(Color.WHITE);
       }
     });
     this.slowKeyTextField.setEditable(false);
     this.slowKeyTextField.addMouseListener(new MouseAdapter()
     {
       public void mousePressed(MouseEvent arg0) {
         SettingsFrame.this.slowKeyTextField.setBackground(Color.CYAN);
         GlobalScreen.getInstance().addNativeKeyListener(SettingsFrame.this.slowKeyInputGetter);
       }
     });
     this.slowKeyTextField.setColumns(10);
     
     JLabel lblModifiedPlaybackSpeed = new JLabel("Modified playback speed multiplier:");
     
     this.modSpeedSpinner = new JSpinner();
     this.modSpeedSpinner.setModel(new SpinnerNumberModel(new Float(Utils.getModifiedPlaybackSpeed()), 
       new Float(0.1F), new Float(2.0F), new Float(0.05F)));
     JComponent comp = this.modSpeedSpinner.getEditor();
     JFormattedTextField field = (JFormattedTextField)comp.getComponent(0);
     field.setEditable(false);
     DefaultFormatter formatter = (DefaultFormatter)field.getFormatter();
     formatter.setCommitsOnValidEdit(true);
     this.modSpeedSpinner.addChangeListener(new ChangeListener()
     {
       public void stateChanged(ChangeEvent arg0) {
         float speed = ((Float)SettingsFrame.this.modSpeedSpinner.getValue()).floatValue();
         if ((speed >= 0.1F) && (speed <= 2.0F)) {
           Utils.setModifiedPlaybackSpeed(speed);
         }
         
       }
     });
     this.incModSpeedHotKeyTextField = new JTextField();
     this.incModSpeedHotKeyTextField.addFocusListener(new FocusAdapter()
     {
       public void focusLost(FocusEvent arg0) {
         GlobalScreen.getInstance().removeNativeKeyListener(SettingsFrame.this.incKeyInputGetter);
         SettingsFrame.this.incModSpeedHotKeyTextField.setBackground(Color.WHITE);
       }
     });
     this.incModSpeedHotKeyTextField.addMouseListener(new MouseAdapter()
     {
       public void mousePressed(MouseEvent arg0) {
         GlobalScreen.getInstance().addNativeKeyListener(SettingsFrame.this.incKeyInputGetter);
         SettingsFrame.this.incModSpeedHotKeyTextField.setBackground(Color.CYAN);
       }
     });
     this.incModSpeedHotKeyTextField.setEditable(false);
     this.incModSpeedHotKeyTextField.setColumns(10);
     
     this.decModSpeedHotKeyTextField = new JTextField();
     this.decModSpeedHotKeyTextField.addFocusListener(new FocusAdapter()
     {
       public void focusLost(FocusEvent e) {
         SettingsFrame.this.decModSpeedHotKeyTextField.setBackground(Color.WHITE);
         GlobalScreen.getInstance().removeNativeKeyListener(SettingsFrame.this.decKeyInputGetter);
       }
     });
     this.decModSpeedHotKeyTextField.addMouseListener(new MouseAdapter()
     {
       public void mousePressed(MouseEvent e) {
         GlobalScreen.getInstance().addNativeKeyListener(SettingsFrame.this.decKeyInputGetter);
         SettingsFrame.this.decModSpeedHotKeyTextField.setBackground(Color.CYAN);
       }
     });
     this.decModSpeedHotKeyTextField.setEditable(false);
     this.decModSpeedHotKeyTextField.setColumns(10);
     
     JLabel lblModifierSpeedIncrement = new JLabel("Modifier speed Increment hotkey:");
     
     JLabel lblNewLabel = new JLabel("Modifier speed Decrement hotkey:");
     
     JLabel lblpushToTalk = new JLabel("VoIP 'Push To Talk' Key(s): ");
     lblpushToTalk.setFont(new Font("Tahoma", 1, 11));
     
     this.pttKeysTextField = new JTextField();
     this.pttKeysTextField.addFocusListener(new FocusAdapter() {
       public void focusLost(FocusEvent arg0) {
         SettingsFrame.this.pttKeysTextField.setBackground(Color.WHITE);
         SettingsFrame.PttKeysNativeKeyInputGetter.access$1(SettingsFrame.this.pttKeysInputGetter);
         SettingsFrame.this.pttKeysTextField.removeKeyListener(SettingsFrame.this.pttKeysInputGetter);
       }
     });
     this.pttKeysTextField.addMouseListener(new MouseAdapter()
     {
       public void mousePressed(MouseEvent arg0) {
         SettingsFrame.this.pttKeysTextField.removeKeyListener(SettingsFrame.this.pttKeysInputGetter);
         SettingsFrame.this.pttKeysTextField.addKeyListener(SettingsFrame.this.pttKeysInputGetter);
         SettingsFrame.this.pttKeysTextField.setBackground(Color.CYAN);
       }
     });
     this.pttKeysTextField.setEditable(false);
     this.pttKeysTextField.setColumns(10);
     this.pttKeysTextField.setFocusTraversalKeysEnabled(false);
     
     JLabel lblOverlapSameSound = new JLabel("Overlap same sound file:");
     lblOverlapSameSound.setFont(new Font("Tahoma", 1, 11));
     
     this.fOverlapClipsCheckbox = new JCheckBox("");
     this.fOverlapClipsCheckbox.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         boolean selected = SettingsFrame.this.fOverlapClipsCheckbox.isSelected();
         Utils.setOverlapSameClipWhilePlaying(selected);
       }
     });
     setIconImage(SoundboardFrame.icon);
     
     String[] inputMixers = MicInjector.getMixerNames(MicInjector.targetDataLineInfo);
     String[] outputMixers = MicInjector.getMixerNames(MicInjector.sourceDataLineInfo);
     String[] arrayOfString1; int j = (arrayOfString1 = inputMixers).length; for (int i = 0; i < j; i++) { String input = arrayOfString1[i];
       this.micComboBox.addItem(input);
     }
     j = (arrayOfString1 = outputMixers).length; for (i = 0; i < j; i++) { String output = arrayOfString1[i];
       this.vacComboBox.addItem(output);
     }
     this.micComboBox.setSelectedItem(SoundboardFrame.micInjectorInputMixerName);
     this.vacComboBox.setSelectedItem(SoundboardFrame.micInjectorOutputMixerName);
     this.micComboBox.addItemListener(new ItemListener() {
       public void itemStateChanged(ItemEvent e) {
         if (e.getStateChange() == 1) {
           SettingsFrame.this.updateMicInjectorSettings();
         }
       }
     });
     this.vacComboBox.addItemListener(new ItemListener() {
       public void itemStateChanged(ItemEvent e) {
         if (e.getStateChange() == 1) {
           SettingsFrame.this.updateMicInjectorSettings();
         }
         
       }
     });
     this.stopAllTextField.setText(NativeKeyEvent.getKeyText(Utils.getStopKey()));
     this.slowKeyTextField.setText(NativeKeyEvent.getKeyText(Utils.getModifiedSpeedKey()));
     this.incModSpeedHotKeyTextField.setText(NativeKeyEvent.getKeyText(Utils.getModspeedupKey()));
     this.decModSpeedHotKeyTextField.setText(NativeKeyEvent.getKeyText(Utils.getModspeeddownKey()));
     this.pttKeysInputGetter.updateTextField();
     this.fOverlapClipsCheckbox.setSelected(Utils.isOverlapSameClipWhilePlaying());
     getContentPane().setLayout(new MigLayout("", "[101px][20px][45px][13px][71px][4px][34px,grow][10px][135px]", "[20px][20px][20px][20px][20px][20px][21px][][2px][14px][20px][20px][13px][2px][14px][23px]"));
     getContentPane().add(lblstopAllSounds, "cell 0 0 3 1,alignx left,aligny center");
     getContentPane().add(lblhalfSpeedPlayback, "cell 0 1 5 1,growx,aligny center");
     getContentPane().add(lblModifiedPlaybackSpeed, "cell 0 2 3 1,alignx left,aligny center");
     getContentPane().add(this.stopAllTextField, "cell 6 0 3 1,growx,aligny top");
     getContentPane().add(this.slowKeyTextField, "cell 6 1 3 1,growx,aligny top");
     getContentPane().add(this.modSpeedSpinner, "cell 6 2 3 1,growx,aligny top");
     
     JLabel lblOverlapSwitchHotkey = new JLabel("Overlap switch hotkey:");
     getContentPane().add(lblOverlapSwitchHotkey, "cell 0 7 3 1");
     
     this.fOverlapHotkeyTextField = new JTextField();
     this.fOverlapHotkeyTextField.setEditable(false);
     getContentPane().add(this.fOverlapHotkeyTextField, "cell 6 7 3 1,growx");
     this.fOverlapHotkeyTextField.setColumns(10);
     this.fOverlapHotkeyTextField.addMouseListener(new MouseAdapter()
     {
       public void mousePressed(MouseEvent e) {
         GlobalScreen.getInstance().addNativeKeyListener(SettingsFrame.this.fOverlapKeyInputGetter);
         SettingsFrame.this.fOverlapHotkeyTextField.setBackground(Color.CYAN);
       }
     });
     this.fOverlapHotkeyTextField.addFocusListener(new FocusAdapter()
     {
       public void focusLost(FocusEvent e) {
         SettingsFrame.this.fOverlapHotkeyTextField.setBackground(Color.WHITE);
         GlobalScreen.getInstance().removeNativeKeyListener(SettingsFrame.this.fOverlapKeyInputGetter);
       }
     });
     this.fOverlapHotkeyTextField.setText(NativeKeyEvent.getKeyText(Utils.getOverlapSwitchKey()));
     
     getContentPane().add(separator, "cell 0 13 9 1,growx,aligny top");
     getContentPane().add(chckbxCheckForUpdate, "cell 0 15 3 1,alignx left,aligny top");
     getContentPane().add(btnProjectWebsite, "cell 4 15 3 1,alignx right,aligny top");
     getContentPane().add(btnCheckForUpdate, "cell 8 15,alignx right,aligny top");
     getContentPane().add(lblVersion, "cell 0 14,alignx left,aligny top");
     getContentPane().add(lblExpenosa, "cell 8 14,alignx right,aligny top");
     getContentPane().add(lblMicInjectorSettings, "cell 0 9,alignx left,aligny top");
     getContentPane().add(lblMicrophone, "cell 0 10,alignx left,aligny center");
     getContentPane().add(lblVirtualAudioCable, "cell 0 11,alignx left,aligny center");
     getContentPane().add(this.vacComboBox, "cell 2 11 7 1,growx,aligny top");
     getContentPane().add(this.micComboBox, "cell 2 10 7 1,growx,aligny top");
     getContentPane().add(lblUseMicInjector, "cell 0 12 9 1,alignx left,aligny top");
     getContentPane().add(separator_1, "cell 0 8 9 1,growx,aligny top");
     getContentPane().add(lblNewLabel, "cell 0 4 5 1,growx,aligny center");
     getContentPane().add(lblModifierSpeedIncrement, "cell 0 3 5 1,growx,aligny center");
     getContentPane().add(lblpushToTalk, "cell 0 5 3 1,alignx left,aligny center");
     getContentPane().add(lblOverlapSameSound, "cell 0 6 3 1,alignx left,growy");
     getContentPane().add(this.fOverlapClipsCheckbox, "cell 6 6,alignx left,aligny top");
     getContentPane().add(this.pttKeysTextField, "cell 6 5 3 1,growx,aligny top");
     getContentPane().add(this.decModSpeedHotKeyTextField, "cell 6 4 3 1,growx,aligny top");
     getContentPane().add(this.incModSpeedHotKeyTextField, "cell 6 3 3 1,growx,aligny top");
     pack();
     setVisible(true);
   }
   
 
 
   private void updateMicInjectorSettings()
   {
     SoundboardFrame.micInjectorInputMixerName = (String)this.micComboBox.getSelectedItem();
     SoundboardFrame.micInjectorOutputMixerName = (String)this.vacComboBox.getSelectedItem();
     if (SoundboardFrame.useMicInjector) {
       Utils.startMicInjector(SoundboardFrame.micInjectorInputMixerName, SoundboardFrame.micInjectorOutputMixerName);
     }
   }
   
 
 
   public void updateDisplayedModSpeed()
   {
     this.modSpeedSpinner.setValue(Float.valueOf(Utils.getModifiedPlaybackSpeed()));
   }
   
 
 
   public void updateOverlapSwitchCheckBox()
   {
     this.fOverlapClipsCheckbox.setSelected(Utils.isOverlapSameClipWhilePlaying());
   }
   
 
 
 
 
   public void dispose()
   {
     super.dispose();
     GlobalScreen gs = GlobalScreen.getInstance();
     gs.removeNativeKeyListener(this.slowKeyInputGetter);
     gs.removeNativeKeyListener(this.stopKeyInputGetter);
     gs.removeNativeKeyListener(this.incKeyInputGetter);
     gs.removeNativeKeyListener(this.decKeyInputGetter);
     this.pttKeysTextField.removeKeyListener(this.pttKeysInputGetter);
     instance = null;
   }
   
   private class OverlapSwitchNativeKeyInputGetter implements NativeKeyListener { private OverlapSwitchNativeKeyInputGetter() {}
     
     int key = Utils.stopKey;
     
     private void updateTextField() {
       String keyname = NativeKeyEvent.getKeyText(this.key);
       SettingsFrame.this.fOverlapHotkeyTextField.setText(keyname);
     }
     
     public void nativeKeyPressed(NativeKeyEvent e)
     {
       this.key = e.getKeyCode();
       Utils.setOverlapSwitchKey(this.key);
       updateTextField();
     }
     
 
 
     public void nativeKeyReleased(NativeKeyEvent arg0) {}
     
 
 
     public void nativeKeyTyped(NativeKeyEvent arg0) {}
   }
   
 
 
   private class PttKeysNativeKeyInputGetter
     implements KeyListener
   {
     HashSet<Integer> pressedkeys = new HashSet();
     
     private PttKeysNativeKeyInputGetter() {}
     
     public void keyPressed(KeyEvent e) { int key = e.getKeyCode();
       this.pressedkeys.add(Integer.valueOf(key));
       Utils.setPTTkeys(this.pressedkeys);
       updateTextField();
       System.out.println("PPT listener key pressed: " + KeyEvent.getKeyText(key));
     }
     
     public void keyReleased(KeyEvent e)
     {
       Integer key = Integer.valueOf(e.getKeyCode());
       this.pressedkeys.remove(key);
       System.out.println("PPT listener key released: " + KeyEvent.getKeyText(key.intValue()));
     }
     
 
     public void keyTyped(KeyEvent arg0) {}
     
 
     private synchronized void updateTextField()
     {
       StringBuilder keyString = new StringBuilder();
       ArrayList<Integer> keys = Utils.getPTTkeys();
       for (int i = 0; i < keys.size(); i++) {
         if (i == 0) {
           keyString.append(KeyEvent.getKeyText(((Integer)keys.get(i)).intValue()));
         } else {
           keyString.append(" + " + KeyEvent.getKeyText(((Integer)keys.get(i)).intValue()));
         }
       }
       SettingsFrame.this.pttKeysTextField.setText(keyString.toString());
       System.out.println("PTT listener text field updated");
     }
     
     private synchronized void clearPressedKeys() {
       this.pressedkeys.clear();
       System.out.println("PTT listener keys cleared");
     }
   }
   
   private class ModSpeedKeyNativeKeyInputGetter implements NativeKeyListener
   {
     int key = Utils.slowKey;
     
     private ModSpeedKeyNativeKeyInputGetter() {}
     
     public void nativeKeyPressed(NativeKeyEvent e) { this.key = e.getKeyCode();
       Utils.setModifiedSpeedKey(this.key);
       updateTextField();
     }
     
 
 
     public void nativeKeyReleased(NativeKeyEvent e) {}
     
 
 
     public void nativeKeyTyped(NativeKeyEvent arg0) {}
     
 
     private synchronized void updateTextField()
     {
       String keyname = NativeKeyEvent.getKeyText(this.key);
       SettingsFrame.this.slowKeyTextField.setText(keyname);
     }
   }
   
   private class StopKeyNativeKeyInputGetter
     implements NativeKeyListener
   {
     int key = Utils.stopKey;
     
     private StopKeyNativeKeyInputGetter() {}
     
     public void nativeKeyPressed(NativeKeyEvent e) { this.key = e.getKeyCode();
       Utils.setStopKey(this.key);
       updateTextField();
     }
     
 
 
     public void nativeKeyReleased(NativeKeyEvent e) {}
     
 
 
     public void nativeKeyTyped(NativeKeyEvent arg0) {}
     
 
     private synchronized void updateTextField()
     {
       String keyname = NativeKeyEvent.getKeyText(this.key);
       SettingsFrame.this.stopAllTextField.setText(keyname);
     }
   }
   
   private class IncKeyNativeKeyInputGetter
     implements NativeKeyListener
   {
     int key = Utils.getModspeedupKey();
     
     private IncKeyNativeKeyInputGetter() {}
     
     public void nativeKeyPressed(NativeKeyEvent e) { this.key = e.getKeyCode();
       Utils.setModspeedupKey(this.key);
       updateTextField();
     }
     
 
 
     public void nativeKeyReleased(NativeKeyEvent e) {}
     
 
 
     public void nativeKeyTyped(NativeKeyEvent arg0) {}
     
 
     private synchronized void updateTextField()
     {
       String keyname = NativeKeyEvent.getKeyText(this.key);
       SettingsFrame.this.incModSpeedHotKeyTextField.setText(keyname);
     }
   }
   
   private class DecKeyNativeKeyInputGetter
     implements NativeKeyListener
   {
     int key = Utils.getModspeeddownKey();
     
     private DecKeyNativeKeyInputGetter() {}
     
     public void nativeKeyPressed(NativeKeyEvent e) { this.key = e.getKeyCode();
       Utils.setModspeeddownKey(this.key);
       updateTextField();
     }
     
 
 
     public void nativeKeyReleased(NativeKeyEvent e) {}
     
 
 
     public void nativeKeyTyped(NativeKeyEvent arg0) {}
     
 
     private synchronized void updateTextField()
     {
       String keyname = NativeKeyEvent.getKeyText(this.key);
       SettingsFrame.this.decModSpeedHotKeyTextField.setText(keyname);
     }
   }
   
 
 
 
 
 
   public static SettingsFrame getInstanceOf()
   {
     if (instance == null) {
       instance = new SettingsFrame();
     } else {
       instance.setVisible(true);
       instance.requestFocus();
     }
     return instance;
   }
   
 
 
   private void focusLostOnItems()
   {
     this.stopAllTextField.setBackground(Color.WHITE);
     this.slowKeyTextField.setBackground(Color.WHITE);
     this.decModSpeedHotKeyTextField.setBackground(Color.WHITE);
     this.incModSpeedHotKeyTextField.setBackground(Color.WHITE);
     this.fOverlapHotkeyTextField.setBackground(Color.WHITE);
     this.pttKeysTextField.setBackground(Color.WHITE);
     this.pttKeysInputGetter.clearPressedKeys();
     GlobalScreen gs = GlobalScreen.getInstance();
     gs.removeNativeKeyListener(this.stopKeyInputGetter);
     gs.removeNativeKeyListener(this.slowKeyInputGetter);
     gs.removeNativeKeyListener(this.incKeyInputGetter);
     gs.removeNativeKeyListener(this.decKeyInputGetter);
     gs.removeNativeKeyListener(this.fOverlapKeyInputGetter);
     this.pttKeysTextField.removeKeyListener(this.pttKeysInputGetter);
   }
 }

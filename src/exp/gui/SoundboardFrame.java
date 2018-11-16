 package exp.gui;
 
 import com.apple.eawt.Application;
 import com.google.gson.Gson;
 import exp.converter.ConverterFrame;
 import exp.soundboard.AudioManager;
 import exp.soundboard.GlobalKeyMacroListener;
 import exp.soundboard.Soundboard;
 import exp.soundboard.SoundboardEntry;
 import exp.soundboard.UpdateChecker;
 import exp.soundboard.Utils;
 import java.awt.Color;
 import java.awt.Container;
 import java.awt.Desktop;
 import java.awt.Dimension;
 import java.awt.Image;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.ItemEvent;
 import java.awt.event.ItemListener;
 import java.awt.event.WindowAdapter;
 import java.awt.event.WindowEvent;
 import java.io.File;
 import java.io.IOException;
 import java.io.PrintStream;
 import java.net.URI;
 import java.net.URISyntaxException;
 import java.util.ArrayList;
 import java.util.logging.Level;
 import java.util.logging.Logger;
 import java.util.prefs.Preferences;
 import javax.swing.ImageIcon;
 import javax.swing.JButton;
 import javax.swing.JCheckBox;
 import javax.swing.JComboBox;
 import javax.swing.JFileChooser;
 import javax.swing.JFrame;
 import javax.swing.JLabel;
 import javax.swing.JMenu;
 import javax.swing.JMenuBar;
 import javax.swing.JMenuItem;
 import javax.swing.JOptionPane;
 import javax.swing.JScrollPane;
 import javax.swing.JSeparator;
 import javax.swing.JTable;
 import javax.swing.KeyStroke;
 import javax.swing.UIManager;
 import javax.swing.UIManager.LookAndFeelInfo;
 import javax.swing.UnsupportedLookAndFeelException;
 import javax.swing.filechooser.FileFilter;
 import javax.swing.table.DefaultTableModel;
 import javax.swing.table.TableColumn;
 import javax.swing.table.TableColumnModel;
 import net.miginfocom.swing.MigLayout;
 import org.jnativehook.GlobalScreen;
 
 
 
 
 
 
 public class SoundboardFrame
   extends JFrame
 {
   private static final long serialVersionUID = 8934802095461138592L;
   final SoundboardFrame thisFrameInstance;
   public static final float VERSION = 0.5F;
   private final String title = "EXP Soundboard vers. 0.5 | ";
   private JComboBox<String> secondarySpeakerComboBox;
   private JComboBox<String> primarySpeakerComboBox;
   public AudioManager audioManager;
   public static Soundboard soundboard;
   public File testFile;
   private JTable table;
   public static GlobalKeyMacroListener macroListener;
   static boolean updateCheck;
   public static String micInjectorInputMixerName = "";
   public static String micInjectorOutputMixerName = "";
   public static boolean useMicInjector = false;
   
   public static final Image icon = new ImageIcon(SoundboardFrame.class.getResource("EXP logo.png")).getImage();
   
   public static JFileChooser filechooser;
   private final String useSecondaryKey = "useSecondSpeaker";
   private final String firstSpeakerKey = "firstSpeaker";
   private final String secondSpeakerKey = "secondSpeaker";
   private final String lastSoundboardFileKey = "lastSoundboardUsed";
   private final String stopallKeyKey = "stopAllKey";
   private final String modPlaybackSpeedKey = "modplaybackspeed";
   private final String modPlaybackSpeedKeyKey = "slowSoundKey";
   private final String modSpeedIncKeyKey = "modSpeedIncKey";
   private final String modSpeedDecKeyKey = "modSpeedDecKey";
   private final String updateCheckKey = "updateCheckOnLaunch";
   private final String micInjectorInputKey = "micInjectorInput";
   private final String micInjectorOutputKey = "micInjectorOutput";
   private final String micInjectorEnabledKey = "micInjectorEnabled";
   private final String primaryOutputGainKey = "primaryOutputGain";
   private final String secondaryOutputGainKey = "secondaryOutputGain";
   private final String micInjectorOutputGainKey = "micInjectorOutputGain";
   private final String autoPPTenabledKey = "autoPPTenabled";
   private final String autoPPTKeysKey = "autoPTTkeys";
   private final String overlapClipsKey = "OverlapClipsWhilePlaying";
   private final String OVERLAPSWITCHKEYKEY = "OverlapClipsKey";
   
   private JCheckBox useSecondaryCheckBox;
   private File currentSoundboardFile = null;
   
   private JCheckBox useMicInjectorCheckBox;
   
   private JMenuBar menuBar;
   
   private JCheckBox autoPptCheckBox;
   
   public static void main(String[] args)
   {
     Utils.initGlobalKeyLibrary();
     Utils.startMp3Decoder();
     new SoundboardFrame().setVisible(true);
   }
   
   public SoundboardFrame() {
     addWindowListener(new WindowAdapter()
     {
 
       public void windowClosing(WindowEvent e) { SoundboardFrame.this.exit(); }
     });
     try {
       UIManager.LookAndFeelInfo[] arrayOfLookAndFeelInfo;
       int j = (arrayOfLookAndFeelInfo = UIManager.getInstalledLookAndFeels()).length; for (int i = 0; i < j; i++) { UIManager.LookAndFeelInfo info = arrayOfLookAndFeelInfo[i];
         if ("Nimbus".equals(info.getName())) {
           UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
           break;
         }
       }
     } catch (ClassNotFoundException ex) {
       Logger.getLogger(SoundboardFrame.class.getName()).log(Level.SEVERE, null, ex);
     } catch (InstantiationException ex) {
       Logger.getLogger(SoundboardFrame.class.getName()).log(Level.SEVERE, null, ex);
     } catch (IllegalAccessException ex) {
       Logger.getLogger(SoundboardFrame.class.getName()).log(Level.SEVERE, null, ex);
     } catch (UnsupportedLookAndFeelException ex) {
       Logger.getLogger(SoundboardFrame.class.getName()).log(Level.SEVERE, null, ex);
     }
     filechooser = new JFileChooser();
     this.audioManager = new AudioManager();
     soundboard = new Soundboard();
     setDefaultCloseOperation(3);
     setTitle("EXP Soundboard vers. 0.5 | ");
     setIconImage(icon);
     
     macInit();
     
     this.secondarySpeakerComboBox = new JComboBox();
     this.secondarySpeakerComboBox.addItemListener(new ItemListener() {
       public void itemStateChanged(ItemEvent e) {
         if (e.getStateChange() == 1) {
           String name = (String)SoundboardFrame.this.secondarySpeakerComboBox.getSelectedItem();
           SoundboardFrame.this.audioManager.setSecondaryOutputMixer(name);
         }
         
       }
     });
     this.primarySpeakerComboBox = new JComboBox();
     this.primarySpeakerComboBox.addItemListener(new ItemListener() {
       public void itemStateChanged(ItemEvent e) {
         if (e.getStateChange() == 1) {
           String name = (String)SoundboardFrame.this.primarySpeakerComboBox.getSelectedItem();
           SoundboardFrame.this.audioManager.setPrimaryOutputMixer(name);
         }
         
       }
     });
     JButton btnStop = new JButton("Stop All");
     btnStop.addActionListener(new ActionListener()
     {
 
       public void actionPerformed(ActionEvent e) {}
 
     });
     this.useSecondaryCheckBox = new JCheckBox("Use");
     this.useSecondaryCheckBox.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         SoundboardFrame.this.audioManager.setUseSecondary(SoundboardFrame.this.useSecondaryCheckBox.isSelected());
       }
       
     });
     JScrollPane scrollPane = new JScrollPane();
     
     JButton btnAdd = new JButton("Add");
     btnAdd.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         new SoundboardEntryEditor(SoundboardFrame.this.thisFrameInstance);
       }
       
     });
     this.useMicInjectorCheckBox = new JCheckBox("Use Mic Injector (see Option -> Settings)");
     this.useMicInjectorCheckBox.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         SoundboardFrame.useMicInjector = SoundboardFrame.this.useMicInjectorCheckBox.isSelected();
         SoundboardFrame.this.updateMicInjector();
       }
       
     });
     JLabel lblstOutputeg = new JLabel("1st Output (e.g. your speakers)");
     lblstOutputeg.setForeground(Color.DARK_GRAY);
     
     JLabel lblndOutputeg = new JLabel("2nd Output (e.g. virtual audio cable \"input\")(optional)");
     lblndOutputeg.setForeground(Color.DARK_GRAY);
     
     JSeparator separator_4 = new JSeparator();
     
     this.autoPptCheckBox = new JCheckBox("Auto-hold PTT key(s)");
     this.autoPptCheckBox.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         boolean selected = SoundboardFrame.this.autoPptCheckBox.isSelected();
         Utils.setAutoPTThold(selected);
       }
       
     });
     this.table = new JTable();
     this.table.setSelectionMode(0);
     this.table.setAutoCreateRowSorter(true);
     this.table.setModel(new DefaultTableModel(
       new Object[][] {
       new Object[2] }, 
       
       new String[] {
       "Sound Clip", "HotKeys" })
       {
         private static final long serialVersionUID = 1L;
         
 
 
 
 
 
 
 
 
 
         Class[] columnTypes = {
           String.class, String.class };
         
 
 
         public Class getColumnClass(int columnIndex) { return this.columnTypes[columnIndex]; }
         
         boolean[] columnEditables = new boolean[2];
         
         public boolean isCellEditable(int row, int column)
         {
           return this.columnEditables[column];
         }
       });
     scrollPane.setViewportView(this.table);
     getContentPane().setLayout(new MigLayout("gapx  2:4:5, gapy 2:4:5, fillx", "[][][][6px][20px][6px][2px][10px][53px][6px][][24px][2px][43px]", "[grow,fill][23px][14px][20px][14px][23px][2px][23px]"));
     
     JButton btnRemove = new JButton("Remove");
     btnRemove.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         int selected = SoundboardFrame.this.table.getSelectedRow();
         if (selected > -1) {
           int index = SoundboardFrame.this.getSelectedEntryIndex();
           SoundboardFrame.soundboard.removeEntry(index);
           SoundboardFrame.this.updateSoundboardTable();
           if (index >= SoundboardFrame.this.table.getRowCount()) {
             index--;
           }
           if (index >= 0) {
             SoundboardFrame.this.table.setRowSelectionInterval(index, index);
           }
         }
       }
     });
     getContentPane().add(btnRemove, "cell 1 1,alignx left,aligny top");
     
     JButton btnEdit = new JButton("Edit");
     btnEdit.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         int selected = SoundboardFrame.this.table.getSelectedRow();
         if (selected > -1) {
           int index = SoundboardFrame.this.getSelectedEntryIndex();
           System.out.println("index " + index);
           SoundboardEntry entry = SoundboardFrame.soundboard.getEntry(index);
           new SoundboardEntryEditor(SoundboardFrame.this.thisFrameInstance, entry);
         }
       }
     });
     getContentPane().add(btnEdit, "cell 2 1,alignx left,aligny top");
     
     JButton btnPlay = new JButton("Play");
     btnPlay.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         int selected = SoundboardFrame.this.table.getSelectedRow();
         if (selected > -1) {
           int index = SoundboardFrame.this.getSelectedEntryIndex();
           SoundboardEntry entry = SoundboardFrame.soundboard.getEntry(index);
           if (SoundboardFrame.macroListener.isSpeedModKeyHeld()) {
             entry.play(SoundboardFrame.this.audioManager, true);
           } else {
             entry.play(SoundboardFrame.this.audioManager, false);
           }
         }
       }
     });
     getContentPane().add(btnPlay, "cell 10 1,alignx right,aligny top");
     getContentPane().add(this.useMicInjectorCheckBox, "cell 0 7 7 1,alignx left,aligny top");
     getContentPane().add(this.autoPptCheckBox, "cell 8 7 6 1,alignx right,aligny top");
     getContentPane().add(separator_4, "cell 0 6 14 1,growx,aligny top");
     getContentPane().add(scrollPane, "cell 0 0 14 1,grow");
     getContentPane().add(this.primarySpeakerComboBox, "cell 0 3 14 1,growx,aligny top");
     getContentPane().add(this.secondarySpeakerComboBox, "cell 0 5 12 1,growx,aligny center");
     getContentPane().add(this.useSecondaryCheckBox, "cell 13 5,alignx left,aligny top");
     getContentPane().add(btnAdd, "cell 0 1,alignx left,aligny top");
     getContentPane().add(btnStop, "cell 11 1 3 1,alignx right,aligny top");
     getContentPane().add(lblndOutputeg, "cell 0 4 7 1,alignx left,aligny top");
     getContentPane().add(lblstOutputeg, "cell 0 2 7 1,alignx left,aligny top");
     
     this.menuBar = new JMenuBar();
     setJMenuBar(this.menuBar);
     
     JMenu mnFile = new JMenu("File");
     this.menuBar.add(mnFile);
     
     JMenuItem mntmNew = new JMenuItem("New");
     mntmNew.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         SoundboardFrame.this.fileNew();
       }
     });
     mnFile.add(mntmNew);
     
     JMenuItem mntmOpen = new JMenuItem("Open");
     mntmOpen.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         SoundboardFrame.this.fileOpen();
       }
     });
     mnFile.add(mntmOpen);
     
     JSeparator separator = new JSeparator();
     mnFile.add(separator);
     
     JMenuItem mntmSave = new JMenuItem("Save");
     mntmSave.setAccelerator(KeyStroke.getKeyStroke(83, 2));
     mntmSave.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         SoundboardFrame.this.fileSave();
       }
     });
     mnFile.add(mntmSave);
     
     JMenuItem mntmSaveAs = new JMenuItem("Save As...");
     mntmSaveAs.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         SoundboardFrame.this.fileSaveAs();
       }
     });
     mnFile.add(mntmSaveAs);
     
     JSeparator separator_3 = new JSeparator();
     mnFile.add(separator_3);
     
     JMenuItem mntmProjectPage = new JMenuItem("Sourceforge Page");
     mntmProjectPage.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         try {
           Desktop.getDesktop().browse(new URI("https://sourceforge.net/projects/expsoundboard/"));
         } catch (IOException|URISyntaxException e) {
           e.printStackTrace();
         }
       }
     });
     mnFile.add(mntmProjectPage);
     
     JSeparator separator_1 = new JSeparator();
     mnFile.add(separator_1);
     
     JMenuItem mntmQuit = new JMenuItem("Quit");
     mntmQuit.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         SoundboardFrame.this.exit();
       }
     });
     mnFile.add(mntmQuit);
     
     JMenu mnEdit = new JMenu("Option");
     this.menuBar.add(mnEdit);
     
     JMenuItem mntmSettings = new JMenuItem("Settings");
     mntmSettings.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         SoundboardFrame.this.getSettingsMenu();
       }
     });
     mnEdit.add(mntmSettings);
     
     JMenuItem mntmAudioLevels = new JMenuItem("Audio Levels");
     mntmAudioLevels.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent arg0) {
         AudioLevelsFrame.getInstance().setLocationRelativeTo(SoundboardFrame.this.thisFrameInstance);
       }
     });
     mnEdit.add(mntmAudioLevels);
     
     JSeparator separator_2 = new JSeparator();
     mnEdit.add(separator_2);
     
     JMenuItem mntmAudioConverter = new JMenuItem("Audio Converter");
     mntmAudioConverter.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent e) {
         if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
           new ConverterFrame();
         } else {
           JOptionPane.showMessageDialog(null, "Audio Converter currently not supported on Mac OS X", "Feature not supported", 1);
         }
       }
     });
     mnEdit.add(mntmAudioConverter);
     
     setMinimumSize(new Dimension(400, 500));
     
     updateSpeakerComboBoxes();
     pack();
     this.thisFrameInstance = this;
     macroListener = new GlobalKeyMacroListener(this);
     GlobalScreen.getInstance().addNativeKeyListener(macroListener);
     setLocationRelativeTo(null);
     loadPrefs();
   }
   
 
 
 
   public void updateSoundboardTable()
   {
     Object[][] entryArray = soundboard.getEntriesAsObjectArrayForTable();
     
     this.table.setModel(new DefaultTableModel(
       entryArray, 
       new String[] {
       "Sound Clip", "HotKeys", "File Locations", "Index" })
       {
         private static final long serialVersionUID = 1L;
         
 
 
 
 
 
 
 
         Class[] columnTypes = {
           String.class, String.class, String.class, Integer.TYPE };
         
 
 
         public Class getColumnClass(int columnIndex) { return this.columnTypes[columnIndex]; }
         
         boolean[] columnEditables = new boolean[4];
         
         public boolean isCellEditable(int row, int column)
         {
           return this.columnEditables[column];
         }
       });
     TableColumnModel columnmod = this.table.getColumnModel();
     columnmod.getColumn(3).setMinWidth(0);
     columnmod.getColumn(3).setMaxWidth(0);
     columnmod.getColumn(3).setWidth(0);
     this.table.removeColumn(columnmod.getColumn(2));
   }
   
 
 
   private void fileNew()
   {
     Utils.stopAllClips();
     saveReminder();
     this.currentSoundboardFile = null;
     soundboard = new Soundboard();
     updateSoundboardTable();
     setTitle("EXP Soundboard vers. 0.5 | ");
   }
   
 
 
   private void fileOpen()
   {
     Utils.stopAllClips();
     saveReminder();
     filechooser.setFileFilter(new JsonFileFilter(null));
     int session = filechooser.showOpenDialog(null);
     if (session == 0) {
       File jsonfile = filechooser.getSelectedFile();
       open(jsonfile);
     }
   }
   
 
 
 
   private void fileSave()
   {
     if (this.currentSoundboardFile != null) {
       soundboard.saveAsJsonFile(this.currentSoundboardFile);
     } else {
       fileSaveAs();
     }
   }
   
 
 
   private void fileSaveAs()
   {
     JFileChooser fc = new JFileChooser();
     fc.setFileFilter(new JsonFileFilter(null));
     if (this.currentSoundboardFile != null) {
       fc.setSelectedFile(this.currentSoundboardFile);
     }
     int session = fc.showSaveDialog(null);
     if (session == 0) {
       File file = fc.getSelectedFile();
       this.currentSoundboardFile = soundboard.saveAsJsonFile(file);
       setTitle("EXP Soundboard vers. 0.5 | " + this.currentSoundboardFile.getName());
     }
   }
   
 
 
   private void getSettingsMenu()
   {
     SettingsFrame.getInstanceOf().setLocationRelativeTo(this);
   }
   
 
 
 
   private void open(File jsonfile)
   {
     if (jsonfile.exists()) {
       Soundboard sb = Soundboard.loadFromJsonFile(jsonfile);
       if (sb != null) {
         soundboard = sb;
         updateSoundboardTable();
         this.currentSoundboardFile = jsonfile;
         setTitle("EXP Soundboard vers. 0.5 | " + this.currentSoundboardFile.getName());
       }
     }
   }
   
 
 
   public void updateSpeakerComboBoxes()
   {
     String[] outputmixerStringArray = Utils.getMixerNames(this.audioManager.standardDataLineInfo);
     String[] arrayOfString1; int j = (arrayOfString1 = outputmixerStringArray).length; for (int i = 0; i < j; i++) { String speaker = arrayOfString1[i];
       this.primarySpeakerComboBox.addItem(speaker);
       this.secondarySpeakerComboBox.addItem(speaker);
     }
   }
   
 
 
 
 
 
   private int getSelectedEntryIndex()
   {
     int selected = this.table.getSelectedRow();
     return ((Integer)this.table.getValueAt(selected, 2)).intValue();
   }
   
 
 
   public void updateMicInjector()
   {
     this.useMicInjectorCheckBox.setSelected(useMicInjector);
     if (useMicInjector) {
       Utils.startMicInjector(micInjectorInputMixerName, micInjectorOutputMixerName);
     } else {
       Utils.stopMicInjector();
     }
   }
   
 
 
 
   private void savePrefs()
   {
     Preferences prefs = Utils.prefs;
     prefs.putBoolean("useSecondSpeaker", this.useSecondaryCheckBox.isSelected());
     prefs.put("firstSpeaker", (String)this.primarySpeakerComboBox.getSelectedItem());
     prefs.put("secondSpeaker", (String)this.secondarySpeakerComboBox.getSelectedItem());
     if (this.currentSoundboardFile != null) {
       prefs.put("lastSoundboardUsed", this.currentSoundboardFile.getAbsolutePath());
     }
     prefs.putBoolean("OverlapClipsWhilePlaying", Utils.isOverlapSameClipWhilePlaying());
     prefs.putInt("OverlapClipsKey", Utils.getOverlapSwitchKey());
     prefs.putInt("stopAllKey", Utils.getStopKey());
     prefs.putFloat("modplaybackspeed", Utils.getModifiedPlaybackSpeed());
     prefs.putInt("slowSoundKey", Utils.getModifiedSpeedKey());
     prefs.putInt("modSpeedIncKey", Utils.getModspeedupKey());
     prefs.putInt("modSpeedDecKey", Utils.getModspeeddownKey());
     prefs.putBoolean("updateCheckOnLaunch", updateCheck);
     prefs.put("micInjectorInput", micInjectorInputMixerName);
     prefs.put("micInjectorOutput", micInjectorOutputMixerName);
     prefs.putBoolean("micInjectorEnabled", useMicInjector);
     prefs.putBoolean("autoPPTenabled", Utils.autoPTThold);
     prefs.put("autoPTTkeys", Utils.getPTTkeys().toString());
     
     prefs.putFloat("primaryOutputGain", AudioManager.getFirstOutputGain());
     prefs.putFloat("secondaryOutputGain", AudioManager.getSecondOutputGain());
     prefs.putFloat("micInjectorOutputGain", Utils.getMicInjectorGain());
   }
   
 
 
 
 
   private void loadPrefs()
   {
     Preferences prefs = Utils.prefs;
     boolean useSecond = prefs.getBoolean("useSecondSpeaker", false);
     this.useSecondaryCheckBox.setSelected(useSecond);
     this.audioManager.setUseSecondary(useSecond);
     
     String firstspeaker = prefs.get("firstSpeaker", null);
     String secondspeaker = prefs.get("secondSpeaker", null);
     if (firstspeaker != null) {
       this.primarySpeakerComboBox.setSelectedItem(firstspeaker);
       this.audioManager.setPrimaryOutputMixer(firstspeaker);
     }
     if (secondspeaker != null) {
       this.secondarySpeakerComboBox.setSelectedItem(secondspeaker);
       this.audioManager.setSecondaryOutputMixer(secondspeaker);
     }
     
     String lastfile = prefs.get("lastSoundboardUsed", null);
     if (lastfile != null) {
       open(new File(lastfile));
     }
     
     float modSpeed = prefs.getFloat("modplaybackspeed", 0.5F);
     Utils.setModifiedPlaybackSpeed(modSpeed);
     
 
     int slowkey = prefs.getInt("slowSoundKey", 35);
     Utils.setModifiedSpeedKey(slowkey);
     int stopkey = prefs.getInt("stopAllKey", 19);
     Utils.setStopKey(stopkey);
     int incKey = prefs.getInt("modSpeedIncKey", 39);
     Utils.setModspeedupKey(incKey);
     int decKey = prefs.getInt("modSpeedDecKey", 37);
     Utils.setModspeeddownKey(decKey);
     
     updateCheck = prefs.getBoolean("updateCheckOnLaunch", true);
     if (updateCheck) {
       new Thread(new UpdateChecker()).start();
     }
     
     float firstOutputGain = prefs.getFloat("primaryOutputGain", 0.0F);
     float secondOutputGain = prefs.getFloat("secondaryOutputGain", 0.0F);
     float micinjectorOutputGain = prefs.getFloat("micInjectorOutputGain", 0.0F);
     AudioManager.setFirstOutputGain(firstOutputGain);
     AudioManager.setSecondOutputGain(secondOutputGain);
     Utils.setMicInjectorGain(micinjectorOutputGain);
     
 
     micInjectorInputMixerName = prefs.get("micInjectorInput", "");
     micInjectorOutputMixerName = prefs.get("micInjectorOutput", "");
     useMicInjector = prefs.getBoolean("micInjectorEnabled", false);
     updateMicInjector();
     
 
     boolean useautoptt = prefs.getBoolean("autoPPTenabled", false);
     this.autoPptCheckBox.setSelected(useautoptt);
     Utils.setAutoPTThold(useautoptt);
     String autopttkeys = prefs.get("autoPTTkeys", null);
     if (autopttkeys != null) {
       ArrayList<Integer> keys = Utils.stringToIntArrayList(autopttkeys);
       Utils.setPTTkeys(keys);
     }
     
 
     Utils.setOverlapSameClipWhilePlaying(prefs.getBoolean("OverlapClipsWhilePlaying", true));
     int overlapKey = prefs.getInt("OverlapClipsKey", 36);
     Utils.setOverlapSwitchKey(overlapKey);
   }
   
 
 
   private void exit()
   {
     Utils.stopAllClips();
     saveReminder();
     savePrefs();
     dispose();
     Utils.deregisterGlobalKeyLibrary();
     System.exit(0);
   }
   
 
 
   private void saveReminder()
   {
     if (this.currentSoundboardFile != null) {
       if (this.currentSoundboardFile.exists()) {
         Gson gson = new Gson();
         Soundboard savedFile = Soundboard.loadFromJsonFile(this.currentSoundboardFile);
         String savedjson = gson.toJson(savedFile);
         String currentjson = gson.toJson(soundboard);
         if (!savedjson.equals(currentjson)) {
           int option = JOptionPane.showConfirmDialog(null, "Soundboard has changed. Do you want to save?", "Save Reminder", 0);
           if (option == 0) {
             soundboard.saveAsJsonFile(this.currentSoundboardFile);
           }
         }
       }
     } else if (soundboard.getSoundboardEntries().size() > 0) {
       int option = JOptionPane.showConfirmDialog(null, "Soundboard has not been saved. Do you want to save?", "Save Reminder", 0);
       if (option == 0) {
         fileSave();
       }
     }
   }
   
 
 
   private void macInit()
   {
     if (System.getProperty("os.name").toLowerCase().contains("mac")) {
       Application application = Application.getApplication();
       application.setDockIconImage(icon);
       System.setProperty("apple.laf.useScreenMenuBar", "true");
       System.setProperty("com.apple.mrj.application.apple.menu.about.name", "EXP Soundboard");
     }
   }
   
 
   private class JsonFileFilter
     extends FileFilter
   {
     private JsonFileFilter() {}
     
 
     public boolean accept(File f)
     {
       if (f.isDirectory()) {
         return true;
       }
       if (f.getName().toLowerCase().endsWith(".json")) {
         return true;
       }
       return false;
     }
     
     public String getDescription()
     {
       return ".json Soundboard save file";
     }
   }
 }

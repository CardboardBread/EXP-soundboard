package ca.exp.soundboard.remake.gui;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.*;

import javax.sound.sampled.*;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import ca.exp.soundboard.remake.model.Entry;

public class MenuController extends GuiController implements ListChangeListener<Entry> {

	// --- Indices groups --- //

	private static final int primaryIndex = 0;
	private static final int secondaryIndex = 1;

	private static final int[] singleIndices = {primaryIndex};
	private static final int[] doubleIndices = {primaryIndex, secondaryIndex};

	// --- GUI Layer Data --- //

	private ObservableList<Entry> tableList;
	private ObservableList<Mixer.Info> audioList;

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	// --- 1st Menu Items --- //

	@FXML // fx:id="newMenuButton"
	private MenuItem newMenuButton; // Value injected by FXMLLoader

	@FXML // fx:id="openMenuButton"
	private MenuItem openMenuButton; // Value injected by FXMLLoader

	@FXML // fx:id="closeMenuButton"
	private MenuItem closeMenuButton; // Value injected by FXMLLoader

	@FXML // fx:id="saveMenuButton"
	private MenuItem saveMenuButton; // Value injected by FXMLLoader

	@FXML // fx:id="saveAsMenuButton"
	private MenuItem saveAsMenuButton; // Value injected by FXMLLoader

	@FXML // fx:id="webpageMenuButton"
	private MenuItem webpageMenuButton; // Value injected by FXMLLoader

	@FXML // fx:id="quitMenuButton"
	private MenuItem quitMenuButton; // Value injected by FXMLLoader

	// --- 2nd Menu Items --- //

	@FXML // fx:id="settingsMenuButton"
	private MenuItem settingsMenuButton; // Value injected by FXMLLoader

	@FXML // fx:id="levelsMenuButton"
	private MenuItem levelsMenuButton; // Value injected by FXMLLoader

	@FXML // fx:id="converterMenuButton"
	private MenuItem converterMenuButton; // Value injected by FXMLLoader

	// --- Entry/Audio Control Buttons --- //

	@FXML // fx:id="addButton"
	private Button addButton; // Value injected by FXMLLoader

	@FXML // fx:id="removeButton"
	private Button removeButton; // Value injected by FXMLLoader

	@FXML // fx:id="editButton"
	private Button editButton; // Value injected by FXMLLoader

	@FXML // fx:id="playButton"
	private Button playButton; // Value injected by FXMLLoader

	@FXML // fx:id="stopButton"
	private Button stopButton; // Value injected by FXMLLoader

	// --- Other items --- //

	@FXML // fx:id="secondarySpeakerCheck"
	private CheckBox secondarySpeakerCheck; // Value injected by FXMLLoader

	@FXML // fx:id="secondarySpeakerCombo"
	private ComboBox<Mixer.Info> secondarySpeakerCombo; // Value injected by FXMLLoader

	@FXML // fx:id="primarySpeakerCombo"
	private ComboBox<Mixer.Info> primarySpeakerCombo; // Value injected by FXMLLoader

	@FXML // fx:id="injectorCheck"
	private CheckBox injectorCheck; // Value injected by FXMLLoader

	@FXML // fx:id="pttHoldCheck"
	private CheckBox pttHoldCheck; // Value injected by FXMLLoader

	@FXML // fx:id="entryTable"
	private TableView<Entry> entryTable; // Value injected by FXMLLoader

	@FXML // fx:id="clipColumn"
	private TableColumn<Entry, String> clipColumn; // Value injected by FXMLLoader

	@FXML // fx:id="hotkeyColumn"
	private TableColumn<Entry, String> hotkeyColumn; // Value injected by FXMLLoader

	// --- GUI Methods --- //

	@FXML // This method is called by the FXMLLoader when initialization is complete
	void initialize() {
		assert newMenuButton != null : "fx:id=\"newMenuButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert openMenuButton != null : "fx:id=\"openMenuButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert closeMenuButton != null : "fx:id=\"closeMenuButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert saveMenuButton != null : "fx:id=\"saveMenuButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert saveAsMenuButton != null : "fx:id=\"saveAsMenuButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert webpageMenuButton != null : "fx:id=\"webpageMenuButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert quitMenuButton != null : "fx:id=\"quitMenuButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert settingsMenuButton != null : "fx:id=\"settingsMenuButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert levelsMenuButton != null : "fx:id=\"levelsMenuButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert converterMenuButton != null : "fx:id=\"converterMenuButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert addButton != null : "fx:id=\"addButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert removeButton != null : "fx:id=\"removeButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert editButton != null : "fx:id=\"editButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert playButton != null : "fx:id=\"playButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert stopButton != null : "fx:id=\"stopButton\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert secondarySpeakerCheck != null : "fx:id=\"secondarySpeakerCheck\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert secondarySpeakerCombo != null : "fx:id=\"secondarySpeakerCombo\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert primarySpeakerCombo != null : "fx:id=\"primarySpeakerCombo\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert injectorCheck != null : "fx:id=\"injectorCheck\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert pttHoldCheck != null : "fx:id=\"pttHoldCheck\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
		assert entryTable != null : "fx:id=\"EntryTable\" was not injected: check your FXML file 'mainmenu_jfx.fxml'.";
	}

	@FXML
	void onAddPressed(ActionEvent event) {
		parent.entryController().start();
	}

	@FXML
	void onEditPressed(ActionEvent event) {
		Entry selected = getSelectedEntry();
		if (selected != null) {
			parent.entryController().start(getSelectedEntry());
		} else {
			logger.warning("Cannot edit no selection!");
		}
	}

	@FXML
	void onPlayPressed(ActionEvent event) {
		Entry selected = getSelectedEntry();
		if (selected != null) {
			logger.info( "Playing entry: \'" + selected.toString() + "\'");
			try {
				parent.getModel().getAudio().play(selected.getFile(), (secondaryChecked() ? doubleIndices : singleIndices));
			} catch (LineUnavailableException | UnsupportedAudioFileException | IOException | IllegalArgumentException e) {
				logger.log(Level.WARNING, "Failed to play target file!", e);
			}
		} else {
			logger.warning( "Cannot play no selection!");
		}
	}

	@FXML
	void onRemovePressed(ActionEvent event) {
		Entry selected = getSelectedEntry();
		if (selected != null) {
			logger.info( "Removing selected entry");
			parent.getModel().getEntries().remove(selected);
		} else {
			logger.warning( "Cannot remove no selection!");
		}
	}

	@FXML
	void onStopPressed(ActionEvent event) {
		parent.getModel().getAudio().stopAll();
	}

	@FXML
	void onNewMenuPressed(ActionEvent event) {
		// TODO check for unsaved, reset ca.exp.soundboard.model,
	}

	@FXML
	void onOpenMenuPressed(ActionEvent event) {
		// TODO open file picker, send to file parser
	}

	@FXML
	void onCloseMenuPressed(ActionEvent event) {
		// TODO poll if working file has been saved, if so ask user before, then close everything
	}

	@FXML
	void onSaveMenuPressed(ActionEvent event) {
		// TODO send saved destination to file parser
	}

	@FXML
	void onSaveAsMenuPressed(ActionEvent event) {
		// TODO open file picker, send destination to file parser
	}

	@FXML
	void onWebpageMenuPressed(ActionEvent event) {
		// TODO ask system to open browser on github page link
	}

	@FXML
	void onQuitMenuPressed(ActionEvent event) {
		// TODO check if setup changed, ask for user confirmation if changed, otherwise close
	}

	@FXML
	void onSettingsMenuPressed(ActionEvent event) {
		parent.settingsController().start();
	}

	@FXML
	void onLevelsMenuPressed(ActionEvent event) {
		parent.levelsController().start();
	}

	@FXML
	void onConverterMenuPressed(ActionEvent event) {
		parent.converterController().start();
	}

	// --- Interaction Methods --- //

	public Entry getSelectedEntry() {
		return entryTable.getSelectionModel().getSelectedItem();
	}

	public Mixer.Info getPrimarySelect() {
		return primarySpeakerCombo.getValue();
	}

	public Mixer.Info getSecondarySelect() {
		return secondarySpeakerCombo.getValue();
	}

	public boolean secondaryChecked() {
		return secondarySpeakerCheck.isSelected();
	}

	public void closeMenu() {
		logger.info( "Main window closed, stopping");
		try {
			parent.stop();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Failed to close the application, exiting by System.exit()", e);
			System.exit(1); // TODO: this may cause file system errors if anything is left open
		}
	}

	public void setPrimarySpeaker(Mixer.Info request) {
		logger.info( "Selected " + request.getName() + " as primary speaker");
		parent.getModel().getAudio().setOutput(primaryIndex, request);
	}

	public void setSecondarySpeaker(Mixer.Info request) {
		logger.info( "Selected " + request.getName() + " as secondary speaker");
		parent.getModel().getAudio().setOutput(secondaryIndex, request);
	}

	// --- General Methods --- //

	// called when the internal list in SoundboardModel changes
	public void onChanged(Change<? extends Entry> change) {
		// Copied from Oracle's Javadoc on javafx.collections.ListChangeListener.Change
		// https://docs.oracle.com/javase/8/javafx/api/javafx/collections/ListChangeListener.Change.html
		while (change.next()) {
			if (change.wasPermutated()) {
				logger.info( "Reflecting entry list permutation in GUI");
				for (int i = change.getFrom(); i < change.getTo(); ++i) {
					// permutate // TODO use this
				}
			} else if (change.wasUpdated()) {
				logger.info( "Reflecting entry list update in GUI");
				// update item // TODO use this
			} else {
				for (Entry remitem : change.getRemoved()) {
					logger.info( "Reflecting entry list reduction in GUI");
					tableList.remove(remitem);
				}
				for (Entry additem: change.getAddedSubList()) {
					logger.info( "Reflecting entry list expansion in GUI");
					tableList.add(additem);
				}
			}
		}
	}

	@Override
	void preload(SoundboardStage parent, Stage stage, Scene scene) {
		super.preload(parent, stage, scene);
		logger.info( "Initializing main menu controller");

		// Set up each column in the table to pull the appropriate data from an Entry within
		// the table's internal list.
		clipColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Entry, String> p) {
				return new ReadOnlyStringWrapper(p.getValue().getFile().getName());
			}
		});
		hotkeyColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Entry, String>, ObservableValue<String>>() {
			public ObservableValue<String> call(TableColumn.CellDataFeatures<Entry, String> p) {
				return new ReadOnlyStringWrapper(p.getValue().getCombo().toString());
			}
		});

		// Set up the table's internal list.
		tableList = FXCollections.observableArrayList();
		entryTable.setItems(tableList);

		// Set up the internal list both combo boxes pull from
		audioList = FXCollections.observableArrayList();

		// Setup a factory to properly pull data from Mixer.Info to display in a combobox
		Callback<ListView<Mixer.Info>, ListCell<Mixer.Info>> cellFactory = new Callback<ListView<Mixer.Info>, ListCell<Mixer.Info>>() {
			public ListCell<Mixer.Info> call(ListView<Mixer.Info> param) {
				return new ListCell<Mixer.Info>() {
					@Override
					protected void updateItem(Mixer.Info item, boolean empty) {
						super.updateItem(item, empty);
						if (item == null || empty) {
							setGraphic(null);
						} else {
							setText(item.getName());
						}
					}

				};
			}
		};

		// Set up the combo boxes for selecting an audio device to play to.
		// Both combo boxes use the same list of items, as all the manipulation they do is
		// selecting one of the elements of the list.
		primarySpeakerCombo.setItems(audioList);
		primarySpeakerCombo.setButtonCell(cellFactory.call(null));
		primarySpeakerCombo.setCellFactory(cellFactory);
		primarySpeakerCombo.valueProperty().addListener(new ChangeListener<Mixer.Info>() {
			public void changed(ObservableValue<? extends Mixer.Info> observable, Mixer.Info oldValue, Mixer.Info newValue) {
				setPrimarySpeaker(newValue);
			}
		});

		// Refer to comments above
		secondarySpeakerCombo.setItems(audioList);
		secondarySpeakerCombo.setButtonCell(cellFactory.call(null));
		secondarySpeakerCombo.setCellFactory(cellFactory);
		secondarySpeakerCombo.valueProperty().addListener(new ChangeListener<Mixer.Info>() {
			public void changed(ObservableValue<? extends Mixer.Info> observable, Mixer.Info oldValue, Mixer.Info newValue) {
				setSecondarySpeaker(newValue);
			}
		});

		// overwrites default behaviour from GuiController, closes the entire program when this window is closed
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent event) {
				closeMenu();
			}
		});
	}

	public void reset() {
		logger.info( "Resetting GUI elements");

		// The zero-th element is preselected to prevent the user from starting with a null audio device.
		init(AudioSystem.getMixerInfo(), parent.getModel().getEntries(), 0, 0, false, false, false);
    }

	private void init(Mixer.Info[] devices, Collection<? extends Entry> entries, int primaryIndex, int secondaryIndex, boolean secondaryCheck, boolean injector, boolean pttHold) {
		audioList.addAll(devices);
		tableList.addAll(entries);

		primarySpeakerCombo.getSelectionModel().select(primaryIndex);
		secondarySpeakerCombo.getSelectionModel().select(secondaryIndex);

		secondarySpeakerCheck.setSelected(secondaryCheck);
		injectorCheck.setSelected(injector);
		pttHoldCheck.setSelected(pttHold);
	}

	@Override
	public void start() {
		reset();
		stage.show();
		active = true;
	}

	@Override
	public void stop() {
		stage.close();
		active = false;
	}
}

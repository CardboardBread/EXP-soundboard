package exp.soundboard;

import exp.gui.UpdateConfirmFrame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

public class UpdateChecker implements Runnable {
	private static final String updatelink = "http://sourceforge.net/projects/expsoundboard/files/";

	public static String getUpdateNotes() {
		boolean internetconnection = false;
		BufferedReader reader = null;
		try {
			URL url = new URL("http://sourceforge.net/projects/expsoundboard/files/");
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			internetconnection = true;
		} catch (MalformedURLException ex) {
			Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
		}

		if (internetconnection) {
			System.out.println("UpdateChecker: System has Internet Connection.");
			boolean versionfound = false;

			String patchlist = "";
			boolean changelogFound = false;
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					String line;
					if (!changelogFound) {
						if (line.startsWith("CHANGELOG")) {
							changelogFound = true;
						}
					} else if (changelogFound) {
						if ((line.startsWith("vers.")) && (!versionfound)) {
							versionfound = true;
							patchlist = patchlist + '\n' + line;
						} else {
							if ((versionfound) && (line.startsWith("vers."))) {
								reader.close();
								return patchlist;
							}
							patchlist = patchlist + '\n' + line;
						}
					}
				}
				reader.close();
			} catch (IOException ex) {
				Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		System.out.println("UpdateChecker: System does not have Internet Connection.");

		return "Update notes could not be found";
	}

	public static boolean isUpdateAvailable() {
		boolean internetconnection = false;
		BufferedReader reader = null;
		try {
			URL url = new URL("http://sourceforge.net/projects/expsoundboard/files/");
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			internetconnection = true;
		} catch (MalformedURLException ex) {
			Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
		}

		if (internetconnection) {
			System.out.println("UpdateChecker: System has Internet Connection.");

			boolean changelogFound = false;
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					String line;
					if (!changelogFound) {
						if (line.startsWith("CHANGELOG")) {
							changelogFound = true;
						}
					} else if ((changelogFound) && (line.startsWith("vers."))) {
						String version = line.substring(line.indexOf('.') + 1, line.lastIndexOf(':')).trim();
						float versionNo = Float.parseFloat(version);
						if (versionNo > 0.5F) {
							System.out.println("UpdateChecker: New version available!");
							reader.close();
							return true;
						}
						System.out.println("UpdateChecker: Currently up to date!");
						reader.close();
						return false;
					}
				}

				reader.close();
			} catch (IOException ex) {
				Logger.getLogger(UpdateChecker.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		System.out.println("UpdateChecker: System does not have Internet Connection.");

		return false;
	}

	public void run() {
		if (isUpdateAvailable()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					new UpdateConfirmFrame(UpdateChecker.getUpdateNotes());
				}
			});
		}
	}
}

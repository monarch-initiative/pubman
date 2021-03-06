package org.monarchinitiative.pubman.gui;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * This is used to figure out where Gopher will store the viewpoint files. For instance, with linux
 * this would be /home/username/.gopher/...
 */
public class PubManPlatform {
    private static final String WEB_ENGINE_DIRNAME = "web_engine_user_data";

    private static final String PUBMAN_FILENAME = "hpocitations.txt";

    private static final String PUBMAN_SETTINGS = "pubman-settings.txt";


    /**
     * This method creates directory where Gopher stores global settings, if the directory does not exist.
     *
     * @throws IOException if it is not possible to create the Gopher directory
     */
    private static void createPubManDir() throws IOException {
        File target = getPubManDir();

        if (target == null)
            throw new IOException("Operating system not recognized. Supported systems: WINDOWS, OSX, LINUX");

        if (target.isFile()) { // a file exists at the place where we want to create a directory
            boolean success = target.delete();
            if (!success) {
                throw new IOException("Unable to create directory for storing PubMan settings at '" + target.getAbsolutePath() + "'");
            }
        }

        if (!target.isDirectory()) {
            boolean success = target.mkdirs();
            if (!success)
                throw new IOException("Unable to create directory for storing PubMan settings at '" + target.getAbsolutePath() + "'");
        }

        // create directory for WebEngine user data
        File userDataDir = new File(target, WEB_ENGINE_DIRNAME);

        if (userDataDir.isFile()) {
            target.delete();
        }
        if (!userDataDir.isDirectory()) { // either does not exist or is not a directory
            boolean success = userDataDir.mkdirs();
        }
    }


    /**
     * Get path to directory where Gopher stores global settings.
     * The path depends on underlying operating system. Linux, Windows and OSX currently supported.
     *
     * @return File to directory
     */
    static File getPubManDir() {
        CurrentPlatform platform = figureOutPlatform();

        File linuxPath = new File(System.getProperty("user.home") + File.separator + ".pubman");
        File windowsPath = new File(System.getProperty("user.home") + File.separator + "pubman");
        File osxPath = new File(System.getProperty("user.home") + File.separator + ".pubman");

        switch (platform) {
            case LINUX:
                return linuxPath;
            case WINDOWS:
                return windowsPath;
            case OSX:
                return osxPath;
            case UNKNOWN:
                return null;
            default:
                return null;
        }
    }

    /**
     * This reads the local PubMan settings file and tries to mine it for
     * the location of the citation file.
     * @return String representing the path of the PubMan citation file.
     * @throws IOException if the setting file cannot be found or if it does not contain path to citation file
     */
    static String getPubManFile() throws IOException {
        File dirpath = getPubManDir();
        String settingsFilePath =  String.format("%s%s%s",dirpath.getAbsolutePath(),File.separator,PUBMAN_SETTINGS);
        BufferedReader br = new BufferedReader(new FileReader(settingsFilePath));
        String line;
        while ((line=br.readLine())!=null) {
            if (line.startsWith("file:")) {
                return line.substring(5).trim(); }
        }
        throw new IOException("Could not find line with location of citation file");
    }


    /**
     * Get a directory where {@link javafx.scene.web.WebEngine}s used to render html data store <code>userData</code>.
     * In order to use the returned <code>userDataDirectory</code> it must actually exist in the file system. Creation
     * of the folder is taken care of in {@link #createPubManDir()} method. If it fails, this method will return <code>null</code>.
     *
     * @return {@link File} leading to <em>existing</em> userData directory or <code>null</code> if {@link #getPubManDir()} returns <code>null</code>
     */
    static File getWebEngineUserDataDirectory() {

        File gopherDir = getPubManDir();

        File webEngineUserData = null;
        if (gopherDir != null)
            webEngineUserData = new File(gopherDir, WEB_ENGINE_DIRNAME);

        if (webEngineUserData != null && webEngineUserData.isDirectory())
            return webEngineUserData;
        else
            return null;
    }


    /**
     * Get the absolute path to the viewpoint file, which is a serialized Java file (suffix {@code .ser}).
     *
     * @param basename The plain viewpoint name, e.g., human37cd4
     * @return the absolute path,e.g., /home/user/data/immunology/human37cd4.ser
     */
    public static String getAbsoluteProjectPath(String basename) {
        File dir = getPubManDir();
        return dir + File.separator + basename + ".ser";
    }


    /**
     * Get the absolute path to the log file.
     *
     * @return the absolute path,e.g., /home/user/.gopher/gopher.log
     */
    public static String getAbsoluteLogPath() {
        File dir = getPubManDir();
        return dir + File.separator + "pubman.log";
    }


    /* Based on this post: http://www.mkyong.com/java/how-to-detect-os-in-java-systemgetpropertyosname/ */
    private static CurrentPlatform figureOutPlatform() {
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return CurrentPlatform.LINUX;
        } else if (osName.contains("win")) {
            return CurrentPlatform.WINDOWS;
        } else if (osName.contains("mac")) {
            return CurrentPlatform.OSX;
        } else {
            return CurrentPlatform.UNKNOWN;
        }
    }


    private enum CurrentPlatform {
        LINUX("Linux"),
        WINDOWS("Windows"),
        OSX("Os X"),
        UNKNOWN("Unknown");

        private final String name;


        CurrentPlatform(String n) {
            this.name = n;
        }


        @Override
        public String toString() {
            return this.name;
        }
    }

}

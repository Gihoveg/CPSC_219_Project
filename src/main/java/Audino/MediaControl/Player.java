package Audino.MediaControl;

import java.io.*;

import Audino.MediaControl.Library;
import Audino.MediaControl.Playlist;
import Audino.MediaControl.AudioFX;
import Audino.State.PlayerState.PausedState;
import Audino.State.PlayerState.PlayingState;
import Audino.State.PlayerState.ReadyState;
import Audino.State.PlayerState.State;
import Audino.State.PlayerState.UnreadyState;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;

import javafx.util.Duration;

/*
 * This class takes care of playing audio and all of the basic functions
 * such as pause/play and stop.
*/
public class Player {
    /*
     * The instance variables are limited to what is useful, for example AudioStream
     * is not an instance variable because we don't need a specific AudioStream to
     * go between functions.
     */
    private Track currentTrack;
    private State state;
    private double volume;
    private Playlist playlist;
    private Library Library;
    private AudioFX audioFX;
    private MediaPlayer mediaPlayer;

    //Getters
    /*
     * getFile returns a copy of File 'file' of the Player that this getter is
     * used on.
     */
    // public File getFile()
    // {
    //     String path = this.file.getAbsolutePath();
    //     return new File(path);
    // }
    /*

    /*
     * IsPlaying returns a boolean that tells you if there is a clip playing or
     * not.
     */
    public boolean IsPlaying()
    {
    	Status status = mediaPlayer.getStatus();
    	if (status.equals(MediaPlayer.Status.PLAYING)) {
    		return true;
    	}
    	return false;
    }
    //Setters

    /*
     * setFile sets the instance variable 'file' to whatever file is at the
     * directory path inputed.
     */

    //Constructors
    /*
     * Sets every instance variable to null or 0
     */
    public Player()
    {
        this.state = new UnreadyState(this);
        this.playlist = new Playlist();
        this.mediaPlayer = null;
        this.currentTrack = null;
        
        try {
            this.Library = Library.deserialize();
        } catch (ClassNotFoundException | IOException e) {
            this.Library = new Library();
        }
    }

    /*
     * Sets 'file' to a new File from the inputed directory path
     */
    // public Player(String fileDir) throws Exception
    // {
    //     this.file = new File(fileDir);
    // }

    /*
     * Sets 'file' to a new File from the inputed directory path and sets
     * 'startTime' to the inputed long (in microseconds).
     */
    // public Player(String fileDir, long microSec) throws Exception
    // {
    //     this.file = new File(fileDir);
    //     this.startTime = microSec;
    // }
    //Methods
    /*
     * Plays the clip (starts audio)
     */
    public void startPlayback()
    {
    	MediaPlayer player = this.mediaPlayer;
    	Status status =  player.getStatus();
    	Status paused = MediaPlayer.Status.PAUSED;
    	if (status.equals(paused)) {
    		player.play();
    	} else {
        	Media file;
    		try {
    			file = currentTrack.getMedia();
    	    	this.mediaPlayer = new MediaPlayer(file);
    	    	this.mediaPlayer.setOnReady(() -> {
    	    		mediaPlayer.setVolume(volume);
    	    		mediaPlayer.play();
    	    		this.state = new PlayingState(this);
    	    	});
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }

    /*
     * Stops the clip.
     */
    public void stopPlayback()
    {
    	if (mediaPlayer != null) {
    		mediaPlayer.stop();
    		mediaPlayer.dispose();
    		this.state = new ReadyState(this);
    	}
    }
    /*
     * Stops the clip and remembers where it left off, in order to be able to resume
     * from the same point again.
     */
    public void pausePlayback()
    {
    	if (mediaPlayer != null) {
    		mediaPlayer.pause();
    		this.state = new PausedState(this);
    	}
    }
    public void fastForward() {
    	if (mediaPlayer != null) {
    		Duration toAdd = new Duration(1000);
    		Duration newTime = mediaPlayer.getCurrentTime().add(toAdd);
    		mediaPlayer.seek(newTime);
    		mediaPlayer.play();
    		this.state = new PlayingState(this);
    	}
    }
    public void rewind() {
    	if (mediaPlayer != null) {
    		Duration toAdd = new Duration(-1000);
    		Duration newTime = mediaPlayer.getCurrentTime().add(toAdd);
    		mediaPlayer.seek(newTime);
    		mediaPlayer.play();
    		this.state = new PlayingState(this);
    	}
    }
    public void playNext() {
     	this.currentTrack = playlist.nextTrack();
    	startPlayback();
    }
    public void playPrevious() {
    	this.currentTrack = playlist.previousTrack();
    	startPlayback();
    }
    public State getState() {
    	return this.state;
    }
    public Library getLibrary() {
    	return this.Library;
    }
    public Playlist getPlaylist() {
    	return this.playlist;
    }
    public void setPlaylist(Playlist playlist) {
    	this.playlist = playlist;
    	this.currentTrack = this.playlist.getCurrentTrack();
    	this.state = new ReadyState(this);
    }
    public void setTrack(Track track) {
    	this.currentTrack = track;
    	this.playlist = new Playlist(track);
    	this.state = new ReadyState(this);
    }
}

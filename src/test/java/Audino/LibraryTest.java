package Audino;

import org.junit.Test;

import Audino.MediaControl.Library;
import Audino.MediaControl.Track;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LibraryTest {
    @Test
    public void testLibraryCreation(){
        File folder = new File("src/test/resources/test_audio/");
        ArrayList<String> tracks = new ArrayList<String>();
        for(File file: folder.listFiles()){
            tracks.add(file.getAbsolutePath());
        }
        System.out.println(tracks.toString());
        Library test = new Library();
        test.collectTracks(tracks);
        assertEquals(test.getTrackList().size(), tracks.size());
    }
    @Test
    public void testSerialization(){
        File folder = new File("src/test/resources/test_audio/");
        ArrayList<String> tracks = new ArrayList<String>();
        for (File file : folder.listFiles()) {
            try {
                tracks.add(file.getCanonicalPath());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Library test = new Library();
        test.collectTracks(tracks);
        try{
            test.serialize(test);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        File file = new File("Library.ser");

        assertTrue(file.exists());
        if (file.exists()){
            file.delete();
        }
    }
    @Test
    public void testDeserialization(){
        File folder = new File("src/test/resources/test_audio/");
        ArrayList<String> tracks = new ArrayList<String>();
        for (File file : folder.listFiles()) {
            try {
                tracks.add(file.getCanonicalPath());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Library test = new Library();
        test.collectTracks(tracks);
        try{
            test.serialize(test);
            File file = new File("Library.ser");
            assertTrue(file.exists());
            Library deserialized = new Library();
            deserialized = Library.deserialize();
            ArrayList<Track> testTrackList = test.getTrackList();
            ArrayList<Track> desTrackList = deserialized.getTrackList();

            assertEquals("Should have the same size tracklist", testTrackList.size(), desTrackList.size());
            for (int i = 0; i < testTrackList.size(); i++){
                assertTrue(testTrackList.get(i).equals(desTrackList.get(i)));
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
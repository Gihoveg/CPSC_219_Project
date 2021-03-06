package Audino.State.PlayerState;

import Audino.MediaControl.Player;

/**
 * State for when the player is ready to play but is not currently playing.
 * Example: Track is loaded, but not paused or started.
 *
 */
public class ReadyState extends PlayerState {

	public ReadyState(Player player) {
		super(player);
		//TODO Auto-generated constructor stub
	}

	@Override
	public void onPlay() {
		player.startPlayback();
	}

	@Override
	public void onStop() {
		// do nothing
	}

	@Override
	public void onNext() {
		player.playNext();
	}

	@Override
	public void onPrevious() {
		player.playPrevious();
	}

    @Override
    public void onFastForward() {
        player.fastForward();
    }

    @Override
    public void onRewind() {
        player.rewind();
    }

    @Override
    public void onSeek(double seekTo) {
        player.seek(seekTo);
    }

	@Override
	public void onRepeatChange() {
		// TODO Auto-generated method stub
      player.getPlaylist().getState().onNextState();

	}

	@Override
	public void onShuffleChange() {
		// TODO Auto-generated method stub

	}

}

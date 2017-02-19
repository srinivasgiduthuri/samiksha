package in.amigoscorp.samiksha.fragments;

import android.util.Log;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by sriny on 14/02/17.
 */

public class YouTubeListener implements YouTubePlayer.OnInitializedListener {
    private String videoId;
    private YouTubePlayer youTubePlayer;

    public YouTubeListener() {
    }

    public YouTubeListener(String videoId) {
        this.videoId = videoId;
    }
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        this.youTubePlayer = youTubePlayer;
        this.youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        this.youTubePlayer.setPlaybackEventListener(playbackEventListener);
        this.youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);

        if (!wasRestored) {
            if(StringUtils.isNotBlank(videoId)) {
                youTubePlayer.cueVideo(videoId);
            } else {
            }
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        String errorMessage = youTubeInitializationResult.toString();
        Log.d("errorMessage:", errorMessage);
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {

        @Override
        public void onBuffering(boolean arg0) {
        }

        @Override
        public void onPaused() {
        }

        @Override
        public void onPlaying() {
        }

        @Override
        public void onSeekTo(int arg0) {
        }

        @Override
        public void onStopped() {
        }

    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {

        @Override
        public void onAdStarted() {
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason arg0) {
            Log.i("YouTubePlayer", arg0.toString());
        }

        @Override
        public void onLoaded(String arg0) {
        }

        @Override
        public void onLoading() {
        }

        @Override
        public void onVideoEnded() {
        }

        @Override
        public void onVideoStarted() {
        }
    };
}

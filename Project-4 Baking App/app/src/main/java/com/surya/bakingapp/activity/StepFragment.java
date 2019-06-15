package com.surya.bakingapp.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.surya.bakingapp.R;


public class StepFragment extends Fragment {

    public static final String ARG_POS = "";
    static long currentposition;
    static boolean playwhenready = true;
    String des, videoLink, thumbnailLink;

    private SimpleExoPlayer simpleExoPlayer;
    private PlayerView playerView;
    private static long playerStopPosition;
    private boolean stopPlay;
    private long playbackPosition  = C.TIME_UNSET;;
    ImageView imageView;

    private static final String BAKING_AGENT = "baking_agent";
    private static final String PLAY_READY = "play_ready";
    private static final String CURRENT_POS = "current_pos";
    private static final String PLAY_BACK = "play_back";
    boolean isInPlayer = false;


    public StepFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assert getArguments() != null;
        if (getArguments().containsKey(ARG_POS)) {

            des = getArguments().getString("description");
            videoLink = getArguments().getString("videourl");
            thumbnailLink = getArguments().getString("thumbnnail");
            if (savedInstanceState != null) {

                playwhenready = savedInstanceState.getBoolean(PLAY_READY);
                currentposition = savedInstanceState.getLong(CURRENT_POS);
                playbackPosition = savedInstanceState.getLong(PLAY_BACK, C.TIME_UNSET);
                Log.i("SuryaTeja onsaved", String.valueOf(playbackPosition));

            } else {
                currentposition = 0;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_step, container, false);


        TextView desTextview = rootView.findViewById(R.id.step_desc);

        imageView = rootView.findViewById(R.id.thumbnail_image);
        playerView = rootView.findViewById(R.id.video_view);
        desTextview.setText(des);
        startvExoVideo();

        return rootView;
    }


    public void startvExoVideo() {

        if (!TextUtils.isEmpty(videoLink) && !isInPlayer) {
            playerView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getActivity()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(simpleExoPlayer);
            Uri uri = Uri.parse(videoLink);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultHttpDataSourceFactory(BAKING_AGENT)).createMediaSource(uri);
            simpleExoPlayer.prepare(mediaSource, true, false);
            isInPlayer = true;
            simpleExoPlayer.setPlayWhenReady(playwhenready);
            if (playbackPosition != 0 && !stopPlay) {
                simpleExoPlayer.seekTo(playbackPosition);
            } else {
                simpleExoPlayer.seekTo(playerStopPosition);
            }
        } else if (TextUtils.isEmpty(videoLink)) {
            playerView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            isInPlayer = false;
            if (!TextUtils.isEmpty(thumbnailLink)) {
                Glide.with(this).load(thumbnailLink).error(R.drawable.one).into(imageView);
            }

        }
    }

    public void stopExoPlayer() {
        if (simpleExoPlayer != null) {
            isInPlayer = false;
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer.setPlayWhenReady(false);
            simpleExoPlayer = null;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            startvExoVideo();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            playerStopPosition = simpleExoPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23 && simpleExoPlayer != null) {
            playerStopPosition = simpleExoPlayer.getCurrentPosition();
            stopPlay = true;
            stopExoPlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopExoPlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            startvExoVideo();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);


        outState.putLong(CURRENT_POS, currentposition);
        outState.putLong(PLAY_BACK, playerStopPosition);
        outState.putBoolean(PLAY_READY, simpleExoPlayer.getPlayWhenReady());

    }
}

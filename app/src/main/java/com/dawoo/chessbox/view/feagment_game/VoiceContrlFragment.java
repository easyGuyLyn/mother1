package com.dawoo.chessbox.view.feagment_game;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dawoo.chessbox.R;
import com.dawoo.chessbox.util.SharePreferenceUtil;
import com.dawoo.chessbox.util.SoundUtil;
import com.dawoo.chessbox.view.Utils.MediaplayerUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class VoiceContrlFragment extends BaseDialogFragment {

    @BindView(R.id.sb_music)
    SeekBar sbMusic;
    @BindView(R.id.sb_sound)
    SeekBar sbSound;
    @BindView(R.id.tv_tips)
    ImageView tvTips;
    @BindView(R.id.ll_tip_bg)
    RelativeLayout llTipBg;
    @BindView(R.id.img_close)
    ImageView imgClose;
    Unbinder unbinder;

    private float BGVoice = 1;
    private int BGShowVoice;
    private float SoundVoice = 1;
    private int SoundShowVoice;
    AudioManager audioManager;

    @Override
    protected int getViewId() {
        return R.layout.qt_voice_contrl_fragment;
    }

    @Override
    protected void initViews(View view) {
        audioManager = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
        BGVoice = SharePreferenceUtil.getVoiceBGSatus(getContext());
        SoundVoice = SharePreferenceUtil.getSoundSatus(getContext());
        BGShowVoice = (int) (BGVoice*100);
        SoundShowVoice = (int) (SoundVoice*100);
        sbMusic.setProgress(BGShowVoice);
        sbSound.setProgress(SoundShowVoice);

        sbMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float voice = (float) (progress/100.0);
                SharePreferenceUtil.saveVoiceBGStatus(getContext(),voice);
                if (voice == 0){
                    MediaplayerUtil.newInstance().close();
                }else {
                    MediaplayerUtil.newInstance().open();
                }
                MediaplayerUtil.newInstance().setVoice(voice);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float voice = (float) (progress/100.0);
                SharePreferenceUtil.saveSoundStatus(getContext(),voice);
                if (voice == 0){
                    SoundUtil.getInstance().close();
                }else {
                    SoundUtil.getInstance().open();
                }

                SoundUtil.getInstance().dowmVioce(voice);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    protected void initData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.img_close)
    public void onViewClicked() {
        SoundUtil.getInstance().startPlayOnClick(SoundUtil.ERROR);
        dismiss();
    }
}

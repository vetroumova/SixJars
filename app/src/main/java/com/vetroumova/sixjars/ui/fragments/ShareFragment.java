package com.vetroumova.sixjars.ui.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.StyleRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.plus.PlusShare;
import com.vetroumova.sixjars.R;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.model.VKScopes;

public class ShareFragment extends DialogFragment implements View.OnClickListener {
    ImageButton googleButton;
    ImageButton vkButton;
    ImageButton facebookButton;
    ImageButton twitterButton;
    String appPackageName;

    public ShareFragment() {
        // Required empty public constructor
    }

    public static ShareFragment newInstance() {
        ShareFragment fragment = new ShareFragment();
        //Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NO_FRAME, theme = android.R.style.Theme_Holo_Light_Dialog_NoActionBar;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        googleButton = (ImageButton) view.findViewById(R.id.share_google_button);
        facebookButton = (ImageButton) view.findViewById(R.id.share_facebook_button);
        vkButton = (ImageButton) view.findViewById(R.id.share_vk_button);
        twitterButton = (ImageButton) view.findViewById(R.id.share_twitter_button);
        googleButton.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        vkButton.setOnClickListener(this);
        twitterButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void setStyle(int style, @StyleRes int theme) {
        super.setStyle(style, theme);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_google_button: {
                appPackageName = getActivity().getApplicationContext().getPackageName();
                Intent shareIntent = new PlusShare.Builder(getActivity())
                        .setType("text/plain")
                        .setText(getString(R.string.share_message_text))
                        .setContentDeepLinkId("https://play.google.com/store/apps/details?id=" + appPackageName)
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName))
                        .getIntent();
                try {
                    startActivityForResult(shareIntent, 0);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.share_facebook_button: {
                Snackbar.make(getView(), "facebook", Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.share_vk_button: {
                Snackbar.make(getView(), "vk", Snackbar.LENGTH_SHORT).show();
                VKSdk.login(getActivity(), VKScopes.WALL, VKScopes.PHOTOS);
                break;
            }
            case R.id.share_twitter_button: {
                Snackbar.make(getView(), "twitter", Snackbar.LENGTH_SHORT).show();
                break;
            }
            case R.id.share_instagram_button: {
                Snackbar.make(getView(), "instagram", Snackbar.LENGTH_SHORT).show();
                break;
            }
        }
    }
}

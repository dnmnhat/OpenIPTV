package com.openiptv.code.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import com.openiptv.code.R;
import com.openiptv.code.SetupActivity;
import com.openiptv.code.epg.EPGService;

import java.util.List;

import static com.openiptv.code.epg.EPGService.setSetupComplete;

public class CompletedFragment extends GuidedStepSupportFragment {
    private static final int ACTION_ID_SETTINGS = 1;
    private static final int ACTION_ID_COMPLETE = 2;
    private static final String TAG = CompletedFragment.class.getName();

    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        return new GuidanceStylist.Guidance(
                "Completed",
                "Complete body",
                getString(R.string.account_label),
                null);
    }

    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        GuidedAction action = new GuidedAction.Builder(getActivity())
                .id(ACTION_ID_COMPLETE)
                .title("Complete title")
                .description("Complete body")
                .editable(false)
                .build();

        actions.add(action);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        if (action.getId() == ACTION_ID_COMPLETE) {

            setSetupComplete(getActivity(), true);

            Log.d(TAG, "Exiting Setup!");
            Intent intent = new Intent(getActivity(), EPGService.class);
            getActivity().startService(intent);

            // Wrap up setup
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();

        }
    }
}

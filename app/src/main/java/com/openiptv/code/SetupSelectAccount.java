package com.openiptv.code;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.leanback.app.GuidedStepSupportFragment;
import androidx.leanback.widget.GuidanceStylist;
import androidx.leanback.widget.GuidedAction;

import java.util.ArrayList;
import java.util.List;

public class SetupSelectAccount extends GuidedStepSupportFragment {
    @NonNull
    @Override
    public GuidanceStylist.Guidance onCreateGuidance(Bundle savedInstanceState) {

        return new GuidanceStylist.Guidance(
                "Select an Account",
                "Or add a new one",
                getString(R.string.account_label),
                ContextCompat.getDrawable(getActivity(), R.drawable.setup_logo2));
    }


    @Override
    public void onCreateActions(@NonNull List<GuidedAction> actions, Bundle savedInstanceState) {
        DatabaseActions databaseActions = new DatabaseActions(getContext());

        //get the data and append to a list


        Cursor accountList = databaseActions.getAccounts();

        ArrayList<String> accountClientNames = new ArrayList<>();
        while (accountList.moveToNext()) {
            //get the value from the database in client name column
            //then add it to the ArrayList
            accountClientNames.add(accountList.getString(5));
        }

        ArrayList<String> accountId = new ArrayList<>();

        accountList.moveToFirst();
        if (accountList.getCount() != 0) {
            do {
                //get the value from the database in id column
                //then add it to the ArrayList
                accountId.add(accountList.getString(0));
            } while (accountList.moveToNext());
        }

        List<GuidedAction> availableAccounts = new ArrayList<>();
        if (accountClientNames.size() > 0) {

            for (int i = 0; i < accountClientNames.size(); i++) {

                availableAccounts.add(new GuidedAction.Builder(getActivity())
                        .title(accountClientNames.get(i))
                        .id(Long.parseLong(accountId.get(i)))
                        .build());
            }
        } else {
            availableAccounts.add(new GuidedAction.Builder(getActivity())
                    .title("No Accounts, Add One!")
                    .editable(false)
                    .build());
        }

        GuidedAction accountSelector = new GuidedAction.Builder(getActivity())
                .title("Available Accounts")
                .description("")
                .editable(false)
                .subActions(availableAccounts)
                .build();
        actions.add(accountSelector);

        GuidedAction addNewAccount = new GuidedAction.Builder(getActivity())
                .title("Add new Account")
                .editable(false)
                .build();

        GuidedAction skipButton = new GuidedAction.Builder(getActivity())
                .title("Skip")
                .editable(false)
                .build();

        actions.add(addNewAccount);
        actions.add(skipButton);
    }

    public void addNewAccountFragmentStarter() {
        GuidedStepSupportFragment fragment = new SetupNewAccountFragment();
        fragment.setArguments(getArguments());
        add(getFragmentManager(), fragment);
    }

    @Override
    public void onGuidedActionClicked(GuidedAction action) {
        /**
         * If add new account is selected start account setup fragment
         * Else, if the skip button is pressed continue without account
         */
        if (action.getTitle().toString().equals("Add new Account")) {
            addNewAccountFragmentStarter();
        } else if (action.getTitle().toString().equals("Skip")) {

            //TODO Change intent to the correct activity
            Intent intent = new Intent();

            intent.setClass(getActivity(), MainActivity.class);
            startActivity(intent);
            startActivity(intent);
        }
    }

    @Override
    public boolean onSubGuidedActionClicked(GuidedAction action) {
        if (action.getTitle().equals("No Accounts, Add One!")) {
            addNewAccountFragmentStarter();
        } else {
            DatabaseActions databaseActions = new DatabaseActions(getContext());

            Cursor accountSelected = databaseActions.getAccountByID(String.valueOf(action.getId()));
            Bundle accountDetails = new Bundle(6);
            accountSelected.moveToFirst();
            accountDetails.putString("id", accountSelected.getString(0));
            accountDetails.putString("username", accountSelected.getString(1));
            accountDetails.putString("password", accountSelected.getString(2));
            accountDetails.putString("hostname", accountSelected.getString(3));
            accountDetails.putString("port", accountSelected.getString(4));
            accountDetails.putString("clientName", accountSelected.getString(5));

            Intent intent = new Intent();

            //TODO Change intent to the correct activity
            intent.setClass(getActivity(), MainActivity.class);
            intent.putExtra("CurrentAccount", accountDetails);
            startActivity(intent);


        }
        return super.onSubGuidedActionClicked(action);
    }
}

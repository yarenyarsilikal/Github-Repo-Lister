package com.example.githubrepolister.ui.activity;

import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.githubrepolister.R;
import com.example.githubrepolister.network.Connection;
import com.example.githubrepolister.network.WebService;
import com.example.githubrepolister.network.model.RepoResponse;
import com.example.githubrepolister.ui.adapter.RepoAdapter;
import com.example.githubrepolister.util.PreferencesUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity implements View.OnClickListener, RepoAdapter.ItemClickListener {

    public final static String KEY_REPO = "repo";
    public final static String KEY_ISFAV = "isfav";

    private String username;
    private boolean isFav;

    private PreferencesUtil preferencesUtil;
    private RecyclerView rvRepo;
    private List<RepoResponse> repoList;
    private RepoAdapter repoAdapter;
    private TextInputEditText etUsername;

    //region LIFECYCLE METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeResources();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (repoAdapter != null)
            repoAdapter.notifyDataSetChanged();
    }
    //endregion


    //region INITIALIZATION METHODS
    private void initializeViews() {
        AppCompatButton button = findViewById(R.id.btn_submit);
        button.setOnClickListener(this);

        rvRepo = findViewById(R.id.rv_repo);
        etUsername = findViewById(R.id.et_username);
        etUsername.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)) {
                usernameEvent();
            }
            return false;
        });
    }

    private void initializeResources() {
        repoList = new ArrayList<>();
        preferencesUtil = new PreferencesUtil(getApplicationContext());
    }

    private void initializeRepoAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        rvRepo.setLayoutManager(layoutManager);

        repoAdapter = new RepoAdapter((ArrayList<RepoResponse>) repoList, this, preferencesUtil);
        rvRepo.setAdapter(repoAdapter);
    }
    //endregion

    //region FLOW METHODS

    @Override
    public void onClick(View v) {
        usernameEvent();
    }

    //endregion

    //region STORY METHODS

    private void getConnection() {
        WebService webService = Connection.getClient().create(WebService.class);

        Call<List<RepoResponse>> call = webService.getUserRepos(username);
        call.enqueue(new Callback<List<RepoResponse>>() {
            @Override
            public void onResponse(Call<List<RepoResponse>> call, Response<List<RepoResponse>> response) {
                if (response.body() != null) {
                    repoList.addAll(response.body());
                    initializeRepoAdapter();
                    hideKeyboard(getApplicationContext(), etUsername);
                } else {
                    if (repoAdapter != null)
                        repoAdapter.notifyDataSetChanged();
                    showToast(getApplicationContext(), getResources().getString(R.string.invalid_username));
                }
                hideProgressDialog();
            }

            @Override
            public void onFailure(Call<List<RepoResponse>> call, Throwable t) {
                hideProgressDialog();
                if (isNetworkAvailable(getApplicationContext())) {
                    showToast(getApplicationContext(), getResources().getString(R.string.error_message));
                } else {
                    showToast(getApplicationContext(), getResources().getString(R.string.no_network_error_message));
                }
            }
        });
    }

    @Override
    public void onRepoClicked(int position, List<RepoResponse> repoList) {
        startDetailActivity(position, repoList);
    }

    private void usernameEvent() {
        if (etUsername.getText() != null) {
            username = etUsername.getText().toString().trim();
            repoList.clear();
            getConnection();
            showProgressDialog();
        }
    }

    private void startDetailActivity(int position, List<RepoResponse> repoList) {
        RepoResponse repo = repoList.get(position);
        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
        intent.putExtra(KEY_REPO, repo);
        if (preferencesUtil.getRepoIdList(DetailActivity.KEY_SAVED_ID) != null)
            isFav = preferencesUtil.getRepoIdList(DetailActivity.KEY_SAVED_ID).contains(repo.getId().toString());
        else
            isFav = false;
        intent.putExtra(KEY_ISFAV, isFav);
        startActivity(intent);
    }
    //endregion

}

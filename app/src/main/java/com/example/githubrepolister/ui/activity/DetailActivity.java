package com.example.githubrepolister.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubrepolister.R;
import com.example.githubrepolister.network.Connection;
import com.example.githubrepolister.network.WebService;
import com.example.githubrepolister.network.model.RepoResponse;
import com.example.githubrepolister.util.PreferencesUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;

public class DetailActivity extends BaseActivity {

    public static final String KEY_SAVED_ID = "saved_id";


    private String avatarURL;
    private String username;
    private String starsCount;
    private String openIssuesCount;
    private String repoId;
    private String repoName;
    private PreferencesUtil preferencesUtil;

    private boolean isFav;

    //region LIFECYCLE METHODS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        showProgressDialog();

        initializeResources();
        initializeViews();
    }

    //endregion


    //region INITIALIZATION METHODS
    private void initializeViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(repoName);
        toolbar.setNavigationOnClickListener(view -> finish());

        ImageView ivAvatar = findViewById(R.id.iv_avatar);
        Picasso.get().load(avatarURL).into(ivAvatar);
        TextView tvOwner = findViewById(R.id.tv_username);
        tvOwner.setText(username);
        TextView tvStars = findViewById(R.id.tv_star_count);
        tvStars.setText(starsCount);
        TextView tvOpenIssues = findViewById(R.id.tv_issues_count);
        tvOpenIssues.setText(openIssuesCount);

        hideProgressDialog();
    }

    private void initializeResources() {
        Intent intent = getIntent();
        handleIntent(intent);

        preferencesUtil = new PreferencesUtil(getApplicationContext());
    }
    //endregion


    //region FLOW METHODS

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if (isFav)
            menu.getItem(0).setIcon(R.drawable.ic_fav);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_favorite) {
            handleFav(item);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // endregion


    //region STORY METHODS

    private void handleIntent(Intent intent) {
        RepoResponse repo = intent.getParcelableExtra(MainActivity.KEY_REPO);
        avatarURL = repo.getOwner().getAvatarUrl();
        username = repo.getOwner().getLogin();
        starsCount = repo.getStargazersCount().toString();
        openIssuesCount = repo.getOpenIssues().toString();
        repoId = repo.getId().toString();
        isFav = intent.getBooleanExtra(MainActivity.KEY_ISFAV, false);
        repoName = repo.getName();
    }

    private void handleFav(MenuItem item) {
        List<String> favRepoList;
        if (preferencesUtil.getRepoIdList(KEY_SAVED_ID) != null) {
            favRepoList = preferencesUtil.getRepoIdList(KEY_SAVED_ID);
            if (favRepoList.contains(repoId)) {
                favRepoList.remove(repoId);
                item.setIcon(R.drawable.ic_unfav);
            } else {
                item.setIcon(R.drawable.ic_fav);
                favRepoList.add(repoId);
            }

        } else {
            favRepoList = new ArrayList<>();
            favRepoList.add(repoId);
            item.setIcon(R.drawable.ic_fav);
        }
        preferencesUtil.saveRepoIdList(KEY_SAVED_ID, favRepoList);

    }
    //endregion


}

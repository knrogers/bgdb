package com.example.android.bgdb.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.bgdb.R;
import com.example.android.bgdb.model.BoardGame;
import com.example.android.bgdb.view.adapter.BaseListAdapter;
import com.example.android.bgdb.view.adapter.BaseListAdapter.BoardGameOnClickHandler;
import com.example.android.bgdb.view.adapter.ListAdapter;

import java.util.List;

import butterknife.BindView;

/**
 * Provides the base view for a list fragment.
 */
public abstract class BaseListViewImpl extends Fragment implements
        BaseListView,
        BoardGameOnClickHandler {

    static final String SEARCH_TYPE = "searchType";
    static final String BOARD_GAME_TAG_ID = "boardGameTagId";

    private BaseListAdapter adapter;
    private RecyclerView.SmoothScroller smoothScroller;

    @BindView(R.id.list_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.list_progress_bar)
    ProgressBar progressBar;

    @Override
    public void onCreate(ListAdapter adapter) {
        this.adapter = adapter;

        recyclerView.setAdapter(adapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        Context context = getContext();
        if (context != null) {
            smoothScroller = new LinearSmoothScroller(context) {
                @Override protected int getVerticalSnapPreference() {
                    return LinearSmoothScroller.SNAP_TO_START;
                }
            };
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }

        if (savedInstanceState.containsKey(getString(R.string.selected_index))) {
            int index = savedInstanceState.getInt(getString(R.string.selected_index));
            adapter.setSelectedIndex(index);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(getString(R.string.selected_index), adapter.getSelectedIndex());
    }

    @Override
    public void onPreLoad() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostLoad(List<BoardGame> boardGames) {
        progressBar.setVisibility(View.GONE);
        if (boardGames == null || boardGames.isEmpty()) {
            displayEmptyView();
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            updateAdapter(boardGames);
        }
    }

    private void updateAdapter(List<BoardGame> boardGames) {
        adapter.setBoardGames(boardGames);
        adapter.notifyDataSetChanged();
    }

    private void displayEmptyView() {

    }

    public void resetScrollPosition() {
        smoothScroller.setTargetPosition(0);
        LinearLayoutManager layoutManager =
                (LinearLayoutManager) recyclerView.getLayoutManager();
        layoutManager.startSmoothScroll(smoothScroller);
    }

    public int getScrollOffset() {
        return recyclerView.computeVerticalScrollOffset();
    }
}
package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.lists.TableAdapter;
import com.dinya.peter.livefootballresults.sync.BackgroundSyncUtils;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnTableFragmentInteractionListener}
 * interface.
 */
public class TableFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, TableAdapter.ListItemClickListener {


    // TODO: Customize parameters
    private int mColumnCount = 1;
    private LoaderManager mLoaderManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TableAdapter mAdapter;

    private LinearLayout mHeader;

    private OnTableFragmentInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private RecyclerView.AdapterDataObserver mAdapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            if(0 == mAdapter.getItemCount()){
                mRecyclerView.setVisibility(View.GONE);
                mHeader.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
            }else {
                mRecyclerView.setVisibility(View.VISIBLE);
                mHeader.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
            }
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TableFragment() {
    }

    public static Fragment newInstance() {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new TableAdapter(getContext(), this);
        mAdapter.registerAdapterDataObserver(mAdapterDataObserver);
        mLoaderManager = getActivity().getLoaderManager();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdapter.unregisterAdapterDataObserver(mAdapterDataObserver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_table, container, false);

        Context context = view.getContext();

        mHeader = (LinearLayout) view.findViewById(R.id.ll_table_header);
        mEmptyView = (TextView) view.findViewById(R.id.tv_empty_view);
        /*
         * RecyclerView initialization
         */
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_table);
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        /*
         * SwipeToRefresh initialization
         */
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh_table);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchData();
            }
        });

        mLoaderManager.initLoader(MainActivity.TABLE_LOADER_ID, null, this);
        return view;
    }

    private void fetchData() {
        if(!BackgroundSyncUtils.startImmediateSync(getContext()))
            mSwipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTableFragmentInteractionListener) {
            mListener = (OnTableFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * ------------------
     * LoaderCallbacks
     * ------------------
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id){
            case MainActivity.TABLE_LOADER_ID:
                return new CursorLoader(getContext(),DbContract.TeamEntry.CONTENT_URI_TABLE,null,null,null,null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swap(data);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swap(null);
    }


    @Override
    public void listItemClick(long id) {
//        Uri uri = ContentUris.withAppendedId(DbContract.TeamEntry.CONTENT_URI_TEAMS,id);
//        Cursor cursor = getContext().getContentResolver().query(uri,null, DbContract.TeamEntry._ID + " = ?",new String[]{String.valueOf(id)},null);
//        if(cursor != null && cursor.moveToFirst()){
//            String text = cursor.getString(cursor.getColumnIndex(DbContract.TeamEntry.COLUMN_TEAM_NAME));
//            Toast.makeText(getContext(),text,Toast.LENGTH_LONG).show();
//            cursor.close();
//        }
        Intent intent = new Intent(getContext(),TeamActivity.class);
        intent.putExtra("id", id);
        getActivity().startActivity(intent);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTableFragmentInteractionListener {
        void onTableFragmentInteraction(long teamId);
    }
}

package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dinya.peter.livefootballresults.database.DbContract;
import com.dinya.peter.livefootballresults.database.DbHelper;
import com.dinya.peter.livefootballresults.lists.MatchAdapter;
import com.dinya.peter.livefootballresults.sync.BackgroundSyncUtils;
import com.dinya.peter.livefootballresults.utils.NetworkUtils;

import java.util.Arrays;



public class FinishedGamesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = FinishedGamesFragment.class.getSimpleName();
    private static final int UPCOMING_GAMES_LOADER_ID = 1;
    private static final int FINISHED_GAMES_LOADER_ID = 2;

    private RecyclerView mMatchesRecyclerView;
    MatchAdapter mMatchAdapter;
    private SQLiteDatabase mDb;
    private LoaderManager mLoaderManager;

//    private OnFragmentInteractionListener mListener;

    public FinishedGamesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FinishedGamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FinishedGamesFragment newInstance() {
        FinishedGamesFragment fragment = new FinishedGamesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMatchAdapter = new MatchAdapter();
        mLoaderManager = getActivity().getLoaderManager();
        mLoaderManager.initLoader(FINISHED_GAMES_LOADER_ID, null, this);
        DbHelper dbHelper = new DbHelper(getContext());
        mDb = dbHelper.getWritableDatabase();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_finished_games, container, false);
        mMatchesRecyclerView = (RecyclerView) view.findViewById(R.id.rv_finished_games);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mMatchesRecyclerView.setLayoutManager(linearLayoutManager);
        mMatchesRecyclerView.setAdapter(mMatchAdapter);
        mMatchesRecyclerView.setHasFixedSize(true);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * ------------------
     * LoaderCallbacks
     * ------------------
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selectionArgs;
        switch (id){
            case UPCOMING_GAMES_LOADER_ID:
                selectionArgs = DbContract.getCurrentDateSelectionArgs();
                Log.d(TAG, "Selection: " + Arrays.toString(selectionArgs));
                return new CursorLoader(getActivity(),DbContract.GameEntry.CONTENT_URI_UPCOMING_GAMES,null, null, selectionArgs,null);
            case FINISHED_GAMES_LOADER_ID:
                selectionArgs = DbContract.getCurrentDateSelectionArgs();
                Log.d(TAG, "Selection: " + Arrays.toString(selectionArgs));
                return new CursorLoader(getActivity(),DbContract.GameEntry.CONTENT_URI_FINISHED_GAMES,null, null, selectionArgs,null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMatchAdapter.swap(data);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMatchAdapter.swap(null);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
}
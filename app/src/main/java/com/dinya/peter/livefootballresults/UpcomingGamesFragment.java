package com.dinya.peter.livefootballresults;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DividerItemDecoration;
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



public class UpcomingGamesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = FinishedGamesFragment.class.getSimpleName();
    private static final int UPCOMING_GAMES_LOADER_ID = 1;
    private static final int FINISHED_GAMES_LOADER_ID = 2;

    private RecyclerView mMatchesRecyclerView;
    MatchAdapter mMatchAdapter;
    private SQLiteDatabase mDb;
    private LoaderManager mLoaderManager;

//    private OnFragmentInteractionListener mListener;

    public UpcomingGamesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment FinishedGamesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpcomingGamesFragment newInstance() {
        UpcomingGamesFragment fragment = new UpcomingGamesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMatchAdapter = new MatchAdapter();
        mLoaderManager = getActivity().getLoaderManager();
        mLoaderManager.initLoader(UPCOMING_GAMES_LOADER_ID, null, this);
        DbHelper dbHelper = new DbHelper(getContext());
        mDb = dbHelper.getWritableDatabase();
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upcoming_games, container, false);
        mMatchesRecyclerView = (RecyclerView) view.findViewById(R.id.rv_upcoming_games);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),linearLayoutManager.getOrientation());
        mMatchesRecyclerView.addItemDecoration(dividerItemDecoration);
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
                int daysAhead = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getContext()).getString("days_ahead", "5"));
                selectionArgs = DbContract.getDateSelectionArgs(daysAhead); // games in the upcoming 'dayDiff' days
                Log.d(TAG, "Selection: " + Arrays.toString(selectionArgs));
                return new CursorLoader(getActivity(),DbContract.GameEntry.CONTENT_URI_UPCOMING_GAMES,null, null, selectionArgs,null);
            case FINISHED_GAMES_LOADER_ID:
                int daysPast = PreferenceManager.getDefaultSharedPreferences(getContext()).getInt("days_past", 5);
                selectionArgs = DbContract.getDateSelectionArgs(daysPast);
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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mLoaderManager.restartLoader(UPCOMING_GAMES_LOADER_ID,null,this);
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

package com.lawrencium.basil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Frag_GraphButton.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Frag_GraphButton#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Frag_GraphButton extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "cat_id";
    private static final String ARG_NAME = "cat_name";
    private static final String ARG_TOTAL = "cat_total";

    // TODO: Rename and change types of parameters
    private String cat_id;
    private String cat_name;
    private String cat_total;

    private OnFragmentInteractionListener mListener;
//    SQLiteDbHelper mDbHelper = new SQLiteDbHelper(getActivity());

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cat_name Parameter 1.
     * @param cat_total Parameter 2.
     * @return A new instance of fragment Frag_GraphButton.
     */
    // TODO: Rename and change types and number of parameters
    public static Frag_GraphButton newInstance(String cat_id, String cat_name, String cat_total) {
        Frag_GraphButton fragment = new Frag_GraphButton();
        Bundle args = new Bundle();
        args.putString(ARG_ID, cat_id);
        args.putString(ARG_NAME, cat_name);
        args.putString(ARG_TOTAL, cat_total);
        fragment.setArguments(args);
        return fragment;

        //fragments ALWAYS go to onCreate -> onCreateView -> onAttach (not as important at the moment)
    }

    public Frag_GraphButton() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //for logic
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cat_id = getArguments().getString(ARG_ID);
            cat_name = getArguments().getString(ARG_NAME);
            cat_total = getArguments().getString(ARG_TOTAL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //things a user sees
        View v = inflater.inflate(R.layout.fragment_graph_button, container, false);
        Button catName = (Button) v.findViewById(R.id.catButton);
        catName.setText("[" + cat_id + "] " + cat_name + ": " + cat_total);
        catName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // here you set what you want to do when user clicks your button,
                // e.g. launch a new activity
                //deleteCategory(v);
                Intent intent = new Intent(getActivity(), Act_CategoryView.class);
                Bundle bundle = new Bundle();
                bundle.putString("CAT_NAME", cat_name);
                bundle.putString("CAT_TOTAL", cat_total);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        /*catName.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // here you set what you want to do when user clicks your button,
                // e.g. launch a new activity
                //deleteCategory(v);
                Intent intent = new Intent(getActivity(), Act_CategoryView.class);
                startActivity(intent);
                return true;
            }
        });*/
        registerForContextMenu(catName);

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Integer.parseInt(cat_id), 1, 0, "Delete");
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_overview_category_floating, menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == Integer.parseInt(cat_id)) {
            System.out.println("\n id from menu: " + cat_id);
            switch (item.getItemId()) {
                case 1:
                    deleteCategory();
                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }
        return false;
        /*AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.category_delete:
                //editNote(info.id);
                deleteCategory();
                return true;
            default:
                return super.onContextItemSelected(item);
        }*/
    }


    public String getCatId() {
        return cat_id;
    }
    public void deleteCategory() {
        Act_BudgetOverview budgetOverviewActivity = (Act_BudgetOverview) getActivity();
        System.out.println("\n id from frag: " + cat_id);
        budgetOverviewActivity.removeCategory(this);
    }
}

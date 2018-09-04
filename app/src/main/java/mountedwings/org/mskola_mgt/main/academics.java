package mountedwings.org.mskola_mgt.main;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.utils.Tools;


public class academics extends Fragment {

    private OnFragmentInteractionListener mListener;
    View view;
    private View search_bar;

    public academics() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    public static academics newInstance(int instance) {
        Bundle args = new Bundle();
        args.putInt("argsInstance", instance);
        academics academics = new academics();
        academics.setArguments(args);
        return academics;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_food_order, container, false);
        // Inflate the layout for this fragment
        Objects.requireNonNull(getActivity()).setTitle(getString(R.string.food_order));
        initComponent();
        return view;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.light_blue_500), PorterDuff.Mode.SRC_ATOP);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setTitle("Twitter");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(getActivity(), R.color.grey_20);
    }

    private void initComponent() {
        search_bar = (View) view.findViewById(R.id.search_bar);

        NestedScrollView nested_content = (NestedScrollView) view.findViewById(R.id.nested_scroll_view);
        nested_content.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY < oldScrollY) { // up
                    animateSearchBar(false);
                }
                if (scrollY > oldScrollY) { // down
                    animateSearchBar(true);
                }
            }
        });
    }

    boolean isFabHide = false;

//    private void animateFab(final boolean hide) {
//        FloatingActionButton fab_add = (FloatingActionButton) view.findViewById(R.id.fab_add);
//        if (isFabHide && hide || !isFabHide && !hide) return;
//        isFabHide = hide;
//        int moveY = hide ? (2 * fab_add.getHeight()) : 0;
//        fab_add.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
//    }

    boolean isSearchBarHide = false;

    private void animateSearchBar(final boolean hide) {
        if (isSearchBarHide && hide || !isSearchBarHide && !hide) return;
        isSearchBarHide = hide;
        int moveY = hide ? -(2 * search_bar.getHeight()) : 0;
        search_bar.animate().translationY(moveY).setStartDelay(100).setDuration(300).start();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}

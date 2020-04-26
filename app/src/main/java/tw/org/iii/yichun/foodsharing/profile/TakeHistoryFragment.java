package tw.org.iii.yichun.foodsharing.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tw.org.iii.yichun.foodsharing.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TakeHistoryFragment extends Fragment {

    public TakeHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_take_history, container, false);
    }
}

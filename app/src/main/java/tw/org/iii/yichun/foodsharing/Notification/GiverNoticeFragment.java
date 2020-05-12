package tw.org.iii.yichun.foodsharing.Notification;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tw.org.iii.yichun.foodsharing.R;

public class GiverNoticeFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_giver_notice, container, false);


        return view;
    }
}

package tw.org.iii.yichun.foodsharing;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public final class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> mfragments;

    public FragmentAdapter(List<Fragment> fragments, FragmentManager fm) {
        super(fm);
        this.mfragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int i) {
        return mfragments.get(i);
    }

    @Override
    public int getCount() {
        return mfragments.size();
    }
}

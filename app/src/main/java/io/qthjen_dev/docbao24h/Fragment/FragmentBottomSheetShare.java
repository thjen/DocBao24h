package io.qthjen_dev.docbao24h.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.qthjen_dev.docbao24h.R;

public class FragmentBottomSheetShare extends BottomSheetDialogFragment {

    private View mView;

    public FragmentBottomSheetShare() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_fragment_bottom_sheet_share, container, false);

        return mView;
    }
}

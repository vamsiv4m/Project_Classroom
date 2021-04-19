package fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectclassroom.R;

public class ShareFilesFragment extends Fragment {
    RecyclerView recycler;
    public ShareFilesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_share_files, container, false);
        recycler=v.findViewById(R.id.sharerecycler);
        return v;
    }
}
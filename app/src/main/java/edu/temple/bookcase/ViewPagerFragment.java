package edu.temple.bookcase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

public class ViewPagerFragment extends Fragment {

    private static final String BOOK_TITLE = "key";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private String book, title, author;
    TextView tv, tv1;

    //This class is not used

    public ViewPagerFragment(){
    }

    public static ViewPagerFragment newInstance(HashMap<Integer,String> hm){
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, hm.get(1));
        bundle.putString(AUTHOR, hm.get(2));
        //Log.d("hello", "key: " + String.valueOf(hm.get(1)) + " value = " + String.valueOf(hm.get(2)));
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ViewPagerFragment newInstance(String title){
        ViewPagerFragment fragment = new ViewPagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BOOK_TITLE, title);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            //book = getArguments().getString(BOOK_TITLE);
            title = bundle.getString(TITLE);
            author = getArguments().getString(AUTHOR);
        }else if(bundle == null){
            //Log.d("hello", "bundles is null");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.view_pager_fragment_activity, container, false);
        tv = view.findViewById(R.id.book_title1);
        tv1 = view.findViewById(R.id.book_author1);
        tv.setText(title);
        tv1.setText(author);
        return view;
    }

}



package edu.temple.bookcase;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class BookListFragment extends Fragment {

    private static final String BOOK_LIST = "key";
    ArrayList<Book> books;
    ListView listView;
    private BookListFragmentListener bl;

    public BookListFragment(){

    }

    public static BookListFragment newInstance(ArrayList<Book> books){
        BookListFragment blFragment = new BookListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(BOOK_LIST, books);
        blFragment.setArguments(bundle);
        return blFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            books = getArguments().getParcelableArrayList(BOOK_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.book_list_fragment_activity, container, false);
        ArrayAdapter arrayAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1 ,books){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView, parent);
                TextView text1 = view.findViewById(android.R.id.text1);
                TextView text2 = view.findViewById(android.R.id.text2);
                text1.setText(books.get(position).getTitle());
                text2.setText(books.get(position).getAuthor());
                text2.setTextColor(Color.DKGRAY);

                return view;
            }
        };
        listView = view.findViewById(R.id.book_list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener((adapterView, view1, i, l) -> {
            bl.selectedBook(books.get(i), i); //When user clicks on list view element
        });

        return view;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof BookListFragmentListener){
            bl = (BookListFragmentListener) context;
        } else{
            throw new RuntimeException(context.toString()
                        + " must implement BookListFragmentListener");
        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        bl = null;
    }


    public interface BookListFragmentListener{
        void selectedBook(Book book, int position);
    }
}

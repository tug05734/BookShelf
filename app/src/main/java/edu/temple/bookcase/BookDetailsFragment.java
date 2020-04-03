package edu.temple.bookcase;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BookDetailsFragment extends Fragment{
    private static final String BOOK_TITLE = "key";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static Book books;
    private String book, title, author;
    TextView tv, tv1;
    private BookDetailsFragmentListener bd;

    public BookDetailsFragment(){
    }

    public static BookDetailsFragment newInstance(String title, String author){
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putString(AUTHOR, author);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static BookDetailsFragment newInstanced(Book book){ //Old code disregard
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BOOK_TITLE, book);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d("hello", "made it");
        if(getArguments() != null){
            books = getArguments().getParcelable(BOOK_TITLE);
           // book = getArguments().getString(BOOK_TITLE);
            //title = getArguments().getString(TITLE);
            //author = getArguments().getString(AUTHOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.book_details_fragment_activity, container, false);
        tv = view.findViewById(R.id.book_title);
        tv1 = view.findViewById(R.id.book_author);
        tv.setText(books.getTitle());
        tv1.setText(books.getAuthor());
        return view;
    }



    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if(context instanceof BookDetailsFragmentListener){
            bd = (BookDetailsFragmentListener) context;
        } else{
            throw new RuntimeException(context.toString()
                        + " must implement BookDetailsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        bd = null;
    }



    public interface BookDetailsFragmentListener{
        void displayBook(Book book);
    }

}

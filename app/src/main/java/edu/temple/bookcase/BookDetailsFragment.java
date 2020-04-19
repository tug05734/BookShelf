package edu.temple.bookcase;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class BookDetailsFragment extends Fragment{
    private static final String BOOK_TITLE = "key";
    private static Book books;
    TextView tv, tv1;
    ImageView cover;
    private BookDetailsFragmentListener bd;

    public BookDetailsFragment(){
    }


    public static BookDetailsFragment newInstance(Book book){
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(BOOK_TITLE, book);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            books = getArguments().getParcelable(BOOK_TITLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.book_details_fragment_activity, container, false);
        tv = view.findViewById(R.id.book_title);
        tv1 = view.findViewById(R.id.book_author);
        cover = view.findViewById(R.id.imageView);
        tv.setText(books.getTitle());
        tv1.setText(books.getAuthor());
        if(!books.getCoverURL().isEmpty())
            Picasso.get().load(books.getCoverURL()).into(cover);

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

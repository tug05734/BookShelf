package edu.temple.bookcase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class BookListFragment extends Fragment {

    //private ArrayList<String> books;
    private static final String BOOK_LIST = "key";
    private static final String TITLE = "title";
    private static final String AUTHOR = "author";
    private static final String HASHMAP = "hashmap";
    ArrayList<HashMap<Integer, String>> newBooks = new ArrayList<>();
    ArrayList<Book> books;
    String book, title, author;
    ListView listView;
    private BookListFragmentListener bl;

    public BookListFragment(){

    }

    //creates new instance of BookListFragment
    public static BookListFragment newInstance(ArrayList<HashMap<Integer, String>> hm){
        Log.d("hello", "It got here 2");
        BookListFragment blFragment = new BookListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(HASHMAP, (Serializable) hm);
       // bundle.putString(TITLE, hm.get(0).get(1));
        //bundle.putString(AUTHOR, hm.get(2));
        blFragment.setArguments(bundle);
        return blFragment;
    }

    public static BookListFragment newInstanced(ArrayList<Book> books){ //Old code disregard
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
            //title = getArguments().getString(TITLE);
            //author = getArguments().getString(AUTHOR);
           // newBooks = (ArrayList<HashMap<Integer, String>>)getArguments().getSerializable(HASHMAP); //Stores the hashmap from bundle to newBooks
            Log.d("hello", "It got here 3");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.book_list_fragment_activity, container, false);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_2, android.R.id.text1 ,books){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){ //Populates the list view with the book details from the hashmap
                View view = super.getView(position,convertView, parent);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                Log.d("hello", "It got here 4");
                text1.setText(books.get(position).getTitle());
                text2.setText(books.get(position).getAuthor());
                Log.d("hello", "It got here 5");
               /* while(newBooks.iterator().hasNext()){
                    text1.setText(newBooks.get(i).get(1));
                    text2.setText(newBooks.get(i).get(2));
                    Log.d("hello", "key: " + String.valueOf(newBooks.get(1)) + " value = " + String.valueOf(newBooks.get(2)));
                    Log.d("hello", "key: " + String.valueOf(newBooks.get(1)) + " value = " + String.valueOf(newBooks.get(2)));
                    i++;
                }

                */

                return view;
            }
        };
        listView = view.findViewById(R.id.book_list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bl.selectedBook(books.get(i)); //When user clicks on listview element
            }
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
        void selectedBook(Book book);
    }
}

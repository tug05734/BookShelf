package edu.temple.bookcase;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentListener, BookDetailsFragment.BookDetailsFragmentListener {
    ArrayList<Book> books;
    EditText bookSearch;
    URL website;
    JSONArray jsonArray = new JSONArray();
    Message msg;
    private static int selectedBookPosition = -1; //Holds the position of the book in list view returned by selectedBook()
    Book startUpBook = new Book(0, "", "", ""); //Used to avoid crashes
    private static int startUp = 0; // 0 - app is initially launched; 1 - app is not in start up (the first search has been made)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bookSearch = findViewById(R.id.bookSearch);
        findViewById(R.id.button).setOnClickListener(v -> { //When search is clicked, app will return list of books matching the search parameter
            startUp = 1; //Lets program know that the first search has been made and app is no longer in start up
            new Thread() {
                @Override
                public void run() { //Queries the search
                    try {
                        if (bookSearch.getText().toString().isEmpty())
                            website = new URL("https://kamorris.com/lab/abp/booksearch.php");
                        else
                            website = new URL("https://kamorris.com/lab/abp/booksearch.php?search=" + bookSearch.getText().toString());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(website.openStream()));
                        String jL = reader.readLine();
                        jsonArray = new JSONArray(jL);
                        msg = Message.obtain();
                        msg.obj = jsonArray;
                        booksHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        });

        //Checks if app is in orientation and if it in start up, if so returns book details fragment with an empty book to avoid crash
        if (checkTablet() || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && startUp == 0) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.book_details_view, BookDetailsFragment.newInstance(startUpBook)).addToBackStack(null)
                    .commit();
        } else if(savedInstanceState != null && startUp != 0){ //If activity has restarted and a search has been made
            bookSearch.setText(savedInstanceState.getString("Search"));
            String contents = savedInstanceState.getString("SearchResult");
            try {
                jsonArray = new JSONArray(contents);
                msg = Message.obtain();
                msg.obj = jsonArray;
                booksHandler.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) { //Saves the search and search results when on restart
        super.onSaveInstanceState(outState);
        outState.putString("Search", bookSearch.getText().toString());
        outState.putString("SearchResult", jsonArray.toString());
    }

    //Not my code
    private boolean checkTablet() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels / displayMetrics.densityDpi;
        int width = displayMetrics.widthPixels / displayMetrics.densityDpi;
        double screenDiagonal = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));

        return screenDiagonal >= 7.0;
    }

    //Method that will display chosen book from list view
    @Override
    public void displayBook(Book book) {
        if (checkTablet() || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { //Checks to see if in landscape mode/tablet or portrait
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.book_details_view);
            if (fragment != null && fragment.getView() != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.book_details_view, BookDetailsFragment.newInstance(book)).commit();
            }
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.layout, BookDetailsFragment.newInstance(book)).addToBackStack(null).commit();
        }
    }

    @Override
    public void selectedBook(Book book, int position) {
        displayBook(book);
        selectedBookPosition = position; //Position of book in list view
    }

    Handler booksHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            JSONArray jsonArray = (JSONArray) msg.obj;
            Book book;
            books = new ArrayList<>();
            try{                            // Adds contents of json array into an array list of books
                for(int i = 0; i < jsonArray.length(); i++){
                    book = new Book(jsonArray.getJSONObject(i).getInt("book_id"),
                            jsonArray.getJSONObject(i).getString("title"),
                            jsonArray.getJSONObject(i).getString("author"),
                            jsonArray.getJSONObject(i).getString("cover_url"));
                    books.add(book);
                }
                if (checkTablet() || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { //If in landscape
                    if(selectedBookPosition != -1) { //If a book has been selected from list view
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.book_list_view, BookListFragment.newInstance(books))
                                .replace(R.id.book_details_view, BookDetailsFragment.newInstance(books.get(selectedBookPosition)))//Shows the book currently selected
                                .commit();
                    } else { //If book has not been selected
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.book_list_view, BookListFragment.newInstance(books))
                                .replace(R.id.book_details_view, BookDetailsFragment.newInstance(startUpBook)) //Shows an empty book to avoid crash
                                .commit();
                    }


                } else {
                    if(selectedBookPosition == -1) { //If no book has been currently selected
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.layout, BookListFragment.newInstance(books))
                                .commit();
                    }else { //If a book has been selected
                        getSupportFragmentManager().beginTransaction() //Updates BookListFragment first
                                .replace(R.id.layout, BookListFragment.newInstance(books))
                                .commit();
                        getSupportFragmentManager().beginTransaction() //Then displays the current book in BookDetailsFragment
                                .replace(R.id.layout, BookDetailsFragment.newInstance(books.get(selectedBookPosition))).addToBackStack(null)
                                .commit();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            selectedBookPosition = -1; //Returns the selected book position to not selected
            return false;
        }
    });

    public Book createBook(String title, String author){
        Book book = new Book(0, title, author, "url");
        return book;
    }


}


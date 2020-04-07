package edu.temple.bookcase;

import android.content.res.Configuration;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentListener, BookDetailsFragment.BookDetailsFragmentListener {
    private ViewPager vp;
    ArrayList<HashMap<Integer, String>> newBooks = new ArrayList<>();
    ArrayList<Book> books;
    ArrayList<Book> test = new ArrayList<Book>();
    private PagerAdapter pa;
    private boolean isPortrait;
    EditText bookSearch;
    URL website;
    JSONArray jsonArray = new JSONArray();
    Message msg;
    Book selected;
    private static int x = -1;
    boolean orientationChange;
    Book startUpBook = new Book(0, "", "", "");
    private static int u = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle info = new Bundle();
        setContentView(R.layout.activity_main);
        bookSearch = findViewById(R.id.bookSearch);
        boolean startUp = true;
        AtomicInteger num = new AtomicInteger(0 );
        findViewById(R.id.button).setOnClickListener(v -> {
            u = 1;
            num.getAndIncrement();
            info.putInt("startUp", num.get());
            Log.d("raz", "onCreate: " + (u));
            new Thread() {
                @Override
                public void run() { ;
                    try {
                        Log.d("hello", "onCreate: 2 ");
                        if (bookSearch.getText().toString().isEmpty())
                            website = new URL("https://kamorris.com/lab/abp/booksearch.php");
                        else
                            website = new URL("https://kamorris.com/lab/abp/booksearch.php?search=" + bookSearch.getText().toString());
                        BufferedReader reader = new BufferedReader(new InputStreamReader(website.openStream()));
                        String jL = reader.readLine();
                        //Log.d("hello", jL);
                        jsonArray = new JSONArray(jL);
                        msg = Message.obtain();
                        //savedInstanceState.putString("Search", bookSearch.getText().toString());
                        msg.obj = jsonArray;
                        booksHandler.sendMessage(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        });



        int h = info.getInt("startUp");
        Log.d("raz", String.valueOf(u));


        if (checkTablet() || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && u == 0) {
            startUp = false;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.book_details_view, BookDetailsFragment.newInstanced(startUpBook)).addToBackStack(null)
                    .commit();
        } else if(savedInstanceState != null && u != 0){
            orientationChange = true;
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
            //displayBook(selected);
        }




   /*     Log.d("hello", "It got here 11");
        test.add(createBook("book1", "author1"));
        test.add(createBook("book2", "author2"));
        //books = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.books)));
        if (checkTablet() || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("hello", "It got here 1");
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.book_list_view, BookListFragment.newInstanced(test))
                    .replace(R.id.book_details_view, BookDetailsFragment.newInstanced(test.get(0)))
                    .commit();
        } else {
            isPortrait = true;
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.layout, BookListFragment.newInstanced(test))
                    .commit();
        }

    */
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
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

    //Method that will display chosen book in a BookDetailsFragment
    @Override
    public void displayBook(Book book) {
        if (checkTablet() || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) { //Checks to see if in landscape mode/tablet or portrait
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.book_details_view);
            if (fragment != null && fragment.getView() != null) {
                getSupportFragmentManager().beginTransaction().replace(R.id.book_details_view, BookDetailsFragment.newInstanced(book)).commit();

            /*    TextView tv = fragment.getView().findViewById(R.id.book_title);
                TextView tv1 = fragment.getView().findViewById(R.id.book_author);
                Log.d("hello", String.valueOf(tv1.getLeft()) + "    " + tv1.getTop());
                tv.setText(title);
                tv1.setText(author);

                */

            }
        } else{
            getSupportFragmentManager().beginTransaction().replace(R.id.layout, BookDetailsFragment.newInstanced(book)).addToBackStack(null).commit();

        }
    }

    @Override
    public void selectedBook(Book book, int position, ArrayList<Book> books) {
        displayBook(book);
        test = books;
        x = position;
        Log.d("raz", test.toString());
    }

    Handler booksHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            JSONArray jsonArray = (JSONArray) msg.obj;
            Book book;
            books = new ArrayList<>();
            try{
                for(int i = 0; i < jsonArray.length(); i++){
                    book = new Book(jsonArray.getJSONObject(i).getInt("book_id"),
                            jsonArray.getJSONObject(i).getString("title"),
                            jsonArray.getJSONObject(i).getString("author"),
                            jsonArray.getJSONObject(i).getString("cover_url"));
                    books.add(book);
                }
                if (checkTablet() || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if(x != -1) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.book_list_view, BookListFragment.newInstanced(books))
                                .replace(R.id.book_details_view, BookDetailsFragment.newInstanced(books.get(x))).addToBackStack(null)
                                .commit();
                    } else {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.book_list_view, BookListFragment.newInstanced(books))
                                .replace(R.id.book_details_view, BookDetailsFragment.newInstanced(startUpBook)).addToBackStack(null)
                                .commit();
                    }


                } else {
                    if(x == -1) {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.layout, BookListFragment.newInstanced(books))
                                .commit();
                    }else {
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.layout, BookDetailsFragment.newInstanced(books.get(x))).addToBackStack(null)
                                .commit();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            x = -1;
            return false;
        }
    });

    public Book createBook(String title, String author){
        Book book = new Book(0, title, author, "url");
        return book;
    }


}


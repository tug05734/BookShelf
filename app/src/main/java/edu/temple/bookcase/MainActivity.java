package edu.temple.bookcase;

import android.content.Intent;
import android.content.res.Configuration;
import android.service.autofill.TextValueSanitizer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements BookListFragment.BookListFragmentListener, BookDetailsFragment.BookDetailsFragmentListener {
    private ViewPager vp;
    ArrayList<HashMap<Integer, String>> newBooks = new ArrayList<>();
    ArrayList<String> books;
    ArrayList<Book> test = new ArrayList<Book>();
    private PagerAdapter pa;
    private boolean isPortrait;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("hello", "It got here 11");
        newBooks.add(createHashMap("Red Book","Diego Forlan"));
        newBooks.add(createHashMap("Blue Book", "Messi"));
        newBooks.add(createHashMap("Green Book","Ronaldo"));
        newBooks.add(createHashMap("Yellow Book", "Wesley Sneijder"));
        newBooks.add(createHashMap("Black Book","Chase Utley"));
        newBooks.add(createHashMap("Pink Book", "Cliff Lee"));
        newBooks.add(createHashMap("White Book","Ryan Howard"));
        newBooks.add(createHashMap("Brown Book", "Carson Wentz"));
        newBooks.add(createHashMap("Teal Book","Michael Vick"));
        newBooks.add(createHashMap("Neon Book", "Nick Foles"));
        test.add(createBook("book1", "author1"));
        test.add(createBook("book2", "author2"));
        books = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.books)));
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
            Log.d("hello", "got here");
            getSupportFragmentManager().beginTransaction().replace(R.id.layout, BookDetailsFragment.newInstanced(book)).addToBackStack(null).commit();
        }
    }

    @Override
    public void selectedBook(Book book) {
        displayBook(book);
    }

    private class SliderAdapter extends FragmentStatePagerAdapter {
        ArrayList<ViewPagerFragment> fragments;

        public SliderAdapter(FragmentManager fm, ArrayList<ViewPagerFragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return newBooks.size();
        }
    }

    //Method to create hashmap for book details
    public HashMap createHashMap(String title, String author){
        HashMap<Integer, String> hm = new HashMap<>();
        hm.put(1, title);
        hm.put(2, author);
        return hm;
    }

    public Book createBook(String title, String author){
        Book book = new Book(0, title, author, "url");
        return book;
    }
}


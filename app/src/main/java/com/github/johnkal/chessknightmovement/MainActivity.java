package com.github.johnkal.chessknightmovement;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private PixelGridView pixelGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pixelGrid = new PixelGridView(this);
        pixelGrid.setNumColumns(6);
        pixelGrid.setNumRows(6);

        setContentView(pixelGrid);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset: // reset the chess board.
                pixelGrid.resetChessBoard();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}

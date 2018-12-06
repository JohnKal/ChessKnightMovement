package com.github.johnkal.chessknightmovement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Class that is responsible for drawing the chess board.
 */
public class PixelGridView extends View {
    private Context context;
    private int numColumns, numRows;
    private int cellWidth, cellHeight, cellHalfWidth, cellHalfHeight;
    public int outOfSpace = 0;
    private Paint blackPaint = new Paint();
    private Paint deepGreenPaint = new Paint();
    private Paint textBlackPaint = new Paint();
    private List<Paint> colors = new ArrayList<>();
    private List<Paint> textColors = new ArrayList<>();
    private boolean[][] cellChecked;
    private List<Cell> selectedCells = new ArrayList<>();
    private LinkedList<LinkedList<KnightMovement.Cell>> paths = new LinkedList<>();
    private LinkedList<LinkedList<KnightMovement.Cell>> fullPaths = new LinkedList<>();
    private boolean startDrawingLines = false;

    public PixelGridView(Context context) {
        this(context, null);
        this.context = context;
    }

    public PixelGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //create colors for lines.
        initializePaintColors();
        initializeTextPaints();
    }

    private void initializePaintColors() {
        blackPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        blackPaint.setColor(getResources().getColor(android.R.color.black));

        deepGreenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        deepGreenPaint.setColor(getResources().getColor(R.color.colorPrimary));
        deepGreenPaint.setStrokeWidth(10f);

        Paint redPaint = new Paint();
        redPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        redPaint.setColor(getResources().getColor(R.color.colorAccent));
        redPaint.setStrokeWidth(10f);
        colors.add(redPaint);

        Paint bluePaint = new Paint();
        bluePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bluePaint.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        bluePaint.setStrokeWidth(10f);
        colors.add(bluePaint);

        Paint greenPaint = new Paint();
        greenPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        greenPaint.setColor(getResources().getColor(android.R.color.holo_green_dark));
        greenPaint.setStrokeWidth(10f);
        colors.add(greenPaint);

        Paint orangePaint = new Paint();
        orangePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        orangePaint.setColor(getResources().getColor(R.color.orange));
        orangePaint.setStrokeWidth(10f);
        colors.add(orangePaint);

        Paint darkeRedPaint = new Paint();
        darkeRedPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        darkeRedPaint.setColor(getResources().getColor(R.color.darker_ruby));
        darkeRedPaint.setStrokeWidth(10f);
        colors.add(darkeRedPaint);

        Paint yellowPaint = new Paint();
        yellowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        yellowPaint.setColor(getResources().getColor(R.color.yellow));
        yellowPaint.setStrokeWidth(10f);
        colors.add(yellowPaint);

        Paint brownPaint = new Paint();
        brownPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        brownPaint.setColor(getResources().getColor(R.color.saddlebrown));
        brownPaint.setStrokeWidth(10f);
        colors.add(brownPaint);

        Paint greyPaint = new Paint();
        greyPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        greyPaint.setColor(getResources().getColor(R.color.grey));
        greyPaint.setStrokeWidth(10f);
        colors.add(greyPaint);

        Paint beigePaint = new Paint();
        beigePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        beigePaint.setColor(getResources().getColor(R.color.tirkouaz));
        beigePaint.setStrokeWidth(10f);
        colors.add(beigePaint);

        Paint purplePaint = new Paint();
        purplePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        purplePaint.setColor(getResources().getColor(R.color.deep_purple));
        purplePaint.setStrokeWidth(10f);
        colors.add(purplePaint);
    }

    private void initializeTextPaints() {
        textBlackPaint.setColor(Color.BLACK);
        textBlackPaint.setTextSize(60);

        Paint textRedPaint = new Paint();
        textRedPaint.setColor(getResources().getColor(R.color.colorAccent));
        textRedPaint.setTextSize(60);
        textColors.add(textRedPaint);

        Paint textBluePaint = new Paint();
        textBluePaint.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        textBluePaint.setTextSize(60);
        textColors.add(textBluePaint);

        Paint textGreenPaint = new Paint();
        textGreenPaint.setColor(getResources().getColor(android.R.color.holo_green_dark));
        textGreenPaint.setTextSize(60);
        textColors.add(textGreenPaint);

        Paint textOrangePaint = new Paint();
        textOrangePaint.setColor(getResources().getColor(R.color.orange));
        textOrangePaint.setTextSize(60);
        textColors.add(textOrangePaint);

        Paint textDarkRedPaint = new Paint();
        textDarkRedPaint.setColor(getResources().getColor(R.color.darker_ruby));
        textDarkRedPaint.setTextSize(60);
        textColors.add(textDarkRedPaint);

        Paint textYellowPaint = new Paint();
        textYellowPaint.setColor(getResources().getColor(R.color.yellow));
        textYellowPaint.setTextSize(60);
        textColors.add(textYellowPaint);

        Paint textBrownPaint = new Paint();
        textBrownPaint.setColor(getResources().getColor(R.color.saddlebrown));
        textBrownPaint.setTextSize(60);
        textColors.add(textBrownPaint);

        Paint textGreyPaint = new Paint();
        textGreyPaint.setColor(getResources().getColor(R.color.grey));
        textGreyPaint.setTextSize(60);
        textColors.add(textGreyPaint);

        Paint textBeigePaint = new Paint();
        textBeigePaint.setColor(getResources().getColor(R.color.tirkouaz));
        textBeigePaint.setTextSize(60);
        textColors.add(textBeigePaint);

        Paint textPurplePaint = new Paint();
        textPurplePaint.setColor(getResources().getColor(R.color.deep_purple));
        textPurplePaint.setTextSize(60);
        textColors.add(textPurplePaint);
    }

    public void setNumColumns(int numColumns) {
        this.numColumns = numColumns;
        calculateDimensions();
    }

    public int getNumColumns() {
        return numColumns;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
        calculateDimensions();
    }

    public int getNumRows() {
        return numRows;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        calculateDimensions();
    }

    /**
     * calculate width and hight for rows and columns.
     */
    private void calculateDimensions() {
        if (numColumns < 1 || numRows < 1) {
            return;
        }

        cellWidth = getWidth() / numColumns;
        cellHeight = cellWidth; // Height must be equal to width for square rectangles.
        //cellHeight = getHeight() / numRows;

        cellHalfWidth = cellWidth / 2;
        cellHalfHeight = cellHeight / 2;

        cellChecked = new boolean[numColumns][numRows];

        invalidate();
    }

    /**
     * This method is responsible for drawing the chess board.
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        outOfSpace = 0;

        if (numColumns == 0 || numRows == 0) {
            return;
        }

        int width = getWidth();
        int height = width; //Height must be equal to width for square rectangles.
        int fullHeight = getHeight();

        //Draw ChessBoard colors.
        Paint lightGreyPaint = new Paint();
        lightGreyPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        lightGreyPaint.setColor(getResources().getColor(R.color.light_grey));
        lightGreyPaint.setStrokeWidth(10f);

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 1) {
                        canvas.drawRect(j * cellWidth, i * cellHeight,
                                (j + 1) * cellWidth, (i + 1) * cellHeight,
                                lightGreyPaint);
                    }
                }
                else {
                    if (j % 2 == 0) {
                        canvas.drawRect(j * cellWidth, i * cellHeight,
                                (j + 1) * cellWidth, (i + 1) * cellHeight,
                                lightGreyPaint);
                    }
                }
            }
        }

        //Draw numbers and letters on ChessBoard.
        Paint textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(60);

        String[] letters = new String[] {"a", "b", "c", "d", "e", "f"};
        int[] numbers = new int[] {6, 5, 4, 3, 2, 1};

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (i == 0) {
                    canvas.drawText(letters[j], (j + 1) * cellWidth - cellHalfWidth / 2,
                            cellHeight - cellHalfHeight - 30, textPaint);
                }
                if (j == 0) {
                    canvas.drawText(String.valueOf(numbers[i]), 0,
                            (i + 1) * cellHeight, textPaint);
                }
            }
        }

        //Draw with green the selected rectangles.
        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                if (cellChecked[i][j]) {
                    canvas.drawRect(i * cellWidth, j * cellHeight,
                            (i + 1) * cellWidth, (j + 1) * cellHeight,
                            deepGreenPaint);
                }
            }
        }

        //Draw the bitmap on selected rectangles.
        for (Cell cell : selectedCells) {
            int i = cell.y;
            int j = cell.x;

            if (cell.firstMove) {
                Rect rectangle = new Rect();
                rectangle.set(i * cellWidth, j * cellHeight + cellHalfHeight / 4,
                        (i + 1) * cellWidth, (j + 1) * cellHeight - cellHalfHeight / 4);

                Drawable knight = getResources().getDrawable(R.drawable.ic_knight);

                canvas.drawBitmap(drawableToBitmap(knight), new Rect(0, 0, 100, 100), rectangle, deepGreenPaint);
            } else {
                Rect rectangle = new Rect();
                rectangle.set(i * cellWidth + cellHalfWidth / 4, j * cellHeight + cellHalfHeight / 2,
                        (i + 1) * cellWidth - cellHalfWidth / 4, (j + 1) * cellHeight - cellHalfHeight / 2);

                Drawable finishLine = getResources().getDrawable(R.drawable.ic_finish_line);

                canvas.drawBitmap(drawableToBitmap(finishLine), new Rect(0, 0, 100, 100), rectangle, deepGreenPaint);

                if (!startDrawingLines) {
                    startMovesCalculation();
                }
            }
        }

        //Draw vertical lines for columns.
        for (int i = 1; i < numColumns; i++) {
            canvas.drawLine(i * cellWidth, 0, i * cellWidth, height, blackPaint);
        }

        //Draw horizontal lines for rows.
        for (int i = 1; i <= numRows; i++) {
            canvas.drawLine(0, i * cellHeight, width, i * cellHeight, blackPaint);
        }

        int countColor = 0;

        //Draw the calculated paths for start position to end position.
        for (LinkedList<KnightMovement.Cell> path : fullPaths) {
            for (int i = 0; i < path.size() - 1; i++) {
                KnightMovement.Cell firstCell = path.get(i);
                KnightMovement.Cell secondCell = path.get(i + 1);
                canvas.drawLine((firstCell.y + 1) * cellWidth - cellHalfWidth, (firstCell.x + 1) *
                                cellHeight - cellHalfHeight, (secondCell.y + 1) * cellWidth - cellHalfWidth,
                        (secondCell.x + 1) * cellHeight - cellHalfHeight, colors.get(countColor));
            }
            if (countColor >= colors.size() - 1) {
                countColor = -1;
            }
            countColor++;
        }

        int yPosition = 80;
        int xPosition = 0;
        countColor = 0;
        if (startDrawingLines) {
            canvas.drawText("Algebraic notations", 0, height + yPosition, textPaint);
        }
        //Draw Algebraic notations about calculated paths.
        for (LinkedList<KnightMovement.Cell> path : paths) {
            String position = "";
            yPosition += 100;
            for (int i = 0; i < path.size(); i++) {
                KnightMovement.Cell cell = path.get(i);
                position += String.valueOf(letters[cell.y]) + String.valueOf(numbers[cell.x]) +
                        String.valueOf((i == path.size() - 1) ? " " : " - ");
            }
            if (countColor >= textColors.size() - 1) {
                countColor = 0;
            }
            if (height + yPosition >= getHeight()) {
                outOfSpace++;
                if (outOfSpace > 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("It's not enough space to write all the Algebraic notations.");
                    builder.setPositiveButton("OK",
                            (dialog, which) -> { });
                    builder.setCancelable(false);
                    builder.show();

                    return;
                }
                xPosition = getWidth() / 2;
                yPosition = 80;
            }
            canvas.drawText(position, xPosition, height + yPosition, textColors.get(countColor));
            countColor++;
        }

        int leftButton = width - 3 * width / 4;
        int topButton = fullHeight - dpToPx(100);
        int rightButton = width - width / 4;
        int bottomButton = fullHeight - dpToPx(50);

        Paint textWhitePaint = new Paint();
        textWhitePaint.setColor(Color.WHITE);
        textWhitePaint.setTextSize(60);

        //Draw button to start paths calculation.
        if (!startDrawingLines) {
            canvas.drawRect(leftButton, topButton, rightButton, bottomButton, deepGreenPaint);

            canvas.drawText("Paths calculation", leftButton + dpToPx(10), fullHeight - dpToPx(150 / 2 - 5), textWhitePaint);
        }
    }

    private void startMovesCalculation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Paths Calculation");
        builder.setMessage("Do you want to start the paths calculation?");
        builder.setPositiveButton("OK",
                (dialog, which) -> {
                    startDrawingLines = true;
                    startDrawLines();
                });
        builder.setNegativeButton("CANCEL", (dialog, which) -> { });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * Calls the approprietes methods for KnightMovement.java
     */
    private void startDrawLines() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Paths Calculation.");
        builder.setView(R.layout.loading_dialog);
        builder.setCancelable(false);

        AlertDialog loadingDialog =  builder.create();
        loadingDialog.show();

        int[] startPosition = {selectedCells.get(0).x, selectedCells.get(0).y};
        int[] endPosition = {selectedCells.get(1).x, selectedCells.get(1).y};
        KnightMovement knightMovement = new KnightMovement(startPosition, endPosition, 6);

        Observable.fromCallable(() -> {
            return fullPaths = knightMovement.calculateMoves();
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((fullPaths) -> {
                    paths = knightMovement.getPaths();
                    if (loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    if (fullPaths.size() == 0) {
                        AlertDialog.Builder builder2 = new AlertDialog.Builder(context);
                        builder2.setMessage("No solution has been found");
                        builder2.setPositiveButton("OK",
                                (dialog, which) -> { });
                        builder2.setCancelable(false);
                        builder2.show();
                    }
                    else {
                        invalidate();
                    }
                });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int width = getWidth();
            int height = width;
            int fullHeight = getHeight();

            int leftButton = width - 3 * width / 4;
            int topButton = fullHeight - dpToPx(100);
            int rightButton = width - width / 4;
            int bottomButton = fullHeight - dpToPx(50);

            //Touch event for button
            if (event.getX() >= leftButton && event.getX() <= rightButton && event.getY() <=
                    bottomButton && event.getY() >= topButton) {
                if (selectedCells.size() == 2) {
                    startDrawingLines = true;
                    startDrawLines();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Wrong move");
                    builder.setMessage("You haven't select two cells for start and end position.");
                    builder.setPositiveButton("OK",
                            (dialog, which) -> { });
                    builder.setCancelable(false);
                    builder.show();

                    return false;
                }
            }

            // if touch is outside of board or button then return;
            if (event.getY() > height)
                return false;

            //calculates in which row and column this touch event happens.
            int column = (int)(event.getX() / cellWidth);
            int row = (int)(event.getY() / cellHeight);

            if (startDrawingLines) {
                return true;
            }

            //check if selected cell is more than 2.
            if (selectedCells.size() >= 2) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Wrong move");
                builder.setMessage("You can't select more than two cells.");
                builder.setPositiveButton("OK",
                        (dialog, which) -> { });
                builder.setCancelable(false);
                builder.show();

                return true;
            }

            //Check if this cell is already selected.
            if (!checkIfCellIsSelected(column, row)) {
                cellChecked[column][row] = !cellChecked[column][row];
                selectedCells.add(new Cell(row, column, selectedCells.size() == 0 ? true : false));
                invalidate();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Wrong move");
                builder.setMessage("This cell has already been selected.");
                builder.setPositiveButton("OK",
                        (dialog, which) -> { });
                builder.setCancelable(false);
                builder.show();
            }
        }

        return true;
    }

    private boolean checkIfCellIsSelected(int column, int row) {
        for (Cell cell : selectedCells) {
            if (cell.x == row && cell.y == column) {
                return true;
            }
        }
        return false;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public void resetChessBoard() {
        selectedCells.clear();
        paths.clear();
        fullPaths.clear();
        startDrawingLines = false;
        outOfSpace = 0;

        for (int i = 0; i < numColumns; i++) {
            for (int j = 0; j < numRows; j++) {
                cellChecked[i][j] = false;
            }
        }

        invalidate();
    }

    /**
     * convert dp to pixels to be consistent across the different screen densities.
     * @param dp
     * @return
     */
    public int dpToPx(int dp) {
        float density = getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }
}
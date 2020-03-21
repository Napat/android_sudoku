package com.github.napat.sudoku.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.github.napat.sudoku.R;
import com.github.napat.sudoku.dialog.SudokuKeypadDialog;
import com.github.napat.sudoku.util.BundleSavedState;
import com.github.napat.sudoku.util.TypeConverter;

import java.util.Arrays;

public class SudokuView extends View {
    protected static final String KEY_DIFFICULTY = "difficulty";
    public static final int DIFFICULTY_CONTINUE = 0;
    public static final int DIFFICULTY_EASY = 1;
    public static final int DIFFICULTY_MEDIUM = 2;
    public static final int DIFFICULTY_HARD = 3;

    private final String easyPuzzle =
            "300000056400000000000897000" +
                    "002080400006103800005070300" +
                    "000462000000000008510000007";

    private final String mediumPuzzle =
            "000070000080000273009036800" +
                    "001247000807050309000389400" +
                    "003510700124000090000020000";

    private final String hardPuzzle =
            "462900000900100000705800000" +
                    "631000000000000000000000259" +
                    "000005901000008003000001542";

    private int puzzle[] = new int[9 * 9];
    private int puzzleConst[] = new int[9 * 9];
    private final int used[][][] = new int[9][9][0];
    private float width;
    private float height;
    private int selRow;
    private int selCol;
    private final Rect selRect = new Rect();
    private int difficulty;
    protected static final String TAG = "Sudoku";

    private boolean isWinner = false;
    private boolean enableHint = true;

    public SudokuView(Context context) {
        super(context);
        init();
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public SudokuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {

        Log.d(TAG, "Print my id: " + this.getId());
        //setSaveEnabled(true);
    }

    public boolean isWinner() {
        return isWinner;
    }

    private void setWinner(boolean winner) {
        isWinner = winner;
    }

    public void setDifficulty(int difficulty) {
        if (difficulty > DIFFICULTY_HARD) {
            this.difficulty = DIFFICULTY_EASY;
        } else if (difficulty < DIFFICULTY_CONTINUE) {
            this.difficulty = DIFFICULTY_EASY;
        } else if (difficulty == DIFFICULTY_CONTINUE) {
            // TODO: DIFFICULTY_CONTINUE
            // - check shared preference by view id name is available
            // - load data to vars
            if (loadCache() == true) {
                calculateUsedTiles();
                return;
            }

            // Something wrong maybe no cache, set to easy mode
            this.difficulty = DIFFICULTY_EASY;

        } else {
            this.difficulty = difficulty;
        }

        //Log.d("xxx", " this..." + this.difficulty);
        puzzle = getSudokuPuzzle(this.difficulty);
        puzzleConst = puzzle.clone();
        calculateUsedTiles();
    }

    private boolean isPuzzleEditable(int row, int col) {
        if (puzzleConst[col * 9 + row] == 0) {
            //Log.d(TAG, "isPuzzleEditable true");
            return true;
        }
        //Log.d(TAG, "isPuzzleEditable false " + puzzleConst[col * 9 + row]);
        return false;
    }

    private int[] getSudokuPuzzle(int difficulty) {
        Log.d(TAG, "SudokuView...getPuzzle level " + difficulty);

        String puz;
        switch (difficulty) {
            case DIFFICULTY_HARD:
                puz = hardPuzzle;
                break;
            case DIFFICULTY_MEDIUM:
                puz = mediumPuzzle;
                break;
            case DIFFICULTY_EASY:
            default:
                puz = easyPuzzle;
        }
        return fromPuzzleString(puz);
    }

    static protected int[] fromPuzzleString(String str) {
        int[] puz = new int[str.length()];
        for (int idx = 0; idx < puz.length; idx++) {
            puz[idx] = str.charAt(idx) - '0';
        }
        return puz;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w / 9f;
        height = h / 9f;
        getRect(selRow, selCol, selRect);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    private void getRect(int row, int col, Rect selRect) {
        selRect.set((int) (row * width), (int) (col * height),
                (int) (row * width + width), (int) (col * height + height)
        );
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "SudokuView...onDraw...");

        int dpLineSize = 3;
        float strokeWidthPixel = dpToPixel(dpLineSize);

        Paint paint = new Paint();
        paint.setColor(ContextCompat.getColor(getContext(), R.color.sudokuBackground)); // paint.setColor(getResources().getColor(R.color.sudokuBackground));
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

        // Draw table line
        Paint dark = new Paint();
        dark.setStrokeWidth(strokeWidthPixel);
        dark.setColor(ContextCompat.getColor(getContext(), R.color.sudokuDark));

        Paint highlight = new Paint();
        highlight.setColor(ContextCompat.getColor(getContext(), R.color.sudokuHighlight));

        Paint light = new Paint();
        light.setStrokeWidth(strokeWidthPixel);
        light.setColor(ContextCompat.getColor(getContext(), R.color.sudokuLight));

        // Draw sub-table line
        for (int row = 0; row < 9; row++) {
            canvas.drawLine(0, row * height, getMeasuredWidth(), row * height, light);
            canvas.drawLine(0, row * height + 1, getMeasuredWidth(), row * height + 1, highlight);
            canvas.drawLine(row * width, 0, row * width, getMeasuredHeight(), light);
            canvas.drawLine(row * width + 1, 0, row * width + 1, getMeasuredHeight(), highlight);
        }

        // Draw main-table line
        for (int row = 0; row < 9; row++) {
            if (row % 3 != 0)
                continue;
            canvas.drawLine(0, row * height, getMeasuredWidth(), row * height, dark);
            canvas.drawLine(0, row * height + 1, getMeasuredWidth(), row * height + 1, highlight);
            canvas.drawLine(row * width, 0, row * width, getMeasuredHeight(), dark);
            canvas.drawLine(row * width + 1, 0, row * width + 1, getMeasuredHeight(), highlight);
        }

        // Draw Number
        Paint paintFont = new Paint(Paint.ANTI_ALIAS_FLAG);
        //paintFont.setColor(ContextCompat.getColor(getContext(), R.color.sudokuForeground));
        paintFont.setStyle(Paint.Style.FILL);
        paintFont.setTextSize(height * 0.75f);
        paintFont.setTextScaleX(width / height);
        paintFont.setTextAlign(Paint.Align.CENTER);

        Paint.FontMetrics fontMetrics = paintFont.getFontMetrics();
        float x = width / 2;
        float y = height / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2;
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String value = getTileString(row, col);

                if (isPuzzleEditable(row, col)) {
                    paintFont.setColor(ContextCompat.getColor(getContext(), R.color.sudokuFontInputNumber));
                } else {
                    paintFont.setColor(ContextCompat.getColor(getContext(), R.color.sudokuForeground));
                }
                canvas.drawText(value, row * width + x, col * height + y, paintFont);
            }
        }

        if (isEnableHint() == true) {
            // Draw Hint for almost complete slot
            int c[] = {ContextCompat.getColor(getContext(), R.color.sudokuHint0),
                    ContextCompat.getColor(getContext(), R.color.sudokuHint1),
                    ContextCompat.getColor(getContext(), R.color.sudokuHint2)
            };

            for (int row = 0; row < 9; row++) {
                for (int col = 0; col < 9; col++) {
                    int numLeft = 9 - getUsedTiles(row, col).length;
                    if (numLeft < c.length) {
                        Rect rect = new Rect();
                        getRect(row, col, rect);

                        Paint hint = new Paint();
                        hint.setColor(c[numLeft]);
                        //Log.d(TAG + "Hint", "numLeft: " + numLeft + " c[numLeft]: " + c[numLeft] + "=" + String.format("0x%08X", c[numLeft]));
                        canvas.drawRect(rect, hint);
                    } else {
                        //Log.d(TAG + "Hint", "row: " + row + " col: " + col + " numLeft: " + numLeft + " c.length: " + c.length);
                    }
                }
            }
        }

        // Drawer Cursor
        Paint selected = new Paint();
        selected.setColor(ContextCompat.getColor(getContext(), R.color.sudokuSelected));
        canvas.drawRect(selRect, selected);
    }

    private float dpToPixel(int dpSize) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, dm);
    }

    private int getTile(int row, int col) {
        return puzzle[col * 9 + row];
    }

    private String getTileString(int row, int col) {
        int value = getTile(row, col);

        // Log.d("Sudoku", "SudokuView...getTileString..." + x + " " + y);

        if (value == 0)
            return "";
        return String.valueOf(value);
    }

    private boolean setTile(int row, int col, int value) {
        if ((row >= 9) || (col >= 9)) {
            return false;
        }
        puzzle[col * 9 + row] = value;
        calculateUsedTiles();
        return true;
    }

    protected boolean setTileIfValid(int row, int col, int tile) {
        int useds[] = getUsedTiles(row, col);
        if (tile != 0) {
            for (int used : useds) {
                if (used == tile)
                    return false;
            }
        }
        setTile(row, col, tile);
        return true;
    }

    protected int[] getUsedTiles(int row, int col) {
        return used[row][col];
    }

    private void calculateUsedTiles() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                used[row][col] = calculateUsedTiles(row, col);
            }
        }
    }

    private int[] calculateUsedTiles(int row, int col) {
        int c[] = new int[9];

        // collect already use number in same row
        for (int i = 0; i < 9; i++) {
            if (i == col)     // skip current position
                continue;
            int t = getTile(row, i);
            if (t == 0)     // skip if space(number 0)
                continue;
            c[t - 1] = t;
        }

        // collect already use number in same column
        for (int i = 0; i < 9; i++) {
            if (i == row)     // skip current position
                continue;
            int t = getTile(i, col);
            if (t == 0)     // skip if space(number 0)
                continue;

            c[t - 1] = t;
        }

        // collect use number in same 3x3 area
        int startX = (row / 3) * 3;
        int startY = (col / 3) * 3;
        for (int i = startX; i < startX + 3; i++) {
            for (int j = startY; j < startY + 3; j++) {
                if (i == row && j == col)   // skip current position
                    continue;

                int t = getTile(i, j);
                if (t == 0)             // skip if space(number 0)
                    continue;

                c[t - 1] = t;
            }
        }

        // convert c to c1 (return array)
        int numUsed = 0;
        for (int t : c) {
            if (t != 0)
                numUsed++;
        }
        int c1[] = new int[numUsed];
        numUsed = 0;
        //Log.d(TAG, "calculateUsedTiles..." + row + " " + col);
        for (int t : c) {
            if (t == 0)     // skip if space(number 0)
                continue;
            c1[numUsed++] = t;
            //Log.d(TAG, "\t " + t);
        }

        return c1;
    }

    public void setSelectTile(int num) {
        if (setTileIfValid(selRow, selCol, num)) {
            invalidate();           // tell android that ui space is dirty and need to onDraw()
        }
        winnerChecker();
    }

    private void selectTile(int row, int col) {
        invalidate(selRect);            // tell android this ui space is dirty and need to onDraw()
        selRow = Math.min(Math.max(row, 0), 8);
        selCol = Math.min(Math.max(col, 0), 8);
        getRect(selRow, selCol, selRect);
        invalidate(selRect);            // tell android this ui space is dirty and need to onDraw()
    }

    // D-pad hardware keyboard support function
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                selectTile(selRow, selCol - 1);
                break;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                selectTile(selRow, selCol + 1);
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                selectTile(selRow - 1, selCol);
                break;

            case KeyEvent.KEYCODE_DPAD_RIGHT:
                selectTile(selRow + 1, selCol);
                break;

            case KeyEvent.KEYCODE_0:
            case KeyEvent.KEYCODE_SPACE:
                setSelectTile(0);
                break;

            case KeyEvent.KEYCODE_1:
                setSelectTile(1);
                break;

            case KeyEvent.KEYCODE_2:
                setSelectTile(2);
                break;

            case KeyEvent.KEYCODE_3:
                setSelectTile(3);
                break;

            case KeyEvent.KEYCODE_4:
                setSelectTile(4);
                break;

            case KeyEvent.KEYCODE_5:
                setSelectTile(5);
                break;

            case KeyEvent.KEYCODE_6:
                setSelectTile(6);
                break;

            case KeyEvent.KEYCODE_7:
                setSelectTile(7);
                break;

            case KeyEvent.KEYCODE_8:
                setSelectTile(8);
                break;

            case KeyEvent.KEYCODE_9:
                setSelectTile(9);
                break;

            default:
                return super.onKeyDown(keyCode, event);
        }

        return true;
    }

    protected void showKeypadDialogOrError(int row, int col) {
        int tiles[] = getUsedTiles(row, col);

        if (tiles.length == 9) {
            Toast toast = Toast.makeText(getContext(), "No number left", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } else {
            Dialog dialog = new SudokuKeypadDialog(getContext(), tiles, this);   // TODO: check argument
            dialog.show();
        }
    }

    private boolean winnerChecker() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (puzzle[col * 9 + row] == 0) {
                    //Log.d(TAG, "winnerChecker...false at " + row + "," + col + "=" + puzzle[col * 9 + row]);
                    return false;
                }
            }
        }
        //Log.d(TAG, "winnerChecker...true");
        winnerDialog();
        return true;
    }

    private void winnerDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle("WIN!");
        alertDialog.setMessage("You are Genius!!");
        //alertDialog.setIcon(R.drawable.ic_message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        setWinner(true);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return super.onTouchEvent(event);

        int row = (int) (event.getX() / width);
        int col = (int) (event.getY() / height);

        selectTile(row, col);

        if (isPuzzleEditable(row, col) == false) {
            return true;
        }

        showKeypadDialogOrError(selRow, selCol);
        return true;
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        BundleSavedState savedState = new BundleSavedState(superState);
        Bundle bundle = new Bundle();
        bundle.putIntArray("puzzle", puzzle);
        bundle.putIntArray("puzzleConst", puzzleConst);
        bundle.putInt("difficulty", difficulty);
        bundle.putInt("selRow", selRow);
        bundle.putInt("selCol", selCol);
        savedState.setBundle(bundle);

        // Save to persistence disk
        //saveCache();

        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState saveState = (BundleSavedState) state;
        super.onRestoreInstanceState(saveState.getSuperState());
        Bundle bundle = saveState.getBundle();
        puzzle = bundle.getIntArray("puzzle");
        puzzleConst = bundle.getIntArray("puzzleConst");
        difficulty = bundle.getInt("difficulty");
        selRow = bundle.getInt("selRow");
        selCol = bundle.getInt("selCol");

        calculateUsedTiles();

        Log.d(TAG, "SudokuView...onRestoreInstanceState..." + Arrays.toString(puzzle));
    }

    // https://proandroiddev.com/the-life-cycle-of-a-view-in-android-6a2c4665b95e
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        saveCache();
    }

    private String viewIdString() {
        int viewId = this.getId();
        if (viewId == View.NO_ID) {
            return null;
        }
        return getResources().getResourceEntryName(viewId);
    }

    public void saveCache() {
        String viewIdString = viewIdString();
        if (viewIdString == null) {
            // NO CACHE if the view has no ID
            return;
        }

        // Cache-filename & mode
        SharedPreferences prefs = getContext().getSharedPreferences(
                viewIdString,
                Context.MODE_PRIVATE
        );

        SharedPreferences.Editor editor = prefs.edit();
        // Add / Edit / Delete
        editor.putString("puzzle", TypeConverter.intArrayToStringSpace(puzzle));
        editor.putString("puzzleConst", TypeConverter.intArrayToStringSpace(puzzleConst));
        editor.putInt("difficulty", difficulty);
        editor.putInt("selRow", selRow);
        editor.putInt("selCol", selCol);
        editor.apply();
    }

    private boolean loadCache() {
        //Log.d(TAG, "loadCache............");
        String viewIdString = viewIdString();
        if (viewIdString == null) {
            // NO CACHE if the view has no ID
            Log.e(TAG, "loadCache error viewIdString is null");
            return false;
        }

        // Cache-filename & mode
        SharedPreferences prefs = getContext().getSharedPreferences(
                viewIdString,
                Context.MODE_PRIVATE
        );

        String puzzleStr = prefs.getString("puzzle", null);
        if (puzzleStr == null)
            return false;
        puzzle = TypeConverter.revertIntArrayToStringSpace(puzzleStr);

        String puzzleConstStr = prefs.getString("puzzleConst", null);
        if (puzzleConstStr == null)
            return false;
        puzzleConst = TypeConverter.revertIntArrayToStringSpace(puzzleConstStr);

        difficulty = prefs.getInt("difficulty", DIFFICULTY_EASY);
        selRow = prefs.getInt("selRow", 0);
        selCol = prefs.getInt("selCol", 0);

        return true;
    }

    public boolean isEnableHint() {
        return enableHint;
    }

    public void setEnableHint(boolean enableHint) {
        this.enableHint = enableHint;
    }
}

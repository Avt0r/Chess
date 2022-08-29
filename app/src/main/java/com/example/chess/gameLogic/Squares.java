package com.example.chess.gameLogic;

import android.annotation.SuppressLint;
import android.widget.ImageButton;

import com.example.chess.R;
import com.example.chess.activities.GameActivity;
import com.example.chess.gameLogic.Pieces.Piece;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public enum Squares {
    A1(0, R.id.A1), B1(1, R.id.B1), C1(2, R.id.C1), D1(3, R.id.D1),
    E1(4, R.id.E1), F1(5, R.id.F1), G1(6, R.id.G1), H1(7, R.id.H1),
    A2(8, R.id.A2), B2(9, R.id.B2), C2(10, R.id.C2), D2(11, R.id.D2),
    E2(12, R.id.E2), F2(13, R.id.F2), G2(14, R.id.G2), H2(15, R.id.H2),
    A3(16, R.id.A3), B3(17, R.id.B3), C3(18, R.id.C3), D3(19, R.id.D3),
    E3(20, R.id.E3), F3(21, R.id.F3), G3(22, R.id.G3), H3(23, R.id.H3),
    A4(24, R.id.A4), B4(25, R.id.B4), C4(26, R.id.C4), D4(27, R.id.D4),
    E4(28, R.id.E4), F4(29, R.id.F4), G4(30, R.id.G4), H4(31, R.id.H4),
    A5(32, R.id.A5), B5(33, R.id.B5), C5(34, R.id.C5), D5(35, R.id.D5),
    E5(36, R.id.E5), F5(37, R.id.F5), G5(38, R.id.G5), H5(39, R.id.H5),
    A6(40, R.id.A6), B6(41, R.id.B6), C6(42, R.id.C6), D6(43, R.id.D6),
    E6(44, R.id.E6), F6(45, R.id.F6), G6(46, R.id.G6), H6(47, R.id.H6),
    A7(48, R.id.A7), B7(49, R.id.B7), C7(50, R.id.C7), D7(51, R.id.D7),
    E7(52, R.id.E7), F7(53, R.id.F7), G7(54, R.id.G7), H7(55, R.id.H7),
    A8(56, R.id.A8), B8(57, R.id.B8), C8(58, R.id.C8), D8(59, R.id.D8),
    E8(60, R.id.E8), F8(61, R.id.F8), G8(62, R.id.G8), H8(63, R.id.H8);


    public final byte number;
    public final int id;
    public ImageButton image;
    public static Squares clicked;
    private static GameActivity activity;

    Squares(int number, int imageId) {
        this.number = (byte) number;
        id = imageId;
    }

    public static void setActivity(GameActivity a){
        activity = a;
        setImages();
    }

    public static void setImages() {
        for (Squares i : Squares.values()) {
            i.image = activity.findViewById(i.id);
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void changeSquareToSelect() {
        image.setForeground(activity.getDrawable(R.color.selected));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void changeSquareToAttack() {
        image.setForeground(activity.getDrawable(R.color.attacking));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void changeSquareToMove() {
        image.setForeground(activity.getDrawable(R.color.moving));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void changeSquareToDeselect() {
        image.setForeground(activity.getDrawable(R.color.transparent));
    }

    public static void listenClick() {
        Board board = activity.game.board;
        for (Squares i : Squares.values()) {
            i.image.setOnClickListener(v -> {
                for (Squares s : Squares.values()) {
                    s.changeSquareToDeselect();
                }
                clicked = i;
                Piece p = board.getPiece(i);
                if (p != null && p.color == activity.game.whoseMove()) {
                    i.changeSquareToSelect();
                    for (Squares j : board.getPaths(p)) {
                        if (board.canMove(p, j)) {
                            j.changeSquareToMove();
                        }
                        if(board.canAttack(p,j) && board.getPiece(j)!=null){
                            j.changeSquareToAttack();
                        }
                    }
                }
            });
        }
    }

    public static void updateImages() {
        Board board = activity.game.board;
        for (Squares i : Squares.values()) {
            i.image.setImageResource(R.drawable.nothing);
        }
        for (Piece i : board.getPieces()) {
            ImageButton image = i.getSquare().image;
            assert image != null;
            switch (i.type) {
                case KING: {
                    if (i.color) {
                        image.setImageResource(R.drawable.piece_king_white);
                    } else {
                        image.setImageResource(R.drawable.piece_king_black);
                    }
                }
                break;
                case QUEEN: {
                    if (i.color) {
                        image.setImageResource(R.drawable.piece_queen_white);
                    } else {
                        image.setImageResource(R.drawable.piece_queen_black);
                    }
                }
                break;
                case BISHOP:
                    if (i.color) {
                        image.setImageResource(R.drawable.piece_bishop_white);
                    } else {
                        image.setImageResource(R.drawable.piece_bishop_black);
                    }
                    break;
                case KNIGHT:
                    if (i.color) {
                        image.setImageResource(R.drawable.piece_knight_white);
                    } else {
                        image.setImageResource(R.drawable.piece_knight_black);
                    }
                    break;
                case PAWN:
                    if (i.color) {
                        image.setImageResource(R.drawable.piece_pawn_white);
                    } else {
                        image.setImageResource(R.drawable.piece_pawn_black);
                    }
                    break;
                case ROOK:
                    if (i.color) {
                        image.setImageResource(R.drawable.piece_rook_white);
                    } else {
                        image.setImageResource(R.drawable.piece_rook_black);
                    }
            }
        }
    }

    public byte getColumn() {
        return (byte) (number & 7);
    }

    public byte getLine() {
        return (byte) (number >> 3);
    }

    public static Squares getSquare(byte number) {
        try {
            return Squares.values()[number];
        } catch (IndexOutOfBoundsException ignored) {
        }
        return null;
    }

    public static ArrayList<Squares> getSquare(int[] numbers) {
        ArrayList<Squares> squares = new ArrayList<>();
        for (int i : numbers) {
            squares.add(getSquare(i));
        }
        return squares;
    }

    public static Squares getSquare(int number) {
        return getSquare((byte) number);
    }

    public static Squares getSquare(String square) {
        String[] array = square.split("");
        return getSquare((byte) (array[0].compareTo("A") + array[1].compareTo("1") * 8));
    }

    public static Squares[] getSquares(String... strings) {
        Squares[] squares = new Squares[strings.length];
        {
            int x = 0;
            for (String i : strings) {
                squares[x] = getSquare(i);
                x++;
            }
        }
        return squares;
    }

    public static byte getLine(int number) {
        return (byte) (number >> 3);
    }

    public static byte getColumn(int number) {
        return (byte) (number & 7);
    }

    public static boolean inOneLine(int first, int second) {
        return first >> 3 == second >> 3;
    }

    public static boolean inOneColumn(int first, int second) {
        return (first & 7) == (second & 7);
    }

    public static boolean isNeighbour(int first, int second) {
        byte line1 = getLine(first);
        byte line2 = getLine(second);
        byte column1 = getColumn(first);
        byte column2 = getColumn(second);
        return Math.abs(line1 - line2) <= 1 && Math.abs(column1 - column2) <= 1;
    }

    public static boolean onEdge(byte number) {
        return (left(number) || right(number) || top(number) || bottom(number));
    }

    public static boolean inCenter(byte number) {
        return !(left(number) || right(number) || top(number) || bottom(number));
    }

    public static boolean left(byte number) {
        return (number & 7) == 0;
    }

    public static boolean right(byte number) {
        return (number & 7) == 7;
    }

    public static boolean top(byte number) {
        return number >> 3 == 7;
    }

    public static boolean bottom(byte number) {
        return number >> 3 == 0;
    }

    public static boolean onLeftTop(byte first, byte second) {
        byte x1 = getColumn(first);
        byte x2 = getColumn(second);
        byte y1 = getLine(first);
        byte y2 = getLine(second);
        return x2 < x1 && y2 > y1;
    }

    public static boolean onRightTop(byte first, byte second) {
        byte x1 = getColumn(first);
        byte x2 = getColumn(second);
        byte y1 = getLine(first);
        byte y2 = getLine(second);
        return x2 > x1 && y2 > y1;
    }

    public static boolean onLeftBot(byte first, byte second) {
        byte x1 = getColumn(first);
        byte x2 = getColumn(second);
        byte y1 = getLine(first);
        byte y2 = getLine(second);
        return x2 < x1 && y2 < y1;
    }

    public static boolean onRightBot(byte first, byte second) {
        byte x1 = getColumn(first);
        byte x2 = getColumn(second);
        byte y1 = getLine(first);
        byte y2 = getLine(second);
        return x2 > x1 && y2 < y1;
    }

    public static ArrayList<Squares> getNeighbourSquares(byte square) {
        ArrayList<Squares> squares = new ArrayList<>(Arrays.asList(getSquare(square + 7), getSquare(square + 8), getSquare(square + 9),
                getSquare(square + 1), getSquare(square - 1), getSquare(square - 7),
                getSquare(square - 8), getSquare(square - 9)));
        squares.removeIf(Objects::isNull);
        squares.removeIf(i -> !isNeighbour(i.number, square));
        return squares;
    }

    public static ArrayList<Squares> getLineUp(byte square) {
        ArrayList<Squares> squares = new ArrayList<>();
        if (top(square)) {
            return null;
        }
        for (byte i = 8; true; i += 8) {
            if ((top((byte) (square + i)))) {
                squares.add(getSquare(square + i));
                break;
            }
            squares.add(getSquare(square + i));
        }
        return squares;
    }

    public static ArrayList<Squares> getLineDown(byte square) {
        ArrayList<Squares> squares = new ArrayList<>();
        if (bottom(square)) {
            return null;
        }
        for (byte i = -8; true; i -= 8) {
            if ((bottom((byte) (square + i)))) {
                squares.add(getSquare(square + i));
                break;
            }
            squares.add(getSquare(square + i));
        }
        return squares;
    }

    public static ArrayList<Squares> getLineRight(byte square) {
        ArrayList<Squares> squares = new ArrayList<>();
        if (right(square)) {
            return null;
        }
        for (byte i = 1; true; i++) {
            if ((right((byte) (square + i)))) {
                squares.add(getSquare(square + i));
                break;
            }
            squares.add(getSquare(square + i));
        }
        return squares;
    }

    public static ArrayList<Squares> getLineLeft(byte square) {
        ArrayList<Squares> squares = new ArrayList<>();
        if (left(square)) {
            return null;
        }
        for (byte i = -1; true; i--) {
            if ((left((byte) (square + i)))) {
                squares.add(getSquare(square + i));
                break;
            }
            squares.add(getSquare(square + i));
        }
        return squares;
    }

    public static ArrayList<Squares> getDiagonalUR(byte square) {
        ArrayList<Squares> squares = new ArrayList<>();
        if (right(square) || top(square)) {
            return null;
        }
        for (byte i = 9; true; i += 9) {
            if (right((byte) (square + i)) || top((byte) (square + i))) {
                squares.add(getSquare(square + i));
                break;
            }
            squares.add(getSquare(square + i));
        }
        return squares;
    }

    public static ArrayList<Squares> getDiagonalUL(byte square) {
        ArrayList<Squares> squares = new ArrayList<>();
        if (left(square) || top(square)) {
            return null;
        }
        for (byte i = 7; true; i += 7) {
            if (left((byte) (square + i)) || top((byte) (square + i))) {
                squares.add(getSquare(square + i));
                break;
            }
            squares.add(getSquare(square + i));
        }
        return squares;
    }

    public static ArrayList<Squares> getDiagonalDR(byte square) {
        ArrayList<Squares> squares = new ArrayList<>();
        if (right(square) || bottom(square)) {
            return null;
        }
        for (byte i = -7; true; i -= 7) {
            if (right((byte) (square + i)) || bottom((byte) (square + i))) {
                squares.add(getSquare(square + i));
                break;
            }
            squares.add(getSquare(square + i));
        }
        return squares;
    }

    public static ArrayList<Squares> getDiagonalDL(byte square) {
        ArrayList<Squares> squares = new ArrayList<>();
        if (left(square) || bottom(square)) {
            return null;
        }
        for (byte i = -9; true; i -= 9) {
            if (left((byte) (square + i)) || bottom((byte) (square + i))) {
                squares.add(getSquare(square + i));
                break;
            }
            squares.add(getSquare(square + i));
        }
        return squares;
    }

    public static ArrayList<Squares> getKnightMoves(byte square) {
        ArrayList<Squares> squares = getSquare(new int[]{square + 17, square + 15,
                square + 10, square + 6, square - 17,
                square - 15, square - 10, square - 6});
        squares.removeIf(Objects::isNull);
        squares.removeIf(i -> Math.abs(getLine(i.number) - getLine(square)) > 2 ||
                Math.abs(getColumn(i.number) - getColumn(square)) > 2);
        return squares;
    }
}

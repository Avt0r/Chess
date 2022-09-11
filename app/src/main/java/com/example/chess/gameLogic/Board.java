package com.example.chess.gameLogic;

import static com.example.chess.gameLogic.Game.black;
import static com.example.chess.gameLogic.Game.white;
import static com.example.chess.gameLogic.Squares.*;

import androidx.annotation.NonNull;

import com.example.chess.gameLogic.Pieces.*;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Board {

//    private ArrayList<Piece> pieces = new ArrayList<>();
    private ArrayList<PiecesListElement> piecesList = new ArrayList<>();

    //7  56 57 68 69 60 61 62 63
    //6  48 49 50 51 52 53 54 55
    //5  40 41 42 43 44 45 46 47
    //4  32 33 34 35 36 37 38 39
    //3  24 25 26 27 28 29 30 31
    //2  16 17 18 19 20 21 22 23
    //1  8  9  10 11 12 13 14 15
    //0  0  1  2  3  4  5  6  7

    private final BitSet grid;
    private EventTypes lastEvent;

    public Board() {
        grid = new BitSet(64);
        grid.set(0, 16);
        grid.set(48, 64);
        setStartPieces();
    }

    public Board(Board board) {
        this.grid = board.grid.get(0, 64);
        this.piecesList = board.piecesList;
    }

    public ArrayList<PiecesListElement> getPiecesList() {
        return piecesList;
    }

    public EventTypes getLastEvent() {
        return lastEvent;
    }

    public Board getCopy() {
        return new Board(this);
    }

    private void setStartPieces() {
        ArrayList<Piece> pieces = new ArrayList<>();
        pieces.add(new King(E1, white));
        pieces.add(new Queen(D1, white));
        pieces.add(new Bishop(F1, white));
        pieces.add(new Bishop(C1, white));
        pieces.add(new Knight(B1, white));
        pieces.add(new Knight(G1, white));
        pieces.add(new Rook(A1, white));
        pieces.add(new Rook(H1, white));
        for (int i = 8; i < 16; i++) {
            pieces.add(new Pawn(getSquare(i), white));
        }

        pieces.add(new King(E8, black));
        pieces.add(new Queen(D8, black));
        pieces.add(new Bishop(F8, black));
        pieces.add(new Bishop(C8, black));
        pieces.add(new Knight(B8, black));
        pieces.add(new Knight(G8, black));
        pieces.add(new Rook(A8, black));
        pieces.add(new Rook(H8, black));
        for (int i = 48; i < 56; i++) {
            pieces.add(new Pawn(getSquare(i), black));
        }
        piecesList.add(makeListElement(pieces));
    }

    public int count() {
        if (isCheck(true)) {
            return -1000;
        }
        if (isCheck(false)) {
            return 1000;
        }
        if (isMate(true)) {
            return -2000;
        }
        if (isMate(false)) {
            return 2000;
        }
        int score = 0;
        for (Piece i : getLastCondition().getCondition()) {
            score += i.value;
        }
        return score;
    }

    public ArrayList<Piece> getPieces() {
        ArrayList<Piece> copy = getLastCondition().getCondition();
        for (Piece i : pieces) {
            switch (i.type) {
                case KING:
                    copy.add(new King((King) i));
                    break;
                case QUEEN:
                    copy.add(new Queen((Queen) i));
                    break;
                case ROOK:
                    copy.add(new Rook((Rook) i));
                    break;
                case BISHOP:
                    copy.add(new Bishop((Bishop) i));
                    break;
                case KNIGHT:
                    copy.add(new Knight((Knight) i));
                    break;
                case PAWN:
                    copy.add(new Pawn((Pawn) i));
            }
        }
        return copy;
    }

    public Piece getPiece(Squares squares) {
        return pieces.stream().filter(piece -> piece.getSquare().equals(squares)).findAny().orElse(null);
    }

    public boolean isMate(boolean color) {
        boolean a = isCheck(color);
        boolean b = !canKingMove(color);
        boolean c = !canKingBeProtected(color);
        return a && b && c;
    }

    private boolean willKingBeProtected(Piece piece, Squares to) {
        Board copy = new Board(this);
        if (grid.get(to.number)) {
            if (copy.canAttack(piece, to)) {
                copy.move(piece.getSquare(), to);
                return !copy.isKingOnAttack(piece.color);
            }
        } else {
            if (copy.canMove(piece, to)) {
                copy.move(piece.getSquare(), to);
                return !copy.isKingOnAttack(piece.color);
            }
        }
        return false;
    }

    private boolean canKingBeProtected(boolean color) {
        if (!isCheck(color)) {
            return true;
        }
        if (canKingMove(color)) {
            return true;
        }
        King king = getKing(color);
        ArrayList<Piece> enemies = getPieces();
        enemies.removeIf(i -> i.color == color);
        enemies.removeIf(i -> !canAttack(i, king));
        if (enemies.size() == 0) {
            return true;
        } else if (enemies.size() > 1) {
            return false;
        } else {
            ArrayList<Squares> squares = movingPath(king.getSquare(),
                    enemies.get(0).getSquare());
            if (squares == null) {
                squares = new ArrayList<>();
            }
            squares.add(enemies.get(0).getSquare());
            ArrayList<Piece> protect = getPieces();
            protect.removeIf(i -> i.color != color || i.type == Types.KING);
            for (Squares i : squares) {
                for (Piece j : protect) {
                    if (willKingBeProtected(j, i)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCheck(boolean color) {
        return isKingOnAttack(color);
    }

    private boolean canKingMove(boolean color) {
        King king = getKing(color);
        List<Squares> squares = getPaths(king);
        if (squares == null) {
            return false;
        }
        for (Squares i : squares) {
            if (grid.get(i.number)) {
                if (getPiece(i).color != color) {
                    List<Piece> copy = getPieces();
                    copy.removeIf(j -> j.color == color);
                    copy.removeIf(j -> j.getSquare() == i);
                    for (Piece p : copy) {
                        if (canAttack(p, i)) {
                            return false;
                        }
                    }
                }
            } else {
                List<Piece> copy = getPieces();
                copy.removeIf(j -> j.color == color);
                for (Piece p : copy) {
                    if (canAttack(p, i)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private King getKing(boolean color) {
        return new King((King) pieces.stream().filter(i -> i.type.equals(Types.KING) && i.color == color).findAny().orElse(null));
    }

    private boolean isKingOnAttack(boolean color) {
        List<Piece> copy = getPieces();
        copy.removeIf(i -> i.color == color);
        King king = getKing(color);
        copy.removeIf(i -> !canAttack(i, king));
        return copy.size() > 0;
    }

    private boolean pathCheck(String path) {
        if (path == null) {
            return false;
        }
        if (!path.contains("-")) {
            return false;
        }
        String[] array = path.split("-");
        return getSquare(array[1]) != null && getSquare(array[0]) != null;
    }

    private boolean mistakeCheck(String path, boolean color) {
        if (!pathCheck(path)) {
            return true;
        }
        Squares[] squares = {getSquare(path.split("-")[0]),
                getSquare(path.split("-")[1])};
        if (getPiece(squares[0]) == null) {
            return true;
        }
        Piece piece = getPiece(squares[0]);
        assert piece != null;
        if (piece.color != color) {
            return true;
        }
        if (getPiece(squares[0]).type != Types.KNIGHT) {
            if (barrierCheck(squares[0], squares[1])) {
                return true;
            }
        }
        if (grid.get(squares[1].number)) {
            Piece enemy = getPiece(squares[1]);
            assert enemy != null;
            if (enemy.color == color) {
                return true;
            }
            return !piece.canAttack(enemy);
        } else {
            return !piece.canMove(squares[1]);
        }
    }

    private boolean barrierCheck(Squares from, Squares to) {
        return barrierCheck(from.number, to.number);
    }

    private boolean barrierCheck(int from, int to) {
        ArrayList<Squares> path = movingPath(from, to);
        if (path == null) {
            return false;
        }
        for (Squares i : path) {
            if (grid.get(i.number)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Squares> movingPath(int from, int to) {
        if (from == to) {
            return null;
        }
        if (isNeighbour(from, to)) {
            return null;
        }
        ArrayList<Squares> path = new ArrayList<>();
        if (inOneColumn(from, to)) {
            if (from > to) {
                do {
                    from -= 8;
                    path.add(getSquare(from));
                } while (!isNeighbour(from, to));
            } else {
                do {
                    from += 8;
                    path.add(getSquare(from));
                } while (!isNeighbour(from, to));
            }
            return path;
        } else if (inOneLine(from, to)) {
            if (from > to) {
                do {
                    from--;
                    path.add(getSquare(from));
                } while (!isNeighbour(from, to));
            } else {
                do {
                    from++;
                    path.add(getSquare(from));
                } while (!isNeighbour(from, to));
            }
        } else {
            if (onRightTop((byte) from, (byte) to)) {
                do {
                    from += 9;
                    path.add(getSquare(from));
                } while (!isNeighbour(from, to) &&
                        !inOneColumn(from, to) &&
                        !inOneLine(from, to));
            } else if (onLeftTop((byte) from, (byte) to)) {
                do {
                    from += 7;
                    path.add(getSquare(from));
                } while (!isNeighbour(from, to) &&
                        !inOneColumn(from, to) &&
                        !inOneLine(from, to));
            } else if (onRightBot((byte) from, (byte) to)) {
                do {
                    from -= 7;
                    path.add(getSquare(from));
                } while (!isNeighbour(from, to) &&
                        !inOneColumn(from, to) &&
                        !inOneLine(from, to));
            } else if (onLeftBot((byte) from, (byte) to)) {
                do {
                    from -= 9;
                    path.add(getSquare(from));
                } while (!isNeighbour(from, to) &&
                        !inOneColumn(from, to) &&
                        !inOneLine(from, to));
            }
            if (!isNeighbour(from, to)) {
                if (inOneLine(from, to)) {
                    if (from > to) {
                        do {
                            from--;
                            path.add(getSquare(from));
                        } while (!isNeighbour(from, to));
                    } else {
                        do {
                            from++;
                            path.add(getSquare(from));
                        } while (!isNeighbour(from, to));
                    }
                } else {
                    if (from > to) {
                        do {
                            from -= 8;
                            path.add(getSquare(from));
                        } while (!isNeighbour(from, to));
                    } else {
                        do {
                            from += 8;
                            path.add(getSquare(from));
                        } while (!isNeighbour(from, to));
                    }
                }
            }
        }
        return path;
    }

    private ArrayList<Squares> movingPath(Squares from, Squares to) {
        return movingPath(from.number, to.number);
    }

    public boolean canChange(boolean color) {
        List<Piece> list = pieces.stream().filter(i -> i.type == Types.PAWN && i.color == color
                && ((Pawn) i).canChange()).collect(Collectors.toList());
        return list.size() > 0;
    }

    public void changePiece(Types type) {
        int index = -1;
        for (Piece i : pieces) {
            if (i.type == Types.PAWN && ((Pawn) i).canChange()) {
                index = pieces.indexOf(i);
            }
        }
        if (index == -1) {
            return;
        }
        Piece pawn = pieces.get(index);
        switch (type) {
            case ROOK:
                pawn = new Rook(pawn.getSquare(), pawn.color);
                break;
            case QUEEN:
                pawn = new Queen(pawn.getSquare(), pawn.color);
                break;
            case BISHOP:
                pawn = new Bishop(pawn.getSquare(), pawn.color);
                break;
            case KNIGHT:
                pawn = new Knight(pawn.getSquare(), pawn.color);
        }
        pieces.set(index, pawn);
    }

    private void move(Squares from, Squares to) {
        lastEvent = EventTypes.MOVING;
        if (grid.get(to.number)) {
            pieces.remove(getPiece(to));
            lastEvent = EventTypes.ATTACKING;
        }
        grid.clear(from.number);
        grid.set(to.number);
        assert getPiece(from) != null;
        getPiece(from).setSquare(to);
    }

    public void fastMove(String path) {
        Squares[] squares = new Squares[]{
                getSquare(path.split("-")[0]),
                getSquare(path.split("-")[1])
        };
        move(squares[0], squares[1]);
    }

    public boolean move(String path, boolean color) {
        if (mistakeCheck(path, color)) {
            return false;
        }
        Squares from;
        Squares to;
        {
            String[] ar = path.split("-");
            from = getSquare(ar[0]);
            to = getSquare(ar[1]);
        }
        if (canCastling(color, path)) {
            castling(from, to);
            lastEvent = EventTypes.CASTLING;
            return true;
        }
        if (willKingBeProtected(getPiece(from), to)) {
            move(from, to);
            return true;
        } else {
            return false;
        }
    }

    public boolean canMove(Piece piece, Squares to) {
        if (getPiece(to) != null) {
            return false;
        }
        if (piece.type.equals(Types.KNIGHT)) {
            return piece.canMove(to);
        } else {
            return piece.canMove(to) &&
                    !barrierCheck(piece.getSquare(), to);
        }
    }

    public boolean canAttack(Piece attacking, Piece attacked) {
        if (attacking.color == attacked.color) {
            return false;
        }
        if (attacking.type.equals(Types.KNIGHT)) {
            return attacking.canAttack(attacked);
        } else {
            return attacking.canAttack(attacked) &&
                    !barrierCheck(attacking.getSquare(),
                            attacked.getSquare());
        }
    }

    public boolean canAttack(Piece attacking, Squares to) {
        if (attacking.type.equals(Types.KNIGHT)) {
            return attacking.canAttack(to);
        } else {
            return attacking.canAttack(to) &&
                    !barrierCheck(attacking.getSquare(), to);
        }
    }

    //есть ли возможность рокировки
    public boolean canCastling(boolean color, String path) {
        //если король под атакой, рокировка невозможна
        if (isKingOnAttack(color)) {
            return false;
        }
        King king = getKing(color);

        Squares[] squares = getSquares(path.split("-"));
        //если король уже ходил, то рокировка невозможна
        if (!king.canCastling(squares[0])) {
            return false;
        }
        //правильно ли указана конечная точка
        if (color) {
            if (squares[1] != C1 && squares[1] != G1) {
                return false;
            }
        } else {
            if (squares[1] != C8 && squares[1] != G8) {
                return false;
            }
        }
        //подходит ли ладья
        Rook rook;
        {
            List<Piece> rooks = pieces.stream().filter(i -> i.type == Types.ROOK && ((Rook) i).canCastling()).collect(Collectors.toList());
            switch (squares[1]) {
                case C1:
                    rook = (Rook) rooks.stream().filter(i -> i.getSquare() == A1).findAny().orElse(null);
                    break;
                case G1:
                    rook = (Rook) rooks.stream().filter(i -> i.getSquare() == H1).findAny().orElse(null);
                    break;
                case C8:
                    rook = (Rook) rooks.stream().filter(i -> i.getSquare() == A8).findAny().orElse(null);
                    break;
                case G8:
                    rook = (Rook) rooks.stream().filter(i -> i.getSquare() == H8).findAny().orElse(null);
                    break;
                default:
                    rook = null;
            }
        }
        if (rook == null) {
            return false;
        }
        //если король будет под атакой, рокировка не возможна
        if (!willKingBeProtected(king, squares[1])) {
            return false;
        }
        //если нет преграды то можно сделать рокировку
        return !barrierCheck(squares[0], squares[1]);
    }

    private void castling(Squares from, Squares to) {
        switch (to) {
            case G1:
                fastMove(H1 + "-" + F1);
                break;
            case B1:
                fastMove(A1 + "-" + C1);
                break;
            case G8:
                fastMove(H8 + "-" + F8);
                break;
            case B8:
                fastMove(A8 + "-" + C8);
                break;
            default: {
                return;
            }
        }
        fastMove(from + "-" + to);
    }

    public String draw() {
        return this.toString();
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder board = new StringBuilder("    A   B   C   D   E   F   G   H\n");
        StringBuilder line = new StringBuilder("  #################################\n");
        board.append(line);
        for (int i = 63; i >= 0; i--) {
            if (right((byte) i)) {
                line = new StringBuilder("\n");
            }
            Piece piece = getPiece(getSquare(i));
            line.insert(0, (piece == null ? " " : piece) + " # ");
            if (left((byte) i)) {
                line.insert(0, (getLine(i) + 1) + " # ");
                board.append(line);
                line = new StringBuilder();
            }
        }
        board.append("  #################################\n");
        board.append("    A   B   C   D   E   F   G   H\n");
        return board.toString();
    }

    public List<Squares> getPaths(Piece piece) {
        List<Squares> paths = new ArrayList<>();
        switch (piece.type) {
            case KING: {
                paths = getNeighbourSquares(piece.getSquare().number);
                List<Squares> remove = new ArrayList<>();
                for (Squares i : paths) {
                    if (grid.get(i.number)) {
                        if (getPiece(i).color == piece.color) {
                            remove.add(i);
                        }
                    }
                }
                paths.removeAll(remove);
                if (((King) piece).isFirstStep())
                    if (piece.color) {
                        if (canCastling(true, piece.getSquare() + "-" + G1)) {
                            paths.add(G1);
                        }
                        if (canCastling(true, piece.getSquare() + "-" + B1)) {
                            paths.add(B1);
                        }
                    } else {
                        if (canCastling(false, piece.getSquare() + "-" + G8)) {
                            paths.add(G8);
                        }
                        if (canCastling(false, piece.getSquare() + "-" + B8)) {
                            paths.add(B8);
                        }
                    }
            }
            break;
            case QUEEN: {
                byte pos = piece.getSquare().number;
                ArrayList<Squares> copy = getLineUp(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineDown(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineRight(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineLeft(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalUR(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalDR(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalDL(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalUL(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
            }
            break;
            case KNIGHT: {
                paths = getKnightMoves(piece.getSquare().number);
                List<Squares> remove = new ArrayList<>();
                for (Squares i : paths) {
                    if (grid.get(i.number)) {
                        if (getPiece(i).color == piece.color) {
                            remove.add(i);
                        }
                    }
                }
                paths.removeAll(remove);
            }
            break;
            case PAWN: {
                byte pos = piece.getSquare().number;
                if (piece.color) {
                    if (pos + 8 < 64)
                        if (!grid.get(pos + 8)) {
                            paths.add(getSquare(pos + 8));
                            if (getLine(pos) == 1) {
                                if (!grid.get(pos + 16)) {
                                    paths.add(getSquare(pos + 16));
                                }
                            }
                        }
                    if (!right(pos))
                        if (pos + 9 < 64)
                            if (grid.get(pos + 9)) {
                                if (!Objects.requireNonNull(getPiece(getSquare(pos + 9))).color) {
                                    paths.add(getSquare(pos + 9));
                                }
                            }
                    if (!left(pos))
                        if (pos + 7 < 64)
                            if (grid.get(pos + 7)) {
                                if (!Objects.requireNonNull(getPiece(getSquare(pos + 7))).color) {
                                    paths.add(getSquare(pos + 7));
                                }
                            }
                } else {
                    if (pos - 8 >= 0)
                        if (!grid.get(pos - 8)) {
                            paths.add(getSquare(pos - 8));
                            if (getLine(pos) == 6) {
                                if (!grid.get(pos - 16)) {
                                    paths.add(getSquare(pos - 16));
                                }
                            }
                        }
                    if (!left(pos))
                        if (pos - 9 >= 0)
                            if (grid.get(pos - 9)) {
                                if (Objects.requireNonNull(getPiece(getSquare(pos - 9))).color) {
                                    paths.add(getSquare(pos - 9));
                                }
                            }
                    if (!right(pos))
                        if (pos - 7 >= 0)
                            if (grid.get(pos - 7)) {
                                if (Objects.requireNonNull(getPiece(getSquare(pos - 7))).color) {
                                    paths.add(getSquare(pos - 7));
                                }
                            }
                }
            }
            break;
            case ROOK: {
                byte pos = piece.getSquare().number;
                ArrayList<Squares> copy = getLineUp(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineDown(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineRight(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineLeft(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
            }
            break;
            case BISHOP: {
                byte pos = piece.getSquare().number;
                ArrayList<Squares> copy = getDiagonalUR(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalDR(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalDL(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalUL(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (grid.get(i.number)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
            }
        }
        return paths;
    }

    public ArrayList<String> generatePaths(boolean color) {
        ArrayList<String> paths = new ArrayList<>();
        ArrayList<Piece> copyPieces = new ArrayList<>(pieces);
        copyPieces.removeIf(i -> i.color != color);
        for (Piece i : copyPieces) {
            List<Squares> squares = getPaths(i);
            if (squares != null) {
                for (Squares j : squares) {
                    if (willKingBeProtected(i, j)) {
                        paths.add(i.getSquare() + "-" + j);
                    }
                }
            }
        }
        return paths;
    }

    private static class PiecesListElement {
        private final Piece[] pieces = new Piece[64];

        PiecesListElement() {
        }

        PiecesListElement(Piece[] pieces) {
            System.arraycopy(pieces, 0, this.pieces, 0, 64);
        }

        public Piece[] getCondition() {
            return pieces;
        }

        public ArrayList<Piece> getPieces(){
            ArrayList<Piece> pieces = new ArrayList<>();
            for (Piece i: getCondition()){
                if(i == null) continue;
                pieces.add(i);
            }
            return pieces;
        }

        private static PiecesListElement makeListElement(Piece... pieces) {
            PiecesListElement element = new PiecesListElement();
            if (pieces != null) {
                for (Piece i : pieces) {
                    element.pieces[i.getSquare().number] = i;
                }
            }
            return element;
        }

        private static PiecesListElement makeListElement(ArrayList<Piece> pieces) {
            PiecesListElement element = new PiecesListElement();
            if (pieces != null) {
                for (Piece i : pieces) {
                    element.pieces[i.getSquare().number] = i;
                }
            }
            return element;
        }
    }

    private PiecesListElement getLastCondition() {
        assert piecesList.size() > 0;
        return piecesList.get(piecesList.size() - 1);
    }

    public void undo() {
        if (piecesList.size() > 1)
            piecesList.remove(piecesList.size() - 1);
    }
}
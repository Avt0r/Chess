package com.example.chess.gameLogic;

import static com.example.chess.gameLogic.Game.black;
import static com.example.chess.gameLogic.Game.white;
import static com.example.chess.gameLogic.Squares.*;

import androidx.annotation.NonNull;

import com.example.chess.gameLogic.Pieces.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {

    //history list of pieces positions
    private final List<PiecesCondition> piecesList = new ArrayList<>();

    //7  56 57 68 69 60 61 62 63
    //6  48 49 50 51 52 53 54 55
    //5  40 41 42 43 44 45 46 47
    //4  32 33 34 35 36 37 38 39
    //3  24 25 26 27 28 29 30 31
    //2  16 17 18 19 20 21 22 23
    //1  8  9  10 11 12 13 14 15
    //0  0  1  2  3  4  5  6  7

    //variable contains last event:moving, attacking, castling, change pawn to another piece
    private EventTypes lastEvent;

    public Board() {
        addElement(getInitialPieces());
    }

    public Board(Board board) {
        addAllElements(board.piecesList);
        this.lastEvent = board.lastEvent;
    }

    //returns last event
    public EventTypes getLastEvent() {
        return lastEvent;
    }

    //returns the initial positions of the pieces
    public static PiecesCondition getInitialPieces() {
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
        return (PiecesCondition.makeListElement(pieces));
    }

    //calculates board score
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

    //returns copy of list pieces
    public List<Piece> getPieces() {
        List<Piece> copy = new ArrayList<>();
        for (Piece i : getLastCondition().getCondition()) {
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

    //returns the piece by position
    public Piece getPiece(Squares squares) {
        return getLastCondition().getPiece(squares);
    }

    //check mate
    public boolean isMate(boolean color) {
        boolean a = isCheck(color);
        boolean b = !canKingMove(color);
        boolean c = !canKingBeProtected(color);
        return a && b && c;
    }

    //method checks if the king will
    //be protected after the move
    private boolean willKingBeProtected(Piece piece, Squares to) {
        Board copy = new Board(this);
        if (isTherePiece(to)) {
            if (copy.canAttack(piece, to)) {
                copy.move(new Path(piece.getSquare(), to));
                return !copy.isKingOnAttack(piece.color);
            }
        } else {
            if (copy.canMove(piece, to)) {
                copy.move(new Path(piece.getSquare(), to));
                return !copy.isKingOnAttack(piece.color);
            }
        }
        return false;
    }

    //method checks if the king
    //can be protected
    private boolean canKingBeProtected(boolean color) {
        if (!isCheck(color)) {
            return true;
        }
        if (canKingMove(color)) {
            return true;
        }
        King king = getKing(color);
        List<Piece> enemies = getPieces();
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
            List<Piece> protect = getPieces();
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

    //the method checks if
    //there is a check
    public boolean isCheck(boolean color) {
        return isKingOnAttack(color);
    }

    //method checks if the king can move
    private boolean canKingMove(boolean color) {
        King king = getKing(color);
        List<Squares> squares = getPaths(king);
        if (squares == null) {
            return false;
        }
        for (Squares i : squares) {
            if (isTherePiece(i)) {
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

    //returns king by color
    private King getKing(boolean color) {
        return getLastCondition().getKing(color);
    }

    //method checks if the
    //king is under attack
    private boolean isKingOnAttack(boolean color) {
        List<Piece> copy = getPieces();
        copy.removeIf(i -> i.color == color);
        King king = getKing(color);
        copy.removeIf(i -> !canAttack(i, king));
        return copy.size() > 0;
    }

    //method that looking for mistakes in the path
    private boolean mistakeCheck(Path path, boolean color) {
        if (!isTherePiece(path.getFrom())) {
            return true;
        }
        Piece piece = getPiece(path.getFrom());
        if (piece.color != color) {
            return true;
        }
        if (piece.type != Types.KNIGHT) {
            if (barrierCheck(path.getFrom(), path.getTo())) {
                return true;
            }
        }
        if (isTherePiece(path.getTo())) {
            Piece enemy = getPiece(path.getTo());
            if (enemy.color == color) {
                return true;
            }
            return !piece.canAttack(enemy);
        } else {
            return !piece.canMove(path.getTo());
        }
    }

    //methods check barrier between two squares
    private boolean barrierCheck(Squares from, Squares to) {
        return barrierCheck(from.number, to.number);
    }

    private boolean barrierCheck(int from, int to) {
        ArrayList<Squares> path = movingPath(from, to);
        if (path == null) {
            return false;
        }
        for (Squares i : path) {
            if (isTherePiece(i)) {
                return true;
            }
        }
        return false;
    }

    //method pave path between two squares
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

    //methods checks if a pawn can be changed
    public boolean canChange(boolean color) {
        List<Piece> list = getLastCondition().getPieces(Types.PAWN, color);
        for (Piece i : list) {
            if (((Pawn) i).canChange()) {
                return true;
            }
        }
        return false;
    }

    //method changes pawn to selected piece type
    public void changePiece(Types type) {
        int index = -1;
        for (Piece i : getLastCondition().list) {
            if (i.type == Types.PAWN && ((Pawn) i).canChange()) {
                index = i.getSquare().number;
            }
        }
        if (index == -1) {
            return;
        }
        Piece pawn = getLastCondition().condition[index];
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
        getLastCondition().condition[index] = pawn;
    }

    //method moves piece by path
    private void move(Path path) {
        lastEvent = EventTypes.MOVING;
        if (isTherePiece(path.getTo())) {
            lastEvent = EventTypes.ATTACKING;
        }
        PiecesCondition element = new PiecesCondition(getLastCondition());
        element.move(path.getFrom(), path.getTo());
        addElement(element);
    }

    //method makes fast move without check mistakes
    public void fastMove(Path path) {
        move(path);
    }

    //method makes move
    public boolean move(Path path, boolean color) {
        if (mistakeCheck(path, color)) {
            return false;
        }

        if (canCastling(color, path)) {
            castling(path);
            lastEvent = EventTypes.CASTLING;
            return true;
        }
        if (willKingBeProtected(getPiece(path.getFrom()), path.getTo())) {
            move(path);
            return true;
        } else {
            return false;
        }
    }

    //method checks if a piece can move to square
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

    //method checks if a piece can attack another piece
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

    //method checks if a piece can attack square
    public boolean canAttack(Piece attacking, Squares to) {
        if (attacking.type.equals(Types.KNIGHT)) {
            return attacking.canAttack(to);
        } else {
            return attacking.canAttack(to) &&
                    !barrierCheck(attacking.getSquare(), to);
        }
    }

    //method checks if a piece can castling
    public boolean canCastling(boolean color, Path path) {
        if (isKingOnAttack(color)) {
            return false;
        }
        King king = getKing(color);

        Squares[] squares = {path.getFrom(), path.getTo()};
        if (!king.canCastling(squares[0])) {
            return false;
        }
        if (color) {
            if (path.getTo() != C1 && path.getTo() != G1) {
                return false;
            }
        } else {
            if (path.getTo() != C8 && path.getTo() != G8) {
                return false;
            }
        }
        Rook rook;
        {
            List<Piece> rooks = getLastCondition().getPieces(Types.ROOK, color);
            switch (path.getTo()) {
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
        if (!willKingBeProtected(king, path.getTo())) {
            return false;
        }
        return !barrierCheck(path.getFrom(), path.getTo());
    }

    //method does castling
    private void castling(Path path) {
        switch (path.getTo()) {
            case G1:
                fastMove(new Path(H1, F1));
                break;
            case B1:
                fastMove(new Path(A1, C1));
                break;
            case G8:
                fastMove(new Path(H8, F8));
                break;
            case B8:
                fastMove(new Path(A8, C8));
                break;
            default: {
                return;
            }
        }
        fastMove(path);
    }

    //method makes String from board
    @NonNull
    @Override
    public String toString() {
        return getLastCondition().toString();
    }

    //method paves paths for piece
    public List<Squares> getPaths(Piece piece) {
        List<Squares> paths = new ArrayList<>();
        switch (piece.type) {
            case KING: {
                paths = getNeighbourSquares(piece.getSquare().number);
                List<Squares> remove = new ArrayList<>();
                for (Squares i : paths) {
                    if (isTherePiece(i)) {
                        if (getPiece(i).color == piece.color) {
                            remove.add(i);
                        }
                    }
                }
                paths.removeAll(remove);
                if (((King) piece).isFirstStep())
                    if (piece.color) {
                        if (canCastling(true, new Path(piece.getSquare(), G1))) {
                            paths.add(G1);
                        }
                        if (canCastling(true, new Path(piece.getSquare(), B1))) {
                            paths.add(B1);
                        }
                    } else {
                        if (canCastling(false, new Path(piece.getSquare(), G8))) {
                            paths.add(G8);
                        }
                        if (canCastling(false, new Path(piece.getSquare(), B8))) {
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
                        if (isTherePiece(i)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineDown(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineRight(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineLeft(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalUR(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalDR(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalDL(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (Objects.requireNonNull(getPiece(i)).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalUL(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
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
                    if (isTherePiece(i)) {
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
                        if (!isTherePiece(pos + 8)) {
                            paths.add(getSquare(pos + 8));
                            if (getLine(pos) == 1) {
                                if (!isTherePiece(pos + 16)) {
                                    paths.add(getSquare(pos + 16));
                                }
                            }
                        }
                    if (!right(pos))
                        if (pos + 9 < 64)
                            if (isTherePiece(pos + 9)) {
                                if (!Objects.requireNonNull(getPiece(getSquare(pos + 9))).color) {
                                    paths.add(getSquare(pos + 9));
                                }
                            }
                    if (!left(pos))
                        if (pos + 7 < 64)
                            if (isTherePiece(pos + 7)) {
                                if (!Objects.requireNonNull(getPiece(getSquare(pos + 7))).color) {
                                    paths.add(getSquare(pos + 7));
                                }
                            }
                } else {
                    if (pos - 8 >= 0)
                        if (!isTherePiece(pos - 8)) {
                            paths.add(getSquare(pos - 8));
                            if (getLine(pos) == 6) {
                                if (!isTherePiece(pos - 16)) {
                                    paths.add(getSquare(pos - 16));
                                }
                            }
                        }
                    if (!left(pos))
                        if (pos - 9 >= 0)
                            if (isTherePiece(pos - 9)) {
                                if (Objects.requireNonNull(getPiece(getSquare(pos - 9))).color) {
                                    paths.add(getSquare(pos - 9));
                                }
                            }
                    if (!right(pos))
                        if (pos - 7 >= 0)
                            if (isTherePiece(pos - 7)) {
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
                        if (isTherePiece(i)) {
                            if (getPiece(i).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineDown(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (getPiece(i).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineRight(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (getPiece(i).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getLineLeft(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (getPiece(i).color != piece.color)
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
                        if (isTherePiece(i)) {
                            if (getPiece(i).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalDR(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (getPiece(i).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalDL(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (getPiece(i).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
                copy = getDiagonalUL(pos);
                if (copy != null)
                    for (Squares i : copy) {
                        if (isTherePiece(i)) {
                            if (getPiece(i).color != piece.color)
                                paths.add(i);
                            break;
                        }
                        paths.add(i);
                    }
            }
        }
        return paths;
    }

    //method generates paths for player
    public List<Path> generatePaths(boolean color) {
        List<Path> paths = new ArrayList<>();
        List<Piece> copyPieces = getLastCondition().getCopyList();
        copyPieces.removeIf(i -> i.color != color);
        for (Piece i : copyPieces) {
            List<Squares> squares = getPaths(i);
            if (squares != null) {
                for (Squares j : squares) {
                    if (willKingBeProtected(i, j)) {
                        paths.add(new Path(i.getSquare(), j));
                    }
                }
            }
        }
        return paths;
    }

    //this class stores the condition of pieces
    public static class PiecesCondition {

        //array stores the state of each square
        private final Piece[] condition = new Piece[64];
        //array stores all pieces on the board
        private final List<Piece> list = new ArrayList<>();

        PiecesCondition(List<Piece> condition) {
            for (Piece i : condition) {
                this.condition[i.getSquare().number] = i;
            }
            updateList();
        }

        PiecesCondition(PiecesCondition element) {
            System.arraycopy(element.condition, 0, this.condition, 0, 64);
            this.list.addAll(element.getCopyList());
        }

        //method updates list of pieces
        private void updateList() {
            list.clear();
            for (Piece i : condition) {
                if (i != null) {
                    list.add(i);
                }
            }
        }

        //method matches the squares of the pieces with array of squares
        private void fixPieces() {
            for (int i = 0; i < 64; i++) {
                if (condition[i] != null){
                    condition[i].setSquare(getSquare(i));
                }
            }
            updateList();
        }

        //method moves piece
        private void move(Squares from, Squares to) {
            if (condition[from.number] == null) {
                return;
            }
            condition[from.number].setSquare(to);
            condition[to.number] = condition[from.number];
            condition[from.number] = null;
            fixPieces();
        }

        //method returns copy of pieces list
        private List<Piece> getCopyList() {
            List<Piece> copy = new ArrayList<>();
            for (Piece i : list) {
                copy.add(Piece.makeCopyPiece(i));
            }
            return copy;
        }

        //method returns pieces list
        public List<Piece> getCondition() {
            return list;
        }

        //method returns sorted pieces list
        public List<Piece> getPieces(Types type, boolean color) {
            List<Piece> copy = new ArrayList<>();
            for (Piece i : list) {
                if (i.type == type && i.color == color) {
                    copy.add(i);
                }
            }
            return copy;
        }

        //method returns king
        public King getKing(boolean color) {
            return (King) getPieces(Types.KING, color).get(0);
        }

        //method returns piece by square
        public Piece getPiece(Squares squares) {
            return condition[squares.number];
        }

        //method makes condition of pieces list
        private static PiecesCondition makeListElement(List<Piece> pieces) {
            return new PiecesCondition(pieces);
        }

        //method returns String of condition
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
    }

    //method returns last condition of list
    public PiecesCondition getLastCondition() {
        assert piecesList.size() > 0;
        return piecesList.get(piecesList.size() - 1);
    }

    //method returns pre last condition of list
    public PiecesCondition getPreLastCondition() {
        return piecesList.size() > 1 ? piecesList.get(piecesList.size() - 2) : getInitialPieces();
    }

    //method adds new element to list
    private void addElement(PiecesCondition element) {
        piecesList.add(new PiecesCondition(element));
    }

    //method adds list of elements
    private void addAllElements(List<PiecesCondition> elements) {
        for (PiecesCondition i : elements) {
            addElement(i);
        }
    }

    //method deletes last condition
    public void undo() {
        if (piecesList.size() > 1)
            piecesList.remove(piecesList.size() - 1);
        getLastCondition().fixPieces();
    }

    //method checks if the piece is on square
    public boolean isTherePiece(Squares square) {
        return getLastCondition().getPiece(square) != null;
    }

    public boolean isTherePiece(int square) {
        return getLastCondition().getPiece(getSquare(square)) != null;
    }
}
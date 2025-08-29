package br.com.dio.model;

import java.util.Collection;
import java.util.List;

import static br.com.dio.model.GameStatusEnum.COMPLETE;
import static br.com.dio.model.GameStatusEnum.INCOMPLETE;
import static br.com.dio.model.GameStatusEnum.NON_STARTED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces;

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus(){
        if (spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))){
            return NON_STARTED;
        }

        return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETE;
    }

    public boolean hasErrors(){
        if(getStatus() == NON_STARTED){
            return false;
        }

        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }

    /**
     * Verifica se a jogada é válida de acordo com as regras do Sudoku.
     *
     * @param row   A linha onde o número será inserido.
     * @param col   A coluna onde o número será inserido.
     * @param value O número a ser inserido.
     * @return true se a jogada for válida, false caso contrário.
     */
    public boolean isValidMove(final int row, final int col, final int value) {
        // Verifica a linha
        for (int i = 0; i < 9; i++) {
            if (i != col && nonNull(this.spaces.get(row).get(i).getActual()) && this.spaces.get(row).get(i).getActual().equals(value)) {
                return false;
            }
        }

        // Verifica a coluna
        for (int i = 0; i < 9; i++) {
            if (i != row && nonNull(this.spaces.get(i).get(col).getActual()) && this.spaces.get(i).get(col).getActual().equals(value)) {
                return false;
            }
        }

        // Verifica a sub-grade 3x3
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int currRow = startRow + i;
                int currCol = startCol + j;
                if (currRow != row || currCol != col) { // Certifica-se de não verificar a própria célula
                    if (nonNull(this.spaces.get(currRow).get(currCol).getActual()) && this.spaces.get(currRow).get(currCol).getActual().equals(value)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean changeValue(final int col, final int row, final int value){
        var space = spaces.get(col).get(row);
        if (space.isFixed()){
            return false;
        }
        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int col, final int row){
        var space = spaces.get(col).get(row);
        if (space.isFixed()){
            return false;
        }
        space.clearSpace();
        return true;
    }

    public void reset(){
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    public boolean gameIsFinished(){
        return !hasErrors() && getStatus().equals(COMPLETE);
    }
}
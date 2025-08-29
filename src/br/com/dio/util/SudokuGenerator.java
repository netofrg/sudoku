package br.com.dio.util;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SudokuGenerator {

    private static final int BOARD_SIZE = 9;
    private static final int SUBGRID_SIZE = 3;
    private static final Random random = new Random();

    public static Board generateBoard(int difficulty) {
        int[][] solvedBoard = new int[BOARD_SIZE][BOARD_SIZE];
        
        // 1. Preenche os blocos 3x3 da diagonal para garantir uma solução
        fillDiagonal(solvedBoard);
        
        // 2. Usa backtracking para preencher o resto do tabuleiro
        solveBoard(solvedBoard);
        
        // 3. Cria a lista de espaços com base no tabuleiro resolvido
        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_SIZE; j++) {
                spaces.get(i).add(new Space(solvedBoard[i][j], true)); // Define todos como fixos inicialmente
            }
        }
        
        // 4. Remove números aleatórios para criar o jogo
        removeNumbers(spaces, difficulty);

        return new Board(spaces);
    }
    
    private static void fillDiagonal(int[][] board) {
        for (int i = 0; i < BOARD_SIZE; i += SUBGRID_SIZE) {
            fillBox(board, i, i);
        }
    }
    
    private static void fillBox(int[][] board, int row, int col) {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= BOARD_SIZE; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);

        int k = 0;
        for (int i = 0; i < SUBGRID_SIZE; i++) {
            for (int j = 0; j < SUBGRID_SIZE; j++) {
                board[row + i][col + j] = numbers.get(k++);
            }
        }
    }
    
    private static boolean solveBoard(int[][] board) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= BOARD_SIZE; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveBoard(board)) {
                                return true;
                            }
                            board[row][col] = 0; // Backtracking
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }
    
    private static boolean isValid(int[][] board, int row, int col, int num) {
        // Lógica de validação (linha, coluna, sub-grade)
        // (A mesma lógica que discutimos para a classe Board)
        // Implementar aqui...
        return false; // Por enquanto, para compilar.
    }
    
    private static void removeNumbers(List<List<Space>> spaces, int difficulty) {
        // Lógica para remover números baseada na dificuldade
        // Implementar aqui...
    }
}
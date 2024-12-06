/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.de.lab;

import java.io.*;
import java.util.*;
import java.util.Scanner;

class Ghost {
    private Player currentUser;
    private char[][] board;
    private int difficulty;
    private int mode;
    private final String FILE_PATH = "players.txt";
    private final int[] ghostCount = {8, 4, 2};
    private final Random random = new Random();
    private static final char EMPTY = '.';
    private int player1Good = 0, player1Bad = 0, player2Good = 0, player2Bad = 0;
    private int turnCount = 0;
    public static Scanner scanner = new Scanner(System.in);

    private Player[] players; 
    private int playerCount;  

    public Ghost() {
        this.board = new char[6][6];
        this.difficulty = 0; // Normal
        this.mode = 0; // Aleatorio
        this.players = new Player[100]; // Capacidad máxima inicial de 100 jugadores
        this.playerCount = 0;
        initializeBoard();
        loadPlayers();
    }

    private void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    public void registerPlayer(String username, String password) {
        if (isUserExists(username)) {
            System.out.println("El username ya existe. Intente con otro.");
            return;
        }
        if (playerCount >= players.length) {
            System.out.println("No se pueden registrar mas jugadores. Capacidad maxima alcanzada.");
            return;
        }
        players[playerCount++] = new Player(username, password);
        savePlayers();
        System.out.println("Jugador registrado exitosamente.");
    }

    public Player login(String username, String password) {
        for (int i = 0; i < playerCount; i++) {
            if (players[i].getUsername().equals(username) && players[i].getPassword().equals(password)) {
                currentUser = players[i];
                System.out.println("Inicio de sesion exitoso.");
                return currentUser;
            }
        }
        System.out.println("Credenciales incorrectas.");
        return null;
    }

    public void logout() {
        currentUser = null;
        System.out.println("Sesion cerrada.");
    }

    public Player getCurrentUser() {
        return currentUser;
    }

    public void deleteUser() {
        if (currentUser != null) {
            for (int i = 0; i < playerCount; i++) {
                if (players[i] == currentUser) {
                   
                    for (int j = i; j < playerCount - 1; j++) {
                        players[j] = players[j + 1];
                    }
                    players[--playerCount] = null; 
                    savePlayers();
                    System.out.println("Cuenta eliminada exitosamente.");
                    currentUser = null;
                    return;
                }
            }
        } else {
            System.out.println("No hay un usuario logueado.");
        }
    }

    public void showLast10Games() {
        if (currentUser != null) {
            System.out.println("Ultimos 10 juegos de " + currentUser.getUsername() + ":");
            for (String log : currentUser.getGameLogs()) {
                System.out.println("- " + log);
            }
        } else {
            System.out.println("No hay un usuario logueado.");
        }
    }

    public void showRanking() {
        Player[] sortedPlayers = Arrays.copyOf(players, playerCount);
        Arrays.sort(sortedPlayers, 0, playerCount, (p1, p2) -> Integer.compare(p2.getPoints(), p1.getPoints()));

        System.out.println("Ranking de jugadores:");
        for (int i = 0; i < playerCount; i++) {
            System.out.println(sortedPlayers[i].getUsername() + " - Puntos: " + sortedPlayers[i].getPoints());
        }
    }

public void playGame(String opponentUsername) {
    Player opponent = getPlayerByUsername(opponentUsername);
    if (opponent == null) {
        System.out.println("El jugador oponente no existe.");
        return;
    }

    
    setupGhosts();

    
    boolean gameActive = true;
    int turn = 0; 
    printBoard(turn == 0); 

    while (gameActive) {
        Player currentPlayer = (turn == 0) ? currentUser : opponent;
        System.out.println("\nTurno de: " + currentPlayer.getUsername());
        
        
        gameActive = takeTurn(currentPlayer, turn == 0);

       
        printBoard(turn == 0);

        
        turn = 1 - turn;
    }
}
    private void setupGhosts() {
        int count = ghostCount[difficulty];
        System.out.println("Configurando fantasmas...");
        if (mode == 0) {
            randomSetup(count, true);
            randomSetup(count, false);
        } else {
            manualSetup(count, true);
            manualSetup(count, false);
        }
    }

    private void randomSetup(int count, boolean isPlayer1) {
        for (int i = 0; i < count; i++) {
            char type = (i < count / 2) ? (isPlayer1 ? 'B' : 'b') : (isPlayer1 ? 'M' : 'm');
            placeGhostRandomly(type, isPlayer1);
        }
    }

    private void placeGhostRandomly(char type, boolean isPlayer1) {
        int row, col;
        do {
            row = random.nextInt(2) + (isPlayer1 ? 0 : 4);
            col = random.nextInt(6);
        } while (board[row][col] != EMPTY || isExit(row, col));
        board[row][col] = type;
    }

    private boolean isExit(int row, int col) {
        return (row == 0 && (col == 0 || col == 5)) || (row == 5 && (col == 0 || col == 5));
    }

    private void manualSetup(int count, boolean isPlayer1) {
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < count; i++) {
            char type = (i < count / 2) ? (isPlayer1 ? 'B' : 'b') : (isPlayer1 ? 'M' : 'm');
            boolean placed = false;
            while (!placed) {
                System.out.print("Ingrese fila y columna para el fantasma " + (type == 'B' || type == 'b' ? "bueno" : "malo") + ": ");
                int row = scanner.nextInt();
                int col = scanner.nextInt();
                if (row >= (isPlayer1 ? 0 : 4) && row < (isPlayer1 ? 2 : 6) && col >= 0 && col < 6 && board[row][col] == EMPTY && !isExit(row, col)) {
                    board[row][col] = type;
                    placed = true;
                } else {
                    System.out.println("Posicion invalida. Intente de nuevo.");
                }
            }
        }
    }

   public void printBoard(boolean isPlayer1) {
    System.out.println("\nTablero actualizado:");
    for (int i = 0; i < board.length; i++) {
        for (int j = 0; j < board[i].length; j++) {
            char cell = board[i][j];
            if ((cell == 'b' || cell == 'm') && isPlayer1) cell = 'X'; 
            if ((cell == 'B' || cell == 'M') && !isPlayer1) cell = 'X'; 
            System.out.print(cell + " ");
        }
        System.out.println();
    }
}

    private void loadPlayers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Player player = Player.fromString(line);
                if (player != null) {
                    players[playerCount++] = player;
                }
            }
        } catch (IOException e) {
            System.out.println("Error al cargar jugadores.");
        }
    }

    private void savePlayers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (int i = 0; i < playerCount; i++) {
                writer.write(players[i].toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error al guardar jugadores.");
        }
    }

    boolean isUserExists(String username) {
        for (int i = 0; i < playerCount; i++) {
            if (players[i].getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private Player getPlayerByUsername(String username) {
        for (int i = 0; i < playerCount; i++) {
            if (players[i].getUsername().equals(username)) {
                return players[i];
            }
        }
        return null;
    }
    
    private int[] askCoordinates(Scanner scanner, String prompt) {
    int[] coordinates = new int[2]; 
    boolean validInput = false;

    while (!validInput) {
        try {
            
            System.out.print(prompt + " (Ingrese la fila entre 0 y 5): ");
            coordinates[0] = scanner.nextInt();

           
            System.out.print(prompt + " (Ingrese la columna entre 0 y 5): ");
            coordinates[1] = scanner.nextInt();

            
            if (coordinates[0] >= 0 && coordinates[0] < 6 && coordinates[1] >= 0 && coordinates[1] < 6) {
                validInput = true; 
            } else {
                System.out.println("Coordenadas fuera del rango. Intente nuevamente (0-5).");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada no valida. Ingrese numeros enteros.");
            scanner.nextLine(); 
        }
    }

    return coordinates;
}

   private boolean takeTurn(Player currentPlayer, boolean isPlayer1) {
    Scanner scanner = new Scanner(System.in);

    
    int[] source = askCoordinates(scanner, "Seleccione la posición del fantasma a mover");

    
    if (source[0] == -1 && source[1] == -1) {
        System.out.print("Esta seguro que desea retirarse? (Si/No): ");
        char confirm = scanner.next().toUpperCase().charAt(0);
        if (confirm == 'S') {
            System.out.println(currentPlayer.getUsername() + " se ha retirado del juego.");
            return false; 
        } else {
            return true; 
        }
    }

    
    if (!isValidMove(source[0], source[1], isPlayer1)) {
        System.out.println("La selección no es valida. Intente nuevamente.");
        return true; 
    }

    
    int[] destination = askCoordinates(scanner, "Ingrese fila y columna destino:");
    if (!processMove(source[0], source[1], destination[0], destination[1], isPlayer1)) {
        System.out.println("Movimiento no valido. Intente nuevamente.");
        return true; 
    }

    
    turnCount++;

    
    if (turnCount > 1) {
        return checkWinConditions();
    }

    return true;
}


    
   private boolean isValidMove(int row, int col, boolean isPlayer1) {
    if (row < 0 || row >= 6 || col < 0 || col >= 6 || board[row][col] == EMPTY) {
        System.out.println("Movimiento invalido: fuera del rango o casilla vacia.");
        return false; 
    }
    char ghost = board[row][col];
    boolean valid = isPlayer1 ? ghost == 'B' || ghost == 'M' : ghost == 'b' || ghost == 'm';
    if (!valid) {
        System.out.println("Movimiento invalido: No puedes mover el fantasma seleccionado.");
    }
    return valid;
}
    
  private boolean processMove(int row, int col, int destRow, int destCol, boolean isPlayer1) {
    
    if (Math.abs(destRow - row) > 1 || Math.abs(destCol - col) > 1 || destRow < 0 || destRow >= 6 || destCol < 0 || destCol >= 6) {
        return false;
    }

    char targetGhost = board[destRow][destCol];
    char movingGhost = board[row][col];

    
    if (targetGhost != EMPTY && (isPlayer1 ? targetGhost == 'B' || targetGhost == 'M' : targetGhost == 'b' || targetGhost == 'm')) {
        return false;
    }

   
    if (targetGhost != EMPTY) {
        System.out.println("Te has comido un fantasma enemigo.");
        if (isPlayer1) {
            if (targetGhost == 'b') {
                player2Good--; 
            } else {
                player2Bad--; 
            }
        } else {
            if (targetGhost == 'B') {
                player1Good--; 
            } else {
                player1Bad--; 
            }
        }
    }

    
    board[destRow][destCol] = movingGhost;
    board[row][col] = EMPTY;

   
    if (isExit(destRow, destCol)) {
    if ((movingGhost == 'B' && isPlayer1) || (movingGhost == 'b' && !isPlayer1)) {
        System.out.println("Un fantasma bueno ha escapado por la salida!");
        return false; 
    }
    if ((movingGhost == 'M' && isPlayer1) || (movingGhost == 'm' && !isPlayer1)) {
        System.out.println("Un fantasma malo no puede salir del castillo.");
         
    }
}
        return false;
  }

private boolean checkWinConditions() {
   
    if (player1Good == 0) {
        System.out.println("¡" + currentUser.getUsername() + " gana porque capturó todos los fantasmas buenos del oponente!");
        return false; // Juego termina
    }
    if (player2Good == 0) {
        System.out.println("El oponente gana porque capturó todos tus fantasmas buenos.");
        return false; 
    }

    
    if (player1Bad == 0) {
        System.out.println("El oponente gana porque perdiste todos tus fantasmas malos.");
        return false; 
    }
    if (player2Bad == 0) {
        System.out.println(currentUser.getUsername() + " gana porque el oponente perdió todos sus fantasmas malos.");
        return false; 
    }

    
    for (int col = 0; col < 6; col++) {
        if (board[5][col] == 'B') { 
            System.out.println("¡" + currentUser.getUsername() + " gana porque un fantasma bueno escapó por la salida!");
            return false; 
        }
        if (board[0][col] == 'b') { 
            System.out.println("El oponente gana porque un fantasma bueno escapo por la salida.");
            return false; 
        }
    }

    return true; 
}

    void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    void setMode(int mode) {
        this.mode = mode;
    }

    void changePassword(String newPassword) {
        throw new UnsupportedOperationException(""); 
    }
}

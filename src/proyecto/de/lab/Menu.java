/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto.de.lab;


import java.util.Scanner;

public class Menu {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Ghost ghost = new Ghost(); 
        int option;

        do {
            System.out.println("\nMenu de Inicio:");
            System.out.println("1. Login");
            System.out.println("2. Crear Player");
            System.out.println("3. Salir");
            System.out.print("Seleccione una opcion: ");
            option = scanner.nextInt();
            scanner.nextLine(); 

            switch (option) {
                case 1 -> {
                    System.out.print("Username: ");
                    String username = scanner.nextLine();
                    System.out.print("Password: ");
                    String password = scanner.nextLine();
                    if (ghost.login(username, password) != null) {
                        showMainMenu(ghost, scanner); 
                    } else {
                        System.out.println("Credenciales incorrectas. Intente nuevamente.");
                    }
                }
                case 2 -> {
                    System.out.print("Ingrese un username: ");
                    String newUsername = scanner.nextLine();
                    System.out.print("Ingrese un password: ");
                    String newPassword = scanner.nextLine();
                    ghost.registerPlayer(newUsername, newPassword);
                }
                case 3 -> System.out.println("Saliendo del programa. Hasta luego!");
                default -> System.out.println("Opcion no valida.");
            }
        } while (option != 3);

        scanner.close();
    }

    static void showMainMenu(Ghost ghost, Scanner scanner) {
        int option;
        do {
            System.out.println("\nMenu Principal:");
            System.out.println("1. Jugar Ghosts");
            System.out.println("2. Reportes");
            System.out.println("3. Mi Perfil");
            System.out.println("4. Configuracion");
            System.out.println("5. Cerrar Sesion");
            System.out.print("Seleccione una opcion: ");
            option = scanner.nextInt();
            scanner.nextLine(); 

            switch (option) {
                case 1 -> playGhosts(ghost, scanner);
                case 2 -> showReports(ghost, scanner);
                case 3 -> showProfile(ghost, scanner);
                case 4 -> configureSettings(ghost, scanner);
                case 5 -> {
                    ghost.logout();
                    System.out.println("Sesion cerrada. Regresando al menu de inicio...");
                }
                default -> System.out.println("Opcion no valida.");
            }
        } while (option != 5);
    }

    static void playGhosts(Ghost ghost, Scanner scanner) {
        System.out.print("Ingrese el username del jugador 2: ");
        String opponentUsername = scanner.nextLine();

        if (ghost.isUserExists(opponentUsername)) {
            System.out.println("Iniciando el juego de Ghosts...");
            ghost.playGame(opponentUsername);
        } else {
            System.out.println("El usuario ingresado no existe. Intente nuevamente.");
        }
    }

    static void showReports(Ghost ghost, Scanner scanner) {
        int option;
        do {
            System.out.println("\nReportes:");
            System.out.println("1. Descripcion de mis ultimos 10 juegos");
            System.out.println("2. Ranking de jugadores");
            System.out.println("3. Regresar");
            System.out.print("Seleccione una opcion: ");
            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> ghost.showLast10Games();
                case 2 -> ghost.showRanking();
                case 3 -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opcion no valida.");
            }
        } while (option != 3);
    }

    static void showProfile(Ghost ghost, Scanner scanner) {
        Player currentUser = ghost.getCurrentUser();
        if (currentUser != null) {
            int option;
            do {
                System.out.println("\nMi Perfil:");
                System.out.println("1. Ver Mis Datos");
                System.out.println("2. Cambiar Contraseña");
                System.out.println("3. Eliminar Cuenta");
                System.out.println("4. Regresar");
                System.out.print("Seleccione una opcion: ");
                option = scanner.nextInt();
                scanner.nextLine(); 

                switch (option) {
                    case 1 -> {
                        System.out.println("Datos del Usuario:");
                        System.out.println("Username: " + currentUser.getUsername());
                        System.out.println("Puntos: " + currentUser.getPoints());
                    }
                    case 2 -> {
                        System.out.print("Ingrese la nueva contraseña: ");
                        String newPassword = scanner.nextLine();
                        ghost.changePassword(newPassword);
                        System.out.println("Contraseña actualizada.");
                    }
                    case 3 -> {
                        System.out.print("¿Esta seguro que desea eliminar su cuenta? (S/N): ");
                        char confirm = scanner.nextLine().toUpperCase().charAt(0);
                        if (confirm == 'S') {
                            ghost.deleteUser();
                            System.out.println("Cuenta eliminada exitosamente.");
                            return; 
                        } else {
                            System.out.println("Operacion cancelada.");
                        }
                    }
                    case 4 -> System.out.println("Regresando al menu principal...");
                    default -> System.out.println("Opcion no valida.");
                }
            } while (option != 4);
        } else {
            System.out.println("No hay un usuario logueado.");
        }
    }

    static void configureSettings(Ghost ghost, Scanner scanner) {
        int option;
        do {
            System.out.println("\nConfiguracion:");
            System.out.println("1. Seleccionar dificultad");
            System.out.println("2. Seleccionar modo");
            System.out.println("3. Regresar");
            System.out.print("Seleccione una opcion: ");
            option = scanner.nextInt();

            switch (option) {
                case 1 -> {
                    System.out.print("Elija dificultad (0: Normal, 1: Experto, 2: Genio): ");
                    int difficulty = scanner.nextInt();
                    ghost.setDifficulty(difficulty);
                    System.out.println("Dificultad actualizada.");
                }
                case 2 -> {
                    System.out.print("Elija modo (0: Aleatorio, 1: Manual): ");
                    int mode = scanner.nextInt();
                    ghost.setMode(mode);
                    System.out.println("Modo actualizado.");
                }
                case 3 -> System.out.println("Regresando al menu principal...");
                default -> System.out.println("Opcion no valida.");
            }
        } while (option != 3);
    }
}

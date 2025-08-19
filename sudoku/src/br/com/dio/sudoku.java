package br.com.dio;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

import java.util.*;
import java.util.stream.Stream;

import static br.com.dio.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

public class Main {
    private final static Scanner scanner = new Scanner(System.in);
    private static Board board;
    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        final var positions = Stream.of(args)
                .collect(toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                ));
        while (true) {
            System.out.println("\n🎲 MENU SUDOKU 🎲");
            System.out.println("1️⃣  Novo jogo");
            System.out.println("2️⃣  Inserir número");
            System.out.println("3️⃣  Remover número");
            System.out.println("4️⃣  Mostrar tabuleiro");
            System.out.println("5️⃣  Status do jogo");
            System.out.println("6️⃣  Resetar jogo");
            System.out.println("7️⃣  Finalizar jogo");
            System.out.println("8️⃣  Sair");

            var option = scanner.nextInt();
            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> inputNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> exit();
                default -> System.out.println("⚠️ Opção inválida!");
            }
        }
    }

    private static void startGame(final Map<String, String> positions) {
        if (nonNull(board)) {
            System.out.println("⚠️ O jogo já foi iniciado!");
            return;
        }
        System.out.print("🔄 Iniciando jogo");
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            try { Thread.sleep(500); } catch (InterruptedException ignored) {}
        }
        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                var config = positions.get("%s,%s".formatted(i, j)).split(",");
                spaces.get(i).add(new Space(Integer.parseInt(config[0]), Boolean.parseBoolean(config[1])));
            }
        }
        board = new Board(spaces);
        System.out.println("\n✅ Jogo iniciado! Boa sorte!");
    }

    private static void inputNumber() {
        if (checkBoardNull()) return;
        var col = askNumber("📍 Coluna (0-8):", 0, 8);
        var row = askNumber("📍 Linha (0-8):", 0, 8);
        var value = askNumber("✏️ Número (1-9):", 1, 9);
        if (!board.changeValue(col, row, value))
            System.out.printf("❌ [%s,%s] é posição fixa!\n", col, row);
        else
            System.out.printf("✅ Inserido %s em [%s,%s]\n", value, col, row);
    }

    private static void removeNumber() {
        if (checkBoardNull()) return;
        var col = askNumber("📍 Coluna (0-8):", 0, 8);
        var row = askNumber("📍 Linha (0-8):", 0, 8);
        if (!board.clearValue(col, row))
            System.out.printf("❌ [%s,%s] é posição fixa!\n", col, row);
        else
            System.out.printf("🧹 Valor removido em [%s,%s]\n", col, row);
    }

    private static void showCurrentGame() {
        if (checkBoardNull()) return;
        var args = new Object[81];
        var pos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++)
            for (var col : board.getSpaces())
                args[pos++] = " " + (isNull(col.get(i).getActual()) ? " " : col.get(i).getActual());
        System.out.println("\n🗂️ Tabuleiro atual:");
        System.out.printf((BOARD_TEMPLATE) + "\n", args);
    }

    private static void showGameStatus() {
        if (checkBoardNull()) return;
        System.out.printf("📊 Status: %s\n", board.getStatus().getLabel());
        System.out.println(board.hasErrors() ? "❌ Contém erros" : "✅ Sem erros até aqui");
    }

    private static void clearGame() {
        if (checkBoardNull()) return;
        System.out.println("⚠️ Deseja resetar o jogo? (sim/não)");
        var confirm = scanner.next();
        if (confirm.equalsIgnoreCase("sim")) {
            board.reset();
            System.out.println("🧹 Jogo resetado!");
        }
    }

    private static void finishGame() {
        if (checkBoardNull()) return;
        if (board.gameIsFinished()) {
            System.out.println("🎉 Parabéns! Você venceu o Sudoku!");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()) {
            System.out.println("❌ Existem erros no tabuleiro!");
        } else {
            System.out.println("⏳ Ainda faltam espaços para preencher!");
        }
    }

    private static void exit() {
        System.out.println("👋 Até mais!");
        System.exit(0);
    }

    private static int askNumber(String msg, int min, int max) {
        System.out.println(msg);
        var n = scanner.nextInt();
        while (n < min || n > max) {
            System.out.printf("Digite um número entre %s e %s:\n", min, max);
            n = scanner.nextInt();
        }
        return n;
    }

    private static boolean checkBoardNull() {
        if (isNull(board)) {
            System.out.println("⚠️ Jogo ainda não iniciado!");
            return true;
        }
        return false;
    }
}

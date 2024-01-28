package org.ylab.infrastructure.in;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Класс для чтения данных из консоли,
 * предоставляет методы для считывания строк, целых чисел и вещественных чисел
 */
public class ConsoleReader {

    /**
     * Объект BufferedReader для чтения данных из консоли
     */
    private BufferedReader reader;

    /**
     * Конструктор класса, инициализирующий объект BufferedReader для чтения данных из консоли
     */
    public ConsoleReader() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * Метод для считывания строки из консоли
     * @return Считанная строка
     * @throws IOException Если произошла ошибка ввода/вывода при чтении строки
     */
    public String readString() throws IOException {
        return reader.readLine();
    }

    /**
     * Метод для считывания целого числа из консоли
     * @return Считанное целое число
     * @throws IOException Если произошла ошибка ввода/вывода при чтении числа
     * @throws RuntimeException Если введенная строка не может быть преобразована в целое число
     */
    public int readInt() throws IOException {
        try {
            return Integer.parseInt(reader.readLine());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid input");
        }
    }

    /**
     * Метод для считывания вещественного числа из консоли
     * @return Считанное вещественное число
     * @throws IOException Если произошла ошибка ввода/вывода при чтении числа
     * @throws RuntimeException Если введенная строка не может быть преобразована в вещественное число
     */
    public double readDouble() throws IOException {
        try {
            return Double.parseDouble(reader.readLine());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid input");
        }
    }
}

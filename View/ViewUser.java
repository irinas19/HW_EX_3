package view;

import controller.UserController;
import exception.DateExc;
import model.User;

import java.time.LocalDate;
import java.util.Scanner;

public class ViewUser {
    private static final byte NUMBER_OF_INPUT_DATA_FIELDS = 6;   // Число входных данных, разделённых пробелом
    private static final byte NUMBER_OF_DATE_DATA_FIELDS = 3;   // Число полей даты, разделенных точкой (dd.mm.yyyy)
    private static final byte DAY_FIELD_SIZE = 2;    // Размерность поля день (dd)
    private static final byte MONTH_FIELD_SIZE = 2;    // Размерность поля месяц (mm)
    private static final byte YEAR_FIELD_SIZE = 4;    // Размерность поля год (yyyy)
    private UserController userController;

    public ViewUser(UserController userController) {
        this.userController = userController;
    }

    public void run() {
        while (true) {
            System.out.println("Формат ввода данных пользователя: ");
            System.out.println("Фамилия Имя Отчество дата_рождения номер_телефона пол");
            System.out.println("Формат даты рождения: dd.mm.yyyy");
            System.out.println("Формат номера телефона: целое беззнаковое число без форматирования");
            System.out.println("Формат пола: символ латиницей f или m");
            Scanner iScanner = new Scanner(System.in);
            System.out.println("Введите данные пользователя:");
            String inputString = iScanner.nextLine();
            String[] inputData = inputString.split(" ");
            int errorCode = checkInputString(inputData);
            if (errorCode == -1) {
                System.out.println("Ошибка! Входных данных меньше, чем должно быть!");
            } else if (errorCode == -2) {
                System.out.println("Ошибка! Входных данных больше, чем должно быть!");
            } else {
                try {
                    this.checkBirthDate(inputData[3]);
                    this.checkPhone(inputData[4]);
                    this.checkSex(inputData[5]);
                    this.userController.saveUser(new User(inputData[0], inputData[1], inputData[2], inputData[3], Long.parseLong(inputData[4]), inputData[5].charAt(0)));
                    System.out.println("Создать нового пользователя (y/n)?: ");
                    inputString = iScanner.nextLine();
                    if (!inputString.equalsIgnoreCase("y")) {
                        return;
                    }
                } catch (DateExc e) {
                    System.out.println(e.getMessage());
                }
            }

        }
    }

    public Integer checkInputString(String[] inputData) {
        int errorCode = 0;      // 0 - OK, -1 - входных данных меньше, чем должно быть, -2 - входных данных больше, чем должно быть
        if (inputData.length < NUMBER_OF_INPUT_DATA_FIELDS) {
            errorCode = -1;
        } else if (inputData.length > NUMBER_OF_INPUT_DATA_FIELDS) {
            errorCode = -2;
        }
        return errorCode;
    }

    public void checkBirthDate(String birthDateString) throws DateExc {
        String[] birthDateData = birthDateString.split("\\.");
        if (birthDateData.length != NUMBER_OF_DATE_DATA_FIELDS) {
            throw new DateExc("Ошибка! Неверный формат даты (dd.mm.yyyy): количество полей, разделённых точкой (.), не равно трём!");
        }
        checkDay(birthDateData[0]);
        checkMonth(birthDateData[1]);
        checkYear(birthDateData[2]);
    }

    public void checkPhone(String phone) throws DateExc {
        if (!isNumeric(phone)) {
            throw new DateExc("Ошибка! Неверный формат телефонного номера: номер телефона должен состоять только из цифр");
        }
    }

    public void checkSex(String sex) throws DateExc {
        if (!sex.equalsIgnoreCase("f") && !sex.equalsIgnoreCase("m")) {
            throw new DateExc("Ошибка! Неверный формат пола: должен быть 'f' или 'm'");
        }
    }

    public void checkDay(String day) throws DateExc {
        if (day.length() != DAY_FIELD_SIZE) {
            throw new DateExc("Ошибка! Неверный формат даты (dd.mm.yyyy): длина поля день (dd) не равна двум!");
        }
        if (!isNumeric(day)) {
            throw new DateExc("Ошибка! Неверный формат даты (dd.mm.yyyy): поле день (dd) должно быть числом!");
        }
        if (Integer.parseInt(day) < 1 || Integer.parseInt(day) > 31) {
            throw new DateExc("Ошибка! Неверный формат даты (dd.mm.yyyy): поле день (dd) должно быть от 01 до 31");
        }
    }
    public void checkMonth(String month) throws DateExc {
        if (month.length() != MONTH_FIELD_SIZE) {
            throw new DateExc("Ошибка! Неверный формат даты (dd.mm.yyyy): длина поля месяц (mm) не равна двум!");
        }
        if (!isNumeric(month)) {
            throw new DateExc("Ошибка! Неверный формат даты (dd.mm.yyyy): поле месяц (mm) должно быть числом!");
        }
        if (Integer.parseInt(month) < 1 || Integer.parseInt(month) > 12) {
            throw new DateExc("Ошибка! Неверный формат даты (dd.mm.yyyy): поле месяц (mm) должно быть от 01 до 12");
        }
    }

    public void checkYear(String year) throws DateExc {
        if (year.length() != YEAR_FIELD_SIZE) {
            throw new DateExc("Ошибка! Неверный формат даты (dd.mm.yyyy): длина поля год (yyyy) не равна двум!");
        }
        if (!isNumeric(year)) {
            throw new DateExc("Ошибка! Неверный формат даты (dd.mm.yyyy): поле год (yyyy) должно быть числом!");
        }
        LocalDate localDate = LocalDate.now();
        if (Integer.parseInt(year) > localDate.getYear()) {
            throw new DateExc("Ошибка! Неверный формат даты (dd.mm.yyyy): поле год (yyyy) должно быть от 0000 до " + localDate.getYear());
        }
    }

    public Boolean isNumeric(String str) {
        for (char symbol : str.toCharArray()) {
            if (!Character.isDigit(symbol)) {
                return false;
            }
        }
        return true;
    }
}
/** Калькулятор арабо-римский
 * Автор: Константин Лозовский
 * Создан: 04.08.2022
 * Цель: тестовая задача для kataacademy
 * Описание:
 * - считает операции (a + - * / b) над целыми от 1 до 10 арабскими или римскими (без смешения).
 *   Достаточно легко настраивается для работы с (римскими) числами до 100 и более, и с дугими операциями.
 * - результат - целое число без округления.
 * **************************************************
 * Для настройки прерывания программы по ошибке ввода
 * установить boolean breakOnError boolean = true в строке 26
 * **************************************************
 * Для проверки правилности работы программы имеется тестовый метод debugResult(),
 * для его запуска поставить true в сторке 31.
 */

import java.util.Scanner;

public class Calculator_Arab_Roman {

    public static void main( String[] args) {

        // ************************************************
        // true - прерывать программу при ошибочном вводе
        // false - возвращать управление на консоль
        boolean breakOnError = true;

        // *************************************************
        // false - нормальная работа
        // true - тестовый режим, задаёт наиболее характерные вырианты и выводит их результаты
        if ( false ) {
            debugResult();
            return;
        }

        System.out.println("Калькулятор арабо-римский.");
        System.out.println("Вводите выражения из двух чисел от 1 до 10.");
        System.out.println("Например 5/3, IX-VI, 4+2, I-III, ... :");
        Scanner in = new Scanner(System.in);

        // ввод строки матем. выражения с расчётом результата
        while (true) {
            String result = "";

            // Ввод строки, например II+VI или 5-9
            System.out.print("\n>");
            String input = in.nextLine();

            // Завершение работы - введено пусто
            if ( input.length() < 1 ) {
                System.out.println("До свидания!");
                break;
            }

            // Расчёт результата
            result = calc( input);

            // Вывод результата / ошибки
            if ( result.indexOf( "Ошибка") >= 0 ) {     // Ошибка
                System.out.print( "  " + result);
                if ( breakOnError ) {
                    break;
                }
            } else {                                    // Выдаём результат
                System.out.print( " = " + result);
            }
            // System.out.printf("Your number: %d \n", num);

        }   // << while true
        in.close();
    }   // << main()

    // calc(): >> строку с матем. выражением; << строку result: результат вычисления или текст ошибки
    private static String calc( String input) {
        String result ="";
        String sNum1 = "";
        String sNum2 = "";
        String oper =  "";
        String ch =    "";
        int   num1 = 0;
        int   num2 = 0;

        String arabChars  = "0123456789";
        String romanChars = "IVX";
        String operChars  = "+-*/";
        // Считаем выражение арабским, если 1-й символ 0..9
        boolean isArab = ( arabChars.indexOf( input.substring(0,1) ) >= 0 );
        String numChars = isArab ? arabChars : romanChars;
        String allChars = numChars + operChars;

        // Выделяем числа и операцию
        for (int i = 0; i < input.length(); i++) {
            ch = input.substring( i, i+1);
            if ( sNum1 == "" && numChars.indexOf( ch) < 0) {      // недопустимый символ
                return "Ошибка! Недопустимый символ "+ ch;
            }
            if ( allChars.indexOf( ch) < 0) {      // недопустимый символ
                return "Ошибка! Недопустимый символ "+ ch;
            }
            if ( operChars.indexOf( ch ) >= 0 ) { oper += ch; }
            else if (oper == "") {               sNum1 += ch; }
            else {                               sNum2 += ch; }

        }   // << for (int i = 0; i < input.length(); i++)

        // Проверка чисел и операции
        // System.out.println( sNum1+"."+oper+"."+sNum2);  // ***
        if (sNum1 == "" || sNum2 == "" || oper == "") { return "Ошибка! Надо a +-*/ b"; }
        if (oper.length() > 1) { return "Ошибка! Знаки операций";}

        // Римские в арабские
        if (isArab) {
            num1 = Integer.valueOf( sNum1);
            num2 = Integer.valueOf( sNum2);
        } else {
            num1 = romanToArab( sNum1 );
            num2 = romanToArab( sNum2 );
            if ( num1 == -1 ) { return "Ошибка! Число "+ sNum1; }
            if ( num2 == -1 ) { return "Ошибка! Число "+ sNum2; }
        }

        if (num1 > 10 || num2 > 10) { return "Ошибка! Число больше 10";}

        // Расчёт
        int iResult = 0;
        if      ( oper.equals("+") ) {iResult = num1 + num2; }
        else if ( oper.equals("-") ) {iResult = num1 - num2; }
        else if ( oper.equals("*") ) {iResult = num1 * num2; }
        else if ( oper.equals("/") ) {
             if ( num2 != 0)         {iResult = num1 / num2; }
             else { return "Ошибка! Деление на ноль";}   }
        else      { return "Ошибка! Недопустимая операция...";}

        if ( isArab) {
            result = Integer.toString( iResult);
        } else {
            result = arabToRoman( iResult);
            if (result=="") { return "Ошибка! Римское меньше 1";}
        }


        return result;
    }   // << calc()

    // Преобразование чисел в латинские: 6 >> "VI" или "" ошибка
    private static String arabToRoman (int arab) {
        final String[] romans = {"_",
                "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X",
                "XI", "XII", "XIII", "XIV", "XV", "XVI", "XVII", "XVIII", "XIX",
                "XX", "XXI", "XXII", "XXIII", "XXIV", "XXV", "XXVI", "XXVII", "XXVIII", "XXIX",
                "XXX", "XXXI", "XXXII", "XXXIII", "XXXIV", "XXXV", "XXXVI", "XXXVII", "XXXVIII", "XXXIX",
                "XL","XLI", "XLII", "XLIII", "XLIV", "XLV", "XLVI", "XLVII", "XLVIII", "XLIX",
                "L", "LI", "LII", "LIII", "LIV", "LV", "LVI", "LVII", "LVIII", "LIX",
                "LX", "LXI", "LXII", "LXIII", "LXIV", "LXV", "LXVI", "LXVII", "LXVIII", "LXIX",
                "LXX", "LXXI", "LXXII", "LXXIII", "LXXIV", "LXXV", "LXXVI", "LXXVII", "LXXVIII", "LXXIX",
                "LXXX", "LXXXI", "LXXXII", "LXXXIII", "LXXXIV", "LXXXV", "LXXXVI", "LXXXVII", "LXXXVIII", "LXXXIX",
                "XC", "XCI", "XCII", "XCIII", "XCIV", "XCV", "XCVI", "XCVII", "XCVIII", "XCIX",
                "C"
        };
        String roman = "";
        if ( arab >= 1 &&  arab <= 100 ) { roman = romans[ arab]; }
        return roman;
    }	// << arabToRoman (int arab)

    // "IX" >> 9 или -1 ошибка
    // ограниченный по велмчмне числа вариант
    private static int romanToArab (String roman) {
        roman = roman.trim().toUpperCase();
        if (roman.equals(	"I")) {               	    return 1;
        } else if (roman.equals("II")) {                return 2;
        } else if (roman.equals("III")) {               return 3;
        } else if (roman.equals("IV")) {                return 4;
        } else if (roman.equals("V")) {                 return 5;
        } else if (roman.equals("VI")) {                return 6;
        } else if (roman.equals("VII")) {               return 7;
        } else if (roman.equals("VIII")) {              return 8;
        } else if (roman.equals("IX")) {                return 9;
        } else if (roman.equals("X")) {                 return 10;
        } else {                                        return -1;
        }
    }	// << romanToArab (String roman)

    // отладочный тест
    private static void debugResult () {
        System.out.println( "Ok     Вход    >   Результат   >    Тест (эталон)" );
        System.out.println( "-------------------------------------------------" );
        String inp, res, test;
        inp= "5/3";    test= "1";       res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "5+9";    test= "14";      res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "5-9";    test= "-4";      res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "5*9";    test= "45";      res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "V-III";  test= "II";      res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "v/iii";  test= "I";       res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "V+VIII"; test= "XIII";    res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "V*X";    test= "L";       res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "5/0";    test= "Ошибка! Деление на ноль"; res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "+5+0";   test= "Ошибка!___"; res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "11*9";   test= "Ошибка!___"; res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "+1-2";   test= "Ошибка!___"; res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "1//5";   test= "Ошибка!___"; res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "v-VIII"; test= "Ошибка!___"; res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "XV-V";   test= "Ошибка!___"; res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "V-1";    test= "Ошибка!___"; res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
        inp= "1-V";    test= "Ошибка!___"; res= calc( inp); debugResult_( inp, res, test, res.equals( test) ? " + " : " ? ");
    }	// << debugResult ()

    // отладочный тест 1 строки
    private static void debugResult_ ( String inp, String res, String test, String ok) {
        System.out.printf( "%s %10s > %10s > %10s \n", ok, inp, res, test);
    }	// << debugResult_ ()

}

// "IX" >> 9 или -1 ошибка
// Универсальный авриант
//    private static int romanToArab (String roman) {
//        roman = roman.trim().toUpperCase();
//        String[] chars = {"M", "D", "C", "L", "X", "V", "I"};
//        int[]    nums  = {1000, 500, 100, 50,  10,  5,   1};
//        int arab = 0, n;
//        String c, c1
//        int maxValue = 0;
//
//        for ( int i=0; i<= chars.length(); i++) {
//            c= roman.substring(i, i+1);
//            n= nums[ ]
//            if ( i < chars.length())
//            let val = vp.value
//            let key = vp.key
//            if (romeCh.range(of: key) != nil) {
//
//                maxValue = max(val, maxValue)
//                result += val == maxValue ? val : -val
//            }
//        }
//    }	// << romanToArab (String roman)

//    public static void expt( String err) {
//        try{
//            throw new Exception( err );
//        }
//        catch(Exception ex){
//            System.out.println(ex.getMessage());
//        }
//    }

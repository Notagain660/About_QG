package com.dorm.utils;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;


public class ScanfNumberResult {

    public static int getInt(Scanner scanner, IntPredicate predicate) {
        while(true){
            try{
                int number = scanner.nextInt();
                scanner.nextLine();
                if(predicate.test(number)) {
                    return number;
                }else{
                    System.out.println("输入无效，请重新输入：");
                }
            }catch(InputMismatchException e){
                System.out.println("输入无效，请重新输入：");
                scanner.nextLine();
            }
        }
    }

    public static long getValidLong(Scanner scanner, String errMessage, LongPredicate predicate) {

        while(true){
            try{
                long value = getLong(scanner);
                scanner.nextLine();
                if(predicate.test(value)) {
                    return value;
                }else{
                    System.out.println(errMessage);
                    System.out.println("请重新输入：");
                }
            }catch(RuntimeException e){
                System.err.println(e.getMessage());
                System.out.println("系统繁忙，请稍后再试。");
            }
        }

    }

    public static long getLong(Scanner scanner) {
        while(true){
            try{
                long number = scanner.nextLong();
                scanner.nextLine();
                return number;
            }catch(InputMismatchException e){
                System.out.println("输入无效，请重新输入：");
                scanner.nextLine();
            }
        }
    }

    public static String getString(Scanner scanner, Predicate<String> predicate) {
        while(true){
            try{
                String string = scanner.nextLine();
                if(predicate.test(string)) {
                    return string;
                }else {
                    System.out.println("学号不符合格式，请重新输入：");
                }
            }catch(InputMismatchException e){
                System.out.println("输入无效，请重新输入：");
                scanner.nextLine();
            }
        }
    }

}

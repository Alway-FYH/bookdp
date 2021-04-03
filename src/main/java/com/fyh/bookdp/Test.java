package com.fyh.bookdp;

public class Test {
    public static void main(String[] args) {
        try {

            System.out.println(10/0);
            System.out.println(121);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com;

import java.util.Scanner;

public class Image {
    public static void main(String[] args) {
        //ImageCollection coll = new ImageCollection("./res/", ".png");
        Scanner in = new Scanner(System.in);
        int size = 0;
        ImageCollection collection = null;
        while (size == 0) {
            System.out.println("Input a name of path with images\nExample: ./res/");
            String path = in.nextLine();
            System.out.println("Input a image format\nExample: .png");
            String format = in.nextLine();
            collection = new ImageCollection(path, format);
            size = collection.data.size();
            if (size == 0){
                System.out.println("There is no images in this path");
            }
        }
        while (true){
            System.out.println("0 - exit\n" +
                    "1 - print names of sorted images\n" +
                    "2 - add new image\n" +
                    "3 - calculate distances between picture and others\n" +
                    "4 - get the closest image");
            int command = in.nextInt();
            switch (command){
                case 0:
                    return;
                case 2:
                    System.out.print("Input a filiname:");
                    String tmp = in.nextLine();
                    String file = in.nextLine();
                    collection.addImage(file);
                    break;
                case 1:
                    collection.printFilenames();
                    break;
                case 3:
                    System.out.println("Input a number of image");
                    int number = in.nextInt();
                    if (number > collection.data.size()){
                        System.out.println("Number of image is out of range");
                    }
                    else
                        collection.getDifferencesFromOthers(number - 1);
                    break;
                case 4:
                    System.out.println("Input a number of image");
                    int idx = in.nextInt();
                    if (idx > collection.data.size()){
                        System.out.println("Number of image is out of range");
                    }
                    else
                        collection.getTheClosestImage(idx - 1);
                    break;
                default:
                    System.out.println("There is no such command. Try again.");
                    break;
            }
        }
    }
}

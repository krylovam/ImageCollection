package com;


import java.awt.*;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.SortedSet;
import java.io.File;
import java.io.FilenameFilter;
import java.util.TreeSet;

public class ImageCollection {
    SortedSet<SingleImage> data;
    String nameOfUnsortedFiles;
    private String resPath;
    private String format;
    ImageCollection(String path, String formatOfImage){
        resPath = path;
        format = formatOfImage;
        data = new TreeSet(new ImageComparator());
        loadImages();
    }

    public SingleImage get(int i){
        if ((i < 0) || (i > data.size())){
            System.out.println("Out of range");
            return null;
        }
        Iterator<SingleImage> imageIterator = data.iterator();
        for (int k = 0; k < i; k ++)
            imageIterator.next();
        return imageIterator.next();
    }

    public void loadImages() {
        File folder = new File(resPath);
        String[] files = folder.list(new FilenameFilter() {
            @Override
            public boolean accept(File folder, String name) {
                return name.endsWith(format);
            }
        });
        try {
            for (String fileName : files) {
                data.add(new SingleImage(resPath + fileName));
            }
        }catch (IOException e){
            System.out.println("files cannot be loaded. Try again");
        }
    }

    public void addImage(String filename){
        try {
            data.add(new SingleImage(filename));
        }catch (IOException e){
            System.out.println("file cannot be loaded. Try again");
        }
    }

    public void printFilenames(){
        int i = 1;
        for (SingleImage image: data){
            System.out.println(i + image.getFilename());
            i ++;
        }
    }

    private int getDistanceByDate(int i, int j){
        Date d1 = get(i).getCreationDate();
        Date d2 = get(j).getCreationDate();
        if (d1.before(d2))
            return (int)((double)d1.getTime()/d2.getTime()*100);
        else
            return (int)((double)d2.getTime()/d1.getTime()*100);
    }

    private int getDistanceByColorHSB(int i, int j){

        Color c1 = get(i).getAverageColor();
        Color c2 = get(j).getAverageColor();
        float[] hsb1 = Color.RGBtoHSB(c1.getRed(), c1.getGreen(), c1.getBlue(), null);
        float[] hsb2 = Color.RGBtoHSB(c2.getRed(), c2.getGreen(), c2.getBlue(), null);
        return  (int)((3 -((hsb1[0] - hsb2[0])*(hsb1[0] - hsb2[0]) +
                (hsb1[1] - hsb2[1])*(hsb1[1] - hsb2[1]) +
                (hsb1[2] - hsb2[2])*(hsb1[2] - hsb2[2])))*100 / 3);
    }

    private int getDistanceByColorRGB(int i, int j){
        Color c1 = get(i).getAverageColor();
        Color c2 = get(j).getAverageColor();
        return (int)((3 -
                (((float)(c1.getRed()-c2.getRed())/255*(c1.getRed()-c2.getRed())/255) +
                ((float)(c1.getGreen()-c2.getGreen())/255*(c1.getGreen()-c2.getGreen())/255) +
                ((float)(c1.getBlue()-c2.getBlue())/255*(c1.getBlue()-c2.getBlue())/255)))*100/3);

    }

    private int getDistanceBySIze(int i, int j){
        int h1 = get(i).getHeight(), w1 = get(i).getWidth();
        int h2 = get(j).getHeight(), w2 = get(j).getWidth();
        int ans;
        if (h1 > h2)
            ans = (int)((float)h2/h1 *50);
        else
            ans = (int)((float)h1/h2 *50);
        if (w1 > w2)
            ans += (int)((float)w2/w1 *50);
        else
            ans += (int)((float)w1/w2 * 50);
        return ans;
    }

    private int getAverageDistance(int i, int j){
        int kHSB = getDistanceByColorHSB(i, j);
        int kRGB = getDistanceByColorRGB(i, j);
        int kSize = getDistanceBySIze(i, j);
        int kDate = getDistanceByDate(i, j);
        return (kHSB + kRGB + kSize + kDate)/4;
    }

    public void getDifferencesFromOthers(int i){
        for (int j = 0; j < data.size(); j ++){
            if (i != j) {
                System.out.println("File " + (i+1) +
                                   "; File " + (j+1) +
                                   ";HSB: " + getDistanceByColorHSB(i, j) +
                                   "%; RGB: " + getDistanceByColorRGB(i, j) +
                                   "%; Size: " + getDistanceBySIze(i, j) +
                                    "%; CreationDate: " + getDistanceByDate(i, j) +
                                   "%; Average: " + getAverageDistance(i, j) + "%");
            }

        }
    }

    public void getTheClosestImage(int i){
        int min = 101;
        int minIdx = -1;
        for (int j = 0; j < data.size(); j ++){
            if (i != j) {
                int distance = getAverageDistance(i, j);
                if (distance < min){
                    min = distance;
                    minIdx = j;
                }
            }

        }
        System.out.println("The closest image to #" + (i+1) + " is #" + (minIdx+1));
    }
}

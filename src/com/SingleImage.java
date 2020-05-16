package com;

import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.io.File;
import java.util.concurrent.TimeUnit;
public class SingleImage {
    private int height;
    private int width;
    private Date creationDate;
    private String filename;
    private Color averageColor;

    public SingleImage(String file)throws IOException{
        filename = file;
        BufferedImage image = javax.imageio.ImageIO.read(new File(filename));
        height = image.getHeight();
        width = image.getWidth();
        creationDate = getDate(filename);
        averageColor = getAverageColor(image);
    }

    private Date getDate(String filename){
        File file = new File(filename);
        Path filePath = file.toPath();
        BasicFileAttributes attributes = null;
        try
        {
            attributes =
                    Files.readAttributes(filePath, BasicFileAttributes.class);
            return new Date(attributes.creationTime().to(TimeUnit.MILLISECONDS));
        }
        catch (IOException exception)
        {
            System.out.println("Exception handled when trying to get file " +
                    "attributes: " + exception.getMessage());
        }
        return null;
    }

    private Color getAverageColor(BufferedImage image){
        int red = 0, green = 0, blue = 0;
        for (int i = 0; i < image.getHeight(); i ++)
        {
            for (int j = 0; j < image.getWidth(); j ++){
                Color currColor = new Color(image.getRGB(j, i));
                red += currColor.getRed();
                green += currColor.getGreen();
                blue += currColor.getBlue();
            }
        }
        return new Color(red / (image.getWidth() * image.getHeight()),
                        green / (image.getWidth() * image.getHeight()),
                        blue / (image.getWidth() * image.getHeight()));
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Date getCreationDate(){
        return creationDate;
    }

    public Color getAverageColor(){
        return averageColor;
    }

    public String getFilename(){
        return filename;
    }
}

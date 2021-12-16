package com.azad.vid2ascii.utils;

import java.awt.image.BufferedImage;

public class ASCII {
    boolean negative;

    public ASCII() {
        this(false);
    }

    public ASCII(final boolean negative) {
        this.negative = negative;
    }
    /*
    public String convert(final BufferedImage image) {
        StringBuilder sb = new StringBuilder((image.getWidth() + 1) * image.getHeight());
        for (int y = 0; y < image.getHeight(); y++) {
            if (sb.length() != 0) sb.append("\n");
            for (int x = 0; x < image.getWidth(); x++) {
                Color pixelColor = new Color(image.getRGB(x, y));
                double gValue = (double) pixelColor.getRed() * 0.2989 + (double) pixelColor.getBlue() * 0.5870 + (double) pixelColor.getGreen() * 0.1140;
                final char s = negative ? returnStrNeg(gValue) : returnStrPos(gValue);
                sb.append(s);
            }
        }
        return sb.toString();
    }*/

    public String convert(final BufferedImage image) {
        StringBuilder sb = new StringBuilder((image.getWidth() + 1) * image.getHeight());
        for (int y = 0; y < image.getHeight(); y++) {
            if (sb.length() != 0) sb.append("\n");
            for (int x = 0; x < image.getWidth(); x++) {
                int pixelColor = image.getRGB(x, y);
                int r = (pixelColor>>16) & 0xff; //thank you random internet tutorial i found for providing me with these three scary looking lines to get the rgb values
                int g = (pixelColor>>8) & 0xff;
                int b = pixelColor & 0xff;
                char s;
                int avgPercent = (int)((((r + g + b)/3.0) / 255) * 100);

                if(avgPercent > 90){ //arbitrary numbers i chose. changing these could lead to more accurate results
                    s = 35;
                } else if (avgPercent > 80){
                    s= 64;
                } else if (avgPercent > 50){
                    s = 43;
                } else if (avgPercent > 35){
                    s = 58;
                } else if(avgPercent > 10){
                    s = 46;
                } else{
                    s = 32;
                }

                sb.append(s);
            }
        }
        return sb.toString();
    }

    /**
     * Create a new string and assign to it a string based on the grayscale value.
     * If the grayscale value is very high, the pixel is very bright and assign characters
     * such as . and , that do not appear very dark. If the grayscale value is very lowm the pixel is very dark,
     * assign characters such as # and @ which appear very dark.
     *
     * @param g grayscale
     * @return char
     */
    private char returnStrPos(double g)//takes the grayscale value as parameter
    {
        final char str;

        if (g >= 230.0) {
            str = ' ';
        } else if (g >= 200.0) {
            str = '.';
        } else if (g >= 180.0) {
            str = '*';
        } else if (g >= 160.0) {
            str = ':';
        } else if (g >= 130.0) {
            str = 'o';
        } else if (g >= 100.0) {
            str = '&';
        } else if (g >= 70.0) {
            str = '8';
        } else if (g >= 50.0) {
            str = '#';
        } else {
            str = '@';
        }
        return str; // return the character

    }

    /**
     * Same method as above, except it reverses the darkness of the pixel. A dark pixel is given a light character and vice versa.
     *
     * @param g grayscale
     * @return char
     */
    private char returnStrNeg(double g) {
        final char str;

        if (g >= 230.0) {
            str = '@';
        } else if (g >= 200.0) {
            str = '#';
        } else if (g >= 180.0) {
            str = '8';
        } else if (g >= 160.0) {
            str = '&';
        } else if (g >= 130.0) {
            str = 'o';
        } else if (g >= 100.0) {
            str = ':';
        } else if (g >= 70.0) {
            str = '*';
        } else if (g >= 50.0) {
            str = '.';
        } else {
            str = ' ';
        }
        return str;

    }
}

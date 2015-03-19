package Test;

import Image_analysis.Image_reader;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Ayost on 3/11/15.
 */
public class Test_Image_Parse {

    public static void main(String[] args){
        Image_reader reader;
        try {
            reader = new Image_reader(ImageIO.read(new File("/home/sudowooo/Pictures/example-free-business-card.png")));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        char[][] picture = new char[reader.width][reader.height];
        for(int i=0; i<reader.width;i++){
            Arrays.fill(picture[i], ' ');
        }

        ArrayList<Rectangle> rects= reader.find_rects(3, 255);
        for(int x=0; x<reader.width; x++){
            for(int y=0; y<reader.height; y++){
                for(Rectangle rect : rects){
                    if(rect.contains(x,y)){
                        picture[x][y] = '|';
                        break;
                    }
                }

            }
        }

        ArrayList<Image_reader.Dot> edges = reader.find_edges(255);
        for(Image_reader.Dot p : edges){
            picture[p.x][p.y]= '#';
        }


        for(int y=0; y<reader.height; y++){
            String row = "";
            for(int x=0; x<reader.width; x++){
                row += picture[x][y];
            }
            System.out.println(row);
        }


    }

}

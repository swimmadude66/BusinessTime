package Image_analysis;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by AYost on 3/11/15.
 */

public class Image_reader {

    public BufferedImage img;
    public Dot[][] pxs;
    private ArrayList<Dot> edges;
    public int width;
    public int height;

    public Image_reader(BufferedImage doc){
        img = doc;
        width = img.getWidth();
        height = img.getHeight();
        pxs = convertToBrightnessArray(img);

    }

    public ArrayList<Rectangle> find_rects(int padding, int threshold){
        ArrayList<Rectangle> areas = new ArrayList<Rectangle>();
        edges = find_edges(threshold);
        for(int x=0; x<img.getWidth(); x++){
            for(int y=0; y<img.getHeight(); y++){
                if(edges.contains(pxs[x][y])){
                    Rectangle newguy = join_up(new Rectangle(x, y, 0, 0), pxs[x][y], 2 * padding, padding);
                    areas.add(pad_rect(newguy, padding));
                }
            }
        }
        return areas;
    }

    private Rectangle pad_rect(Rectangle r, int padding){
        if(r.x - padding > 0)
            r.x -= padding;
        else
            r.x = 0;
        if(r.width + 2*padding <width)
            r.width += 2*padding;
        else
            r.width = width-r.x;
        if(r.y - padding > 0)
            r.y -= padding;
        else
            r.y = 0;
        if(r.height + 2*padding <height)
            r.height += 2*padding;
        else
            r.height = height-r.y;
        return r;
    }

    public Rectangle join_up(Rectangle rect, Dot start, int xdist, int ydist){
        ArrayList<Dot> friends = new ArrayList<Dot>();
        for(Dot d :getNeighbors(start.x, start.y, xdist, ydist)){
            if(edges.contains(d)){
                friends.add(d);
            }
        }
        for(Dot friend : friends){
            edges.remove(friend);
            if(friend.x < rect.x)
                rect.x = friend.x;
            if(friend.y < rect.y)
                rect.y = friend.y;
            if(friend.x > (rect.x+rect.width))
                rect.width = friend.x-rect.x;
            if(friend.y > (rect.y+rect.height))
                rect.height = friend.y-rect.y;
            rect = join_up(rect, friend, xdist, ydist);
        }
        return rect;
    }

    public ArrayList<Dot> find_edges(int threshold){
        ArrayList<Dot> edges = new ArrayList<Dot>();

        for(int x=0; x<img.getWidth(); x++){
            for(int y=0; y<img.getHeight(); y++){
                Dot px = pxs[x][y];
                ArrayList<Dot> neighbors = getNeighbors(x,y, 1, 1);
                for(Dot neighbor : neighbors){
                    if(Math.abs(neighbor.brightness-px.brightness) > threshold){
                        edges.add(px);
                        pxs[x][y].edge = true;
                    }
                }
            }
        }
        return edges;
    }


    private Dot[][] convertToBrightnessArray(BufferedImage image) {
        Dot[][] result = new Dot[width][height];
        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                int argb = 0;
                Color c = new Color(img.getRGB(x,y));
                argb += c.getRed();
                argb += c.getGreen();
                argb += c.getBlue();
                result[x][y] = new Dot(x, y);
                result[x][y].brightness = argb;
            }
        }
        return result;

    }

    private ArrayList<Dot> getNeighbors(int x, int y, int xdist, int ydist){
        ArrayList<Dot> res = new ArrayList<Dot>();
        for(int i=xdist; i>0; i--){
            if(x-i>=0){
                res.add(pxs[x-i][y]);
            }
            if(x+i<width){
                res.add(pxs[x+i][y]);
            }
        }
        for(int j=ydist; j>0; j--){
            if(y-j>=0){
                res.add(pxs[x][y-j]);
            }
            if(y+j<height){
                res.add(pxs[x][y+j]);
            }
        }
        return res;
    }

    public class Dot{
        public int x, y;
        int brightness;
        boolean edge;

        public Dot(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

}

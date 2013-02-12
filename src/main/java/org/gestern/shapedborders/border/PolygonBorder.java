package org.gestern.shapedborders.border;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

public class PolygonBorder extends AbstractBorder {
    
    /** The individual lines making up the polygon. */
    private final List<Line> lines;
    
    /** Save point list for serialization convenience. */
    private final List<List<Double>> points;
    
    /**
     * Create polygon border via a sequence of points. Each polygon face is defined by 2 consecutive points. 
     * The final point is implicitly joined with the first.
     * @param points
     */
    public PolygonBorder(List<List<Double>> points) {
        if (points.size() < 3) throw new IllegalArgumentException("Too few points defined for polygon.");
        this.points = points;
        List<Double> first = points.get(0);
        checkPoint(first);
        
        List<Line> lineList = new ArrayList<Line>(points.size());
        
        double pxFirst = first.get(0), pzFirst = first.get(1);
        double pxLast = pxFirst, pzLast = pzFirst;
        for (List<Double> p : points.subList(1, points.size())) {
            checkPoint(p);
            double px = p.get(0), pz = p.get(1);
            lineList.add(new Line(pxLast,pzLast,px,pz));
            pxLast = px; pzLast = pz;
        }
        lineList.add(new Line(pxLast,pzLast,pxFirst,pzFirst));
        
        this.lines = new ArrayList<Line>(0);
    }
    
    private void checkPoint(List<Double> point) {
        if (point.size() != 2) 
            throw new IllegalArgumentException("Bad number of values defining point. Expected: 2, found: "+point.size());
    }

    @Override
    public boolean inside(Location loc) {
        // ray-casting algorithm! http://en.wikipedia.org/wiki/Point_in_polygon#Ray_casting_algorithm
        // idea: cast ray through the point, count number of times it crosses a face of the polygon
        // this implementation casts on a horizontal line from negative infinity up to the point given by loc
        
        double x = loc.getX(), z = loc.getZ();
        int crosses = 0;
        for (Line line : lines) 
            if (line.rayIntersects(x,z)) crosses++;
        
        // uneven number of line crossings mean the point is inside
        return crosses%2 == 1;
    }
    

    @Override
    public Location reposition(Location loc) {
        // TODO Auto-generated method stub
        return null;
    }

    @SuppressWarnings("serial")
    @Override
    public Map<String, Object> serialize() {
        return new HashMap<String,Object>() {{
            put("points", points);
        }};
    }
    
    @SuppressWarnings("unchecked")
    public PolygonBorder deserialize(Map<String,Object> source) {
        return new PolygonBorder((List<List<Double>>) source.get("points"));
    }
    
    private class Line {
        // conditions for variables: x1 <= x2, z1 <= z2
        public final double x1, z1, x2, z2;
        
        public Line(double x1, double z1, double x2, double z2) {
            this.x1 = x1;
            this.z1 = z1;
            this.x2 = x2;
            this.z2 = z2;
        }
        
        /**
         * Find out if a ray on height z from negative infinity crosses this line on its way to point x/z
         * @param x point x coord
         * @param z point z coord
         * @return
         */
        public boolean rayIntersects(double x, double z) {
            // below or above: no cross
            if ((z < z1 && z < z2) || (z > z1 && z > z2)) return false;
            // left or right of the smaller/larger x/z coord of line? easy.
            if (x < x1 && x < x2) return false;
            if (x > x1 && x > x2) return true;
            
            // finally, calculate the more complicated case: point is somewhere in box circumscribing the line
            // formula from http://stackoverflow.com/questions/3461453/determine-which-side-of-a-line-a-point-lies
            return ((x2 - x1)*(z - z1) - (z2 - z1)*(z - x1)) > 0;
        }
    }

}

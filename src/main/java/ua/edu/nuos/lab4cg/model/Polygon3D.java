package ua.edu.nuos.lab4cg.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by eugeny on 04.11.2015.
 */
public class Polygon3D {
    private final List<Point3D> points;

    Polygon3D(Point3D... points) {
        this(Arrays.asList(points));
    }

    private Polygon3D(List<Point3D> points) {
        this.points = points;
    }

    public List<Point3D> getPoints() {
        return points;
    }

    public double distanceFromO2(double[][] m) {
        return - points.stream()
                .map(p->p.transform(m))
                .mapToDouble(p->p.getX()*p.getX()+p.getY()*p.getY()+p.getZ()*p.getZ()).max().orElse(0);
    }
}

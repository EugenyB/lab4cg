package ua.edu.nuos.lab4cg.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import ua.edu.nuos.lab4cg.model.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MyView {
    private final double f = 400;
    private final double cx = 300;
    private final double cy = 300;
    private final double cz = 700;

    private double[][] m = {
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}
    };

    boolean ensureVisibility = true;

    private double xs(Point3D p) {
        return p.getX() * f / p.getZ();
    }

    private double ys(Point3D p) {
        return p.getY() * f / p.getZ();
    }

    public void drawFigure(Figure f, GraphicsContext gc) {
        Polygon3D[] faces = new Polygon3D[f.getFacesCount()];
        for (int i = 0; i < f.getFacesCount(); i++) {
            faces[i] = f.getFace(i);
        }

        Arrays.sort(faces, Comparator.comparingDouble(p -> p.distanceFromO2(m)));
        for (Polygon3D face : faces) {
            if (!ensureVisibility || visible(face)) {
                drawPolygon(face, gc, ensureVisibility);
            }
        }
    }

    private boolean visible(Polygon3D face) {
        List<Point3D> points = face.getPoints().stream().map(p -> p.transform(m)).collect(Collectors.toList());
        double xv = -points.get(0).getX();
        double yv = -points.get(0).getY();
        double zv = -points.get(0).getZ();

        double xa = points.get(1).getX() - points.get(0).getX();
        double ya = points.get(1).getY() - points.get(0).getY();
        double za = points.get(1).getZ() - points.get(0).getZ();

        double xb = points.get(2).getX() - points.get(0).getX();
        double yb = points.get(2).getY() - points.get(0).getY();
        double zb = points.get(2).getZ() - points.get(0).getZ();

        double xn = ya * zb - yb * za;
        double yn = za * xb - zb * xa;
        double zn = xa * yb - xb * ya;

        return xn * xv + yn * yv + zn * zv > 0;
    }

    private void drawPolygon(Polygon3D polygon3D, GraphicsContext gc, boolean fill) {
        Polygon2D polygon2D = makeProjection(polygon3D);
        if (fill) {
            gc.setFill(Color.WHITE);
            gc.fillPolygon(polygon2D.getXs(), polygon2D.getYs(), polygon2D.size());
        }
        gc.strokePolygon(polygon2D.getXs(), polygon2D.getYs(), polygon2D.size());
    }

    private Polygon2D makeProjection(Polygon3D polygon3D) {
        return new Polygon2D(
                polygon3D
                        .getPoints()
                        .stream()
                        .map(p -> p.transform(m))
                        .map(this::projection)
                        .collect(Collectors.toList())
        );
    }

    private Point2D projection(Point3D p) {
        return new Point2D(xs(p), ys(p));
    }

    public void moveFigure(double dx, double dy, double dz) {
        double[][] t = {
                {1, 0, 0, dx},
                {0, 1, 0, dy},
                {0, 0, 1, dz},
                {0, 0, 0, 1}
        };
        m = multiply(t, m);
    }

    private double[][] multiply(double[][] t, double[][] m) {
        double[][] result = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    result[i][j] += t[i][k] * m[k][j];
                }
            }
        }
        return result;
    }

    public void rotateFigureY(double angle) {
        angle = Math.toRadians(angle);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double[][] t = {
                {cos, 0, sin, 0},
                {0, 1, 0, 0},
                {-sin, 0, cos, 0},
                {0, 0, 0, 1}
        };
        moveFigure(-cx, -cy, -cz);
        m = multiply(t, m);
        moveFigure(cx, cy, cz);
    }

    public void rotateFigureX(double angle) {
        angle = Math.toRadians(angle);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double[][] t = {
                {1, 0, 0, 0},
                {0, cos, -sin, 0},
                {0, sin, cos, 0},
                {0, 0, 0, 1}
        };
        moveFigure(-cx, -cy, -cz);
        m = multiply(t, m);
        moveFigure(cx, cy, cz);
    }

    public void rotateFigureZ(double angle) {
        angle = Math.toRadians(angle);
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double[][] t = {
                {cos, -sin, 0, 0},
                {sin, cos, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        };
        moveFigure(-cx, -cy, -cz);
        m = multiply(t, m);
        moveFigure(cx, cy, cz);
    }

    public void setVisibilityCheck(boolean selected) {
        ensureVisibility = selected;
    }
}

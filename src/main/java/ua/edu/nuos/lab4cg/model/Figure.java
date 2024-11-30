package ua.edu.nuos.lab4cg.model;

public class Figure {

    public Figure(double[] x, double[] y, double[] z, int[][] p) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.p = p;
    }

    private final double[] x;
    private final double[] y;
    private final double[] z;

    private final int[][] p;

    public int getFacesCount() {
        return p.length;
    }

    public Polygon3D getFace(int i) {
        if (i<0 || i>=p.length) {
            throw new IndexOutOfBoundsException("face num must be in[0..."+(p.length-1)+"], but was:" + i);
        }
        Point3D[] p3d = new Point3D[p[i].length];
        for (int j = 0; j < p[i].length; j++) {
            p3d[j] = new Point3D(x[p[i][j]], y[p[i][j]], z[p[i][j]]);
        }
        return new Polygon3D(p3d);
    }
}

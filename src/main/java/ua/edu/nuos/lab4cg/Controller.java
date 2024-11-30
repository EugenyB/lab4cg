package ua.edu.nuos.lab4cg;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import ua.edu.nuos.lab4cg.model.Figure;
import ua.edu.nuos.lab4cg.view.MyView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    @FXML
    private CheckBox ensureVisibility;

    @FXML
    private Canvas canvas;

    @FXML
    private Pane pane;

    private Figure figure = null;
    private final MyView view = new MyView();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        canvas.widthProperty().bind(pane.widthProperty());
        canvas.heightProperty().bind(pane.heightProperty());

        canvas.widthProperty().addListener(evt -> draw());
        canvas.heightProperty().addListener(evt -> draw());
    }

    private void draw() {
        view.setVisibilityCheck(ensureVisibility.isSelected());
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITESMOKE);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
        gc.setStroke(Color.BLUE);
        gc.scale(1.25,1.25);
        if (figure!=null) {
            view.drawFigure(figure, gc);
        }
        gc.scale(.8,.8);
    }

    public void moveXplus() {
        view.moveFigure(10,0,0);
        draw();
    }

    public void moveXminus() {
        view.moveFigure(-10,0,0);
        draw();
    }

    public void rotateXplus() {
        view.rotateFigureX(10);
        draw();
    }

    public void rotateXminus() {
        view.rotateFigureX(-10);
        draw();
    }

    public void rotateZplus() {
        view.rotateFigureZ(10);
        draw();
    }

    public void rotateZminus() {
        view.rotateFigureZ(-10);
        draw();
    }

    public void rotateYplus() {
        view.rotateFigureY(10);
        draw();
    }

    public void rotateYminus() {
        view.rotateFigureY(-10);
        draw();
    }

    public void openFile() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("."));
        File file = fc.showOpenDialog(null);
        if (file!=null) {
            figure = readFromFile(file);
            draw();
        }
    }

    private Figure readFromFile(File file) {
        try (BufferedReader reader = Files.newBufferedReader(file.toPath())) {
            String[] s = reader.readLine().split(" ");
            int nPoints = Integer.parseInt(s[0]);
            int nFaces = Integer.parseInt(s[1]);
            double[] x = new double[nPoints];
            double[] y = new double[nPoints];
            double[] z = new double[nPoints];
            int[][] p = new int[nFaces][];
            for (int i = 0; i < x.length; i++) {
                s = reader.readLine().split(" ");
                x[i] = Double.parseDouble(s[0]);
                y[i] = Double.parseDouble(s[1]);
                z[i] = Double.parseDouble(s[2]);
            }
            for (int i = 0; i < p.length; i++) {
                s = reader.readLine().split(" ");
                p[i] = new int[s.length];
                for (int j = 0; j < p[i].length; j++) {
                    p[i][j] = Integer.parseInt(s[j]);
                }
            }
            return new Figure(x,y,z,p);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void repaint() {
        draw();
    }
}

package com.ccs3402.lab.smartcampusnavigationsystem.graphs.Application;

import com.ccs3402.lab.smartcampusnavigationsystem.graphs.DijkstraAlgorithm;
import com.ccs3402.lab.smartcampusnavigationsystem.graphs.Graph;
import com.ccs3402.lab.smartcampusnavigationsystem.graphs.Route;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private Graph graph = new Graph();

    @Override
    public void start(Stage stage) {

        // Build Graph
        buildGraph();

        // =========================
        // Title
        // =========================

        Label title = new Label("🗺 Smart Campus Navigation System");
        title.getStyleClass().add("title");

        // =========================
        // Combo Boxes
        // =========================

        ComboBox<String> startBox = new ComboBox<>();
        ComboBox<String> endBox = new ComboBox<>();

        ComboBox<String> fromBox = new ComboBox<>();
        ComboBox<String> toBox = new ComboBox<>();

        startBox.getItems().addAll(graph.getNodes());
        endBox.getItems().addAll(graph.getNodes());

        fromBox.getItems().addAll(graph.getNodes());
        toBox.getItems().addAll(graph.getNodes());

        startBox.setPromptText("Select Start");
        endBox.setPromptText("Select Destination");

        fromBox.setPromptText("Road From");
        toBox.setPromptText("Road To");

        startBox.setPrefWidth(250);
        endBox.setPrefWidth(250);
        fromBox.setPrefWidth(250);
        toBox.setPrefWidth(250);

        // =========================
        // Buttons
        // =========================

        Button findBtn = new Button("🔍 Find Shortest Route");
        Button blockBtn = new Button("🚧 Mark Road as Closed");

        // =========================
        // Output
        // =========================

        TextArea output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);
        output.setPrefHeight(250);

        // =========================
        // Route Finder Layout
        // =========================

        GridPane inputPane = new GridPane();
        inputPane.setHgap(15);
        inputPane.setVgap(15);
        inputPane.setPadding(new Insets(10));

        inputPane.add(new Label("Start Location :"), 0, 0);
        inputPane.add(startBox, 1, 0);

        inputPane.add(new Label("Destination :"), 0, 1);
        inputPane.add(endBox, 1, 1);

        inputPane.add(findBtn, 1, 2);

        // =========================
        // Maintenance Layout
        // =========================

        // =========================

        GridPane maintenancePane = new GridPane();
        maintenancePane.setHgap(15);
        maintenancePane.setVgap(15);
        maintenancePane.setPadding(new Insets(10));

        maintenancePane.add(new Label("Block Road From :"), 0, 0);
        maintenancePane.add(fromBox, 1, 0);

        maintenancePane.add(new Label("Block Road To :"), 0, 1);
        maintenancePane.add(toBox, 1, 1);

        maintenancePane.add(blockBtn, 1, 2);


        // =========================
        // FIND ROUTE ACTION
        // =========================
        findBtn.setOnAction(e -> {

            output.setText("Checking for available routes...\n");

            String start = startBox.getValue();
            String end = endBox.getValue();

            if (start == null || end == null) {
                output.setText("Please select both locations.");
                return;
            }

            if (start.equals(end)) {
                output.setText("Start and destination cannot be same.");
                return;
            }

            Route result = DijkstraAlgorithm.findShortestPath(graph, start, end);

            if (result.getTotalDistance() == Integer.MAX_VALUE) {
                output.setText("No route available due to road maintenance.");
                return;
            }

            // =========================
            // BUILD PATH OUTPUT (FIXED)
            // =========================
            StringBuilder sb = new StringBuilder();
            sb.append("Shortest Route:\n\n");

            for (int i = 0; i < result.getPath().size(); i++) {

                sb.append(result.getPath().get(i));

                if (i < result.getPath().size() - 1) {
                    sb.append(" → ");
                }
            }

            sb.append("\n\nTotal Distance: ")
                    .append(result.getTotalDistance())
                    .append(" meters");

            double time = result.getTotalDistance() / 80.0;

            sb.append("\nEstimated Time: ")
                    .append(String.format("%.2f", time))
                    .append(" minutes");

            output.setText(sb.toString());
        });

        // =========================
        // BLOCK ROAD ACTION (FIXED)
        // =========================
        blockBtn.setOnAction(f -> {

            String from = fromBox.getValue();
            String to = toBox.getValue();

            if (from == null || to == null) {
                output.setText("Please select road to block.");
                return;
            }

            graph.blockEdge(from, to);

            output.setText("⚠ Road CLOSED for maintenance: "
                    + from + " ↔ " + to);
        });

        // =========================
        // LAYOUT
        // =========================
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        root.getChildren().addAll(
                title,
                new Separator(),
                inputPane,
                new Separator(),
                maintenancePane,
                new Separator(),
                output
        );


        // =========================
        // SCENE
        // =========================
        Scene scene = new Scene(root, 500, 600);

        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );

        stage.setTitle("Campus Navigation System");
        stage.setScene(scene);
        stage.show();
    }

    // =========================
    // GRAPH DATA
    // =========================
    private void buildGraph() {

        graph.addEdge("FBMK", "KAA", 150);
        graph.addEdge("FSKTM", "DKAP", 80);
        graph.addEdge("FSKTM", "Kolej Canselor", 250);
        graph.addEdge("FSKTM", "UPM main gate", 60);
        graph.addEdge("UPM main gate", "KAA", 300);
        graph.addEdge("UPM main gate", "DKAP", 150);
        graph.addEdge("FSKTM", "Guard House KC",80);
        graph.addEdge("Guard House KC", "FBMK", 250);
        graph.addEdge("Engineering", "Hostel", 500);
        graph.addEdge("Engineering", "Main Gate", 600);
    }

    public static void main(String[] args) {
        launch();
    }
}
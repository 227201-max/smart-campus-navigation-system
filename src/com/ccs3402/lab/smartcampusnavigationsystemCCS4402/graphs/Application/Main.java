package com.ccs3402.lab.smartcampusnavigationsystemCCS4402.graphs.Application;

import com.ccs3402.lab.smartcampusnavigationsystemCCS4402.graphs.DijkstraAlgorithm;
import com.ccs3402.lab.smartcampusnavigationsystemCCS4402.graphs.Graph;
import com.ccs3402.lab.smartcampusnavigationsystemCCS4402.graphs.Route;
import com.ccs3402.lab.smartcampusnavigationsystemCCS4402.Service.Facilities;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

import java.util.*;
import java.time.LocalTime;

import static javafx.application.Application.launch;

public class Main extends Application {

    private Graph graph = new Graph(); // walking routes
    private Graph busGraph = new Graph(); // bus routes
    private TextArea facilityOutput;
    private TextArea routeOutput;
    private ArrayList<Facilities> facilities = new ArrayList<>();
    private Set<String> busStops = new HashSet<>();
    private final double BUS_SPEED = 300.0;
    int waitingTime;// meters per minute
    private Set<String> blockedEdges = new HashSet<>();
    private final List<Integer> busSchedule = Arrays.asList(0, 10, 20, 30, 40, 50);



    public void start(Stage stage) {

        buildGraph(); // create walking map
        buildbusGraph(); // create bus routes
        buildFacilities(); // stores campus services

        routeOutput = new TextArea();
        facilityOutput = new TextArea();

        routeOutput.setEditable(false);
        routeOutput.setWrapText(true);
        routeOutput.setPrefHeight(250);

        facilityOutput.setEditable(false);
        facilityOutput.setWrapText(true);
        facilityOutput.setPrefHeight(250);

        Label title = new Label(" Smart Campus Navigation System");
        title.getStyleClass().add("title");


        ComboBox<String> startBox = new ComboBox<>();
        ComboBox<String> endBox = new ComboBox<>();

        ComboBox<String> facilityBox = new ComboBox<>();

        facilityBox.getItems().addAll(
                "Printing Service",
                "Cafe",
                "Convenience Store",
                "Meeting Rooms",
                "Toilets",
                "Vending Machines"
        );

        facilityBox.setPromptText("Find Facility");

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
        facilityBox.setPrefWidth(250);

        // =========================
        // BUTTONS
        // =========================
        Button findBtn = new Button("🔍 Find Shortest Route");
        Button blockBtn = new Button("🚧 Mark Road as Closed");
        Button facilityBtn = new Button("Find Facility");
        Button resetBtn = new Button("🔄 Reset");

        TextArea output = new TextArea();
        output.setEditable(false);
        output.setWrapText(true);
        output.setPrefHeight(250);

        // =========================
        // GRID PANE
        // =========================
        GridPane inputPane = new GridPane();
        inputPane.setHgap(15);
        inputPane.setVgap(15);
        inputPane.add(resetBtn, 2, 2);
        inputPane.setPadding(new Insets(10));

        inputPane.add(new Label("Start Location :"), 0, 0);
        inputPane.add(startBox, 1, 0);

        inputPane.add(new Label("Destination :"), 0, 1);
        inputPane.add(endBox, 1, 1);

        inputPane.add(findBtn, 1, 2);

        GridPane facilityPane = new GridPane();
        facilityPane.setHgap(15);
        facilityPane.setVgap(15);
        facilityPane.setPadding(new Insets(10));

        facilityPane.add(new Label("Facility :"), 0, 0);
        facilityPane.add(facilityBox, 1, 0);
        facilityPane.add(facilityBtn, 1, 1);

        facilityBtn.setOnAction(e -> {

            String selectedFacility = facilityBox.getValue();
            String start = startBox.getValue();

            if (selectedFacility == null || start == null) {
                facilityOutput.setText("Please select start location and facility.");
                return;
            }

            StringBuilder sb = new StringBuilder();
            double bestDistance = Double.MAX_VALUE;
            Route bestRoute = null;
            String bestLocation = null;

            for (Facilities f : facilities) {

                if (f.getServices().contains(selectedFacility)) {

                    Route route = DijkstraAlgorithm.findShortestPath(
                            graph,
                            start,
                            f.getLocation(),
                            false,
                            blockedEdges
                    );

                    // 🚨 SKIP unreachable routes
                    if (route.getTotalDistance() == Integer.MAX_VALUE) {
                        continue;
                    }

                    if (route.getTotalDistance() < bestDistance) {
                        bestDistance = route.getTotalDistance();
                        bestRoute = route;
                        bestLocation = f.getLocation();
                    }
                }
            }
            if (bestRoute == null || bestRoute.getTotalDistance() == Integer.MAX_VALUE) {
                facilityOutput.setText("❌ No reachable facility found.");
                return;
            }


            double travelTime = bestDistance / 30.0; // walking speed
            int minutes = (int) travelTime;
            int seconds = (int) ((travelTime - minutes) * 60);


            sb.append("🏫 NEAREST FACILITY\n");
            sb.append("====================\n\n");

            sb.append("Service: ").append(selectedFacility).append("\n");
            sb.append("Nearest Location: ").append(bestLocation).append("\n\n");

            sb.append("🚶 Route:\n");
            sb.append(String.join(" → ", bestRoute.getPath())).append("\n\n");

            sb.append("📏 Distance: ").append(bestDistance).append(" m\n");
            sb.append("⏱ Estimated Time: ")
                    .append(minutes)
                    .append(" min ")
                    .append(seconds)
                    .append(" sec");

            facilityOutput.setText(sb.toString());
        });

        // =========================
        // MAINTENANCE LAYOUT
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

        blockBtn.setOnAction(e -> {

            String from = fromBox.getValue();
            String to = toBox.getValue();

            if (from == null || to == null) {
                output.setText("Please select both nodes to block a road.");
                return;
            }

            String edge1 = from + "-" + to;
            String edge2 = to + "-" + from;

            blockedEdges.add(edge1);
            blockedEdges.add(edge2);

            output.setText("🚧 Road blocked between " + from + " and " + to);
        });

        resetBtn.setOnAction(e -> {

            startBox.getSelectionModel().clearSelection();
            endBox.getSelectionModel().clearSelection();
            fromBox.getSelectionModel().clearSelection();
            toBox.getSelectionModel().clearSelection();
            facilityBox.getSelectionModel().clearSelection();

            output.clear();
            facilityOutput.clear();

            blockedEdges.clear();

            output.setText("🔄 System reset successfully.");
        });

        //find route action
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


            // =====================
            // ROUTES
            // =====================
            Route walkingRoute = DijkstraAlgorithm.findShortestPath(graph, start, end,false,blockedEdges);
            Route busRoute = DijkstraAlgorithm.findShortestPath(busGraph, start, end,true,blockedEdges);

            double busDistance = busRoute.getTotalDistance();
            double busTravelTime = busDistance / BUS_SPEED;

            LocalTime now = LocalTime.now();
            int currentMinute = now.getMinute();

            int nextBus = getNextBusArrival(currentMinute);

            int waitingTime = nextBus - currentMinute;

            if (waitingTime < 0) {
                waitingTime += 60;
            }

            double totalBusTime = busTravelTime + waitingTime;

            boolean busAvailable = busStops.contains(start) && busStops.contains(end);

            if (walkingRoute.getTotalDistance() == Integer.MAX_VALUE) {
                output.setText("No route available due to road maintenance.");
                return;
            }

            // =====================
            // WALKING SECTION
            // =====================
            StringBuilder sb = new StringBuilder();

            sb.append("🚶 Walking Route:\n\n");

            sb.append(String.join(" → ", walkingRoute.getPath()));

            sb.append("\n\nTotal Distance: ")
                    .append(walkingRoute.getTotalDistance())
                    .append(" meters");

            double walkTime = walkingRoute.getTotalDistance() / 70.0;

            int minutes = (int) walkTime;
            int seconds = (int) Math.round((walkTime - minutes) * 60);

            if (seconds == 60) {
                minutes++;
                seconds = 0;
            }

            sb.append("\nEstimated Walking Time: ")
                    .append(minutes)
                    .append(" min ")
                    .append(seconds)
                    .append(" sec");


            // =====================
            // BUS SECTION
            // =====================

            sb.append("\n\n🚌 BUS ROUTE:");

            if (busRoute.getPath().isEmpty() || !busAvailable) {
                sb.append("\n❌ No valid bus route available");
            } else {

                sb.append("\nRoute: ")
                        .append(String.join(" → ", busRoute.getPath()));

                sb.append("\nDistance: ")
                        .append(busRoute.getTotalDistance())
                        .append(" meters");

                sb.append("\nEstimated Time:");

                double busTime = busDistance / BUS_SPEED;

                int busMinutes = (int) busTime;
                int busSeconds = (int) ((busTime - busMinutes) * 60);

                sb.append("\n🚌 Travel time: ")
                        .append(busMinutes)
                        .append(" min ")
                        .append(busSeconds)
                        .append(" sec");

                sb.append("\n⏳ Waiting time: ")
                        .append(waitingTime)
                        .append(" min");

                sb.append("\n🕒 Current Time: ")
                        .append(now.withSecond(0).withNano(0));

                LocalTime arrivalTime = now.plusMinutes(waitingTime);

                sb.append("\n🚌 Next Bus Arrival: ")
                        .append(arrivalTime.withSecond(0).withNano(0));

                int totalMinutes = (int) totalBusTime;
                int totalSeconds = (int) Math.round((totalBusTime - totalMinutes) * 60);

                if (totalSeconds == 60) {
                    totalMinutes++;
                    totalSeconds = 0;
                }

                sb.append("\n⏱ Total time: ")
                        .append(totalMinutes)
                        .append(" min ")
                        .append(totalSeconds)
                        .append(" sec");

            }
            output.setText(sb.toString());
        });


        //layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        root.getChildren().addAll(
                title,
                new Separator(),
                inputPane,
                new Separator(),
                facilityPane,
                facilityOutput,
                new Separator(),
                maintenancePane,
                new Separator(),
                output
        );


        //scene
        Scene scene = new Scene(root, 500, 600);

        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );

        stage.setTitle("Campus Navigation System");
        stage.setScene(scene);
        stage.show();
    }

    private int getNextBusArrival(int currentMinute) {

        for (int time : busSchedule) {
            if (time >= currentMinute) {
                return time;
            }
        }
        return busSchedule.get(0) + 60;
    }

    // =========================
    // FACILITY DATA
    // =========================

    private void buildFacilities() {

        Facilities fsktm = new Facilities("FSKTM");
        fsktm.addService("Printing");
        fsktm.addService("Cafe");
        fsktm.addService("Meeting Room");
        fsktm.addService("Toilets");
        fsktm.addService("Vending Machines");
        fsktm.addService("Bus Stop");
        facilities.add(fsktm);

        Facilities fbmk = new Facilities("FBMK");
        fbmk.addService("Printing Service");
        fbmk.addService("Bus Stop");
        facilities.add(fbmk);

        Facilities kaa = new Facilities("KAA");
        kaa.addService("Cafe");
        kaa.addService("Printing Service");
        kaa.addService("Convenience Store");
        kaa.addService("Toilets");
        kaa.addService("Bus Stop");
        facilities.add(kaa);

        Facilities dkap = new Facilities("DKAP");
        dkap.addService("Convenience Store");
        dkap.addService("Toilets");
        dkap.addService("Vending Machines");
        facilities.add(dkap);

        Facilities bistro = new Facilities("Bistro");
        bistro.addService("Convenience Store");
        bistro.addService("Cafe");
        bistro.addService("Printing Service");
        bistro.addService("Bus Stop");
        bistro.addService("Vending Machines");
        facilities.add(bistro);

        Facilities library = new Facilities("Library");
        library.addService("Printing Service");
        library.addService("Convenience Store");
        library.addService("Cafe");
        library.addService("Toilets");
    }

    // =========================
    // GRAPH DATA
    // =========================
    private void buildGraph() {

        graph.addEdge("FBMK", "KAA", 180);
        graph.addEdge("FSKTM", "DKAP", 80);
        graph.addEdge("FSKTM", "Guard House KC", 250);
        graph.addEdge("FSKTM", "UPM main gate", 80);
        graph.addEdge("UPM main gate", "KAA", 400);
        graph.addEdge("UPM main gate", "DKAP", 180);
        graph.addEdge("Guard House KC", "FBMK", 500);
        graph.addEdge("Guard House KC", "KMR", 700);
        graph.addEdge("KMR", "Bistro", 160);
        graph.addEdge("Bistro", "Canselori", 300);
        graph.addEdge("Canselori", "Faculty of Science", 700);
        graph.addEdge("Faculty of Science", "SPE", 80);
        graph.addEdge("SPE", "Library", 350);
        graph.addEdge("DKAP", "Dewan Besar", 400);
        graph.addEdge("Dewan Besar", "PKSASS", 50);
        graph.addEdge("Library", "Bilik Kuliah", 280);
        graph.addEdge("Dewan Besar", "Dewan Kuliah", 850);
        graph.addEdge("Dewan Kuliah", "Foodtech Faculty", 100);
        graph.addEdge("Bilik Kuliah", "Foodtech Faculty", 600);
        graph.addEdge("Guard House KC", "Dewan Besar", 280);
        graph.addEdge("PKSASS", "SPE", 280);

        busStops.add("FSKTM");
        busStops.add("KAA");
        busStops.add("FBMK");
        busStops.add("SPE");
        busStops.add("Library");
        busStops.add("Faculty of Science");
        busStops.add("Foodtech Faculty");
    }

    // =========================
    // BUS GRAPH DATA
    // =========================
    private void buildbusGraph() {

        busGraph.addEdge("FSKTM", "SPE", 500);
        busGraph.addEdge("SPE", "Library", 350);
        busGraph.addEdge("FBMK", "SPE",450);
        busGraph.addEdge("Library","Foodtech Faculty",200);

    }
    private boolean hasBusPath(Route route) {

        List<String> path = route.getPath();

        for (int i = 0; i < path.size(); i++) {

            String node = path.get(i);

            // only check START and END (not every node)
            if (i == 0 || i == path.size() - 1) {
                if (!busStops.contains(node)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        launch();
    }
}

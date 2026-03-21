package com.carrental.server.network;

import com.carrental.client.Message;
import com.carrental.server.model.*;
import com.carrental.server.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Socket Server pentru comunicarea cu aplicația Client
 * Folosește serializare pentru trimiterea obiectelor Message
 */
@Component
public class SocketServer {

    @Value("${socket.server.port:9090}")
    private int serverPort;

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private RentalService rentalService;

    private ServerSocket serverSocket;
    private boolean running = false;
    private final Map<String, ClientHandler> connectedClients = new ConcurrentHashMap<>();

    /**
     * Pornește serverul la inițializare
     */
    @PostConstruct
    public void start() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(serverPort);
                running = true;
                System.out.println("🔌 Socket Server pornit pe portul " + serverPort);

                while (running) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("✅ Client conectat: " + clientSocket.getInetAddress());

                    ClientHandler handler = new ClientHandler(clientSocket);
                    new Thread(handler).start();
                }
            } catch (IOException e) {
                if (running) {
                    System.err.println("❌ Eroare Socket Server: " + e.getMessage());
                }
            }
        }).start();
    }

    /**
     * Oprește serverul la shutdown
     */
    @PreDestroy
    public void stop() {
        running = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Eroare la oprirea serverului: " + e.getMessage());
        }
    }

    /**
     * Handler pentru fiecare client conectat
     */
    private class ClientHandler implements Runnable {
        private Socket socket;
        private ObjectInputStream input;
        private ObjectOutputStream output;
        private String username;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                output = new ObjectOutputStream(socket.getOutputStream());
                input = new ObjectInputStream(socket.getInputStream());

                Message message;
                while ((message = (Message) input.readObject()) != null) {
                    System.out.println("📩 Primit: " + message.getType());
                    Message response = processMessage(message);
                    output.writeObject(response);
                    output.flush();
                }
            } catch (Exception e) {
                System.out.println("❌ Client deconectat: " + username);
            } finally {
                cleanup();
            }
        }

        /**
         * Procesează mesajele primite de la client
         */
        private Message processMessage(Message message) {
            try {
                switch (message.getType()) {
                    case "LOGIN":
                        return handleLogin(message);
                    case "GET_CARS":
                        return handleGetCars(message);
                    case "GET_AVAILABLE_CARS":
                        return handleGetAvailableCars();
                    case "CREATE_RENTAL":
                        return handleCreateRental(message);
                    case "GET_RENTALS":
                        return handleGetRentals(message);
                    case "ADD_CAR":
                        return handleAddCar(message);
                    case "UPDATE_CAR":
                        return handleUpdateCar(message);
                    case "DELETE_CAR":
                        return handleDeleteCar(message);
                    case "GET_USERS":
                        return handleGetUsers();
                    case "CREATE_USER":
                        return handleCreateUser(message);
                    case "UPDATE_RENTAL_STATUS":
                        return handleUpdateRentalStatus(message);
                    default:
                        return createErrorResponse("Tip mesaj necunoscut: " + message.getType());
                }
            } catch (Exception e) {
                e.printStackTrace();
                return createErrorResponse("Eroare: " + e.getMessage());
            }
        }

        private Message handleLogin(Message message) {
            String username = (String) message.getData("username");
            String password = (String) message.getData("password");

            if (userService.validateUser(username, password)) {
                User user = userService.getUserByUsername(username).orElse(null);
                this.username = username;
                connectedClients.put(username, this);

                Map<String, Object> data = new HashMap<>();
                data.put("success", true);
                data.put("userId", user.getId());
                data.put("username", user.getUsername());
                data.put("role", user.getRole());
                data.put("email", user.getEmail());

                return new Message("SUCCESS", data);
            } else {
                return createErrorResponse("Credențiale invalide!");
            }
        }

        private Message handleGetCars(Message message) {
            List<Car> cars = carService.getAllCars();
            Map<String, Object> data = new HashMap<>();
            data.put("cars", new ArrayList<>(cars));
            return new Message("SUCCESS", data);
        }

        private Message handleGetAvailableCars() {
            List<Car> cars = carService.getAvailableCars();
            Map<String, Object> data = new HashMap<>();
            data.put("cars", new ArrayList<>(cars));
            return new Message("SUCCESS", data);
        }

        private Message handleCreateRental(Message message) {
            Long clientId = ((Number) message.getData("clientId")).longValue();
            Long carId = ((Number) message.getData("carId")).longValue();

            // Folosește metoda pentru socket (fără date)
            rentalService.createRentalForSocket(clientId, carId);

            Map<String, Object> data = new HashMap<>();
            data.put("message", "Cerere creată cu succes!");
            return new Message("SUCCESS", data);
        }

        private Message handleGetRentals(Message message) {
            Long clientId = message.getData("clientId") != null ?
                    ((Number) message.getData("clientId")).longValue() : null;

            List<RentalRequest> rentals;
            if (clientId != null) {
                rentals = rentalService.getRentalsByClientId(clientId);
            } else {
                rentals = rentalService.getAllRentals();
            }

            Map<String, Object> data = new HashMap<>();
            data.put("rentals", new ArrayList<>(rentals));
            return new Message("SUCCESS", data);
        }

        private Message handleAddCar(Message message) {
            Car car = (Car) message.getData("car");
            Car saved = carService.createCar(car);

            Map<String, Object> data = new HashMap<>();
            data.put("car", saved);
            return new Message("SUCCESS", data);
        }

        private Message handleUpdateCar(Message message) {
            Long carId = ((Number) message.getData("carId")).longValue();
            Car carDetails = (Car) message.getData("car");

            Car updated = carService.updateCar(carId, carDetails);

            Map<String, Object> data = new HashMap<>();
            data.put("car", updated);
            return new Message("SUCCESS", data);
        }

        private Message handleDeleteCar(Message message) {
            Long carId = ((Number) message.getData("carId")).longValue();
            carService.deleteCar(carId);

            return new Message("SUCCESS", Map.of("message", "Autoturism șters!"));
        }

        private Message handleGetUsers() {
            List<User> users = userService.getAllUsers();
            Map<String, Object> data = new HashMap<>();
            data.put("users", new ArrayList<>(users));
            return new Message("SUCCESS", data);
        }

        private Message handleCreateUser(Message message) {
            User user = (User) message.getData("user");
            User created = userService.createUser(user);

            Map<String, Object> data = new HashMap<>();
            data.put("user", created);
            return new Message("SUCCESS", data);
        }

        private Message handleUpdateRentalStatus(Message message) {
            Long rentalId = ((Number) message.getData("rentalId")).longValue();
            String status = (String) message.getData("status");

            RentalRequest rental = rentalService.getRentalById(rentalId).orElse(null);
            if (rental == null) {
                return createErrorResponse("Cerere negăsită!");
            }

            if ("APPROVED".equals(status)) {
                rental = rentalService.approveRental(rentalId);
            } else if ("REJECTED".equals(status)) {
                rental = rentalService.rejectRental(rentalId);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("rental", rental);
            return new Message("SUCCESS", data);
        }

        private Message createErrorResponse(String errorMessage) {
            Map<String, Object> data = new HashMap<>();
            data.put("error", errorMessage);
            return new Message("ERROR", data);
        }

        private void cleanup() {
            try {
                if (username != null) {
                    connectedClients.remove(username);
                }
                if (socket != null) socket.close();
                if (input != null) input.close();
                if (output != null) output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
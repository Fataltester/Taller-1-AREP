
package com.edu.eci.httpserver;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
/**
 * @author juan.mmendez
 */
public class WebServer {
    /**
     * Main method that create the sockets and start the server at the 35000 port
     * @param args
     * @throws IOException
     * @throws URISyntaxException 
     */
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        Socket clientSocket = null;
        boolean running = true;
        while (running){
            try {
            System.out.println("Listo para recibir ...");
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String inputLine, outputLine;
        boolean isFirstLine = true;
        String method = null;
        URI requestUri = null;
        while ((inputLine = in.readLine()) != null) {
            if(isFirstLine){
                requestUri = new URI(inputLine.split(" ")[1]);
                System.out.println("Path: " + requestUri.getPath());
                isFirstLine = false;
                method = inputLine.split(" ")[0];
            }
            System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }
        if(requestUri.getPath().startsWith("/src/main/public")){
            ResponseService(requestUri,clientSocket,out);
        }else if(requestUri.getPath().startsWith("/hello") && method.equals("GET")){
            handleHello(requestUri, out);
        }else if(requestUri.getPath().startsWith("/hellopost") && method.equals("POST")){
            handleHelloPost(requestUri, out);
        }else{
            String responseMessage = "HTTP/1.1 404 Not Found\r\n" +
                             "Content-Type: text/plain\r\n" +
                             "Content-Length: 19\r\n" +
                             "\r\n" +
                             "Error 404: Not Found";
            out.write(responseMessage);
            out.flush();
        }
        out.close();
        in.close();
        clientSocket.close();
        
        }
        serverSocket.close();
    }
    /**
     * Handle the get response from the form 
     * @param requestUri
     * @param out 
     */
    private static void handleHello(URI requestUri, PrintWriter out) {
        String name = extractName(requestUri);

        String body = "<html>" +
                "<body>" +
                "<h2>Hola (GET), " + name + "!</h2>" +
                "<img src=\"/src/main/public/cat.jpg\" style=\"max-width:200px; margin-top:10px;\">" +
                "</body>" +
                "</html>";
        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + body.getBytes().length + "\r\n" +
                "\r\n" +
                body;
        out.write(response);
        out.flush();
    }
    /**
     * Handle the Post response from the form
     * @param requestUri
     * @param out 
     */
    private static void handleHelloPost(URI requestUri, PrintWriter out) {
        String name = extractName(requestUri);
        String html = "<html><body>" +
                    "<h2>Hola (POST), " + name + "!</h2>" +
                    "<img src=\"/src/main/public/cat.jpg\" alt=\"Mi Imagen\" style=\"max-width:200px;\">" +
                    "</body></html>";

        String response = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html; charset=UTF-8\r\n" +
                "Content-Length: " + html.getBytes().length + "\r\n" +
                "\r\n" +
                html;

        out.write(response);
        out.flush();
    }
    /**
     * Extract the parameter name from the url
     * @param requestUri
     * @return 
     */
    private static String extractName(URI requestUri) {
        String query = requestUri.getQuery(); // "name=John"
        String name = "Mundo";
        if (query != null && query.startsWith("name=")) {
            name = query.substring(5);
        }
        return name;
    }
    /**
     * This method response every request from the client related with the web application
     * @param requestUri
     * @param client
     * @param out
     * @throws IOException 
     */
    private static void ResponseService(URI requestUri, Socket client, PrintWriter out) throws IOException {
        String path = requestUri.getPath();
        path = path.substring(1);
        String extension = getFileExtension(path);
        String response = "";
        byte[] file = FileToBytes(path);
        if(extension.equals("html") || extension.equals("css") || extension.equals("js")){
                String fileToString = FileToString(path);
            response = BuildPetition(extension, response, fileToString);
            out.write(response);
        }else{
            response = BuildPetitionForImage(extension, response, file);
            out.write(response);
            out.flush();
            OutputStream os = client.getOutputStream();
            os.write(file);  // Enviar los bytes binarios de la imagen
            os.flush();
        }
    }
    /**
     * convert a file to a String
     * @param path
     * @return
     * @throws IOException 
     */
    private static String FileToString(String path) throws IOException {
    Path filePath = Paths.get(path);
    return new String(Files.readAllBytes(filePath), Charset.forName("UTF-8"));
    }
    /**
     * Convert A File to bytes
     * @param path
     * @return
     * @throws IOException 
     */
    private static byte[] FileToBytes(String path) throws IOException{
        try{
            Path filePath = Paths.get(path);
            return Files.readAllBytes(filePath);
        }catch(IOException e){
            System.err.println("Error del archivo a bytes");
            e.printStackTrace();
        }
        return null;
    }
    /**
     * Get the Extension of a file
     * @param filePath
     * @return 
     */
    public static String getFileExtension(String filePath){
        int lastIndexOfDot = filePath.lastIndexOf('.');
        return filePath.substring(lastIndexOfDot + 1);
    }
    /**
     * Build the petition for js, css or html files
     * @param extension
     * @param response
     * @param fileToString
     * @return 
     */
    private static String BuildPetition(String extension, String response, String fileToString) {
        String query = "";
        switch (extension) {
            case "html":
                query = "text/html";
                break;
            case "css":
                query = "text/css";
                break;
            case "js":
                query = "text/javascript";
                break;
            default:
                System.err.println("extensión no válida");
                System.exit(1);
        }
        response = "HTTP/1.1 200 OK\n\r"
        + "content-type: " + query + "\n\r"
        + "\n\r" + fileToString; 
        return response;
    }
    /**
     * Build the petition for an image
     * @param extension
     * @param response
     * @param file
     * @return 
     */
    private static String BuildPetitionForImage(String extension, String response, byte[] file) {
        String query = "";
        switch (extension) {
            case "png":
                query = "image/png";
                break;
            case "jpg":
                query = "image/jpeg";
                break;
            case "jpeg":
                query = "image/jpeg";
                break;
            case "gif":
                query = "image/gif";
                break;
            case "svg":
                query = "image/svg+xml";
                break;
            default:
                System.err.println("extensión no válida");
                System.exit(1);
        }
        response = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: " + query + "\r\n"
                + "Content-Length: " + file.length + "\r\n"
                + "\r\n"; 
        return response;
    }   
}

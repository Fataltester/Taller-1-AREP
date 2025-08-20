import com.edu.eci.httpserver.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.Socket;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class WebServerTest {

    @Test
    public void testExtractNameWithValidQuery() throws Exception {
        URI uri = new URI("/hello?name=Juan");
        String result = invokeExtractName(uri);
        assertEquals("Juan", result);
    }

    @Test
    public void testExtractNameWithoutQuery() throws Exception {
        URI uri = new URI("/hello");
        String result = invokeExtractName(uri);
        assertEquals("Mundo", result);
    }

    private String invokeExtractName(URI uri) throws Exception {
        var method = WebServer.class.getDeclaredMethod("extractName", URI.class);
        method.setAccessible(true);
        return (String) method.invoke(null, uri);
    }

    @Test
    public void testGetFileExtensionHtml() {
        assertEquals("html", WebServer.getFileExtension("index.html"));
    }

    @Test
    public void testGetFileExtensionImage() {
        assertEquals("jpg", WebServer.getFileExtension("cat.jpg"));
    }

    @Test
    public void testFileToStringReadsContent() throws Exception {
        Path tempFile = Files.createTempFile("test", ".txt");
        Files.write(tempFile, "Hola mundo".getBytes());
        var method = WebServer.class.getDeclaredMethod("FileToString", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(null, tempFile.toString());
        assertEquals("Hola mundo", result);
    }

    @Test
    public void testFileToBytesReadsBinary() throws Exception {
        Path tempFile = Files.createTempFile("test", ".bin");
        byte[] expected = {65, 66, 67}; // ABC
        Files.write(tempFile, expected);

        var method = WebServer.class.getDeclaredMethod("FileToBytes", String.class);
        method.setAccessible(true);
        byte[] result = (byte[]) method.invoke(null, tempFile.toString());

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBuildPetitionHtml() throws Exception {
        String html = "<h1>Hola</h1>";
        var method = WebServer.class.getDeclaredMethod("BuildPetition", String.class, String.class, String.class);
        method.setAccessible(true);
        String response = (String) method.invoke(null, "html", "", html);
        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("text/html"));
        assertTrue(response.contains(html));
    }

    @Test
    public void testBuildPetitionCss() throws Exception {
        String css = "body {color:red;}";
        var method = WebServer.class.getDeclaredMethod("BuildPetition", String.class, String.class, String.class);
        method.setAccessible(true);
        String response = (String) method.invoke(null, "css", "", css);
        assertTrue(response.contains("text/css"));
    }

    @Test
    public void testBuildPetitionForImagePng() throws Exception {
        byte[] fakeImage = {1, 2, 3, 4};
        var method = WebServer.class.getDeclaredMethod("BuildPetitionForImage", String.class, String.class, byte[].class);
        method.setAccessible(true);
        String response = (String) method.invoke(null, "png", "", fakeImage);
        assertTrue(response.contains("image/png"));
        assertTrue(response.contains("Content-Length: 4"));
    }

}
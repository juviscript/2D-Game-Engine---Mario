package Jubi;

import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;
import java.sql.SQLOutput;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;


public class Window {

    private int width, height;
    private String title;
    private long glfwWindow;
    private float r, g, b, a;
    private boolean fadeToBlack = false;

    private static Window window = null;

    private Window() {
        this.width = 1920;
        this.height = 1080;
        this.title = "Mario";
        r = 1;
        g = 1;
        b = 1;
        a = 1;
    }

    // The only time the window can be created is the first time we call the window object.
    // Singleton
    public static Window get() {
        if (Window.window == null) {
            Window.window = new Window();
        }

        return Window.window;
    }

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        init();
        loop();

        // Free the memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        glfwDestroyWindow(glfwWindow);
    }

    public void init() {
        // Setup error callback - Where GLFW Error prints error to
        // Free the window callbacks and destroy the window
        GLFWErrorCallback.createPrint(System.err).set();

        // Initialize GLFW
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        // Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);   // Window is invisible until properly initialized
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);  // Makes sure window is maximized by default

        // Create the window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.title, NULL, NULL); // Returns long, which is a number where the window is in our memory space
        if (glfwWindow == NULL) {
            throw new IllegalStateException("Unable to create the GLFW window");
        }

        // Event listeners
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);

        // Make the OpenGL context current
        glfwMakeContextCurrent(glfwWindow);

        // Enable V-Sync
        glfwSwapInterval(1);

        // Make window visible
        glfwShowWindow(glfwWindow);

        // Critical*** LWJGL detects the context that is current in the current thread, creates the GLCapabilities instance and makes the OpenGL bindings available for use.
        GL.createCapabilities();
    }

    public void loop() {
        while (!glfwWindowShouldClose(glfwWindow)) {
            // Poll events
            glfwPollEvents();

            // Red!
            glClearColor(r, g, b, a);
            glClear((GL_COLOR_BUFFER_BIT));     // Takes color above and flushes to screen

            if (fadeToBlack) {
                r = Math.max(r - 0.01f, 0);
                g = Math.max(r - 0.01f, 0);
                b = Math.max(r - 0.01f, 0);
            }

            if (KeyListener.isKeyPressed((GLFW_KEY_SPACE))) {
                fadeToBlack = true;
            }

            glfwSwapBuffers(glfwWindow);
        }
    }
}

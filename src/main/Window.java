package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class Window extends JFrame implements Runnable{
	
	public static final int WIDTH = 800, HEIGHT = 600; //Dimensiones de la ventana
	private Canvas canvas;
	private Thread thread; //Hilo para ejecutar el juego
	private boolean running = false;
	
	private BufferStrategy bs; // Estrategia de doble/triple buffer
	private Graphics g; //Objeto para dibujar
	
	private final int FPS = 60; //Fotogramas por segundo objetivo
	private double TARGETTIME = 1000000000/FPS;
	private double delta = 0; //Acumulador de tiempo
	private int AVERAGEFPS = FPS;
	
	public Window()
	{
		setTitle("Juego de Asteroides");
		setSize(WIDTH, HEIGHT); //Dimensiones de la ventana
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //Cierra el programa al cerrar la ventana
		setResizable(false); //No se puede redimensionar
		setLocationRelativeTo(null); //Centrar la ventana
		setVisible(true); //Mostrar la ventana
		
		canvas = new Canvas();
		
		canvas.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		canvas.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		canvas.setMinimumSize(new Dimension(WIDTH, HEIGHT)); 
		canvas.setFocusable(true); // Permite que reciba entradas de teclado
		
		add(canvas); // Agrega el canvas al JFrame
	}
	
	public static void main(String[] args) {
		new Window().start();
	}
	
	int x = 0;
	private void update(){
		x++;
	}

	private void draw(){
		bs = canvas.getBufferStrategy();
		
		if(bs == null){
			canvas.createBufferStrategy(3);
			return;
		}
		
		g = bs.getDrawGraphics(); // Obtiene el contexto gráfico
		
		//-----------------------
		
		g.clearRect(0, 0, WIDTH, HEIGHT); // Limpia la pantalla
		g.setColor(Color.BLACK);
		g.drawString(""+AVERAGEFPS, 10, 20); //Muestra los FPS en la pantalla
		

		g.dispose(); // Libera recursos del gráfico
		bs.show(); // Muestra el buffer en pantalla
	}
	
	@Override
	public void run(){
		long now = 0;
		long lastTime = System.nanoTime(); // Tiempo inicial en nanosegundos
		int frames = 0; // Contador de frames
		long time = 0; // Acumulador de tiempo
		
		while(running)
		{
			now = System.nanoTime(); // Tiempo actual
			delta += (now - lastTime)/TARGETTIME;
			time += (now - lastTime);
			lastTime = now;
			
			if(delta >= 1){		
				update();
				draw();
				delta --;
				frames ++;
			}
                        
			if(time >= 1000000000){
				AVERAGEFPS = frames;
				frames = 0;
				time = 0;
			}
		}
		stop();
	}
	
	private void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}
        
	private void stop(){
		try {
			thread.join();
			running = false; 
		} catch (InterruptedException e) {
			e.printStackTrace(); // Muestra error si falla el join
		}
	}
}
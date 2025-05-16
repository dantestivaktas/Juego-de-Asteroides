package main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

public class Window extends JFrame implements Runnable{
	
	public static final int WIDTH = 800, HEIGHT = 600; // Tamaño de la ventana
	private Canvas canvas; //Área de dibujo
	private Thread thread; //Hilo para ejecutar el juego
	private boolean running = false; //Flag para saber si el juego está corriendo
	
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
		canvas.setFocusable(true); // Permite que el canvas reciba eventos de teclado
		
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
		bs = canvas.getBufferStrategy(); // Obtiene la estrategia de buffer
		
		if(bs == null){
			canvas.createBufferStrategy(3); // Si no existe, crea una con triple buffer
			return;
		}
		
		g = bs.getDrawGraphics(); // Obtiene el contexto gráfico
		
		//-----------------------
		
		g.clearRect(0, 0, WIDTH, HEIGHT); // Limpia la pantalla
		
		g.setColor(Color.BLACK); // Setea el color a negro
		
		g.drawString(""+AVERAGEFPS, 10, 20); // Dibuja el FPS en pantalla
		

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
			delta += (now - lastTime)/TARGETTIME; // Calcula cuántos updates deberían hacerse
			time += (now - lastTime); // Acumula tiempo para medir los FPS
			lastTime = now; // Actualiza el tiempo anterior
			
			if(delta >= 1){		
				update(); // Actualiza lógica del juego
				draw(); // Dibuja en pantalla
				delta --;
				frames ++; // Cuenta el frame actual
			}
                        
			if(time >= 1000000000){
				AVERAGEFPS = frames; // Calcula el FPS promedio cada segundo
				frames = 0;
				time = 0;
			}
		}
		stop(); // Cuando termina el bucle, detiene el hilo
	}
	
	private void start(){
		thread = new Thread(this); // Crea un nuevo hilo con el método run
		thread.start(); // Inicia el hilo
		running = true; // Activa el flag de ejecución
	}
        
	private void stop(){
		try {
			thread.join(); // Espera que el hilo termine
			running = false; // Marca que ya no está corriendo
		} catch (InterruptedException e) {
			e.printStackTrace(); // Muestra error si falla el join
		}
	}
}
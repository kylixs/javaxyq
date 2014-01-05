package com.javaxyq.util;

/*************************************************************************
 *  Compilation:  javac -classpath .:jl1.0.jar MP3.java         (OS X)
 *                javac -classpath .;jl1.0.jar MP3.java         (Windows)
 *  Execution:    java -classpath .:jl1.0.jar MP3 filename.mp3  (OS X / Linux)
 *                java -classpath .;jl1.0.jar MP3 filename.mp3  (Windows)
 *  
 *  Plays an MP3 file using the JLayer MP3 library.
 *
 *  Reference:  http://www.javazoom.net/javalayer/sources.html
 *
 *
 *  To execute, get the file jl1.0.jar from the website above or from
 *
 *      http://www.cs.princeton.edu/introcs/24inout/jl1.0.jar
 *
 *  and put it in your working directory with this file MP3.java.
 *
 *************************************************************************/

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javazoom.jl.player.Player;

import com.javaxyq.core.Toolkit;

public class MP3Player {

	private static Map<String, Player> playersMap = new HashMap<String, Player>();
	private static Player loopplayer;
	private static Thread loopThread;
	private static String loopfile;

	public static void stopAll() {
		Collection<Player> players = playersMap.values();
		for (Player player : players) {
			player.close();
		}
	}

	public static void stop(String filename) {
		Player player = playersMap.get(filename);
		if (player != null)
			player.close();
	}

	// play the MP3 file to the sound card
	public static void play(String filename) {
		try {
			BufferedInputStream bis = new BufferedInputStream(Toolkit.getInputStream(filename));

			final Player player = new Player(bis);
			playersMap.put(filename, player);
			// run in new thread to play in background
			new Thread() {
				public void run() {
					try {
						player.play();
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}.start();
		} catch (Exception e) {
			System.out.println("Problem playing file " + filename);
			System.out.println(e);
		}

	}

	/**
	 * —≠ª∑≤•∑≈“Ù¿÷
	 * 
	 * @param filename
	 */
	public static void loop(String filename) {
		try {
			BufferedInputStream bis = new BufferedInputStream(Toolkit.getInputStream(filename));

			if (loopplayer != null)
				loopplayer.close();
			loopplayer = new Player(bis);
			loopfile = filename;
			// run in new thread to play in background
			if (loopThread == null) {
				loopThread = new Thread() {
					public void run() {
						try {
							while (true) {
								loopplayer.play();
								if (loopplayer.isComplete()) {
									loopplayer = new Player(new BufferedInputStream(new FileInputStream(loopfile)));
								}
								Thread.sleep(500);
							}
						} catch (Exception e) {
							System.out.println(e);
						}
					}
				};
				loopThread.start();
			}
		} catch (Exception e) {
			System.out.println("Problem playing file " + filename);
			System.out.println(e);
		}

	}

	// test client
	public static void main(String[] args) {
		String filename = "resources/music/wzg.mp3";
		MP3Player mp3 = new MP3Player();
		mp3.play(filename);

		// do whatever computation you like, while music plays
		int N = 4000;
		double sum = 0.0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				sum += Math.sin(i + j);
			}
		}
		System.out.println(sum);

		// when the computation is done, stop playing it
		mp3.stopAll();

		// play from the beginning
		mp3.play(filename);

	}

	/**
	 * 
	 */
	public static void stopLoop() {
		if (loopplayer != null)
			loopplayer.close();
	}

}

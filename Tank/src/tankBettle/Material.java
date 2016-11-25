package tankBettle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;

class Veriable{
	int x=0,y=0;
	int speed=4;
	int direction;
	boolean life=true;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	public Veriable(int x,int y) {
		this.x=x;
		this.y=y;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}
}
class MyTank extends Veriable{
	Vector<bullet> bs=new Vector<bullet>();
	bullet bu=null;
	public MyTank(int x, int y) {
		super(x, y);
	}
	public void forward() {
		if (y>10) {
			y-=speed;			
		}
	}
	public void backward() {
		if (y<330) {
			y+=speed;			
		}
	}
	public void left() {
		if (x>10) {
			x-=speed;			
		}
	}
	public void right() {
		if (x<370) {
			x+=speed;			
		}
	}
	public void shoot() {
		switch (this.direction) {
		case 1://forward
			bu=new bullet(x+8, y-8, 1);
			bs.add(bu);
			break;
		case 2://backward
			bu=new bullet(x+8, y+26, 2);
			bs.add(bu);
			break;
		case 3://left
			bu=new bullet(x-8, y+8, 3);
			bs.add(bu);
			break;
		case 4://right
			bu=new bullet(x+26, y+8, 4);
			bs.add(bu);
			break;
		}
		Thread t=new Thread(bu);
		t.start();
	}
}
class EnemyTank extends Veriable implements Runnable{
	int speed=4;
	int time=0;
	Vector<bullet> ebs=new Vector<bullet>();
	Vector<EnemyTank> etk=new Vector<EnemyTank>();
	public EnemyTank(int x, int y) {
		super(x, y);
	}
	public void etkvec(Vector<EnemyTank> etkv) {
		this.etk=etkv;
	}
	public boolean collied() {//let enemy tank can not move over each other 
		boolean b=false;
		switch (this.direction) {
		case 1:
			for (int i = 0; i < etk.size(); i++) {
				EnemyTank et=etk.get(i);
				if (et!=this) {
					if (et.direction==1||et.direction==2) {
						if ((this.x-4>=et.x-4 && this.x-4<=et.x+24 && this.y-5>=et.y-5 && this.y-5<=et.y+25)||
							(this.x+24>=et.x-4 && this.x+24<=et.x+24 && this.y-5>=et.y-5 && this.y-5<=et.y+25)) {
							return true;
						}
					}
					if (et.direction==3||et.direction==4) {
						if ((this.x-4>=et.x-5 && this.x-4<=et.x+25 && this.y-5>=et.y-4 && this.y-5<=et.y+24)||
							(this.x+24>=et.x-5 && this.x+24<=et.x+25 && this.y-5>=et.y-4 && this.y-5<=et.y+24)) {
							return true;
						}
					}
				}
			}
			break;
		case 2:
			for (int i = 0; i < etk.size(); i++) {
				EnemyTank et=etk.get(i);
				if (et!=this) {
					if (et.direction==1||et.direction==2) {
						if ((this.x-4>=et.x-4 && this.x-4<=et.x+24 && this.y+25>=et.y-5 && this.y+25<=et.y+25)||
							(this.x+24>=et.x-4 && this.x+24<=et.x+24 && this.y+25>=et.y-5 && this.y+25<=et.y+25)) {
							return true;
						}
					}
					if (et.direction==3||et.direction==4) {
						if ((this.x-4>=et.x-5 && this.x-4<=et.x+25 && this.y+25>=et.y-4 && this.y+25<=et.y+24)||
							(this.x+24>=et.x-5 && this.x+24<=et.x+25 && this.y+25>=et.y-4 && this.y+25<=et.y+24)) {
							return true;
						}
						
					}
				}
			}
			break;
		case 3:
			for (int i = 0; i < etk.size(); i++) {
				EnemyTank et=etk.get(i);
				if (et!=this) {
					if (et.direction==1||et.direction==2) {
						if ((this.x-5>=et.y-4 && this.x-5<=et.x+24 && this.y-4>=et.y-5 && this.y-4<=et.y+25)||
							(this.x-5>=et.y-4 && this.x-5<=et.x+24 && this.y+24>=et.y-5 && this.y+24<=et.y+25)) {
							return true;
						}
					}
					if (et.direction==3||et.direction==4) {
						if ((this.x-5>=et.y-5 && this.x-5<=et.x+25 && this.y-4>=et.y-4 && this.y-4<=et.y+24)||
							(this.x-5>=et.y-5 && this.x-5<=et.x+25 && this.y+24>=et.y-4 && this.y+24<=et.y+24)) {
							return true;
						}
					}
				}
			}
			break;
		case 4:
			for (int i = 0; i < etk.size(); i++) {
				EnemyTank et=etk.get(i);
				if (et!=this) {
					if (et.direction==1||et.direction==2) {
						if ((this.x+25>=this.x-4 && this.x+25<=this.x+24 && this.y-4>=et.y-5 && this.y-4<=et.y+25)||
						    (this.x+25>=this.x-4 && this.x+25<=this.x+24 && this.y+24>=et.y-5 && this.y+24<=et.y+25)) {
						    	return true;
						}
					}
					if (et.direction==3||et.direction==4) {
						if ((this.x+25>=this.x-5 && this.x+25<=this.x+25 && this.y-4>=et.y-4 && this.y-4<=et.y+24)||
							(this.x+25>=this.x-5 && this.x+25<=this.x+25 && this.y+24>=et.y-4 && this.y+24<=et.y+2)) {
							    	return true;
						}
					}
				}
			}
			break;
			
		default:
			break;
		}
		return b;
	}

	public void run() {
		while (true) {
			switch (this.direction) {//enemy tank move
			case 1:
				for (int i = 0; i < 20; i++) {
					if (y>10 && !collied()) {//border
						y-=speed;						
					}
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				break;
			case 2:
                for (int i = 0; i < 20; i++) {
                	if (y<330 && !collied()) {
                		y+=speed;
					}
                	try {
                		Thread.sleep(50);
                	} catch (InterruptedException e) {
                		e.printStackTrace();
                	}
				}
				break;
			case 3:
                for (int i = 0; i < 20; i++) {	
                	if (x>10 && !collied()) {
                		x-=speed;						
					}
                	try {
                		Thread.sleep(50);
                	} catch (InterruptedException e) {
                		e.printStackTrace();
                	}
				}
				break;
			case 4:
                for (int i = 0; i < 20; i++) {	
                	if (x<370 && !collied()) {
                		x+=speed;						
					}
                	try {
                		Thread.sleep(50);
                	} catch (InterruptedException e) {
                		e.printStackTrace();
                	}
				}
				break;
			default:
				break;
			}
			this.direction=(int)(Math.random()*4+1);//random enemy direction
			if (this.life==false) {
				break;
			}
			this.time++;
			if (time%2==0) {
				if (life) {
					if (ebs.size()<5) {
						bullet bu=null;
						switch (this.direction) {
						case 1://forward
							bu=new bullet(x+8, y-8, 1);
							ebs.add(bu);
							break;
						case 2://backward
							bu=new bullet(x+8, y+26, 2);
							ebs.add(bu);
							break;
						case 3://left
							bu=new bullet(x-8, y+8, 3);
							ebs.add(bu);
							break;
						case 4://right
							bu=new bullet(x+26, y+8, 4);
							ebs.add(bu);
							break;
						}
						Thread t1=new Thread(bu);
						t1.start();
					}
				}
			}
		}
	}
	
}
class bullet implements Runnable {
	int x;
	int y;
	int direction;
	int speed=10;
	boolean life=true;
	
	public bullet(int x,int y,int direction) {
		this.x=x;
		this.y=y;
		this.direction=direction;
	}
	public void run() {
		while (true) {
			try {
				Thread.sleep(50);
			} catch (Exception e) {
				
			}
		    switch(direction){
		    case 1:
		    	y-=speed;
		    	break;
		    case 2:
		    	y+=speed;
		    	break;
		    case 3:
		    	x-=speed;
		    	break;
		    case 4:
		    	x+=speed;
		    	break;
		    }
		    if (x<0||y<0||x>400||y>360) {
				this.life=false;
				break;
			}
		}
	}
}
class Explode{
	int x,y;
	int lifetime=3;
	boolean life=true;
	public Explode(int x,int y){
		this.x=x;
		this.y=y;
	}
	public void lifeT() {
		if (lifetime>0) {
			lifetime--;
		}
		else {
			this.life=false;
		}
	}
}
class location{
	int x;
	int y;
	int direction;
	public location(int x,int y, int direction) {
		this.x=x;
		this.y=y;
		this.direction=direction;
	}
}
class Record{
	private static int totalKills=0;
	private static FileWriter fw;
	private static BufferedWriter bw;
	private static FileReader fr;
	private static BufferedReader br;
	private static Vector<EnemyTank> etk=new Vector<EnemyTank>();
	static Vector<location> locate=new Vector<location>();

	public Vector<EnemyTank> getEtk() {
		return etk;
	}
	public void setEtk(Vector<EnemyTank> etk) {
		Record.etk = etk;
	}
	public static int getTotalKills() {
		return totalKills;
	}
	public static void setTotalKills(int totalKills) {
		Record.totalKills = totalKills;
	}
	public static void killsRecord() {
		totalKills++;
	}
	public static void writeKills() {
		try {
			fw= new FileWriter("data/gameData.txt");
			bw= new BufferedWriter(fw);
			bw.write(totalKills+"\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void readKills() {
		try {
			fr=new FileReader("data/gameData.txt");
			br=new BufferedReader(fr);
			String s=br.readLine();
			totalKills=Integer.parseInt(s);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void saveExit() {
		try {
			fw= new FileWriter("data/gameData.txt");
			bw= new BufferedWriter(fw);
			bw.write(totalKills+"\r\n");
			for (int i = 0; i < etk.size(); i++) {
				EnemyTank et=etk.get(i);
				if (et.life) {
					String loc=et.x+" "+et.y+" "+et.direction+"";
					bw.write(loc+"\r\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				bw.flush();
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static Vector<location> resume() {
		try {
			fr=new FileReader("data/gameData.txt");
			br=new BufferedReader(fr);
			String s=br.readLine();
			totalKills=Integer.parseInt(s);
			while ((s=br.readLine())!=null) {
				String[] ary=s.split(" ");
				location loc=new location(Integer.parseInt(ary[0]), Integer.parseInt(ary[1]), Integer.parseInt(ary[2]));
				locate.add(loc);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return locate;
	}
}
class Sound extends Thread{
	private String fileName;
	public Sound(String audioFileName) {
		fileName=audioFileName;
	}
	public void run() {
		File file=new File(fileName);
		AudioInputStream ais = null;
		try {
			ais=AudioSystem.getAudioInputStream(file);
		} catch (UnsupportedAudioFileException | IOException e) {
			e.printStackTrace();
		}
		
		AudioFormat format=ais.getFormat();
		DataLine.Info info=new DataLine.Info(SourceDataLine.class, format);
		SourceDataLine sdl = null;
		
		try {
			sdl=(SourceDataLine)AudioSystem.getLine(info);
			sdl.open(format);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
		sdl.start();
		
		int b=0;
		byte[] bufferb=new byte[1024];
		try {
			while (b!=-1) {
				b=ais.read(bufferb, 0, bufferb.length);
				if (b>=0) {
					sdl.write(bufferb, 0, b);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally {
			sdl.drain();
			sdl.close();
		}
	}
}
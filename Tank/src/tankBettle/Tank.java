package tankBettle;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JButton;
import java.awt.SystemColor;

class MyPanel extends JPanel implements KeyListener, Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MyTank mt=null;
	EnemyTank et=null;
	Vector<EnemyTank> etk=new Vector<EnemyTank>();
	Vector<Explode> exp=new Vector<Explode>();
	Vector<location> locate=new Vector<location>();
	int enemyTankAmount=5;
	
	Image im1=null;
	Image im2=null;
	Image im3=null;
	
	public MyPanel(String gameType) {
		if (gameType.equals("newGame")) {
			mt=new MyTank(150,334);
			for(int i=0;i<enemyTankAmount;i++){
			    et=new EnemyTank(i*92+5, 6);
//				EnemyTank et=new EnemyTank(i*185+5, 6);
				et.setDirection(2);
				et.etkvec(etk);
				Thread ttank=new Thread(et);
				ttank.start();
				bullet bu=new bullet(et.x+8, et.y+26, 2);
				et.ebs.add(bu);
				Thread tbu=new Thread(bu);
				tbu.start();
				etk.add(et);
			}
		}else if (gameType.equals("resume")) {
			mt=new MyTank(150,334);
			locate=Record.resume();
			for (int i = 0; i < locate.size(); i++) {
				location loc=locate.get(i);
				EnemyTank et=new EnemyTank(loc.x, loc.y);
				et.setDirection(loc.direction);
				et.etkvec(etk);
				Thread ttank=new Thread(et);
				ttank.start();
				bullet bu=new bullet(et.x+8, et.y+26, 2);
				et.ebs.add(bu);
				Thread tbu=new Thread(bu);
				tbu.start();
				etk.add(et);
			}
		}
		im1=new ImageIcon("image/1.png").getImage();
		im2=new ImageIcon("image/2.png").getImage();
		im3=new ImageIcon("image/3.png").getImage();
		Sound sound=new Sound("sound/aonlin.wav");
		Sound sound2=new Sound("sound/Infantryattacks.wav");
		sound.start();
		sound2.start();
	}
	
	public void paintComponent(Graphics g) {//right side component
		super.paintComponent(g);
		g.setColor(Color.BLACK);
		g.setFont(new Font("Segoe UI", Font.BOLD, 18));
		g.drawString("TOTAL KILLS: ", 430, 90);
		g.drawString(Record.getTotalKills()+"", 560, 90);
		g.drawString("Direction:", 430, 200);
		g.drawString("W", 550, 200);
		g.drawString("S", 552, 220);
		g.drawString("A", 530, 220);
		g.drawString("D", 572, 220);
		g.drawString("Shoot: K", 430, 250);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 360);
		if (mt.life) {
			this.drawTank(mt.getX(), mt.getY(), g, 1, mt.direction);//my tank
		}
		for (int i = 0; i < etk.size(); i++) {//enemy tank
			EnemyTank et=etk.get(i);
			if (et.life==true) {
				this.drawTank(et.getX(), et.getY(), g, 2, et.direction);
				for (int j = 0; j <et.ebs.size(); j++) {//bullet
					bullet etbu=et.ebs.get(j);
					if (etbu.life==true) {
						g.setColor(Color.white);
						g.fill3DRect(etbu.x, etbu.y, 4, 4, true);
					}
					else {
						et.ebs.remove(etbu);						
					}
				}
			}
		}
		for (int i = 0; i <mt.bs.size(); i++) {//bullet
			bullet bu=mt.bs.get(i);
			if (bu!=null&&bu.life==true) {
				g.setColor(Color.white);
				g.fill3DRect(bu.x, bu.y, 4, 4, true);
//			this.repaint();
			}
			if (bu.life==false) {
				mt.bs.remove(bu);
			}
		}
		for (int i = 0; i < exp.size(); i++) {//explode effect
			Explode ex=exp.get(i);
			if (ex.lifetime>2) {
				g.drawImage(im1, ex.x, ex.y, 30, 30, this);
			}else if (ex.lifetime>1) {
				g.drawImage(im2, ex.x, ex.y, 30, 30, this);
			}else {
				g.drawImage(im3, ex.x, ex.y, 30, 30, this);
			}
			ex.lifeT();
			if (ex.lifetime==0) {
				exp.remove(ex);
			}
		}
	}
	
	
	public void drawTank(int x,int y,Graphics g,int type,int direction) {
		switch (type) {
		case 1://my tank
			g.setColor(Color.orange);
			break;
		case 2://enemy tank
			g.setColor(Color.lightGray);
			break;
		}
		
		switch (direction){
		case 1://forward
			g.fill3DRect(x-4, y-5, 8, 30, true);
			g.fill3DRect(x+16, y-5, 8, 30, true);
			g.fill3DRect(x+8, y-7, 4, 8, true);
			g.fill3DRect(x, y, 10, 10, true);
			g.fill3DRect(x+10, y, 10, 10, true);
			g.fill3DRect(x, y+10, 10, 10, true);
			g.fill3DRect(x+10, y+10, 10, 10, true);
			break;
		case 2://backward
			g.fill3DRect(x-4, y-5, 8, 30, true);
			g.fill3DRect(x+16, y-5, 8, 30, true);
			g.fill3DRect(x+8, y+19, 4, 8, true);
			g.fill3DRect(x, y, 10, 10, true);
			g.fill3DRect(x+10, y, 10, 10, true);
			g.fill3DRect(x, y+10, 10, 10, true);
			g.fill3DRect(x+10, y+10, 10, 10, true);
			break;
		case 3://left
			g.fill3DRect(x-5, y-4, 30, 8, true);
			g.fill3DRect(x-5, y+16, 30, 8, true);
			g.fill3DRect(x-7, y+8, 8, 4, true);
			g.fill3DRect(x, y, 10, 10, true);
			g.fill3DRect(x+10, y, 10, 10, true);
			g.fill3DRect(x, y+10, 10, 10, true);
			g.fill3DRect(x+10, y+10, 10, 10, true);
			break;
		case 4://right
			g.fill3DRect(x-5, y-4, 30, 8, true);
			g.fill3DRect(x-5, y+16, 30, 8, true);
			g.fill3DRect(x+19, y+8, 8, 4, true);
			g.fill3DRect(x, y, 10, 10, true);
			g.fill3DRect(x+10, y, 10, 10, true);
			g.fill3DRect(x, y+10, 10, 10, true);
			g.fill3DRect(x+10, y+10, 10, 10, true);
			break;
		default:
			g.fill3DRect(x-4, y-5, 8, 30, true);
			g.fill3DRect(x+16, y-5, 8, 30, true);
			g.fill3DRect(x+8, y-7, 4, 8, true);
			g.fill3DRect(x, y, 10, 10, true);
			g.fill3DRect(x+10, y, 10, 10, true);
			g.fill3DRect(x, y+10, 10, 10, true);
			g.fill3DRect(x+10, y+10, 10, 10, true);
			break;
		}
	}
	
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()==KeyEvent.VK_W) {
			this.mt.setDirection(1);
			this.mt.forward();
		}
		else if (e.getKeyCode()==KeyEvent.VK_S) {
			this.mt.setDirection(2);
			this.mt.backward();
		}
		else if (e.getKeyCode()==KeyEvent.VK_A) {
			this.mt.setDirection(3);
			this.mt.left();
		}
		else if (e.getKeyCode()==KeyEvent.VK_D) {
			this.mt.setDirection(4);
			this.mt.right();
		}
		if (e.getKeyCode()==KeyEvent.VK_K){
			if (mt.life) {
				if (this.mt.bs.size()<20) {
					this.mt.shoot();
					Sound sound=new Sound("sound/fire.wav");
					sound.start();
				}
			}
		}
		this.repaint();
	}
	public void keyReleased(KeyEvent e) {
		
	}
	public void keyTyped(KeyEvent e) {
	}
	
	public boolean hit(bullet bu,Veriable et) {
		boolean hb=false;
		switch (bu.direction) {
		case 1:
		case 2:
			if (bu.x+4>et.x-4 && bu.y+4>et.y-5 && bu.x<et.x+24 && bu.y<et.y+25 ) {
				bu.life=false;
				et.life=false;
				hb=true;
				Explode ex=new Explode(et.x, et.y);
				exp.add(ex);
				Sound sound=new Sound("sound/explod.wav");
				sound.start();
			}
			break;
		case 3:
		case 4:
			if (bu.x+4>et.x-5 && bu.y+4>et.y-4 && bu.x<et.x+25 && bu.y<et.y+24 ) {
				bu.life=false;
				et.life=false;
				hb=true;
				Explode ex=new Explode(et.x, et.y);
				exp.add(ex);
				Sound sound=new Sound("sound/explod.wav");
				sound.start();
			}
			break;
		}
		return hb;
	}
	public void hitEnemy() {
		for (int i = 0; i < mt.bs.size(); i++) {
			bullet bu = mt.bs.get(i);
			if (bu.life) {
				for (int j = 0; j < etk.size(); j++) {
					EnemyTank et=etk.get(j);
					if (et.life) {
						if (this.hit(bu, et)) {
							Record.killsRecord();
						}
					}
				}
			}
			this.repaint();
		}
	}
	public void hitMy() {
		for (int i = 0; i < this.etk.size(); i++) {
			EnemyTank et = etk.get(i);
			for (int j = 0; j < et.ebs.size(); j++) {
				bullet bu=et.ebs.get(j);
				if (mt.life) {
					this.hit(bu, mt);					
				}
			}
		}
	}
	
    public void run() {
		while (true) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				
			}
			this.hitEnemy();
			this.hitMy();
			this.repaint();
		}
	}
}
class LevelPanel extends JPanel implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int time=0;
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 360);
		if (time%2==0) {
			g.setColor(Color.lightGray);
			g.setFont(new Font("Segoe UI", Font.BOLD, 42));
			g.drawString("Level 1", 140, 170);
		}
	}
	public void run() {
		while (true) {
			try {
				Thread.sleep(700);//set the time of "level 1" show and hide
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			time++;
			this.repaint();
		}
	}
}

class Lose extends JPanel implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int time=0;
	public void paint(Graphics g) {
		super.paint(g);
		g.fillRect(0, 0, 400, 360);
		if (time%2==0) {
			g.setColor(Color.lightGray);
			g.setFont(new Font("Segoe UI", Font.BOLD, 42));
			g.drawString("LOSE", 140, 170);
			g.setFont(new Font("Segoe UI", Font.PLAIN, 30));
			g.drawString("Press 'New Game' to start", 100, 190);
		}
	}
	public void run() {
		while (true) {
			try {
				Thread.sleep(700);//set the time of "Lose" show and hide
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			time++;
			this.repaint();
		}
	}
}

public class Tank extends JFrame implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	MyPanel mp;
	LevelPanel lp;
	Lose lose;
	MyTank mt;
	JMenuBar menuBar;
	private JButton btnNewGame;
	private JButton btnExit;
	private JButton btnSaveAndExit;
	private JButton btnResume;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Tank frame = new Tank();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Tank() {
		getContentPane().setFont(new Font("Consolas", Font.PLAIN, 12));
		menuBar = new JMenuBar();
		menuBar.setBackground(SystemColor.menu);
		menuBar.setFont(new Font("Consolas", Font.PLAIN, 12));
		getContentPane().add(menuBar, BorderLayout.NORTH);
		
//		JMenu mnNewMenu = new JMenu("New menu");
//		menuBar.add(mnNewMenu);
//		
//		mntmNewMenuItem = new JMenuItem("New menu item");
//		mntmNewMenuItem.addActionListener(this);
//		mntmNewMenuItem.setActionCommand("ng");
//		mnNewMenu.add(mntmNewMenuItem);
		
		btnNewGame = new JButton("New Game(N)");//new game button
		btnNewGame.setToolTipText("Alt+n");
		btnNewGame.setFocusable(false);
		btnNewGame.setMnemonic('n');
		btnNewGame.setFocusPainted(false);
		btnNewGame.setBorderPainted(false);
		btnNewGame.setFont(new Font("Consolas", Font.PLAIN, 12));
		btnNewGame.setBackground(SystemColor.menu);
		btnNewGame.addActionListener(this);
		btnNewGame.setActionCommand("newGame");
		menuBar.add(btnNewGame);
		
		btnExit = new JButton("Exit(E)");//exit button
		btnExit.setToolTipText("Alt+e");
		btnExit.setFocusable(false);//let key can be used to control tank
		btnExit.setFocusPainted(false);
		btnExit.setBorderPainted(false);
		btnExit.setMnemonic('e');
		btnExit.setFont(new Font("Consolas", Font.PLAIN, 12));
		btnExit.setBackground(SystemColor.menu);
		btnExit.addActionListener(this);
		btnExit.setActionCommand("exit");
		menuBar.add(btnExit);
		
		btnSaveAndExit = new JButton("Save and Exit(S)");
		btnSaveAndExit.setToolTipText("Alt+s");
		btnSaveAndExit.setFocusable(false);
		btnSaveAndExit.setFocusPainted(false);
		btnSaveAndExit.setBorderPainted(false);
		btnSaveAndExit.setMnemonic('s');
		btnSaveAndExit.setFont(new Font("Consolas", Font.PLAIN, 12));
		btnSaveAndExit.setBackground(SystemColor.menu);
		btnSaveAndExit.addActionListener(this);
		btnSaveAndExit.setActionCommand("saveExit");
		menuBar.add(btnSaveAndExit);
		
		btnResume = new JButton("Resume(R)");
		btnResume.setToolTipText("Alt+r");
		btnResume.setFocusable(false);
		btnResume.setFocusPainted(false);
		btnResume.setBorderPainted(false);
		btnResume.setMnemonic('r');
		btnResume.setFont(new Font("Consolas", Font.PLAIN, 12));
		btnResume.setBackground(SystemColor.menu);
		btnResume.addActionListener(this);
		btnResume.setActionCommand("resume");
		btnResume.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==1) {//this button will paint new tank each time on click, so only let it been clicked once 
					btnResume.setEnabled(false);
				}
			}
		});
		menuBar.add(btnResume);
		
		lp=new LevelPanel();
		Record.readKills();//to put in here so the "total kills" won't lose if exit without start new game
		getContentPane().add(lp);
		Thread t=new Thread(lp);
		t.start();
				
//		mp=new MyPanel();
//		this.remove(lp);
//		getContentPane().add(mp);
//		this.addKeyListener(mp);
//		Thread t=new Thread(mp);
//		t.start();
		
		setTitle("TANK BATTLE");
		setIconImage(Toolkit.getDefaultToolkit().getImage("image/icon.png"));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setResizable(false);
		setBounds(380, 140, 621, 414);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
//		setContentPane(contentPane);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("newGame")) {
			if (mp!=null) {
				this.remove(mp);
			}else if (lose!=null) {
				this.remove(lose);
			}
			mp=new MyPanel("newGame");
			this.remove(lp);
			getContentPane().add(mp);
			this.addKeyListener(mp);
			Thread t=new Thread(mp);
			t.start();
			this.setVisible(true);
		}else if (e.getActionCommand().equals("exit")) {
			Record.writeKills();
			System.exit(0);
		}else if (e.getActionCommand().equals("saveExit")) {
			Record record=new Record();
			record.setEtk(mp.etk);
			record.saveExit();
			System.exit(0);
		}else if (e.getActionCommand().equals("resume")) {
			if (mp!=null) {
				this.remove(mp);
			}else if (lose!=null) {
				this.remove(lose);
			}
			mp=new MyPanel("resume");
			this.remove(lp);
			getContentPane().add(mp);
			this.addKeyListener(mp);
			Thread t=new Thread(mp);
			t.start();
			this.setVisible(true);
		}
	}
}




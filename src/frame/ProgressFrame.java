/**
 * 
 */
package frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * @author wan
 *
 */
public class ProgressFrame extends JFrame{
	private JPanel progressPanel;
	private String display;
	private JProgressBar progressBar;
    public void setValue(int i){
        progressBar.setValue(i);
        progressBar.paintImmediately(progressBar.getX(), progressBar.getY(), progressBar.getWidth(), progressBar.getHeight());
    }
    public void setValue(int i,String str){
    	if(display==null||str==null){
    		setValue(i);
    		return;
    	}
    	display = str;
    	progressBar.setValue(i);
    	progressBar.paintImmediately(progressBar.getX(), progressBar.getY(), progressBar.getWidth(), progressBar.getHeight());
    }
    public ProgressFrame(){
        progressBar = new JProgressBar(0,100);
        init();
    }
    public ProgressFrame(int i){
        progressBar = new JProgressBar(0,i);
        init();
    }
    public ProgressFrame(String dis){
    	display = dis;
    	progressBar = new JProgressBar(0,100){
			@Override
			protected void paintComponent(Graphics g) {
				// TODO 自动生成的方法存根
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D)g;
				g2d.setColor(Color.black);
				g2d.drawString(display, 5, 15);
			}
    	};
    	init();
    }
    public ProgressFrame(int i,String dis){
    	display = dis;
    	progressBar = new JProgressBar(0,i){
			@Override
			protected void paintComponent(Graphics g) {
				// TODO 自动生成的方法存根
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D)g;
				g2d.setColor(Color.black);
				g2d.drawString(display, 5, 15);
			}
    	};
    	init();
    }
    private void init(){
        add(progressBar);
    	setTitle("progress");
    	setSize(400, 50);
    	setLocationRelativeTo(null);
    	setResizable(false);
    	setVisible(true);
    }
}
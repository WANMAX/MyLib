/**
 * 
 */
package panel;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * @author wan
 *
 */
public class ChessboardPanel extends JPanel{
	private int horizontal = 8;
	private int vertical = 8;
	private Color firstColor = Color.WHITE;
	private Color secondColor = Color.BLACK;
	public void setGrid(int horizontal,int vertical) {
		this.horizontal = horizontal;
		this.vertical = vertical;
	}
	public void setColor(Color firstColor,Color secondColor) {
		this.firstColor = firstColor;
		this.secondColor = secondColor;
	}
	public ChessboardPanel(){}
	public ChessboardPanel(Color firstColor,Color secondColor){
		this.firstColor = firstColor;
		this.secondColor = secondColor;
	}
	public ChessboardPanel(int horizontal,int vertical){
		this.horizontal = horizontal;
		this.vertical = vertical;
	}
	public ChessboardPanel(int horizontal,int vertical,Color firstColor,Color secondColor){
		this.horizontal = horizontal;
		this.vertical = vertical;
		this.firstColor = firstColor;
		this.secondColor = secondColor;
	}
	@Override
	protected void paintComponent(Graphics g) {
		// TODO 自动生成的方法存根
		super.paintComponent(g);
		int width = this.getWidth(),height = this.getHeight();
		g.setColor(firstColor);
		g.fillRect(0, 0, width, height);
		g.setColor(secondColor);
		for(int i = 0;i < horizontal;++i)for(int j = 0;j < vertical;++j){
			if((i+j)%2!=0)
				g.fillRect(i*width/horizontal, j*height/vertical, width/horizontal, height/vertical);
		}
	}
}

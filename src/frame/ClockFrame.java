/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package frame;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author wan
 */
public class ClockFrame extends JFrame{
    private final ClockPanel clock = new ClockPanel();
    private final JPanel secondPanel = new JPanel();
    private final MyButton startButton = new MyButton("start");
    private final MyButton continueButton = new MyButton("continue");
    private final MyButton stopButton = new MyButton("stop");
    public ClockFrame(){
        setLayout(new BorderLayout());
        add(clock,BorderLayout.CENTER);
        add(secondPanel,BorderLayout.SOUTH);
        secondPanel.add(startButton);
        secondPanel.add(continueButton);
        secondPanel.add(stopButton);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // TODO code application logic here
        JFrame frame = new ClockFrame();
        frame.setTitle("Clock");
        frame.setSize(500, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }
    private class ClockPanel extends JPanel{//一个时钟面板
        protected Time time = new Time();
        protected Timer timer = new Timer(1000,new ActionListener(){
            public void actionPerformed(ActionEvent e){
                clock.time.nextTime();
                clock.repaint();
            }
        });
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            int a = getWidth(),b = getHeight();
            g.setColor(Color.BLACK);
            g.fillOval(a/10,b/10,a*8/10,b*8/10);
            g.setColor(Color.WHITE);
            g.fillOval(a/10+a/90,b/10+b/90,a*8/10-a/45,b*8/10-b/45);
            g.setColor(Color.BLACK);
            for(int i=1;i<=60;i++){
                if(i%5==0){
                    g.drawLine((int)(a/2+(a*4/10-a/25)*Math.sin(i*Math.PI/30)), (int)(b/2-(b*4/10-b/25)*Math.cos(i*Math.PI/30)), (int)(a/2+a*4/10*Math.sin(i*Math.PI/30)), (int)(b/2-b*4/10*Math.cos(i*Math.PI/30)));
                }
                else{
                    g.drawLine((int)(a/2+(a*4/10-a/45)*Math.sin(i*Math.PI/30)), (int)(b/2-(b*4/10-b/45)*Math.cos(i*Math.PI/30)), (int)(a/2+a*4/10*Math.sin(i*Math.PI/30)), (int)(b/2-b*4/10*Math.cos(i*Math.PI/30)));
                }
            }
            g.setFont(new Font("Serif",Font.BOLD,(a<b)?a/10:b/10));
            FontMetrics fontMetrics = g.getFontMetrics();
            g.setColor(Color.DARK_GRAY);
            g.drawString("3",a/2+a*3/10-fontMetrics.stringWidth("3")/2,b/2+fontMetrics.getAscent()/2);
            g.drawString("6",a/2-fontMetrics.stringWidth("6")/2,b/2+b*3/10+fontMetrics.getAscent()/2);
            g.drawString("9",a/2-a*3/10-fontMetrics.stringWidth("9")/2,b/2+fontMetrics.getAscent()/2);
            g.drawString("12",a/2-fontMetrics.stringWidth("12")/2,b/2-b*3/10+fontMetrics.getAscent()/2);
            g.setColor(Color.BLACK);
            Graphics2D g2d=(Graphics2D)g;
            Stroke stroke=new BasicStroke(4.0f);
            g2d.setStroke(stroke);
            g.drawLine((int)(a/2-a*3/100*Math.sin(time.h*Math.PI/6)), (int)(b/2+b*3/100*Math.cos(time.h*Math.PI/6)), (int)(a/2+a*3/20*Math.sin(time.h*Math.PI/6)), (int)(b/2-b*3/20*Math.cos(time.h*Math.PI/6)));
            stroke = new BasicStroke(2.0f);
            g2d.setStroke(stroke);
            g.drawLine((int)(a/2-a*5/100*Math.sin(time.m*Math.PI/30)), (int)(b/2+b*5/100*Math.cos(time.m*Math.PI/30)), (int)(a/2+a*5/20*Math.sin(time.m*Math.PI/30)), (int)(b/2-b*5/20*Math.cos(time.m*Math.PI/30)));
            stroke = new BasicStroke(1.0f);
            g2d.setStroke(stroke);
            g.setColor(Color.RED);
            g.drawLine((int)(a/2-a*3/50*Math.sin(time.s*Math.PI/30)), (int)(b/2+b*3/50*Math.cos(time.s*Math.PI/30)), (int)(a/2+a*3/10*Math.sin(time.s*Math.PI/30)), (int)(b/2-b*3/10*Math.cos(time.s*Math.PI/30)));
            g.setColor(Color.DARK_GRAY);
            g.fillOval(a/2-a/90, b/2-b/90, a/45, b/45);
        }
        protected class Time{
            private double h,m,s;
            protected void setTime(long time){
                s = time/1000 % 60;
                m = time/1000 / 60 % 60+s/60;
                if((h=time/1000 / 60 / 60 %24 + m/60)<16){
                    h +=8;
                }
                else{
                    h -=16;
                }
            }
            protected void nextTime(){
                if(s<60){
                    s++;
                }
                else s-=59;
                if(m<60){
                    m+=(double)1/60;
                }
                else m-=59;
                if(h<24){
                    h+=(double)1/3600;
                }
                else h-=23;
            }
        }
    }
    private class MyButton extends JButton{
        public MyButton(String str){
            super(str);
            if(str.equals("start")){
                addActionListener(new StartAction());
            }
            else if(str.equals("continue")){
                addActionListener(new ContinueAction());
            }
            else{
                addActionListener(new StopAction());
            }
        }
    }
    private class StartAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            clock.setToolTipText("时钟初始化");
            clock.time.setTime(System.currentTimeMillis());
            clock.timer.start();
            clock.setToolTipText("时钟");
        }
    }
    private class ContinueAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            clock.timer.start();
            clock.setToolTipText("时钟");
        }
    }
    private class StopAction implements ActionListener{
        public void actionPerformed(ActionEvent e){
            clock.timer.stop();
            clock.setToolTipText("时钟停止");
        }
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hook.mousehook;

import com.sun.jna.examples.win32.W32API;
import hook.HOOKSTRUCT;
import java.awt.Point;

/**
 *
 * @author wan
 */
public class MouseHEvent{
    public static final int WM_MOUSEMOVE = 512;
    public static final int WM_LBUTTONDOWN = 513;
    public static final int WM_LBUTTONUP = 514;
    public static final int WM_RBUTTONDOWN = 516;
    public static final int WM_RBUTTONUP = 517;
    public static final int WM_MBUTTONDOWN = 519;
    public static final int WM_MBUTTONUP = 520;
    public static final int WM_MOUSEHWHEEL = 526;
    public static final int WM_MOUSEWHEEL = 522;
    
    public final int nCode;
    public final int keyValue;
    public final int x;
    public final int y;
    public MouseHEvent(int nCode,W32API.WPARAM wParam,HOOKSTRUCT info){
        this.nCode=nCode;
        this.keyValue=wParam.intValue();
        this.x=info.pt.x;
        this.y=info.pt.y;
    }
    public Point getPoint(){
        return new Point(x,y);
    }
}

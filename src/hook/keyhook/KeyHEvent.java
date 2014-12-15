/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hook.keyhook;

import com.sun.jna.examples.win32.User32;
import com.sun.jna.examples.win32.W32API;
import hook.HOOKSTRUCT;

/**
 *
 * @author wan
 */
public class KeyHEvent {
    public final static int DOWN = User32.WM_KEYDOWN;
    public final static int SYS_DOWN = User32.WM_SYSKEYDOWN;
    public final static int UP = User32.WM_KEYUP;
    public final static int SYS_UP = User32.WM_SYSKEYUP;
    
    public final int nCode;
    public final int keyType;
    public final int keyValue;
    public KeyHEvent(int nCode,W32API.WPARAM wParam,HOOKSTRUCT info){
        this.nCode=nCode;
        this.keyType=wParam.intValue();
        this.keyValue=info.pt.x;
    }
}

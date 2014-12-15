/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hook;

import com.sun.jna.Structure;
import com.sun.jna.examples.win32.User32;
import com.sun.jna.examples.win32.W32API;

/**
 *
 * @author wan
 */

public class HOOKSTRUCT extends Structure {
    public User32.POINT pt;
    public W32API.LPARAM hwnd;
    public int wHitTestCode;
    public User32.ULONG_PTR dwExtraInfo;
}

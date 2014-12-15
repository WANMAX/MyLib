/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hook.keyhook;

import hook.HOOKSTRUCT;

import com.sun.jna.examples.win32.Kernel32;
import com.sun.jna.examples.win32.User32;
import com.sun.jna.examples.win32.W32API;

/**
 *
 * @author wan
 */
public class KeyHook {
    private static KeyHook entity;
    private static User32.HHOOK hhk;
    private static LowLevelProc kbHook;
    private static Thread thread;
    private static KeyHListener listener;
    private static KeyHListener LSNBackup;
    private static boolean isHooked;
    private KeyHook(){};
    public static KeyHook initialise(){
        if(entity==null)entity = new KeyHook();
        return entity;
    }
    public static KeyHook initialise(KeyHListener listener){
        if(entity==null){
            entity = new KeyHook();
            LSNBackup = listener;
        }
        return entity;
    }
    public static void addKeyHListener(KeyHListener listener){
        LSNBackup = listener;
    }
    public static boolean isHook(){
        return isHooked;
    }
    
    public static void install(){
        thread = new Thread(){
            public void run() {
                W32API.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
                kbHook = new LowLevelProc() {
                    public W32API.LRESULT callback(int nCode, W32API.WPARAM wParam,HOOKSTRUCT info) {
                        if(listener!=null)
	                        listener.hookAction(new KeyHEvent(nCode, wParam, info));
                        return User32.INSTANCE.CallNextHookEx(hhk, nCode, wParam,info.hwnd );
                    }
                };
                hhk = User32.INSTANCE.SetWindowsHookEx(User32.WH_KEYBOARD_LL, kbHook, hMod, 0);
                User32.INSTANCE.GetMessage(new User32.MSG(), null, 0, 0);
            }
        };
        thread.start();
        isHooked = false;
    }
    public static void stop(){
        listener = null;
        isHooked = false;
    }
    public static void start(){
        if(listener==null&&LSNBackup!=null){
            listener = LSNBackup;
            isHooked = true;
        }
    }
    public static void unInstall(){
        User32.INSTANCE.UnhookWindowsHookEx(hhk);
    }
    private interface LowLevelProc extends User32.HOOKPROC {
        W32API.LRESULT callback(int nCode, W32API.WPARAM wParam, HOOKSTRUCT lParam);
    }
}
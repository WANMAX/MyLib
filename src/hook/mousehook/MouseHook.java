/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hook.mousehook;

import com.sun.jna.examples.win32.Kernel32;
import com.sun.jna.examples.win32.User32;
import com.sun.jna.examples.win32.W32API;
import hook.HOOKSTRUCT;

/**
 *
 * @author wan
 */
public class MouseHook {
    private static MouseHook entity;
    private static User32.HHOOK hhk;
    private static LowLevelProc msHook;
    private static MouseHListener listener;
    private static MouseHListener LSNBackup;
    private static Thread thread;
    private static boolean isHooked;
    private MouseHook(){}
    public static MouseHook initialise(){
        if(entity==null)entity = new MouseHook();
        return entity;
    }
    public static MouseHook initialise(MouseHListener listener){
        if(entity==null){
            entity = new MouseHook();
            LSNBackup = listener;
        }
        return entity;
    }
    public static void addMouseHListener(MouseHListener listener){
        LSNBackup = listener;
    }
    public static boolean isHook(){
        return isHooked;
    }
    public static void install(){
        thread=new Thread(){
            @Override
            public void run() {
                W32API.HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
                msHook = new LowLevelProc() {
                    public W32API.LRESULT callback(int nCode, W32API.WPARAM wParam,HOOKSTRUCT info) {
                        if(listener!=null)
	                        listener.hookAction(new MouseHEvent(nCode, wParam, info));
                        return User32.INSTANCE.CallNextHookEx(hhk, nCode, wParam,info.hwnd );
                    }
                };
                hhk = User32.INSTANCE.SetWindowsHookEx(User32.WH_MOUSE_LL, msHook, hMod, 0);
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
    public static void main(String[] args){
    	MouseHook mouseHook = MouseHook.initialise(new MouseHListener(){
			@Override
			public void hookAction(MouseHEvent e) {
				// TODO 自动生成的方法存根
				if(e.nCode>=0&&e.keyValue==MouseHEvent.WM_RBUTTONDOWN)
					System.out.println(e.x+" "+e.y);
			}
    	});
    	mouseHook.install();
    	mouseHook.start();
    }
}
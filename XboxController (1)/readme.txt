XboxController Library
Version 1.06, December 2, 2011
==================================================================

See the websites www.aplu.ch/xboxcontroller for most recent information.

History:
-------
V1.00 - Jan 2009: First public release
V1.01 - Feb 2009: Stop Java poll thread when calling release()
V1.02 - Jun 2009: Compile J2SE V5 instead of V6 for backward compatibility
                  Use JAW release with multi-threading bug fix  
                  Default queue period is 10 ms now
V1.03 - Jun 2009: Source distribution included
V1.04 - Jun 2009: Added getVersion(), 
                  Setting native controller poll period now works
V1.05 - Aug 2010: Distribution includes Win7 versions
V1.06 - Dec 2011: Added 64bit DLL xboxcontroller64.dll. XboxController class
                  now checks the OS version and loads the appropriate DLL 

Installation for Windows XP/Vista/Win7:
--------------------------------------

1. Download the latest version of the XboxController library.

2. Install the Xbox360 controller driver distributed with the hardware. 
   English version Xbox350_Eng.exe and German version Xbox360_Deu.exe 
   also contained in XboxController distribution (subdirectory 'driver').
   For Windows 7 you don't need to install the drivers manually, the are
   downloaded via Internet and installed as soon as you plug the Xbox 
   controller to the USB port.

3. (Only for Windows XP): Install the DirectX Runtime:
   Execute dxwebsetup.exe contained the the XboxController distribution 
   (subdirectory 'DirectX).

4  Install the VisualC Runtime:  Execute 
   vcredist_x86.exe (for 32-bit Windows) or vredist_x64 (for 64-bit Windows) 
   in the XboxController distribution  (subdirectory 'VisualC').

5. Copy both xboxcontroller.dll and xboxcontroller64.dll into a directory that 
   is part of the operating system's path (usually c:\windows, NEVER 
   c:\windows\system32, because it may be hidden to Java).
   Alternative: Copy the DLLs into the directory, where the Java 
   class or jar files of your application reside. 
   (The absolute path to the xboxcontroller.dll or xboxcontroller64.dll can 
   also be passed to the constructor of the XboxController class. But it
   is up to you to choose if you need the 32 or 64 bit version.) For 32-bit OS
   only xboxcontroller.dll is needed.

6. Add XboxController.jar and jaw.jar to the external libraries 
   of your Java project within your favorite Jave IDE.

7. Try to compile and understand the examples in the subdirectory 'examples' 
   of the XboxController distribution. 
   Consult the JavaDoc by opening index.html in the subdirectory 'doc'.
   (For some of these examples you need additionals libraries like aplu5.jar
    and NxtJLib.jar. You may download them from www.aplu.ch/aplu5 and
    www.aplu.ch/nxtjlib)


For any help or suggestions send an e-mail to support@aplu.ch or post an article
to the forum at www.aplu.ch/forum.

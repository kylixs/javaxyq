package com.jidesoft.utils;

import com.jidesoft.icons.JideIconsFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.PopupWindow;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.beans.Beans;
import java.io.PrintStream;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public final class Lm
  implements ProductNames
{
  public static final boolean DEBUG = false;
  public static final boolean DOC_DEBUG = false;
  public static final boolean CM_DEBUG = false;
  public static final boolean RA_DEBUG = false;
  public static final boolean ID_DEBUG = false;
  public static final boolean PG_DEBUG = false;
  public static final boolean CB_DEBUG = false;
  public static final boolean HG_DEBUG = false;
  public static final boolean AF_DEBUG = false;
  public static final boolean BP_DEBUG = false;
  public static final boolean DOCK_DEBUG = false;
  public static final boolean DOCK_ID_DEBUG = false;
  public static final boolean ACTION_DEBUG = false;
  public static final boolean COMPONENT_DEBUG = false;
  public static final boolean PROPERTY_TABLE_DEBUG = false;
  public static final boolean COMBOBOX_DEBUG = false;
  public static final boolean BEAN_INTROSPECTOR_DEBUG = false;
  public static final boolean DEMO = false;
  private static boolean a = false;
  private static int b = 0;
  private static JFrame c;
  private static final Calendar d = Calendar.getInstance();
  private static int e;
  private static boolean f;
  private static int g;
  private static final String[] h;
  private static final BigInteger i;
  private static final BigInteger j;
  public static boolean k;

  public static String getProductVersion()
  {
    return "2.5.0";
  }

  public static boolean showDemoMessageBoxDocking()
  {
    boolean bool = k;
    b += 1;
    if (!(bool))
      if (b != 0)
      {
        if (bool)
        	return false;
        if (b % 20 == 0)
        {
          a = false;
          a();
        }
      }
    label43: return false;
  }

  public static void showAboutMessageBox()
  {
    a = false;
    a();
  }

  private static String a(int paramInt)
  {
    boolean bool = k;
    StringBuffer localStringBuffer = new StringBuffer();
    if (!(bool))
      if (paramInt == 0)
        return "";
    if (!(bool))
      if ((paramInt & 0x1) != 0)
        localStringBuffer.append("JIDE Docking Framework, ");
    if (!(bool))
      if ((paramInt & 0x2) != 0)
        localStringBuffer.append("JIDE Components, ");
    if (!(bool))
      if ((paramInt & 0x4) != 0)
        localStringBuffer.append("JIDE Grids, ");
    if (!(bool))
      if ((paramInt & 0x8) != 0)
        localStringBuffer.append("JIDE Dialogs, ");
    if (!(bool))
      if ((paramInt & 0x10) != 0)
        localStringBuffer.append("JIDE Action Framework, ");
    if (!(bool))
      if ((paramInt & 0x40) != 0)
        localStringBuffer.append("JIDE Shortcut Editor, ");
    if (!(bool))
      if ((paramInt & 0x20) != 0)
        localStringBuffer.append("JIDE Pivot Grid, ");
    if (!(bool))
      if ((paramInt & 0x80) != 0)
        localStringBuffer.append("JIDE Code Editor, ");
    if (!(bool))
      if ((paramInt & 0x100) != 0)
        localStringBuffer.append("JIDE Feed Reader, ");
    if (!(bool))
      if ((paramInt & 0x400) != 0)
        localStringBuffer.append("JIDE Dashboard, ");
    if (!(bool))
      if ((paramInt & 0x800) != 0)
        localStringBuffer.append("JIDE Data Grids, ");
    if ((paramInt & 0x200) != 0)
      localStringBuffer.append("JIDE Desktop Application Framework, ");
    String str = new String(localStringBuffer);
    return str.substring(0, str.length() - 2);
  }

  protected static String a(int[] paramArrayOfInt)
  {
    boolean bool = k;
    StringBuffer localStringBuffer = new StringBuffer();
    int l = 0;
    do
    {
      if (l >= paramArrayOfInt.length)
        break;
      int i1 = paramArrayOfInt[l];
      if (bool)
        continue;
      if (i1 == 1)
        localStringBuffer.append(h[l]);
      ++l;
    }
    while (!(bool));
    return localStringBuffer.toString();
  }

  public static void showInvalidProductMessage(String paramString, int paramInt)
  {
//    boolean bool = k;
//    if (!(bool))
//      if (g == 0)
//      {
//        if ((!(bool)) && (f))
//          return;
//        if (!(bool))
//          if ("true".equals(SecurityUtils.getProperty("jide.verifyLicense", "false")))
//          {
//            System.err.println("Verifying " + paramString);
//            Thread.dumpStack();
//          }
//        if ((bool) || ((!(Beans.isDesignTime())) && (Beans.isGuiAvailable())))
//        {
//          a("<html><font size=3><b>Unauthorized usage of JIDE products</b></font><br><br><hr size='1'><br>You get this message box is because you didn't input a correct license key.<br>If you see this message box in one of our demo examples, just ignore it.<br>If you wish to use JIDE products in your application, please contact sales@jidesoft.com.</html>", "JIDE Software, Inc.", -1, "Click to Continue");
//          if (!(bool))
//        	  f = true;
//        }
//        System.out.println("Unauthorized usage of JIDE products\nYou get this message is because you didn't input a correct license key.\nIf you see this message box in one of our demo examples, just ignore it.\nIf you wish to use JIDE products in your application, please contact sales@jidesoft.com.");
//      }
//    if ((!(bool)) || ((!(bool)) && ((e & paramInt) != 0)))
//      return;
//    if (!(bool))
//      if ("true".equals(SecurityUtils.getProperty("jide.verifyLicense", "false")))
//      {
//        System.err.println("Verifying " + paramString + " for " + paramInt);
//        Thread.dumpStack();
//      }
//    String str1 = a(paramInt);
//    String str2 = a(g);
//    if ((bool) || ((!(Beans.isDesignTime())) && (Beans.isGuiAvailable())))
//    {
//      a("<html><font size=3><b>Unauthorized usage of JIDE products</b></font><br><br><hr size='1'><br>You get this message box is because the license key(s) are for " + str2 + " only.<br>" + "The class " + paramString + " you tried to use is part of " + str1 + ".<br>If you wish to use " + str1 + " in your application, please contact sales@jidesoft.com.</html>", "JIDE Software, Inc.", -1, "Click to Continue");
//      if (!(bool))
//    	  e |= paramInt;
//    }
//    System.out.println("Unauthorized usage of JIDE products\nYou get this message is because the license key(s) are for " + str2 + " only.\n" + "The class " + paramString + " you tried to use is part of " + str1 + ".\nIf you wish to use " + str1 + " in your application, please contact sales@jidesoft.com.");
  }

  private static void b()
  {
//    Calendar localCalendar = Calendar.getInstance();
//    String str = "JIDE Docking Framework, JIDE Action Framework, JIDE Components, JIDE Grids, JIDE Dialogs, JIDE Pivot Grid, JIDE Shortcut Editor, JIDE Code Editor, JIDE Feed Reader, JIDE Dashboard, and JIDE Data Grids";
//    int l = str.indexOf(",", str.length() / 2);
//    str = str.substring(0, l + 1) + "<BR>" + str.substring(l + 1);
//    if (localCalendar.after(d))
//    {
//      a("<html><font size=3><b>Demo for " + str + "</b></font><br><br>" + "Copyright ? 2002-2008 JIDE Software, Inc, all rights reserved.<br><br><hr size='1'><br>" + "<font color='red'><b>This demo version has expired on " + DateFormat.getDateInstance(1).format(d.getTime()) + ".</b></font>" + "<br>If you want to continue evaluating, please download it again from http://www.jidesoft.com." + "<br><br><b>Please Note: This release package is for evaluation purpose only. Distribution of it is strictly prohibited.<br> " + "If you wish to use any JIDE products in your application, please contact sales@jidesoft.com.</html>", "JIDE Software, Inc.", -1, "Click to Exit");
//      System.exit(0);
//    }
//    else
//    {
//      if (a)
//        return;
//      if ((!(Beans.isDesignTime())) && (Beans.isGuiAvailable()))
//        a("<html><font size=3><b>Demo for " + str + "</b></font><br><br>" + "Copyright ? 2002-2008 JIDE Software, Inc, all rights reserved.<br><br><hr size='1'><br>" + "This is a demo version of JIDE products. It will expire on " + DateFormat.getDateInstance(1).format(d.getTime()) + ".<br><br><b>Please Note: This release package is for evaluation purpose only. Distribution of it is strictly prohibited.<br> " + "If you wish to use any JIDE products in your application, please contact sales@jidesoft.com.</html>", "JIDE Software, Inc.", -1, "Continue to Evaluate");
//      else
//        System.out.println("Demo for JIDE Docking Framework, JIDE Action Framework, JIDE Components, JIDE Grids, JIDE Dialogs, JIDE Pivot Grid, JIDE Shortcut Editor, JIDE Code Editor, JIDE Feed Reader, JIDE Dashboard, and JIDE Data Grids\n\nCopyright ? 2002-2008 JIDE Software, Inc, all rights reserved.\n\n---------\nThis is a demo version of JIDE products. It will expire on " + DateFormat.getDateInstance(1).format(d.getTime()) + ".\n\nPlease Note: This release package is for evaluation purpose only. Distribution of it is strictly prohibited.\n " + "If you wish to use any JIDE products in your application, please contact sales@jidesoft.com.");
//      a = true;
//    }
  }

  public static void a()
  {
    b();
  }

  private static String a(String paramString1, String paramString2, int paramInt1, int paramInt2)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(paramString1);
    localStringBuffer.append(":");
    localStringBuffer.append(paramString2);
    localStringBuffer.append(":");
    localStringBuffer.append(paramInt1);
    if (!(k))
    {
      if (paramInt2 == 0)
          return new String(localStringBuffer);
      localStringBuffer.append(":");
    }
    localStringBuffer.append(paramInt2);
    label71: return new String(localStringBuffer);
  }

  private static String a(String paramString1, String paramString2, String paramString3)
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append(paramString1);
    localStringBuffer.append(":");
    localStringBuffer.append(paramString2);
    localStringBuffer.append(":");
    localStringBuffer.append(paramString3);
    return new String(localStringBuffer);
  }

  private static int a(String paramString)
  {
    boolean bool = k;
    int l = 0;
    if (!(bool))
      if (paramString.indexOf("Docking") != -1)
        l |= 1;
    if (!(bool))
      if (paramString.indexOf("Component") != -1)
        l |= 2;
    if (!(bool))
      if (paramString.indexOf("Grid") != -1)
        l |= 4;
    if (!(bool))
      if (paramString.indexOf("Dialog") != -1)
        l |= 8;
    if (!(bool))
      if (paramString.indexOf("Action") != -1)
        l |= 16;
    if (!(bool))
      if (paramString.indexOf("Shortcut") != -1)
        l |= 64;
    if (!(bool))
      if (paramString.indexOf("Pivot") != -1)
        l |= 32;
    if (!(bool))
      if (paramString.indexOf("CodeEditor") != -1)
        l |= 128;
    if (!(bool))
      if (paramString.indexOf("FeedReader") != -1)
        l |= 256;
    if (!(bool))
      if (paramString.indexOf("Dashboard") != -1)
        l |= 1024;
    if (!(bool))
    {
      if (paramString.indexOf("Data") != -1)
        l |= 2048;
      if (bool)
          return l;
    }
    if (paramString.indexOf("JDAF") != -1)
      l |= 512;
    label237: return l;
  }

  static boolean b(int paramInt)
  {
    boolean bool = k;
    if (!(bool))
      if (g == 0)
        c();
    if (!(bool));
    return ((g & paramInt) != 0);
  }

  public static void verifyLicense(String paramString1, String paramString2, String paramString3)
  {
    b(paramString1, paramString2, paramString3);
  }

  private static void c()
  {
    boolean bool = k;
    ResourceBundle localResourceBundle = null;
    try
    {
      localResourceBundle = ResourceBundle.getBundle("com.jidesoft.utils.utils");
    }
    catch (Exception localException1)
    {
    }
    if (!(bool))
      if (localResourceBundle == null)
        try
        {
          ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
          if (localClassLoader != null)
            localResourceBundle = ResourceBundle.getBundle("com.jidesoft.utils.utils", Locale.getDefault(), localClassLoader);
        }
        catch (Exception localException2)
        {
        }
    try
    {
      String str1 = ((bool) || (localResourceBundle != null)) ? localResourceBundle.getString("verifyLicense.companyName") : SecurityUtils.getProperty("verifyLicense.companyName", null);
      if (((!(bool)) && (str1 == null)) || (str1.trim().length() == 0))
        return;
      String str2 = ((bool) || (localResourceBundle != null)) ? localResourceBundle.getString("verifyLicense.projectName") : SecurityUtils.getProperty("verifyLicense.projectName", null);
      if (((!(bool)) && (str2 == null)) || (str2.trim().length() == 0))
        return;
      String str3 = ((bool) || (localResourceBundle != null)) ? localResourceBundle.getString("verifyLicense.licenseKey") : SecurityUtils.getProperty("verifyLicense.licenseKey", null);
      if ((bool) || (str3 != null))
      {
        if (bool)
            b(str1, str2, str3);
        if (str3.trim().length() != 0)
            b(str1, str2, str3);
      }
      return;
    }
    catch (Exception localException3)
    {
    }
  }

  private static void b(String paramString1, String paramString2, String paramString3)
  {
    boolean bool = k;
    int[] arrayOfInt = new int[h.length];
    Arrays.fill(arrayOfInt, 0);
    BigInteger localBigInteger1 = b(paramString3);
    BigInteger localBigInteger2 = localBigInteger1.modPow(i, j);
    arrayOfInt[0] = 1;
    int i1;
    do
    {
      if (arrayOfInt[0] < 0)
        break;
      arrayOfInt[1] = 1;
      do
      {
        if (arrayOfInt[1] < 0)
          break;
        if (bool)
          break;
        arrayOfInt[2] = 1;
        do
        {
          if (arrayOfInt[2] < 0)
            break;
          if (bool)
            break;
          arrayOfInt[3] = 1;
          do
          {
            if (arrayOfInt[3] < 0)
              break;
            if (bool)
              break;
            arrayOfInt[4] = 1;
            do
            {
              if (arrayOfInt[4] < 0)
                break;
              if (bool)
                break;
              arrayOfInt[5] = 1;
              do
              {
                if (arrayOfInt[5] < 0)
                  break;
                if (bool)
                  break;
                arrayOfInt[6] = 1;
                do
                {
                  if (arrayOfInt[6] < 0)
                    break;
                  if (bool)
                    break;
                  arrayOfInt[7] = 1;
                  do
                  {
                    if (arrayOfInt[7] < 0)
                      break;
                    if (bool)
                      break;
                    arrayOfInt[8] = 1;
                    do
                    {
                      if (arrayOfInt[8] < 0)
                        break;
                      if (bool)
                        break;
                      arrayOfInt[9] = 1;
                      do
                      {
                        if (arrayOfInt[9] < 0)
                          break;
                        if (bool)
                          break;
                        arrayOfInt[10] = 1;
                        do
                        {
                          if (arrayOfInt[10] < 0)
                            break;
                          if (bool)
                            break;
                          arrayOfInt[11] = 1;
                          do
                          {
                            if (arrayOfInt[11] < 0)
                              break;
                            String str = a(arrayOfInt);
                            i1 = d(a(paramString1, paramString2, str));
                            if (bool)
                              continue;
                            if (!(bool));
                            if (localBigInteger2.equals(new BigInteger("" + i1)))
                            {
                              g |= a(str);
                              return;
                            }
                            arrayOfInt[11] -= 1;
                          }
                          while (!(bool));
                          arrayOfInt[10] -= 1;
                        }
                        while (!(bool));
                        arrayOfInt[9] -= 1;
                      }
                      while (!(bool));
                      arrayOfInt[8] -= 1;
                    }
                    while (!(bool));
                    arrayOfInt[7] -= 1;
                  }
                  while (!(bool));
                  arrayOfInt[6] -= 1;
                }
                while (!(bool));
                arrayOfInt[5] -= 1;
              }
              while (!(bool));
              arrayOfInt[4] -= 1;
            }
            while (!(bool));
            arrayOfInt[3] -= 1;
          }
          while (!(bool));
          arrayOfInt[2] -= 1;
        }
        while (!(bool));
        arrayOfInt[1] -= 1;
      }
      while (!(bool));
      arrayOfInt[0] -= 1;
    }
    while (!(bool));
    int l = 15;
    do
    {
      if (l < 0)
        return;
      i1 = 7;
      do
      {
        if (i1 < 0)
          break;
        int i2 = c(a(paramString1, paramString2, l, i1 * 16));
        if (bool)
          continue;
        if (!(bool));
        if (localBigInteger2.equals(new BigInteger("" + i2)))
        {
          g |= l + i1 * 16;
          return;
        }
        --i1;
      }
      while (!(bool));
      --l;
    }
    while (!(bool));
  }

  public static void clearLicense()
  {
    g = 0;
  }

  private static BigInteger b(String paramString)
  {
    boolean bool = k;
    BigInteger localBigInteger = BigInteger.ZERO;
    return localBigInteger;
  }

  private static int c(String paramString)
  {
    boolean bool = k;
    int l = 1976;
    int i1 = 0;
    do
    {
      if (i1 >= paramString.length())
        break;
      l += paramString.charAt(i1);
      if (bool)
        break;
      l = (l += 666) ^ 0x7895;
      ++i1;
    }
    while (!(bool));
    return l;
  }

  private static int d(String paramString)
  {
    boolean bool = k;
    int l = 1979;
    int i1 = 0;
    do
    {
      if (i1 >= paramString.length())
        break;
      l += paramString.charAt(i1);
      l += 88888;
      if (bool)
        break;
      l ^= 3232379;
      ++i1;
    }
    while (!(bool));
    return l;
  }

  public static void setParent(JFrame paramJFrame)
  {
    c = paramJFrame;
  }

  private static void a(Object paramObject, String paramString, int paramInt)
  {
    a(paramObject, paramString, paramInt, null);
  }

  private static void a(Object paramObject, String paramString1, int paramInt, String paramString2)
  {
    Object localObject;
    if (c == null)
    {
      localObject = new JFrame("JIDE Software, Inc.");
      ((JFrame)localObject).setIconImage(JideIconsFactory.getImageIcon("jide/jide32.png").getImage());
      ((JFrame)localObject).setLocation(0, 2147483647);
      ((JFrame)localObject).pack();
      ((JFrame)localObject).setVisible(true);
      PortingUtils.notifyUser();
      ((JFrame)localObject).toFront();
      b_ localb_ = new b_((Frame)localObject, paramString1, paramObject.toString(), paramInt, paramString2);
      localb_.display(false);
      ((JFrame)localObject).setVisible(false);
      ((JFrame)localObject).dispose();
    }
    else
    {
      localObject = new b_(c, paramString1, paramObject.toString(), paramInt, paramString2);
      ((b_)localObject).display(false);
    }
  }

  public static void showPopupMessageBox(String paramString)
  {
    PopupWindow localPopupWindow = new PopupWindow(c);
    JLabel localJLabel = new JLabel();
    localJLabel.setBackground(UIDefaultsLookup.getColor("ContentContainer.background"));
    localJLabel.setForeground(Color.black);
    localJLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), BorderFactory.createEmptyBorder(3, 3, 3, 3)));
    localJLabel.setText(paramString);
    localPopupWindow.add(localJLabel);
    localPopupWindow.show(c, 100, 200);
  }

  private static void d()
  {
    StringBuffer localStringBuffer = new StringBuffer();
    localStringBuffer.append("This is a demo version for evaluation purpose only.<BR><BR>");
    localStringBuffer.append("JIDE Version: ").append("2.5.0");
    localStringBuffer.append("<BR>");
    localStringBuffer.append("JDK Version: ").append(SystemInfo.getJavaVersion());
    localStringBuffer.append("<BR>");
    localStringBuffer.append("JDK Vendor: ").append(SystemInfo.getJavaVendor());
    localStringBuffer.append("<BR>");
    localStringBuffer.append("Java Class Version: ").append(SystemInfo.getJavaClassVerion());
    localStringBuffer.append("<BR>");
    localStringBuffer.append("Platform: ").append(SystemInfo.getOS());
    localStringBuffer.append("<BR>");
    localStringBuffer.append("Platform Version: ").append(SystemInfo.getOSVersion());
    localStringBuffer.append("<BR>");
    localStringBuffer.append("Platform Archtechture: ").append(SystemInfo.getOSArchitecture());
    localStringBuffer.append("<BR>");
    String str = localStringBuffer.toString();
    str = str.replaceAll("<BR>", "\n");
    System.out.println(str);
    a("<HTML>" + localStringBuffer + "</HTML>", "JIDE Products - System Information", -1);
  }

  public static void main(String[] paramArrayOfString)
  {
    d();
  }

  public static void getUIError(String paramString)
  {
    a("<HTML><B>Missing value in LookAndFeel's UIDefaults</B>. <BR><BR><HR><BR>Component \"<B>" + paramString + "</B>\" requires its own ComponentUI and additional values in LookAndFeel's UIDefaults.<BR>Please make sure you call LookAndFeelFactory.installJideExtension() whenever you switch L&F." + "<BR>For more details, please refer to Developer Guides or visit JIDE Developer's Forum (http://www.jidesoft.com/forum).\n", "LookAndFeel Error", -1, "  Exit  ");
    System.exit(-1);
  }

  static
  {
    d.set(2009, 2, 8, 0, 0, 0);
    e = 0;
    f = false;
    g = 0;
    h = new String[] { "Docking", "Component", "Grid", "Dialog", "Action", "Shortcut", "Pivot", "CodeEditor", "FeedReader", "Dashboard", "Data", "JDAF" };
    i = new BigInteger("19", 10);
    j = new BigInteger("305508269643653255827856366547026610628423058227532461973", 10);
  }

  private static class a_ extends JDialog
  {
    private boolean a;
    private b_ b;

    public a_()
      throws HeadlessException
    {
      this(null, "", true);
    }

    public a_(Frame paramFrame)
      throws HeadlessException
    {
      this(paramFrame, "", true);
    }

    public a_(Frame paramFrame, String paramString)
      throws HeadlessException
    {
      this(paramFrame, paramString, true);
    }

    public a_(Frame paramFrame, boolean paramBoolean)
      throws HeadlessException
    {
      this(paramFrame, "", paramBoolean);
    }

    public a_(Frame paramFrame, String paramString, boolean paramBoolean)
    {
      super(paramFrame, paramString, paramBoolean);
      this.a = false;
      this.b = new b_(this);
      getRootPane().registerKeyboardAction(this.b, KeyStroke.getKeyStroke(27, 0), 1);
    }

    public final boolean getResult()
    {
      return this.a;
    }

    public final void setResult(boolean paramBoolean)
    {
      this.a = paramBoolean;
    }

    public void display(boolean paramBoolean)
    {
      a(getRootPane());
      pack();
      setResizable(paramBoolean);
      JideSwingUtilities.globalCenterWindow(this);
      setVisible(true);
    }

    private void a(Component paramComponent)
    {
      if (paramComponent instanceof JButton)
        paramComponent.addFocusListener(new FocusAdapter()
        {
          public void focusGained(FocusEvent paramFocusEvent)
          {
            Lm.a_.this.getRootPane().setDefaultButton((JButton)paramFocusEvent.getSource());
          }
        });
      Component[] arrayOfComponent = null;
      if (paramComponent instanceof JMenu)
        arrayOfComponent = ((JMenu)paramComponent).getMenuComponents();
      else if (paramComponent instanceof Container)
        arrayOfComponent = ((Container)paramComponent).getComponents();
      if (arrayOfComponent == null)
        return;
      for (int i = 0; i < arrayOfComponent.length; ++i)
        a(arrayOfComponent[i]);
    }

    public void display()
    {
      display(false);
    }

    public AbstractAction getDefaultCancelAction()
    {
      return this.b;
    }

    private class b_ extends AbstractAction
    {
      private JDialog a;

      protected b_(JDialog paramJDialog)
      {
        super("Close");
        this.a = paramJDialog;
      }

      public void actionPerformed(ActionEvent paramActionEvent)
      {
//        Lm.a_.access$002(Lm.a_.this, false);
        this.a.setVisible(false);
      }
    }
  }

  private static class b_ extends Lm.a_
  {
    private String c;
    private int d;
    private String e;

    public b_(Frame paramFrame, String paramString1, String paramString2)
      throws HeadlessException
    {
      this(paramFrame, paramString1, paramString2, -1, null);
    }

    public b_(Frame paramFrame, String paramString1, String paramString2, int paramInt)
      throws HeadlessException
    {
      this(paramFrame, paramString1, paramString2, paramInt, null);
    }

    public b_(Frame paramFrame, String paramString1, String paramString2, int paramInt, String paramString3)
      throws HeadlessException
    {
      super(paramFrame, paramString1, true);
      this.c = paramString2;
      this.d = paramInt;
      this.e = paramString3;
      a();
    }

    private void a()
    {
      JPanel localJPanel1 = new JPanel(new BorderLayout(6, 6));
      JButton localJButton = new JButton(getDefaultCancelAction());
      getRootPane().setDefaultButton(localJButton);
      if (this.e != null)
        localJButton.setText(this.e);
      localJPanel1.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
      JLabel localJLabel1 = new JLabel(this.c);
      JPanel localJPanel2 = new JPanel(new BorderLayout(12, 12));
      JLabel localJLabel2;
      switch (this.d)
      {
      case 2:
        localJLabel2 = new JLabel(UIDefaultsLookup.getIcon("OptionPane.warningIcon"));
        break;
      case 0:
        localJLabel2 = new JLabel(UIDefaultsLookup.getIcon("OptionPane.errorIcon"));
        break;
      case 1:
        localJLabel2 = new JLabel(UIDefaultsLookup.getIcon("OptionPane.informationIcon"));
        break;
      case 3:
        localJLabel2 = new JLabel(UIDefaultsLookup.getIcon("OptionPane.questionIcon"));
        break;
      default:
        localJLabel2 = new JLabel(JideIconsFactory.getImageIcon("jide/jide_logo.png"));
      }
      localJPanel2.add(JideSwingUtilities.createTopPanel(localJLabel2), "Before");
      localJPanel2.add(localJLabel1, "Center");
      localJPanel1.add(localJPanel2, "Center");
      localJPanel1.add(JideSwingUtilities.createCenterPanel(localJButton), "Last");
      getContentPane().add(localJPanel1);
    }
  }
}


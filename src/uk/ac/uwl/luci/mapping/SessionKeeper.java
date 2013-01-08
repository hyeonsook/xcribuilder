

package uk.ac.uwl.luci.mapping;

import java.util.*;
import java.util.logging.*;
import uk.ac.uwl.luci.hb.CourseController;

// Referenced classes of package uk.ac.uwl.luci.mapping:
//            Hb2Xcri

public class SessionKeeper extends TimerTask
{
    private static Timer timer = null;
    private static Calendar calendar = Calendar.getInstance();
    private static Date firstDate = null;
    private static int period = -1;
    
    private static Logger logger;
    private CourseController controller = null;

    public SessionKeeper(CourseController xcriController){
        controller = xcriController;
    }
    
    public void start()
    {
        timer = new Timer();
        Logger.getLogger(SessionKeeper.class.getName()).log(Level.INFO, "Starting sessionKeeper.");
        //runs at 8:05am on inputed date 
        firstDate = calendar.getTime();
        
        //weekly run (ms): 1000(sec) * 60 (min) * 60 (hr) * 24 (day) 
        //period = 1000 * 60 * 60 * 24;
        period = 1000 * 60 * 60 * 6;
        timer.scheduleAtFixedRate(this, firstDate, period);
        
        Logger.getLogger(SessionKeeper.class.getName()).log(Level.INFO, "SessionKeeper has been started.");
    }


    public static void stop()
    {
        timer.cancel();
        timer.purge();
       
        Logger.getLogger(XCRIGenerator.class.getName()).log(Level.INFO, "SessionKeeper has been stopped.");
    }

    public void run()
    {
        try {
            controller.openSession();
            controller.getDummyCourses();
            controller.closeSession();
            Logger.getLogger(SessionKeeper.class.getName()).log(Level.INFO, "Session Keeper run.");
            
        } catch (Exception ex) {
            Logger.getLogger(XCRIGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}

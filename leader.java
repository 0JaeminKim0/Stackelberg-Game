import comp34120.ex2.PlayerImpl;
import comp34120.ex2.PlayerType;
import comp34120.ex2.Record;
import java.lang.Math;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.io.*;

// our leader class
final class leader extends PlayerImpl {

    // y = a + bx; ReactionFunction(a,b); instance.GetReactionFunction(U_L);
    class ReactionFunction {
        double a;
        double b;
        public ReactionFunction(double a, double b) {
            this.a = a;
            this.b = b;
        }
        //Constructing Reaction function and return the result
        public double GetReactionFunction(double U_L) { return this.a + this.b * U_L; }
        public double getA() { return this.a; }
        public double getB() { return this.b; }
    }
    
    // Reaction Function instance & Record
    private ReactionFunction rf = null;
    private Record data[];
    private int window_size = 100;

    private leader() throws RemoteException, NotBoundException {
        super(PlayerType.LEADER, "Leader");
    }

    @Override
    public void goodbye() throws RemoteException {
        ExitTask.exit(500);
    }
    private static class ExitTask extends TimerTask {
        static void exit(final long p_delay) {
            (new Timer()).schedule(new ExitTask(), p_delay);
        }
        @Override
        public void run() {
            System.exit(0);
        }
    }

/////////////////////////////////////////////////////////////////////////////////

    // SUBMIT LEADER VALUE
    @Override
    public void proceedNewDay(int p_date) throws RemoteException {
        double leader_ul;
        estimatedReactionFunction(p_date);
        
        // Work out global maximum
        leader_ul = (3 * (rf.b - rf.a) - 30) / ((6*rf.b) - 20);
        // Publish result leader price
        m_platformStub.publishPrice(m_type, (float)leader_ul);
        // Insert new response of follower
        data[p_date] = m_platformStub.query(m_type, p_date);

        //m_platformStub.publishPrice(m_type, genPrice(1.8f, 0.05f));
    }

    // p_steps = no of future days
    @Override
    public void startSimulation(int p_steps) throws RemoteException
    {
        int no_history = 100;
        data = new Record[no_history+p_steps+1];
        for (int i=1;i<=no_history;i++) data[i] = m_platformStub.query(m_type, i);
        
		//super.startSimulation(p_steps);
		//TO DO: delete the line above and put your own initialization code here
	}

    //estimating the reaction Function
    private void estimatedReactionFunction(int p_date) {
        double sum_L = 0;
        double sum_F = 0;
        double sum_L2 = 0;
        double sum_LF = 0;
        
        //end day
        int e_day = p_date - 1;


        //System.out.println("esti_p_date = " + p_date);
        
        //From start date to end of date
        //To estimate least ordinary function, get a and b
        for (int i = e_day - window_size + 1 ; i <= e_day; i++) {
            Record record = data[i];
            sum_L  += record.m_leaderPrice;
            sum_F  += record.m_followerPrice;
            sum_L2 += Math.pow(record.m_leaderPrice, 2);
            sum_LF += record.m_leaderPrice * record.m_followerPrice;
        }

        double est_a = ((sum_L2 * sum_F) - (sum_L * sum_LF)) / ((window_size * sum_L2) - Math.pow(sum_L, 2));
        double est_b = ((window_size * sum_LF) - (sum_L * sum_F)) / ((window_size * sum_L2) - Math.pow(sum_L, 2));

        this.rf = new ReactionFunction(est_a, est_b);
    }

    private double demandModel(float U_L, float U_F) {
        return 2 - U_L + 0.3 * U_F;
    }
    private double revenue(float U_L, float U_F) {
        return (U_L - 1) * demandModel(U_L, U_F);
    }

    public static void main(final String[] p_args) throws RemoteException, NotBoundException {
        new leader();  
    }


}
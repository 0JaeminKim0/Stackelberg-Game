//import src.*;
//import comp34120.ex2.*;
import comp34120.ex2.PlayerImpl;
import comp34120.ex2.PlayerType;
import comp34120.ex2.Record;

import java.lang.Math;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.*;
import java.io.*;



final class leader extends PlayerImpl {
    class ReactionFunction {
        double a;
        double b;
        // when a and b has specific value
        //linear function form
        public ReactionFunction(double a, double b) {
            this.a = a;
            this.b = b;
        }
        //Constructing Reaction function and return the result
        public double GetReactionFunction(double U_L) {
            return this.a + this.b * U_L;
        }

        public double getA() {
            return this.a;
        }

        public double getB() {
            return this.b;
        }
    }
    //make class for reaction function design
    private ReactionFunction rf = null;
    //get value of history data
    private Record data[];

    private leader() throws RemoteException, NotBoundException {
        super(PlayerType.LEADER, "Leader");
    }

    @Override
    public void proceedNewDay(int p_date) throws RemoteException {
        //estimatedReactionFunction(p_date);
        //this.data[p_date] = m_platformStub.query(this.m_type, p_date);
        
        m_platformStub.publishPrice(m_type, 1.8f);
        //m_platformStub.publishPrice(m_type, genPrice(1.8f, 0.05f));
    }


    /*
    Record(final int p_date,
            final float p_leaderPrice,
            final float p_followerPrice,
            final float p_cost)
*/

    @Override
    public void startSimulation(int p_steps) throws RemoteException
    {
        int no_history = 100;
        data = new Record[no_history+p_steps];
        
        for (int i=1;i<=no_history;i++){
            data[i] = m_platformStub.query(m_type, i);
            System.out.println(data[i].m_date + "-" + data[i].m_leaderPrice);
        }

		super.startSimulation(p_steps);
		//TO DO: delete the line above and put your own initialization code here
	}

    //////////////////////////////////////////////////////////////////
    @Override
    public void goodbye() throws RemoteException {
        ExitTask.exit(500);
    }

    private static class ExitTask
    extends TimerTask {
        static void exit(final long p_delay) {
            (new Timer()).schedule(new ExitTask(), p_delay);
        }

        @Override
        public void run() {
            System.exit(0);
        }
    }
    //////////////////////////////////////////////////////////////////

    //estimating the reaction Function
    private void estimatedReactionFunction(int latestDate) {
        double sum_L = 0;
        double sum_F = 0;
        double sum_L2 = 0;
        double sum_LF = 0;
        //simulate day
        int s_day;
        //last day
        int e_day = latestDate - 1;

        //From start date to end of date
        //To estimate least ordinary function, get a and b
        for (s_day = e_day; s_day < e_day; ++s_day) {
            Record record = this.data[s_day];
            sum_L = record.m_leaderPrice;
            sum_F = record.m_followerPrice;
            sum_L2 = Math.pow(record.m_leaderPrice, 2);
            sum_LF = record.m_leaderPrice * record.m_followerPrice;
        }

        double est_a = ((sum_L2 * sum_F) - (sum_L * sum_LF)) / ((e_day * sum_L2) - Math.pow(sum_L, 2));
        double est_b = ((e_day * sum_LF) - (sum_L * sum_F)) / ((e_day * sum_L2) - Math.pow(sum_L, 2));

        this.rf = new ReactionFunction(est_a, est_b);

    }

    // Making a revenue

    private double demandModel(float U_L, float U_F) {
        return 2 - U_L + 0.3 * U_F;
    }

    private double revenue(float U_L, float U_F) {
        return (U_L - 1) * demandModel(U_L, U_F);
    }

    //
    public static void main(final String[] p_args) throws RemoteException, NotBoundException {
        new leader();
        //system.out.println("a: " + rf.a + "b: " + rf.b);
        //System.out.println(data);
    }


}
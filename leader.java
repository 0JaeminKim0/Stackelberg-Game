//import src.*;
import comp34120.ex2.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.Math;

final class leader extends PlayerImpl{
    //make class for reaction function design
    public ReactionFunction rf = new ReactionFunction();
    //get value of history data
    private Record data[];
    //get the value of estimated reaction function
    //rf.reactionFunction payoff = null;

    private leader() throws RemoteException, NotBoundException
	  {
		    super(PlayerType.LEADER, "Leader");
    }

    private record(){
      this.data = new Record[100];
    }

    //estimating the reaction Function
    private void estimatedReactionFunction(int latestDate){
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
        for(s_day = e_day; s_day < e_day; ++s_day){
          Record record = this.data[s_day];
          sum_L = record.m_leaderPrice;
          sum_F = record.m_followerPrice;
          sum_L2 = Math.pow(day.m_leaderPrice, 2);
          sum_LF = record.m_leaderPrice * record.m_followerPrice;
        }

        double est_a = ((sum_L2*sum_F)-(sum_L*sum_LF))/((e_day * sum_L2)-Math.pow(sum_L, 2));
        double est_b = ((e_day*sum_LF)-(sum_L*sum_F))/((e_day*sum_L2)-Math.pow(sum_L, 2));

        rf.reactionFunction(est_a, est_b);

    }


    // Making a revenue

    private double demandModel(float U_L, float U_F){
        return 2 - U_L + 0.3*U_F;
    }

    private double revenue(float U_L, float U_F){
        return (U_L - 1) * demandModel(U_L, U_F);
    }

    //
    public static void main(final String[] p_args) throws RemoteException, NotBoundException{
      new leader();
      //system.out.println("a: " + rf.a + "b: " + rf.b);
      system.out.println(data);
    }


}

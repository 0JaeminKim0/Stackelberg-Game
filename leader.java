import src.*;
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
    private ReactionFunction reactionfunction;
    //get value of history data
    private record data[];

    private leader() throws RemoteException, NotBoundException
	{
		super(PlayerType.LEADER, "Leader");
	}


    //estimating the reaction Function
    private void estimatedReactionFunction(int latestDate){
        double sum_L = 0;
        double sum_F = 0;
        double sum_L2 = 0;
        double sum_LF = 0;

    }


    // Making a revenue

    private double demandModel(float U_L, float U_F){
        return 2 - U_L + 0.3*U_F;
    }

    private double revenue(float U_L, float U_F){
        return (U_L - 1) * demandModel(U_L, U_F);
    }

    //
    

}
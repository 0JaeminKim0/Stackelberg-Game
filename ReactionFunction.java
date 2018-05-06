/*  Making reaction function which is linear function form
    set reaction function = a + b *
*/
class ReactionFunction{

    public double a;
    public double b;
    public double result;

    // when a and b has specific value
    //linear function form
    public void reactionFunction(double a, double b){
        this.a = a;
        this.b = b;
    }

    //Constructing Reaction function and return the result
    public double getReactionFinction(double U_L){
        result = a + b * U_L;
        return result;
    }

    public double getA(){
        return a;
    }
    public double getB(){
        return b;
    }
}

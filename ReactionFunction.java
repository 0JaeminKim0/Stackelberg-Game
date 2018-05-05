/*  Making reaction function which is linear function form
    set reaction function = a + b * 
*/
class ReactionFunction{

    private float a;
    private float b;
    private float result;

    // when a and b has specific value
    //linear function form
    public reactionFunction(float a, float b){
        this.a = a;
        this.b = b;
    }

    //Constructing Reaction function and return the result
    public float getReactionFinction(float U_L){
        result = a + b * U_L;
        return result;
    }

    public float getA(){
        return a;
    }
    public float getB(){
        return b;
    }

    public setA(float a_){
        a = a_;
    }
    
    public setB(flaot b_){
        b = b_;
    }

}
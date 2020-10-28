package  dependencias_hh.jhelper.metrics.extrametrics;

import dependencias_class.JavaRandomGenerator;

//import org.uma.jmetal.util.front.Front;
//import org.uma.jmetal.util.pseudorandom.PseudoRandomGenerator;
//import org.uma.jmetal.util.pseudorandom.impl.JavaRandomGenerator;

import  dependencias_hh.jhelper.util.ExtraPseudoRandom;
import  dependencias_hh.jhelper.util.metrics.Calculator;
import dependencias_interfaces.Front;
import dependencias_interfaces.PseudoRandomGenerator;

/**
 *
 * @author vinicius
 */
public class RandomMetric extends Calculator{

    protected PseudoRandomGenerator random;
    public RandomMetric(int numberOfObjectives) {
        super(numberOfObjectives);
        random=new JavaRandomGenerator();
        long seed=ExtraPseudoRandom.getInstance().getSeed();
        if(seed!=-1){
            random.setSeed(seed);
        }
    }

    @Override
    public double calculate(Front front, double[] maximumValues, double[] minimumValues) {
        return random.nextDouble();
    }
    
}

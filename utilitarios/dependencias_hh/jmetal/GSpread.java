package dependencias_hh.jmetal;

import java.util.List;

import dependencias_class.ArrayFront;
import dependencias_hh.jhelper.util.metrics.GeneralizedSpread;
import dependencias_interfaces.Front;
//import org.uma.jmetal.qualityindicator.impl.GeneralizedSpread;
//import org.uma.jmetal.solution.Solution;
//import org.uma.jmetal.util.front.Front;
//import org.uma.jmetal.util.front.imp.ArrayFront;
import dependencias_interfaces.Solution;

/**
 *
 * @author vinicius
 */
public class GSpread<S extends Solution<?>> extends GeneralizedSpread<S>{

    public double generalizedSpread(List f1set, List pfSet, int numberOfObj) {
        Front f1=new ArrayFront(f1set);
        Front pf=new ArrayFront(pfSet);
        return this.generalizedSpread(f1, pf);
    }
    
}

package operadores;

//import org.uma.jmetal.operator.crossover.CrossoverOperator;
//import org.uma.jmetal.solution.Solution;

import java.util.List;

import dependencias_interfaces.CrossoverOperator;
import dependencias_interfaces.Solution;

/**
 * Created by FlapKap on 27-05-2017.
 */
@SuppressWarnings("serial")
public class TwoPointCrossover<T> implements CrossoverOperator<Solution<T>> {
  NPointCrossover<T> operator;

  public TwoPointCrossover(double probability) {
    this.operator = new NPointCrossover<>(probability, 2);
  }

  @Override
  public List<Solution<T>> execute(List<Solution<T>> solutions) {
    return operator.execute(solutions);
  }

//  @Override
  public double getCrossoverProbability() {
    return operator.getCrossoverProbability() ;
  }

  @Override
  public int getNumberOfRequiredParents() {
    return operator.getNumberOfRequiredParents();
  }

  @Override
  public int getNumberOfGeneratedChildren() {
    return 2;
  }
}

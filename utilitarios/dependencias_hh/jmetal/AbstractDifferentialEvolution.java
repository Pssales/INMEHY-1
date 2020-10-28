package dependencias_hh.jmetal;

import dependencias_abstract.AbstractEvolutionaryAlgorithm;
import dependencias_hh.operator.DifferentialEvolutionCrossover;
import dependencias_hh.operator.DifferentialEvolutionSelection;
import dependencias_interfaces.DoubleSolution;

//import org.uma.jmetal.operator.crossover.impl.DifferentialEvolutionCrossover;
//import org.uma.jmetal.operator.selection.impl.DifferentialEvolutionSelection;
//import org.uma.jmetal.solution.doublesolution.DoubleSolution;

/**
 * Abstract class representing differential evolution (DE) algorithms
 *
 * @author Antonio J. Nebro
 * @version 1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractDifferentialEvolution<Result> extends AbstractEvolutionaryAlgorithm<DoubleSolution, Result> {
  protected DifferentialEvolutionCrossover crossoverOperator ;
  protected DifferentialEvolutionSelection selectionOperator ;
}

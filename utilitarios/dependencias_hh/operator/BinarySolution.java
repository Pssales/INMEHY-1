package dependencias_hh.operator;

import dependencias_interfaces.Solution;

//import org.uma.jmetal.solution.Solution;
//import org.uma.jmetal.util.binarySet.BinarySet;

/**
 * Interface representing a binary (bitset) solutions
 *
 * @author Antonio J. Nebro <antonio@lcc.uma.es>
 */
public interface BinarySolution extends Solution<BinarySet> {
  int getNumberOfBits(int index) ;
  int getTotalNumberOfBits() ;
}

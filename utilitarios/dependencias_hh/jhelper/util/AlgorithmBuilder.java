package  dependencias_hh.jhelper.util;

import dependencias_hh.jhelper.core.ParametersforHeuristics;
import dependencias_hh.jhelper.imp.crossover.BlxAlphaCrossover;
//import dependencias_hh.jhelper.imp.crossover.DifferentialEvolution;
import dependencias_hh.jhelper.imp.crossover.HuxCrossover;
import dependencias_hh.jhelper.imp.crossover.IntegerSbxCrossover;
import dependencias_hh.jhelper.imp.crossover.NullCross;
import dependencias_hh.jhelper.imp.crossover.OnePointCrossover;
import dependencias_hh.jhelper.imp.crossover.PmxCrossover;
import dependencias_hh.jhelper.imp.crossover.SbxCrossover;
import dependencias_hh.jhelper.imp.mutation.BitFlipMuta;
import dependencias_hh.jhelper.imp.mutation.IntegerPolynomialMuta;
import dependencias_hh.jhelper.imp.mutation.NonUniformMuta;
import dependencias_hh.jhelper.imp.mutation.NullMuta;
import dependencias_hh.jhelper.imp.mutation.PermutationSwapMuta;
import dependencias_hh.jhelper.imp.mutation.PolynomialMuta;
import dependencias_hh.jhelper.imp.mutation.UniformMuta;
import dependencias_interfaces.CrossoverOperator;
import dependencias_interfaces.MutationOperator;
import dependencias_interfaces.Problem;
import dependencias_interfaces.SelectionOperator;
import dependencias_interfaces.Solution;

//import dependencias_interfaces.CrossoverOperator;
//import dependencias_interfaces.MutationOperator;
//import dependencias_interfaces.Problem;
//import dependencias_interfaces.SelectionOperator;
//import dependencias_interfaces.Solution;

import dependencias_abstract.Operator;
import dependencias_class.BinaryTournamentSelection;

import java.util.HashMap;
import java.util.Map;

import javax.management.JMException;
/**
 * This class builds algorithms.
 */
/**
 * The type Algorithm builder.
 *
 * @param <S> jMetal need.
 */
public class AlgorithmBuilder<S extends Solution<?>> {

    /**
     * The Problem.
     */
    protected Problem problem;

    /**
     * Instantiates a new Algorithm builder.
     *
     * @param problem the problem
     */
    public AlgorithmBuilder(Problem problem) {
        this.problem = problem;
    }

    /**
     * Generate cross crossover operator.
     *
     * @param pmxswap the config params
     * @return the crossover operator
     * @throws JMException the jm exception
     */
    public CrossoverOperator generateCross(ParametersforHeuristics pmxswap) throws JMException {
    	CrossoverOperator operator = null;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("probability", pmxswap.getCrossoverProbality());
        if ("sbxCrossover".equalsIgnoreCase(pmxswap.getCrossoverName())) {
            parameters.put("distributionIndex", pmxswap.getCrossoverDistribution());
            operator = new SbxCrossover();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

        } else if ("blxAlphaCrossover".equalsIgnoreCase(pmxswap.getCrossoverName())) {
            parameters.put("alpha", pmxswap.getAlpha());
            operator = new BlxAlphaCrossover();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

        } else if ("huxCrossover".equalsIgnoreCase(pmxswap.getCrossoverName())) {
            operator = new HuxCrossover();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

        } else if ("onePointCrossover".equalsIgnoreCase(pmxswap.getCrossoverName())) {
            operator = new OnePointCrossover();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

        } else if ("NullCross".equalsIgnoreCase(pmxswap.getCrossoverName())) {
            operator = new NullCross();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

//        } else if ("DifferentialEvolutionCrossover".equalsIgnoreCase(pmxswap.getCrossoverName())) {
//            parameters.put("cr", pmxswap.getDeCr());
//            parameters.put("f", pmxswap.getDeF());
//            parameters.put("k", pmxswap.getDeK());
//            parameters.put("variant", pmxswap.getDeVariant());
//            operator = new DifferentialEvolution(0, 0, pmxswap.getDeVariant());
//            operator.setParameters(parameters);
//            operator.allocateParameters();

        } else if ("pmxCrossover".equalsIgnoreCase(pmxswap.getCrossoverName())) {
            operator = (CrossoverOperator) new PmxCrossover();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

        } else if ("sbxIntegerCrossover".equalsIgnoreCase(pmxswap.getCrossoverName())) {
            parameters.put("distributionIndex", pmxswap.getCrossoverDistribution());
            operator = new IntegerSbxCrossover();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

        }
        return (CrossoverOperator) operator;
    }

    /**
     * Generate muta mutation operator.
     *
     * @param pmxswap the config params
     * @param maxIterations the max iterations
     * @return the mutation operator
     * @throws JMException the jm exception
     */
    public MutationOperator generateMuta( dependencias_hh.jhelper.core.ParametersforHeuristics pmxswap, int maxIterations)
            throws JMException {
    	MutationOperator operator = null;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("probability", pmxswap.getMutationProbability());
        if ("polynomialMutation".equalsIgnoreCase(pmxswap.getMutationName())) {
            parameters.put("distributionIndex", pmxswap.getMutationDistribution());
            operator = new PolynomialMuta();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

        } else if ("uniformMutation".equalsIgnoreCase(pmxswap.getMutationName())) {
            parameters.put("perturbation", pmxswap.getMutationPertubation());
            operator = new UniformMuta();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

//        } else if ("nonUniformMutation".equalsIgnoreCase(pmxswap.getMutationName())) {
//            parameters.put("perturbation", pmxswap.getMutationPertubation());
//            parameters.put("maxIterations", maxIterations);
//            operator = new NonUniformMuta();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

        } else if ("bitFlipMutation".equalsIgnoreCase(pmxswap.getMutationName())) {
            operator = new BitFlipMuta();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

//        } else if ("nullMutation".equalsIgnoreCase(pmxswap.getMutationName())) {
//            operator = new NullMuta();
////            operator.setParameters(parameters);
////            operator.allocateParameters();
//
//        } else if ("PermutationSwapMutation".equalsIgnoreCase(pmxswap.getMutationName())) {
//            operator = new PermutationSwapMuta();
////            operator.setParameters(parameters);
////            operator.allocateParameters();

        } else if ("integerPolynomialMutation".equalsIgnoreCase(pmxswap.getMutationName())) {
            parameters.put("distributionIndex", pmxswap.getMutationDistribution());
            operator = new IntegerPolynomialMuta();
//            operator.setParameters(parameters);
//            operator.allocateParameters();

        }
        return (MutationOperator) operator;
    }

    /**
     * Generate selection selection operator.
     *
     * @return the selection operator
     * @throws JMException the jm exception
     */
    public SelectionOperator generateSelection() throws JMException {
        SelectionOperator se = new BinaryTournamentSelection();
        return se;
    }

    
    
    /*
    
    /**
     * Create ibea standard meta-heuristic.
     *
     * @param configAlg the config alg
     * @param configHeuristic the config heuristic
     * @return the standard meta-heuristic
     * @throws JMException the jm exception
     */
    /*
    public StandardMetaheuristic createIbea(ParametersforAlgorithm configAlg,
            ParametersforHeuristics configHeuristic) throws JMException {
    	
    	
    	System.out.println("NAO NAO NAO IBEA %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" +
    			"************************");
        SelectionOperator selection = this.generateSelection();
        CrossoverOperator crossover = this.generateCross(configHeuristic);
        MutationOperator mutation = this.generateMuta(configHeuristic, configAlg.getMaxIteractions());
        StandardMetaheuristic algorithm = new Ibea(problem, configAlg.getPopulationSize(),
                configAlg.getArchiveSize(),
                configAlg.getMaxIteractions() * configAlg.getPopulationSize(),
                selection, crossover, mutation);
        return algorithm;
    }*/

    /**
     * Create nsga-ii standard meta-heuristic.
     *
     * @param configAlg the config alg
     * @param configHeuristic the config heuristic
     * @return the standard meta-heuristic
     * @throws JMException the jm exception
     */
   /*
    public StandardMetaheuristic createNsgaii(ParametersforAlgorithm configAlg,
            ParametersforHeuristics configHeuristic) throws JMException {
        SelectionOperator selection = this.generateSelection();
        CrossoverOperator crossover = this.generateCross(configHeuristic);
        MutationOperator mutation = this.generateMuta(configHeuristic, configAlg.getMaxIteractions());

        System.out.println("NAO NAO NAO NSGA-II %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" +
    			"************************");
        StandardMetaheuristic algorithm = new Nsgaii(problem,
                configAlg.getMaxIteractions() * configAlg.getPopulationSize(),
                configAlg.getPopulationSize(), crossover,
                mutation, selection, new CrowdingDistanceComparator<S>(), new SequentialSolutionListEvaluator());
        return algorithm;
    }*/

    /**
     * Create spea 2 standard meta-heuristic.
     *
     * @param configAlg the config alg
     * @param configHeuristic the config heuristic
     * @return the standard meta-heuristic
     * @throws JMException the jm exception
     */
    /*
    public StandardMetaheuristic createSpea2(ParametersforAlgorithm configAlg,
            ParametersforHeuristics configHeuristic) throws JMException {
        SelectionOperator selection = this.generateSelection();
        CrossoverOperator crossover = this.generateCross(configHeuristic);
        MutationOperator mutation = this.generateMuta(configHeuristic, configAlg.getMaxIteractions());
        
        System.out.println("NAO NAO NAO SPEA2 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%" +
    			"************************");
        
        StandardMetaheuristic algorithm = new Spea2(problem, configAlg.getMaxIteractions(),
                configAlg.getPopulationSize(), crossover,
                mutation, selection, new SequentialSolutionListEvaluator());
        return algorithm;
    }*/

    /**
     * Create spea 2 standard meta-heuristic.
     *
     * @param configAlg the config alg
     * @param configHeuristic the config heuristic
     * @return the standard meta-heuristic
     * @throws JMException the jm exception
     */
    /*
    public StandardMetaheuristic createGde3(ParametersforAlgorithm configAlg,
            ParametersforHeuristics configHeuristic) throws JMException {
        configHeuristic.setDeCr(0.2);//TEMP @TODO new parameter file
        configHeuristic.setDeF(0.2);
        CrossoverOperator crossover = this.generateCross(configHeuristic);
        StandardMetaheuristic algorithm = new Gde3((DoubleProblem) problem,
                configAlg.getPopulationSize(), configAlg.getMaxIteractions()
                * configAlg.getPopulationSize(), new DifferentialEvolutionSelection(),
                (DifferentialEvolutionCrossover) crossover,
                new SequentialSolutionListEvaluator());
        return algorithm;
    }*/

    /**
     * Create spea 2 standard meta-heuristic.
     *
     * @param configAlg the config alg
     * @param configHeuristic the config heuristic
     * @return the standard meta-heuristic
     * @throws JMException the jm exception
     */
    /*
    public StandardMetaheuristic createPaes(ParametersforAlgorithm configAlg,
            ParametersforHeuristics configHeuristic) throws JMException {
        int biSections = 3;
        MutationOperator mutation = this.generateMuta(configHeuristic, configAlg.getMaxIteractions());
        StandardMetaheuristic algorithm = new Paes(problem, configAlg.getPopulationSize(), configAlg.getMaxIteractions()
                * configAlg.getPopulationSize(), biSections, mutation);
        return algorithm;
    }*/

    /**
     * Create standard metaheuristic.
     *
     * @param configAlg the config alg
     * @param configHeuristic the config heuristic
     * @return the standard metaheuristic
     * @throws JMException the jm exception
     * @throws FileNotFoundException the file not found exception
     */
    /*
    public StandardMetaheuristic create(ParametersforAlgorithm configAlg,
            ParametersforHeuristics configHeuristic) throws JMException, FileNotFoundException {
        switch (configAlg.getAlgorithmName()) {
            case "Ibea":
                return createIbea(configAlg, configHeuristic);
            case "Nsgaii":
                return this.createNsgaii(configAlg, configHeuristic);
            case "Spea2":
                return createSpea2(configAlg, configHeuristic);
            case "Gde3":
                return createGde3(configAlg, configHeuristic);
            case "Paes":
                return createPaes(configAlg, configHeuristic);
            default:
                System.err.println("Algorithm not found");
                return null;
        }
    }*/
    

}

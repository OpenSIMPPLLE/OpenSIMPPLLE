package simpplle.comcode;

/**
 * The University of Montana owns copyright of the designated documentation contained 
 * within this file as part of the software product designated by Uniform Resource Identifier 
 * UM-OpenSIMPPLLE-1.0.  By copying this file the user accepts the University of Montana
 * Open Source License Contract pertaining to this documentation and agrees to abide by all 
 * restrictions, requirements, and assertions contained therein.  All Other Rights Reserved.
 *
 * @author Documentation by Brian Losi
 * <p>Original source authorship: Steve Verrill
 *
*/

public class CDF_Weibull2 extends Object {

/**
*
*This method calculates the inverse
*of the 2-parameter Weibull cumulative distribution function.
*<p>
*
*@param   lambda    The 2-parameter Weibull scale parameter.
*                   In \LaTeX notation,
*                   the distribution function is
*                   1 - \exp(-(\lambda x)^{\beta}).
*@param   beta      The 2-parameter Weibull shape parameter.
*                   In \LaTeX notation,
*                   the distribution function is
*                   1 - \exp(-(\lambda x)^{\beta}).
*@param   p         p must lie between 0 and 1.  w2inv returns
*                   the 2-parameter Weibull cdf inverse evaluated
*                   at p.
*
*/

//  FIX: Eventually I should build in a check that p lies in (0,1)

   public static double w2inv(double lambda, double beta, double p) {

      double power,x;

      power = 1.0/beta;
      x = Math.pow(-Math.log(1.0 - p),power)/lambda;

      return x;

   }


/**
*
*This method calculates the 2-parameter Weibull
*cumulative distribution function.
*<p>
*
*@param   lambda    The 2-parameter Weibull scale parameter.
*                   In \LaTeX notation,
*                   the distribution function is
*                   1 - \exp(-(\lambda x)^{\beta}).
*@param   beta      The 2-parameter Weibull shape parameter.
*                   In \LaTeX notation,
*                   the distribution function is
*                   1 - \exp(-(\lambda x)^{\beta}).
*@param   x         x must be greater than 0.  w2inv returns
*                   the 2-parameter Weibull cumulative
*                   distribution function evaluated
*                   at x.
*
*@author Steve Verrill
*@version .5 --- January 10, 2001
*
*/

//  FIX: Eventually I should build in a check that x is greater than 0

   public static double w2cdf(double lambda, double beta, double x) {

      double p;

//      p = 1.0 - Math.exp(-Math.pow(lambda*x,beta));
      p = 1.0 - Math.exp(-lambda * Math.pow(x,beta));

      return p;

   }

}




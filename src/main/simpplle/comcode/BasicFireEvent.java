package simpplle.comcode;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a process occurrence that spreads fire to all immediate neighbors. This was the original
 * SIMPPLLE spreading behavior. Fire spreads at a constant rate, which is only disturbed by fire
 * spread rules and fire suppression lines. This algorithm results in square-shaped fires.
 */
public class BasicFireEvent extends ProcessOccurrenceSpreadingFire {

  /**
   * Creates a basic fire event.
   *
   * @param evu source of fire
   * @param lifeform life form
   * @param processData process probability
   * @param timeStep time step
   */
  public BasicFireEvent(Evu evu, Lifeform lifeform, ProcessProbability processData, int timeStep) {
    super(evu,lifeform,processData,timeStep);
  }

  /**
   * Spreads fire from a burning vegetation unit to all adjacent vegetation units. Fire spread
   * rules are applied to each neighbor. Units that burn are appended to a list of burned units.
   *
   * @param source a burning vegetation unit
   * @param burned a list to store units that have been spread to
   * @param isExtreme indicates if fire is intense enough to influence weather
   */
  @Override
  void spreadToNeighbors(Evu source, ArrayList<Evu> burned, boolean isExtreme) {

    List<AdjacentData> adjacencies = source.getAdjacencies();
    if (adjacencies == null) return;

    for (AdjacentData adjacent : adjacencies) {
      if (!hasSuppressionLine(adjacent.evu)) {
        boolean alreadyBurning = adjacent.evu.hasFireAnyLifeform();
        if (Evu.doSpread(source, adjacent.evu, source.getDominantLifeformFire())) {
          if (!alreadyBurning) {
            burned.add(adjacent.evu);
          }
        }
      }
    }
  }

}

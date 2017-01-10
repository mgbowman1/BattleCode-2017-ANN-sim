package battlecode.common;

/**
 * Defines constants that affect gameplay.
 */
@SuppressWarnings("unused")
public interface GameConstants {

    /**
     * The current spec version the server compiles with.
     */
    String SPEC_VERSION = "1.0";

    // *********************************
    // ****** MAP CONSTANTS ************
    // *********************************

    /** The minimum possible map height. */
    int MAP_MIN_HEIGHT = 30;

    /** The maximum possible map height. */
    int MAP_MAX_HEIGHT = 100;

    /** The minumum possible map width. */
    int MAP_MIN_WIDTH = 30;

    /** The maxiumum possible map width. */
    int MAP_MAX_WIDTH = 100;

    // *********************************
    // ****** GAME PARAMETERS **********
    // *********************************

    /** The number of victory points required to win the game. */
    int VICTORY_POINTS_TO_WIN = 1000;

    /** The number of longs that your team can remember between games. */
    int TEAM_MEMORY_LENGTH = 32;

    /** The number of indicator strings that a player can associate with a robot */
    int NUMBER_OF_INDICATOR_STRINGS = 3;

    /** The bytecode penalty that is imposed each time an exception is thrown */
    int EXCEPTION_BYTECODE_PENALTY = 500;

    /** Maximum archons that can appear on a map (per team). */
    int NUMBER_OF_ARCHONS_MAX = 3;

    // *********************************
    // ****** TREES ********************
    // *********************************

    /** The max health of bullet trees */
    float BULLET_TREE_MAX_HEALTH = 50;

    /** The radius of bullet trees */
    float BULLET_TREE_RADIUS = 1F;

    /** The cost in bullets to spawn a bullet tree */
    float BULLET_TREE_COST = 50;

    /** The amount of health bullet trees lose per turn from decay */
    float BULLET_TREE_DECAY_RATE = BULLET_TREE_MAX_HEALTH / 100f;

    /** The amount of bullets produced from one unit of health per bullet tree */
    float BULLET_TREE_BULLET_PRODUCTION_RATE = 1f / BULLET_TREE_MAX_HEALTH;
    
    /** Number of cooldown turns robot must wait between planting trees */
    int BULLET_TREE_CONSTRUCTION_COOLDOWN = 10;

    /** The min radius a neutral tree can have */
    float NEUTRAL_TREE_MIN_RADIUS = .5F;

    /** The max radius a neutral tree can have */
    float NEUTRAL_TREE_MAX_RADIUS = 10;

    /**
     * The rate at which the max health of neutral trees are determined;
     * i.e. maxHealth = NEUTRAL_TREE_HEALTH_RATE * treeRadius
     */
    float NEUTRAL_TREE_HEALTH_RATE = 200;

    /**
     * The chop damage when lumberjacks perform the chop action on a tree.
     */
    float LUMBERJACK_CHOP_DAMAGE = 5;
    
    /**
     * The damage a tank does to a tree when it attempts to move on top of it.
     */
    float TANK_BODY_DAMAGE = 2;
    
    /**
     * Planted units (trees and non-gardener non-archon robots) start at 20% health.
     */
    float PLANTED_UNIT_STARTING_HEALTH_FRACTION = 0.2f;

    // *********************************
    // ****** BULLETS ******************
    // *********************************

    /** The amount that each team starts with */
    float BULLETS_INITIAL_AMOUNT = 300;
    
    /** The bullet income per turn (independent of number of archons).  */
    float ARCHON_BULLET_INCOME = 2;

    /** The decrease in bullet income per turn per bullet that you have. */
    float BULLET_INCOME_UNIT_PENALTY = 0.01F;

    // *********************************
    // ****** ATTACKING ****************
    // *********************************

    /** The degrees at which the bullets are spread apart in a triad shot */
    float TRIAD_SPREAD_DEGREES = 20;

    /** The degrees at which the bullets are spread apart in a pentad shot */
    float PENTAD_SPREAD_DEGREES = 15;

    /** The bullet cost to fire a single shot */
    float SINGLE_SHOT_COST = 1;

    /** The bullet cost to fire a triad shot */
    float TRIAD_SHOT_COST = 4;

    /** The bullet cost to fire a pentad shot */
    float PENTAD_SHOT_COST = 6;

    /** The distance from the outer edge of a robot bullets are spawned */
    float BULLET_SPAWN_OFFSET = .05f;

    /** The radius around a lumberjack affected by a strike() */
    float LUMBERJACK_STRIKE_RADIUS = 1;
    
    // *********************************
    // ****** MESSAGING ****************
    // *********************************

    /** The size of the team-shared array for signaling*/
    int BROADCAST_MAX_CHANNELS = 1000;

    // *********************************
    // ****** MISC. ********************
    // *********************************

    /** The number of bullets required for 1 victory point */
    int BULLET_EXCHANGE_RATE = 10;

    /**
     * The distance between the edge of the robot spawning a robot/tree
     * to spawned robot's/tree's edge
     */
    float GENERAL_SPAWN_OFFSET = .01f;

    /** The amount of health a tree gains when watered */
    float WATER_HEALTH_REGEN_RATE = BULLET_TREE_MAX_HEALTH/10f;

    /** The amount of health a robot gains when repaired by an archon */
    float REPAIR_HEALTH_REGEN_RATE = 1;

    /** The max radius a robot can have */
    float MAX_ROBOT_RADIUS = 2;
    
    // *********************************
    // ****** GAMEPLAY PROPERTIES ******
    // *********************************

    /** The default game seed. **/
    int GAME_DEFAULT_SEED = 6370;

    /** The default game maxiumum number of rounds. **/
    int GAME_DEFAULT_ROUNDS = 3000;
}

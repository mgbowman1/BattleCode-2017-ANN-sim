package battlecode.common;

import java.util.LinkedList;

/**
 * A RobotController allows contestants to make their robot sense and interact
 * with the game world. When a contestant's <code>RobotPlayer</code> is
 * constructed, it is passed an instance of <code>RobotController</code> that
 * controls the newly created robot.
 */
@SuppressWarnings("unused")
public strictfp class RobotController {
	
	private final Map map;
	private RobotInfo ri;
	private int attacks = 0;
	private int moves = 0;
	
	public RobotController(Map map, RobotInfo ri) {
		this.map = map;
		this.ri = ri;
		if (ri.type.equals(RobotType.ARCHON)) RobotMonitor.setMaxBytecodes(20000);
		else RobotMonitor.setMaxBytecodes(10000);
	}

    // *********************************
    // ****** GLOBAL QUERY METHODS *****
    // *********************************

    /**
     * Gets the number of rounds in the game. After this many rounds, if neither
     * team has been destroyed, then the tiebreakers will be used.
     *
     * @return the number of rounds in the game.
     *
     * @battlecode.doc.costlymethod
     */
    int getRoundLimit() {
    	RobotMonitor.useBytecodes(1);
    	return map.getRounds();
    }

    /**
     * Returns the current round number, where round 0 is the first round of the
     * match.
     *
     * @return the current round number, where 0 is the first round of the
     * match.
     *
     * @battlecode.doc.costlymethod
     */
    int getRoundNum() {
    	RobotMonitor.useBytecodes(1);
    	return map.getRoundsPassed();
    }

    /**
     * Gets the team's total bullet supply.
     *
     * @return the team's total bullet supply.
     *
     * @battlecode.doc.costlymethod
     */
    float getTeamBullets() {
    	RobotMonitor.useBytecodes(1);
    	return (float)map.getBullets(ri.team);
    }

    /**
     * Gets the team's total victory points.
     *
     * @return the team's total victory points.
     *
     * @battlecode.doc.costlymethod
     */
    int getTeamVictoryPoints() {
    	RobotMonitor.useBytecodes(1);
    	return map.getVictoryPoints(ri.team);
    }

    /**
     * Returns the number of robots on your team, including your archons.
     * If this number ever reaches zero, the opposing team will automatically
     * win by destruction.
     *
     * @return the number of robots on your team, including your archons.
     *
     * @battlecode.doc.costlymethod
     */
    int getRobotCount() {
    	RobotMonitor.useBytecodes(20);
    	int ret = 0;
    	for (RobotInfo r : map.robots) {
    		if (r.team.equals(ri.team)) ret++;
    	}
    	return ret;
    }

    /**
     * Returns the number of trees on your team.
     *
     * @return the number of trees on your team.
     *
     * @battlecode.doc.costlymethod
     */
    int getTreeCount() {
    	RobotMonitor.useBytecodes(20);
    	int ret = 0;
    	for (TreeInfo t : map.trees) {
    		if (t.team.equals(ri.team)) ret++;
    	}
    	return ret;
    }

    /**
     * Returns a list of the INITIAL locations of the archons of a particular
     * team. The locations will be sorted by increasing x, with ties broken by
     * increasing y. Will return an empty list if you query for NEUTRAL.
     *
     * @param t the team whose archons you want to query the initial locations
     * for. Will return an empty list if you query for NEUTRAL.
     * @return a list of the INITIAL locations of the archons of that team, or
     * an empty list for team NEUTRAL.
     *
     * @battlecode.doc.costlymethod
     */
    MapLocation[] getInitialArchonLocations(Team t) {
    	RobotMonitor.useBytecodes(100);
    	return map.getStartingArchons(t);
    	
    }

    // *********************************
    // ****** UNIT QUERY METHODS *******
    // *********************************

    /**
     * Use this method to access your ID.
     *
     * @return the ID of this robot.
     *
     * @battlecode.doc.costlymethod
     */
    int getID() {
    	RobotMonitor.useBytecodes(1);
    	return ri.ID;
    }

    /**
     * Gets the Team of this robot.
     *
     * @return this robot's Team.
     *
     * @battlecode.doc.costlymethod
     */
    Team getTeam() {
    	RobotMonitor.useBytecodes(1);
    	return ri.team;
    }

    /**
     * Gets this robot's type (SOLDIER, ARCHON, etc.).
     *
     * @return this robot's type.
     *
     * @battlecode.doc.costlymethod
     */
    RobotType getType() {
    	RobotMonitor.useBytecodes(1);
    	return ri.type;
    }

    /**
     * Gets this robot's current location.
     *
     * @return this robot's current location.
     *
     * @battlecode.doc.costlymethod
     */
    MapLocation getLocation() {
    	RobotMonitor.useBytecodes(1);
    	return ri.location;
    }

    /**
     * Gets this robot's current health.
     *
     * @return this robot's current health.
     *
     * @battlecode.doc.costlymethod
     */
    float getHealth() {
    	RobotMonitor.useBytecodes(1);
    	return (float)ri.health;
    }
    
    /**
     * Returns the number of times the robot has attacked this turn.
     * 
     * @return the number of times the robot has attacked this turn.
     *
     * @battlecode.doc.costlymethod
     */
    int getAttackCount() {
    	RobotMonitor.useBytecodes(1);
    	return attacks;
    }
    
    /**
     * Returns the number of times the robot has moved this turn.
     * 
     * @return the number of times the robot has moved this turn.
     *
     * @battlecode.doc.costlymethod
     */
    int getMoveCount() {
    	RobotMonitor.useBytecodes(1);
    	return moves;
    }

    // ***********************************
    // ****** GENERAL SENSOR METHODS *****
    // ***********************************

    /**
     * Senses whether a MapLocation is on the map. Will throw an exception if
     * the location is not currently within sensor range.
     *
     * @param loc the location to check.
     * @return true if the location is on the map, and false if it is not.
     * @throws GameActionException if the location is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean onTheMap(MapLocation loc) throws GameActionException {
    	RobotMonitor.useBytecodes(5);
    	if (Math.sqrt(Math.pow(loc.x - ri.location.x, 2) + Math.pow(loc.y - ri.location.y, 2)) > ri.type.sensorRadius) throw new GameActionException(GameActionExceptionType.OUT_OF_RANGE, "Check if locaiton exists");
    	else {
    		if (loc.x >= map.getOrigin().x && loc.x < map.getOrigin().x + map.getWidth() && loc.y >= map.getOrigin().y && loc.y < map.getOrigin().y + map.getHeight()) return true;
    		else return false;
    	}
    	
    }

    /**
     * Senses whether a given circle is completely on the map. Will throw an exception if
     * the circle is not completely within sensor range.
     *
     * @param center the center of the circle to check.
     * @param radius the radius of the circle to check.
     * @return true if the circle is completely on the map, false otherwise.
     * @throws GameActionException if any portion of the given circle is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean onTheMap(MapLocation center, float radius) throws GameActionException {
    	RobotMonitor.useBytecodes(5);
    	MapLocation top = new MapLocation(center.x, center.y - radius);
    	MapLocation right = new MapLocation(center.x + radius, center.y);
    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
    	MapLocation left = new MapLocation(center.x - radius, center.y);
    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
    	double badDistance = ri.type.sensorRadius;
    	if (distTop > badDistance || distRight > badDistance || distLeft > badDistance || distBottom > badDistance) throw new GameActionException(GameActionExceptionType.OUT_OF_RANGE, "Check if circle exists");
    	else {
    		if (top.y >= map.getOrigin().y && right.x < map.getOrigin().x + map.getWidth() && left.x >= map.getOrigin().x && bottom.y < map.getOrigin().y + map.getHeight()) return true;
    		else return false;
    	}
    }

    /**
     * Returns true if the given location is within the robot's sensor range.
     *
     * @param loc the location to check.
     * @return whether the given location is within the robot's sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canSenseLocation(MapLocation loc) {
    	RobotMonitor.useBytecodes(5);
    	if (Math.sqrt(Math.pow(loc.x - ri.location.x, 2) + Math.pow(loc.y - ri.location.y, 2)) > ri.type.sensorRadius) return false;
    	else return true;
    }

    /**
     * Returns true if any portion of the given circle is within the robot's sensor range.
     *
     * @param center the center of the circle to check.
     * @param radius the radius of the circle to check.
     * @return whether a portion of the circle is within the robot's sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canSensePartOfCircle(MapLocation center, float radius) {
    	RobotMonitor.useBytecodes(5);
    	MapLocation top = new MapLocation(center.x, center.y - radius);
    	MapLocation right = new MapLocation(center.x + radius, center.y);
    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
    	MapLocation left = new MapLocation(center.x - radius, center.y);
    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
    	double badDistance = ri.type.sensorRadius;
    	if (distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance) return false;
    	else return true;
    }

    /**
     * Returns true if all of the given circle is within the robot's sensor range.
     *
     * @param center the center of the circle to check.
     * @param radius the radius of the circle to check.
     * @return whether all of the circle is within the robot's sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canSenseAllOfCircle(MapLocation center, float radius) {
    	RobotMonitor.useBytecodes(5);
    	MapLocation top = new MapLocation(center.x, center.y - radius);
    	MapLocation right = new MapLocation(center.x + radius, center.y);
    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
    	MapLocation left = new MapLocation(center.x - radius, center.y);
    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
    	double badDistance = ri.type.sensorRadius;
    	if (distTop > badDistance || distRight > badDistance || distLeft > badDistance || distBottom > badDistance) return false;
    	else return true;
    }

    /**
     * Returns whether there is a robot or tree at the given location.
     *
     * @param loc the location to check.
     * @return whether there is a robot or tree at the given location.
     * @throws GameActionException if the location is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean isLocationOccupied(MapLocation loc) throws GameActionException {
    	RobotMonitor.useBytecodes(20);
    	if (Math.sqrt(Math.pow(loc.x - ri.location.x, 2) + Math.pow(loc.y - ri.location.y, 2)) > ri.type.sensorRadius) throw new GameActionException(GameActionExceptionType.OUT_OF_RANGE, "Check if location occupied");
    	else {
    		for (RobotInfo r : map.robots) {
    			if (Math.sqrt(Math.pow(r.location.x - loc.x, 2) + Math.pow(r.location.y - loc.y, 2)) <= r.getRadius()) return true;
    		}
    		for (TreeInfo t : map.trees) {
    			if (Math.sqrt(Math.pow(t.location.x - loc.x, 2) + Math.pow(t.location.y - loc.y, 2)) <= t.getRadius()) return true;
    		}
    		return false;
    	}
    }

    /**
     * Returns whether there is a tree at the given location.
     *
     * @param loc the location to check.
     * @return whether there is a tree at the given location.
     * @throws GameActionException if the location is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean isLocationOccupiedByTree(MapLocation loc) throws GameActionException {
    	RobotMonitor.useBytecodes(20);
    	if (Math.sqrt(Math.pow(loc.x - ri.location.x, 2) + Math.pow(loc.y - ri.location.y, 2)) > ri.type.sensorRadius) throw new GameActionException(GameActionExceptionType.OUT_OF_RANGE, "Check if location occupied by tree");
    	else {
    		for (TreeInfo t : map.trees) {
    			if (Math.sqrt(Math.pow(t.location.x - loc.x, 2) + Math.pow(t.location.y - loc.y, 2)) <= t.getRadius()) return true;
    		}
    		return false;
    	}
    }

    /**
     * Returns whether there is a robot at the given location.
     *
     * @param loc the location to check.
     * @return whether there is a robot at the given location.
     * @throws GameActionException if the location is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean isLocationOccupiedByRobot(MapLocation loc) throws GameActionException {
    	RobotMonitor.useBytecodes(20);
    	if (Math.sqrt(Math.pow(loc.x - ri.location.x, 2) + Math.pow(loc.y - ri.location.y, 2)) > ri.type.sensorRadius) throw new GameActionException(GameActionExceptionType.OUT_OF_RANGE, "Check if location occupied by robot");
    	else {
    		for (RobotInfo r : map.robots) {
    			if (Math.sqrt(Math.pow(r.location.x - loc.x, 2) + Math.pow(r.location.y - loc.y, 2)) <= r.getRadius()) return true;
    		}
    		return false;
    	}
    }

    /**
     * Returns whether there is any robot or tree within a given circle
     *
     * @param center the center of the circle to check.
     * @param radius the radius of the circle to check.
     * @return whether there is a robot or tree in the given circle.
     * @throws GameActionException if any portion of the given circle is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean isCircleOccupied(MapLocation center, float radius) throws GameActionException {
    	RobotMonitor.useBytecodes(20);
    	MapLocation top = new MapLocation(center.x, center.y - radius);
    	MapLocation right = new MapLocation(center.x + radius, center.y);
    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
    	MapLocation left = new MapLocation(center.x - radius, center.y);
    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
    	double badDistance = ri.type.sensorRadius;
    	if (distTop > badDistance || distRight > badDistance || distLeft > badDistance || distBottom > badDistance) throw new GameActionException(GameActionExceptionType.OUT_OF_RANGE, "Check if circle occupied");
    	else {
    		for (RobotInfo r : map.robots) {
    			if (Math.sqrt(Math.pow(r.location.x - center.x, 2) + Math.pow(r.location.y - center.y, 2)) <= radius) return true;
    		}
    		for (TreeInfo t : map.trees) {
    			if (Math.sqrt(Math.pow(t.location.x - center.x, 2) + Math.pow(t.location.y - center.y, 2)) <= radius) return true;
    		}
    		return false;
    	}
    }

    /**
     * Returns whether there is any robot or tree within a given circle, ignoring this robot
     * if it itself occupies the circle
     *
     * @param center the center of the circle to check.
     * @param radius the radius of the circle to check.
     * @return whether there is a robot or tree in the given circle.
     * @throws GameActionException if any portion of the given circle is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean isCircleOccupiedExceptByThisRobot(MapLocation center, float radius) throws GameActionException {
    	RobotMonitor.useBytecodes(20);
    	MapLocation top = new MapLocation(center.x, center.y - radius);
    	MapLocation right = new MapLocation(center.x + radius, center.y);
    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
    	MapLocation left = new MapLocation(center.x - radius, center.y);
    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
    	double badDistance = ri.type.sensorRadius;
    	if (distTop > badDistance || distRight > badDistance || distLeft > badDistance || distBottom > badDistance) throw new GameActionException(GameActionExceptionType.OUT_OF_RANGE, "Check if circle occupied except by this robot");
    	else {
    		for (RobotInfo r : map.robots) {
    			if (r != ri && Math.sqrt(Math.pow(r.location.x - center.x, 2) + Math.pow(r.location.y - center.y, 2)) <= radius) return true;
    		}
    		for (TreeInfo t : map.trees) {
    			if (Math.sqrt(Math.pow(t.location.x - center.x, 2) + Math.pow(t.location.y - center.y, 2)) <= radius) return true;
    		}
    		return false;
    	}
    }

    /**
     * Returns the tree at the given location, or null if there is no tree
     * there.
     *
     * @param loc the location to check.
     * @return the tree at the given location.
     * @throws GameActionException if the location is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    TreeInfo senseTreeAtLocation(MapLocation loc) throws GameActionException {
    	RobotMonitor.useBytecodes(20);
    	if (Math.sqrt(Math.pow(loc.x - ri.location.x, 2) + Math.pow(loc.y - ri.location.y, 2)) > ri.type.sensorRadius) throw new GameActionException(GameActionExceptionType.OUT_OF_RANGE, "Find tree at location");
    	else {
    		for (TreeInfo t : map.trees) {
    			if (Math.sqrt(Math.pow(t.location.x - loc.x, 2) + Math.pow(t.location.y - loc.y, 2)) <= t.getRadius()) return t;
    		}
    		return null;
    	}
    }

    /**
     * Returns the robot at the given location, or null if there is no robot
     * there.
     *
     * @param loc the location to check.
     * @return the robot at the given location.
     * @throws GameActionException if the location is not within sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo senseRobotAtLocation(MapLocation loc) throws GameActionException {
    	RobotMonitor.useBytecodes(20);
    	if (Math.sqrt(Math.pow(loc.x - ri.location.x, 2) + Math.pow(loc.y - ri.location.y, 2)) > ri.type.sensorRadius) throw new GameActionException(GameActionExceptionType.OUT_OF_RANGE, "Find robot at location");
    	else {
    		for (RobotInfo r : map.robots) {
    			if (Math.sqrt(Math.pow(r.location.x - loc.x, 2) + Math.pow(r.location.y - loc.y, 2)) <= r.getRadius()) return r;
    		}
    		return null;
    	}
    }

    /**
     * Returns true if the given tree exists and any part of the given tree is
     * within this robot's sensor range.
     *
     * @param id the ID of the tree to query.
     * @return whether the given tree is within this robot's sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canSenseTree(int id) {
    	RobotMonitor.useBytecodes(5);
    	MapLocation center = new MapLocation(0, 0);
    	float radius = 0;
    	boolean found = false;
    	for (TreeInfo t : map.trees) {
    		if (t.ID == id) {
    			found = true;
    			center = t.location;
    			radius = t.radius;
    			break;
    		}
    	}
    	if (!found) return false;
    	MapLocation top = new MapLocation(center.x, center.y - radius);
    	MapLocation right = new MapLocation(center.x + radius, center.y);
    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
    	MapLocation left = new MapLocation(center.x - radius, center.y);
    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
    	double badDistance = ri.type.sensorRadius;
    	if (distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance) return false;
    	else return true;
    }

    /**
     * Returns true if the given robot exists and any part of the given robot is
     * within this robot's sensor range.
     *
     * @param id the ID of the robot to query.
     * @return whether the given robot is within this robot's sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canSenseRobot(int id) {
    	RobotMonitor.useBytecodes(5);
    	MapLocation center = new MapLocation(0, 0);
    	float radius = 0;
    	boolean found = false;
    	for (RobotInfo r : map.robots) {
    		if (r.ID == id) {
    			found = true;
    			center = r.location;
    			radius = r.getRadius();
    			break;
    		}
    	}
    	if (!found) return false;
    	MapLocation top = new MapLocation(center.x, center.y - radius);
    	MapLocation right = new MapLocation(center.x + radius, center.y);
    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
    	MapLocation left = new MapLocation(center.x - radius, center.y);
    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
    	double badDistance = ri.type.sensorRadius;
    	if (distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance) return false;
    	else return true;
    }

    /**
     * Returns true if the given bullet exists and if it is within this robot's
     * sensor range.
     *
     * @param id the ID of the bullet to query.
     * @return whether the given bullet is within this robot's sensor range.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canSenseBullet(int id) {
    	RobotMonitor.useBytecodes(5);
    	MapLocation center = new MapLocation(0, 0);
    	boolean found = false;
    	for (BulletInfo b : map.bullets) {
    		if (b.ID == id) {
    			found = true;
    			center = b.location;
    			break;
    		}
    	}
    	if (!found) return false;
    	double dist = Math.sqrt(Math.pow(center.x - ri.location.x, 2) + Math.pow(center.y - ri.location.y, 2));
    	if (dist > ri.type.bulletSightRadius) return false;
    	else return true;
    }

    /**
     * Senses information about a particular tree given its ID.
     *
     * @param id the ID of the tree to query.
     * @return a TreeInfo object for the sensed tree.
     * @throws GameActionException if the tree cannot be sensed (for example,
     * if it doesn't exist or is out of sight range).
     *
     * @battlecode.doc.costlymethod
     */
    TreeInfo senseTree(int id) throws GameActionException {
    	RobotMonitor.useBytecodes(25);
    	MapLocation center = new MapLocation(0, 0);
    	float radius = 0;
    	TreeInfo ret = null;
    	boolean found = false;
    	for (TreeInfo t : map.trees) {
    		if (t.ID == id) {
    			found = true;
    			center = t.location;
    			radius = t.radius;
    			ret = t;
    			break;
    		}
    	}
    	if (!found) throw new GameActionException(GameActionExceptionType.CANT_DO_THAT, "Get info on tree by id");
    	MapLocation top = new MapLocation(center.x, center.y - radius);
    	MapLocation right = new MapLocation(center.x + radius, center.y);
    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
    	MapLocation left = new MapLocation(center.x - radius, center.y);
    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
    	double badDistance = ri.type.sensorRadius;
    	if (distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance) throw new GameActionException(GameActionExceptionType.CANT_DO_THAT, "Get info on tree by id");
    	else return ret;
    }

    /**
     * Senses information about a particular robot given its ID.
     *
     * @param id the ID of the robot to query.
     * @return a RobotInfo object for the sensed robot.
     * @throws GameActionException if the robot cannot be sensed (for example,
     * if it doesn't exist or is out of sight range).
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo senseRobot(int id) throws GameActionException {
    	RobotMonitor.useBytecodes(25);
    	MapLocation center = new MapLocation(0, 0);
    	float radius = 0;
    	RobotInfo ret = null;
    	boolean found = false;
    	for (RobotInfo r : map.robots) {
    		if (r.ID == id) {
    			found = true;
    			center = r.location;
    			radius = r.getRadius();
    			ret = r;
    			break;
    		}
    	}
    	if (!found) throw new GameActionException(GameActionExceptionType.CANT_DO_THAT, "Get info on robot by id");
    	MapLocation top = new MapLocation(center.x, center.y - radius);
    	MapLocation right = new MapLocation(center.x + radius, center.y);
    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
    	MapLocation left = new MapLocation(center.x - radius, center.y);
    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
    	double badDistance = ri.type.sensorRadius;
    	if (distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance) throw new GameActionException(GameActionExceptionType.CANT_DO_THAT, "Get info on robot by id");
    	else return ret;
    }

    /**
     * Senses information about a particular bullet given its ID.
     *
     * @param id the ID of the bullet to query.
     * @return a BulletInfo object for the sensed bullet.
     * @throws GameActionException if the bullet cannot be sensed (for example,
     * if it doesn't exist or is out of sight range).
     *
     * @battlecode.doc.costlymethod
     */
    BulletInfo senseBullet(int id) throws GameActionException {
    	RobotMonitor.useBytecodes(25);
    	MapLocation center = new MapLocation(0, 0);
    	boolean found = false;
    	BulletInfo ret = null;
    	for (BulletInfo b : map.bullets) {
    		if (b.ID == id) {
    			found = true;
    			center = b.location;
    			break;
    		}
    	}
    	if (!found) throw new GameActionException(GameActionExceptionType.CANT_DO_THAT, "Get info on bullet by id");
    	double dist = Math.sqrt(Math.pow(center.x - ri.location.x, 2) + Math.pow(center.y - ri.location.y, 2));
    	if (dist > ri.type.bulletSightRadius) throw new GameActionException(GameActionExceptionType.CANT_DO_THAT, "Get info on bullet by id");
    	else return ret;
    }

    /**
     * Returns all robots within sense radius.
     *
     * @return array of RobotInfo objects, which contain information about all
     * the robots you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo[] senseNearbyRobots() {
    	LinkedList<RobotInfo> l = new LinkedList<>();
    	RobotMonitor.useBytecodes(100);
    	for (RobotInfo r : map.robots) {
    		MapLocation center = r.location;
    		float radius = r.getRadius();
	    	MapLocation top = new MapLocation(center.x, center.y - radius);
	    	MapLocation right = new MapLocation(center.x + radius, center.y);
	    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
	    	MapLocation left = new MapLocation(center.x - radius, center.y);
	    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
	    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
	    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
	    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
	    	double badDistance = ri.type.sensorRadius;
	    	if (!(distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance)) l.add(r);
    	}
    	RobotInfo[] ret = new RobotInfo[l.size()];
    	int index = 0;
    	for (RobotInfo r : l) {
    		ret[index++] = r;
    	}
    	return ret;
    }

    /**
     * Returns all robots that can be sensed within a certain radius of this
     * robot.
     *
     * @param radius return robots this distance away from the center of
     * this robot. If -1 is passed, all robots within sense radius are returned.
     * @return array of RobotInfo objects of all the robots you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo[] senseNearbyRobots(float rad) {
    	LinkedList<RobotInfo> l = new LinkedList<>();
    	if (rad < 0 || rad > ri.type.sensorRadius) rad = ri.type.sensorRadius;
    	RobotMonitor.useBytecodes(100);
    	for (RobotInfo r : map.robots) {
    		MapLocation center = r.location;
    		float radius = r.getRadius();
	    	MapLocation top = new MapLocation(center.x, center.y - radius);
	    	MapLocation right = new MapLocation(center.x + radius, center.y);
	    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
	    	MapLocation left = new MapLocation(center.x - radius, center.y);
	    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
	    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
	    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
	    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
	    	double badDistance = rad;
	    	if (!(distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance)) l.add(r);
    	}
    	RobotInfo[] ret = new RobotInfo[l.size()];
    	int index = 0;
    	for (RobotInfo r : l) {
    		ret[index++] = r;
    	}
    	return ret;
    }

    /**
     * Returns all robots of a given team that can be sensed within a certain
     * radius of this robot.
     *
     * @param radius return robots this distance away from the center of
     * this robot. If -1 is passed, all robots within sense radius are returned.
     * @param team filter game objects by the given team. If null is passed,
     * robots from any team are returned.
     * @return array of RobotInfo objects of all the robots you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo[] senseNearbyRobots(float rad, Team team) {
    	LinkedList<RobotInfo> l = new LinkedList<>();
    	if (rad < 0 || rad > ri.type.sensorRadius) rad = ri.type.sensorRadius;
    	RobotMonitor.useBytecodes(100);
    	for (RobotInfo r : map.robots) {
    		MapLocation center = r.location;
    		float radius = r.getRadius();
	    	MapLocation top = new MapLocation(center.x, center.y - radius);
	    	MapLocation right = new MapLocation(center.x + radius, center.y);
	    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
	    	MapLocation left = new MapLocation(center.x - radius, center.y);
	    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
	    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
	    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
	    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
	    	double badDistance = rad;
	    	if (!(distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance) && r.team.equals(team)) l.add(r);
    	}
    	RobotInfo[] ret = new RobotInfo[l.size()];
    	int index = 0;
    	for (RobotInfo r : l) {
    		ret[index++] = r;
    	}
    	return ret;
    }

    /**
     * Returns all robots of a given team that can be sensed within a certain
     * radius of a specified location.
     *
     * @param center center of the given search radius.
     * @param radius return robots this distance away from the given center
     * location. If -1 is passed, all robots within sense radius are returned.
     * @param team filter game objects by the given team. If null is passed,
     * objects from all teams are returned.
     * @return array of RobotInfo objects of the robots you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    RobotInfo[] senseNearbyRobots(MapLocation cen, float rad, Team team) {
    	LinkedList<RobotInfo> l = new LinkedList<>();
    	if (rad < 0 || rad > ri.type.sensorRadius) rad = ri.type.sensorRadius;
    	RobotMonitor.useBytecodes(100);
    	for (RobotInfo r : map.robots) {
    		MapLocation center = r.location;
    		float radius = r.getRadius();
	    	MapLocation top = new MapLocation(center.x, center.y - radius);
	    	MapLocation right = new MapLocation(center.x + radius, center.y);
	    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
	    	MapLocation left = new MapLocation(center.x - radius, center.y);
	    	double distTop = Math.sqrt(Math.pow(top.x - cen.x, 2) + Math.pow(top.y - cen.y, 2));
	    	double distRight = Math.sqrt(Math.pow(right.x - cen.x, 2) + Math.pow(right.y - cen.y, 2));
	    	double distLeft = Math.sqrt(Math.pow(left.x - cen.x, 2) + Math.pow(left.y - cen.y, 2));
	    	double distBottom = Math.sqrt(Math.pow(bottom.x - cen.x, 2) + Math.pow(bottom.y - cen.y, 2));
	    	double badDistance = rad;
	    	if (!(distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance) && r.team.equals(team)) l.add(r);
    	}
    	RobotInfo[] ret = new RobotInfo[l.size()];
    	int index = 0;
    	for (RobotInfo r : l) {
    		ret[index++] = r;
    	}
    	return ret;
    }

    /**
     * Returns all trees within sense radius.
     *
     * @return array of TreeInfo objects, which contain information about all
     * the trees you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    TreeInfo[] senseNearbyTrees() {
    	LinkedList<TreeInfo> l = new LinkedList<>();
    	RobotMonitor.useBytecodes(100);
    	for (TreeInfo t : map.trees) {
    		MapLocation center = t.location;
    		float radius = t.getRadius();
	    	MapLocation top = new MapLocation(center.x, center.y - radius);
	    	MapLocation right = new MapLocation(center.x + radius, center.y);
	    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
	    	MapLocation left = new MapLocation(center.x - radius, center.y);
	    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
	    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
	    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
	    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
	    	double badDistance = ri.type.sensorRadius;
	    	if (!(distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance)) l.add(t);
    	}
    	TreeInfo[] ret = new TreeInfo[l.size()];
    	int index = 0;
    	for (TreeInfo t : l) {
    		ret[index++] = t;
    	}
    	return ret;
    }

    /**
     * Returns all trees that can be sensed within a certain radius of this
     * robot.
     *
     * @param radius return trees this distance away from the center of
     * this robot. If -1 is passed, all trees within sense radius are returned.
     * @return array of TreeInfo objects of all the trees you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    TreeInfo[] senseNearbyTrees(float rad) {
    	LinkedList<TreeInfo> l = new LinkedList<>();
    	if (rad < 0 || rad > ri.type.sensorRadius) rad = ri.type.sensorRadius;
    	RobotMonitor.useBytecodes(100);
    	for (TreeInfo t : map.trees) {
    		MapLocation center = t.location;
    		float radius = t.getRadius();
	    	MapLocation top = new MapLocation(center.x, center.y - radius);
	    	MapLocation right = new MapLocation(center.x + radius, center.y);
	    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
	    	MapLocation left = new MapLocation(center.x - radius, center.y);
	    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
	    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
	    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
	    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
	    	double badDistance = rad;
	    	if (!(distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance)) l.add(t);
    	}
    	TreeInfo[] ret = new TreeInfo[l.size()];
    	int index = 0;
    	for (TreeInfo t : l) {
    		ret[index++] = t;
    	}
    	return ret;
    }

    /**
     * Returns all trees of a given team that can be sensed within a certain
     * radius of this robot.
     *
     * @param radius return trees this distance away from the center of
     * this robot. If -1 is passed, all trees within sense radius are returned.
     * @param team filter game objects by the given team. If null is passed,
     * robots from any team are returned.
     * @return array of TreeInfo objects of all the trees you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    TreeInfo[] senseNearbyTrees(float rad, Team team) {
    	LinkedList<TreeInfo> l = new LinkedList<>();
    	if (rad < 0 || rad > ri.type.sensorRadius) rad = ri.type.sensorRadius;
    	RobotMonitor.useBytecodes(100);
    	for (TreeInfo t : map.trees) {
    		MapLocation center = t.location;
    		float radius = t.getRadius();
	    	MapLocation top = new MapLocation(center.x, center.y - radius);
	    	MapLocation right = new MapLocation(center.x + radius, center.y);
	    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
	    	MapLocation left = new MapLocation(center.x - radius, center.y);
	    	double distTop = Math.sqrt(Math.pow(top.x - ri.location.x, 2) + Math.pow(top.y - ri.location.y, 2));
	    	double distRight = Math.sqrt(Math.pow(right.x - ri.location.x, 2) + Math.pow(right.y - ri.location.y, 2));
	    	double distLeft = Math.sqrt(Math.pow(left.x - ri.location.x, 2) + Math.pow(left.y - ri.location.y, 2));
	    	double distBottom = Math.sqrt(Math.pow(bottom.x - ri.location.x, 2) + Math.pow(bottom.y - ri.location.y, 2));
	    	double badDistance = rad;
	    	if (!(distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance) && t.team.equals(team)) l.add(t);
    	}
    	TreeInfo[] ret = new TreeInfo[l.size()];
    	int index = 0;
    	for (TreeInfo t : l) {
    		ret[index++] = t;
    	}
    	return ret;
    }

    /**
     * Returns all trees of a given team that can be sensed within a certain
     * radius of a specified location.
     *
     * @param center center of the given search radius.
     * @param radius return trees this distance away from given center
     * location. If -1 is passed, all trees within sense radius are returned.
     * @param team filter game objects by the given team. If null is passed,
     * objects from all teams are returned.
     * @return array of TreeInfo objects of the trees you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    TreeInfo[] senseNearbyTrees(MapLocation cen, float rad, Team team) {
    	LinkedList<TreeInfo> l = new LinkedList<>();
    	if (rad < 0 || rad > ri.type.sensorRadius) rad = ri.type.sensorRadius;
    	RobotMonitor.useBytecodes(100);
    	for (TreeInfo t : map.trees) {
    		MapLocation center = t.location;
    		float radius = t.getRadius();
	    	MapLocation top = new MapLocation(center.x, center.y - radius);
	    	MapLocation right = new MapLocation(center.x + radius, center.y);
	    	MapLocation bottom = new MapLocation(center.x, center.y + radius);
	    	MapLocation left = new MapLocation(center.x - radius, center.y);
	    	double distTop = Math.sqrt(Math.pow(top.x - cen.x, 2) + Math.pow(top.y - cen.y, 2));
	    	double distRight = Math.sqrt(Math.pow(right.x - cen.x, 2) + Math.pow(right.y - cen.y, 2));
	    	double distLeft = Math.sqrt(Math.pow(left.x - cen.x, 2) + Math.pow(left.y - cen.y, 2));
	    	double distBottom = Math.sqrt(Math.pow(bottom.x - cen.x, 2) + Math.pow(bottom.y - cen.y, 2));
	    	double badDistance = rad;
	    	if (!(distTop > badDistance && distRight > badDistance && distLeft > badDistance && distBottom > badDistance) && t.team.equals(team)) l.add(t);
    	}
    	TreeInfo[] ret = new TreeInfo[l.size()];
    	int index = 0;
    	for (TreeInfo t : l) {
    		ret[index++] = t;
    	}
    	return ret;
    }

    /**
     * Returns all bullets within bullet sense radius.
     *
     * @return array of BulletInfo objects, which contain information about all
     * the bullets you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    BulletInfo[] senseNearbyBullets() {
    	LinkedList<BulletInfo> l = new LinkedList<>();
    	RobotMonitor.useBytecodes(50);
    	for (BulletInfo b : map.bullets) {
    		double dist = Math.sqrt(Math.pow(b.location.x - ri.location.x, 2) + Math.pow(b.location.y - ri.location.y, 2));
        	if (!(dist > ri.type.bulletSightRadius)) l.add(b);
    	}
    	BulletInfo[] ret = new BulletInfo[l.size()];
    	int index = 0;
    	for (BulletInfo b : l) {
    		ret[index++] = b;
    	}
    	return ret;
    }

    /**
     * Returns all bullets that can be sensed within a certain radius of this
     * robot.
     *
     * @param radius return bullets this distance away from the center of
     * this robot. If -1 is passed, bullets from the whole map are returned.
     * @return array of BulletInfo objects of all the bullets you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    BulletInfo[] senseNearbyBullets(float rad) {
    	LinkedList<BulletInfo> l = new LinkedList<>();
    	if (rad < 0 || rad > ri.type.bulletSightRadius) rad = ri.type.bulletSightRadius;
    	RobotMonitor.useBytecodes(50);
    	for (BulletInfo b : map.bullets) {
    		double dist = Math.sqrt(Math.pow(b.location.x - ri.location.x, 2) + Math.pow(b.location.y - ri.location.y, 2));
        	if (!(dist > rad)) l.add(b);
    	}
    	BulletInfo[] ret = new BulletInfo[l.size()];
    	int index = 0;
    	for (BulletInfo b : l) {
    		ret[index++] = b;
    	}
    	return ret;
    }

    /**
     * Returns all bullets that can be sensed within a certain
     * radius of a specified location.
     *
     * @param center center of the given search radius.
     * @param radius return bullets this distance away from the given center
     * location. If -1 is passed, all bullets within bullet sense radius are returned..
     * @return array of TreeInfo objects of the bullets you sensed.
     *
     * @battlecode.doc.costlymethod
     */
    BulletInfo[] senseNearbyBullets(MapLocation cen, float rad) {
    	LinkedList<BulletInfo> l = new LinkedList<>();
    	if (rad < 0 || rad > ri.type.bulletSightRadius) rad = ri.type.bulletSightRadius;
    	RobotMonitor.useBytecodes(50);
    	for (BulletInfo b : map.bullets) {
    		double dist = Math.sqrt(Math.pow(b.location.x - cen.x, 2) + Math.pow(b.location.y - cen.y, 2));
        	if (!(dist > rad)) l.add(b);
    	}
    	BulletInfo[] ret = new BulletInfo[l.size()];
    	int index = 0;
    	for (BulletInfo b : l) {
    		ret[index++] = b;
    	}
    	return ret;
    }

    /**
     * Returns an array of all the locations of the robots that have
     * broadcasted in the last round (unconstrained by sensor range or distance)
     *
     * @return an array of all the locations of the robots that have
     * broadcasted in the last round.
     *
     * @battlecode.doc.costlymethod
     */
    MapLocation[] senseBroadcastingRobotLocations() {
    	RobotMonitor.useBytecodes(100);
    	MapLocation[] ret = new MapLocation[map.broadcasters.size()];
    	int index = 0;
    	for (RobotInfo r : map.broadcasters) {
    		ret[index++] = r.location;
    	}
    	return ret;
    }

    // ***********************************
    // ****** MOVEMENT METHODS ***********
    // ***********************************
    
    /**
     * Returns true if the robot has moved this turn.
     * 
     * @return true if the robot has moved this turn.
     *
     * @battlecode.doc.costlymethod
     */
    boolean hasMoved() {
    	RobotMonitor.useBytecodes(1);
    	return moves > 0;
    }
    
    /**
     * Returns true if the robot has attacked this turn.
     * 
     * @return true if the robot has attacked this turn.
     *
     * @battlecode.doc.costlymethod
     */
    boolean hasAttacked() {
    	RobotMonitor.useBytecodes(1);
    	return attacks > 0;
    }
    
    /**
     * Returns true if the robot's build cooldown has expired.
     * 
     * @return true if the robot's build cooldown has expired.
     *
     * @battlecode.doc.costlymethod
     */
    boolean isBuildReady() {
    	RobotMonitor.useBytecodes(1);
    	if (map.buildCooldown.containsKey(ri) && map.buildCooldown.get(ri) > 0) return false;
    	else return true;
    }

    /**
     * Tells whether this robot can move one stride in the given direction,
     * without taking into account if they have already moved. Takes into account only
     * the positions of trees, positions of other robots, and the edge of the
     * game map. Does not take into account whether this robot is currently
     * active.  Note that one stride is equivalent to StrideRadius.
     *
     * @param dir the direction to move in.
     * @return true if there is nothing preventing this robot from moving one
     * stride in the given direction; false otherwise (does not account for
     * core delay).
     *
     * @battlecode.doc.costlymethod
     */
    boolean canMove(Direction dir) {
    	RobotMonitor.useBytecodes(10);
    	float deltax = dir.getDeltaX(ri.type.strideRadius);
    	float deltay = dir.getDeltaY(ri.type.strideRadius);
    	if (ri.location.x + deltax < map.getOrigin().x || ri.location.x + deltax >= map.getOrigin().x + map.getWidth() || ri.location.y + deltay < map.getOrigin().y || ri.location.y + deltay >= map.getOrigin().y + map.getHeight()) return false;
    	float M = deltay / deltax;
		for (RobotInfo r : map.robots) {
			float R = r.type.bodyRadius;
			float B = ri.location.x - r.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) return false;
		}
		for (TreeInfo t : map.trees) {
			float R = t.radius;
			float B = ri.location.x - t.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) return false;
		}
		return true;
    }

    /**
     * Tells whether this robot can move distance in the given direction,
     * without taking into account if they have already moved. Takes into
     * account only the positions of trees, positions of other robots, and the
     * edge of the game map. Does not take into account whether this robot is
     * currently active. Note that one stride is equivalent to StrideRadius.
     *
     *
     * @param dir the direction to move in.
     * @param distance the distance of a move you wish to check. Must be
     * from 0 to RobotType.strideRadius (inclusive).
     * @return true if there is nothing preventing this robot from moving distance
     * in the given direction; false otherwise (does not account for
     * the robot having already moved that turn).
     *
     * @battlecode.doc.costlymethod
     */
    boolean canMove(Direction dir, float distance) {
    	RobotMonitor.useBytecodes(10);
    	float deltax = dir.getDeltaX(distance);
    	float deltay = dir.getDeltaY(distance);
    	if (ri.location.x + deltax < map.getOrigin().x || ri.location.x + deltax >= map.getOrigin().x + map.getWidth() || ri.location.y + deltay < map.getOrigin().y || ri.location.y + deltay >= map.getOrigin().y + map.getHeight()) return false;
    	float M = deltay / deltax;
		for (RobotInfo r : map.robots) {
			float R = r.type.bodyRadius;
			float B = ri.location.x - r.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) return false;
		}
		for (TreeInfo t : map.trees) {
			float R = t.radius;
			float B = ri.location.x - t.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) return false;
		}
		return true;
    }
    
    /**
     * Tells whether this robot can move to the target MapLocation. If the location
     * is outside the robot's StrideRadius, the location is rescaled to be at the
     * StrideRadius. Takes into account only the positions of rees, positions of
     * other robots, and the edge of the game map. Does not take into account whether
     * this robot is currently active.
     * 
     * @param center the MapLocation to move to.
     * @return true if there is nothing preventing this robot from moving to this
     * MapLocation (or in the direction of this MapLocation if it is too far);
     * false otherwise (does not account for the robot having already moved that turn).
     *
     * @battlecode.doc.costlymethod
     */
    boolean canMove(MapLocation center) {
    	RobotMonitor.useBytecodes(10);
    	float deltax = center.x - ri.location.x;
    	float deltay = center.y - ri.location.y;
    	if (Math.sqrt(Math.pow(deltax, 2) + Math.pow(deltay, 2)) > ri.type.strideRadius) {
    		Direction d = new Direction(deltax, deltay);
    		deltax = d.getDeltaX(ri.type.strideRadius);
    		deltay = d.getDeltaY(ri.type.strideRadius);
    	}
    	if (ri.location.x + deltax < map.getOrigin().x || ri.location.x + deltax >= map.getOrigin().x + map.getWidth() || ri.location.y + deltay < map.getOrigin().y || ri.location.y + deltay >= map.getOrigin().y + map.getHeight()) return false;
    	float M = deltay / deltax;
		for (RobotInfo r : map.robots) {
			float R = r.type.bodyRadius;
			float B = ri.location.x - r.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) return false;
		}
		for (TreeInfo t : map.trees) {
			float R = t.radius;
			float B = ri.location.x - t.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) return false;
		}
		return true;
    }
    
    /**
     * Moves one stride in the given direction. One stride is given by the robot's
     * stride radius (RobotType.strideRadius).
     *
     * @param dir the direction to move in.
     * @throws GameActionException if the robot cannot move one stride in this
     * direction, such as already moved that turn, the target location being
     * off the map, and the target destination being occupied with either
     * another robot or a tree.
     *
     * @battlecode.doc.costlymethod
     */
    void move(Direction dir) throws GameActionException {
    	float deltax = dir.getDeltaX(ri.type.strideRadius);
    	float deltay = dir.getDeltaY(ri.type.strideRadius);
    	if (ri.location.x + deltax < map.getOrigin().x || ri.location.x + deltax >= map.getOrigin().x + map.getWidth() || ri.location.y + deltay < map.getOrigin().y || ri.location.y + deltay >= map.getOrigin().y + map.getHeight()) throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving");
    	float M = deltay / deltax;
		for (RobotInfo r : map.robots) {
			float R = r.type.bodyRadius;
			float B = ri.location.x - r.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving");
		}
		for (TreeInfo t : map.trees) {
			float R = t.radius;
			float B = ri.location.x - t.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving");
		}
		if (moves > 0)  throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving");
		moves++;
		MapLocation newloc = new MapLocation(ri.getLocation().x + deltax, ri.getLocation().y + deltay);
		map.robots.remove(ri);
		ri = new RobotInfo(ri.ID, ri.team, ri.type, newloc, ri.health, ri.attackCount, ri.moveCount);
    }

    /**
     * Moves distance in the given direction.
     *
     * @param dir the direction to move in.
     * @param distance the distance to move in that direction.
     * @throws GameActionException if the robot cannot move distance in this
     * direction, such as already moved that turn, the target location being
     * off the map, and the target destination being occupied with either
     * another robot or a tree.
     *
     * @battlecode.doc.costlymethod
     */
    void move(Direction dir, float distance) throws GameActionException {
    	float deltax = dir.getDeltaX(distance);
    	float deltay = dir.getDeltaY(distance);
    	if (ri.location.x + deltax < map.getOrigin().x || ri.location.x + deltax >= map.getOrigin().x + map.getWidth() || ri.location.y + deltay < map.getOrigin().y || ri.location.y + deltay >= map.getOrigin().y + map.getHeight()) throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving distance");
    	float M = deltay / deltax;
		for (RobotInfo r : map.robots) {
			float R = r.type.bodyRadius;
			float B = ri.location.x - r.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving distance");
		}
		for (TreeInfo t : map.trees) {
			float R = t.radius;
			float B = ri.location.x - t.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving distance");
		}
		if (moves > 0)  throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving distance");
		moves++;
		MapLocation newloc = new MapLocation(ri.getLocation().x + deltax, ri.getLocation().y + deltay);
		map.robots.remove(ri);
		ri = new RobotInfo(ri.ID, ri.team, ri.type, newloc, ri.health, ri.attackCount, ri.moveCount);
    }
    
    /**
     * Moves to the target MapLocation. If the target location is outside the robot's
     * StrideRadius, it is rescaled to be one StrideRadius away.
     * 
     * @param center the MapLocation to move to (or toward)
     * @throws GameActionException if the robot can not move to the target MapLocation,
     * such as already having moved that turn, the target location being off the map,
     * or a target destination being occupied with either another robot or a tree.
     *
     * @battlecode.doc.costlymethod
     */
    void move(MapLocation center) throws GameActionException {
    	float deltax = center.x - ri.location.x;
    	float deltay = center.y - ri.location.y;
    	if (Math.sqrt(Math.pow(deltax, 2) + Math.pow(deltay, 2)) > ri.type.strideRadius) {
    		Direction d = new Direction(deltax, deltay);
    		deltax = d.getDeltaX(ri.type.strideRadius);
    		deltay = d.getDeltaY(ri.type.strideRadius);
    	}
    	if (ri.location.x + deltax < map.getOrigin().x || ri.location.x + deltax >= map.getOrigin().x + map.getWidth() || ri.location.y + deltay < map.getOrigin().y || ri.location.y + deltay >= map.getOrigin().y + map.getHeight()) throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving location");
    	float M = deltay / deltax;
		for (RobotInfo r : map.robots) {
			float R = r.type.bodyRadius;
			float B = ri.location.x - r.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving location");
		}
		for (TreeInfo t : map.trees) {
			float R = t.radius;
			float B = ri.location.x - t.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving location");
		}
		if (moves > 0)  throw new GameActionException(GameActionExceptionType.CANT_MOVE_THERE, "Moving location");
		moves++;
		MapLocation newloc = new MapLocation(ri.getLocation().x + deltax, ri.getLocation().y + deltay);
		map.robots.remove(ri);
		ri = new RobotInfo(ri.ID, ri.team, ri.type, newloc, ri.health, ri.attackCount, ri.moveCount);
    }

    // ***********************************
    // ****** ATTACK METHODS *************
    // ***********************************

    /**
     * Returns true if a robot is able to strike this turn. This takes into accout
     * the robot's type, and if the robot has attacked this turn.
     *
     * @return True if the robot is able to strike this turn.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canStrike() {
    	RobotMonitor.useBytecodes(5);
    	if (ri.type.equals(RobotType.LUMBERJACK) && attacks == 0) return true;
    	else return false;
    }

    /**
     * Strikes and deals damage to all other robots and trees within one stride of
     * this robot. Note that only Lumberjacks can perform this function.
     *
     * @throws GameActionException if the robot is not of type LUMBERJACK or
     * cannot attack due to having already attacked that turn.
     *
     * @battlecode.doc.costlymethod
     */
    void strike() throws GameActionException {
    	if (ri.type.equals(RobotType.LUMBERJACK) && attacks == 0) {
    		RobotMonitor.useBytecodes(-100);
    		RobotInfo[] ris = senseNearbyRobots(ri.type.strideRadius);
    		for (RobotInfo r : ris) {
    			map.robots.remove(r);
    			if (r.health - RobotType.LUMBERJACK.attackPower > 0) {
    				map.robots.add(new RobotInfo(r.ID, r.team, r.type, r.location, r.health - RobotType.LUMBERJACK.attackPower, r.attackCount, r.moveCount));
    			}
    		}
    	}
    	else throw new GameActionException(GameActionExceptionType.CANT_DO_THAT, "Striking");
    }

    /**
     * Tells whether there is enough bullets in your bullet supply to
     * fire a single shot and if the robot is of an appropriate type and
     * if the robot has not attacked in the current turn.
     *
     * @return true if there are enough bullets in the bullet supply,
     * this robot is of an appropriate type, and the robot hasn't attacked this turn.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canFireSingleShot() {
    	RobotMonitor.useBytecodes(5);
    	if (map.getBullets(ri.team) >= GameConstants.SINGLE_SHOT_COST && ri.type != RobotType.ARCHON && ri.type != RobotType.GARDENER && ri.type != RobotType.LUMBERJACK && attacks == 0) return true;
    	else return false;
    }

    /**
     * Tells whether there is enough bullets in your bullet supply to
     * fire a triad shot and if the robot is of an appropriate type and
     * if the robot has not attacked in the current turn.
     *
     * @return true if there are enough bullets in the bullet supply,
     * this robot is of an appropriate type, and the robot hasn't attacked this turn.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canFireTriadShot() {
    	RobotMonitor.useBytecodes(5);
    	if (map.getBullets(ri.team) >= GameConstants.TRIAD_SHOT_COST && ri.type != RobotType.ARCHON && ri.type != RobotType.GARDENER && ri.type != RobotType.LUMBERJACK && attacks == 0) return true;
    	else return false;
    }

    /**
     * Tells whether there is enough bullets in your bullet supply to
     * fire a pentad shot and if the robot is of an appropriate type and
     * if the robot has not attacked in the current turn.
     *
     * @return true if there are enough bullets in the bullet supply,
     * this robot is of an appropriate type, and the robot hasn't attacked this turn.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canFirePentadShot() {
    	RobotMonitor.useBytecodes(5);
    	if (map.getBullets(ri.team) >= GameConstants.PENTAD_SHOT_COST && ri.type != RobotType.ARCHON && ri.type != RobotType.GARDENER && ri.type != RobotType.LUMBERJACK && attacks == 0) return true;
    	else return false;
    }

    /**
     * Fires a single bullet in the direction dir at the cost of
     * GameConstants.SINGLE_SHOT_COST from your team's bullet supply. The speed
     * and damage of the bullet is determined from the type of this robot.
     *
     * @param dir the direction you wish to fire the bullet.
     * @throws GameActionException if this robot is not of a type that can
     * fire single shots (ARCHON, GARDENER, etc.), cannot attack due to having
     * already attacked, or for having insufficient bullets in the bullet supply.
     *
     * @battlecode.doc.costlymethod
     */
    void fireSingleShot(Direction dir) throws GameActionException {
    	if (map.getBullets(ri.team) >= GameConstants.SINGLE_SHOT_COST && ri.type != RobotType.ARCHON && ri.type != RobotType.GARDENER && ri.type != RobotType.LUMBERJACK && attacks == 0) {
    		map.changeBullets(ri.team, -GameConstants.SINGLE_SHOT_COST);
    		map.createBullet(ri.location, dir, ri.type.bulletSpeed, ri.type.attackPower, ri);
    	}
    	else throw new GameActionException(GameActionExceptionType.CANT_DO_THAT, "Fire single shot");
    }

    /**
     * Fires a three bullets with the center bullet in the direction dir and
     * with a spread of GameConstants.TRIAD_SPREAD_DEGREES degrees for the other
     * bullets.  This function costs GameConstants.TRIAD_SHOT_COST bullets from
     * your team's supply. The speed and damage of the bullets is determined
     * from the type of this robot.
     *
     * @param dir the direction you wish to fire the center bullet.
     * @throws GameActionException if this robot is not of a type that can
     * fire triad shots (ARCHON, GARDENER, etc.), cannot attack due to having
     * already attacked, or for having insufficient bullets in the bullet supply.
     *
     * @battlecode.doc.costlymethod
     */
    void fireTriadShot(Direction dir) throws GameActionException {
    	if (map.getBullets(ri.team) >= GameConstants.TRIAD_SHOT_COST && ri.type != RobotType.ARCHON && ri.type != RobotType.GARDENER && ri.type != RobotType.LUMBERJACK && attacks == 0) {
    		map.changeBullets(ri.team, -GameConstants.TRIAD_SHOT_COST);
    		map.createBullet(ri.location, dir, ri.type.bulletSpeed, ri.type.attackPower, ri);
    		map.createBullet(ri.location, dir.rotateLeftDegrees(GameConstants.TRIAD_SPREAD_DEGREES), ri.type.bulletSpeed, ri.type.attackPower, ri);
    		map.createBullet(ri.location, dir.rotateRightDegrees(GameConstants.TRIAD_SPREAD_DEGREES), ri.type.bulletSpeed, ri.type.attackPower, ri);
    	}
    	else throw new GameActionException(GameActionExceptionType.CANT_DO_THAT, "Fire triad shot");
    }

    /**
     * Fires a five bullets with the center bullet in the direction dir and
     * with a spread of GameConstants.PENTAD_SPREAD_DEGREES degrees for the other
     * bullets.  This function costs GameConstants.PENTAD_SHOT_COST bullets from
     * your team's supply. The speed and damage of the bullets is determined
     * from the type of this robot.
     *
     * @param dir the direction you wish to fire the center bullet.
     * @throws GameActionException if this robot is not of a type that can
     * fire pentad shots (ARCHON, GARDENER, etc.), cannot attack due to having
     * already attacked, or for having insufficient bullets in the bullet supply.
     *
     * @battlecode.doc.costlymethod
     */
    void firePentadShot(Direction dir) throws GameActionException {
    	if (map.getBullets(ri.team) >= GameConstants.PENTAD_SHOT_COST && ri.type != RobotType.ARCHON && ri.type != RobotType.GARDENER && ri.type != RobotType.LUMBERJACK && attacks == 0) {
    		map.changeBullets(ri.team, -GameConstants.PENTAD_SHOT_COST);
    		map.createBullet(ri.location, dir, ri.type.bulletSpeed, ri.type.attackPower, ri);
    		map.createBullet(ri.location, dir.rotateLeftDegrees(GameConstants.PENTAD_SPREAD_DEGREES), ri.type.bulletSpeed, ri.type.attackPower, ri);
    		map.createBullet(ri.location, dir.rotateRightDegrees(GameConstants.PENTAD_SPREAD_DEGREES), ri.type.bulletSpeed, ri.type.attackPower, ri);
    		map.createBullet(ri.location, dir.rotateRightDegrees(GameConstants.PENTAD_SPREAD_DEGREES * 2), ri.type.bulletSpeed, ri.type.attackPower, ri);
    		map.createBullet(ri.location, dir.rotateRightDegrees(GameConstants.PENTAD_SPREAD_DEGREES * 2), ri.type.bulletSpeed, ri.type.attackPower, ri);
    	}
    	else throw new GameActionException(GameActionExceptionType.CANT_DO_THAT, "Fire pentad shot");
    }

    // ***********************************
    // ****** TREE METHODS ***************
    // ***********************************

    /**
     * Tells whether the robot can chop, has not already attacked this turn,
     * and will hit an in-range tree at the given location.
     *
     * @param loc The location of the tree to test
     * @return True if the tree can be chopped this turn
     *
     * @battlecode.doc.costlymethod
     */
    boolean canChop(MapLocation loc) {
    	RobotMonitor.useBytecodes(5);
    	boolean tree = false;
    	for (TreeInfo t : map.trees) {
			if (Math.sqrt(Math.pow(t.location.x - loc.x, 2) + Math.pow(t.location.y - loc.y, 2)) <= t.getRadius()) tree = true;
		}
    	if (ri.type.equals(RobotType.LUMBERJACK) && attacks == 0 && tree && Math.sqrt(Math.pow(loc.x - ri.location.x, 2) + Math.pow(loc.y - ri.location.y, 2)) <= ri.type.sensorRadius) return true;
    	else return false;
    }

    /**
     * Tells whether the robot can chop, has not already attacked this turn,
     * and "id" corresponds to an in-range tree.
     *
     * @param id The id of the tree to chop
     * @return True of the tree can be chopped by this robot
     *
     * @battlecode.doc.costlymethod
     */
    boolean canChop(int id) {
    	TreeInfo tree = null;
    	for (TreeInfo t : map.trees) {
    		if (t.ID == id) tree = t;
    	}
    	if (tree == null) return false;
    	else return canChop(tree.location);
    }

    /**
     * Chops the target tree at location loc. This action counts as an attack.
     *
     * @param loc the location of the tree you wish to chop, does not
     * have to be the center of the tree
     * @throws GameActionException if the given location does not contain
     * a tree, if the tree (not location) is not within one stride of this
     * robot, or cannot perform action due to having already moved.
     *
     * @battlecode.doc.costlymethod
     */
    void chop(MapLocation loc) throws GameActionException {
    	
    }

    /**
     * Chops the target tree at location loc. This action counts as an attack.
     *
     * @param id the id of the tree you wish to chop.
     * @throws GameActionException if there isn't a tree with the given id,
     * if the tree (not location) is not within one stride of this robot,
     * or cannot perform action due to having already moved.
     *
     * @battlecode.doc.costlymethod
     */
    void chop(int id) throws GameActionException;

    /**
     * Tells if this robot can shake the tree at the given location. Checks robot
     * stride radius, if a tree exists, and if the robot hasn't shaken this turn.
     *
     * @param loc The location of a tree to shake.
     * @return true if this tree can be shaken by this robot this turn.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canShake(MapLocation loc);

    /**
     * Tells if a robot can shake a tree with this id. Checks robot stride radius,
     * if a tree exists, and if the robot hasn't shaken this turn.
     *
     * @param id The ID of a tree to shake.
     * @return true if this tree can be shaken by this robot this turn.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canShake(int id);

    /**
     * Shakes the target tree at location loc for all the bullets held within
     * the tree; these bullets will be added to your team's bullet supply.
     * Robots can only shake once per turn.
     *
     * @param loc the location of the tree you wish to shake, does not
     * have to be the center of the tree
     * @throws GameActionException if the given location does not contain
     * a tree, if the tree (not location) is not within one stride of this
     * robot, or if this robot has already shook a tree this turn
     *
     * @battlecode.doc.costlymethod
     */
    void shake(MapLocation loc) throws GameActionException;

    /**
     * Shakes the target tree at location loc for all the bullets held within
     * the tree; these bullets will be added to your team's bullet supply.
     * Robots can only shake once per turn.
     *
     * @param id the id of the tree you wish to shake.
     * @throws GameActionException if there isn't a tree with the given id,
     * if the tree (not location) is not within one stride of this robot,
     * or if this robot has already shook a tree this turn
     *
     * @battlecode.doc.costlymethod
     */
    void shake(int id) throws GameActionException;

    /**
     * Determines whether the robot can water a tree. Takes into accout the
     * robot's type, if it's already watered this turn, and if a valid
     * tree at this location exists within range.
     *
     * @param loc The location of a tree to check.
     * @return true if this robot can water a tree, false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canWater(MapLocation loc);

    /**
     * Determines whether the robot can water a tree. Takes into accout the
     * robot's type, if it's already watered this turn, and if a valid
     * tree with this id exists within range.
     *
     * @param id The id of a tree to check.
     * @return true if this robot can water a tree, false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canWater(int id);

    /**
     * Waters the target tree at location loc, healing
     * GameConstants.WATER_HEALTH_REGEN_RATE health to the tree.
     * Robots can only water once per turn and only with robots
     * of type GARDENER.
     *
     * @param loc the location of the tree you wish to water, does not
     * have to be the center of the tree
     * @throws GameActionException if the given location does not contain
     * a tree, if the tree (not location) is not within one stride of this
     * robot, or this robot is not of type GARDENER
     *
     * @battlecode.doc.costlymethod
     */
    void water(MapLocation loc) throws GameActionException;

    /**
     * Waters the target tree at location loc, healing
     * GameConstants.WATER_HEALTH_REGEN_RATE health to the tree.
     * Robots can only water once per turn and only with robots
     * of type GARDENER.
     *
     * @param id the id of the tree you wish to water.
     * @throws GameActionException if there isn't a tree with the given id,
     * if the tree (not location) is not within one stride of this robot,
     * or this robot is not of type GARDENER
     *
     * @battlecode.doc.costlymethod
     */
    void water(int id) throws GameActionException;

    /**
     * Determines whether or not this robot can water a tree, taking into
     * account how many times this robot has watered this turn and this
     * robot's type
     *
     * @return true if this robot can water a tree, false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canWater();

    /**
     * Determines whether or not this robot can shake a tree, taking into
     * account how many times this robot has shook this turn.
     *
     * @return true if this robot can shake a tree, false otherwise.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canShake();

    /**
     * Determines whether or not there is a tree at location loc and, if so,
     * if the tree is within one stride of this robot and can therefore be
     * interacted with through chop(), shake(), or water().
     *
     * @param loc the location you wish to test
     * @return true if there is a tree located at loc and if said tree is
     * within one stride of this robot
     *
     * @battlecode.doc.costlymethod
     */
    boolean canInteractWithTree(MapLocation loc);

    /**
     * Determines whether or not there is a tree with the given id and, if so,
     * if the tree is within one stride of this robot and can therefore be
     * interacted with through chop(), shake(), or water().
     *
     * @param id the id of the tree you wish to test
     * @return true if there is a tree with the given id and if siad tree is
     * within a stride of this robot
     *
     * @battlecode.doc.costlymethod
     */
    boolean canInteractWithTree(int id);

    // ***********************************
    // ****** SIGNALING METHODS **********
    // ***********************************

    /**
     * Broadcasts a message to the team-shared array at index channel.
     * The data is not written until the end of the robot's turn.
     *
     * @param channel - the index to write to, from 0 to <code>BROADCAST_MAX_CHANNELS</code>
     * @param data - one int's worth of data to write
     * @throws GameActionException if the channel is invalid
     *
     * @battlecode.doc.costlymethod
     */
    void broadcast(int channel, int data) throws GameActionException;

    /**
     * Retrieves the message stored in the team-shared array at index channel.
     *
     * @param channel the index to query, from 0 to <code>BROADCAST_MAX_CHANNELS</code>
     * @return data currently stored on the channel
     * @throws GameActionException  if the channel is invalid
     *
     * @battlecode.doc.costlymethod
     */
    int readBroadcast(int channel) throws GameActionException;

    // ***********************************
    // ****** BUILDING/SPAWNING **********
    // ***********************************

    /**
     * Returns whether you have the bullets and dependencies to build the given
     * robot, and this robot is a valid builder for the target robot.
     *
     * @param type the type to build.
     * @return whether the requirements to build are met.
     *
     * @battlecode.doc.costlymethod
     */
    boolean hasRobotBuildRequirements(RobotType type);

    /**
     * Returns whether you have the bullets and dependencies to build a
     * bullet tree, and this robot is a valid builder for a bullet tree.
     *
     * @return whether the requirements to build are met.
     *
     * @battlecode.doc.costlymethod
     */
    boolean hasTreeBuildRequirements();

    /**
     * Returns whether the robot can build a robot of the given type in the
     * given direction. Checks dependencies, cooldown turns remaining,
     * bullet costs, whether the robot can build, and that the given direction is
     * not blocked.
     *
     * @param dir the direction to build in.
     * @param type the robot type to build.
     * @return whether it is possible to build a robot of the given type in the
     * given direction.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canBuildRobot(RobotType type, Direction dir);

    /**
     * Plants/Builds a robot of the given type in the given direction.
     *
     * @param dir the direction to spawn the unit.
     * @param type the type of robot to build
     * @throws GameActionException if the build is bad: if you don't have enough
     * bullets, if you have coreDelay, if the direction is not a good build
     * direction, or if you are not of type GARDENER.
     *
     * @battlecode.doc.costlymethod
     */
    void buildRobot(RobotType type, Direction dir) throws GameActionException;

    /**
     * Returns whether the robot can build a bullet tree in the given direction.
     * Checks dependencies, cooldown turns remaining, bullet costs,
     * whether the robot can build, and that the given direction is
     * not blocked.
     *
     * @param dir the direction to build in.
     * @return whether it is possible to build a bullet tree in the
     * given direction.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canPlantTree(Direction dir);

    /**
     * Plants a bullet tree in the given direction. This is a core action.
     *
     * @param dir the direction to plant the bullet tree.
     * @throws GameActionException if the build is bad: if you don't have enough
     * bullets, if you have coreDelay, if the direction is not a good build
     * direction, or if you are not of type GARDENER.
     *
     * @battlecode.doc.costlymethod
     */
    void plantTree(Direction dir) throws  GameActionException;

    /**
     * Returns whether the robot can hire a gardener in the given direction.
     * Checks dependencies, cooldown turns remaining, bullet costs,
     * whether the robot can build, and that the given direction is
     * not blocked.
     * 
     * @param dir the direction to build in.
     * @return whether it is possible to hire a gardener in the given direction.
     *
     * @battlecode.doc.costlymethod
     */
    boolean canHireGardener(Direction dir);
    
    /**
     * Hires a Gardener in the given direction. This is a core action.
     *
     * @param dir the direction to spawn the GARDENER unit.
     * @throws GameActionException if the build is bad: if you don't have enough
     * bullets, if you have coreDelay, if the direction is not a good build
     * direction, or if you are not of type ARCHON.
     *
     * @battlecode.doc.costlymethod
     */
    void hireGardener(Direction dir) throws GameActionException;

    // ***********************************
    // ****** OTHER ACTION METHODS *******
    // ***********************************

    /**
     * Donates the given amount of bullets to the reforestation fund in
     * exchange for one victory point per ten bullets donated.  Note there
     * are no fractions of victory points, meaning, for example, donating
     * 11 bullets will only result in 1 victory point, not 1.1 victory points.
     *
     * @param bullets the amount of bullets you wish to donate
     * @throws GameActionException if you have less bullets in your bullet
     * supply than the amount of bullet you wish to donate.
     *
     * @battlecode.doc.costlymethod
     */
    void donate(float bullets) throws GameActionException;

    /**
     * Determines whether or not there is a robot at location loc and, if so,
     * if the robot is within one stride of this robot.
     *
     * @param loc the location you wish to test
     * @return true if there is a robot located at loc and if said robot is
     * within one stride of this robot
     *
     * @battlecode.doc.costlymethod
     */
    boolean canInteractWithRobot(MapLocation loc);

    /**
     * Determines whether or not there is a robot with the given id and, if so,
     * if the robot is within one stride of this robot.
     *
     * @param id the id of the robot you wish to test
     * @return true if there is a robot with the given id and if siad robot is
     * within a stride of this robot
     *
     * @battlecode.doc.costlymethod
     */
    boolean canInteractWithRobot(int id);

    /**
     * Kills your robot and ends the current round. Never fails.
     *
     * @battlecode.doc.costlymethod
     */
    void disintegrate();

    /**
     * Causes your team to lose the game. It's like typing "gg."
     *
     * @battlecode.doc.costlymethod
     */
    void resign();

    // ***********************************
    // **** INDICATOR STRING METHODS *****
    // ***********************************

    /**
     * Draw a dot on the game map for debugging purposes.
     *
     * @param loc the location to draw the dot.
     * @param red the red component of the dot's color.
     * @param green the green component of the dot's color.
     * @param blue the blue component of the dot's color.
     * @throws GameActionException if loc is not a valid location on the map
     *
     * @battlecode.doc.costlymethod
     */
    void setIndicatorDot(MapLocation loc, int red, int green, int blue) throws GameActionException;

    /**
     * Draw a line on the game map for debugging purposes.
     *
     * @param startLoc the location to draw the line from.
     * @param endLoc the location to draw the line to.
     * @param red the red component of the line's color.
     * @param green the green component of the line's color.
     * @param blue the blue component of the line's color.
     * @throws GameActionException if startLoc or endLoc is not a valid location on the map
     *
     * @battlecode.doc.costlymethod
     */
    void setIndicatorLine(MapLocation startLoc, MapLocation endLoc, int red, int green, int blue) throws GameActionException;

    // ***********************************
    // ******** TEAM MEMORY **************
    // ***********************************

    /**
     * Sets the team's "memory", which is saved for the next game in the match.
     * The memory is an array of {@link GameConstants#TEAM_MEMORY_LENGTH} longs.
     * If this method is called more than once with the same index in the same
     * game, the last call is what is saved for the next game.
     *
     * @param index the index of the array to set.
     * @param value the data that the team should remember for the next game.
     * @throws java.lang.ArrayIndexOutOfBoundsException if {@code index} is less
     * than zero or greater than or equal to
     * {@link GameConstants#TEAM_MEMORY_LENGTH}.
     * @see #getTeamMemory
     * @see #setTeamMemory(int, long, long)
     *
     * @battlecode.doc.costlymethod
     */
    void setTeamMemory(int index, long value);

    /**
     * Sets this team's "memory". This function allows for finer control than
     * {@link #setTeamMemory(int, long)} provides. For example, if
     * {@code mask == 0xFF} then only the eight least significant bits of the
     * memory will be set.
     *
     * @param index the index of the array to set.
     * @param value the data that the team should remember for the next game.
     * @param mask indicates which bits should be set.
     * @throws java.lang.ArrayIndexOutOfBoundsException if {@code index} is less
     * than zero or greater than or equal to
     * {@link GameConstants#TEAM_MEMORY_LENGTH}.
     * @see #getTeamMemory
     * @see #setTeamMemory(int, long)
     *
     * @battlecode.doc.costlymethod
     */
    void setTeamMemory(int index, long value, long mask);

    /**
     * Returns the team memory from the last game of the match. The return value
     * is an array of length {@link GameConstants#TEAM_MEMORY_LENGTH}. If
     * setTeamMemory was not called in the last game, or there was no last game,
     * the corresponding long defaults to 0.
     *
     * @return the team memory from the the last game of the match.
     * @see #setTeamMemory(int, long)
     * @see #setTeamMemory(int, long, long)
     *
     * @battlecode.doc.costlymethod
     */
    long[] getTeamMemory();

    // ***********************************
    // ******** DEBUG METHODS ************
    // ***********************************

    /**
     * Gets this robot's 'control bits' for debugging purposes. These bits can
     * be set manually by the user, so a robot can respond to them. To set these
     * bits, you must run the client in lockstep mode and right click the
     * units.
     *
     * @return this robot's control bits
     *
     * @battlecode.doc.costlymethod
     */
    long getControlBits();

}

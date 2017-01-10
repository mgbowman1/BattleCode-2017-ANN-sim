package battlecode.common;

import java.util.HashMap;
import java.util.LinkedList;

public class Map {
	
	private final int height, width, rounds;
	private final MapLocation origin;
	private final MapLocation[] teamAArchons;
	private final MapLocation[] teamBArchons;
	final LinkedList<TreeInfo> trees;
	final LinkedList<RobotInfo> robots;
	final LinkedList<BulletInfo> bullets = new LinkedList<>();
	final LinkedList<RobotInfo> broadcasters = new LinkedList<>();
	final HashMap<RobotInfo, Integer> buildCooldown = new HashMap<>();
	private int roundsPassed, teamAVictoryPoints, teamBVictoryPoints;
	private double teamABullets, teamBBullets;
	
	public Map(int height, int width, int rounds, MapLocation origin, LinkedList<TreeInfo> trees, LinkedList<RobotInfo> robots) {
		this.height = height;
		this.width = width;
		this.rounds = rounds;
		this.origin = origin;
		this.trees = trees;
		this.robots = robots;
		this.roundsPassed = 0;
		this.teamABullets = 300;
		this.teamBBullets = 300;
		this.teamAVictoryPoints = 0;
		this.teamBVictoryPoints = 0;
		int check = robots.size();
		int indexA = 0;
		int indexB = 0;
		teamAArchons = new MapLocation[check / 2];
		teamBArchons = new MapLocation[check / 2];
		for (RobotInfo r : robots) {
			if (r.type.equals(RobotType.ARCHON)) {
				if (r.team.equals(Team.A)) teamAArchons[indexA++] = new MapLocation(r.getLocation().x, r.getLocation().y);
				else teamBArchons[indexB++] = new MapLocation(r.getLocation().x, r.getLocation().y);
			}
		}
	}
	
	public void createBullet(MapLocation loc, Direction dir, float speed, float damage, RobotInfo maker) {
		int id = (int)(Math.random() * 32000 + 1);
		boolean taken = false;
		for (BulletInfo b : bullets) {
			if (b.ID == id) taken = true;
		}
		while (taken) {
			id = (int)(Math.random() * 32000 + 1);
			taken = false;
			for (BulletInfo b : bullets) {
				if (b.ID == id) taken = true;
			}
		}
		bullets.add(new BulletInfo(id, loc, dir, speed, damage));
		moveBullet(bullets.getLast(), maker);
	}
	
	public void changeBullets(Team t, float b) {
		if (t.equals(Team.A)) teamABullets += b;
		else teamBBullets += b;
	}
	
	public void addRound() {
		roundsPassed++;
		double teamAB = 2 - teamABullets / 100;
		double teamBB = 2 - teamBBullets / 100;
		for (TreeInfo t : trees) {
			if (t.team.equals(Team.A)) teamAB++;
			else if (t.team.equals(Team.B)) teamBB++;
		}
		teamABullets += teamAB;
		teamBBullets += teamBB;
		for (BulletInfo b : bullets) {
			moveBullet(b);
		}
		broadcasters.clear();
		for (RobotInfo r : buildCooldown.keySet()) {
			buildCooldown.put(r, buildCooldown.get(r) - 1);
		}
	}
	
	public void moveBullet(BulletInfo b) {
		moveBullet(b, null);
	}
	
	public void moveBullet(BulletInfo b, RobotInfo ignore) {
		float deltax = b.dir.getDeltaX(b.speed);
		float deltay = b.dir.getDeltaY(b.speed);
		float M = deltay / deltax;
		float min = deltax;
		BodyInfo body = null;
		for (RobotInfo r : robots) {
			if (!r.equals(ignore)) {
				float R = r.type.bodyRadius;
				float B = b.location.x - r.location.x;
				if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) {
					double disc = Math.sqrt(4 * ((Math.pow(M, 2) + 1) * Math.pow(R, 2) - Math.pow(B, 2)));
					disc = disc / (-2 * (Math.pow(M, 2) + 1));
					double x1 = (B * M) / ((Math.pow(M, 2) + 1)) + disc;
					double x2 = (B * M) / ((Math.pow(M, 2) + 1)) - disc;
					double minx = x1;
					if (Math.abs(x2 - b.location.x) < Math.abs(x1 - b.location.x)) minx = x2;
					if (minx <= min) {
						min = (float)minx;
						body = r;
					}
				}
			}
		}
		for (TreeInfo t : trees) {
			float R = t.radius;
			float B = b.location.x - t.location.x;
			if ((Math.pow(M, 2) + 1) * Math.pow(R, 2) >= Math.pow(B, 2)) {
				double disc = Math.sqrt(4 * ((Math.pow(M, 2) + 1) * Math.pow(R, 2) - Math.pow(B, 2)));
				disc = disc / (-2 * (Math.pow(M, 2) + 1));
				double x1 = (B * M) / ((Math.pow(M, 2) + 1)) + disc;
				double x2 = (B * M) / ((Math.pow(M, 2) + 1)) - disc;
				double minx = x1;
				if (Math.abs(x2 - b.location.x) < Math.abs(x1 - b.location.x)) minx = x2;
				if (minx <= min) {
					min = (float)minx;
					body = t;
				}
			}
		}
		bullets.remove(b);
		if (body != null) {
			if (body.isRobot()) {
				RobotInfo newri = (RobotInfo)body;
				robots.remove(newri);
				if (newri.health - b.damage > 0) {
					robots.add(new RobotInfo(newri.ID, newri.team, newri.type, newri.location, newri.health - b.damage, newri.attackCount, newri.moveCount));
				}
			} else {
				TreeInfo newti = (TreeInfo)body;
				trees.remove(newti);
				if (newti.health - b.damage > 0) {
					trees.add(new TreeInfo(newti.ID, newti.team, newti.location, newti.radius, newti.health - b.damage, newti.containedBullets, newti.containedRobot));
				}
			}
		} else {
			bullets.add(new BulletInfo(b.ID, new MapLocation(b.location.x + deltax, b.location.y + deltay), b.dir, b.speed, b.damage));
		}
	}
	
	public void donate(Team t, double d) {
		if (t.equals(Team.A)) teamAVictoryPoints += Math.floor(d / 10);
		else teamBVictoryPoints += Math.floor(d / 10);
	}
	
	public int getVictoryPoints(Team t) {
		if (t.equals(Team.A)) return teamAVictoryPoints;
		else return teamBVictoryPoints;
	}
	
	public double getBullets(Team t) {
		if (t.equals(Team.A)) return teamABullets;
		else return teamBBullets;
	}
	
	public MapLocation[] getStartingArchons(Team t) {
		if (t.equals(Team.A)) return teamAArchons;
		else return teamBArchons;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getRounds() {
		return rounds;
	}
	
	public MapLocation getOrigin() {
		return origin;
	}
	
	public int getRoundsPassed() {
		return roundsPassed;
	}
	
}

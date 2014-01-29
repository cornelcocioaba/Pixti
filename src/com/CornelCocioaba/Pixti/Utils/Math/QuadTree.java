package com.CornelCocioaba.Pixti.Utils.Math;

import java.util.ArrayList;
import java.util.List;

import com.CornelCocioaba.Pixti.GameObject.RectangleShape;

public class QuadTree {

	private int MAX_OBJECTS = 8;
	private int MAX_LEVELS = 5;

	private int level;
	private List<RectangleShape> objects;
	private RectangleShape bounds;
	private QuadTree[] nodes;

	public QuadTree(int pLevel, RectangleShape pBounds) {
		level = pLevel;
		bounds = pBounds;
		objects = new ArrayList<RectangleShape>(MAX_OBJECTS);
		nodes = new QuadTree[4];
	}

	public void clear() {
		objects.clear();

		for (int i = 0; i < nodes.length; i++) {
			if (nodes[i] != null) {
				nodes[i].clear();
				nodes[i] = null;
			}
		}
	}

	private void split() {
		int subWidth = (int) (bounds.getWidth() / 2);
		int subHeight = (int) (bounds.getHeight() / 2);
		int x = (int) bounds.getX();
		int y = (int) bounds.getY();

		nodes[0] = new QuadTree(level + 1, new RectangleShape(x + subWidth, y, subWidth, subHeight));
		nodes[1] = new QuadTree(level + 1, new RectangleShape(x, y, subWidth, subHeight));
		nodes[2] = new QuadTree(level + 1, new RectangleShape(x, y + subHeight, subWidth, subHeight));
		nodes[3] = new QuadTree(level + 1, new RectangleShape(x + subWidth, y + subHeight, subWidth, subHeight));
	}

	private int getIndex(RectangleShape pRect) {
		int index = -1;
		double verticalMidpoint = bounds.getX() + (bounds.getWidth() / 2);
		double horizontalMidpoint = bounds.getY() + (bounds.getHeight() / 2);

		boolean topQuadrant = (pRect.getY() < horizontalMidpoint && pRect.getY() + pRect.getHeight() < horizontalMidpoint);
		boolean bottomQuadrant = (pRect.getY() > horizontalMidpoint);

		if (pRect.getX() < verticalMidpoint && pRect.getX() + pRect.getWidth() < verticalMidpoint) {
			if (topQuadrant) {
				index = 1;
			} else if (bottomQuadrant) {
				index = 2;
			}
		} else if (pRect.getX() > verticalMidpoint) {
			if (topQuadrant) {
				index = 0;
			} else if (bottomQuadrant) {
				index = 3;
			}
		}

		return index;
	}

	public void insert(RectangleShape pRect) {
		if (nodes[0] != null) {
			int index = getIndex(pRect);

			if (index != -1) {
				nodes[index].insert(pRect);

				return;
			}
		}

		objects.add(pRect);

		if (objects.size() > MAX_OBJECTS && level < MAX_LEVELS) {
			if (nodes[0] == null) {
				split();
			}

			int i = 0;
			while (i < objects.size()) {
				int index = getIndex(objects.get(i));
				if (index != -1) {
					nodes[index].insert(objects.remove(i));
				} else {
					i++;
				}
			}
		}
	}

	public List<RectangleShape> retrieve(List<RectangleShape> returnObjects, RectangleShape pRect) {
		int index = getIndex(pRect);
		if (index != -1 && nodes[0] != null) {
			nodes[index].retrieve(returnObjects, pRect);
		}

		returnObjects.addAll(objects);

		return returnObjects;
	}
}

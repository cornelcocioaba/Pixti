package com.CornelCocioaba.Pixti.Examples.AndroidInvaders;

public class ProjectilePool extends GenericPool<Projectile> {

	private Projectile projectile;

	public ProjectilePool(Projectile prototype, int initialSize) {
		this.projectile = prototype;
		
		this.batchAllocatePoolItems(initialSize);
	}

	@Override
	protected Projectile onAllocatePoolItem() {
		return new Projectile(0, 0, projectile.getTextureRegion(), this);
	}

	@Override
	protected void onHandleRecycleItem(Projectile pItem) {
		pItem.removeSelf();
	}
}

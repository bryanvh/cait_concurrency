package edu.wustl.cait.concurrency;

public interface Effect {
	public void apply(Player actor, Player target);

	public default boolean isExpired() {
		return true;
	}

	public static Effect createBane(final Condition cf) {
		return (actor, target) -> target.addCondition(cf.create());
	}

	public static Effect createAura(final Condition cf) {
		return (actor, target) -> actor.addCondition(cf.create());
	}

	public static Effect createMelee(final int i) {
		return (actor, target) -> target.applyMelee(i);
	}

	public static Effect createMagic(final int i) {
		return (actor, target) -> target.applyMagic(i);
	}

	public static Effect createArmor(final int i) {
		return (actor, target) -> actor.applyArmor(i);
	}

	public static Effect createWard(final int i) {
		return (actor, target) -> actor.applyWard(i);
	}

	public static Effect createStun(final int i) {
		return (actor, target) -> target.applyStun(i);
	}

	public static Effect createHeal(final int i) {
		return (actor, target) -> actor.applyHeal(i);
	}
}

package edu.wustl.cait.concurrency;

public interface Effect {
	public void apply(Player actor, Player target);

	public default boolean isExpired() {
		return true;
	}

	public static Effect createBane(final Condition cf) {
		return new Effect() {
			@Override
			public void apply(Player actor, Player target) {
				// banes apply to the target
				target.addCondition(cf.create());
			}

			@Override
			public String toString() {
				return "bane:" + cf.toString();
			}
		};
	}

	public static Effect createAura(final Condition cf) {
		return new Effect() {
			@Override
			public void apply(Player actor, Player target) {
				// auras apply to the actor
				actor.addCondition(cf.create());
			}

			@Override
			public String toString() {
				return "aura:" + cf.toString();
			}
		};
	}

	public static Effect createMelee(final int i) {
		return new Effect() {
			@Override
			public void apply(Player actor, Player target) {
				target.applyMelee(i);
			}

			@Override
			public String toString() {
				return "melee/" + i;
			}
		};
	}

	public static Effect createMagic(final int i) {
		return new Effect() {
			@Override
			public void apply(Player actor, Player target) {
				target.applyMagic(i);
			}

			@Override
			public String toString() {
				return "magic/" + i;
			}
		};
	}

	public static Effect createArmor(final int i) {
		return new Effect() {
			@Override
			public void apply(Player actor, Player target) {
				actor.applyArmor(i);
			}

			@Override
			public String toString() {
				return "armor/" + i;
			}
		};
	}

	public static Effect createWard(final int i) {
		return new Effect() {
			@Override
			public void apply(Player actor, Player target) {
				actor.applyWard(i);
			}

			@Override
			public String toString() {
				return "ward/" + i;
			}
		};
	}

	public static Effect createStun(final int i) {
		return new Effect() {
			@Override
			public void apply(Player actor, Player target) {
				target.applyStun(i);
			}
		};
	}
}

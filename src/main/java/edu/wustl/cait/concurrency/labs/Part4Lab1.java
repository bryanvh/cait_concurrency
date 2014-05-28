package edu.wustl.cait.concurrency.labs;

public class Part4Lab1 {
	
	public static class Player {
		private int health = 0;
		private int armor = 0;
		private int ward = 0;
		private final int originalHealth; // immutable
		
		public Player(int health) {
			this.health = health;
			this.originalHealth = health;
		}
	}

	public static void main(String[] args) {
		// *** Identify the fields that form the object’s state
		// Start with a Player class that has multiple fields, like health and
		// armor and ward, that form the object's state.
		
		// *** Identify the invariants that constrain the object’s state
		// + health can never fall below zero
		// + health can never exceed original value
		// + armor can never fall below zero
		// + ward can never fall below zero
		
		// *** Define a policy for managing concurrent access
		// + make it clear which lock must be held
		// + make it clear which state is immutable (original health)
		
		// *** Define the postconditions that must be true
		// heal() -- precondition? cannot already be defeated
		// applyMelee()
		// applyMagic()
		// applyArmor()
		// applyWard()
		// isDefeated()
	}

}

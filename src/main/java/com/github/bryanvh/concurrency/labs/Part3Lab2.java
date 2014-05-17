package com.github.bryanvh.concurrency.labs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Safe Publication
 * 
 * Fix a class that unsafely publishes its state by:
 * 
 * 1. making member fields public or protected - easy fix
 * 
 * 2. letting "this" escape from constructor - use static factory
 * 
 * 3. returning references to internal state - defensive copy - is this really
 * about immutability or publication?
 * 
 * 4. passing reference to an alien method - immutability or publication?
 *
 */
public class Part3Lab2 {

	public static class Check {
	}

	public static class Reminder {
		public static void set(Checkbook c) {
		}
	}

	public static class Accountant {
		public static void review(List<Check> checks) {
			// alien method that might access checks from multiple threads
		}

		public static void review(Checkbook cb) {
			// if Checkbook class is thread-safe, this alien method cannot cause
			// any harm
		}
	}

	public static class Checkbook {
		// compromises encapsulation!
		public final List<Check> checks;

		public Checkbook() {
			checks = new ArrayList<Check>();

			// do not let this escape from constructor!
			Reminder.set(this);
		}

		// escapes by returned reference!
		public List<Check> getChecks() {
			// TODO: make sure the returned list cannot be modified
			// let's assume the Check class is already immutable
			return checks;
		}

		public void reconcile() {
			// Reference to internal state escapes by passing reference to an
			// alien method, which may not be safe if Accountant uses multiple
			// threads.
			// TODO: do this instead: Accountant.review(this);
			Accountant.review(checks);
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO: replace this with the use of a static factory method
		Checkbook cb = new Checkbook();
		// TODO: this code is better; doesn't publish "this" from constructor
		// Checkbook cb = Checkbook.create();

		// TODO: this code should not compile
		List<Check> unsafe = cb.checks;
		// TODO: this code should compile; returns an unmodifiable list
		// List<Check> safe = cb.getChecks()
	}
}

package com.amolik.misc;

public class Persoon {
	private final String naam;
	private final String geslacht;
	private final int leeftijd;

	public Persoon(String naam, String geslacht, int leeftijd) {
		this.naam = naam;
		this.geslacht = geslacht;
		this.leeftijd = leeftijd;
	}

	public String getNaam() {
		return naam;
	}

	public String getString() {
		return geslacht;
	}

	public int getLeeftijd() {
		return leeftijd;
	}

	public int leeftijdsverschil(final Persoon andere) {
		return leeftijd - andere.leeftijd;
	}

	@Override
	public String toString() {
		return String.format("%-9s - %s - %2d", naam, geslacht, leeftijd);
	}
}
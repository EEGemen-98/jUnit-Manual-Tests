/*
 * Copyright (c) 2009,  Sarah Heckman, Laurie Williams, Dright Ho
 * All Rights Reserved.
 *
 * Permission has been explicitly granted to the University of Minnesota
 * Software Engineering Center to use and distribute this source for
 * educational purposes, including delivering online education through
 * Coursera or other entities.
 *
 * No warranty is given regarding this software, including warranties as
 * to the correctness or completeness of this software, including
 * fitness for purpose.
 *
 *
 * Modifications
 * 20171114 - Ian De Silva - Updated to comply with JUnit 4 and to adhere to
 * 							 coding standards.  Added test documentation.
 */
package edu.ncsu.csc326.coffeemaker;

import jdk.nashorn.internal.objects.annotations.Property;
import org.junit.Before;
import org.junit.Test;

import edu.ncsu.csc326.coffeemaker.exceptions.InventoryException;
import edu.ncsu.csc326.coffeemaker.exceptions.RecipeException;

import static org.junit.Assert.*;

/**
 * Unit tests for CoffeeMaker class.
 *
 * @author Sarah Heckman
 */
public class CoffeeMakerTest {

	/**
	 * The object under test.
	 */
	private CoffeeMaker coffeeMaker;

	// Sample recipes to use in testing.
	private Recipe recipe1;
	private Recipe recipe2;
	private Recipe recipe3;
	private Recipe recipe4;
	private Recipe recipebad;
	private String currInventory;	// String version
	private Inventory currentInventory;

	/**
	 * Initializes some recipes to test with and the {@link CoffeeMaker}
	 * object we wish to test.
	 *
	 * @throws RecipeException  if there was an error parsing the ingredient
	 * 		amount when setting up the recipe.
	 */
	@Before
	public void setUp() throws RecipeException {
		coffeeMaker = new CoffeeMaker();

		//Set up for r1
		recipe1 = new Recipe();
		recipe1.setName("Coffee");
		recipe1.setAmtChocolate("0");
		recipe1.setAmtCoffee("3");
		recipe1.setAmtMilk("1");
		recipe1.setAmtSugar("1");
		recipe1.setPrice("50");

		//Set up for r2
		recipe2 = new Recipe();
		recipe2.setName("Mocha");
		recipe2.setAmtChocolate("20");
		recipe2.setAmtCoffee("3");
		recipe2.setAmtMilk("1");
		recipe2.setAmtSugar("1");
		recipe2.setPrice("75");

		//Set up for r3
		recipe3 = new Recipe();
		recipe3.setName("Latte");
		recipe3.setAmtChocolate("0");
		recipe3.setAmtCoffee("3");
		recipe3.setAmtMilk("3");
		recipe3.setAmtSugar("1");
		recipe3.setPrice("100");

		//Set up for r4
		recipe4 = new Recipe();
		recipe4.setName("Hot Chocolate");
		recipe4.setAmtChocolate("4");
		recipe4.setAmtCoffee("0");
		recipe4.setAmtMilk("1");
		recipe4.setAmtSugar("1");
		recipe4.setPrice("65");

		currInventory = coffeeMaker.checkInventory();


	}


	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with well-formed quantities
	 * Then we do not get an exception trying to read the inventory quantities.
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test
	public void testAddInventory() throws InventoryException {
		coffeeMaker.addInventory("4","7","0","9");
	}

	/**
	 * Given a coffee maker with the default inventory
	 * When we add inventory with malformed quantities (i.e., a negative
	 * quantity and a non-numeric string)
	 * Then we get an inventory exception
	 *
	 * @throws InventoryException  if there was an error parsing the quanity
	 * 		to a positive integer.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInventoryException() throws InventoryException {
		coffeeMaker.addInventory("4", "-1", "asdf", "3");
	}

	/**
	 * Given a coffee maker with one valid recipe
	 * When we make coffee, selecting the valid recipe and paying more than
	 * 		the coffee costs
	 * Then we get the correct change back.
	 */
	@Test
	public void testMakeCoffee() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(25, coffeeMaker.makeCoffee(0, 75));
	}

	/**
	 * -----------------------------------------------------------------------------------------------------
	 * The following tests below are written by Egemen Bozkus
	 * These were written in regards to the Use Cases stated on pages 2 and 3 of "CoffeeMaker Example pdf"
	 * -----------------------------------------------------------------------------------------------------
	 */

	/**
	 * Given a valid recipe, attempt to buy it with insufficient funds
	 * Program should return the amount paid.
	 */
	@Test
	public void testMakeCoffeeBadPrice() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(1, coffeeMaker.makeCoffee(0,1));
	}

	/**
	 * Try to makeCoffee with an index out of bounds.
	 * Program should return amtPaid.
	 */
	@Test
	public void testMakeCoffeeBadIndex() {
		coffeeMaker.addRecipe(recipe1);
		assertEquals(2, coffeeMaker.makeCoffee(45,2));
	}


	/**
	 * UC2: Add Recipe Tests
	 *
	 * Test the main flow of adding a valid recipe.
	 * Adding a valid recipe should complete successfully.
	 */
	@Test
	public void testAddRecipe() {
		assertTrue(coffeeMaker.addRecipe(recipe1));
	}

	/**
	 * Only a total of 3 recipes are allowed, attempt to enter a 4th.
	 * AddRecipe should return false on the 4th attempt.
	 */
	@Test
	public void testAddRecipe1() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		coffeeMaker.addRecipe(recipe3);
		assertFalse(coffeeMaker.addRecipe(recipe4));
	}

	/**
	 * Add recipe with a non int price value
	 * AddRecipe should throw RecipeException
	 */
	@Test(expected = RecipeException.class)
	public void testAddRecipe2() throws RecipeException{
		recipebad = new Recipe();
		recipebad.setName("Bad Latte");
		recipebad.setAmtChocolate("0");
		recipebad.setAmtCoffee("3");
		recipebad.setAmtMilk("3");
		recipebad.setAmtSugar("1");
		recipebad.setPrice("this better throw an exception!");
		coffeeMaker.addRecipe(recipebad);
	}

	/**
	 * Add recipe with invalid coffee, sugar, milk, chocolate values
	 * AddRecipe should throw RecipeException
	 */
	@Test(expected = RecipeException.class)
	public void testAddRecipe3() throws RecipeException{
		recipebad = new Recipe();
		recipebad.setName("Bad Latte");
		recipebad.setAmtChocolate("cookie");
		recipebad.setAmtCoffee("no coffee");
		recipebad.setAmtMilk("3.0022");
		recipebad.setAmtSugar("3.42");
		recipebad.setPrice("400");
		coffeeMaker.addRecipe(recipebad);
	}

	/**
	 * Add a recipe with negative price and units.
	 * Should throw RecipeException
	 */
	@Test(expected = RecipeException.class)
	public void testAddRecipe4() throws RecipeException{
		recipebad = new Recipe();
		recipebad.setName("Bad Latte");
		recipebad.setAmtChocolate("-2");
		recipebad.setAmtCoffee("-4");
		recipebad.setAmtMilk("-2");
		recipebad.setAmtSugar("-5");
		recipebad.setPrice("-400");
		coffeeMaker.addRecipe(recipebad);
	}

	/**
	 * Add a recipe that already exists.
	 * AddRecipe should return false.
	 */
	@Test
	public void testAddRecipe5() {
		coffeeMaker.addRecipe(recipe1);
		assertFalse(coffeeMaker.addRecipe(recipe1));
	}

	/**
	 * UC3: Delete Recipe Tests
	 *
	 * Add a recipe then delete it. Make sure it is deleted afterwards.
	 * Should return recipe name of deleted.
	 */
	@Test
	public void testDeleteRecipe() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.addRecipe(recipe2);
		Recipe [] recipes = coffeeMaker.getRecipes();
		assert("Coffee" == coffeeMaker.deleteRecipe(0));
		assertNull(recipes[0]);
	}

	/**
	 * Try to delete a recipe with index out of bounds.
	 * Should return to main menu with message.
	 */
	@Test
	public void testDeleteRecipe1() {
		assertNull(coffeeMaker.deleteRecipe(3));
	}

	/**
	 * Try to delete a recipe with a letter in input.
	 * Should return to main menu with message.
	 */
	@Test
	public void testDeleteRecipe2() {
		assertNull(coffeeMaker.deleteRecipe('a'));
	}

	/**
	 * Try to delete an empty recipe.
	 * Should return null.
	 */
	@Test
	public void testDeleteRecipe3() {
		assertNull(coffeeMaker.deleteRecipe(0));
	}

	/**
	 * UC4: Edit Recipe Testing
	 *
	 * Edit a valid recipe.
	 * Should return old recipe name.
	 */
	@Test
	public void testEditRecipe() {
		coffeeMaker.addRecipe(recipe1);
		assert("Coffee" == coffeeMaker.editRecipe(0, recipe2));
	}

	/**
	 * Try to input recipe index as a char.
	 */
	@Test
	public void testEditRecipe1() {
		coffeeMaker.addRecipe(recipe1);
		assertFalse("Coffee".equals(coffeeMaker.editRecipe('a', recipe2)));
	}

	/**
	 * Replace a valid recipe with a bad one. Price of bad one is not a number.
	 */
	@Test(expected = RecipeException.class)
	public void testEditRecipe2() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);
		recipebad = new Recipe();
		recipebad.setName("Bad Latte");
		recipebad.setAmtChocolate("2");
		recipebad.setAmtCoffee("4");
		recipebad.setAmtMilk("2");
		recipebad.setAmtSugar("5");
		recipebad.setPrice("addwad");
		coffeeMaker.editRecipe(0, recipebad);
		//assert("Coffee".equals(coffeeMaker.editRecipe(0, recipebad)));
	}

	/**
	 * Replace a valid recipe with a bad one. Non int units.
	 */
	@Test(expected = RecipeException.class)
	public void testEditRecipe3() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);
		recipebad = new Recipe();
		recipebad.setName("Bad Latte");
		recipebad.setAmtChocolate("2.65");
		recipebad.setAmtCoffee("4.1");
		recipebad.setAmtMilk("2.345");
		recipebad.setAmtSugar("0.4");
		recipebad.setPrice("20");
		coffeeMaker.editRecipe(0, recipebad);
		//assert("Coffee".equals(coffeeMaker.editRecipe(0, recipebad)));
	}

	/**
	 * Replace a valid recipe with a bad one. All negative params
	 */
	@Test(expected = RecipeException.class)
	public void testEditRecipe4() throws RecipeException {
		coffeeMaker.addRecipe(recipe1);
		recipebad = new Recipe();
		recipebad.setName("Bad Latte");
		recipebad.setAmtChocolate("-2");
		recipebad.setAmtCoffee("-4");
		recipebad.setAmtMilk("-2");
		recipebad.setAmtSugar("-5");
		recipebad.setPrice("-400");
		coffeeMaker.addRecipe(recipebad);
		coffeeMaker.editRecipe(0, recipebad);
		assert("Coffee".equals(coffeeMaker.editRecipe(0, recipebad)));
	}

	/**
	 * Select an empty index to edit.
	 * Should return null.
	 */
	@Test
	public void testEditRecipe5() {
		assertNull(coffeeMaker.editRecipe(0, recipe1));
	}

	/**
	 * UC5: Add Inventory Testing
	 *
	 * Already tested near top of file by default. More tests below.
	 *
	 * Test adding negative units to inventory for each parameter individually.
	 * Should throw exception.
	 */
	@Test(expected = InventoryException.class)
	public void testAddInventoryNegCoffee() throws InventoryException {
		coffeeMaker.addInventory("-4", "1", "1", "3");
	}

	@Test(expected = InventoryException.class)
	public void testAddInventoryNegMilk() throws InventoryException {
		coffeeMaker.addInventory("4", "-1", "1", "3");
	}

	@Test(expected = InventoryException.class)
	public void testAddInventoryNegSugar() throws InventoryException {
		coffeeMaker.addInventory("4", "1", "-1", "3");
	}

	@Test(expected = InventoryException.class)
	public void testAddInventoryNegChocolate() throws InventoryException {
		coffeeMaker.addInventory("4", "1", "1", "-3");
	}

	/**
	 * UC6: Check Inventory Test
	 *
	 * Run checkInventory
	 */
	@Test
	public void testCheckInventory() {
		assert(currInventory.equals(coffeeMaker.checkInventory()));
	}

	/**
	 * UC7: Make Coffee Testing
	 *
	 * Tested above, add more if needed.
	 *
	 * Test if inventory correctly reduces as a coffee is made.
	 * Recipe1:
	 * Choc - 0
	 * Coffee - 3
	 * Milk - 1
	 * Sugar - 1
	 * Price - 50
	 */
	@Test
	public void testReduceInventory() {
		coffeeMaker.addRecipe(recipe1);
		coffeeMaker.makeCoffee(0, 50);
		System.out.println("Inventory before makeCoffee: \n" + currInventory);
		System.out.println("Inventory after makeCoffee: \n" + coffeeMaker.checkInventory());

		assertEquals("Coffee: 12\n" +
				"Milk: 14\n" +
				"Sugar: 14\n" +
				"Chocolate: 15\n", coffeeMaker.checkInventory());
	}





}

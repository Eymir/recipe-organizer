package net.bilnoski.recipebook.model;

public class Ingredient
{
   public int ordinal;
   public double qty;
   public String kind;
   public String unit;
   public String preparation;
   
   @Override
   public String toString()
   {
      return "Ingredient["+kind+"]";
   }
}

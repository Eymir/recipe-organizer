package net.bilnoski.recipebook.model;

import java.util.List;

import net.bilnoski.recipebook.properties.PropertyMap;

public class Recipe
{
   public String title;
   public List<Ingredient> ingredients;
   
   //TODO: convert to something readable in chunks for steps
   public String instructions;
   
   public String notes;
   
   public PropertyMap props;
   
   public Recipe()
   {
      props = new PropertyMap();
   }
   
   @Override
   public String toString()
   {
      return "Recipe["+title+"]";
   }
}

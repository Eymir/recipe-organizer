package net.bilnoski.recipebook.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Cookbook
{
   protected Map<String,Recipe> recipes;
   
   public Cookbook()
   {
      recipes = new HashMap<String, Recipe>();
   }
   
   public Recipe get(String title)
   {
      return recipes.get(title);
   }
   
   public void add(Recipe recipe)
   {
      recipes.put(recipe.title, recipe);
   }
   
   public Collection<Recipe> getRecipes()
   {
      return new ArrayList<Recipe>(recipes.values());
   }
   
   public void clear()
   {
      recipes.clear();
   }
}
